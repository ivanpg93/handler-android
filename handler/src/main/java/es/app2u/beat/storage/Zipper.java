package es.app2u.beat.storage;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {

    private Zipper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Zip a file
     */
    public static void zip(String source, String destination) {
        final int BUFFER = 6 * 1024;

        try (FileOutputStream dest = new FileOutputStream(destination);
             ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
             FileInputStream fi = new FileInputStream(source);
             BufferedInputStream origin = new BufferedInputStream(fi, BUFFER)) {
            byte[] data = new byte[BUFFER];
            ZipEntry entry = new ZipEntry(getLastPathComponent(source));
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
        } catch (Exception e) {
            Log.e(Zipper.class.getSimpleName(), e.getLocalizedMessage());
        }
    }

    private static String getLastPathComponent(String filePath) {
        int index = filePath.lastIndexOf(File.separator);
        return index < 0 ? "" : filePath.substring(index + 1);
    }

}
