package tennisclub.entity.ranking;

import tennisclub.entity.Tournament;
import tennisclub.entity.User;

import java.io.Serializable;

/**
 * @author Ondrej Holub
 */
public class RankingId implements Serializable {

    private Tournament tournament;

    private User player;

    protected RankingId() {

    }

    public RankingId(Tournament tournament, User player) {
        this.tournament = tournament;
        this.player = player;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public User getPlayer() {
        return player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ranking)) return false;

        Ranking ranking = (Ranking) o;

        if (!getTournament().equals(ranking.getTournament())) return false;
        return getPlayer().equals(ranking.getPlayer());
    }

    @Override
    public int hashCode() {
        int result = getTournament().hashCode();
        result = 31 * result + getPlayer().hashCode();
        return result;
    }


}
