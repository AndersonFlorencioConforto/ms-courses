package com.ead.course.dtos;

import lombok.Data;
import java.util.UUID;

@Data
public class UserCourseDTO {

    private UUID userId;
    private UUID courseId;
}
