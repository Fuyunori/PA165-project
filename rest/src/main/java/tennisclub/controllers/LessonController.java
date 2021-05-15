package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.lesson.LessonCreateDTO;
import tennisclub.dto.lesson.LessonFullDTO;
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

    @PutMapping("/enroll-student")
    public LessonFullDTO enrollStudent(@RequestParam Long lessonId, @RequestParam Long studentId){
        return lessonFacade.enrollStudent(lessonId, studentId);
    }

    @PutMapping("/add-teacher")
    public LessonFullDTO addTeacher(@RequestParam Long lessonId, @RequestParam Long teacherId){
        return lessonFacade.addTeacher(lessonId, teacherId);
    }

    @PutMapping("/withdraw-student")
    public LessonFullDTO withdrawStudent(@RequestParam Long lessonId, @RequestParam Long studentId){
        return lessonFacade.withdrawStudent(lessonId, studentId);
    }

    @PutMapping("/remove-teacher")
    public LessonFullDTO removeTeacher(@RequestParam Long lessonId, @RequestParam Long teacherId){
        return lessonFacade.removeTeacher(lessonId, teacherId);
    }

    @DeleteMapping("/{id}")
    public void deleteLesson(@PathVariable Long id){
        lessonFacade.deleteLesson(id);
    }
}
