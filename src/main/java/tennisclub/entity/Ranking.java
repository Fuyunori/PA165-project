package tennisclub.entity;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Ranking {
    @ManyToOne
    private Tournament tournaments;
}
