package tennisclub.entity.ranking;

import tennisclub.entity.Tournament;
import tennisclub.entity.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * @author Ondrej Holub
 */
@Entity
@IdClass(RankingId.class)
public class Ranking {

    @ManyToOne
    @NotNull
    @Id
    private Tournament tournament;

    @ManyToOne
    @NotNull
    @Id
    private User user;

    private int playerPlacement;

    public  Ranking() { }

    public Ranking(Tournament tournament, User user) {
        this.tournament = tournament;
        this.user = user;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament){
        this.tournament = tournament;
    }

    public User getUser() {
        return user;
    }

    public void setPlayerPlacement(int playerPlacement) {
        this.playerPlacement = playerPlacement;
    }

    public int getPlayerPlacement() {
        return playerPlacement;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ranking)) return false;

        Ranking ranking = (Ranking) o;

        if (!getTournament().equals(ranking.getTournament())) return false;
        return getUser().equals(ranking.getUser());
    }

    @Override
    public int hashCode() {
        int result = getTournament().hashCode();
        result = 31 * result + getUser().hashCode();
        return result;
    }
}


