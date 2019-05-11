package org.zerock.task;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.zerock.domain.BoardAttachVO;
import org.zerock.mapper.BoardAttachMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class FileCheckTask {

	@Setter(onMethod_ = { @Autowired })
	private BoardAttachMapper attachMapper;

	private String getFolderYesterDay() {
		SimpleDateFormat sdf = new SimpleDateFormat();

		Calendar cal = Calendar.getInstance();

		cal.add(Calendar.DATE, -1);

		String str = sdf.format(cal.getTime());

		return str.replace("-", File.separator);
	}

	// 스케줄러: cron이라는 속성을 부여해서 주기를 제어
	// 순서대로 seconds, minutes, hours, day, months, day of week, year(optional)
	// 매일 새벽 2시에 동작
	@Scheduled(cron = "0 0 2 * * *")
	public void checkFiles() throws Exception {
		log.warn("File Check Task run........................");
		log.warn(new Date());

		// 데이터베이스의 파일 목록
		List<BoardAttachVO> fileList = attachMapper.getOldFiles();

		// 디렉토리 안의 파일과 데이터베이스의 파일 목록 비교
		List<Path> fileListPaths = fileList.stream()
				.map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), vo.getUuid() + "_" + vo.getFileName()))
				.collect(Collectors.toList());

		// 섬네일 파일을 가진 이미지 파일
		fileList.stream().filter(vo -> vo.isFileType() == true)
				.map(vo -> Paths.get("C:\\upload", vo.getUploadPath(), "s_" + vo.getUuid() + "_" + vo.getFileName()))
				.forEach(p -> fileListPaths.add(p));

		log.warn("=================================================");

		fileListPaths.forEach(p -> log.warn(p));

		// 어제자 디렉토리에 있는 파일
		File targetDir = Paths.get("C:\\upload", getFolderYesterDay()).toFile();

		File[] removeFiles = targetDir.listFiles(file -> fileListPaths.contains(file.toPath()) == false);

		log.warn("------------------------------------------------");
		for (File file : removeFiles) {
			log.warn(file.getAbsolutePath());
			file.delete();
		}

	}
}
