package com.acme.flug.controller;

import com.acme.flug.entity.StatusType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.hibernate.validator.constraints.UniqueElements;

/**
 * ValueObject für das Neuanlegen und Ändern eines neuen Fluges.
 *
 * @param startFlughafen startFlughafen
 * @param zielFlughafen  zielFlughafen
 * @param status         Status
 * @param buchungen      Buchungen
 * @param flugzeug       Flugzeug
 * @param hotelId        HotelId
 * @param hotelName      HotelName
 */
record FlugDTO(
    @NotBlank
    String startFlughafen,

    @NotBlank
    String zielFlughafen,

    StatusType status,

    UUID hotelId,

    String hotelName,

    @NotNull
    @Valid
    FlugzeugDTO flugzeug,

    @NotEmpty
    @UniqueElements
    List<BuchungDTO> buchungen
) {
}

