package org.zerock.domain;

import lombok.Data;

@Data
public class BoardAttachVO {

	private String uuid;
	private String uploadPath; // 실제 파일이 업로드 된 경로
	private String fileName;
	private boolean fileType;
	
	private Long bno;
}
