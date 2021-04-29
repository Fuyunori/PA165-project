package tennisclub.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class TournamentDTO {
    private String name;
    private Integer capacity;
    private Integer prize;
    // TODO: uncomment once the RankingDTO is available :)
    /* From RankingDTO side there shouldn't be reference to TournamentDTO
    since I don't expect to see RankingDTO separately (i.e., not inside Tournament - the relationship is strong)
    From the User side, it is meaningless to have a set of Rankings, since
    the user would be missing information about what tournament he ranked in
    (we would have to add a tournament name to make it meaningful).
     */
    // private Set<RankingDTO> playerPlacements = new HashSet<>();

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
}
