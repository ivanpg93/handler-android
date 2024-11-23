package es.ivanpg93.demo.remote_errors;

import org.json.JSONObject;

import es.ivanpg93.handlers.handler.ConnectionHandler;
import es.ivanpg93.handlers.handler.Credentials;
import es.ivanpg93.handlers.handler.JsonHandler;
import es.ivanpg93.handlers.handler.Parameters;
import es.ivanpg93.demo.logger.ArrayRequestLogger;
import io.reactivex.Single;

public class UnknownErrorHandler {

    private static final String UNREACHABLE_URL = "http://unreachable.url";

    private final JsonHandler handler = new JsonHandler();

    public Single<JSONObject> execute() {
        Parameters parameters = new Parameters();
        parameters.setMethod(ConnectionHandler.HttpMethod.GET);
        parameters.setUrl(UNREACHABLE_URL);
        parameters.setAuthorization(() -> Credentials.basic("username", "password"));

        // Add custom logger
        parameters.addLogger(new ArrayRequestLogger());

        return handler.execute(parameters);
    }

}
