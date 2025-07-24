package com.algaworks.algaposts.post.service.domain.repositories;

import com.algaworks.algaposts.post.service.domain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostServiceRepository extends JpaRepository<Post, UUID> {
}
