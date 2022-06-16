package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.exceptions.ResourceNotFoundException;
import com.ead.course.specifications.SpecificationTemplate;
import com.ead.course.validation.CourseValidator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/courses")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;
    @Autowired
    private CourseValidator validator;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@RequestBody CourseDTO courseDTO, Errors errors) {
        validator.validate(courseDTO,errors);
        if (errors.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors.getAllErrors());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.save(courseDTO));
    }

    @DeleteMapping(value = "/{courseId}")
    public ResponseEntity<Void> deleteCourse(@PathVariable(value = "courseId") UUID courseId) {
        courseService.delete(courseId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable(value = "courseId") UUID courseId, @Valid @RequestBody CourseDTO courseDTO) {
        return ResponseEntity.ok().body(courseService.update(courseId,courseDTO));
    }

    @GetMapping
    public ResponseEntity<Page<CourseModel>> findAllCourse(SpecificationTemplate.CourseSpec spec,
            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "courseId",
                    direction = Sort.Direction.ASC) Pageable pageable,
                                                           @RequestParam(required = false) UUID userId) {
        return ResponseEntity.ok().body(courseService.findAll(spec,pageable));
    }

    @GetMapping(value = "/{courseId}")
    public ResponseEntity<Object> findById(@PathVariable(value = "courseId") UUID courseId) {
        return ResponseEntity.ok().body(courseService.findById(courseId));
    }
}
