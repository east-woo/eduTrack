package com.eastwoo.toy.edutrack.auth.instructor.dto;

public record InstructorSignupRequestDto(
        String name,
        String email,
        String message
) {}