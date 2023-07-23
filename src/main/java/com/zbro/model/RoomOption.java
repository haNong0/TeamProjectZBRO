package com.zbro.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class RoomOption {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomOptionId;

	@ManyToOne
    @JoinColumn(name = "room_id")
	private Room room;
	
	@ManyToOne
    @JoinColumn(name = "option_type")
	private RoomOptionType optionType;
	
}
