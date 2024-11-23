package es.ivanpg93.logging.repository;

import es.ivanpg93.storage.StorageError;

public interface LogRepository {
    void write(String string) throws StorageError;
}
