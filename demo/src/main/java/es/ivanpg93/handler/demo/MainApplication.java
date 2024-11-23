package es.ivanpg93.handler.demo;

import android.app.Application;

import es.ivanpg93.handler.ConnectionConfiguration;
import es.ivanpg93.logging.HttpLogger;
import es.ivanpg93.logging.repository.FileLogRepository;
import es.ivanpg93.logging.repository.LogCatLogRepository;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //This log will apply for all connections
        FileLogRepository fileWriter = new FileLogRepository(getApplicationContext());
        LogCatLogRepository logCatWriter = new LogCatLogRepository();
        ConnectionConfiguration.getInstance().addLogger(new HttpLogger(fileWriter, logCatWriter));
    }

}