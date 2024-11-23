package es.app2u.beat.logging;

import androidx.annotation.NonNull;

import es.app2u.beat.model.RequestMessage;

public interface Logger {
    void log(@NonNull RequestMessage message);
}
