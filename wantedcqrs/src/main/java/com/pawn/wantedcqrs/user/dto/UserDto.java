package com.pawn.wantedcqrs.user.dto;

import com.pawn.wantedcqrs.user.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDto {

    private Long id;

    private String name;

    private String email;

    private String avatarUrl;

    private LocalDateTime createdAt;

    public User toEntity() {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .avatarUrl(avatarUrl)
                .createdAt(createdAt)
                .build();
    }

    public static UserDto fromEntity(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

}
