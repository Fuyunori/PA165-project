package tennisclub.entity.ranking;

import tennisclub.entity.Tournament;
import tennisclub.entity.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ranking ranking = (Ranking) o;
        return tournament.equals(ranking.tournament) && user.equals(ranking.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tournament, user);
    }

    public Tournament getTournament() {
        return tournament;
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
}


