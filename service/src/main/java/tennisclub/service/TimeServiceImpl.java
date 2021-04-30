package tennisclub.service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeServiceImpl implements TimeService {

    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    @Override
    public LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

    @Override
    public LocalTime getCurrentTime() {
        return LocalTime.now();
    }

    @Override
    public Instant getCurrentInstant() {
        return Instant.now();
    }
}
