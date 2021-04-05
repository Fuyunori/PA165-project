package tennisclub.entity;

import tennisclub.entity.enums.Role;
import tennisclub.entity.ranking.Ranking;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String name;

    private  String username;

    private  String passwordHash;

    private String email;

    @Enumerated
    private Role role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean verifyPasswordHash(String hash) {
        return hash.equals(passwordHash);
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }



    @ManyToMany
    private Set<Booking> bookings = new HashSet<>();

//    @ManyToMany
//    private Set<Lesson>  lessonsToTeach = new HashSet<>();

//    @ManyToMany
//    private Set<Lesson> lessonsToAttend = new HashSet<>();

    @OneToMany
    private Set<Ranking> rankings = new HashSet<>();

}
