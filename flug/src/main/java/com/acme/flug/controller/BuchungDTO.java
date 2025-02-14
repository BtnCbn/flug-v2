package com.acme.flug.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * ValueObject für das Neuanlegen und Ändern eines neuen Fluges.
 *
 * @param nachname  Nachname
 * @param zeitpunkt zeitPunkt
 */
record BuchungDTO(
    @NotNull
    @Pattern(regexp = NACHNAME_PATTERN)
    String nachname,

    @NotNull
    @Past
    LocalDateTime zeitpunkt
) {
    /**
     * Muster für einen gültigen Nachnamen.
     */
    public static final String NACHNAME_PATTERN =
        "(o'|von|von der|von und zu|van)?[A-ZÄÖÜ][a-zäöüß]+(-[A-ZÄÖÜ][a-zäöüß]+)?";
}

