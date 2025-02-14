package com.acme.flug.repository;

import com.acme.flug.entity.Flug;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * Singleton-Klasse, um Specifications für Queries in Spring Data JPA zu bauen.
 */
@Component
@Slf4j
@SuppressWarnings({"LambdaParameterName", "IllegalIdentifierName"})
public class SpecificationBuilder {

    /**
     * Specification für eine Query mit Spring Data bauen.
     *
     * @param queryParams als MultiValueMap
     * @return Specification für eine Query mit Spring Data
     */
    public Optional<Specification<Flug>> build(final Map<String, ? extends List<String>> queryParams) {
        log.debug("build: queryParams={}", queryParams);

        if (queryParams.isEmpty()) {
            // keine Suchkriterien
            return Optional.empty();
        }

        final var specs = queryParams.entrySet().stream()
            .map(this::toSpecification)
            .filter(Objects::nonNull)
            .toList();

        if (specs.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(specs.stream().reduce(Specification::and).orElseThrow());
    }

    private Specification<Flug> toSpecification(final Map.Entry<String, ? extends List<String>> entry) {
        log.trace("toSpec: entry={}", entry);
        final var key = entry.getKey();
        final var values = entry.getValue();
        if ("status".contentEquals(key)) {
            return toSpecificationStatus(values);
        }

        if (values == null || values.size() != 1) {
            return null;
        }

        final var value = values.getFirst();
        return switch (key) {
            case "zielFlughafen" -> zielflughafen(value);
            case "startFlughafen" -> startFlughafen(value);
            default -> null;
        };
    }

    private Specification<Flug> toSpecificationStatus(final Collection<String> status) {
        log.trace("build: status={}", status);
        if (status == null || status.isEmpty()) {
            return null;
        }

        final var specs = status.stream()
            .map(this::status)
            .filter(Objects::nonNull)
            .toList();

        if (specs.isEmpty()) {
            return null;
        }

        return specs.stream().reduce(Specification::and).orElse(null);
    }

    private Specification<Flug> startFlughafen(final String teil) {
        return (root, _, builder) -> builder.like(
            builder.lower(root.get("startFlughafen")),
            "%" + teil.toLowerCase() + "%"
        );
    }

    private Specification<Flug> zielflughafen(final String teil) {
        return (root, _, builder) -> builder.like(
            builder.lower(root.get("zielFlughafen")),
            "%" + teil.toLowerCase() + "%"
        );
    }

    private Specification<Flug> status(final String status) {
        return (root, _, builder) -> builder.equal(
            root.get("status"),
            status
        );
    }
}



