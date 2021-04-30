package tennisclub.dto.lesson;

import tennisclub.dto.event.EventWithCourtDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.enums.Level;

import java.util.Set;

public class LessonFullDTO extends EventWithCourtDTO {
    private Integer capacity;
    private Level level;
    private Set<UserDTO> teachers;
    private Set<UserDTO> students;

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Set<UserDTO> getTeachers() {
        return teachers;
    }

    public void setTeachers(Set<UserDTO> teachers) {
        this.teachers = teachers;
    }

    public Set<UserDTO> getStudents() {
        return students;
    }

    public void setStudents(Set<UserDTO> students) {
        this.students = students;
    }
}
