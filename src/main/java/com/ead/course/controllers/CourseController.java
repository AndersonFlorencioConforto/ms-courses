package com.ead.course.controllers;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.services.CourseService;
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
@RequestMapping(value = "/courses")
@CrossOrigin(origins = "*",maxAge = 3600)
public class CourseController {

    @Autowired
    private CourseService courseService;

    @PostMapping
    public ResponseEntity<Object> saveCourse(@Valid @RequestBody CourseDTO courseDTO) {
        var courseModel = new CourseModel();
        BeanUtils.copyProperties(courseDTO,courseModel);
        CourseModel save = courseService.save(courseModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(save);
    }

    @DeleteMapping(value = "/{courseId}")
    public ResponseEntity<Object> deleteCourse(@PathVariable (value = "courseId")UUID courseId) {
        Optional<CourseModel> findById = courseService.findById(courseId);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        courseService.delete(findById.get());
        return ResponseEntity.ok().body("Course deleted successfully");
    }

    @PutMapping(value = "/{courseId}")
    public ResponseEntity<Object> updateCourse(@PathVariable (value = "courseId")UUID courseId,@Valid @RequestBody CourseDTO courseDTO) {
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
    public ResponseEntity<List<CourseModel>> findAllCourse() {
        return ResponseEntity.ok().body(courseService.findAll());
    }

    @GetMapping(value = "/{courseId}")
    public ResponseEntity<Object> findById(@PathVariable (value = "courseId")UUID courseId) {
        Optional<CourseModel> findById = courseService.findById(courseId);
        if (!findById.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course Not Found");
        }
        return ResponseEntity.ok().body(findById.get());
    }
}