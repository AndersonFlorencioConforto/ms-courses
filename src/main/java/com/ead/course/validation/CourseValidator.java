package com.ead.course.validation;

import com.ead.course.client.AuthUserClient;
import com.ead.course.dtos.CourseDTO;
import com.ead.course.dtos.UserDTO;
import com.ead.course.models.enums.UserType;
import com.ead.course.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Autowired
    @Qualifier("defaultValidator")
    private Validator validator;
    
    @Autowired
    private  AuthUserClient authUserClient;
    
    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseDTO courseDTO = (CourseDTO) target;
        validator.validate(courseDTO,errors);
        if(!errors.hasErrors()) {
            validateUserInstructor(courseDTO.getUserInstructor(),errors);
        }
    }
    
    private void validateUserInstructor (UUID userInstructor,Errors errors) {
        try {
            UserDTO userById = authUserClient.findUserById(userInstructor);
            if (userById.getUserType().equals(UserType.STUDENT)) {
                errors.rejectValue("userInstructor","UserInstructorError","User must be INSTRUCTOR or ADMIN");
            }
        }catch (HttpStatusCodeException e) {
            if (e.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                errors.rejectValue("userInstructor","UserInstructorError","Instructor not found");
            }
        }
    }
}
