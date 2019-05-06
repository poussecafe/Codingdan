package org.zerock.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.domain.AttachFileDTO;

import lombok.extern.log4j.Log4j;
import net.coobird.thumbnailator.Thumbnailator;

@Controller
@Log4j
public class UploadController {

	@GetMapping("/uploadForm")
	public void uploadForm() {
		log.info("upload form");
	}

	@PostMapping("/uploadFormAction")
	public void uploadFormPost(MultipartFile[] uploadFile, Model model) {

		String uploadFolder = "C:\\upload";

		for (MultipartFile multipartFile : uploadFile) {
			log.info("-------------------------");
			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
			log.info("Upload File size: " + multipartFile.getSize());

			File saveFile = new File(uploadFolder, multipartFile.getOriginalFilename());

			try {

				// transferTo에 파일 객체를 지정
				multipartFile.transferTo(saveFile);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}
	
	@GetMapping("/uploadAjax")
	public void uploadAjax() {
		log.info("upload ajax");
	}
	
	// 오늘 날짜의 경로를 문자열로 생성
	private String getFolder() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Date date = new Date();
		
		String str = sdf.format(date);
		
		return str.replace("-", File.separator);
	}
	
	// 특정 파일이 이미지 타입인지 검사
	private boolean checkImageType(File file) {
		try {
			String contentType= Files.probeContentType(file.toPath());
			return contentType.startsWith("image");
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/*
	 * @PostMapping("/uploadAjaxAction") public void uploadAjaxPost(MultipartFile[]
	 * uploadFile) {
	 * 
	 * log.info("update ajax post............");
	 * 
	 * String uploadFolder = "C:\\upload";
	 * 
	 * // 업로드 폴더 생성 File uploadPath = new File(uploadFolder, getFolder());
	 * log.info("upload path: "+uploadPath);
	 * 
	 * // 존재하지 않는다면 새로 만든다 if(uploadPath.exists()==false) { uploadPath.mkdirs(); }
	 * 
	 * for (MultipartFile multipartFile : uploadFile) {
	 * log.info("-------------------------"); log.info("Upload File Name: " +
	 * multipartFile.getOriginalFilename()); log.info("Upload File size: " +
	 * multipartFile.getSize());
	 * 
	 * String uploadFileName = multipartFile.getOriginalFilename();
	 * 
	 * uploadFileName =
	 * uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
	 * log.info("only file name: "+uploadFileName);
	 * 
	 * // 파일 이름 중복 방지를 위한 UUID 적용 UUID uuid = UUID.randomUUID(); uploadFileName =
	 * uuid.toString()+"_"+uploadFileName;
	 * 
	 * 
	 * try { // File saveFile = new File(uploadFolder, uploadFileName); File
	 * saveFile = new File(uploadPath, uploadFileName); // transferTo에 파일 객체를 지정
	 * multipartFile.transferTo(saveFile);
	 * 
	 * // 파일이 이미지 타입인지 검사 if(checkImageType(saveFile)) { FileOutputStream thumbnail
	 * = new FileOutputStream(new File(uploadPath, "s_"+uploadFileName));
	 * Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100,
	 * 100); thumbnail.close(); }
	 * 
	 * } catch (Exception e) { log.error(e.getMessage()); } } }
	 */
	
	// 업로드 파일과 관련된 데이터를 AttachDTO로 전달하는 구조로 수정
	@PostMapping(value="/uploadAjaxAction", produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<AttachFileDTO>> uploadAjaxPost(MultipartFile[] uploadFile) {

		log.info("update ajax post............");
		
		List<AttachFileDTO> list = new ArrayList<>();
		String uploadFolder = "C:\\upload";
		
		String uploadFolderPath = getFolder();
		
		// 업로드 폴더 생성
		File uploadPath = new File(uploadFolder, uploadFolderPath);
		log.info("upload path: "+uploadPath);
		
		// 존재하지 않는다면 새로 만든다
		if(uploadPath.exists()==false) {
			uploadPath.mkdirs();
		}

		for (MultipartFile multipartFile : uploadFile) {
			log.info("-------------------------");
			log.info("Upload File Name: " + multipartFile.getOriginalFilename());
			log.info("Upload File size: " + multipartFile.getSize());
			
			AttachFileDTO attachDTO = new AttachFileDTO();

			String uploadFileName = multipartFile.getOriginalFilename();
			
			uploadFileName = uploadFileName.substring(uploadFileName.lastIndexOf("\\")+1);
			log.info("only file name: "+uploadFileName);
			attachDTO.setFileName(uploadFileName);
			
			// 파일 이름 중복 방지를 위한 UUID 적용
			UUID uuid = UUID.randomUUID();
			uploadFileName = uuid.toString()+"_"+uploadFileName;
			
			
			try {
				// File saveFile = new File(uploadFolder, uploadFileName);
				File saveFile = new File(uploadPath, uploadFileName);
				// transferTo에 파일 객체를 지정
				multipartFile.transferTo(saveFile);
				
				attachDTO.setUuid(uuid.toString());
				attachDTO.setUploadPath(uploadFolderPath);
				
				// 파일이 이미지 타입인지 검사
				if(checkImageType(saveFile)) {
					
					attachDTO.setImage(true);
					
					FileOutputStream thumbnail = new FileOutputStream(new File(uploadPath, "s_"+uploadFileName));
					Thumbnailator.createThumbnail(multipartFile.getInputStream(), thumbnail, 100, 100);
					thumbnail.close();
				}
				
				list.add(attachDTO);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	// 섬네일 데이터를 브라우저로 전송
	@GetMapping("/display")
	@ResponseBody
	public ResponseEntity<byte[]> getFile(String fileName){
		log.info("fileName: "+fileName);
		File file = new File("c:\\upload\\"+fileName);
		log.info("file: "+file);
		ResponseEntity<byte[]> result=null;
		
		try {
			HttpHeaders header = new HttpHeaders();
			
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
