package tennisclub.entity;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tournament)) return false;
        if (!super.equals(o)) return false;

        Tournament tournament = (Tournament) o;

        return getCapacity().equals(tournament.getCapacity())
                && getPrize().equals(tournament.getPrize())
                && Objects.equals(getRankings(), tournament.getRankings());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCapacity(), getPrize(), getRankings());
    }
}
