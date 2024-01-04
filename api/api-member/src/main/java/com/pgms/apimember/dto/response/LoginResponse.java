package com.pgms.apimember.dto.response;

public record LoginResponse(String jwtToken, Long id, String email, String roleName) {
}
