package es.ivanpg93.handlers.error;

/**
 * Represents an error that occurs when the host address cannot be resolved.
 * This error is thrown when the specified hostname cannot be mapped to an IP address.
 * Example: "java.net.UnknownHostException: Unable to resolve host 'unreachable.url':
 * No address associated with hostname"
 */
public class UnknownHostError extends ConnectionError {

    public UnknownHostError(Exception e) {
        super(e);
    }

}