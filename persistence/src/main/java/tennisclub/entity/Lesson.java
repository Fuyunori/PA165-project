package tennisclub.entity;

import tennisclub.entity.enums.Level;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Xuan Linh Phamová
 */
@Entity
public class Lesson extends Event {
    private Integer capacity;

    @Enumerated
    @NotNull
    private Level level;

    @ManyToMany
    private Set<User> teachers = new HashSet<>();

    @ManyToMany
    private Set<User> students = new HashSet<>();

    public Lesson(){}

    public Lesson(LocalDateTime startTime, LocalDateTime endTime, Level level) {
        super(startTime, endTime);
        this.level = level;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public Set<User> getTeachers(){
        return Collections.unmodifiableSet(teachers);
    }

    public Set<User> getStudents(){
        return Collections.unmodifiableSet(students);
    }

    public void addTeacher(User teacher){
        teachers.add(teacher);
    }

    public void addStudent(User student){
        students.add(student);
    }

    public void removeTeacher(User teacher){
        teachers.remove(teacher);
    }

    public void removeStudent(User student){
        students.remove(student);
    }
}