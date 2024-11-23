package es.ivanpg93.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

import java.io.IOException;

import es.ivanpg93.error.ResponseError;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;

public class XMLHandler extends ConnectionHandler<String> {

    private static final MediaType CONTENT_TYPE_XML = MediaType.parse("text/xml");

    /**
     * Convert JSON to XML as String
     * @param json: JsonObject from gson library
     * @param body: accumulated xml content as String
     */
    public static String JSONtoXML(JsonObject json, String body) {

        for (String key : json.keySet()) {

            JsonElement value = json.get(key);

            //Check value type
            if (value instanceof JsonArray) {

                //Array
                JsonArray array = (JsonArray) value;
                for (int i = 0; i < array.size(); i++) {
                    JsonObject object = array.get(i).getAsJsonObject();
                    String valueBody = JSONtoXML(object, "");
                    body += "<" + key + ">" + valueBody + "</" + key + ">";
                }
            } else if (value instanceof JsonObject) {

                //JSON
                String valueBody = JSONtoXML((JsonObject) value, "");
                body += "<" + key + ">" + valueBody + "</" + key + ">";
            } else {
                if (value instanceof JsonNull || value == null) {
                    body += "<" + key + "></" + key + ">";
                } else {
                    //String
                    body += "<" + key + ">" + value.getAsString() + "</" + key + ">";
                }
            }
        }
        return body;
    }

    @Override
    public RequestBody getRequestBody(Object data) {
        String bodyString = data.toString();
        return RequestBody.create(CONTENT_TYPE_XML, bodyString);
    }

    @Override
    public String processResponseSuccess(Call call, Response response) throws ResponseError {
        try {
            return response.body().string();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            throw new ResponseError.Builder().exception(e).build();
        }
    }

}
