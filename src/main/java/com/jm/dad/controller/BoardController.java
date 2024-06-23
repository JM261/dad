package com.jm.dad.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import com.jm.dad.model.entity.Board;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BoardController {

	/**
	 * @DESC   : 게시물 등록 및 수정 페이지
	 * @UPDATE : 
	 */
	@GetMapping("/board")
	public ModelAndView boardUpdatePage(HttpServletRequest request, Board board) {
		
		ModelAndView mav = new ModelAndView("thymeleaf/user/board/board-update");
		
        mav.addObject("updateForm", board);
        
		return mav;
	}
}
