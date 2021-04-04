package tennisclub.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

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

    public int getPlayerPlacement() {
        return playerPlacement;
    }

    public  Ranking() {

    }

    public Ranking(Tournament tournament, User user) {
        this.tournament = tournament;
        this.user = user;
    }

    public void setPlayerPlacement(int playerPlacement) {
        this.playerPlacement = playerPlacement;
    }

}


