package org.zerock.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.zerock.domain.SampleVO;
import org.zerock.domain.Ticket;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/sample")
@Log4j
public class SampleController {

	// 문자열 반환
	@GetMapping(value="/getText", produces="text/plain; charset=UTF-8")
	public String getText() {
		log.info("MIME TYPE: " + MediaType.TEXT_PLAIN_VALUE);
		
		// @RestController의 경우 문자열을 반환하는 경우 순수한 데이터를 리턴(cf. @Controller의 경우에는 JSP 파일 이름으로 처리)
		return "안녕하세요.";
	}
	
	// XML, JSON 방식의 데이터 반환
	// 기본적으로는 XML 데이터 전송, 확장자 뒤에 .json을 붙이면 JSON 형태의 데이터 전송
	@GetMapping(value="/getSample", produces= {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
	public SampleVO getSample() {
		return new SampleVO(112, "스타", "로드");
	}
	
	// produces 속성은 생략 가능
	@GetMapping(value="/getSample2")
	public SampleVO getSample2() {
		return new SampleVO(113, "로켓", "라쿤");
	}
	
	// 배열이나 리스트, 맵 타입의 객체 전송도 가능
	// ex. list
	@GetMapping(value="/getList")
	public List<SampleVO> getList(){
		
		// 1부터 10미만까지 루프 돌면서 SampleVO 객체를 만들어 List<SampleVO>에 담는다
		return IntStream.range(1, 10).mapToObj(i->new SampleVO(i, i+" First", i+" Last")).collect(Collectors.toList());
	}
	
	// ex. map
	@GetMapping(value="/getMap")
	public Map<String, SampleVO> getMap(){
		Map<String, SampleVO> map = new HashMap<>();
		map.put("First", new SampleVO(111, "그루트", "주니어"));
		
		return map;
	}
	
	// ResponseEntity: 데이터와 함께 HTTP 헤더의 상태 메시지 등을 같이 전달하는 용도
	// height와 weight를 반드시 파라미터로 전달 받는다
	@GetMapping(value="/check", params= {"height", "weight"})
	public ResponseEntity<SampleVO> check(Double height, Double weight){
		
		SampleVO vo = new SampleVO(0, ""+height, ""+weight);
		
		ResponseEntity<SampleVO> result = null;
		
		if(height<150) {
			result=ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(vo);
		} else {
			result=ResponseEntity.status(HttpStatus.OK).body(vo);
		}
		
		return result;
	}
	
	// @PathVariable: URL 경로의 일부를 파라미터로 사용할 때 이용
	// {}안에 지정한 변수명을 가지고 변숫값을 얻을 수 있다, int나 double 같은 기본 자료형은 사용할 수 없다
	@GetMapping("/product/{cat}/{pid}")
	public String[] getPath(@PathVariable("cat") String cat, @PathVariable("pid") Integer pid) {
		return new String[] {"category: "+cat, "productid: "+pid};
	}
	
	// @RequestBody: JSON 데이터를 원하는 타입을 객체로 변환하는 경우에 주로 사용
	// 요청한 내용을 처리하는 것이므로 @PostMapping 적용
	// JSON으로 전달되는 데이터를 Ticket 타입으로 변환
	@PostMapping("/ticket")
	public Ticket convert(@RequestBody Ticket ticket) {
		log.info("convert................ticket"+ticket);
		return ticket;
	}
}
