package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.Salle;
import mbourdin.cinema_cours.model.Seance;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SeanceDao extends CrudRepository<Seance,Long> {
    List<Seance> getAllByDebutIsGreaterThanEqualAndDebutIsLessThanEqual(LocalDateTime datemin, LocalDateTime datemax);
    List<Seance> getAllByDebutIsBeforeAndDebutIsAfterAndSalle(LocalDate date1,LocalDate date2, Salle salle);
}
