package es.app2u.beat.logging.repository;

import es.app2u.beat.storage.StorageError;

public interface LogRepository {
    void write(String string) throws StorageError;
}
