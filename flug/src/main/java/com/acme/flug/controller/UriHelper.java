package com.acme.flug.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import static com.acme.flug.controller.FlugGetController.REST_PATH;

/**
 * Hilfsklasse um URIs für HATEOAS oder für URIs in ProblemDetail zu ermitteln.
 */
@Component
@Slf4j
class UriHelper {

    /**
     * Basis-URI ermitteln.
     *
     * @param request Servlet-Request
     * @return Die Basis-URI als String
     */
    URI getBaseUri(final HttpServletRequest request) {
        final var uriComponents = ServletUriComponentsBuilder.fromRequestUri(request).build();
        final var baseUri =
            uriComponents.getScheme() + "://" + uriComponents.getHost() + ':' + uriComponents.getPort() + REST_PATH;
        log.debug("getBaseUri (ohne Forwarding): baseUri={}", baseUri);
        return URI.create(baseUri);
    }
}

