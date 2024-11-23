package es.ivanpg93.handlers.logging.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.ivanpg93.handlers.storage.FileStorageWriter;
import es.ivanpg93.handlers.storage.StorageError;
import es.ivanpg93.handlers.storage.Zipper;

public class FileLogRepository implements LogRepository {

    private static final long K = 1024;

    private static final String DEFAULT_DIRECTORY = "log";
    private static final String DEFAULT_FILE_NAME = "http.txt";
    private static final int DEFAULT_MAX_SIZE = 25;

    public static final String ZIP_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    private final Context context;

    public FileLogRepository(@NonNull Context context) {
        this.context = context;
    }

    @Override
    public void write(String line) throws StorageError {
        File dir = this.getLogDir();

        // Check if directory writeable
        if (!dir.canWrite()) {
            throw new StorageError(StorageError.Cause.NOT_WRITABLE);
        }

        // Write into a file
        File file = new File(dir, getFileName());
        FileStorageWriter.write(file, line);
        if (maxSizeReached(file)) {
            this.zipFile(file);
        }
    }

    private void zipFile(File file) {
        Zipper.zip(file.getAbsolutePath(), file.getParent() + "/" + createLogZipFileName());

        try {
            PrintWriter writer = new PrintWriter(file);
            writer.close();
        } catch (FileNotFoundException var3) {
            Log.e("StorageManager", "error en borrar contenido previo");
        }

    }

    private File getLogDir() {
        return context.getExternalFilesDir(getDirName());
    }

    protected String getDirName() {
        return DEFAULT_DIRECTORY;
    }

    protected String getFileName() {
        return DEFAULT_FILE_NAME;
    }

    protected int getMaxSize() {
        // In MB
        return DEFAULT_MAX_SIZE;
    }

    private String createLogZipFileName() {
        return new SimpleDateFormat(ZIP_DATE_FORMAT, Locale.getDefault()).format(new Date()) + "_log.zip";
    }

    private boolean maxSizeReached(File file) {
        long sizeInBytes = file.length();
        // Transform to MB
        long sizeInMb = sizeInBytes / (K * K);
        return sizeInMb > getMaxSize();
    }

}