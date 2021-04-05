package tennisclub.dao;

import tennisclub.entity.Court;
import tennisclub.entity.CourtType;

import java.util.List;

public interface CourtDao {
    void create(Court court);
    void delete(Court court);
    Court findById(Long id);
    List<Court> findByAddress(String address);
    List<Court> findByType(CourtType type);
}
