package com.ead.course.dtos;

import com.ead.course.models.enums.CourseLevel;
import com.ead.course.models.enums.CourseStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.UUID;

@Data
public class CourseDTO implements Serializable {
    private static final long serialVersionUID= 1L;

    @NotBlank
    private String name;
    @NotBlank
    private String description;
    private String imageUrl;
    @NotNull
    private CourseStatus courseStatus;
    @NotNull
    private CourseLevel courseLevel;
    @NotNull
    private UUID userInstructor;
}
