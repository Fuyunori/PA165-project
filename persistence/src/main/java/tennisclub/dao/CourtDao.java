package tennisclub.dao;

import tennisclub.entity.Court;
import tennisclub.enums.CourtType;

import java.util.List;

/**
 * DAO interface for basic CRUD operations on the Court entity.
 * @author Pavel Tobias
 */
public interface CourtDao {
    /**
     * Persists a court in the database.
     * @param court a court in transient state
     */
    void create(Court court);

    /**
     * Deletes a court from the database.
     * @param court a court in managed or detached state
     */
    void delete(Court court);

    /**
     * Finds a court with a given primary key.
     * @param id the primary key of some persistent court
     * @return the found court
     */
    Court findById(Long id);

    /**
     * Finds every court such that {@code addressSubstr} is a substring of its address.
     * @param addressSubstr the address substring to match
     * @return a list of all matching courts
     */
    List<Court> findByAddress(String addressSubstr);

    /**
     * Finds every court of a given type (grass, turf, ...).
     * @param type the type to match
     * @return a list of all matching courts
     */
    List<Court> findByType(CourtType type);

    /**
     * Merges a detached court into the current persistence context and updates its fields in the database.
     * @param court A court in detached state.
     * @return a managed reference to the same court
     */
    Court update(Court court);
}
