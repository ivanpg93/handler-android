package es.ivanpg93.logging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.ivanpg93.logging.repository.LogRepository;
import es.ivanpg93.model.RequestMessage;
import es.ivanpg93.storage.StorageError;
import okhttp3.Connection;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class HttpLogger implements Logger {

    private static final String DEFAULT_EMPTY_FIELD = "-";
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

    private final LogRepository[] writers;

    public HttpLogger(LogRepository... writers) {
        this.writers = writers;
    }

    public void log(@NonNull RequestMessage requestMessage) {

        // Get text to be logged
        String requestLog = getExtendedRequest(requestMessage);

        // Log text for each writer
        for (LogRepository writer : writers) {
            try {
                writer.write(requestLog);
            } catch (StorageError e) {
                e.printStackTrace();
            }
        }

    }

    protected String getFormattedDate(@NonNull Date date) {
        return new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(date);
    }

    protected String getSimpleRequest(@NonNull RequestMessage requestMessage) {
        /*
        Using NCSA Common log format
         */

        // Request info
        String date = getFormattedDate(new Date());
        String method = requestMessage.getMethod();
        String url = requestMessage.getUrl().toString();
        String protocol = getProtocol(requestMessage);

        // Response info
        long contentLength = 0L;
        String responseCode = DEFAULT_EMPTY_FIELD;
        Response response = requestMessage.getResponse();
        if (response != null) {
            responseCode = Integer.toString(response.code());
            contentLength = getContentLength(response);
        }
        return String.format(Locale.getDefault(), "%s %s %s %s %s %d", date, method, url, protocol,
                responseCode, contentLength);
    }

    protected String getExtendedRequest(@NonNull RequestMessage requestMessage) {

        // Using simple log format with request and response bodies in a new line
        String request = getSimpleRequest(requestMessage);
        String requestLog = String.format(Locale.getDefault() , "%s%n", request);

        // Add request body if any
        String requestBody = getRequestBody(requestMessage);
        if (requestBody != null) {
            requestLog += String.format(Locale.getDefault() , "RQ:%n%s%n", requestBody);
        }

        // Add response body if any
        String responseBody = requestMessage.getResponseBody();
        if (responseBody != null) {
            requestLog += String.format(Locale.getDefault() , "RS:%n%s%n", responseBody);
        }

        return requestLog;
    }

    protected long getContentLength(@NonNull Response response) {
        long contentLength = 0L;
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            contentLength = responseBody.contentLength();
        }
        return contentLength;
    }

    protected String getProtocol(RequestMessage requestMessage) {
        Connection connection = requestMessage.getConnection();
        String protocol = DEFAULT_EMPTY_FIELD;
        if (connection != null) {
            protocol = connection.protocol().toString();
        }
        return protocol;
    }

    protected @Nullable String getRequestBody(RequestMessage requestMessage) {

        // Ensure that body is not empty
        RequestBody requestBody = requestMessage.getRequestBody();
        if (requestBody == null) {
            return null;
        }

        String body;
        Buffer buffer = new Buffer();
        try {
            requestBody.writeTo(buffer);
            Charset charset = StandardCharsets.UTF_8;
            MediaType contentType = requestBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(StandardCharsets.UTF_8);
                if (charset == null) {
                    charset = StandardCharsets.UTF_8;
                }
            }
            body = buffer.readString(charset);
        } catch (IOException e) {
            body = String.format("Unknown body: %s", e.getMessage());
        }
        return body;
    }

}
