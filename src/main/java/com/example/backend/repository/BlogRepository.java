package com.example.backend.repository;

import com.example.backend.model.Blog;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends MongoRepository<Blog, String> {
    List<Blog> findByuserId(String userId);
    List<Blog> findAll(Sort sort);
    List<Blog> findByIsPublicTrue();
}
