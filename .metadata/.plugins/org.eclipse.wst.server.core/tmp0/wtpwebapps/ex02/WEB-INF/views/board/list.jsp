<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@include file="../includes/header.jsp"%>

<div class="row">
	<div class="col-lg-12">
		<h1 class="page-header">Tables</h1>
	</div>
</div>

<div class="row">
	<div class="col-lg-12">
		<div class="panel panel-default">
			<div class="panel-heading">Board List Page<button id='regBtn' type="button" class="btn btn-xs pull-right">Register New Board</button></div>
			<div class="panel-body">
				<table class="table table-striped table-bordered table-hover">
					<thead>
						<tr>
							<th>#번호</th>
							<th>제목</th>
							<th>작성자</th>
							<th>작성일</th>
							<th>수정일</th>
						</tr>
					</thead>

					<c:forEach items="${list }" var="board">
						<tr>
							<td><c:out value="${board.bno }" /></td>
							<!-- 게시물 제목에 조회 페이지로 가는 링크 걸기, 현재창 내에서 이동 -->
							<!-- 새창을 열고 싶다면 a 태그 속성에 target='_blank' 추가 -->
							<td><a href='/board/get?bno=<c:out value="${board.bno }"/>'><c:out value="${board.title }" /></a></td>
							<td><c:out value="${board.writer }" /></td>
							<td><fmt:formatDate pattern="yyyy-MM-dd"
									value="${board.regdate }" /></td>
							<td><fmt:formatDate pattern="yyyy-MM-dd"
									value="${board.updateDate }" /></td>
						</tr>
					</c:forEach>
				</table>

				<!-- 게시글 등록 확인 모달 -->
				<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
					aria-labelledby="myModalLabel" aria-hidden="true">
					<div class="modal-dialog">
						<div class="modal-content">
							<div class="modal-header">
								<h4 class="modal-title" id="myModalLabel">Modal title</h4>
								<button type="button" class="close" data-dismiss="modal"
									aria-hidden="true">&times;</button>
							</div>
							<div class="modal-body">처리가 완료되었습니다.</div>
							<div class="modal-footer">
								<button type="button" class="btn btn-default"
									data-dismiss="modal">Close</button>
								<button type="button" class="btn btn-primary">Save
									changes</button>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		// 새로운 게시글 번호
		var result = '<c:out value="${result}"/>';
		
		checkModal(result);
		
		// 뒤로 가기 문제를 해결하기 위한 history 객체 조작
		// 모달창이 보이는 여부와 관계 없이, Javascript 처리 후 history 상태는 모달창이 필요 없는 상태가 된다
		history.replaceState({}, null, null);
		
		function checkModal(result){
			
			// 새로운 게시글이 등록되지 않았을 경우, history 상태가 
			if(result==='' || history.state){
				return;
			}
			
			// 새로운 게시글이 등록되었을 경우
			if(parseInt(result)>0){
				$(".modal-body").html("게시글 "+parseInt(result)+"번이 등록되었습니다.");
			}
			$("#myModal").modal("show");
		}
		
		$("#regBtn").on("click", function(){
			self.location="/board/register";
		});
	});
</script>

<%@include file="../includes/footer.jsp"%>