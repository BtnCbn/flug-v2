package com.acme.flug.repository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.IF_NONE_MATCH;

/**
 * "HTTP Interface" für den REST-Client für Hotel Daten.
 */
@HttpExchange("/rest")
public interface HotelRepository {
    /**
     * Einen Kundendatensatz vom Microservice "hotel" mit "Basic Authentication" anfordern.
     *
     * @param id ID des angeforderten Hotel.
     * @return Gefundenes Hotel.
     */
    @GetExchange("/{id}")
    Hotel getById(@PathVariable String id);

    /**
     * Einen Hoteldatensatz vom Microservice "hotel" mit "Basic Authentication" anfordern.
     *
     * @param id ID des angeforderten Hotels
     * @param version Version des angeforderten Datensatzes
     * @param authorization String für den HTTP-Header "Authorization"
     * @return Gefundenes Hotel
     */
    @GetExchange("/{id}")
    @SuppressWarnings("unused")
    ResponseEntity<Hotel> getById(
        @PathVariable String id,
        @RequestHeader(IF_NONE_MATCH) String version,
        @RequestHeader(AUTHORIZATION) String authorization
    );
}
