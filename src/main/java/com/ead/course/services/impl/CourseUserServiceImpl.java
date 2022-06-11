package com.ead.course.services.impl;

import com.ead.course.client.AuthUserClient;
import com.ead.course.dtos.UserDTO;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.models.enums.UserStatus;
import com.ead.course.repositories.CourseUserRepository;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import com.ead.course.services.exceptions.ConflictException;
import com.ead.course.services.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Optional;
import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    private final CourseUserRepository courseUserRepository;
    private final CourseService courseService;
    private final AuthUserClient authUserClient;

    public CourseUserServiceImpl(CourseUserRepository courseUserRepository, CourseService courseService, AuthUserClient authUserClient) {
        this.courseUserRepository = courseUserRepository;
        this.courseService = courseService;
        this.authUserClient = authUserClient;
    }

    @Transactional
    @Override
    public CourseUserModel save(UUID userId, UUID courseId) {
        Optional<CourseModel> course = courseService.findById(courseId);
        CourseModel obj = course.orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        if (courseUserRepository.existsByCourseAndUserId(obj, userId)) {
            throw new ConflictException("Error : subscription already exists!");
        }
        try {
            UserDTO userResponse = authUserClient.findUserById(userId);
            if (userResponse.getUserStatus().equals(UserStatus.BLOCKED)) {
                throw new ConflictException("User is blocked");
            }
        } catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                throw new ResourceNotFoundException("User not found");
            }
        }
        CourseUserModel courseUserModel = obj.convertToCourseUserModel(userId);
        authUserClient.postSubscriptionUserInCourse(courseUserModel.getCourse().getCourseId(), courseUserModel.getUserId());
        return courseUserRepository.save(courseUserModel);
    }

    @Transactional
    @Override
    public void delete(UUID userId) {
        if (!courseUserRepository.existsByUserId(userId)){
            throw new ResourceNotFoundException("CourseUser not found");
        }
        courseUserRepository.deleteAllByUserId(userId);
    }

}
