package es.app2u.handler.demo.remote;

import org.json.JSONArray;
import org.json.JSONObject;

import es.app2u.beat.handler.ConnectionHandler;
import es.app2u.beat.handler.Credentials;
import es.app2u.beat.handler.JsonHandler;
import es.app2u.beat.handler.Parameters;
import es.app2u.handler.demo.logger.ArrayRequestLogger;
import io.reactivex.Single;

public class PutJSONWS {

    private static final String URL = "https://www.empresaiformacio.org/sBidTest/rest/app/notificacio/5?acceptada=true";

    private final JsonHandler handler = new JsonHandler();

    public Single<JSONObject> execute() {
        Parameters parameters = new Parameters();
        parameters.setMethod(ConnectionHandler.HttpMethod.PUT);
        parameters.setUrl(URL);
        parameters.setAuthorization(() -> Credentials.basic("mtaberner", "123456"));
        JSONArray body = new JSONArray();

        try {
            body.put(new JSONObject()
                    .put("codExtra", "1")
                    .put("valor", "prueba@mail.com")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

        parameters.setData(body);

        // Add custom logger
        parameters.addLogger(new ArrayRequestLogger());
        return handler.execute(parameters);
    }

}
