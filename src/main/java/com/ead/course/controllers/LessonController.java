package com.ead.course.controllers;

import com.ead.course.dtos.LessonDTO;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.services.LessonService;
import com.ead.course.services.ModuleService;
import com.ead.course.specifications.SpecificationTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*",maxAge = 3600)
public class LessonController {

    @Autowired
    private LessonService lessonService;
    @Autowired
    private ModuleService moduleService;

    @PostMapping(value = "/modules/{moduleId}/lessons")
    public ResponseEntity<Object> saveLesson(@Valid @RequestBody LessonDTO lessonDTO,
                                             @PathVariable(value = "moduleId") UUID moduleId) {
        Optional<ModuleModel> findById = moduleService.findById(moduleId);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module Not Found");
        }
        var lessonModel = new LessonModel();
        BeanUtils.copyProperties(lessonDTO,lessonModel);
        lessonModel.setModule(findById.get());
        LessonModel save = lessonService.save(lessonModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @DeleteMapping(value = "/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> deleteLesson(@PathVariable (value = "moduleId")UUID moduleId,
                                               @PathVariable (value = "lessonId")UUID lessonId) {
        Optional<LessonModel> findById = lessonService.findLessonIntoModule(moduleId,lessonId);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module or Lesson Not Found");
        }
        lessonService.delete(findById.get());
        return ResponseEntity.ok().body("Lesson deleted successfully");
    }

    @PutMapping(value = "/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> updateLesson(@PathVariable (value = "moduleId")UUID moduleId,
                                               @PathVariable (value = "lessonId")UUID lessonId,
                                               @Valid @RequestBody LessonDTO lessonDTO) {
        Optional<LessonModel> findById = lessonService.findLessonIntoModule(moduleId,lessonId);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module or Lesson Not Found");
        }
        var lessonModel = findById.get();
        lessonModel.setTitle(lessonDTO.getTitle());
        lessonModel.setDescription(lessonDTO.getDescription());
        lessonModel.setVideoUrl(lessonDTO.getVideoUrl());
        return ResponseEntity.ok().body(lessonService.save(lessonModel));
    }

    @GetMapping(value = "/modules/{moduleId}/lessons")
    public ResponseEntity<Page<LessonModel>> findAllLesson(
            @PathVariable (value = "moduleId")UUID moduleId,
            SpecificationTemplate.LessonSpec spec,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "lessonId",
                    direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok().body(lessonService.findAllByModule(SpecificationTemplate.lessonModuleId(moduleId).and(spec),pageable));
    }

    @GetMapping(value = "/modules/{moduleId}/lessons/{lessonId}")
    public ResponseEntity<Object> findByIdLesson(@PathVariable (value = "moduleId")UUID moduleId,
                                                 @PathVariable (value = "lessonId")UUID lessonId) {
        Optional<LessonModel> findById = lessonService.findLessonIntoModule(moduleId,lessonId);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Module or Lesson Not Found");
        }
        return ResponseEntity.ok().body(findById.get());
    }


}
