package esiea.ds.sondage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import esiea.ds.sondage.model.DateRendezVous;
import esiea.ds.sondage.model.Lieu;

import java.util.Date;
import java.util.Optional;

@Repository
public interface DateRendezVousRepository extends JpaRepository<DateRendezVous, Long> {
    Optional<DateRendezVous> findByDate(DateRendezVous date);
}
