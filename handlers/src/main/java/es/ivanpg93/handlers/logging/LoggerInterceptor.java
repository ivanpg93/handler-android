package es.ivanpg93.handlers.logging;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import es.ivanpg93.handlers.model.RequestMessage;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class LoggerInterceptor implements Interceptor {

    private final Set<Logger> loggerList = new HashSet<>();

    public void addLoggers(Set<Logger> loggers) {
        this.loggerList.addAll(loggers);
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {

        RequestMessage.Builder builder = new RequestMessage.Builder();

        try {
            Request request = chain.request();

            Connection connection = chain.connection();

            builder
                .method(request.method())
                .connection(connection)
                .requestBody(request.body())
                .url(request.url());

            Response response;
            long requestDuration;
            try {
                long timeBeforeRequest = System.nanoTime();
                response = chain.proceed(request);
                requestDuration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - timeBeforeRequest);
            } catch (Exception e) {
                catchError(e, builder.build());
                throw e;
            }

            // Add response
            builder.ms(requestDuration);
            builder.response(response);

            ResponseBody responseBody = response.body();

            // Add responseBody
            if (responseBody != null) {
                BufferedSource source = responseBody.source();
                // Buffer the entire body.
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.buffer();
                String responseBuffer = buffer.clone().readString(StandardCharsets.UTF_8);
                builder.responseBody(responseBuffer);
            }

            requestIntercepted(builder.build());

            return response;

        } catch (Exception e) {
            //Avoid that a logger crash, crash the app.
            catchError(e, builder.build());
            throw e;
        }
    }

    // Sonar thinks that Logger class must be a static class
    @SuppressWarnings("squid:S1312")
    private void requestIntercepted(@NonNull RequestMessage message) {
        for (Logger logger: loggerList) {
            logger.log(message);
        }
    }

    private void catchError(Exception e, RequestMessage message) {
        message.setInterceptedError(e);
        requestIntercepted(message);
    }

}
