package com.ead.course.services;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UtilsService {

    String getAllUsersByCourseId(UUID courseId, Pageable pageable);
    String findUserById(UUID userId);
}
