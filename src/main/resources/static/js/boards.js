var BOARDS = (function (window){

	 'use strict';

	function init(){

    	$("#modal").modal();
		$(".board-list").on("click", ".board", gotoBoard);
		$(".add-board-btn").on("click", showCreateBoardForm);
		$(".add-board-form .save").on("click", createNewBoard);
		$(".close-modal").on("click", closeModal);

	}

	function showCreateBoardForm(){

		$('#modal').modal('open');

	}

	function createNewBoard(){
		console.log("in to create board");
		var boardName = $(".board-name").val();

		if(boardName == ""){
			$(".warning").css("display","block");
			return;
		}

        var url = $(".add-board-form").attr("action");
        console.log("url is : " + url);

        $.ajax({
            type: 'post',
            url: url,
            contentType: 'text/html; charset=utf-8',
            data: boardName,
            dataType: 'json',
        }).done(function makeBoardSuccess(data) {		//수정 필요 부분. -> board 생성 후에 다른 주소로 이동했다가 뒤로가기 눌렀을 시, 새로 생성된 board는 안뜸.
            	console.log("data is : ", data);
            	console.log("data : ", data.id);

            	$(".warning").css("display","none");
				var str = (Template.board.replace(/\{\{input-value\}\}/gi, boardName)).replace(/\{\{id\}\}/gi, data.id);

				$(".board-name").val("");
				$("#modal").modal("close");
				$(".board-list").append(str);
		}).fail(function makeBoardFail(data) {
            console.log("data is : ", data);
            console.log("fail");
		});
    }

    function makeBoardFail(data) {
		console.log("data is : " + data);
		console.log("fail");
	}

	function makeBoardSuccess(data) {
        console.log("data is : " + data);
		console.log("success");
	}

	function gotoBoard(){
        // var url = $(".board").attr("title");
        var url = $(".board").attr("value");
        // var index = $("#boardId").index(this);
        console.log("title " + url);
		// window.location.href = ("/boards/" + url);
	}

	function closeModal(){

        $("#modal").modal("close");

	}


	return {
		"init" : init
	}
})(window);

$(function(){
    BOARDS.init();
});
