package com.ead.course.controllers;

import com.ead.course.client.AuthUserClient;
import com.ead.course.dtos.SubscriptionDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
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

    private final AuthUserClient authUserClient;
    private final CourseUserService courseUserService;
    private final CourseService courseService;

    public CourseUserController(AuthUserClient authUserClient, CourseUserService courseUserService, CourseService courseService) {
        this.authUserClient = authUserClient;
        this.courseUserService = courseUserService;
        this.courseService = courseService;
    }


    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserDTO>> getAllUsersByCourse(@PageableDefault(sort = "userId",direction = Sort.Direction.ASC)Pageable pageable, @PathVariable(value = "courseId") UUID courseId){
        courseService.findById(courseId);
       return ResponseEntity.ok().body(authUserClient.getAllUsersByCourseId(courseId, pageable));
    }

    @PostMapping(("/courses/{courseId}/users/subscription"))
    public ResponseEntity<CourseUserModel> saveSubscriptionUserInCourse(@PathVariable(value = "courseId") UUID courseId, @RequestBody @Valid SubscriptionDTO subscriptionDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserService.save(subscriptionDTO.getUserId(),courseId));
    }

    @DeleteMapping("/courses/users/{userId}")
    public ResponseEntity<Void> deleteCourseUserByUser(@PathVariable(value = "userId") UUID userId){
        courseUserService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
