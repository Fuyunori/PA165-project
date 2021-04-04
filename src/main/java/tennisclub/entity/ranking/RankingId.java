package tennisclub.entity.ranking;

import tennisclub.entity.Tournament;
import tennisclub.entity.User;

import java.io.Serializable;

public class RankingId implements Serializable {

    private Tournament tournament;

    private User user;

    public RankingId() {

    }

    public RankingId(Tournament tournament, User user) {
        this.tournament = tournament;
        this.user = user;
    }
}
