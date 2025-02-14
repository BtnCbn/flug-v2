package com.acme.flug.service;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;

/**
 * RuntimeException, falls kein FLug gefunden wurde.
 */
@Getter
public final class NotFoundException extends RuntimeException {
    /**
     * Nicht-vorhandene ID.
     */
    private final UUID id;

    /**
     * Suchkriterien, zu denen nichts gefunden wurde.
     */
    private final Map<String, List<String>> suchkriterien;

    NotFoundException(final UUID id) {
        super("Kein Flug mit der ID " + id + " gefunden.");
        this.id = id;
        suchkriterien = null;
    }

    NotFoundException(final Map<String, List<String>> suchkriterien) {
        super("Keinen Flug gefunden.");
        id = null;
        this.suchkriterien = suchkriterien;
    }

    NotFoundException() {
        super("Keinen Flug gefunden.");
        id = null;
        suchkriterien = null;
    }
}



