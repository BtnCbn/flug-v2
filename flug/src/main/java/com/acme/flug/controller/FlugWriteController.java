package com.acme.flug.controller;

import com.acme.flug.service.FlugWriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import static com.acme.flug.controller.FlugGetController.ID_PATTERN;
import static com.acme.flug.controller.FlugGetController.REST_PATH;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static org.springframework.http.HttpStatus.PRECONDITION_REQUIRED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

/**
 * Eine Controller-Klasse, welche auf die REST-Schnittstelle abbildet.
 * <img src="C:\ideaProjects\flug-v0\flug\build\docs\asciidoc\FlugWriteController.svg" alt="Klassendiagramm"/>
 */
@Controller
@RequestMapping(REST_PATH)
@RequiredArgsConstructor
@Slf4j
@Validated
@SuppressWarnings({"ClassFanOutComplexity", "java:S1075"})
public class FlugWriteController {
    private static final String PROBLEM_PATH = "/problem/";
    private final FlugWriteService service;
    private final FlugMapper mapper;
    private final UriHelper uriHelper;

    /**
     * Einen neuen Flug-Datensatz anlegen.
     *
     * @param flugDTO Das Flugobjekt aus dem eingegangenen Request-Body.
     * @param request Das Request-Objekt, um `Location` im Response-Header zu erstellen.
     * @return Response mit Statuscode 201, einschließlich Location-Header oder Statuscode 422,
     */
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @Operation(summary = "Einen neuen Fluege anlegen", tags = "Neuanlegen")
    @ApiResponse(responseCode = "201", description = "Flug neu angelegt")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte")
    ResponseEntity<Void> post(
        @RequestBody @Valid final FlugDTO flugDTO,
        final HttpServletRequest request
    ) {
        log.debug("post: {}", flugDTO);

        final var flugInput = mapper.toFlug(flugDTO);
        final var flug = service.create(flugInput);
        final var baseUri = uriHelper.getBaseUri(request).toString();
        final var location = URI.create(baseUri + '/' + flug.getId());
        return created(location).build();
    }

    /**
     * Einen vorhandenen Flug-Datensatz überschreiben.
     *
     * @param id ID des zu aktualisierenden Fluges.
     * @param flugDTO Das Flugbjekt aus dem eingegangenen Request-Body
     * @param versionOpt Versionsnummer aus dem Header If-Match
     * @param request Das Request-Objekt, um ggf. die URL für ProblemDetail zu ermitteln
     * @return Response mit Statuscode 204 oder Statuscode 400, falls der JSON-Datensatz syntaktisch nicht korrekt ist
     *         oder 422, falls Constraints verletzt sind.
     *         oder 412, falls die Versionsnummer nicht ok ist oder 428, falls die Versionsnummer fehlt.
     */
    @PutMapping(path = "{id:" + ID_PATTERN + "}", consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(NO_CONTENT)
    @Operation(summary = "Einen Flug mit neuen Werten aktualisieren", tags = "Aktualisieren")
    @ApiResponse(responseCode = "204", description = "Aktualisiert")
    @ApiResponse(responseCode = "400", description = "Syntaktische Fehler im Request-Body")
    @ApiResponse(responseCode = "404", description = "Flug nicht vorhanden")
    @ApiResponse(responseCode = "422", description = "Ungültige Werte")
    @ApiResponse(responseCode = "428", description = "Versionsnummer fehlt")
    ResponseEntity<Void> put(
        @PathVariable final UUID id,
        @RequestBody final FlugDTO flugDTO,
        @RequestHeader("If-Match") final Optional<String> versionOpt,
        final HttpServletRequest request
    ) {
        log.debug("put: dto={} id={}", flugDTO, id);
        final int version = getVersion(versionOpt, request);

        final var flugMap = mapper.toFlug(flugDTO);
        final var flug = service.update(flugMap, id, version);

        log.debug("put: {}", flug);
        return noContent().eTag(String.format("\"%s\"", flug.getVersion())).build();
    }


    /**
     * Ausgelagerte Methode für Versionsprüfung.
     *
     * @param versionOpt Versionsnummer aus dem Header If-Match
     * @param request Request-Objekt für Problem URI Konstruktion
     * @return Geprüfte Versionsnummer
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    private int getVersion(final Optional<String> versionOpt, final HttpServletRequest request) {
        log.trace("getVersion: versionOpt={}", versionOpt);
        final var versionStr = versionOpt.orElseThrow(() -> new VersionInvalidException(
            PRECONDITION_REQUIRED,
            "Versionsnummer fehlt",
            URI.create(request.getRequestURL().toString())
        ));

        if (versionStr.length() < 3 ||
            versionStr.charAt(0) != '"' ||
            versionStr.charAt(versionStr.length() - 1) != '"'
        ) {
            throw new VersionInvalidException(
                PRECONDITION_FAILED,
                String.format("Versionsnummer falsch: %s", versionStr),
                URI.create(request.getRequestURL().toString())
            );
        }

        final int version;
        try {
            version = Integer.parseInt(versionStr.substring(1, versionStr.length() - 1));
        } catch (final NumberFormatException ex) {
            throw new VersionInvalidException(
                PRECONDITION_FAILED,
                String.format("Versionsnummer falsch: %s", versionStr),
                URI.create(request.getRequestURL().toString()),
                ex
            );
        }

        log.trace("getVersion: {}", version);
        return version;
    }



    @ExceptionHandler
    ProblemDetail onConstraintViolations(
        final MethodArgumentNotValidException ex,
        final HttpServletRequest request
    ) {
        log.debug("onConstraintViolations: {}", ex.getMessage());

        final var detailMessages = ex.getDetailMessageArguments();
        final var detail = detailMessages == null
            ? "Constraint Violations"
            : ((String) detailMessages[1]).replace(", and ", ", ");
        final var problemDetail = ProblemDetail.forStatusAndDetail(UNPROCESSABLE_ENTITY, detail);
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.CONSTRAINTS.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));

        return problemDetail;
    }

    @ExceptionHandler
    ProblemDetail onHttpMessageNotReadable(
        final HttpMessageNotReadableException ex,
        final HttpServletRequest request
    ) {
        log.debug("onMessageNotReadable: {}", ex.getMessage());
        final var problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create(PROBLEM_PATH + ProblemType.BAD_REQUEST.getValue()));
        problemDetail.setInstance(URI.create(request.getRequestURL().toString()));
        return problemDetail;
    }
}

