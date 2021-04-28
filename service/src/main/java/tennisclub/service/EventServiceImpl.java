package tennisclub.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tennisclub.dao.EventDao;
import tennisclub.entity.Event;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    final private EventDao eventDao;

    @Autowired
    public EventServiceImpl(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    @Override
    public Event reschedule(Event event, LocalDateTime newStart, LocalDateTime newEnd) {
        event.setStartTime(newStart);
        event.setEndTime(newEnd);
        return eventDao.update(event);
    }

    @Override
    public Event findById(Long id) {
        return eventDao.findById(id);
    }

    @Override
    public List<Event> findAll() {
        return eventDao.findAll();
    }

    @Override
    public List<Event> findByTimeInterval(LocalDateTime from, LocalDateTime to) {
        return eventDao.findByTimeInterval(from, to);
    }

    @Override
    public List<Event> findByStartTime(LocalDateTime start) {
        return eventDao.findByStartTime(start);
    }

    @Override
    public List<Event> findByEndTime(LocalDateTime end) {
        return eventDao.findByEndTime(end);
    }
}
