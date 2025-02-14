package com.acme.flug.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

/**
 * Enum für Status.
 */
public enum StatusType {
    /**
     * _GESTARTET_ mit dem internen Wert `GES`.
     */
    GESTARTET("GES"),

    /**
     * _GELANDET_ mit dem internen Wert `GEL`.
     */
    GELANDET("GEL"),

    /**
     * _ READY_FOR_TAKEOFF_ mit dem internen Wert `R`.
     */
    READY_FOR_TAKEOFF("R");

    private final String value;

    StatusType(final String value) {
        this.value = value;
    }

    /**
     * Konvertierung eines Strings in einen Enum-Wert.
     *
     * @param value Der String, zu dem ein passender Enum-Wert ermittelt werden soll.
     * @return Passender Enum-Wert oder null.
     */
    @JsonCreator
    public static StatusType of(final String value) {
        return Stream.of(values())
            .filter(status -> status.value.equalsIgnoreCase(value))
            .findFirst()
            .orElse(null);
    }

    /**
     * Einen enum-Wert als String mit dem internen Wert ausgeben.
     * Dieser Wert wird durch Jackson in einem JSON-Datensatz verwendet.
     *
     * @return Interner Wert
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}
