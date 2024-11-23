package es.ivanpg93.handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import es.ivanpg93.error.ResponseError;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class JsonHandler extends ConnectionHandler<JSONObject> {

    public static final String JSON_KEY_DATA = "data";

    private static final MediaType CONTENT_TYPE_JSON = MediaType.parse("application/json");

    protected JSONObject getResponseBody(Response response) throws IOException, JSONException {

        // if there is no body, return empty JsonObject
        ResponseBody responseBody = response.body();
        if (responseBody == null) {
            return new JSONObject();
        }

        // Read response as string and check ifs a json object or an array
        String responseString = responseBody.string();
        if (responseString.isEmpty()) {
            return new JSONObject();
        } else {
            Object parsedData = new JSONTokener(responseString).nextValue();
            if (parsedData instanceof JSONObject) {
                return (JSONObject) parsedData;
            } else {

                // Data is not a JSONObject, can be a JSONArray, String, Boolean, Integer, Long, Double or
                // JSONObject#NULL
                JSONObject json = new JSONObject();
                json.put(JSON_KEY_DATA, parsedData);
                return json;
            }
        }
    }

    @Override
    public RequestBody getRequestBody(Object data) {
        if (data instanceof JSONObject || data instanceof JSONArray) {
            String bodyString = data.toString();
            return RequestBody.create(CONTENT_TYPE_JSON, bodyString);
        } else {
            throw new UnsupportedOperationException("JsonHandler does not allow non JSON content type ");
        }
    }

    @Override
    public JSONObject processResponseSuccess(Call call, Response response) throws ResponseError {
        try {
            //Read response body as json
            return getResponseBody(response);
        } catch (IOException | JSONException e) {
            throw new ResponseError.Builder().exception(e).build();
        }
    }

    @Override
    public ResponseError.Builder processResponseError(Call call, final Response response) {
        ResponseError.Builder builder = super.processResponseError(call, response);

        // Add response body as json to the error
        try {
            JSONObject json = getResponseBody(response);
            builder.jsonObject(json);
        } catch (IOException | JSONException e) {
           e.printStackTrace();
        }

        return builder;
    }

}
