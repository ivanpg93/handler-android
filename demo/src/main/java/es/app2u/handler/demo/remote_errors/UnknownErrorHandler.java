package es.app2u.handler.demo.remote_errors;

import org.json.JSONObject;

import es.app2u.beat.handler.ConnectionHandler;
import es.app2u.beat.handler.Credentials;
import es.app2u.beat.handler.JsonHandler;
import es.app2u.beat.handler.Parameters;
import es.app2u.handler.demo.logger.ArrayRequestLogger;
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
