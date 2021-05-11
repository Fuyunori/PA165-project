package tennisclub.facade;

import com.github.dozermapper.core.Mapper;
import org.springframework.stereotype.Service;
import tennisclub.dto.booking.BookingFullDTO;
import tennisclub.dto.event.EventRescheduleDTO;
import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.dto.lesson.LessonFullDTO;
import tennisclub.dto.lesson.LessonWithCourtDTO;
import tennisclub.dto.tournament.TournamentFullDTO;
import tennisclub.entity.*;
import tennisclub.exceptions.FacadeLayerException;
import tennisclub.exceptions.ServiceLayerException;
import tennisclub.service.CourtService;
import tennisclub.service.EventService;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Miroslav Demek
 */
@Service
@Transactional
public class EventFacadeImpl implements EventFacade {

    private final EventService eventService;
    private final CourtService courtService;

    private final Mapper mapper;

    public EventFacadeImpl(EventService eventService, CourtService courtService, Mapper mapper) {
        this.eventService = eventService;
        this.courtService = courtService;
        this.mapper = mapper;
    }

    @Override
    public void reschedule(EventRescheduleDTO eventRescheduleDTO) {
        Event event = eventService.findById(eventRescheduleDTO.getId());
        Court court = event.getCourt();

        if (!courtService.isFree(court, eventRescheduleDTO.getStart(), eventRescheduleDTO.getEnd())) {
            throw new FacadeLayerException("Can't reschedule the event. The court is not free at the new time.");
        }

        eventService.reschedule(event, eventRescheduleDTO.getStart(), eventRescheduleDTO.getEnd());
    }

    @Transactional(readOnly = true)
    @Override
    public EventWithCourtDTO findById(Long id) {
        Event event = eventService.findById(id);
        return map(event);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventWithCourtDTO> findAll() {
        List<Event> events = eventService.findAll();
        return events.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventWithCourtDTO> findByTimeInterval(LocalDateTime from, LocalDateTime to) {
        List<Event> events = eventService.findByTimeInterval(from ,to);
        return events.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventWithCourtDTO> findByStartTime(LocalDateTime start) {
        List<Event> events = eventService.findByStartTime(start);
        return events.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventWithCourtDTO> findByEndTime(LocalDateTime end) {
        List<Event> events = eventService.findByEndTime(end);
        return events.stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    
    private EventWithCourtDTO map(Event event) {
        if (event instanceof Booking) {
            return mapper.map(event, BookingFullDTO.class);
        }
        if (event instanceof Lesson) {
            return mapper.map(event, LessonFullDTO.class);
        }
        if (event instanceof Tournament) {
            return mapper.map(event, TournamentFullDTO.class);
        }
        throw new FacadeLayerException("Trying to map invalid event.");
    }
}
