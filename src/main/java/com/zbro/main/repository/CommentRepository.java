package com.zbro.main.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Comment;
import com.zbro.model.Community;


public interface CommentRepository extends JpaRepository<Comment, Long>{
	
    Page<Comment> findByUserConsumerId(Long consumerId, Pageable pageable);
	
	List<Comment> findTop8ByUserConsumerIdOrderByCreateDateDesc(Long consumerId);
	
	List<Comment> findAllByPost(Community post);


}
