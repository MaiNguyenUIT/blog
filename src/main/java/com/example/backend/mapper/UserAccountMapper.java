package com.example.backend.mapper;

import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Mapper
public interface UserAccountMapper {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    UserAccountMapper INSTANCE = Mappers.getMapper(UserAccountMapper.class);

    //Map dto to entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "avatarUrl", ignore = true)
    @Mapping(target = "userRole", ignore = true)
    User toEntity(RegisterRequest registerRequest);

    RegisterRequest toDTO(User user);
    @AfterMapping
    default void encodePassword(@MappingTarget User user, RegisterRequest registerRequest) {
        if (registerRequest.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        }
    }
}
