package com.ead.course.controllers;

import com.ead.course.dtos.ModuleDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.ModuleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
public class ModuleController {

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private CourseService courseService;

    @PostMapping(value = "/courses/{courseId}/modules")
    public ResponseEntity<Object> saveModule(@Valid @RequestBody ModuleDTO moduleDTO, @PathVariable (value = "courseId") UUID courseId) {
        Optional<CourseModel> findById = courseService.findById(courseId);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        var moduleModel = new ModuleModel();
        BeanUtils.copyProperties(moduleDTO,moduleModel);
        moduleModel.setCourse(findById.get());
        ModuleModel save = moduleService.save(moduleModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @DeleteMapping(value = "/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable (value = "courseId")UUID courseId,
                                               @PathVariable (value = "moduleId")UUID moduleId) {
        Optional<ModuleModel> findById = moduleService.findModuleIntoCourse(courseId,moduleId);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course or Module Not Found");
        }
        moduleService.delete(findById.get());
        return ResponseEntity.ok().body("Module deleted successfully");
    }


    @PutMapping(value = "/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> updateModule(@PathVariable (value = "courseId")UUID courseId,
                                               @PathVariable (value = "moduleId")UUID moduleId,
                                               @Valid @RequestBody ModuleDTO moduleDTO) {
        Optional<ModuleModel> moduleIntoCourse = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (!moduleIntoCourse.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course or Module Not Found");
        }
        var moduleModel = moduleIntoCourse.get();
        moduleModel.setTitle(moduleDTO.getTitle());
        moduleModel.setDescription(moduleDTO.getDescription());
        return ResponseEntity.ok().body(moduleService.save(moduleModel));
    }


    @GetMapping(value = "/courses/{courseId}/modules")
    public ResponseEntity<List<ModuleModel>> findAllCourse(@PathVariable (value = "courseId")UUID courseId) {
        return ResponseEntity.ok().body(moduleService.findAllByCourse(courseId));
    }

    @GetMapping(value = "/courses/{courseId}/modules/{moduleId}")
    public ResponseEntity<Object> findByIdCourse(@PathVariable (value = "courseId")UUID courseId,
                                           @PathVariable (value = "moduleId")UUID moduleId) {
        Optional<ModuleModel> moduleIntoCourse = moduleService.findModuleIntoCourse(courseId, moduleId);
        if (!moduleIntoCourse.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        return ResponseEntity.ok().body(moduleIntoCourse.get());
    }
}
