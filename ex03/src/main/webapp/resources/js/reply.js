console.log("Reply Module.........")

var replyService = (function() {

	function add(reply, callback, error) {
		console.log("add reply...............");

		$.ajax({
			type : 'post',
			url : '/replies/new',
			data : JSON.stringify(reply),
			contentType : "application/json; charset=utf-8",
			success : function(result, status, xhr) {
				if (callback) {
					callback(result);
				}
			},
			error : function(xhr, status, er) {
				if (error) {
					error(er);
				}
			}
		})
	}

	// 댓글 목록 처리
	// param이라는 객체를 통해 필요한 파라미터를 전달받아 JSON 목록을 호출
	// JSON 형태가 필요하므로 URL 호출 시 확장자를 .json으로 요구
	function getList(param, callback, error) {

		var bno = param.bno;
		var page = param.page || 1;

		$.getJSON("/replies/pages/" + bno + "/" + page + ".json",
				function(data) {
					if (callback) {
						callback(data);
					}
				}).fail(function(xhr, status, err) {
			if (error) {
				error();
			}
		});
	}

	return {
		add : add,
		getList : getList
	};

})();