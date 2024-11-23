package es.ivanpg93.logging;

import androidx.annotation.NonNull;

import es.ivanpg93.model.RequestMessage;

public interface Logger {
    void log(@NonNull RequestMessage message);
}
