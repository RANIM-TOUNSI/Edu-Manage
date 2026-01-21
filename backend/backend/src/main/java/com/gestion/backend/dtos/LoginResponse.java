package com.gestion.backend.dtos;

import com.gestion.backend.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserDto user;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserDto {
        private Long id;
        private String username;
        private String role;
        private Long trainerId;
        private Long studentId;
    }
}
