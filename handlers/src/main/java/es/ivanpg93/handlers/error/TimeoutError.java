package es.ivanpg93.handlers.error;

/**
 * Represents an error that occurs when the connection to a server exceeds the configured timeout limit.
 * This error is thrown when the connection or the read/write operation on a socket takes longer than allowed.
 * Example: "java.net.SocketTimeoutException: failed to connect to /192.168.1.75 (port 8000)
 * from /10.0.2.16 (port 40892) after 1000ms"
 */
public class TimeoutError extends ConnectionError {

    public TimeoutError(Exception e) {
        super(e);
    }

}

