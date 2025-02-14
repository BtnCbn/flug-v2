package com.acme.flug.service;

import com.acme.flug.entity.Flug;
import jakarta.annotation.PostConstruct;
import com.acme.flug.repository.HotelRepository;
import com.acme.flug.repository.FlugRepository;
import com.acme.flug.repository.Hotel;
import java.util.Base64;
import java.util.UUID;
import com.acme.flug.security.KeycloakProps;
import com.acme.flug.security.KeycloakRepository;
import java.nio.charset.Charset;
import java.util.Collection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

/**
 * Anwendungslogik für Flüge.
 * <img src="C:\ideaProjects\flug-v0\flug\build\docs\asciidoc\FlugReadService.svg" alt="Klassendiagramm"/>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FlugReadService {
    private static final String ADMIN = "admin";
    private static final String PASSWORD = "p";

    private final FlugRepository repo;
    private final HotelRepository hotelRepo;
    private final KeycloakRepository keycloakRepository;
    private final KeycloakProps keycloakProps;

    private String clientAndSecretEncoded;

    @PostConstruct
    private void encodeClientAndSecret() {
        final var clientAndSecret = keycloakProps.clientId() + ':' + keycloakProps.clientSecret();
        clientAndSecretEncoded = Base64
            .getEncoder()
            .encodeToString(clientAndSecret.getBytes(Charset.defaultCharset()));
    }

    /**
     * Alle Bestellungen ermitteln.
     *
     * @return Alle Bestellungen.
     */
    public Collection<Flug> findAll() {
        final var fluege = repo.findAll();
        fluege.forEach(flug -> {
            final var hotelId = flug.getHotelId();

            final var name = findHotelById(hotelId).name();
            flug.setHotelName(name);
        });
        return fluege;
    }

    private String getAuthorization() {
        log.debug("login: loginDto={}");
        final var tokenDTO = keycloakRepository.login(
            "grant_type=password&username=" + ADMIN + "&password=" + PASSWORD,
            "Basic " + clientAndSecretEncoded,
            APPLICATION_FORM_URLENCODED_VALUE
        );
        log.debug("login: tokenDTO={}", tokenDTO);
        return "Bearer " + tokenDTO.accessToken();
    }

    @SuppressWarnings("ReturnCount")
    private Hotel findHotelById(final UUID hotelId) {
        log.debug("findHotelById: hotelId={}", hotelId);

        final Hotel hotel;
        try {
            hotel = hotelRepo.getById(hotelId.toString());
        } catch (final HttpClientErrorException.NotFound ex) {
            log.debug("findHotelById: HttpClientErrorException.NotFound");
            return new Hotel("N/A");
        } catch (final HttpStatusCodeException ex) {

            log.debug("findKundeById", ex);
            return new Hotel("Exception");
        }

        log.debug("findHotelById: {}", hotel);
        return hotel;
    }

    /**
     * Einen Flug anhand seiner ID suchen.
     *
     * @param id Die Id des gesuchten Fluges
     * @return Der gefundene Flug
     * @throws NotFoundException Falls kein Kunde gefunden wurde
     */
    public @NonNull Flug findById(
        final UUID id
    ) {
        log.debug("findById: id={}", id);

        final var flugOptional = repo.findById(id);
        final var flug = flugOptional.orElse(null);
        log.trace("findById: flug={}", flug);

        if (flug == null) {
            throw new NotFoundException(id);
        }

        return flug;
    }
}

