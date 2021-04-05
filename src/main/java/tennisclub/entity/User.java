package tennisclub.entity;

import tennisclub.entity.enums.Role;
import tennisclub.entity.ranking.Ranking;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;

import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    private String name;

    @NotNull
    @Column(unique = true)
    private  String username;

    private  String passwordHash;

    private String email;

    @Enumerated
    private Role role;

    public Long getId() { return id; }

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



    @ManyToMany(mappedBy = "users")
    private Set<Booking> bookings = new HashSet<>();

    @ManyToMany
    private Set<Lesson>  lessonsToTeach = new HashSet<>();

    @ManyToMany
    private Set<Lesson> lessonsToAttend = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Ranking> rankings = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        return getUsername().equals(user.getUsername());
    }

    @Override
    public int hashCode() {
        return getUsername().hashCode();
    }
}
