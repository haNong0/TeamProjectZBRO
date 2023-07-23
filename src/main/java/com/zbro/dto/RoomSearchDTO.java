package com.zbro.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.zbro.model.Room;
import com.zbro.type.CostType;
import com.zbro.type.RoomType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomSearchDTO {
	
	/*### Search Field ###*/
	private String searchWord = "";
	private RoomType type = RoomType.none;
	private CostType costType = CostType.none;
	/*### Search Field ###*/
	
	private Long roomId;
	private String address;
	private String intro;
	private int monthCost;
	private int deposit;
	private Long favoriteId;
	private boolean isFavorite = false;
	
	
	public RoomSearchDTO(Room room) {
		this.roomId = room.getRoomId();
		this.type = (room.getType() == null)? RoomType.none : room.getType();
		this.costType = (room.getCostType() == null)? CostType.none : room.getCostType();
		this.address = (room.getAddress() == null)? "" : room.getAddress();
		this.intro = (room.getIntro() == null)? "" : room.getIntro();
		this.monthCost = room.getMonthCost();
		this.deposit = room.getDeposit();
	}
	
	public static List<RoomSearchDTO> convertToDTO(List<Room> rooms) {
	        return rooms.stream()
	                    .map(RoomSearchDTO::new)
	                    .collect(Collectors.toList());
    }
	
//	public void setType(RoomType type) {
//		if(type == null) {
//			this.type = RoomType.none;
//		} else {
//			this.type = type;
//		}
//	}
//	
//	public void setCostType(CostType costType) {
//		if(costType == null) {
//			this.costType = CostType.none;
//		} else {
//			this.costType = costType;
//		}
//	}
	
	
}


