package org.zerock.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.log4j.Log4j;

// AOP: 핵심적인 로직은 아니지만 프로그램에서 필요한 공통 관심사 분리
// 공통적인 예외사항을 @ControllerAdvice를 이용해서 분리
// 해당 객체가 스프링 컨트롤러에서 발생하는 예외를 처리하는 존재임을 명시
@ControllerAdvice
@Log4j
public class CommonExceptionAdvice {

	// 해당 메서드가 () 들어가는 예외 타입을 처리
	@ExceptionHandler(Exception.class)
	public String except(Exception ex, Model model) {
		
		log.error("Exception..................."+ex.getMessage());
		model.addAttribute("exception", ex);
		log.error(model);
		
		return "error_page";
	}
}
