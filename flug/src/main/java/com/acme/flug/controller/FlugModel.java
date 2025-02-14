package com.acme.flug.controller;

import com.acme.flug.entity.Buchung;
import com.acme.flug.entity.Flug;
import com.acme.flug.entity.Flugzeug;
import com.acme.flug.entity.StatusType;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

/**
 * Model-Klasse für Spring HATEOAS.
 * <img src="C:\ideaProjects\flug-v0\flug\build\docs\asciidoc\FlugModel.svg" alt="Klassendiagramm"/>
 */
@JsonPropertyOrder({
    "startFlughafen", "zielFlughafen", "status", "buchungen", "flugzeug", "id",
    "hotelId", "hotelname"
})
@Relation(collectionRelation = "fluege", itemRelation = "flug")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@ToString(callSuper = true)
class FlugModel extends RepresentationModel<FlugModel> {
    private final String startFlughafen;
    private final String zielFlughafen;
    private final StatusType status;
    private final List<Buchung> buchungen;
    private final Flugzeug flugzeug;
    private final UUID id;
    private final UUID hotelId;
    private final String hotelname;

    FlugModel(final Flug flug) {
        startFlughafen = flug.getStartFlughafen();
        zielFlughafen = flug.getZielFlughafen();
        status = flug.getStatus();
        buchungen = flug.getBuchungen();
        flugzeug = flug.getFlugzeug();
        id = flug.getId();
        hotelId = flug.getHotelId();
        hotelname = flug.getHotelName();
    }
}

