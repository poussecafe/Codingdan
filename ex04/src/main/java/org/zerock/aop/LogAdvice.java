package org.zerock.aop;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j;

// 해당 객체가 Aspect를 구현한 것임을 나타낸다
@Aspect
@Log4j
// 스프링에서 빈으로 인식하기 위해 사용
@Component
public class LogAdvice {

	// @Before: BeforeAdvice를 구현한 메서드에 추가
	@Before("execution(* org.zerock.service.SampleService*.*(..))")
	public void logBefore() {
		
		log.info("=====================================");
	}
	
	// 전달된 파라미터까지 파악해보기
	@Before("execution(* org.zerock.service.SampleService*.doAdd(String, String)) && args(str1, str2)")
	public void logBeforeWithParam(String str1, String str2) {
		log.info("str1: "+str1);
		log.info("str2: "+str2);
	}
	
	// 지정된 대상이 예외를 발생한 후에 동작
	@AfterThrowing(pointcut = "execution(* org.zerock.service.SampleService*.*(..))", throwing="exception")
	public void logException(Exception exception) {
		log.info("Exception...........!!!!!!!!");
		log.info("exception: "+exception);
	}
	
	// @Around: 직접 대상 메서드를 실행할 수 있는 권한을 가짐, 메서드의 실행 전과 실행 후에 처리 가능, 리턴 타입은 void가 아니게 설정하고 메서드의 실행 결과도 직접 반환하는 형태로만 작성
	// ProceedingJoinPoint는 @Around와 결합해서 파라미터나 예외 등을 처리
	@Around("execution(* org.zerock.service.SampleService*.*(..))")
	public Object logTime(ProceedingJoinPoint pjp) {
		long start = System.currentTimeMillis();
		
		log.info("Target: "+pjp.getTarget());
		log.info("Param: "+Arrays.toString(pjp.getArgs()));
		
		Object result = null;
		
		try {
			result = pjp.proceed();
		} catch (Throwable e){
			e.printStackTrace();
		}
		
		long end = System.currentTimeMillis();
		
		log.info("TIME: "+(end-start));
		
		return result;
	}
}
