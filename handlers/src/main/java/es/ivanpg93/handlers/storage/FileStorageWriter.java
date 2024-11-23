package es.ivanpg93.handlers.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileStorageWriter {

    private FileStorageWriter() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Write a line to a file
     * The file will be created if it does not exist
     * @param file
     * @param line
     * @throws StorageError: if file cannot be created, if file is not writable
     * or if there is an error writing
     */
    public static void write(File file, String line) throws StorageError {

        // Check if file exists and create it if not
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    throw new StorageError(StorageError.Cause.OTHER, "Cannot create file");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Ensure that file is writable
        if (!file.canWrite()) {
            throw new StorageError(StorageError.Cause.NOT_WRITABLE, file.getAbsolutePath());
        }

        // Write line
        try (FileWriter fileWriter = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fileWriter)) {
            bw.write(line);
            bw.newLine();
            bw.flush();
        } catch (IOException e) {
            throw new StorageError(StorageError.Cause.WRITING_ERROR,
                    e.getLocalizedMessage());
        }
    }

}
