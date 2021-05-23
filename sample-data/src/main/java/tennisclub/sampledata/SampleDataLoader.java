package tennisclub.sampledata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import tennisclub.dao.LessonDao;
import tennisclub.dao.RankingDao;
import tennisclub.dao.TournamentDao;
import tennisclub.dao.UserDao;
import tennisclub.entity.*;
import tennisclub.entity.ranking.Ranking;
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
    private final LessonDao lessonDao;
    private final TournamentService tournamentService;
    private final TournamentDao tournamentDao;
    private final RankingDao rankingDao;

    private final TimeService timeService;

    @Autowired
    public SampleDataLoader(UserService userService,
                            UserDao userDao,
                            CourtService courtService,
                            BookingService bookingService,
                            LessonService lessonService,
                            LessonDao lessonDao, TournamentService tournamentService,
                            TournamentDao tournamentDao, RankingDao rankingDao, TimeService timeService) {
        this.userService = userService;
        this.userDao = userDao;
        this.courtService = courtService;
        this.bookingService = bookingService;
        this.lessonService = lessonService;
        this.lessonDao = lessonDao;
        this.tournamentService = tournamentService;
        this.tournamentDao = tournamentDao;
        this.rankingDao = rankingDao;
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
        User user4 = persistUser("User1 Smith", "user1", "123", "user1@gmail.com", Role.USER);
        User user5 = persistUser("User2 Smith", "user2", "123", "user2@gmail.com", Role.USER);
        User user6 = persistUser("User3 Smith", "user3", "123", "user3@gmail.com", Role.USER);
        User user7 = persistUser("User4 Smith", "user4", "123", "user4@gmail.com", Role.USER);
        User user8 = persistUser("Human Nocat", "notacat", "123", "hooman@gmail.com", Role.USER);


        Court court1 = persistCourt("Pretty nice court", "Brno, Czech Republic", CourtType.GRASS);
        court1.setPreviewImageUrl("https://images.unsplash.com/photo-1567220720374-a67f33b2a6b9?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=2089&q=80");
        Court court2 = persistCourt("Courty court", "Prague, Czech Republic", CourtType.TURF);
        Court court3 = persistCourt("Cat court", "Cat Nation", CourtType.GRASS);
        court3.setPreviewImageUrl("https://images.unsplash.com/photo-1526336024174-e58f5cdd8e13?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=934&q=80");


        LocalDate today = timeService.getCurrentDate();
        LocalDate yesterday = today.minusDays(1);
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

        persistBooking(court1, addTime(tomorrow.minusDays(2), 10, 30), addTime(tomorrow.minusDays(2), 11, 30),
                user1, List.of(user2));
        persistBooking(court1, addTime(tomorrow.minusDays(3), 10, 30), addTime(tomorrow.minusDays(3), 11, 30),
                user1, List.of(user2));
        persistBooking(court1, addTime(tomorrow.minusDays(21), 10, 30), addTime(tomorrow.minusDays(21), 11, 30),
                user1, List.of(user2));
        persistBooking(court1, addTime(tomorrow.minusDays(24), 10, 30), addTime(tomorrow.minusDays(24), 11, 30),
                user1, List.of(user2));
        persistBooking(court1, addTime(tomorrow.minusDays(50), 10, 30), addTime(tomorrow.minusDays(50), 11, 30),
                user1, List.of(user2));
        persistBooking(court1, addTime(tomorrow.minusDays(100), 10, 30), addTime(tomorrow.minusDays(100), 11, 30),
                user1, List.of(user2));

        persistLesson(court1, addTime(tomorrow, 12, 0), addTime(tomorrow, 14, 30),
                4, Level.ADVANCED, List.of(user1), List.of(admin));
        persistLesson(court2, addTime(tomorrow, 8, 0), addTime(tomorrow, 10, 0),
                2, Level.ADVANCED, List.of(user3), List.of(admin, user1));
        persistLesson(court3, addTime(yesterday, 7, 25), addTime(tomorrow, 8, 30),
                2, Level.ADVANCED, List.of(user8), List.of(user3, user4));

        persistTournament(court1, addTime(tomorrow, 16, 0), addTime(tomorrow, 20, 0),
                "Fun tournament for funny players", 6, 50, List.of(user1, user2));
        persistTournament(court2, addTime(tomorrow, 15, 0), addTime(tomorrow, 21, 0),
                "Tennis championship", 3, 50_000, List.of(user1, user3, admin));

        Tournament t = persistTournament(court3, addTime(yesterday, 10, 0), addTime(yesterday, 20, 0),
                "Cat championship", 4, 10_000, List.of(user4, user5, user6, user8));
        tournamentService.rankPlayer(t, user4, 3);
        tournamentService.rankPlayer(t, user5, 2);
        tournamentService.rankPlayer(t, user6, 4);
        tournamentService.rankPlayer(t, user8, 1);

        persistTournament(court2, addTime(yesterday, 10, 0), addTime(yesterday, 20, 0),
                "Turnajik", 4, 10_000, List.of(user3, user4, user6, user7));
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
            lesson.addTeacher(t);
        }
        for (User s : students) {
            lesson.addStudent(s);
        }
        return lessonService.update(lesson);
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
            Ranking r = new Ranking(tournament, p);
            rankingDao.create(r);
        }
        return tournamentService.update(tournament);
    }

    private LocalDateTime addTime(LocalDate date, int hour, int minute) {
        return LocalDateTime.of(date, LocalTime.of(hour, minute));
    }
}
