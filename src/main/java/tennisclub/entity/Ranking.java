package tennisclub.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Ranking {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Tournament tournaments;
}
