package org.zerock.domain;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Criteria {

	// 페이징을 위한 파라미터
	private int pageNum; // 페이지 번호
	private int amount; // 한 페이지당 몇 개의 데이터를 보여줄 건지
	
	// 검색을 위한 파라미터
	private String type; // 검색 조건
	private String keyword; // 검색어
	
	public Criteria() {
		this(1, 10);
	}
	
	public Criteria(int pageNum, int amount) {
		this.pageNum=pageNum;
		this.amount=amount;
	}
	
	public String[] getTypeArr() {
		return type==null? new String[] {}:type.split("");
	}
	
	// 게시물 삭제 후 페이지 번호나 검색 조건 유지하면서 이동하기 위한 메서드 
	public String getListLink() {
		
		// UriComponentsBuilder: 브라우저에서 GET 방식 등의 파라미터 전송에 사용되는 문자열(쿼리스트링)을 손쉽게 처리할 수 있는 클래스
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("pageNum", this.pageNum)
				.queryParam("amount", this.getAmount())
				.queryParam("type", this.getType())
				.queryParam("keyword", this.getKeyword());
		
		return builder.toUriString();
	}
}
