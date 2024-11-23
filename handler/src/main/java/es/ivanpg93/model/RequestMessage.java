package es.ivanpg93.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RequestMessage {

    @NonNull private final String method;
    @NonNull private final HttpUrl url;
    @Nullable private final Connection connection;
    @Nullable private final RequestBody requestBody;
    private final Headers headers;
    @Nullable private final Response response;
    private final Long ms;
    @Nullable private final String responseBody;
    private Exception interceptedError;

    public RequestMessage(Builder builder) {
        this.method = builder.method;
        this.url = builder.url;
        this.connection = builder.connection;
        this.requestBody = builder.requestBody;
        this.headers = builder.headers;
        this.response = builder.response;
        this.ms = builder.ms;
        this.responseBody = builder.responseBody;
    }

    @NonNull
    public String getMethod() {
        return method;
    }

    @NonNull
    public HttpUrl getUrl() {
        return url;
    }

    @Nullable
    public Connection getConnection() {
        return connection;
    }

    @Nullable
    public RequestBody getRequestBody() {
        return requestBody;
    }

    public Headers getHeaders() {
        return headers;
    }

    @Nullable
    public Response getResponse() {
        return response;
    }

    public Long getMs() {
        return ms;
    }

    @Nullable
    public String getResponseBody() {
        return responseBody;
    }

    public Exception getInterceptedError() {
        return interceptedError;
    }

    public void setInterceptedError(Exception interceptedError) {
        this.interceptedError = interceptedError;
    }

    public static class Builder {
        private String method;
        private HttpUrl url;
        private Connection connection;
        private RequestBody requestBody;
        private Headers headers;
        private Response response;
        private Long ms;
        private String responseBody;

        public RequestMessage build() {
            return new RequestMessage(this);
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder url(HttpUrl url) {
            this.url = url;
            return this;
        }

        public Builder connection(Connection connection) {
            this.connection = connection;
            return this;
        }

        public Builder requestBody(RequestBody requestBody) {
            this.requestBody = requestBody;
            return this;
        }

        public Builder headers(Headers headers) {
            this.headers = headers;
            return this;
        }

        public Builder response(Response response) {
            this.response = response;
            return this;
        }

        public Builder ms(Long ms) {
            this.ms = ms;
            return this;
        }

        public Builder responseBody(String responseBody) {
            this.responseBody = responseBody;
            return this;
        }
    }

}
