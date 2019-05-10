package org.zerock.service;

import java.util.List;

import org.zerock.domain.BoardAttachVO;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardService {
	
	public void register(BoardVO board);
	
	public BoardVO get(Long bno);
	
	// 수정 여부를 boolean으로 처리
	public boolean modify(BoardVO board);
	
	public boolean remove(Long bno);
	
	// 목록의 페이징 처리를 위해서는 Criteria 객체를 파라미터로 받아야 함
	public List<BoardVO> getList(Criteria cri);
	
	// 전체 게시글 수
	public int getTotal(Criteria cri);
	
	// 첨부 파일 데이터 가지고 오기
	public List<BoardAttachVO> getAttachList(Long bno);
}
