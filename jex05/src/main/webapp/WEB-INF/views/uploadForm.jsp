<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<form action="uploadFormAction" method="post"
		enctype="multipart/form-data">

		<!-- name 속성이 컨트롤러의 변수명으로 쓰인다 -->
		<input type="file" name='uploadFile' multiple>
		<button>Submit</button>

	</form>


</body>
</html>