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
			<div class="panel-heading">
				Board List Page
				<button id='regBtn' type="button" class="btn btn-outline-primary">Register
					New Board</button>
			</div>
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
							<td><a class='move' href='<c:out value="${board.bno }"/>'><c:out
										value="${board.title }" /></a></td>
							<td><c:out value="${board.writer }" /></td>
							<td><fmt:formatDate pattern="yyyy-MM-dd"
									value="${board.regdate }" /></td>
							<td><fmt:formatDate pattern="yyyy-MM-dd"
									value="${board.updateDate }" /></td>
						</tr>
					</c:forEach>
				</table>
				
				<!-- 검색 -->
				<div class="row">
				<div class="col-lg-12">
				
				<form id='searchForm' action="/board/list" method='get'>
				
				<!-- select 태그 안에 삼항 연산자를 이용, 검색된 조건에 selected 속성을 추가해서 페이지 이동 후에도 선택 항목으로 표시 -->
				<select name='type'>
				<option value="" <c:out value="${pageMaker.cri.type==null?'selected':'' }"/>>--</option>
				<option value="T" <c:out value="${pageMaker.cri.type eq 'T'?'selected':'' }"/>>제목</option>
				<option value="C" <c:out value="${pageMaker.cri.type eq 'C'?'selected':'' }"/>>내용</option>
				<option value="W" <c:out value="${pageMaker.cri.type eq 'W'?'selected':'' }"/>>작성자</option>
				<option value="TC" <c:out value="${pageMaker.cri.type eq 'TC'?'selected':'' }"/>>제목 or 내용</option>
				<option value="TW" <c:out value="${pageMaker.cri.type eq 'TW'?'selected':'' }"/>>제목 or 작성자</option>
				<option value="TWC" <c:out value="${pageMaker.cri.type eq 'TWC'?'selected':'' }"/>>전체</option>
				</select>
				<input type='text' name='keyword' value='<c:out value="${pageMaker.cri.keyword }"/>' />
				<input type='hidden' name='pageNum' value='<c:out value="${pageMaker.cri.pageNum }"/>' />
				<input type='hidden' name='amount' value='<c:out value="${pageMaker.cri.amount }"/>' />
				<button class='btn btn-default'>Search</button>
				</form>
				
				</div>
				</div>

				<!-- 페이징 버튼 -->
				<div class='pull-right'>
					<ul class="pagination justify-content-end">

						<c:if test="${pageMaker.prev}">
							<li class="paginate_button page-item previous"><a class="page-link" href='<c:out value="${pageMaker.startPage-1 }"/>' aria-controls="dataTable" tabindex="0">Previous</a></li>
						</c:if>

						<c:forEach var="num" begin="${pageMaker.startPage }"
							end="${pageMaker.endPage }">
							<li class="paginate_button page-item ${pageMaker.cri.pageNum==num?"active":""}"><a class="page-link" aria-controls="dataTable" tabindex="0" href="${num }">${num }</a></li>
						</c:forEach>

						<c:if test="${pageMaker.next }">
							<li class="paginate_button page-item next"><a class="page-link" aria-controls="dataTable" tabindex="0" href="<c:out value="${pageMaker.endPage+1 }"/>">Next</a></li>
						</c:if>

					</ul>
				</div>
				
				<!-- 페이지 버튼을 누를 때 넘길 데이터를 input 태그에 hidden으로  숨겨둔다-->
				<form id='actionForm' action="/board/list" method='get'>
					<input type='hidden' name='pageNum' value='${pageMaker.cri.pageNum }'>
					<input type='hidden' name='amount' value='${pageMaker.cri.amount }'>
					<!-- 검색 조건 유지를 위한 파라미터 추가 -->
					<input type='hidden' name='type' value='<c:out value="${pageMaker.cri.type }"/>' />
					<input type='hidden' name='keyword' value='<c:out value="${pageMaker.cri.keyword }"/>' />
				</form>

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
	$(document).ready(
			function() {
				// 새로운 게시글 번호
				var result = '<c:out value="${result}"/>';

				checkModal(result);

				// 뒤로 가기 문제를 해결하기 위한 history 객체 조작
				// 모달창이 보이는 여부와 관계 없이, Javascript 처리 후 history 상태는 모달창이 필요 없는 상태가 된다
				history.replaceState({}, null, null);

				function checkModal(result) {

					// 새로운 게시글이 등록되지 않았을 경우, history 상태가 
					if (result === '' || history.state) {
						return;
					}

					// 새로운 게시글이 등록되었을 경우
					if (parseInt(result) > 0) {
						$(".modal-body").html(
								"게시글 " + parseInt(result) + "번이 등록되었습니다.");
					}
					$("#myModal").modal("show");
				}

				$("#regBtn").on("click", function() {
					self.location = "/board/register";
				});
				
				var actionForm = $("#actionForm");
				
				$(".paginate_button a").on("click", function(e){
					
					// 버튼의 기본 동작을 막는다
					e.preventDefault();
					
					console.log('click');
					
					// form 태그 내의 pageNum값을 href 속성값으로 변경
					actionForm.find("input[name='pageNum']").val($(this).attr("href"));
					actionForm.submit();
					
				});
				
				// 게시물 조회 시 게시글 번호, 페이지 번호, 한 페이지에 보여줄 게시글을 양에 대한 데이터를 다 같이 넘기기 위한 이벤트 처리
				$(".move").on("click", function(e){
					e.preventDefault();
					actionForm.append("<input type='hidden' name='bno' value='"+$(this).attr("href")+"'>");
					actionForm.attr("action", "/board/get");
					actionForm.submit();
				});
				
				var searchForm = $("#searchForm");
				
				$("#searchForm button").on("click", function(e){
					
					if(!searchForm.find("option:selected").val()){
						alert("검색 종류를 선택하세요.");
						return false;
					}
					
					if(!searchForm.find("input[name='keyword']").val()){
						alert("키워드를 입력하세요.");
						return false;
					}
					
					// 검색 후 페이지 번호를 1로 바꾼다
					searchForm.find("input[name='pageNum']").val("1");
					e.preventDefault();
					
					searchForm.submit();
				});
				
			});
</script>

<%@include file="../includes/footer.jsp"%>