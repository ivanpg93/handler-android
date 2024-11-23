package es.ivanpg93.error;

/**
 * Represents an error that occurs when a connection to the server cannot be established.
 * This error can be caused by a lack of internet connection or any other reason that prevents accessing the network.
 * Example: "java.net.ConnectException: failed to connect to /192.168.1.75 (port 8000) from /10.0.2.16
 * (port 40894) after 999ms: isConnected failed: ENETUNREACH (Network is unreachable)"
 */
public class ConnectError extends ConnectionError {

    public ConnectError(Exception e) {
        super(e);
    }

}
