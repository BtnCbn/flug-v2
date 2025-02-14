package com.acme.flug.controller;

/**
 * Enum für ProblemDetail.type.
 *
 */
enum ProblemType {
    /**
     * Constraints als Fehlerursache.
     */
    CONSTRAINTS("constraints"),

    /**
     * Fehler, wenn z.B. ungültiger Startflughafen.
     */
    UNPROCESSABLE("unprocessable"),

    /**
     * Fehler, wenn z.B. Anfrage nicht lesbar ist
     */
    BAD_REQUEST("badRequest"),

    PRECONDITION("precondition");

    private final String value;

    ProblemType(final String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }
}
