package tennisclub.dao;

import tennisclub.entity.Tournament;

import java.util.List;

/**
 * @author Xuan Linh Phamová
 */
public interface TournamentDao {
    void create(Tournament tournament);
    void update(Tournament tournament);
    void remove(Tournament tournament);

    List<Tournament> findAll();
    Tournament findById(Long id);
    List<Tournament> findByCapacity(int capacity);
    List<Tournament> findByPrize(int prize);
}
