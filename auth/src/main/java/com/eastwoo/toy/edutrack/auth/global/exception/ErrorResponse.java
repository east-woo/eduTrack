package com.eastwoo.toy.edutrack.auth.global.exception;

public record ErrorResponse(
        String code,
        String message
) {}