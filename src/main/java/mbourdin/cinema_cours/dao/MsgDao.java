package mbourdin.cinema_cours.dao;

import mbourdin.cinema_cours.model.MsgToAdmin;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface MsgDao extends CrudRepository<MsgToAdmin,Integer> {
    Set<MsgToAdmin> findAllByLuFalse();
}
