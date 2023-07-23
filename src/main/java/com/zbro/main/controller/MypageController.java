package com.zbro.main.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zbro.main.repository.RoomOptionRepository;
import com.zbro.main.repository.RoomOptionTypeRepository;
import com.zbro.main.service.MypageService;
import com.zbro.main.service.UserService;
import com.zbro.model.Comment;
import com.zbro.model.Community;
import com.zbro.model.ConsumerUser;
import com.zbro.model.Favorite;
import com.zbro.model.Room;
import com.zbro.model.RoomOption;
import com.zbro.model.RoomOptionType;
import com.zbro.type.PostType;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller
public class MypageController {
	
	@Autowired
	private MypageService mypageService;
	
	@Autowired
	private UserService userService;
	
	
	
	@GetMapping("/mypage/favorite/compare")
	public String favoriteComparePage(Model model) {

	    List<Favorite> favorites = mypageService.getFavoritesByUserConsumerId();
	    List<RoomOption> roomOptions = mypageService.getAllRoomOptions();
	    ConsumerUser consumerUser = mypageService.getConsumerUser();
        List<RoomOptionType> roomOptionTypes = mypageService.getAllRoomOptionTypes();
        
        
        model.addAttribute("roomOptions", roomOptions);
        model.addAttribute("favorites", favorites);
        model.addAttribute("roomOptionTypes", roomOptionTypes);
        model.addAttribute("consumerUser", consumerUser);
        
        return "main/mypage/favoriteCompare";
    }
	
	

	@PostMapping("/saveMemo")
	public ResponseEntity<String> saveMemo(@RequestParam("favoriteId") Long favoriteId, @RequestParam("memo") String memo) {
	    mypageService.saveMemo(favoriteId, memo);
	    return ResponseEntity.ok().build();
	}
	

	@GetMapping("/mypage/favorite/list")
	public String favoriteListPage(Model model) {
		List<Favorite> favorites = mypageService.getFavoritesByUserConsumerId();
		ConsumerUser consumerUser = mypageService.getConsumerUser();
		
                      
        model.addAttribute("favorites", favorites);  
        model.addAttribute("consumerUser", consumerUser);

        return "main/mypage/favoriteList";
    }
	
		
	
    @DeleteMapping("/favorites/{favoriteId}")
    public ResponseEntity<String> deleteFavorite(@PathVariable Long favoriteId) {
    	mypageService.deleteFavorite(favoriteId);
        return ResponseEntity.ok("Favorite deleted");
    }	
    
    @GetMapping("/mypage/content/list")
    public String getAllCommunities(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "꿀팁") String type) {

		Page<Community> tipsPage = mypageService.getCommunitiesAllTips(page, size);
		Page<Community> questionsPage = mypageService.getCommunitiesAllQuestions(page, size);
		ConsumerUser consumerUser = mypageService.getConsumerUser();
		
		List<Community> tips = tipsPage.getContent();
		List<Community> questions = questionsPage.getContent();
		
		model.addAttribute("tips", tips);
		model.addAttribute("questions", questions);
		model.addAttribute("consumerUser", consumerUser);
		model.addAttribute("type", type);
		model.addAttribute("tipsPage", tipsPage); // 페이징
		model.addAttribute("questionsPage", questionsPage); // 페이징
		
		return "main/mypage/contentList";
		}
	
    @GetMapping("/mypage/comment/list")
    public String getAllComments(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 Model model) {
        Page<Comment> comments = mypageService.getCommentsByUser(page, size);
        ConsumerUser consumerUser = mypageService.getConsumerUser();

        model.addAttribute("comments", comments);
        model.addAttribute("consumerUser", consumerUser);
        
        return "main/mypage/commentList";
    }
    
    
    @GetMapping("/sidebar")
    public String getSidebar(Model model) {
        ConsumerUser consumerUser = mypageService.getConsumerUser();

        model.addAttribute("consumerUser", consumerUser);

        return "main/mypage/sidebar";
    }
	
    @GetMapping("/mypage/member/info")
    public String getMenberInfo(Model model) {
        ConsumerUser consumerUser = mypageService.getConsumerUser();

        model.addAttribute("consumerUser", consumerUser);

        return "main/mypage/memberInfo";
    }
	
    @PostMapping("/memberInfo")
    public String updateUserInfo(@RequestParam("imageUpload") MultipartFile file, ConsumerUser consumerUser, Model model) {
        ConsumerUser consumerUserUP = mypageService.getConsumerUser();
        consumerUserUP.setPassword(consumerUser.getPassword());
        consumerUserUP.setName(consumerUser.getName());

        if (file != null && !file.isEmpty()) {
            try {
                String fileName = userService.profileImageSave(file);
                consumerUserUP.setProfilePhoto(fileName);
            } catch (Exception e) {
                log.error("Error saving profile image: ", e);
                model.addAttribute("response", "error");
                return "redirect:/memberInfo";
            }
        }

        mypageService.updateUserInfo(consumerUserUP);

        return "redirect:/mypage/member/info";
    }

    
    @PostMapping("/memberDelete")
    public String memberDelete() {
      mypageService.memberDelete();
      return "redirect:/logout"; 
    }
    
  
    @GetMapping("/mypage")
    public String myMainPage(Model model) {
        List<Favorite> favorites = mypageService.getFavoritesByUserConsumerId();
        List<Community> top10Contents = mypageService.getTop10();
        List<Comment> comments = mypageService.getComments10ByUser();
        ConsumerUser consumerUser = mypageService.getConsumerUser();

        model.addAttribute("favorites", favorites);
        model.addAttribute("top10Contents", top10Contents);
        model.addAttribute("comments", comments);
        model.addAttribute("consumerUser", consumerUser);

        return "main/mypage/myMain";
    }
    

}
	



