package org.zerock.mapper;

import java.util.List;

import org.zerock.domain.BoardAttachVO;

public interface BoardAttachMapper {
	
	public void insert(BoardAttachVO vo);
	
	// 첨부 파일 삭제
	public void delete(String uuid);
	
	public List<BoardAttachVO> findByBno(Long bno);
	
	// 게시물과 첨부 파일 삭제
	public void deleteAll(Long bno);
	
	// 어제 등록된 모든 첨부 파일의 목록 가져오기
	public List<BoardAttachVO> getOldFiles();

}
