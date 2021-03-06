package org.zerock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;
import org.zerock.domain.PageDTO;
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
//	@GetMapping("/list")
//	public void list(Model model) {
//		log.info("list");
//		model.addAttribute("list", service.getList());
//	}
	@GetMapping("/list")
	public void list(Criteria cri, Model model) {
		log.info("list");
		log.info("list: " + cri);
		model.addAttribute("list", service.getList(cri));

		// 페이징 처리를 위해 PageDTO 담아 보내기
		// model.addAttribute("pageMaker", new PageDTO(cri, 123));

		// 실제 전체 게시글 수 구해서 적용
		int total = service.getTotal(cri);
		log.info("total: " + total);

		model.addAttribute("pageMaker", new PageDTO(cri, total));
	}

	// 등록 처리 페이지(입력 페이지를 보여주는 역할만 하기 때문에 별도의 처리가 필요하지 않다)
	@GetMapping("/register")
	public void register() {

	}

	// 등록 처리
	@PostMapping("/register")
	// RedirectAttributes를 파라미터로 지정: 등록 작업이 끝난 후 다시 목록 화면으로 이동하기 위함, 추가적으로 등록된 게시물의
	// 번호를 같이 전달하기 위해서 이용
	public String register(BoardVO board, RedirectAttributes rttr) {
		log.info("register: " + board);

		service.register(board);

		rttr.addFlashAttribute("result", board.getBno());

		// 접두어 'redirect:' 사용: 스프링 MVC가 내부적으로 response.sendRedirect()를 처리
		return "redirect:/board/list";
	}

	// 조회 처리, 수정과 삭제를 위한 화면으로 이동
	// @GetMapping이나 @PostMapping은 URL을 배열로 처리할 수 있다
	@GetMapping({ "/get", "/modify" })
	// 페이지 번호 유지를 위해 Criteria를 파라미터로 받고 전달한다
	// @ModelAttribute는 자동으로 Model에 데이터를 지정한 이름으로 담아준다
	public void get(@RequestParam("bno") Long bno, Model model, @ModelAttribute("cri") Criteria cri) {

		log.info("/get or modify");
		log.info("get: " + cri);
		model.addAttribute("board", service.get(bno));
	}

	// 수정 처리
	@PostMapping("/modify")
	// 페이지 번호 유지를 위해 Criteria 객체를 파라미터로 추가, rttr에 담아서 뷰 페이지로 전달
	public String modify(BoardVO board, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		log.info("modify:" + board);

		// service.modify()는 수정 여부를 boolean으로 처리하므로 성공한 경우에만 RedirectAttributes에 추가
		if (service.modify(board)) {
			rttr.addFlashAttribute("result", "success");
		}

		// 페이징을 위한 파라미터
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		// 검색 조건을 위한 파라미터
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());

		return "redirect:/board/list";
	}

	// 삭체 처리
	@PostMapping("/remove")
	// 페이지 번호 유지를 위해 Criteria 객체를 파라미터로 추가, rttr에 담아서 뷰 페이지로 전달
	public String remove(@RequestParam("bno") Long bno, @ModelAttribute("cri") Criteria cri, RedirectAttributes rttr) {
		log.info("remove...." + bno);

		if (service.remove(bno)) {
			rttr.addFlashAttribute("result", "success");
		}

		// 페이징을 위한 파라미터
		rttr.addAttribute("pageNum", cri.getPageNum());
		rttr.addAttribute("amount", cri.getAmount());
		// 검색 조건을 위한 파라미터
		rttr.addAttribute("type", cri.getType());
		rttr.addAttribute("keyword", cri.getKeyword());

		return "redirect:/board/list";
	}
}
