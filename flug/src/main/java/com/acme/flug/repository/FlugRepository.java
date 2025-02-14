package com.acme.flug.repository;

import com.acme.flug.entity.Flug;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * Repository für Datenbanken-Zugriff für Flüge.
 */
@Repository
public interface FlugRepository extends JpaRepository<Flug, UUID>, JpaSpecificationExecutor<Flug> {

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"flugzeug", "buchungen"})
    List<Flug> findAll();

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"flugzeug", "buchungen"})
    List<Flug> findAll(@NonNull Specification<Flug> spec);

    @NonNull
    @Override
    @EntityGraph(attributePaths = {"flugzeug", "buchungen"})
    Optional<Flug> findById(@NonNull UUID id);

    /**
     * Fluege zu gegebener Hotel-ID aus der DB ermitteln.
     *
     * @param hotelId hotel-ID für die Suche
     * @return Liste der gefundenen Fluege
     */
    @EntityGraph(attributePaths = {"flugzeug", "buchungen"})
    List<Flug> findByHotelId(UUID hotelId);
}
