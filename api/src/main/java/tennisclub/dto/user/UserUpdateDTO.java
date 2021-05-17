package tennisclub.dto.user;

import tennisclub.dto.booking.BookingWithCourtDTO;
import tennisclub.dto.lesson.LessonWithCourtDTO;
import tennisclub.dto.ranking.RankingWithTournamentDTO;
import tennisclub.enums.Role;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * User DTO for updates
 * @author Ondrej Holub
 */
public class UserUpdateDTO {

    @NotBlank
    private String username;

    private String name;

    private String email;

    private Role role;

    private Set<LessonWithCourtDTO> lessonsToTeach = new HashSet<>();

    private Set<LessonWithCourtDTO> lessonsToAttendTo = new HashSet<>();

    private Set<BookingWithCourtDTO> bookings = new HashSet<>();

    private Set<RankingWithTournamentDTO> rankings = new HashSet<>();

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public Role getRole() { return role; }

    public void setRole(Role role) { this.role = role; }

    public Set<LessonWithCourtDTO> getLessonsToTeach() {
        return lessonsToTeach;
    }

    public void setLessonsToTeach(Set<LessonWithCourtDTO> lessonsToTeach) {
        this.lessonsToTeach = lessonsToTeach;
    }

    public Set<LessonWithCourtDTO> getLessonsToAttendTo() {
        return lessonsToAttendTo;
    }

    public void setLessonsToAttendTo(Set<LessonWithCourtDTO> lessonsToAttendTo) {
        this.lessonsToAttendTo = lessonsToAttendTo;
    }

    public Set<BookingWithCourtDTO> getBookings() {
        return bookings;
    }

    public void setBookings(Set<BookingWithCourtDTO> bookings) {
        this.bookings = bookings;
    }

    public Set<RankingWithTournamentDTO> getRankings() {
        return rankings;
    }

    public void setRankings(Set<RankingWithTournamentDTO> rankings) {
        this.rankings = rankings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserUpdateDTO)) return false;

        UserUpdateDTO userUpdateDTO = (UserUpdateDTO) o;

        return getUsername() != null ? getUsername().equals(userUpdateDTO.getUsername()) : userUpdateDTO.getUsername() == null;
    }

    @Override
    public int hashCode() {
        return getUsername() != null ? getUsername().hashCode() : 0;
    }

}
