package org.zerock.mapper;

import org.apache.ibatis.annotations.Select;

public interface TimeMapper {

	@Select("SELECT sysdate FROM dual")
	public String getTime();
	
	// 메서드 선언은 인터페이스에 존재, SQL 처리는 XML을 이용
	public String getTime2();
}
