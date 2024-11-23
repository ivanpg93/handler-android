package es.ivanpg93.handlers.logging.repository;

import android.util.Log;

public class LogCatLogRepository implements LogRepository {

    @Override
    public void write(String string) {
        Log.d(getClass().getSimpleName(), string);
    }

}
