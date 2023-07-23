package com.zbro.main.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.zbro.dto.CommentDto;
import com.zbro.main.repository.CommentRepository;
import com.zbro.main.repository.CommunityRepository;
import com.zbro.main.repository.ConsumerUserRepository;
import com.zbro.model.Comment;
import com.zbro.model.Community;
import com.zbro.model.ConsumerUser;
import com.zbro.type.PostType;

@Service
public class CommunityService {
	
	@Autowired
	private CommunityRepository commuRepo;
	@Autowired
	private ConsumerUserRepository consumerUserRepo;
	@Autowired
	private CommentRepository commentRepo;

	
	public Page<Community> getPosts(Pageable pageable, String searchType, String searchWord, PostType type, String categoryType) {
		if(searchType.equalsIgnoreCase("title")) {
			return commuRepo.findByTitleContainingAndTypeAndCategoryType(searchWord, type, categoryType, pageable);
		} else if(searchType.equalsIgnoreCase("content")) {
			return commuRepo.findByContentContainingAndTypeAndCategoryType(searchWord, type, categoryType, pageable);
		} else if(searchType.equalsIgnoreCase("user")) {
			return commuRepo.findByUser_NameContainingAndTypeAndCategoryType(searchWord, type, categoryType, pageable);
		} else {
	        return commuRepo.findByTypeAndCategoryType(type, categoryType, pageable);
	    }
	}


	public void postInsert(PostType postType, String categoryType, String title, String content, long userId) {
		ConsumerUser user = new ConsumerUser();
		user.setConsumerId(userId);
		
		// 나머지 매개변수를 사용하여 Community 객체 생성 및 저장
		Community community = new Community();
		community.setType(postType);
		community.setCategoryType(categoryType);
		community.setTitle(title);
		community.setContent(content);
		community.setUser(user);
		
		commuRepo.save(community);
	}


	public Community getPost(Long postId) {
		Optional<Community> findPost = commuRepo.findById(postId);
		
		if(findPost.isPresent()) {
			Community post = findPost.get();
			post.setViewCount(post.getViewCount()+1);
			commuRepo.save(post);
			return post;
		}
		else return null;
	}


	public void postRevise(PostType postType, String categoryType, Long postId, String title, String content,
			long userId, int viewCount) {
		ConsumerUser user = new ConsumerUser();
		user.setConsumerId(userId);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		String formattedDateTime = LocalDateTime.now().format(formatter);
		
		Community community = new Community();
		community.setType(postType);
		community.setCategoryType(categoryType);
		community.setPostId(postId);
		community.setTitle(title);
		community.setContent(content);
		community.setUser(user);
		community.setViewCount(viewCount);
		community.setUpdateDate(LocalDateTime.parse(formattedDateTime, formatter));
		
		commuRepo.save(community);
	}


	public void postDelete(Long postId) {
		commuRepo.deleteById(postId);
	}


	public List<Comment> getComment(Long postId) {
		Optional<Community> post = commuRepo.findById(postId);
//		System.out.println(post.get());
		
		return commentRepo.findAllByPost(post.get());
	}
	
	
	public Comment getThisComment(Long commentId) {
		return commentRepo.findById(commentId).get();
	}
	
	
	public List<Comment> getAllComments() {
		return commentRepo.findAll();
	}


	public void addComment(CommentDto commentDto) {
		Optional<Community> post = commuRepo.findById(commentDto.getPostId());
		Optional<ConsumerUser> user = consumerUserRepo.findById(commentDto.getUserId());
		
		if(post.isPresent() && user.isPresent()) {
			Comment comment = new Comment();
			comment.setContent(commentDto.getContent());
			comment.setPost(post.get());
			comment.setUser(user.get());
			if(commentDto.getParentId() != null) {
				Optional<Comment> parentCommentOptional = commentRepo.findById(commentDto.getParentId());
				Comment parentComment = parentCommentOptional.get();
				comment.setParent(parentComment);
				
				//commentType 설정
				int parentTypePlus = (parentCommentOptional.get().getCommentType())+1;
				comment.setCommentType(parentTypePlus);
			}
			
			commentRepo.save(comment);
		}
	}


	public void delComment(Long commentId) {
		commentRepo.deleteById(commentId);
	}


	public void reviseComment(Long commentId, String content, int commentType) {
		Optional<Comment> comment = commentRepo.findById(commentId);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
		String formattedDateTime = LocalDateTime.now().format(formatter);
		
		Comment reviseComment = new Comment();
		reviseComment.setCommentId(commentId);
		reviseComment.setContent(content);
		reviseComment.setCreateDate(comment.get().getCreateDate());
		reviseComment.setParent(comment.get().getParent());
		reviseComment.setPost(comment.get().getPost());
		reviseComment.setUpdateDate(LocalDateTime.parse(formattedDateTime, formatter));
		reviseComment.setUser(comment.get().getUser());
		reviseComment.setCommentType(commentType);
		
		commentRepo.save(reviseComment);
	}

}
