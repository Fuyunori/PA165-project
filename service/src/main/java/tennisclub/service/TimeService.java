package tennisclub.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface TimeService {

    LocalDate getCurrentDate();

    LocalDateTime getCurrentDateTime();

    LocalTime getCurrentTime();

    Instant getCurrentInstant();
}
