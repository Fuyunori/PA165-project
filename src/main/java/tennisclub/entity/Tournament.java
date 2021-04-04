package tennisclub.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author Xuan Linh PHamová
 */
@Entity
public class Tournament extends Event  {
    private int capacity;
    private int prize;

    @ManyToOne
    private Ranking ranking;

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

    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }
}
