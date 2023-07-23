package com.zbro.main.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.FavoritRepository;
import com.zbro.model.ConsumerUser;
import com.zbro.model.Favorite;
import com.zbro.model.Room;

@Service
public class FavoriteService {
	
	@Autowired
	private FavoritRepository favRepository;

	public Favorite favorite(Favorite fav) {
		return favRepository.save(fav);
	}
	
	public void unfavorite(Favorite fav) {
		favRepository.deleteById(fav.getFavoriteId());
	}

	public Optional<Favorite> getFavoriteDetail(ConsumerUser user, Room room){
			
		favRepository.findByUserAndRoom(user, room);
		
		return favRepository.findByUserAndRoom(user, room);
		
	}
}
