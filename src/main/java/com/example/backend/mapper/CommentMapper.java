package com.example.backend.mapper;

import com.example.backend.dto.BlogDTO;
import com.example.backend.dto.CommentDTO;
import com.example.backend.model.Blog;
import com.example.backend.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    //Map dto to entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "blogId", ignore = true)
    Comment toEntity(CommentDTO CommentDTO);

    CommentDTO toDTO(Comment comment);
}
