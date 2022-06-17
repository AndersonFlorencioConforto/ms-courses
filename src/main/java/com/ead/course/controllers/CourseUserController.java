package com.ead.course.controllers;

import com.ead.course.dtos.SubscriptionDTO;
import com.ead.course.models.UserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import com.ead.course.specifications.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    private final UserService userService;
    private final CourseService courseService;

    public CourseUserController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }


    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserModel>> getAllUsersByCourse(SpecificationTemplate.UserSpec spec, @PageableDefault(sort = "userId",direction = Sort.Direction.ASC)Pageable pageable, @PathVariable(value = "courseId") UUID courseId){
        courseService.findById(courseId);
       return ResponseEntity.ok().body(userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec),pageable));
    }

    @PostMapping(("/courses/{courseId}/users/subscription"))
    public ResponseEntity<UserModel> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid SubscriptionDTO subscriptionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null); //todo arrumar
    }

}
