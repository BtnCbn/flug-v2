/*
 * Copyright (C) 2024 - present Juergen Zimmermann, Hochschule Karlsruhe
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.acme.flug.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.stream.Stream;

/**
 * Token-Typ für OAuth 2.0 (hier nur: Bearer).
 */
public enum TokenType {
    /**
     * Bearer als Token-Typ.
     */
    BEARER("Bearer");

    private final String value;

    TokenType(final String value) {
        this.value = value;
    }

    /**
     * Konvertierung eines Strings in einen Enum-Wert.
     *
     * @param value Der String, zu dem ein passender Enum-Wert ermittelt werden soll.
     * @return Passender Enum-Wert oder null.
     */
    @JsonCreator
    public static TokenType of(final String value) {
        return Stream.of(values())
            .filter(token -> token.value.equalsIgnoreCase(value))
            .findFirst()
            .orElse(null);
    }

    /**
     * Einen enum-Wert als String mit dem internen Wert ausgeben.
     * Dieser Wert wird durch Jackson in einem JSON-Datensatz verwendet.
     * [<a href="https://github.com/FasterXML/jackson-databind/wiki">Wiki-Seiten</a>]
     *
     * @return Interner Wert
     */
    @JsonValue
    public String getValue() {
        return value;
    }
}
