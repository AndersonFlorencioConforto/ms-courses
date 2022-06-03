package com.ead.course.controllers;


import com.ead.course.client.UserClient;
import com.ead.course.dtos.UserDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    private final UserClient userClient;

    public CourseUserController(UserClient userClient) {
        this.userClient = userClient;
    }


    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserDTO>> getAllUsersByCourse(@PageableDefault(sort = "userId",direction = Sort.Direction.ASC)Pageable pageable, @PathVariable(value = "courseId") UUID courseId){
       return ResponseEntity.ok().body(userClient.getAllUsersByCourseId(courseId, pageable));
    }
}
