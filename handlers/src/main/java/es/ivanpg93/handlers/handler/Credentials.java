package es.ivanpg93.handlers.handler;

import android.text.TextUtils;

import androidx.annotation.Nullable;

public class Credentials {
    private static final String KEY_TOKEN = "Token";
    private static final String KEY_BEARER = "Bearer";

    private Credentials() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Basic authentication
     * @param username
     * @param password
     * @return
     */
    public static @Nullable String basic(String username, String password) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            return null;
        }
        return okhttp3.Credentials.basic(username, password);
    }

    public static String token(String token) {
        return customToken(KEY_TOKEN, token);
    }

    public static String bearer(String token) {
        return customToken(KEY_BEARER, token);
    }

    private static String customToken(String keyword, String token) {
        return String.format("%s %s", keyword, token);
    }

}