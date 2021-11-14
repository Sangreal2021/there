package kr.co.there.place.controller;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import kr.co.there.place.model.entity.PlaceVo;
import kr.co.there.place.service.PlaceService;
import kr.co.there.review.model.entity.ReviewVo;
import lombok.extern.log4j.Log4j;

@Log4j
@Controller
//@RequestMapping("/admin/place")
public class PlaceController {
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	PlaceService placeService;
	
	
	// ===== admin page =====
	@GetMapping("/admin/place")
	public String list(Model model) throws Exception {
		model.addAttribute("list", placeService.list());
		return "/admin/place/admin_place_list";
	}

	@GetMapping("/admin/place/{place_idx}")
	public String detail(@PathVariable("place_idx") int place_idx, Model model) throws SQLException {
		HashMap<String, Object> map = new HashMap<>();
		map = placeService.One(place_idx, false, false);
		model.addAttribute("likeCnt", map.get("likeCnt"));
		model.addAttribute("reviewCnt", map.get("reviewCnt"));
		model.addAttribute("plbean", map.get("placeInfo"));
		model.addAttribute("scoreAvg", map.get("scoreAvg"));
		
		return "/admin/place/admin_place_detail";
	}
	
	@GetMapping("/admin/place/form")
	public String moveAddPage() {
		return "/admin/place/admin_place_add";
	}
	
	@PostMapping("/admin/place")
	public String add(MultipartFile file, Model model, HttpServletRequest request,
			String place_category, String place_name, String place_addr, String place_opentime, 
			String place_endtime, String place_tel, String place_url, String place_content, 
			String place_longitude, String place_latitude, String place_thumb, String place_hashtag) throws SQLException {		
		
		PlaceVo bean = new PlaceVo();
		bean.setPlace_category(place_category);
		bean.setPlace_name(place_name);
		bean.setPlace_addr(place_addr);
		bean.setPlace_opentime(place_opentime);
		bean.setPlace_endtime(place_endtime);
		bean.setPlace_tel(place_tel);
		bean.setPlace_url(place_url);
		bean.setPlace_content(place_content);
		bean.setPlace_hashtag(place_hashtag);
		bean.setPlace_latitude(Float.parseFloat(place_latitude));
		bean.setPlace_longitude(Float.parseFloat(place_longitude));
		
        if (!file.isEmpty()) {
        	String savePath = request.getSession().getServletContext().getRealPath("/") + "resources\\img\\place\\";
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            bean.setPlace_thumb(filename);
            log.info(savePath + filename);
       
            try {
				file.transferTo(new File(savePath + filename));
				placeService.add(bean);
				 
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
        }
		
		return "redirect:/admin/place";
	}
	
	@GetMapping("/admin/place/form/{place_idx}")
	public String moveEditPage(@PathVariable("place_idx") int place_idx, Model model) throws SQLException {
		HashMap<String, Object> map = new HashMap<>();
		map = placeService.One(place_idx, false, false);
		model.addAttribute("plbean", map.get("placeInfo"));
		
		return "/admin/place/admin_place_edit";
	}
	
//	@PutMapping("/admin/place/{place_idx}")
//	public String edit(@PathVariable("place_idx") int place_idx, PlaceVo bean, Model model) throws SQLException {
//		model.addAttribute("plbean", placeService.edit(bean));
//		return "redirect:/admin/place/{place_idx}";
//	}
	@PostMapping("/admin/place/{place_idx}")
	public String edit(@PathVariable("place_idx") int place_idx, MultipartFile file, Model model, HttpServletRequest request,
			String place_category, String place_name, String place_addr, String place_opentime, 
			String place_endtime, String place_tel, String place_url, String place_content, 
			String place_longitude, String place_latitude, String place_thumb, String place_hashtag) throws SQLException {		
		
		PlaceVo bean = new PlaceVo();
		bean.setPlace_idx(place_idx);
		bean.setPlace_category(place_category);
		bean.setPlace_name(place_name);
		bean.setPlace_addr(place_addr);
		bean.setPlace_opentime(place_opentime);
		bean.setPlace_endtime(place_endtime);
		bean.setPlace_tel(place_tel);
		bean.setPlace_url(place_url);
		bean.setPlace_content(place_content);
		bean.setPlace_hashtag(place_hashtag);
		bean.setPlace_latitude(Float.parseFloat(place_latitude));
		bean.setPlace_longitude(Float.parseFloat(place_longitude));
		
        if (!file.isEmpty()) {
        	String savePath = request.getSession().getServletContext().getRealPath("/") + "resources\\img\\place\\";
            String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            bean.setPlace_thumb(filename);
            log.info(savePath + filename);
       
            try {
				file.transferTo(new File(savePath + filename));
				placeService.edit(bean);
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
        } else {
        	bean.setPlace_thumb(place_thumb);
        	placeService.edit(bean);
        }
		
        return "redirect:/admin/place/{place_idx}";
	}
	
	
	@DeleteMapping("/admin/place/{place_idx}")
	public String remove(@PathVariable("place_idx") int param) throws SQLException {
		placeService.remove(param);
		return "redirect:/admin/place";
	}
	
	@GetMapping("/admin/place/review")
	public String ReviewList(Model model) throws SQLException {
		model.addAttribute("list", placeService.reviewList());
		return "/admin/place/admin_place_review";
	}

	
	
	// ===== home page =====
	@GetMapping("/categroy")
	public String showCategoryPage(Model model) throws SQLException {
		model.addAttribute("list", placeService.listHome());
		return "/home/place/place-by-category";
	}
	
	@GetMapping("/location")
	public String moveLocationPage() {
		return "/home/place/place-by-location";
	}
	
	@GetMapping("/place/{place_idx}")
	public String showPlaceDetailPage(@PathVariable("place_idx") int place_idx, Model model) throws SQLException {
		HashMap<String, Object> map = new HashMap<>();
		map = placeService.One(place_idx, true, true);
		model.addAttribute("plbean", map.get("placeInfo"));
		model.addAttribute("rvlist", map.get("reviewList"));
		model.addAttribute("likeCnt", map.get("likeCnt"));
		model.addAttribute("reviewCnt", map.get("reviewCnt"));
		model.addAttribute("scoreAvg", map.get("scoreAvg"));
		
		//System.out.println(map.get("placeInfo"));
		
		return "/home/place/place-detail";
	}
	
	@PostMapping("/place/{place_idx}")
	public String addReview(ReviewVo bean, Model model) throws SQLException {
		model.addAttribute("rvbean", placeService.addReveiw(bean));
		return "redirect:/place/{place_idx}";
	}
	
	
	

}
