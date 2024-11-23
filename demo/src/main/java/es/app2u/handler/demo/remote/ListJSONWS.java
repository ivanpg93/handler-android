package es.app2u.handler.demo.remote;

import org.json.JSONObject;

import es.app2u.beat.handler.ConnectionHandler;
import es.app2u.beat.handler.JsonHandler;
import es.app2u.beat.handler.Parameters;
import es.app2u.handler.demo.logger.ArrayRequestLogger;
import io.reactivex.Single;

public class ListJSONWS {

    private static final String URL = "https://jsonplaceholder.typicode.com/posts";

    private final JsonHandler handler = new JsonHandler();

    public Single<JSONObject> executeGetArray() {
        Parameters parameters = new Parameters();
        parameters.setMethod(ConnectionHandler.HttpMethod.GET);
        parameters.setUrl(URL);
        parameters.addQueryParams("warehouse", "05");

        // Add custom logger
        parameters.addLogger(new ArrayRequestLogger());

        return handler.execute(parameters);
    }
}
