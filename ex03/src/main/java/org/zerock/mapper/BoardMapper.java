package org.zerock.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.zerock.domain.BoardVO;
import org.zerock.domain.Criteria;

public interface BoardMapper {
	
	// xml 파일에 sql문 처리 했으니까 주석 처리
	// @Select("select * from tbl_board where bno > 0")
	public List<BoardVO> getList();
	
	// 페이징 처리한 목록
	public List<BoardVO> getListWithPaging(Criteria cri);
	
	// 전체 게시물의 수
	public int getTotalCount(Criteria cri);
	
	// insert만 처리되고 생성된 PK값을 알 필요가 없는 경우
	public void insert(BoardVO board);
	
	// insert문이 실행되고 생성된 PK값을 알아야 하는 경우
	public void insertSelectKey(BoardVO board);
	
	public BoardVO read(Long bno);
	
	public int delete(Long bno);
	
	public int update(BoardVO board);
	
	// 댓글 갯수 업데이트
	// MyBatis의 SQL 처리는 기본적으로 하나의 파라미터 타입을 사용, 따라서 2개 이상의 데이터를 전달하려면 @Param 어노테이션 이용
	public void updateReplyCnt(@Param("bno") Long bno, @Param("amount") int amount);

}
