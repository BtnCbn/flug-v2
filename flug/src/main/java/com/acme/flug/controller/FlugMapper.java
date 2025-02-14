package com.acme.flug.controller;

import com.acme.flug.entity.Buchung;
import com.acme.flug.entity.Flug;
import com.acme.flug.entity.Flugzeug;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

/**
 * Mapper zwischen Entity-Klassen.
 */
@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_DEFAULT)
interface FlugMapper {
    /**
     * Ein DTO-Objekt von FlugDTO in ein Objekt für Flug konvertieren.
     *
     * @param dto DTO-Objekt für FlugDTO ohne ID
     * @return Konvertiertes Kunde-Objekt mit null als ID
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "erzeugt", ignore = true)
    @Mapping(target = "aktualisiert", ignore = true)
    Flug toFlug(FlugDTO dto);

    /**
     * Ein DTO-Objekt von FlugzeugDTO in ein Objekt für Flugzeug konvertieren.
     *
     * @param dto DTO-Objekt für FlugzeugDTO ohne Flug.
     * @return Konvertiertes Flugzeug-Objekt
     */
    @Mapping(target = "id", ignore = true)
    Flugzeug toFlugzeug(FlugzeugDTO dto);

    /**
     * Ein DTO-Objekt von BuchungDTO in ein Objekt für Buchung konvertieren.
     *
     * @param dto DTO-Objekt für BuchungDTO ohne Buchung
     * @return Konvertiertes Buchung-Objekt
     */
    @Mapping(target = "id", ignore = true)
    Buchung toBuchung(BuchungDTO dto);
}
