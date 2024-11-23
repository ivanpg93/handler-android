package es.ivanpg93.demo.logger;

import android.util.Log;

import androidx.annotation.NonNull;

import es.ivanpg93.handlers.logging.Logger;
import es.ivanpg93.handlers.model.RequestMessage;

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
