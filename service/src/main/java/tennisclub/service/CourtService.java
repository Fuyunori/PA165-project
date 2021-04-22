package tennisclub.service;

import tennisclub.entity.Court;
import java.util.List;

public interface CourtService {
    void create(Court court);
    List<Court> listByAddress(String address);
}
