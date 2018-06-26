var ROOMS = (function (window){

    'use strict';

    function init(){

        $("#modal").modal();
        $(".room-list").on("click", ".room", gotoRoom);
        $(".add-room-btn").on("click", showCreateRoomForm);
        $(".add-room-form .save").on("click", createNewRoom);
        $(".close-modal").on("click", closeModal);

    }

    function showCreateRoomForm(){

        $('#modal').modal('open');

    }

    function createNewRoom(){
        console.log("in to create room");
        var roomName = $(".room-name").val();

        if(roomName == ""){
            $(".warning").css("display","block");
            return;
        }

        var url = $(".add-room-form").attr("action");
        console.log("url is : " + url);

        $.ajax({
            type: 'post',
            url: url,
            contentType: 'text/html; charset=utf-8',
            data: roomName,
            dataType: 'json'}).done(function makeRoomSuccess(data) {
            console.log("data is : ", data);
            console.log("data : ", data.id);

            $(".warning").css("display","none");
            var str = Template.board.replace(/\{\{input-value\}\}/gi, roomName);
            $(".room-name").val("");
            $("#modal").modal("close");
            $(".room-list").append(str);
            location.reload();		//무조건 이 부분은 수정해야합니다.
        }).fail(function makeRoomFail(data) {
            console.log("data is : ", data);
            console.log("fail");
        });
    }

    function gotoRoom(){
        // var url = $(".room").attr("title");
        var url = $(".room").attr("value");
        // var index = $("#roomId").index(this);
        console.log("title " + url);
        // window.location.href = ("/rooms/" + url);
    }

    function closeModal(){

        $("#modal").modal("close");

    }


    return {
        "init" : init
    }
})(window);

$(function(){
    ROOMS.init();
});
