package org.kickmyb.serveur.photo;

import org.kickmyb.serveur.tache.MTache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepotPhoto extends JpaRepository<MPhoto, Long> {
    Optional<MPhoto> findByTache(MTache task);
}
