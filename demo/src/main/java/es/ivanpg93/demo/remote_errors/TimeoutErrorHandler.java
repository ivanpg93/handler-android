package es.ivanpg93.demo.remote_errors;

import org.json.JSONObject;

import es.ivanpg93.handlers.handler.ConnectionHandler;
import es.ivanpg93.handlers.handler.Credentials;
import es.ivanpg93.handlers.handler.JsonHandler;
import es.ivanpg93.handlers.handler.Parameters;
import es.ivanpg93.demo.logger.ArrayRequestLogger;
import io.reactivex.Single;

public class TimeoutErrorHandler {

    private static final String URL = "http://192.168.1.75:8000/webservice/v2/inventory/";

    private final JsonHandler handler = new JsonHandler();

    public Single<JSONObject> execute() {
        Parameters parameters = new Parameters();
        parameters.setMethod(ConnectionHandler.HttpMethod.POST);
        parameters.setUrl(URL);
        parameters.setAuthorization(() -> Credentials.basic("test", "1234"));

        // Important to force a timeout error
        parameters.setConnectionTimeout(1);

        parameters.setData(new JSONObject());

        // Add custom logger
        parameters.addLogger(new ArrayRequestLogger());
        return handler.execute(parameters);
    }

}
