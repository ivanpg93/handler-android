package es.ivanpg93.handlers.handler;

import android.webkit.MimeTypeMap;

import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MultipartFormHandler extends JsonHandler {

    @Override
    public RequestBody getRequestBody(Object data) {
        if (data instanceof JSONObject) {
            return generateMultipartForm((JSONObject) data);
        } else {
            throw new UnsupportedOperationException("MultipartFormHandler must receive a JSON with key - value pairs");
        }
    }

    /**
     * Converts a JSON object to a multipart form
     * @param json
     * @return
     */
    private MultipartBody generateMultipartForm(JSONObject json) {

        // Create builder
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        // Iterate through json to add key - value pairs on form
        for (Iterator<String> keys = json.keys(); keys.hasNext();) {
            String key = keys.next();
            Object value = json.opt(key);

            // Do nothing, continue iterating
            if (value == null) {
                continue;
            }

            if (value instanceof List) {
                List<Object> listValues = (List<Object>) value;
                for (Object listValue : listValues) {
                    addValueFormData(builder, key, listValue);
                }
            } else {
                addValueFormData(builder, key, value);
            }
        }

        // Build multipart body
        return builder.build();
    }

    private void addValueFormData(MultipartBody.Builder builder, String key, Object value) {
        if (value instanceof File) {

            // File value
            File file = (File) value;
            String mimeType = getMimeType(file.getAbsolutePath());
            if (mimeType != null) {
                RequestBody fileData = RequestBody.create(MediaType.parse(mimeType), file);
                builder.addFormDataPart(key, file.getName(), fileData);
            }
        }  else {

            // String value
            String valueString = String.valueOf(value);
            builder.addFormDataPart(key, valueString);
        }
    }

    /**
     * Returns the mime type of a path
     * @param url
     * @return
     */
    private static String getMimeType(String url) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        return extension != null ? MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension) : null;
    }

}
