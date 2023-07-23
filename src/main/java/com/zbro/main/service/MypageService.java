package com.zbro.main.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.zbro.main.repository.CommentRepository;
import com.zbro.main.repository.CommunityRepository;
import com.zbro.main.repository.ConsumerUserRepository;
import com.zbro.main.repository.MypageRepository;
import com.zbro.main.repository.RoomOptionRepository;
import com.zbro.main.repository.RoomOptionTypeRepository;
import com.zbro.model.Comment;
import com.zbro.model.Community;
import com.zbro.model.ConsumerUser;
import com.zbro.model.Favorite;
import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomOptionType;
import com.zbro.type.PostType;
import com.zbro.type.UserStatusType;

@Service
public class MypageService {
	
	@Autowired
	private MypageRepository mypageRepository;
	
	@Autowired
	private RoomOptionRepository roomOptionRepository;
	
	@Autowired
	private RoomOptionTypeRepository roomOptionTypeRepository;

	
	@Autowired
	private CommunityRepository communityRepository;
	
	@Autowired
	private ConsumerUserRepository consumerUserRepository;
	
	@Autowired
	private CommentRepository commentRepository;

	
    private Long getLoggedInUserId() {
        return 1L;
    }	
	
	
    public List<Favorite> getFavoritesByUserConsumerId() {
        Long userId = getLoggedInUserId();  //아이디
        return mypageRepository.findByUserConsumerId(userId);
    }
      
    public List<RoomOptionType> getAllRoomOptionTypes() {
        return roomOptionTypeRepository.findAll();
    }
    
    public List<RoomOption> getAllRoomOptions() {
        return roomOptionRepository.findAll();
    }
    
   
    public void saveMemo(Long favoriteId, String memo) {
        Favorite favorite = mypageRepository.findById(favoriteId).orElse(null);
        if (favorite != null) {
            favorite.setMemo(memo);
            mypageRepository.save(favorite);
        }
    }
    
    
    
    public void deleteFavorite(Long favoriteId) {
        mypageRepository.deleteById(favoriteId);
    }
    
    
    public Page<Community> getCommunitiesAllTips(int page, int size) {
        Long userId = getLoggedInUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return communityRepository.findByUserConsumerIdAndTypeOrderByCreateDateDesc(userId, PostType.꿀팁, pageable);
    }

    public Page<Community> getCommunitiesAllQuestions(int page, int size) {
        Long userId = getLoggedInUserId();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return communityRepository.findByUserConsumerIdAndTypeOrderByCreateDateDesc(userId, PostType.질문, pageable);
    }
	
	public Page<Comment> getCommentsByUser(int page, int size) {
	    Long userId = getLoggedInUserId();
	    Pageable pageable = PageRequest.of(page, size);
	    return commentRepository.findByUserConsumerId(userId, pageable);
	}
	
	
	public ConsumerUser getConsumerUser() {
	    Long userId = getLoggedInUserId();
	    return consumerUserRepository.findByConsumerId(userId);
	}
	
    public ConsumerUser updateUserInfo(ConsumerUser consumerUser) {       
        return consumerUserRepository.save(consumerUser);
    }
    
    public void memberDelete() {
        Long userId = getLoggedInUserId();
        ConsumerUser consumerUser = consumerUserRepository.findByConsumerId(userId);
        consumerUser.setStatus(UserStatusType.DELETE);
        consumerUserRepository.save(consumerUser);
    }
    
    public List<Community> getTop10() {
        Long userId = getLoggedInUserId();
        return communityRepository.findTop8ByUserConsumerIdOrderByCreateDateDesc(userId);
    }


    
	public List<Comment> getComments10ByUser() {
		Long userId = getLoggedInUserId();
		return commentRepository.findTop8ByUserConsumerIdOrderByCreateDateDesc(userId);
	}




    
}
