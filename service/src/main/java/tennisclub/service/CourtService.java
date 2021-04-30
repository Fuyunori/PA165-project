package tennisclub.service;

import org.springframework.stereotype.Service;
import tennisclub.entity.Court;
import tennisclub.enums.CourtType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Pavel Tobiáš
 */
@Service
public interface CourtService {
    void create(Court court);
    Court update(Court court);
    void delete(Court court);
    Court getById(Long id);
    List<Court> listByAddress(String addressSubstr);
    List<Court> listByType(CourtType type);
    boolean isFree(Court court, LocalDateTime from, LocalDateTime to);
}
