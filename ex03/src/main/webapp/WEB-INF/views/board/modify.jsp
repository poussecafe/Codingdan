<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@include file="../includes/header.jsp"%>

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

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Board Register</h1>
	</div>
</div>

<!-- input이나 textarea 태그의 name 속성은 BaordVO 클래스의 변수명과 일치시켜야 한다 -->

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board Modify Page</div>
			<div class="panel-body">

				<form role="form" action="/board/modify" method="post">

					<!-- 페이지 번호 유지를 위한 파라미터 추가 -->
					<input type='hidden' name='pageNum'
						value='<c:out value="${cri.pageNum }"/>'> <input
						type='hidden' name='amount'
						value='<c:out value="${cri.amount }"/>'>
					<!-- 검색 조건 유지를 위한 파라미터 추가 -->
					<input type='hidden' name='keyword'
						value='<c:out value="${cri.keyword }"/>'> <input
						type='hidden' name='type' value='<c:out value="${cri.type }"/>'>

					<div class="form-group">
						<label>Bno</label><input class="form-control" name='bno'
							value='<c:out value="${board.bno }"/>' readonly="readonly">
					</div>

					<div class="form-group">
						<label>Title</label><input class="form-control" name='title'
							value='<c:out value="${board.title }"/>'>
					</div>

					<div class="form-group">
						<label>Text area</label>
						<textarea class="form-control" rows="3" name='content'><c:out
								value="${board.content }" /></textarea>
					</div>

					<div class="form-group">
						<label>Writer</label><input class="form-control" name='writer'
							value='<c:out value="${board.writer }"/>' readonly="readonly">
					</div>

					<div class="form-group">
						<label>RegDate</label> <input class="form-control" name='regDate'
							value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.regdate }"/>'
							readonly="readonly">
					</div>

					<div class="form-group">
						<label>Update Date</label> <input class="form-control"
							name='updateDate'
							value='<fmt:formatDate pattern="yyyy/MM/dd" value="${board.updateDate }"/>'
							readonly="readonly">
					</div>

					<button type="submit" data-oper='modify' class="btn btn-default">Modify</button>
					<button type="submit" data-oper='remove' class="btn btn-danger">Remove</button>
					<button type="submit" data-oper='list' class="btn btn-info">List</button>

				</form>

			</div>
		</div>
	</div>
</div>

<!-- 첨부 파일이 보여질 영역 -->
<!-- 첨부 파일 목록 -->
<div class="row">
	<div class="col-lg-12">
		<div class="panel-heading">Files</div>
		<div class="panel-body">

			<!-- 첨부 파일 변경을 위한 input 태그 추가 -->
			<div class="form-group"uploadDiv">
				<input type="file" name='uploadFile' multiple="multiple">
			</div>

			<div class='uploadResult'>
				<ul></ul>
			</div>
		</div>
	</div>
</div>

<!-- 원본 이미지 -->
<div class='bigPictureWrapper'>
	<div class='bigPicture'></div>
</div>

<script type="text/javascript">
	$(document)
			.ready(
					function() {

						var formObj = $("form");

						$('button')
								.on(
										"click",
										function(e) {

											// 모든 버튼은 기본적으로 submit 처리가 되므로, 이 동작을 막고 마지막에 직접 submit 처리를 해준다
											e.preventDefault();

											var operation = $(this)
													.data("oper");

											console.log(operation);

											if (operation === 'remove') {
												formObj.attr("action",
														"/board/remove");

											} else if (operation === 'list') {
												// 목록으로 이동
												formObj.attr("action",
														"/board/list").attr(
														"method", "get");

												// 필요한 파라미터만 복사해서 보관
												// 페이징
												var pageNumTag = $(
														"input[name='pageNum']")
														.clone();
												var amountTag = $(
														"input[name='amount']")
														.clone();
												// 검색
												var keywordTag = $(
														"input[name='keyword']")
														.clone();
												var typeTag = $(
														"input[name='type']")
														.clone();

												// list로 이동할 때는 form 태그의 모든 내용은 삭제한 상태에서,
												formObj.empty();
												// 필요한 태그만 추가한 뒤 submit
												formObj.append(pageNumTag);
												formObj.append(amountTag);
												formObj.append(keywordTag);
												formObj.append(typeTag);

											} else if (operation === 'modify') { // 게시글 수정 시 삭제하는 파일 정보 보관 필요

												console.log("submit clicked");

												var str = "";

												$(".uploadResult ul li")
														.each(
																function(i, obj) {

																	var jobj = $(obj);

																	console
																			.dir(jobj);

																	str += "<input type='hidden' name='attachList["
																			+ i
																			+ "].fileName' value='"
																			+ jobj
																					.data("filename")
																			+ "'>";
																	str += "<input type='hidden' name='attachList["
																			+ i
																			+ "].uuid' value='"
																			+ jobj
																					.data("uuid")
																			+ "'>";
																	str += "<input type='hidden' name='attachList["
																			+ i
																			+ "].uploadPath' value='"
																			+ jobj
																					.data("path")
																			+ "'>";
																	str += "<input type='hidden' name='attachList["
																			+ i
																			+ "].fileType' value='"
																			+ jobj
																					.data("type")
																			+ "'>";

																});

												formObj.append(str).submit();
											}

											formObj.submit();
										});

						$(".uploadResult").on("click", "button", function(e) {
							console.log("delete file");

							if (confirm("첨부 파일을 삭제하시겠습니까?")) {
								var targetLi = $(this).closest("li");
								targetLi.remove();
							}
						});

						// RegExp 객체의 생성자 함수를 사용하면 정규식이 실행 시점에 컴파일 된다
						var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
						var maxSize = 5242880; //5MB

						function checkExtension(fileName, fileSize) {

							if (fileSize >= maxSize) {
								alert("파일 사이즈 초과");
								return false;
							}

							if (regex.test(fileName)) {
								alert("해당 종류의 파일은 업로드할 수 없습니다.");
								return false;
							}

							return true;
						}

						// 섬네일 처리 함수
						function showUploadResult(uploadResultArr) {

							if (!uploadResultArr || uploadResultArr.length == 0) {
								return;
							}

							var uploadUL = $(".uploadResult ul");
							var str = "";

							$(uploadResultArr)
									.each(
											function(i, obj) {

												// 이미지 파일일 경우
												if (obj.image) {

													var fileCallPath = encodeURIComponent(obj.uploadPath
															+ "/s_"
															+ obj.uuid
															+ "_"
															+ obj.fileName);

													str += "<li data-path='"+obj.uploadPath+"'";
str += " data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'";
str += "><div>";
													str += "<span>"
															+ obj.fileName
															+ "</span>";
													str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
													str += "<img src='/display?fileName="
															+ fileCallPath
															+ "'>";
													str += "</div>";
													str += "</li>";

												} else {

													var fileCallPath = encodeURIComponent(obj.uploadPath
															+ "/"
															+ obj.uuid
															+ "_"
															+ obj.fileName);
													var fileLink = fileCallPath
															.replace(
																	new RegExp(
																			/\\/g),
																	"/");

													str += "<li data-path='"+obj.uploadPath+"'";
str += " data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'";
str += "><div>";
													str += "<span>"
															+ obj.fileName
															+ "</span>";
													str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
													str += "<img src='/resources/img/attach.png'></a>";
													str += "</div";
													str += "</li>";

												}

											});

							uploadUL.append(str);
						}

						$("input[type='file']")
								.change(
										function(e) {

											var formData = new FormData();
											var inputFile = $("input[name='uploadFile']");
											var files = inputFile[0].files;

											for (var i = 0; i < files.length; i++) {

												if (!checkExtension(
														files[i].name,
														files[i].size)) {
													return false;
												}

												formData.append("uploadFile",
														files[i]);

											}

											$.ajax({
												url : '/uploadAjaxAction',
												processData : false,
												contentType : false,
												data : formData,
												type : 'POST',
												dataType : 'json',
												success : function(result) {
													console.log(result);
													showUploadResult(result); // 업로드 결과 처리 함수
												}
											});

										});

					});
</script>

<script>
	$(document)
			.ready(
					function() {

						// 첨부 파일 데이터 가져오기
						(function() {

							var bno = '<c:out value="${board.bno}"/>';

							$
									.getJSON(
											"/board/getAttachList",
											{
												bno : bno
											},
											function(arr) {
												console.log(arr);

												var str = "";

												$(arr)
														.each(
																function(i,
																		attach) {

																	if (attach.fileType) {
																		var fileCallPath = encodeURIComponent(attach.uploadPath
																				+ "/s_"
																				+ attach.uuid
																				+ "_"
																				+ attach.fileName);

																		str += "<li data-path='"+attach.uploadPath+"'";
str += " data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"'";
str += "><div>";
																		str += "<span> "
																				+ attach.fileName
																				+ "</span><br>";
																		str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='image' ";
str += "class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
																		str += "<img src='/display?fileName="
																				+ fileCallPath
																				+ "'>";
																		str += "</div>";
																		str += "</li>";

																	} else {
																		str += "<li data-path='"+attach.uploadPath+"'";
str += " data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-type='"+attach.fileType+"'";
str += "><div>";
																		str += "<span> "
																				+ attach.fileName
																				+ "</span><br/>";
																		str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='file' ";
																				str += "class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
																		str += "<img src='/resources/img/attach.png'></a>";
																		str += "</div";
																		str += "</li>";
																	}
																});

												$(".uploadResult ul").html(str);

											}); // end getjson
						})();
					});
</script>

<%@include file="../includes/footer.jsp"%>