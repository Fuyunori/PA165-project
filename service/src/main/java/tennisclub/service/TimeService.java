package tennisclub.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public interface TimeService {

    LocalDate getCurrentDate();

    LocalDateTime getCurrentDateTime();

    LocalTime getCurrentTime();

    Instant getCurrentInstant();
}
