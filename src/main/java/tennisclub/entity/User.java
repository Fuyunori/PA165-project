package tennisclub.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToMany
    private Set<Lesson>  lessonsToTeach = new HashSet<>();

    @ManyToMany
    private Set<Lesson> lessonsToAttend = new HashSet<>();
}
