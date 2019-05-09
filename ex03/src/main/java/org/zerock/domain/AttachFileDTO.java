package org.zerock.domain;

import lombok.Data;

@Data
public class AttachFileDTO {

	private String fileName; // 원본 파일 이름
	private String uploadPath; // 파일 업로드 경로
	private String uuid; // UUID
	private boolean image; // 이미지 파일인지 여부
	
}
