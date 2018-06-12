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
            dataType: 'json'}).done(function makeBoardSuccess(data) {
            	console.log("data is : ", data);
            	console.log("data : ", data.id);

            	$(".warning").css("display","none");
				var str = Template.board.replace(/\{\{input-value\}\}/gi, boardName);
				$(".board-name").val("");
				$("#modal").modal("close");
				$(".board-list").append(str);
				location.reload();		//무조건 이 부분은 수정해야합니다.
		}).fail(function makeBoardFail(data) {
            console.log("data is : ", data);
            console.log("fail");
		});
            // error: makeBoardFail,
            // success: makeBoardSuccess});

        // $(".warning").css("display","none");
        // var str = Template.board.replace(/\{\{input-value\}\}/gi,boardName);
        // $(".board-name").val("");
        // $("#modal").modal("close");
        // $(".board-list").append(str);

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
