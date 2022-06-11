package com.ead.course.services.impl;

import com.ead.course.services.UtilsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UtilsServiceImpl implements UtilsService {

    @Value("${ead.api.url.authuser}")
    private String REQUEST_URL_AUTHUSER;


    @Override
    public String getAllUsersByCourseId(UUID courseId, Pageable pageable) {
        return REQUEST_URL_AUTHUSER + "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber()
                + "&size=" + pageable.getPageSize() + "&sort=" + pageable.getSort().toString()
                .replaceAll(": ", ",");
    }

    @Override
    public String findUserById(UUID userId) {
        return REQUEST_URL_AUTHUSER + "/users/" + userId;
    }

    @Override
    public String postSubscriptionUserInCourse(UUID userId) {
      return REQUEST_URL_AUTHUSER + "/users/" + userId + "/courses/subscription";
    }

    @Override
    public String deleteCourseInAuthUser(UUID courseId) {
        return REQUEST_URL_AUTHUSER + "/users/courses/" + courseId;
    }
}
