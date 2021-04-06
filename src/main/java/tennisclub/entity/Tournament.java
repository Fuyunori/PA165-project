package tennisclub.entity;

import tennisclub.entity.ranking.Ranking;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Xuan Linh PHamová
 */
@Entity
public class Tournament extends Event  {
    private Integer capacity;

    @NotNull
    private Integer prize;

    @OneToMany(mappedBy = "tournament")
    private Set<Ranking> rankings = new HashSet<>();

    public Tournament(){}

    public Tournament(LocalDateTime startTime, LocalDateTime endTime, int capacity, int prize) {
        super(startTime, endTime);
        this.capacity = capacity;
        this.prize = prize;
    }


    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Integer getPrize() {
        return prize;
    }

    public void setPrize(Integer prize) {
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
