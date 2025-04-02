package com.example.backend.mapper;

import com.example.backend.dto.BlogDTO;
import com.example.backend.dto.request.RegisterRequest;
import com.example.backend.model.Blog;
import com.example.backend.model.User;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public interface BlogMapper {

    //Map dto to entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "userId", ignore = true)
    Blog toEntity(BlogDTO blogDTO);

    BlogDTO toDTO(Blog blog);
}
