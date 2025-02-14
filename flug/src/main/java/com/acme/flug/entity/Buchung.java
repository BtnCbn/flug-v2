package com.acme.flug.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Klasse für die Entity Buchung.
 */
@Entity
@Table(name = "buchung")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Builder
@Getter
@Setter
@ToString
public class Buchung {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    private String nachname;

    private LocalDateTime zeitpunkt;
}
