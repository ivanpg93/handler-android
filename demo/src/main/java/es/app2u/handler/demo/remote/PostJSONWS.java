package es.app2u.handler.demo.remote;

import org.json.JSONObject;

import java.util.Locale;

import es.app2u.beat.handler.ConnectionHandler;
import es.app2u.beat.handler.Credentials;
import es.app2u.beat.handler.JsonHandler;
import es.app2u.beat.handler.Parameters;
import es.app2u.handler.demo.logger.ArrayRequestLogger;
import io.reactivex.Single;


public class PostJSONWS {

    private static final String URL = "http://192.168.1.75:8000/webservice/v2/inventory/";

    private final JsonHandler handler = new JsonHandler();

    public Single<JSONObject> execute() {
        Parameters parameters = new Parameters();
        parameters.setMethod(ConnectionHandler.HttpMethod.POST);
        parameters.setUrl(URL);
        parameters.setAuthorization(() -> Credentials.basic("test", "1234"));
        parameters.setData(new JSONObject());

        // Add custom logger
        parameters.addLogger(new ArrayRequestLogger());
        return handler.execute(parameters);
    }

}
