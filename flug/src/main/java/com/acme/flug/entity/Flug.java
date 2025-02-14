package com.acme.flug.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;
import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;

/**
 * Klasse für die Entity Flug.
 * <img src="C:\ideaProjects\flug-v0\flug\build\docs\asciidoc\Flug.svg" alt="Klassendiagramm"/>
 */
@Entity
@Table(name = "flug")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Getter
@Setter
@ToString
@Builder
@SuppressWarnings({
    "ClassFanOutComplexity",
    "RequireEmptyLineBeforeBlockTagGroup",
    "DeclarationOrder",
    "JavadocDeclaration",
    "MissingSummary",
    "RedundantSuppression", "com.intellij.jpb.LombokEqualsAndHashCodeInspection"})
public class Flug {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "hotel_id")
    private UUID hotelId;

    @Version
    private int version;

    private String startFlughafen;

    private String zielFlughafen;

    @Enumerated(STRING)
    private StatusType status;

    @OneToOne(optional = false, cascade = {PERSIST, REMOVE}, fetch = LAZY, orphanRemoval = true)
    @ToString.Exclude
    private Flugzeug flugzeug;

    /**
     * Buchungen des Fluges.
     * Können doppelt vorkommen z.b. bei gemeinsamen Buchungen von einer Familie.
     * Siehe auch {@link Buchung}
     */
    @OneToMany(cascade = {PERSIST, REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "flug_id")
    @OrderColumn(name = "idx", nullable = false)
    @ToString.Exclude
    private List<Buchung> buchungen;

    @CreationTimestamp
    private LocalDateTime erzeugt;

    @UpdateTimestamp
    private LocalDateTime aktualisiert;

    @Transient
    private String hotelName;
}

