package es.app2u.handler.demo.remote;

import org.json.JSONObject;

import java.util.Locale;

import es.app2u.beat.handler.ConnectionHandler;
import es.app2u.beat.handler.Credentials;
import es.app2u.beat.handler.JsonHandler;
import es.app2u.beat.handler.Parameters;
import es.app2u.handler.demo.logger.ArrayRequestLogger;
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
