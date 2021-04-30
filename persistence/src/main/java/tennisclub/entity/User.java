package tennisclub.entity;

import tennisclub.enums.Role;
import tennisclub.entity.ranking.Ranking;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;

import java.util.Set;

/**
 * @author Ondrej Holub
 */
@Entity
@Table(name = "user")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private  String username;

    private  String passwordHash;

    @Column(unique = true)
    private String email;

    @Enumerated
    private Role role;

    @ManyToMany(mappedBy = "users")
    private Set<Booking> bookings = new HashSet<>();

    @OneToMany(mappedBy = "author")
    private Set<Booking> authoredBookings = new HashSet<>();

    @ManyToMany(mappedBy = "teachers")
    private Set<Lesson>  lessonsToTeach = new HashSet<>();

    @ManyToMany(mappedBy = "students")
    private Set<Lesson> lessonsToAttend = new HashSet<>();

    @OneToMany(mappedBy = "player")
    private Set<Ranking> rankings = new HashSet<>();

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }


    public Set<Booking> getBookings() { return Collections.unmodifiableSet(bookings); }

    public void addBooking(Booking booking) {
        bookings.add(booking);
        booking.addUser(this);
    }

    public void removeBooking(Booking booking) {
        bookings.remove(booking);
        booking.removeUser(this);
    }

    public Set<Booking> getAuthoredBookings() {
        return Collections.unmodifiableSet(authoredBookings);
    }

    public void addAuthoredBooking(Booking booking) {
        authoredBookings.add(booking);
        booking.setAuthor(this);
    }

    public Set<Lesson> getLessonsToTeach() { return Collections.unmodifiableSet(lessonsToTeach); }

    public void addLessonToTeach(Lesson lesson) {
        lessonsToTeach.add(lesson);
        lesson.addTeacher(this);
    }

    public void removeLessonToTeach(Lesson lesson) {
        lessonsToTeach.remove(lesson);
        lesson.removeTeacher(this);
    }

    public Set<Lesson> getLessonsToAttend() { return Collections.unmodifiableSet(lessonsToAttend); }

    public void addLessonToAttend(Lesson lesson) {
        lessonsToAttend.add(lesson);
        lesson.addStudent(this);
    }

    public void removeLessonToAttend(Lesson lesson) {
        lessonsToAttend.remove(lesson);
        lesson.removeStudent(this);
    }

    public Set<Ranking> getRankings() { return Collections.unmodifiableSet(rankings); }

    public void addRanking(Ranking ranking) {
        rankings.add(ranking);
    }

    public void removeRanking(Ranking ranking) {
        rankings.remove(ranking);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User otherUser = (User) o;
        String username = getUsername();
        return username != null && username.equals(otherUser.getUsername());
    }

    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }

}
