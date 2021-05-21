package tennisclub.sampledata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import tennisclub.dao.UserDao;
import tennisclub.entity.*;
import tennisclub.enums.CourtType;
import tennisclub.enums.Level;
import tennisclub.enums.Role;
import tennisclub.service.*;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class SampleDataLoader implements ApplicationRunner {
    private final UserService userService;
    private final UserDao userDao;
    private final CourtService courtService;
    private final BookingService bookingService;
    private final LessonService lessonService;
    private final TournamentService tournamentService;

    private final TimeService timeService;

    @Autowired
    public SampleDataLoader(UserService userService,
                            UserDao userDao,
                            CourtService courtService,
                            BookingService bookingService,
                            LessonService lessonService,
                            TournamentService tournamentService,
                            TimeService timeService) {
        this.userService = userService;
        this.userDao = userDao;
        this.courtService = courtService;
        this.bookingService = bookingService;
        this.lessonService = lessonService;
        this.tournamentService = tournamentService;
        this.timeService = timeService;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        loadData();
    }

    public void loadData() {
        User admin = persistUser("Admin Adminy", "admin", "admin", "admin@gmail.com", Role.MANAGER);
        admin.setRole(Role.MANAGER);
        userDao.update(admin);
        User user1 = persistUser("Bob Smith", "Bobby123", "password", "bob@gmail.com", Role.USER);
        User user2 = persistUser("Mark Tennisy", "TennisDevil666", "password", "mark@gmail.com", Role.USER);
        User user3 = persistUser("Lucy Fast", "lussy", "passwod", "lucy@gmail.com", Role.USER);

        Court court1 = persistCourt("Pretty nice court", "Brno, Czech Republic", CourtType.GRASS);
        Court court2 = persistCourt("Courty court", "Prague, Czech Republic", CourtType.TURF);

        LocalDate today = timeService.getCurrentDate();
        LocalDate tomorrow = today.plusDays(1);
        LocalDate dayAfterTomorrow = tomorrow.plusDays(2);

        persistBooking(court1, addTime(tomorrow, 10, 30), addTime(tomorrow, 11, 30),
                user1, List.of(user2));
        persistBooking(court1, addTime(dayAfterTomorrow, 14, 0), addTime(dayAfterTomorrow, 16, 0),
                user3, List.of(user2, user1));
        persistBooking(court2, addTime(tomorrow, 12, 30), addTime(tomorrow, 14, 0),
                user2, List.of(user3));
        persistBooking(court2, addTime(dayAfterTomorrow, 10, 30), addTime(dayAfterTomorrow, 11, 30),
                user1, List.of(user2, user3));

        persistLesson(court1, addTime(tomorrow, 12, 0), addTime(tomorrow, 14, 30),
                4, Level.ADVANCED, List.of(user1), List.of(admin));
        persistLesson(court2, addTime(tomorrow, 8, 0), addTime(tomorrow, 10, 0),
                2, Level.ADVANCED, List.of(user3), List.of(admin, user1));

        persistTournament(court1, addTime(tomorrow, 16, 0), addTime(tomorrow, 20, 0),
                "Fun tournament for funny players", 6, 50, List.of(user1, user2));
        persistTournament(court2, addTime(tomorrow, 15, 0), addTime(tomorrow, 21, 0),
                "Tennis championship", 3, 50_000, List.of(user1, user3, admin));
    }

    private User persistUser(String name, String username, String password, String email, Role role) {
        User user = new User();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setRole(role);
        return userService.register(user, password);
    }

    private Court persistCourt(String name, String address, CourtType type) {
        Court court = new Court();
        court.setName(name);
        court.setAddress(address);
        court.setType(type);
        courtService.create(court);
        return court;
    }

    private Booking persistBooking(Court court,
                                   LocalDateTime start,
                                   LocalDateTime end,
                                   User author,
                                   List<User> bookees) {
        Booking booking = new Booking(start, end);
        booking.setCourt(court);
        booking.setAuthor(author);
        for (User u : bookees) {
            booking.addUser(u);
        }
        return bookingService.create(booking);
    }

    private Lesson persistLesson(Court court,
                                 LocalDateTime start,
                                 LocalDateTime end,
                                 Integer capacity,
                                 Level level,
                                 List<User> teachers,
                                 List<User> students) {
        Lesson lesson = new Lesson(start, end, level);
        lesson.setCourt(court);
        lesson.setCapacity(capacity);
        lessonService.create(lesson);
        for (User t : teachers) {
            lessonService.addTeacher(lesson, t);
        }
        for (User s : students) {
            lessonService.enrollStudent(lesson, s);
        }
        return lesson;
    }

    private Tournament persistTournament(Court court,
                                         LocalDateTime start,
                                         LocalDateTime end,
                                         String name,
                                         Integer capacity,
                                         Integer prize,
                                         List<User> players) {
        Tournament tournament = new Tournament(start, end, name, capacity, prize);
        tournament.setCourt(court);
        tournamentService.create(tournament);
        for (User p : players) {
            tournamentService.enrollPlayer(tournament, p);
        }
        return tournament;
    }

    private LocalDateTime addTime(LocalDate date, int hour, int minute) {
        return LocalDateTime.of(date, LocalTime.of(hour, minute));
    }
}
