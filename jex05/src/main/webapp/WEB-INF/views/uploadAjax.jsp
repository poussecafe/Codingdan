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
}

.uploadResult ul li img {
	width: 100px;
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

	<script src="http://code.jquery.com/jquery-latest.min.js"></script>

	<script>
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
					str += "<li><img src='/resources/img/attach.png'>"
					+ obj.fileName + "</li>";
					} else{
					
					// str += "<li>" + obj.fileName + "</li>";
					// URI 호출에 적합한 문자열로 인코딩(파일 이름에 포함된 공백이나 한글 처리)
					var fileCallPath = encodeURIComponent(obj.uploadPath+"/s_"+obj.uuid+"_"+obj.fileName);
					str += "<li><img src='/display?fileName="+fileCallPath+"'></li>";
					
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
		});
	</script>


</body>
</html>