package es.ivanpg93.demo.remote;

import org.json.JSONObject;

import java.util.Locale;

import es.ivanpg93.handlers.handler.ConnectionHandler;
import es.ivanpg93.handlers.handler.Credentials;
import es.ivanpg93.handlers.handler.JsonHandler;
import es.ivanpg93.handlers.handler.Parameters;
import es.ivanpg93.demo.logger.ArrayRequestLogger;
import io.reactivex.Single;


public class GetJSONWS {

    private static final String URL = "https://jsonplaceholder.typicode.com/posts";

    private final JsonHandler handler = new JsonHandler();

    public Single<JSONObject> executeGetObject(int objectId) {
        Parameters parameters = new Parameters();
        parameters.setMethod(ConnectionHandler.HttpMethod.GET);
        String url = String.format(Locale.getDefault(), "%s/%d", URL, objectId);
        parameters.setUrl(url);
        parameters.setAuthorization(() -> Credentials.basic("username", "password"));

        // Add custom logger
        parameters.addLogger(new ArrayRequestLogger());
        return handler.execute(parameters);
    }

}
