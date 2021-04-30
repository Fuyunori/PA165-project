package tennisclub.entity;

import tennisclub.entity.ranking.Ranking;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Xuan Linh Phamová
 */
@Entity
public class Tournament extends Event  {
    @Column(nullable = false)
    private String name;

    private Integer capacity;

    @Column(nullable = false)
    private Integer prize;

    @OneToMany(mappedBy = "tournament")
    private Set<Ranking> rankings = new HashSet<>();

    public Tournament(){}

    public Tournament(LocalDateTime startTime, LocalDateTime endTime, String name, int capacity, int prize) {
        super(startTime, endTime);
        this.name = name;
        this.capacity = capacity;
        this.prize = prize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        ranking.setTournament(this);
    }

    public void removeRanking(Ranking ranking) {
        rankings.remove(ranking);
    }
}
