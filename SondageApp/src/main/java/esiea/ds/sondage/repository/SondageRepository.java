package esiea.ds.sondage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import esiea.ds.sondage.model.Role;
import esiea.ds.sondage.model.RoleName;
import esiea.ds.sondage.model.Sondage;

import java.util.Optional;

@Repository
public interface SondageRepository extends JpaRepository<Sondage, Long> {
    Optional<Sondage> findByTitre(String titre);
}
