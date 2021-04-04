package tennisclub.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Ranking {
    @OneToMany
    private Set<Tournament> tournaments = new HashSet<>();
}
