package tennisclub.dao;

import tennisclub.entity.Court;

import java.util.List;

public interface CourtDao {
    void create(Court court);
    List<Court> findByLocation(String location);
}
