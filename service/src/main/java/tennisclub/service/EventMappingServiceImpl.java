package tennisclub.service;

import com.github.dozermapper.core.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dto.booking.BookingFullDTO;
import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.dto.lesson.LessonFullDTO;
import tennisclub.dto.tournament.TournamentFullDTO;
import tennisclub.entity.Booking;
import tennisclub.entity.Event;
import tennisclub.entity.Lesson;
import tennisclub.entity.Tournament;
import tennisclub.exceptions.ServiceLayerException;

@Service
public class EventMappingServiceImpl implements EventMappingService {

    private final Mapper mapper;

    @Autowired
    public EventMappingServiceImpl(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public EventWithCourtDTO map(Event event) {
        if (event instanceof Booking) {
            return mapper.map(event, BookingFullDTO.class);
        }
        if (event instanceof Lesson) {
            return mapper.map(event, LessonFullDTO.class);
        }
        if (event instanceof Tournament) {
            return mapper.map(event, TournamentFullDTO.class);
        }
        throw new ServiceLayerException("Trying to map invalid event.");
    }
}
