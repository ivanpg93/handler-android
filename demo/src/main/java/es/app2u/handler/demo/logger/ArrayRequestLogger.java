package es.app2u.handler.demo.logger;


import android.util.Log;

import androidx.annotation.NonNull;

import es.app2u.beat.logging.Logger;
import es.app2u.beat.model.RequestMessage;

public class ArrayRequestLogger implements Logger {

    @Override
    public void log(@NonNull RequestMessage message) {
        // Simple custom log cat example
        String log = getCustomLog(message);
        Log.d("CUSTOM LOG", log);
    }


    private String getCustomLog(@NonNull RequestMessage requestMessage) {
        // Only log request method
        return "REQUEST " + requestMessage.getMethod();
    }
}
