package es.ivanpg93.handlers.logging.repository;

import es.ivanpg93.handlers.storage.StorageError;

public interface LogRepository {
    void write(String string) throws StorageError;
}
