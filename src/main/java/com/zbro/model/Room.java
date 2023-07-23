package com.zbro.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;

import com.zbro.type.CostType;
import com.zbro.type.RoomType;

import lombok.Data;

@Data
@Entity
public class Room {

	@Comment("매물_번호")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
	
	@Comment("판매자_id")
	@ManyToOne
	@JoinColumn(name = "seller_id")
	private SellerUser seller;
	
	@Comment("방종류 - 원룸,오피스텔,고시원")
	@Column(nullable = false, length = 20)
	@Enumerated(EnumType.STRING)
	private RoomType type;
	
	@Comment("매물_간단소개")
	@Column(nullable = false)
	private String intro;
	
	@Comment("매물_상세소개")
	@Lob
	private String description;
	
	@Comment("건물명")
	@Column(length = 100)
	private String buildingName;
	
	@Comment("주소")
	private String address;
	
	@Comment("호수")
	@Column(length = 30)
	private String roomNumber;
	
	@Comment("월세")
	private int	monthCost = 0;
	
	@Comment("보증금")
	private int deposit = 0;
	
	@Comment("관리비")
	private int manageCost = 0;
	
	@Comment("전/월세 구분")
	@Column(length = 20)
	@Enumerated(EnumType.STRING)
	private CostType costType;
	
	@Comment("면적")
	private double size;
	
	@Comment("방향")
	@Column(length = 10)
	private String direction;
	
	@Comment("교통정보(대중교통)")
	@Column(length = 100)
	private String transportInfo;
	
	@Comment("건물_층수")
	private int totalFloor;
	
	@Comment("매물_층수")
	private int roomFloor;
	
	@Comment("즉시입주여부")
	private boolean isRoomIn;
	
	@Comment("안전(보안)")
	@Column(length = 50)
	private String securityInfo;
	
	@Comment("엘리베이터_여부")
	private boolean isElevator;
	
	@Comment("냉난방여부(중앙,개별)")
	@Column(length = 30)
	private String heatInfo;
	
	@Comment("주차장_정보")
	@Column(length = 30)
	private String parkingInfo;
	
	@Comment("인터넷_정보")
	@Column(length = 50)
	private String internetInfo;
	
	@Comment("성별분리_정보")
	@Column(length = 30)
	private String genderInfo;
	
	@Comment("제공식사_고시원")
	@Column(length = 100)
	private String foodOffer;
	
	@Comment("제공비품_고시원")
	@Column(length = 100)
	private String amenityOffer;
	
	@Comment("조회수")
	@ColumnDefault("0")
	private int viewCount;
	
	@Comment("등록일")
	@Column(updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private LocalDateTime createDate;


}
