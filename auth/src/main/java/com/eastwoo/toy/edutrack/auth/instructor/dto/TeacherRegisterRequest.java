package com.eastwoo.toy.edutrack.auth.instructor.dto;

//강사 회원가입 요청 DTO
public record TeacherRegisterRequest(
        String token,
        String name,
        String password
) { }