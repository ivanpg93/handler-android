package es.ivanpg93.handlers.handler;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import es.ivanpg93.handlers.error.ConnectError;
import es.ivanpg93.handlers.error.ConnectionError;
import es.ivanpg93.handlers.error.ResponseError;
import es.ivanpg93.handlers.error.TimeoutError;
import es.ivanpg93.handlers.error.UnknownHostError;
import es.ivanpg93.handlers.logging.LoggerInterceptor;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.subjects.PublishSubject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public abstract class ConnectionHandler<T> {

    private static final String TAG = ConnectionHandler.class.getSimpleName();

    // HTTP Client can be reused
    private OkHttpClient client;

    private final PublishSubject<ProgressResponseBody.Progress> progressListener = PublishSubject.create();

    public interface Authorization {
        String getAuthorizationValue();
    }

    public @interface HttpMethod {
        String GET = "GET";
        String POST = "POST";
        String PUT = "PUT";
        String PATCH = "PATCH";
        String DELETE = "DELETE";
        String HEAD = "HEAD";
        String OPTIONS = "OPTIONS";
    }

    /**
     * Each Handler must implement this method to process the response and return a useful object
     * @param call
     * @param response
     * @return
     * @throws ResponseError
     */
    public abstract T processResponseSuccess(Call call, Response response) throws ResponseError;

    public abstract RequestBody getRequestBody(Object data);

    /**
     * Returns a listener to allow receive response updates
     * IMPORTANT: parameter notifyProgress must be set to true @see setNotifyProgress(boolean notifyProgress)
     * @return Objects that contains content size and current progress
     */
    public PublishSubject<ProgressResponseBody.Progress> getProgressListener() {
        return progressListener;
    }

    protected ResponseError.Builder processResponseError(Call call, Response response) {
        return new ResponseError.Builder()
                .response(response)
                .errorCode(response.code())
                .call(call);
    }

    // Suppress "Unused method parameters should be removed.
    // "Call" parameter may be useful in the future if the method is overridden in a subclass
    @SuppressWarnings("squid:S1186")
    protected ConnectionError processConnectionFailure(Call call, IOException e) {
        if (e instanceof SocketTimeoutException) {
            return new TimeoutError(e);
        }

        if (e instanceof UnknownHostException) {
            return new UnknownHostError(e);
        }

        if (e instanceof ConnectException) {
            return new ConnectError(e);
        }

        return new ConnectionError(e);
    }

    public Single<T> execute(Parameters parameters) {
        return Single.create(emitter -> {

            // Create HTTP request and client
            Request request = getRequestBuilder(parameters).build();
            if (client == null) {
                client = createClient(parameters).build();
            }

            // If there is any middleware defined, process the request
            RequestMiddleware middleware = ConnectionConfiguration.getInstance().getRequestMiddleware();
            if (middleware != null) {
                request = middleware.processRequest(request);
            }

            // Execute request
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull final Call call, @NonNull final IOException e) {
                    // Error connection to the server
                    manageOnFailureRequest(call, e, emitter);
                }

                @Override
                public void onResponse(@NonNull final Call call, @NonNull final Response response) {
                    manageOnSucceedRequest(call, response, emitter);
                }
            });
        });
    }

    private void manageOnFailureRequest(Call call, IOException e, SingleEmitter<T> emitter) {
        // Response Error
        ConnectionError exception = processConnectionFailure(call, e);
        if (!emitter.isDisposed()) {
            emitter.onError(exception);
        }
    }

    private void manageOnSucceedRequest(Call call, Response response, SingleEmitter<T> emitter) {
        // Connection completed, check if is 200
        if (response.isSuccessful()) {
            try {
                // Connection OK
                T responseObject = processResponseSuccess(call, response);
                if (!emitter.isDisposed()) {
                    emitter.onSuccess(responseObject);
                }

            } catch (Exception e) {
                // Exception processing response
                if (!emitter.isDisposed()) {
                    emitter.onError(e);
                }
            }
        } else {
            // Response Error
            ResponseError error = processResponseError(call, response).build();
            if (!emitter.isDisposed()) {
                emitter.onError(error);
            }
        }
    }

    private Request.Builder getRequestBuilder(Parameters parameters) {

        // Create the URL
        Uri.Builder builder = Uri.parse(parameters.getUrl()).buildUpon();

        // Add query params
        for (Map.Entry<String, String> entry : parameters.getQueryParams().entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }

        // Create the HTTP Request
        String url = builder.build().toString();
        Request.Builder requestBuilder = new Request.Builder().url(url);

        // Create the header with all param header values.
        for (Map.Entry<String, String> entry : parameters.getHeaderValues().entrySet()) {
            requestBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        // Set Authorization header if any
        Authorization auth = parameters.getAuthorization();
        if (auth != null && auth.getAuthorizationValue() != null) {
            requestBuilder.header("Authorization", auth.getAuthorizationValue());
        }

        // Set body if any
        RequestBody body = null;
        Object data = parameters.getData();
        if (data != null) {
            body = getRequestBody(data);
        }

        String method = parameters.getMethod();

        // Log request
        Log.d(TAG, String.format("%s %s", method, url));
        if (body != null) {
            // Print data and not body because body is an RequestBody and has not readable representation
            Log.d(TAG, data.toString());
        }

        return requestBuilder.method(method, body);
    }

    private void initInterceptor(OkHttpClient.Builder builder, Parameters parameters) {
        LoggerInterceptor interceptor = new LoggerInterceptor();
        interceptor.addLoggers(ConnectionConfiguration.getInstance().getLoggers());
        interceptor.addLoggers(parameters.getLoggers());
        builder.addInterceptor(interceptor);
    }

    /**
     * Creates an OkHttpClient with custom parameters
     * @param parameters
     * @return
     */
    private OkHttpClient.Builder createClient(Parameters parameters) {

        OkHttpClient.Builder clientBuilder = new OkHttpClient().newBuilder();

        // Set up timeouts
        clientBuilder.connectTimeout(parameters.getConnectionTimeout(), TimeUnit.SECONDS);
        clientBuilder.writeTimeout(parameters.getWriteTimeout(), TimeUnit.SECONDS);
        clientBuilder.readTimeout(parameters.getReadTimeout(), TimeUnit.SECONDS);

        initInterceptor(clientBuilder, parameters);

        // Set up connection certificates
        if (parameters.getTrustAllCertificates()) {
            setUpTrustAllCertificates(clientBuilder);
        }

        // Set progress interceptor if necessary
        if (parameters.notifyProgress()) {
            addProgressListener(clientBuilder);
        }

        return clientBuilder;
    }

    /**
     * This methods indicates HttpClient to trust in all certificates involved in the request.
     * WARNING: Remember that it is highly discouraged to return always true in the hostnameVerifier
     * to avoid risk of man in the middle attacks.
     * @param builder: The client builder where trusting will be configured
     */
    private void setUpTrustAllCertificates(@NonNull OkHttpClient.Builder builder) {
        final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        // Do nothing
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        // Do nothing
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
        };

        final SSLContext sslContext;
        try {
            // Install the all-trusting trust manager
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);
        } catch (NoSuchAlgorithmException | KeyManagementException ignored) {
            // Do nothing, trust all certificates will not be performed
        }

    }

    /**
     * Allows track response progress
     * @param builder
     */
    private void addProgressListener(@NonNull OkHttpClient.Builder builder) {
        builder.addNetworkInterceptor(chain -> {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
        });
    }

}

