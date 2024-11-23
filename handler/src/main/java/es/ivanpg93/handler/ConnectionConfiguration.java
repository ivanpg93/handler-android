package es.ivanpg93.handler;

import androidx.annotation.Nullable;

import java.util.HashSet;
import java.util.Set;

import es.ivanpg93.logging.Logger;

/**
 * Singleton class that contains default configurations for the ConnectionHandler.
 * If want to customize each request, use the Parameters class. @see Parameters
 */
public class ConnectionConfiguration {

    // Singleton
    private static final ConnectionConfiguration instance = new ConnectionConfiguration();

    public static final int DEFAULT_TIMEOUT_CONNECTION = 10; //seconds
    public static final int DEFAULT_TIMEOUT_WRITE = 20; //seconds
    public static final int DEFAULT_TIMEOUT_READ = 20; //seconds

    private int timeoutConnection = DEFAULT_TIMEOUT_CONNECTION;
    private int timeoutWrite = DEFAULT_TIMEOUT_WRITE;
    private int timeoutRead = DEFAULT_TIMEOUT_READ;
    private RequestMiddleware requestMiddleware;
    private final Set<Logger> loggers = new HashSet<>();

    // Indicates if must trust all SSL certificates or must indicate which certificates are trusted
    private boolean trustAllCertificates = false;

    private ConnectionConfiguration() {
    }

    public static ConnectionConfiguration getInstance() {
        return instance;
    }

    public int getTimeoutConnection() {
        return timeoutConnection;
    }

    public void setTimeoutConnection(int timeoutConnection) {
        this.timeoutConnection = timeoutConnection;
    }

    public int getTimeoutWrite() {
        return timeoutWrite;
    }

    public void setTimeoutWrite(int timeoutWrite) {
        this.timeoutWrite = timeoutWrite;
    }

    public int getTimeoutRead() {
        return timeoutRead;
    }

    public void setTimeoutRead(int timeoutRead) {
        this.timeoutRead = timeoutRead;
    }

    public void setTrustAllCertificates(boolean trustAllCertificates) {
        this.trustAllCertificates = trustAllCertificates;
    }

    public boolean getTrustAllCertificates() {
        return this.trustAllCertificates;
    }

    @Nullable
    public RequestMiddleware getRequestMiddleware() {
        return requestMiddleware;
    }

    public void setRequestMiddleware(RequestMiddleware requestMiddleware) {
        this.requestMiddleware = requestMiddleware;
    }

    public void addLogger(Logger logger) {
        loggers.add(logger);
    }

    public Set<Logger> getLoggers() {
        return loggers;
    }

}
