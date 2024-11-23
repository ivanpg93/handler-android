package es.ivanpg93.error;

/**
 * Exception that represents an http connection error
 */
public class ConnectionError extends Exception {

    public ConnectionError(Exception e) {
        super(e.getMessage(), e.getCause());
    }

}
