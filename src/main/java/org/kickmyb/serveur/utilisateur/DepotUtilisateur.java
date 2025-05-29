package org.kickmyb.serveur.utilisateur;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepotUtilisateur extends JpaRepository<MUtilisateur, Long> {

    // Un peu de magie, la méthode est générée par Spring Data JPA.
    // https://docs.spring.io/spring-data/data-commons/docs/1.6.1.RELEASE/reference/html/repositories.html
    Optional<MUtilisateur> findByNom(String nomUtilisateur);
}
