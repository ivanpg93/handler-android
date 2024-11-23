package es.app2u.beat.storage;

public class StorageError extends Exception {

    public enum Cause {
        NOT_WRITABLE,
        WRITING_ERROR,
        OTHER
    }

    private final Cause errorCause;

    public StorageError(Cause errorCause) {
        this.errorCause = errorCause;
    }

    public StorageError(Cause errorCause, String message) {
        super(message);
        this.errorCause = errorCause;
    }

    public Cause getErrorCause() {
        return errorCause;
    }

}
