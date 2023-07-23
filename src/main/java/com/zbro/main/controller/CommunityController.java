package com.zbro.main.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zbro.dto.CommentDto;
import com.zbro.dto.PageInfo;
import com.zbro.main.service.CommunityService;
import com.zbro.model.Comment;
import com.zbro.model.Community;
import com.zbro.model.ConsumerUser;
import com.zbro.type.Category;
import com.zbro.type.PostType;

@Controller
public class CommunityController {
	
	@Autowired
	private CommunityService commuService;
	
	@RequestMapping("/post_list")
	public String postList(Model model,
							@RequestParam(defaultValue = "0") int page,
							@RequestParam(defaultValue = "10") int pageSize,
							@RequestParam(defaultValue = "자취") String categoryType,
							@RequestParam(defaultValue = "title") String searchType,
							@RequestParam(defaultValue = "") String searchWord,
							@RequestParam(defaultValue = "꿀팁") String type) {
		PostType postType = PostType.valueOf(type);
		
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("postId").descending());
		Page<Community> pageResult = commuService.getPosts(pageable, searchType, searchWord, postType, categoryType);
		
		List<Community> postList = pageResult.getContent();
		
		// 페이징 확인
		for(Community post:postList) {
			System.out.println(post.toString());
		}
		
		// 쿼리실행 후 총 게시물 수
		int resultTotalCnt = pageResult.getNumberOfElements();
		System.out.println("쿼리실행 후 총 게시물 수 : " + resultTotalCnt);
		
		// 페이징처리
		PageInfo pageInfo = new PageInfo((int)pageResult.getTotalElements(), page, pageSize, searchType, searchWord, categoryType);
		
		
		model.addAttribute("pageable", pageable);		// 쿼리문 실행 전 페이징 정보
		model.addAttribute("pageInfo", pageInfo);		// 쿼리문 실행 후 페이징 정보
		model.addAttribute("pageResult", pageResult);	// 페이징 처리된 데이터
		
		model.addAttribute("pg", pageInfo);
		model.addAttribute("bp", pageInfo.getBeginPage());
		model.addAttribute("ep", pageInfo.getEndPage());
		model.addAttribute("ns", pageInfo.getNaviSize());
		model.addAttribute("ps", pageSize);
		model.addAttribute("tp", pageInfo.getTotalPage());
		model.addAttribute("st", searchType);
		model.addAttribute("sw", searchWord);
		model.addAttribute("ct", categoryType);
		model.addAttribute("type", type);
		model.addAttribute("categories", Category.values());
		
		
		return "main/community/post_list";
	}

	
	
	@GetMapping("/post_add")
	public String postAddView(Model model,
							@RequestParam String type,
							@RequestParam String categoryType) {
		
		model.addAttribute("type", type);
		model.addAttribute("ct", categoryType);
		model.addAttribute("categories", Category.values());
		return "main/community/post_add";
	}
	
	
	
	@PostMapping("/post_add")
	public String postAdd(@RequestParam String type,
						  @RequestParam String categoryType,
						  @RequestParam String title,
						  @RequestParam String content,
						  @RequestParam(defaultValue = "1") long user,
						  RedirectAttributes redirectAttributes) {
		PostType postType = PostType.valueOf(type);
		
		commuService.postInsert(postType, categoryType, title, content, user);
		
		redirectAttributes.addAttribute("type", type);
		redirectAttributes.addAttribute("categoryType", categoryType);
		return "redirect:post_list";
	}
	
	
	
	@GetMapping("/post_detail")
	public String postDetail(Model model,
							 @RequestParam Long postId) {
		
		Community community = commuService.getPost(postId);
		
		model.addAttribute("post", community);
		model.addAttribute("type", community.getType());
		model.addAttribute("ct", community.getCategoryType());
		model.addAttribute("categories", Category.values());
		
		return "main/community/post_detail";
	}
	
	
	
	@GetMapping("/post_revise")
	public String postReviseView(Model model,
								 @RequestParam Long postId) {
		
		Community community = commuService.getPost(postId);
		model.addAttribute("post", community);
		model.addAttribute("type", community.getType());
		model.addAttribute("ct", community.getCategoryType());
		model.addAttribute("categories", Category.values());
		
		return "main/community/post_revise";
	}
	
	
	
	@PostMapping("/post_revise")
	public String postRevise(@RequestParam String type,
						  	 @RequestParam String categoryType,
						  	 @RequestParam Long postId,
						  	 @RequestParam String title,
						  	 @RequestParam String content,
						  	 @RequestParam int viewCount,
						  	 @RequestParam(defaultValue = "1") long user,
						  	 RedirectAttributes redirectAttributes) {
		
		PostType postType = PostType.valueOf(type);
		
		commuService.postRevise(postType, categoryType, postId, title, content, user, viewCount);
		
		redirectAttributes.addAttribute("type", type);
		redirectAttributes.addAttribute("categoryType", categoryType);
		
		return "redirect:post_list";
	}
	
	
	
	@GetMapping("/post_delete")
	public String postDelete(@RequestParam Long postId,
							 @RequestParam String type,
							 @RequestParam String categoryType,
							 RedirectAttributes redirectAttributes) {
		
		commuService.postDelete(postId);
		
		redirectAttributes.addAttribute("type", type);
		redirectAttributes.addAttribute("categoryType", categoryType);
		
		return "redirect:post_list";
	}
	
	
	
	
	/*		댓글 관련		*/
	@PostMapping("/comment_add")
	public ResponseEntity<?> commentAdd(@RequestBody CommentDto commentDto) {
		commuService.addComment(commentDto);
		return ResponseEntity.ok().build();
	}
	
	
	
//	@GetMapping("/get_comments")
//	public ResponseEntity<?> getComments(@RequestParam("postId") Long postId) {
//		List<Comment> comments = commuService.getComment(postId);	//postId의 댓글목록 가져오기
//		
//		List<CommentDto> commentDtos = new ArrayList<>();
//		
//		for (Comment comment : comments) {
//		    if (comment.getParent() == null) {
//		        // 부모 댓글인 경우
//		        CommentDto commentDto = new CommentDto();
//		        commentDto.setContent(comment.getContent());
//		        commentDto.setPostId(postId);
//		        commentDto.setUserId(comment.getUser().getConsumerId());
//		        commentDto.setUserName(comment.getUser().getName());
//		        commentDto.setProfilePhoto(comment.getUser().getProfilePhoto());
//		        commentDto.setCreateDate(comment.getCreateDate());
//		        commentDto.setCommentId(comment.getCommentId());
//		        if (comment.getParent() != null) {
//	                commentDto.setParentId(comment.getParent().getCommentId());
//	            }
//		        commentDtos.add(commentDto);
//		        sortChildComments(comment, comments, commentDtos, postId);
//		    }
//		}
//		
//	    return ResponseEntity.ok(commentDtos);
//	}
//	
//	private void sortChildComments(Comment parentComment, List<Comment> comments, List<CommentDto> commentDtos, Long postId) {
//	    for (Comment comment : comments) {
//	        if (comment.getParent() == parentComment) {
//	            CommentDto commentDto = new CommentDto();
//	            commentDto.setContent(comment.getContent());
//	            commentDto.setPostId(postId);
//	            commentDto.setUserId(comment.getUser().getConsumerId());
//	            commentDto.setUserName(comment.getUser().getName());
//	            commentDto.setProfilePhoto(comment.getUser().getProfilePhoto());
//	            commentDto.setCreateDate(comment.getCreateDate());
//	            commentDto.setCommentId(comment.getCommentId());
//	            if (comment.getParent() != null) {
//	                commentDto.setParentId(comment.getParent().getCommentId());
//	            }
//	            commentDtos.add(commentDto);
//	            sortChildComments(comment, comments, commentDtos, postId);
//	        }
//	    }
//	}
	@GetMapping("/get_comments")
	public ResponseEntity<?> getComments(@RequestParam("postId") Long postId) {
	    List<Comment> comments = commuService.getComment(postId); // postId의 댓글목록 가져오기
	    
	    List<CommentDto> commentDtos = new ArrayList<>();
	    
	    for (Comment comment : comments) {
	        if (comment.getParent() == null) {
	            // 부모 댓글인 경우
	            CommentDto commentDto = createCommentDto(comment, postId);
	            commentDtos.add(commentDto);
	            sortChildComments(comment, comments, commentDtos, postId);
	        }
	    }
	    
	    return ResponseEntity.ok(commentDtos);
	}
	
	private void sortChildComments(Comment parentComment, List<Comment> comments, List<CommentDto> commentDtos, Long postId) {
	    for (Comment comment : comments) {
	        if (comment.getParent() == parentComment) {
	            CommentDto commentDto = createCommentDto(comment, postId);
	            commentDtos.add(commentDto);
	            sortChildComments(comment, comments, commentDtos, postId);
	        }
	    }
	}
	
	private CommentDto createCommentDto(Comment comment, Long postId) {
	    CommentDto commentDto = new CommentDto();
	    commentDto.setContent(comment.getContent());
	    commentDto.setPostId(postId);
	    commentDto.setUserId(comment.getUser().getConsumerId());
	    commentDto.setUserName(comment.getUser().getName());
	    commentDto.setProfilePhoto(comment.getUser().getProfilePhoto());
	    commentDto.setCreateDate(comment.getCreateDate());
	    commentDto.setCommentId(comment.getCommentId());
	    commentDto.setCommentType(comment.getCommentType());
	    if (comment.getParent() != null) {
	        commentDto.setParentId(comment.getParent().getCommentId());
	    }
	    return commentDto;
	}
	
	
	
	@GetMapping("/comment_delete")
	public ResponseEntity<?> commentDel(@RequestParam("commentId") Long commentId) {
		Comment thisComment = commuService.getThisComment(commentId);
		List<Comment> allComments = commuService.getAllComments();
		
		delChildComment(thisComment, allComments);
		System.out.println("삭제된 댓글 : " + thisComment.getCommentId());
		commuService.delComment(commentId);
		
		
		return ResponseEntity.ok().build();
	}
	
	private void delChildComment(Comment parentComment, List<Comment> allComments) {
		for(Comment comment:allComments) {
			if(comment.getParent() != null && parentComment.getCommentId() == comment.getParent().getCommentId()) {
				System.out.println(parentComment.getCommentId()+"의 자식답글 : "+comment.getCommentId());
				delChildComment(comment, allComments);
				System.out.println("삭제된 댓글 : " + comment.getCommentId());
				commuService.delComment(comment.getCommentId());
			}
		}
	}
	
	
	
	@GetMapping("/comment_revise")
	public ResponseEntity<?> commentRevise(@RequestParam("commentId") Long commentId,
										   @RequestParam("content") String content,
										   @RequestParam("commentType") int commentType) {
		
		commuService.reviseComment(commentId, content, commentType);
		return ResponseEntity.ok().build();
	}
	
}

