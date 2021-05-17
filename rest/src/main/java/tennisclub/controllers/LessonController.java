package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.lesson.LessonCreateDTO;
import tennisclub.dto.lesson.LessonFullDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.facade.LessonFacade;

import java.util.List;

/**
 * @author Xuan Linh Phamová
 */
@CrossOrigin
@RestController
@RequestMapping("/lessons")
public class LessonController {
    private final LessonFacade lessonFacade;

    @Autowired
    public LessonController(LessonFacade lessonFacade){
        this.lessonFacade = lessonFacade;
    }

    @GetMapping
    public List<LessonFullDTO> getLessons(){
        return lessonFacade.getAllLessons();
    }

    @GetMapping("/{id}")
    public LessonFullDTO getLessonById(@PathVariable Long id){
        return lessonFacade.getLessonWithId(id);
    }

    @PostMapping
    public LessonFullDTO createLesson(@RequestBody LessonCreateDTO lessonDTO){
        return lessonFacade.createLesson(lessonDTO);
    }

    @PostMapping("/{lessonId}/users")
    public LessonFullDTO enrollStudent(@PathVariable Long lessonId, @RequestBody UserDTO player){
        return lessonFacade.enrollStudent(lessonId, player.getId());
    }

    @PostMapping("/{lessonId}/users/")
    public LessonFullDTO addTeacher(@PathVariable Long lessonId, @RequestBody UserDTO teacher){
        return lessonFacade.addTeacher(lessonId, teacher.getId());
    }

    @DeleteMapping("/{lessonId}/users/{studentId}")
    public LessonFullDTO withdrawStudent(@PathVariable Long lessonId, @PathVariable Long studentId){
        return lessonFacade.withdrawStudent(lessonId, studentId);
    }

    @DeleteMapping("/{lessonId}/users/{teacherId}")
    public LessonFullDTO removeTeacher(@PathVariable Long lessonId, @PathVariable Long teacherId){
        return lessonFacade.removeTeacher(lessonId, teacherId);
    }

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable Long id){
        lessonFacade.deleteLesson(id);
    }
}
