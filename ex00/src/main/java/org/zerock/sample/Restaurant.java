package org.zerock.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.Setter;

@Component
@Data
public class Restaurant {

	// onMethod: Setter 메서드 생성 시 메서드에 추가할 어노테이션 지정
	// Chef 객체를 Restaurant 객체에 주입
	@Setter(onMethod_ = @Autowired)
	private Chef chef;
}
