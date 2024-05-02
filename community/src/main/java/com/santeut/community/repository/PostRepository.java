package com.santeut.community.repository;

import com.santeut.community.entity.PostEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<PostEntity, Integer> {

        // 게시글 목록 조회
        Optional<List<PostEntity>> findAllByPostType(char postType);

        // 게시글 디테일 조회
        Optional<PostEntity> findByIdAndPostType(int postId, char postType);

        // 게시글 삭제
        @Modifying
        @Transactional
        @Query("UPDATE PostEntity p SET p.isDeleted = true, p.deletedAt=:deletedAt WHERE p.id=:postId")
        void deletePostDirectly(int postId, LocalDateTime deletedAt);
}
