package com.ead.course.services;

import com.ead.course.models.CourseUserModel;
import java.util.UUID;

public interface CourseUserService {

    CourseUserModel save(UUID userId,UUID courseId);

    void delete(UUID userId);
}
