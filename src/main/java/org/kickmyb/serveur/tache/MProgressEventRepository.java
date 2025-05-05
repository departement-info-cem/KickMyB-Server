package org.kickmyb.serveur.tache;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Mostly used to have creation method, could use cascading

@Repository
public interface MProgressEventRepository extends JpaRepository<MProgressEvent, Long> { }
