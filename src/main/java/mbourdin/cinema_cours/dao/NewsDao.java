package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.NewsLetter;
import org.springframework.data.repository.CrudRepository;

public interface NewsDao extends CrudRepository<NewsLetter,Integer> {
}
