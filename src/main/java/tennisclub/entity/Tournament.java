package tennisclub.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Xuan Linh PHamová
 */
@Entity
public class Tournament extends Event  {
    @NotNull
    private int capacity;

    @NotNull
    private int prize;

    @OneToMany
    private Set<Ranking> rankings = new HashSet<>();

    public Tournament(){}

    public Tournament(LocalDateTime startTime, LocalDateTime endTime, int capacity, int prize) {
        super(startTime, endTime);
        this.capacity = capacity;
        this.prize = prize;
    }


    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public Set<Ranking> getRankings(){
        return Collections.unmodifiableSet(rankings);
    }

    public void addRanking(Ranking ranking){
        rankings.add(ranking);
        // TODO: uncomment this code where User entity is finished
        // user.setTournament(this);
    }
}
