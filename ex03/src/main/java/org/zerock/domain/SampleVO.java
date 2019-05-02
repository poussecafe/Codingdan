package org.zerock.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
// 모든 속성을 사용하는 생성자 생성
@AllArgsConstructor
// 비어있는 생성자 생성
@NoArgsConstructor
public class SampleVO {

	private Integer mno;
	private String firstName;
	private String lastName;
}
