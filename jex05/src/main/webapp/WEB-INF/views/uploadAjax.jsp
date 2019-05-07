<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
<title>Insert title here</title>
</head>
<style>
.uploadResult {
	width: 100%;
	background-color: gray;
}

.uploadResult ul {
	display: flex;
	flex-flow: row;
	justify-content: center;
	align-items: center;
}

.uploadResult ul li {
	list-style: none;
	padding: 10px;
	align-content: center;
	text-align: center;
}

.uploadResult ul li img {
	width: 100px;
}

.uploadResult ul li span {
	color: white;
}

.bigPictureWrapper {
	position: absolute;
	display: none;
	justify-content: conter;
	align-items: center;
	top: 0%;
	width: 100%;
	height: 100%;
	background-color: gray;
	z-index: 100;
	background: rgba(255, 255, 255, 0.5);
}

.bigPicture {
	position: relative;
	display: flex;
	justify-content: center;
	align-items: center;
}

.bigPicture img {
	width: 600px;
}
</style>
<body>
	<h1>Upload with Ajax</h1>

	<div class="uploadDiv">
		<input type='file' name='uploadFile' multiple>
	</div>

	<!-- 업로드 파일 목록 출력 -->
	<div class='uploadResult'>
		<ul></ul>
	</div>

	<button id="uploadBtn">Upload</button>

	<!-- 이미지 파일 원본 출력 -->
	<div class='bigPictureWrapper'>
		<div class='bigPicture'></div>
	</div>


	<script src="http://code.jquery.com/jquery-latest.min.js"></script>

	<script>
	
	// 이미지 파일 클릭 시 원본 이미지 보여주기
	// 여기 작성하는 이유는 나중에 <a> 태그에서 직접 호출할 수 있도록 해주기 위해
	function showImage(fileCallPath){
		// alert(fileCallPath);
		
		$(".bigPictureWrapper").css("display", "flex").show();
		
		$(".bigPicture").html("<img src='/display?fileName="+encodeURI(fileCallPath)+"'>").animate({width:'100%', height:'100%'}, 1000);
		
	}
	
		$(document).ready(function() {

			// 파일 확장자와 크기 설정하고 검사
			var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
			var maxSize = 5242880; // 3MB

			function checkExtension(fileName, fileSize) {

				if (fileSize >= maxSize) {
					alert("파일 크기 초과");
					return false;
				}

				if (regex.test(fileName)) {
					alert("해당 종류의 파일은 업로드할 수 없습니다.");
					return false;
				}

				return true;
			}

			// 파일 목록 출력 함수
			var uploadResult = $(".uploadResult ul");

			function showUploadedFile(uploadResultArr) {

				var str = "";

				$(uploadResultArr).each(function(i, obj) {
					
					// 이미지 파일이 아닐 경우
					if(!obj.image){
						
						// 파일 다운로드를 위한 경로 구하기
						var fileCallPath = encodeURIComponent(obj.uploadPath+"/"+obj.uuid+"_"+obj.fileName);
						
						var fileLink = fileCallPath.replace(new RegExp(/\\/g), "/");
						
						// href로 파일 이미지 클릭 시 파일 다운로드가 되도록 설정
					str += "<li><a href='/download?fileName="+fileCallPath+"'>"+"<img src='/resources/img/attach.png'>"
					+ obj.fileName + "</a>"+"<span data-file=\'"+fileCallPath+"\' data-type='file'> X </span>"+"<div></li>";
					} else{
					
					// str += "<li>" + obj.fileName + "</li>";
					// URI 호출에 적합한 문자열로 인코딩(파일 이름에 포함된 공백이나 한글 처리)
					var fileCallPath = encodeURIComponent(obj.uploadPath+"/s_"+obj.uuid+"_"+obj.fileName);
					
					var originPath = obj.uploadPath+"\\"+obj.uuid+"_"+obj.fileName;
					
					originPath = originPath.replace(new RegExp(/\\/g), "/");
					
					str += "<li><a href=\"javascript:showImage(\'"+originPath+"\')\">"+"<img src='/display?fileName="+fileCallPath+"'></a>"
					+"<span data-file=\'"+fileCallPath+"\' data-type='image'> X </span>"+"</li>";
					
					}
				});

				uploadResult.append(str);
			}

			// 파일 업로드 input 클론
			var cloneObj = $(".uploadDiv").clone();

			$("#uploadBtn").on("click", function(e) {

				// jQuery를 이용하는 경우 파일 업로드는 FormData 객체를 이용
				var formData = new FormData();
				var inputFile = $("input[name='uploadFile']");
				var files = inputFile[0].files;

				console.log(files);

				for (var i = 0; i < files.length; i++) {

					if (!checkExtension(files[i].name, files[i].size)) {
						return false;
					}

					formData.append("uploadFile", files[i]);
				}

				// formData에 데이터를 추가해서 그 자체를 전송한다
				// contentType과 processData 모두 반드시 false로 지정해야만 전송되므로 주의
				$.ajax({
					url : '/uploadAjaxAction',
					contentType : false,
					processData : false,
					data : formData,
					type : 'POST',
					dataType : 'json', // Ajax 호출 결과 타입
					success : function(result) {

						console.log(result);

						// 업로드 파일 목록 출력
						showUploadedFile(result);

						// 업로드 초기화
						$(".uploadDiv").html(cloneObj.html());
					}
				});

			});
			
			// 원본 이미지 한 번 더 클릭하면 사라지도록 하는 이벤트
			$(".bigPictureWrapper").on("click", function(e){
				$(".bigPicture").animate({width:'0%', height:'0%'}, 1000);
				setTimeout(()=>{$(this).hide();}, 1000);
			});
			
			// 첨부파일 삭제
			$(".uploadResult").on("click", "span", function(e){
				
				var targetFile= $(this).data("file");
				var type=$(this).data("type");
				console.log(targetFile);
				
				$.ajax({
					
					url: '/deleteFile',
				data: {fileName: targetFile, type:type},
				dataType:'text',
				type:'POST',
				success: function(result){
					alert(result);
				}
				});
			});
			
		});
	</script>


</body>
</html>