package es.ivanpg93.handlers.handler;

import android.util.Log;

import java.io.File;
import java.io.IOException;

import es.ivanpg93.handlers.error.ResponseError;
import io.reactivex.Single;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * This Handler is used to send and download files.
 * Downloaded files will be stored in the destinationPath with the fileName
 */
public class FileHandler extends ConnectionHandler<File> {

    private static final MediaType CONTENT_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    // Destination directory path where file will be stored
    private String destination;
    private String fileName;

    public static class FileParameters extends Parameters {

        private String destination;
        private String fileName;

        public void setDestination(String destinationPath) {
            this.destination = destinationPath;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }
    }

    @Override
    public Single<File> execute(Parameters parameters) {

        // Must be FileParameters because need destinationPath and fileName
        if (!(parameters instanceof FileParameters)) {
            throw new IllegalArgumentException("FileHandler requires FileParameters");
        }

        destination = ((FileParameters) parameters).destination;
        fileName = ((FileParameters) parameters).fileName;

        return super.execute(parameters);
    }

    @Override
    public RequestBody getRequestBody(Object data) {
        if (data instanceof File) {
           return RequestBody.create(CONTENT_TYPE_MARKDOWN, (File)data);
        } else {
            throw new UnsupportedOperationException("FileHandler does not allow non File content type ");
        }
    }

    @Override
    public File processResponseSuccess(final Call call, final Response response) throws ResponseError {

        ResponseBody body = response.body();

        // Ensure has response data
        if (body == null) {
            throw new ResponseError.Builder().message("Empty response body").build();
        }

        // Create file directory if not exists
        File directory = new File(destination);
        if (!directory.exists()) {
            boolean success = directory.mkdirs();
            if (!success) {
                throw new ResponseError.Builder().message("Failed to create directory: " + destination).build();
            }
            Log.i(FileHandler.class.getSimpleName(), "Directory created: " + directory.getName());
        }

        // Write file
        File file = new File(directory, fileName);
        BufferedSource source = body.source();
        try (BufferedSink sink = Okio.buffer(Okio.sink(file))) {
            sink.writeAll(source);
        } catch (IOException e) {
            throw new ResponseError.Builder().message("Failed to write file: " + file.getAbsolutePath()).build();
        }
        body.close();

        return file;
    }

}
