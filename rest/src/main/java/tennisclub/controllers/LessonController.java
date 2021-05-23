package tennisclub.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tennisclub.dto.event.EventRescheduleDTO;
import tennisclub.dto.lesson.LessonCreateDTO;
import tennisclub.dto.lesson.LessonFullDTO;
import tennisclub.dto.user.UserDTO;
import tennisclub.dto.user.UserFullDTO;
import tennisclub.enums.Role;
import tennisclub.facade.EventFacade;
import tennisclub.facade.LessonFacade;
import tennisclub.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * @author Xuan Linh Phamová
 */
@CrossOrigin
@RestController
@RequestMapping("/rest/lessons")
public class LessonController {
    private final LessonFacade lessonFacade;
    private final EventFacade eventFacade;
    private final UserService userService;

    @Autowired
    public LessonController(LessonFacade lessonFacade, EventFacade eventFacade,
                            UserService userService){
        this.lessonFacade = lessonFacade;
        this.eventFacade = eventFacade;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<LessonFullDTO>> getLessons(@RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.ok(lessonFacade.getAllLessons());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LessonFullDTO> getLessonById(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.ok(lessonFacade.getLessonWithId(id));
    }

    @PostMapping
    public ResponseEntity<LessonFullDTO> createLesson(@Valid @RequestBody LessonCreateDTO lessonDTO, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.MANAGER);
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonFacade.createLesson(lessonDTO));
    }

    @PostMapping("/{lessonId}/students")
    public ResponseEntity<LessonFullDTO> enrollStudent(@PathVariable Long lessonId, @RequestBody UserFullDTO player, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.ok(lessonFacade.enrollStudent(lessonId, player.getId()));
    }

    @PostMapping("/{lessonId}/teachers")
    public ResponseEntity<LessonFullDTO> addTeacher(@PathVariable Long lessonId, @RequestBody UserFullDTO teacher, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.MANAGER);
        return ResponseEntity.ok(lessonFacade.addTeacher(lessonId, teacher.getId()));
    }

    @PutMapping("/{lessonId}")
    public ResponseEntity<LessonFullDTO> rescheduleLesson(@PathVariable Long lessonId, @Valid @RequestBody EventRescheduleDTO dto, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.MANAGER);
        eventFacade.reschedule(lessonId, dto);
        return ResponseEntity.ok(lessonFacade.getLessonWithId(lessonId));
    }

    @DeleteMapping("/{lessonId}/students/{studentId}")
    public ResponseEntity<LessonFullDTO> withdrawStudent(@PathVariable Long lessonId, @PathVariable Long studentId, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.USER);
        return ResponseEntity.ok(lessonFacade.withdrawStudent(lessonId, studentId));
    }

    @DeleteMapping("/{lessonId}/teachers/{teacherId}")
    public ResponseEntity<LessonFullDTO> removeTeacher(@PathVariable Long lessonId, @PathVariable Long teacherId, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.MANAGER);
        return ResponseEntity.ok(lessonFacade.removeTeacher(lessonId, teacherId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable Long id, @RequestHeader(value = "Authorization", required = false) String jwt){
        userService.verifyRole(jwt, Role.MANAGER);
        lessonFacade.deleteLesson(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
