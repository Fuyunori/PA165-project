package tennisclub.dto.tournament;

import tennisclub.dto.event.EventDTO;

/**
 * @author Xuan Linh Phamová
 */
public class TournamentDTO extends EventDTO {
    private String name;
    private Integer capacity;
    private Integer prize;

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
}
