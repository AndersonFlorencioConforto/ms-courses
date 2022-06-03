package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
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
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@Valid @RequestBody CourseDTO courseDTO) {
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO, courseModel);
        CourseModel save = courseService.save(courseModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @DeleteMapping(value = "/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> findById = courseService.findById(courseId);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        courseService.delete(findById.get());
        return ResponseEntity.ok().body("Course deleted successfully");
    }

    @PutMapping(value = "/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId, @Valid @RequestBody CourseDTO courseDTO) {
        Optional<CourseModel> findById = courseService.findById(courseId);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        var courseModel = findById.get();
        courseModel.setName(courseDTO.getName());
        courseModel.setDescription(courseDTO.getDescription());
        courseModel.setImageUrl(courseDTO.getImageUrl());
        courseModel.setCourseStatus(courseDTO.getCourseStatus());
        courseModel.setCourseLevel(courseDTO.getCourseLevel());
        return ResponseEntity.ok().body(courseService.save(courseModel));
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> findAllCourse(SpecificationTemplate.CourseSpec spec,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "courseId",
                    direction = Sort.Direction.ASC) Pageable pageable,
                                                           @RequestParam(required = false) UUID userId) {
        if (userId != null) {
           return ResponseEntity.ok().body(courseService.findAll(SpecificationTemplate.courseUserId(userId).and(spec),pageable));
        }
        return ResponseEntity.ok().body(courseService.findAll(spec,pageable));
    }

    @GetMapping(value = "/{courseId}")
    public ResponseEntity<Object> findById(@PathVariable(value = "courseId") UUID courseId) {
        Optional<CourseModel> findById = courseService.findById(courseId);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        return ResponseEntity.ok().body(findById.get());
    }
}
