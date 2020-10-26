package web.sondage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import web.sondage.model.Role;
import web.sondage.model.RoleName;
import web.sondage.model.Sondage;

import java.util.Optional;

@Repository
public interface SondageRepository extends JpaRepository<Sondage, Long> {
    Optional<Sondage> findByTitre(String titre);
}
