package es.ivanpg93.demo.remote;

import android.content.Context;

import java.io.File;

import es.ivanpg93.handlers.handler.ConnectionHandler;
import es.ivanpg93.handlers.handler.FileHandler;
import es.ivanpg93.handlers.handler.ProgressResponseBody;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

public class GetFileWS {

    private static final String URL = "https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_30mb.mp4";

    private final FileHandler handler = new FileHandler();

    public PublishSubject<ProgressResponseBody.Progress> getProgressListener() {
        return handler.getProgressListener();
    }

    public Single<File> execute(Context context) {

        String dirPath = context != null ? context.getFilesDir().getAbsolutePath() + File.separator + "test" : null;

        FileHandler.FileParameters parameters = new FileHandler.FileParameters();
        parameters.setMethod(ConnectionHandler.HttpMethod.GET);
        parameters.setUrl(URL);
        parameters.setDestination(dirPath);
        parameters.setFileName("video.mp4");
        parameters.setNotifyProgress(true);

        return handler.execute(parameters);
    }

}
