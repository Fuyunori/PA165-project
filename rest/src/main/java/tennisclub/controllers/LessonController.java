package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.lesson.LessonCreateDTO;
import tennisclub.dto.lesson.LessonFullDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.dto.user.UserFullDTO;
import tennisclub.facade.LessonFacade;

import javax.validation.Valid;
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
    public ResponseEntity<List<LessonFullDTO>> getLessons(){
        return ResponseEntity.ok(lessonFacade.getAllLessons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonFullDTO> getLessonById(@PathVariable Long id){
        return ResponseEntity.ok(lessonFacade.getLessonWithId(id));
    }

    @PostMapping
    public ResponseEntity<LessonFullDTO> createLesson(@Valid @RequestBody LessonCreateDTO lessonDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonFacade.createLesson(lessonDTO));
    }

    @PostMapping("/{lessonId}/students")
    public ResponseEntity<LessonFullDTO> enrollStudent(@PathVariable Long lessonId, @RequestBody UserFullDTO player){
        return ResponseEntity.ok(lessonFacade.enrollStudent(lessonId, player.getId()));
    }

    @PostMapping("/{lessonId}/teachers")
    public ResponseEntity<LessonFullDTO> addTeacher(@PathVariable Long lessonId, @RequestBody UserFullDTO teacher){
        return ResponseEntity.ok(lessonFacade.addTeacher(lessonId, teacher.getId()));
    }

    @DeleteMapping("/{lessonId}/students/{studentId}")
    public ResponseEntity<LessonFullDTO> withdrawStudent(@PathVariable Long lessonId, @PathVariable Long studentId){
        return ResponseEntity.ok(lessonFacade.withdrawStudent(lessonId, studentId));
    }

    @DeleteMapping("/{lessonId}/teachers/{teacherId}")
    public ResponseEntity<LessonFullDTO> removeTeacher(@PathVariable Long lessonId, @PathVariable Long teacherId){
        return ResponseEntity.ok(lessonFacade.removeTeacher(lessonId, teacherId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id){
        lessonFacade.deleteLesson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
