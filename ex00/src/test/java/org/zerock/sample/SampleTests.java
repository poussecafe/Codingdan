package org.zerock.sample;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

// 현재 테스트 코드가 스프링을 실행하는 역할을 할 것이라는 걸 표시
@RunWith(SpringJUnit4ClassRunner.class)
// 필요한 객체들을 스프링의 빈으로 등록, 스프링이 실행되면서 어떤 설정 정보를 읽어 들여야 하는지 명시
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
// Spring Legacy Project로 생성하는 경우 별도의 설정 없이 사용 가능
@Log4j
public class SampleTests {

	@Setter(onMethod_ = {@Autowired})
	private Restaurant restaurant;
	
	// JUnit 테스트 대상 표시
	@Test
	public void testExist() {
		
		// restaurant 변수가 null이 아니어야만 테스트가 성공한다는 의미
		assertNotNull(restaurant);
	
		log.info(restaurant);
		log.info("---------------------");
		log.info(restaurant.getChef());
		
	}
}
