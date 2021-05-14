package tennisclub.facade;

import tennisclub.dto.court.CourtCreateDto;
import tennisclub.dto.court.CourtDto;
import tennisclub.dto.court.CourtUpdateDto;
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
     * @param id the id of the court to update
     * @param updateDto a DTO carrying the values to update the court's attributes with
     * @return the updated court
     */
    CourtDto update(Long id, CourtUpdateDto updateDto);

    /**
     * Deletes a court
     * @param id the id of the court to delete
     */
    void delete(Long id);

    /**
     * Retrieve a court with a given id
     * @param id the id of the court to be retrieved
     * @return the retrieved court
     */
    CourtDto getById(Long id);

    /**
     * Lists all courts
     * @return a list of all matching courts
     */
    List<CourtDto> listAll();

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