package com.acme.flug.dev;

import org.springframework.context.annotation.Profile;
import static com.acme.flug.dev.DevConfig.DEV;

/**
 * Konfigurationsklasse für die Anwendung bzw. den Microservice, falls das Profile dev aktiviert ist.
 *
 * @author <a href="mailto:Juergen.Zimmermann@h-ka.de">Jürgen Zimmermann</a>
 */
@Profile(DEV)
@SuppressWarnings({"ClassNamePrefixedWithPackageName", "HideUtilityClassConstructor"})
public class DevConfig implements Flyway, LogRequestHeaders, K8s {
    /**
     * Konstante für das Spring-Profile "dev".
     */
    public static final String DEV = "dev";

    DevConfig() {
    }
}

