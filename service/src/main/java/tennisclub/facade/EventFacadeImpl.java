package tennisclub.facade;

import org.springframework.stereotype.Service;
import tennisclub.dto.event.EventRescheduleDTO;
import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.entity.*;
import tennisclub.exceptions.FacadeLayerException;
import tennisclub.service.CourtService;
import tennisclub.service.EventMappingService;
import tennisclub.service.EventService;

import javax.transaction.Transactional;
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

    private final EventMappingService eventMapper;

    public EventFacadeImpl(EventService eventService,
                           CourtService courtService,
                           EventMappingService eventMapper) {
        this.eventService = eventService;
        this.courtService = courtService;
        this.eventMapper = eventMapper;
    }

    @Override
    public EventWithCourtDTO reschedule(Long id, EventRescheduleDTO eventRescheduleDTO) {
        Event event = eventService.findById(id);
        Court court = event.getCourt();

        List<Event> conflicts = courtService.getConflictingEvents(court, eventRescheduleDTO.getStartTime(), eventRescheduleDTO.getEndTime());
        if (conflicts.isEmpty() || (conflicts.size() == 1 && conflicts.get(0) == event)) {
            Event updated = eventService.reschedule(event, eventRescheduleDTO.getStartTime(), eventRescheduleDTO.getEndTime());
            return eventMapper.map(updated);
        }
        throw new FacadeLayerException("Can't reschedule the event. The court is not free at the new time.");
    }

    @Override
    public EventWithCourtDTO findById(Long id) {
        Event event = eventService.findById(id);
        return eventMapper.map(event);
    }

    @Override
    public List<EventWithCourtDTO> findAll() {
        List<Event> events = eventService.findAll();
        return events.stream()
                .map(eventMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventWithCourtDTO> findByTimeInterval(LocalDateTime from, LocalDateTime to) {
        List<Event> events = eventService.findByTimeInterval(from ,to);
        return events.stream()
                .map(eventMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventWithCourtDTO> findByStartTime(LocalDateTime start) {
        List<Event> events = eventService.findByStartTime(start);
        return events.stream()
                .map(eventMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<EventWithCourtDTO> findByEndTime(LocalDateTime end) {
        List<Event> events = eventService.findByEndTime(end);
        return events.stream()
                .map(eventMapper::map)
                .collect(Collectors.toList());
    }

}
