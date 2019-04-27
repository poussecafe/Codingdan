package org.zerock.sample;

import org.springframework.stereotype.Component;

import lombok.Data;

// @Component: 자바 클래스를 스프링 빈으로 표시, 스프링의 component-scanning을 통해 해당 클래스가 Application context에 빈으로 등록된다.
@Component
@Data
public class Chef {

}
