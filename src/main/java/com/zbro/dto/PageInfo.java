package com.zbro.dto;

import lombok.Data;

@Data
public class PageInfo {	//페이징처리에 필요한 정보 클래스
	
	// 입력받는 데이터
	private int totalCnt;		// 총 게시물 개수
	private int pageSize;		// 한 페이지의 게시물 수
	private int naviSize = 10;	// 페이지 네이게이션 크기
	private int totalPage;		// 전체 페이지 개수
	private int page = 1;		// 현재 페이지
	private int beginPage;		// 네비의 첫번째 페이지
	private int endPage;		// 네비의 마지막 페이지
	private boolean showPrev;	// 이전 페이지 이동 링크 여부
	private boolean showNext;	// 다음 페이지 이동 링크 여부
	private boolean showBegin;	// 맨 첫페이지
	private boolean showEnd;	// 맨 마지막페이지
	private String searchWord;
	private String searchType;
	private String categoryType;
	
	
	// 생성자
	public PageInfo() {
		this.pageSize = 10;
	}
	
					// 매개변수 : 총게시물 수, 현재페이지, 한페이지 게시물수, 검색단어, 검색타입
	public PageInfo(int totalCnt, int page, int pageSize, String searchWord, String searchType, String categoryType) {
		this.totalCnt = totalCnt;
		this.page = page;
		this.pageSize = pageSize;
		this.searchType = searchType;
		this.searchWord = searchWord;
		this.categoryType = categoryType;
		
		totalPage = (int)Math.ceil(totalCnt / (double)pageSize);	// Math.ceil : 올림
		beginPage = page / naviSize * naviSize + 1;
		endPage = Math.min(beginPage + naviSize - 1, totalPage);
		showPrev = (beginPage != 1);
		showNext = (endPage != totalPage);
		showBegin = (beginPage != 1);
		showEnd = (endPage != totalPage);
	}
	
}
