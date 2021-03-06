package tennisclub.entity.ranking;

import tennisclub.entity.Tournament;
import tennisclub.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Ondrej Holub
 */
@Entity
@Table(name = "ranking")
@IdClass(RankingId.class)
public class Ranking implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @Id
    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private User player;

    private Integer playerPlacement;

    protected Ranking() { }

    public Ranking(Tournament tournament, User user) {
        this.tournament = tournament;
        this.player = user;
        user.addRanking(this);
        tournament.addRanking(this);
    }

    public Tournament getTournament() {
        return tournament;
    }

    public User getPlayer() {
        return player;
    }
  
    public void setTournament(Tournament tournament){
        this.tournament = tournament;
    }

    public void setPlayerPlacement(Integer playerPlacement) {
        this.playerPlacement = playerPlacement;
    }

    public Integer getPlayerPlacement() {
        return playerPlacement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ranking)) return false;
        Ranking ranking = (Ranking) o;
        return getTournament() != null && getPlayer() != null &&
                getTournament().equals(ranking.getTournament()) &&
                getPlayer().equals(ranking.getPlayer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTournament(), getPlayer());
    }
}


