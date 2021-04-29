package tennisclub.dto.tournament;

import java.time.LocalDateTime;
import java.util.Objects;

public class TournamentDTO {
    private Long id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TournamentDTO tournamentDTO = (TournamentDTO) o;
        return Objects.equals(startTime, tournamentDTO.getStartTime())
                && Objects.equals(endTime, tournamentDTO.getEndTime());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result  + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result  + ((endTime == null) ? 0 : endTime.hashCode());
        return result;
    }
}
