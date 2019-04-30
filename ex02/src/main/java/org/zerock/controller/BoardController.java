package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.service.BoardService;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;

// 스프링의 빈으로 인식할 수 있도록 해줌
@Controller
@Log4j
// /board/로 시작하는 모든 처리를 BoardController가 하도록 지정
@RequestMapping("/board/*")
// BoardService의 생성자를 만들고 자동으로 주입
@AllArgsConstructor
public class BoardController {

	private BoardService service;
	
	// 목록에 대한 처리
	@GetMapping("/list")
	public void list(Model model) {
		log.info("list");
		model.addAttribute("list", service.getList());
	}
	
	// 등록 처리 페이지(입력 페이지를 보여주는 역할만 하기 때문에 별도의 처리가 필요하지 않다)
	@GetMapping("/register")
	public void register() {
		
	}
	
	// 등록 처리
	@PostMapping("/register")
	// RedirectAttributes를 파라미터로 지정: 등록 작업이 끝난 후 다시 목록 화면으로 이동하기 위함, 추가적으로 등록된 게시물의 번호를 같이 전달하기 위해서 이용
	public String register(BoardVO board, RedirectAttributes rttr) {
		log.info("register: "+board);
		
		service.register(board);
		
		rttr.addFlashAttribute("result", board.getBno());
		
		// 접두어 'redirect:' 사용: 스프링 MVC가 내부적으로 response.sendRedirect()를 처리
		return "redirect:/board/list";
	}
	
	// 조회 처리, 수정과 삭제를 위한 화면으로 이동
	// @GetMapping이나 @PostMapping은 URL을 배열로 처리할 수 있다
	@GetMapping({"/get", "/modify"})
	public void get(@RequestParam("bno") Long bno, Model model) {
		
		log.info("/get or modify");
		model.addAttribute("board", service.get(bno));
	}
	
	// 수정 처리
	@PostMapping("/modify")
	public String modify(BoardVO board, RedirectAttributes rttr) {
		log.info("modify:"+board);
		
		// service.modify()는 수정 여부를 boolean으로 처리하므로 성공한 경우에만 RedirectAttributes에 추가
		if(service.modify(board)) {
			rttr.addFlashAttribute("result", "success");
		}
		
		return "redirect:/board/list";
	}
	
	// 삭체 처리
	@PostMapping("/remove")
	public String remove(@RequestParam("bno") Long bno, RedirectAttributes rttr) {
		log.info("remove...." + bno);
		
		if(service.remove(bno)) {
			rttr.addFlashAttribute("result", "success");
		}
		return "redirect:/board/list";
	}
}
