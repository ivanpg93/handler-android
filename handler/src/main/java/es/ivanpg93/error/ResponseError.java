package es.ivanpg93.error;

import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Exception that represents an http response error
 */
public class ResponseError extends Exception {

    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_INTERNAL_SERVER = 500;

    protected final transient Call call;

    protected final transient Response response;

    // HTTP Status Code
    protected final int errorCode;

    // JSON response
    protected final transient JSONObject jsonObject;

    protected ResponseError(Builder builder) {
        super(builder.message, builder.cause);
        this.response = builder.response;
        this.errorCode = builder.errorCode;
        this.jsonObject = builder.jsonObject;
        this.call = builder.call;
    }

    public static ResponseError createUnauthorizedError() {
        return new Builder()
                .errorCode(CODE_UNAUTHORIZED)
                .build();
    }

    public static ResponseError createInternalError() {
        return new Builder()
                .errorCode(CODE_INTERNAL_SERVER)
                .build();
    }

    public Response getResponse() {
        return response;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public Call getCall() {
        return call;
    }

    public static class Builder {
        protected Response response;
        protected int errorCode;
        protected JSONObject jsonObject;
        protected Throwable cause;
        protected String message;
        protected Call call;

        public ResponseError build() {
            return new ResponseError(this);
        }

        public Builder exception(Exception exception) {
            this.message = exception.getMessage();
            this.cause = exception.getCause();
            return this;
        }

        public Builder response(Response response) {
            this.response = response;
            return this;
        }

        public Builder errorCode(int errorCode) {
            this.errorCode = errorCode;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder jsonObject(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
            return this;
        }

        public Builder call(Call call) {
            this.call = call;
            return this;
        }
    }

}
