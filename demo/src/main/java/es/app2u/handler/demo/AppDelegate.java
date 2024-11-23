package es.app2u.handler.demo;

import android.app.Application;

import es.app2u.beat.handler.ConnectionConfiguration;
import es.app2u.beat.logging.HttpLogger;
import es.app2u.beat.logging.repository.FileLogRepository;
import es.app2u.beat.logging.repository.LogCatLogRepository;

public class AppDelegate extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //This log will apply for all connections
        FileLogRepository fileWriter = new FileLogRepository(getApplicationContext());
        LogCatLogRepository logCatWriter = new LogCatLogRepository();
        ConnectionConfiguration.getInstance().addLogger(new HttpLogger(fileWriter, logCatWriter));
    }

}