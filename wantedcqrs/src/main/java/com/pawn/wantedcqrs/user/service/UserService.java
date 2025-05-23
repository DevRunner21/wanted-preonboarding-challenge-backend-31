package com.pawn.wantedcqrs.user.service;

import com.pawn.wantedcqrs.common.exception.e4xx.ResourceNotFoundException;
import com.pawn.wantedcqrs.user.dto.UserDto;
import com.pawn.wantedcqrs.user.entity.User;
import com.pawn.wantedcqrs.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Map<Long, UserDto> getUsersMapBy(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return new HashMap<>();
        }

        List<User> foundUsers = userRepository.findUsersByIdIn(userIds);

        return foundUsers.stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toMap(UserDto::getId, user -> user));
    }

    public UserDto getUserBy(Long userId) {
        if(userId == null) {
            return null;
        }
        User user = userRepository.findById(userId).orElseThrow(ResourceNotFoundException.USER::getResponseException);

        return UserDto.fromEntity(user);
    }

}
