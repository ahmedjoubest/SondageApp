package web.sondage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import web.sondage.model.Lieu;
import web.sondage.model.Sondage;

import java.util.Optional;

@Repository
public interface LieuRepository extends JpaRepository<Lieu, Long> {
    Optional<Lieu> findByNom(String nom);
    @Query("from Lieu as l where l.id in ( select MAX(id)  FROM Lieu ) ")
    Lieu findByMaxId();
}
