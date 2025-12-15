package com.eastwoo.toy.edutrack.auth.instructor.dto;

import com.eastwoo.toy.edutrack.auth.user.entity.User;
import com.eastwoo.toy.edutrack.auth.user.enumtype.UserRole;

public record InstructorRegisterResponse(
        Long id,
        String email,
        UserRole role
) {
    public static InstructorRegisterResponse from(User user) {
        return new InstructorRegisterResponse(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}