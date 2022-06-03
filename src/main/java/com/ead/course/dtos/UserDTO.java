package com.ead.course.dtos;

import com.ead.course.models.enums.UserStatus;
import com.ead.course.models.enums.UserType;
import lombok.Data;

import java.util.UUID;

@Data
public class UserDTO {

    private UUID userId;
    private String username;
    private String email;
    private String fullname;
    private UserStatus userStatus;
    private UserType userType;
    private String cpf;
    private String imageUrl;
}
