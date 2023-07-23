package com.zbro.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CommentDto {
	private String content;
    private Long postId;
    private Long userId;
    private String userName;
    private String profilePhoto;
    private LocalDateTime createDate;
    private Long commentId;
    private Long parentId;
    private int commentType;
}
