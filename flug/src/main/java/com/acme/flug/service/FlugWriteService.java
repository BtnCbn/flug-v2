package com.acme.flug.service;

import com.acme.flug.entity.Flug;
import com.acme.flug.repository.FlugRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Anwendungslogik für flugn auch mit Bean Validation.
 * <img src="../../../../../asciidoc/flugWriteService.svg" alt="Klassendiagramm">
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class FlugWriteService {
    private final FlugRepository repo;

    /**
     * Einen neuen Flugn anlegen.
     *
     * @param flug Das Objekt des neu anzulegenden Flugn.
     * @return Der neu angelegte Flugn mit generierter ID
     */
    @Transactional
    @SuppressWarnings("TrailingComment")
    public Flug create(final Flug flug) {
        log.debug("create: flug={}", flug);
        log.debug("create: flugzeug={}", flug.getFlugzeug());
        log.debug("create: Buchungen={}", flug.getBuchungen());

        final var flugDB = repo.save(flug);

        log.trace("create: Thread-ID={}", Thread.currentThread().threadId());
        log.debug("create: flugDB={}", flugDB);
        return flugDB;
    }

    /**
     * Einen vorhandenen Flug aktualisieren.
     *
     * @param flug Das Objekt mit den neuen Daten (ohne ID)
     * @param id ID des zu aktualisierenden Fluges
     * @param version Die erforderliche Version
     * @return Aktualisierter Flug mit erhöhter Versionsnummer
     * @throws NotFoundException Kein Flug zur ID vorhanden.
     * @throws VersionOutdatedException Die Versionsnummer ist veraltet und nicht aktuell.
     */
    @Transactional
    public Flug update(final Flug flug, final UUID id, final int version) {
        log.debug("update: Flug={}", flug);
        log.debug("update: id={}, version={}", id, version);

        var flugDb = repo
            .findById(id)
            .orElseThrow(() -> new NotFoundException(id));
        log.trace("update: version={}, flugDb={}", version, flugDb);
        if (version != flugDb.getVersion()) {
            throw new VersionOutdatedException(version);
        }
        flugDb = repo.save(flugDb);

        log.debug("update: {}", flugDb);
        return flugDb;
    }
}
