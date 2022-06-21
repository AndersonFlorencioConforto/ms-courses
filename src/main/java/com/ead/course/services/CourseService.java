package com.ead.course.services;

import com.ead.course.dtos.CourseDTO;
import com.ead.course.dtos.SubscriptionDTO;
import com.ead.course.models.CourseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;
import java.util.UUID;

public interface CourseService {

    void delete(UUID courseId);
    CourseModel save(CourseDTO courseDTO);
    Optional<CourseModel> findById(UUID courseId);
    Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable);
    CourseModel update(UUID courseId, CourseDTO courseDTO);
    void saveSubscriptionUserInCourse(UUID courseId, SubscriptionDTO subscriptionDTO);
}
