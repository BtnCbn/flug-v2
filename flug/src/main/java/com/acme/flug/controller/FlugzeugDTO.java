package com.acme.flug.controller;

import jakarta.validation.constraints.NotBlank;

/**
 * ValueObject für das Neuanlegen und Ändern eines neuen Fluges.
 *
 * @param model Model
 */
record FlugzeugDTO(
    @NotBlank
    String model
) {
}
