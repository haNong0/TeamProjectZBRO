package com.zbro.main.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Community;
import com.zbro.model.Favorite;
import com.zbro.type.PostType;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zbro.model.Community;
import com.zbro.type.PostType;



public interface CommunityRepository extends JpaRepository<Community, Long> {
 
	 	List<Community> findTop8ByUserConsumerIdOrderByCreateDateDesc(Long consumerId);
		 
		Page<Community> findByUserConsumerIdAndTypeOrderByCreateDateDesc(Long userId, PostType type, Pageable pageable);

		Page<Community> findByTitleContainingAndTypeAndCategoryType(String searchWord, PostType type, String categoryType,
				Pageable pageable);
		Page<Community> findByContentContainingAndTypeAndCategoryType(String searchWord, PostType type, String categoryType,
				Pageable pageable);
		Page<Community> findByUser_NameContainingAndTypeAndCategoryType(String searchWord, PostType type,
				String categoryType, Pageable pageable);
		Page<Community> findByTypeAndCategoryType(PostType type, String categoryType, Pageable pageable);

	 
}

