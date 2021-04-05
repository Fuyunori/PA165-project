package tennisclub.entity;

import tennisclub.entity.enums.Level;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;

/**
 * @author Xuan Linh Phamová
 */
@Entity
public class Lesson extends Event{
    private int capacity;

    @Enumerated
    private Level level;

    @ManyToMany(mappedBy = "lessonsToTeach")
    private Set<User> teachers;

    @ManyToMany(mappedBy = "lessonsToAttend")
    private Set<User> students;

    public Lesson(){}

    public Lesson(LocalDateTime startTime, LocalDateTime endTime, Level level, int capacity) {
        super(startTime, endTime);
        this.level = level;
        this.capacity = capacity;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
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
        // TODO: uncomment this code where User entity is finished
        // user.addLesson(this);
    }

    public void addStudent(User student){
        students.add(student);
        // TODO: uncomment this code where User entity is finished
        // user.addLesson(this);
    }

    public void removeTeacher(User teacher){
        teachers.remove(teacher);
        // TODO: uncomment this code where User entity is finished
        // user.removeLesson(this);
    }

    public void removeStudent(User student){
        students.remove(student);
        // TODO: uncomment this code where USer entity is finished
        // user.removeLesson(this);
    }
}
