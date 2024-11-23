package es.ivanpg93.handlers.logging;

import androidx.annotation.NonNull;

import es.ivanpg93.handlers.model.RequestMessage;

public interface Logger {
    void log(@NonNull RequestMessage message);
}
