package tennisclub.facade;

import tennisclub.dto.court.CourtCreateDto;
import tennisclub.dto.court.CourtDto;
import tennisclub.enums.CourtType;
import java.util.List;

/**
 * Facade for court manipulation
 * @author Pavel Tobiáš
 */
public interface CourtFacade {
    /**
     * Adds a new court
     * @param court the court to add
     */
    CourtDto create(CourtCreateDto court);

    /**
     * Updates court information
     * @param court the court to update
     * @return the updated court
     */
    CourtDto update(CourtDto court);

    /**
     * Deletes a court
     * @param court the court to delete
     */
    void delete(CourtDto court);

    /**
     * Retrieve a court with a given id
     * @param id the id of the court to be retrieved
     * @return the retrieved court
     */
    CourtDto getById(Long id);

    /**
     * Lists all courts such that {@code address} is a substring of their addresses
     * @param address address substring to match
     * @return a list of all matching courts
     */
    List<CourtDto> listByAddress(String address);

    /**
     * Lists all courts of a given type (grass, turf, ...)
     * @param type the type to match
     * @return a list of all matching courts
     */
    List<CourtDto> listByType(CourtType type);
}