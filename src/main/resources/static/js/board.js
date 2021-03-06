var BOARD = (function (window){

    'use strict';

    var deckTemplate = Handlebars.compile(Template.deck),
        cardTemplate = Handlebars.compile(Template.card),
        commentTemplate = Handlebars.compile(Template.comment);

    function init(){

        $("#modal").modal();
        $("#warning-modal").modal();
        $("#warnNotOwner").modal();
        $("#warn-delete-board").modal();
        $("#warn-delete-deck").modal();
        $("#warn-delete-card").modal();
        $(".close-moadl").on("click", closeModal);
        $(".members-btn").on("click", showMembers);
        $("#board-canvas").on("click",".add-card-btn", showCreateCardForm);
        $("#board-canvas").on("click",".save-card", saveCard);
        $("#board-canvas").on("click",".cancel-card", cancelCard);
        $("#board-canvas").on("click", ".deck-card-title", openCardModal);
        $(".add-deck-btn").on("click", showCreateDeckForm);
        $(".save-deck").on("click", saveDeck);
        $(".cancel-deck").on("click", cancelDeck);
        $(".attach-from-computer").on("click", fileUpload);
        $(".comment-send").on("click", addComment);
        $(".datepicker").pickadate({
            selectMonths: true,
            selectYears: 15
        });
        $(".due-date-btn").on("click", setDate);
        $(".file-attachment").on("click", setAttachment);
        $(".members-btn-in-card").on("click", searchMember);
        $(".card-description-edit-btn").on("click", showCardDescriptionEdit);
        $(".card-edit-close").on("click", closeCardEdit);
        $(".card-edit-save").on("click", saveCardEdit);
        $(".datepicker").on("change", setDueDate);
        $(".edit-board-title-btn").on("click", editBoardForm);
        $(".edit-board-title-btn-submit").on("click", editBoardTitle);
        $(".edit-board-title-btn-cancel").on("click", cancelEditBoardTitle);
        $(".delete-board-btn").on("click", deleteBoardForm);
        $(".deck-header-name").on("change", editDeckTitle);
        $(".card-title-in-modal").on("change", editCardTitle);
        $(".submit-delete-board").on("click", deleteBoard);
        $("#delete-card-btn").on("click", deleteCardForm);
        $(".submit-delete-card").on("click", deleteCard);
        $(".delete-deck-btn").on("click", deleteDeckForm);
        $(".submit-delete-deck").on("click", deleteDeck);

    }

    function closeModal(e){

        var modalName = $(e.target).closest(".modal").attr('id');

        if(modalName === "modal") {

            $("#modal").modal("close");

        }else if(modalName === "warning-modal") {

            $("#warning-modal").modal("close");
        }else if (modalName === "warnNotOwner") {

            $("#warnNotOwner").modal("close");
        }else if (modalName === "warn-delete-board") {

            $("#warn-delete-board").modal("close");
        }

    }

    function showMembers(){

        if($(".member-list").hasClass("clicked")){
            $(".member-list").removeClass("clicked").slideUp();
            return;
        }

        $(".member-list").addClass("clicked").slideDown();

    }

    function showCreateCardForm(e){
        $(e.target).closest(".card-composer").find(".add-card-form").css('display', 'block');
        $(e.target).closest("a.add-card-btn").css('display', 'none');

    }

    function saveCard(e){

        var cardTitle = $(e.target).parents(".add-card-form").find(".card-title").val();

        if(cardTitle == "") {
            $("#warning-modal").modal("open");
            return;
        }

        var boardId = $(".board-header-area").attr("value");
        var deckId = $(e.target).parents(".add-card-form").find(".save-card").attr("value");
        var url = "/api/boards/" + boardId + "/" + deckId + "/add";
        var eventTarget = e.target;

        console.log("board id is : " + boardId);
        console.log("deck id is : " + deckId);
        console.log("url is : " + url);

        $.ajax({
            type: 'post',
            url: url,
            contentType: 'text/html; charset=utf-8',
            data: cardTitle,
            dataType: 'json'}).done(function makeCardSuccess(data) {     //ajax success
                console.log("make card success.");

                $(".add-card-form").css('display', 'none');
                var card = cardTemplate({"value":data.title, "cardId":data.id});
                var $deckWrapper = $(eventTarget.closest(".deck-wrapper"));
                $deckWrapper.find(".deck-cards-exist").append(card);

                $(eventTarget).parents(".add-card-form").find(".card-title").val("");
                $(eventTarget).parents(".card-composer").find("a.add-card-btn").css('display', 'block');
        }).fail(function makeCardFail() {       //ajax fail
            console.log("make card fail.");
            // window.location.replace("/");       //재시작(도중에 로그인이 끊겼을 시)
        });
    }

    function cancelCard(e){

        $(e.target).closest(".card-composer .add-card-form").css('display', 'none');
        $(e.target).parent(".add-card-form").find(".card-title").val("");
        $(e.target).closest(".card-composer").find("a.add-card-btn").css('display', 'block');
    }

    function showCreateDeckForm(){
        $(".add-deck-btn").css('display','none');
        $(".add-deck-form").css('display','block');
    }

    function saveDeck(e){

        e.preventDefault();

        var deckTitle = $("#add-deck").val();
        console.log("title is " + deckTitle);

        if(deckTitle == ""){
            $("#warning-modal").modal('open');
            return;
        }

        // var url = $(".add-deck-form").attr("action");
        var url = $(".save-deck").attr("value");        //reload를 막기위해 수정.
        console.log("url is " + url);

        $.ajax({
            type: 'post',
            url: url,
            contentType: 'text/html; charset=utf-8',
            data: deckTitle,
            dataType: 'json'}).done(function makeDeckSuccess(data, e) {
                console.log("make deck success.");

                $(".add-deck-form").css('display','none');
                var deck = deckTemplate({"value":deckTitle, "deckId":data.id});

                $(deck).ready(function() {      //template.js 에서 만든 deck은 기능이 없기 때문에 ready 메서드를 이용해서 기능을 넣어준다.
                    $(".delete-deck-btn").on("click", deleteDeckForm);
                    $(".submit-delete-deck").on("click", deleteDeck);
                    $(".deck-header-name").on("change", editDeckTitle);
                });

                $(".add-deck-area").before(deck);
                $("#add-deck").val("");
                $(".add-deck-btn").css('display','block');
        }).fail(function makeDeckFail() {
            console.log("make deck fail.");
            // window.location.replace("/");       //재시작(도중에 로그인이 끊겼을 시)
        });
        return false;
    }

    function cancelDeck(e){
        e.preventDefault();
        $(".add-deck-form").css('display','none');
        $(".add-deck-btn").css('display','block');
    }

    function openCardModal(e){

        $("#modal").modal('open');

        var boardId = $(".board-header-area").attr("value");
        var cardId = $(e.target).attr("value");
        var deckId = $(e.target).closest(".deck-cards-exist").attr("value");
        console.log("target : ", e.target);
        console.log("card id : ", cardId);
        console.log("deck id : ", deckId);

        $(".hiddenCardTitle").text(cardId);
        $(".hiddenDeckTitle").text(deckId);

        var url = "/api/boards/" + boardId + "/" + deckId + "/" + cardId + "/cardInfo";
        console.log("url : ", url);

        $.ajax({
            type: 'post',
            url: url,
            contentType: 'text/html; charset=utf-8',
            data: cardId,
            dataType: 'json'}).done(function getCardSuccess(data) {
                console.log("data : ", data);

                $(".comments").empty();

                $(".card-description").text(data.description);
                $(".card-title-in-modal").text(data.title);
                $(".deck-name").text(data.toDoDeck.title);

                for (var i = 0; i < data.comments.length; i++) {
                    var writerSection = data.comments[i].writer.userId + "'s comment :";
                    var comment = data.comments[i].comment;
                    $(commentTemplate({"comment-contents":comment, "writer-name":writerSection})).appendTo(".comments");
                }
        }).fail(function getCardFail() {
            console.log("get card info fail.");
            // window.location.replace("/");       //재시작(도중에 로그인이 끊겼을 시)
        });

    }

    function setAttachment(){

        if($(".modal-for-attachment").hasClass("clicked")){
            $(".modal-for-attachment").removeClass("clicked").slideUp();
            return;
        }

        $(".modal-for-due-date").removeClass("clicked").slideUp();
        $(".modal-for-members").removeClass("clicked").slideUp();
        $(".modal-for-attachment").addClass("clicked").slideDown("slow", "easeInOutQuart");
    }

    function setDate(){

        if($(".modal-for-due-date").hasClass("clicked")){
            $(".modal-for-due-date").removeClass("clicked").slideUp();
            return;
        }

        $(".modal-for-attachment").removeClass("clicked").slideUp();
        $(".modal-for-members").removeClass("clicked").slideUp();
        $(".modal-for-due-date").addClass("clicked").slideDown("slow", "easeInOutQuart");

    }

    function searchMember(){

        if($(".modal-for-members").hasClass("clicked")){
            $(".modal-for-members").removeClass("clicked").slideUp();
            return;
        }

        $(".modal-for-attachment").removeClass("clicked").slideUp();
        $(".modal-for-due-date").removeClass("clicked").slideUp();
        $(".modal-for-members").addClass("clicked").slideDown("slow", "easeInOutQuart");
    }

    function addComment(e){

        var commentContent = $(".comment-contents").val();

        if(commentContent == ""){

            $("#warning-modal").modal('open');
            return;

        }

        var boardId = $(".board-header-area").attr("value");
        var deckId = $(".hiddenDeckTitle").text();
        var cardId = $(".hiddenCardTitle").text();
        console.log("card id : ", cardId + " , board id : ", boardId + " , deck id : ", deckId);

        var url = "/api/boards/" + boardId + "/" + deckId + "/" + cardId + "/addComment";

        $.ajax({
            type: 'post',
            url: url,
            contentType: 'text/html; charset=utf-8',
            data: commentContent,
            dataType: 'json'}).done(function addCommentSuccess(data, e) {
                console.log("add comment success.");

                var writerSection = data.writer.userId + "'s comment :";
                console.log("data : ", data);

                var now = new Date();
                var currentTime = now.getDate() + " " +
                    monthToString(now.getMonth()+1) + " " +
                    now.getFullYear() + " at " +
                    now.getHours() + ":" +
                    now.getMinutes();

                console.log('time type : ', jQuery.type(currentTime));

                $(commentTemplate({"comment-contents":commentContent, "current-time":currentTime,
                    "writer-name":writerSection})).appendTo(".comments");

                $(".comment-contents").val("");
        }).fail(function addCommentFail() {
            console.log("add comment fail.");
            // window.location.replace("/");       //재시작(도중에 로그인이 끊겼을 시)
        });

    }

    function monthToString(month){

        if(month === 1){
            return "Jan";
        }else if(month === 2){
            return "Feb";
        }else if(month === 3){
            return "Mar";
        }else if(month === 4){
            return "Apr";
        }else if(month === 5){
            return "May";
        }else if(month === 6){
            return "Jun";
        }else if(month === 7){
            return "July";
        }else if(month === 8){
            return "Aug";
        }else if(month === 9){
            return "Sep";
        }else if(month === 10){
            return "Oct";
        }else if(month === 11){
            return "Nov";
        }else if(month === 12){
            return "Dec";
        }
    }

    function fileUpload(){

        $("#fileUpload").trigger("click");

    }

    function showCardDescriptionEdit(e){

        $(".card-description-edit").css("display","block");
        $(".card-description-edit-btn").css("display","none");

        var descriptionContent = $(".card-description").text();

        if($(".card-description").text() != null){

            $(".card-description-textarea").val(descriptionContent);
            $(".card-description").css("display", "none");
        }

    }

    function closeCardEdit(){

        $(".card-description-edit").css("display","none");
        $(".card-description-edit-btn").css("display","block");
        $(".card-description").css("display","block");

    }

    function saveCardEdit(e){

        var description = $(".card-description-textarea").val();

        if(description == ""){

            $("#warning-modal").modal('open');
            return;

        }

        var boardId = $(".board-header-area").attr("value");
        var deckId = $(".hiddenDeckTitle").text();
        var cardId = $(".hiddenCardTitle").text();
        console.log("card id : ", cardId + " , board id : ", boardId + " , deck id : ", deckId);

        var url = "/api/boards/" + boardId + "/" + deckId + "/" + cardId + "/editDescription";

        $.ajax({
            type: 'post',
            url: url,
            contentType: 'text/html; charset=utf-8',
            data: description,
            dataType: 'json'}).done(function editDescriptionSuccess() {
                $(".card-description-textarea").val("");
                $(".card-description").text(description);       //jquery 의 data() 를 이용해서 직접 값을 저장하는 기능을 넣어야함. 그래야 refresh하지 않아도 변경이력이 바로 저장됨.
                $(".card-description-edit").css("display","none");
                $(".card-description-edit-btn").css("display","block");
                $(".card-description").css("display","block");
        }).fail(function editDescriptionFail() {
            console.log("fail to edit card description.");
            $("#warnNotOwner").modal('open');
            // window.location.replace("/");       //재시작(도중에 로그인이 끊겼을 시)
        });
    }

    function editBoardForm(e) {
        $(".board-name-area").css("display", "none");
        $(".edit-board-title-btn").css("display", "none");
        $(".delete-board-btn").css("display", "none");
        $(".board-name-textarea").css("display", "block");
        $(".edit-board-title-btn-submit").css("display", "inline-block");
        $(".edit-board-title-btn-cancel").css("display", "inline-block");
    }

    function cancelEditBoardTitle() {
        $(".board-name-textarea").css("display", "none");
        $(".edit-board-title-btn-submit").css("display", "none");
        $(".edit-board-title-btn-cancel").css("display", "none");
        $(".board-name-area").css("display", "block");
        $(".edit-board-title-btn").css("display", "inline-block");
        $(".delete-board-btn").css("display", "inline-block");
    }

    function editBoardTitle(e) {
        var boardId = $(e.target).attr("value");
        var url = "/api/boards/" + boardId;
        var newTitle = $(".board-name-textarea").val();

        $.ajax({
            type: 'put',
            url: url,
            contentType: 'text/html; charset=utf-8',
            data: newTitle,
            dataType: 'json'
        }).done(function editBoardNameSuccess(data) {
            console.log("edit board name success.");

            var newTitle = "to do List : " + data.title;
            $(".board-name-area").text(newTitle);

            $(".edit-board-title-btn").css("display", "block");
            $(".board-name-area").css("display", "block");
            $(".delete-board-btn").css("display", "block");
            $(".board-name-textarea").css("display", "none");
            $(".edit-board-title-btn-submit").css("display", "none");
            $(".edit-board-title-btn-cancel").css("display", "none");
        }).fail(function editBoardNameFail() {
            console.log("edit board name fail.");
            // window.location.replace("/");       //재시작(도중에 로그인이 끊겼을 시)
        });
    }

    function deleteBoardForm(e) {
        console.log("delete board");
        $("#warn-delete-board").modal('open');
    }

    function deleteBoard(e) {
        var boardId = $(".board-header-area").attr("value");
        var password = $(".password-to-delete").val();
        var url = "/api/boards/" + boardId;

        console.log(typeof password);
        console.log(boardId);
        console.log(url);

        $.ajax({
            type: 'delete',
            url: url,
            data: password,
            contentType: 'text/html; charset=utf-8',
            dataType: 'json'}).done(function deleteBoardSuccess() {
                console.log("success to delete.");
                $("#warn-delete-board").modal("close");
                window.location.replace("/boards");     //이 부분도 수정이 필요할 수 있다. replace가 아닌 다른방법이 있는지 확인 필요.
        }).fail(function deleteBoardFail() {
            console.log("fail to delete.");
            alert("비밀번호를 확인해주세요.");
            $(".password-to-delete").val('');
        });
    }

    function deleteDeckForm(e) {
        console.log("open delete deck form.");
        var deckId = $(e.target).parents(".deck-wrapper").attr("id");
        $(".hiddenDeckId").text(deckId);
        $("#warn-delete-deck").modal('open');
    }

    function deleteDeck(e) {
        console.log("delete deck.");

        var password = $("#deleteDeckPassword").val();
        var boardId = $(".board-header-area").attr("value");
        var deckId = $(".hiddenDeckId").text();
        var url = "/api/boards/" + boardId + "/" + deckId;

        $.ajax({
            type: 'delete',
            url: url,
            data: password,
            contentType: 'text/html; charset=utf-8',
            dataType: 'json'}).done(function deleteDeckSuccess() {
                console.log("success to delete.");
                $("#warn-delete-deck").modal("close");

                var deckTitles = $(".deck-sortable").find(".deck-header-name");

                for (var i = 0; i < deckTitles.length; i++) {
                    if ($(deckTitles.get(i)).attr('value') === deckId) {
                        console.log($(deckTitles.get(i)).attr('value'));
                        $(deckTitles.get(i)).parents(".deck-wrapper").remove();
                    }
                }
            $("#deleteDeckPassword").val('');
        }).fail(function deleteDeckFail() {
            $(".password-to-delete").val('');
            console.log("fail to delete.");
            alert("비밀번호를 확인해주세요.");
        });
    }

    function deleteCardForm(e) {
        console.log("open delete card form.");
        $("#warn-delete-card").modal('open');
    }

    function deleteCard(e) {
        console.log("delete card.");

        var password = $("#deleteCardPassword").val();
        var boardId = $(".board-header-area").attr("value");
        var deckId = $(".hiddenDeckTitle").text();
        var cardId = $(".hiddenCardTitle").text();

        var url = "/api/boards/" + boardId + "/" + deckId + "/" + cardId;

        $.ajax({
            type: 'delete',
            url: url,
            data: password,
            contentType: 'text/html; charset=utf-8',
            dataType: 'json'}).done(function deleteCardSuccess() {
                console.log("success to delete.");
                $("#warn-delete-card").modal("close");
                $("#modal").modal("close");

                var deckTitle = $(".deck-cards-exist").find(".deck-card-title");

                for (var i = 0; i < deckTitle.length; i++) {
                    if ($(deckTitle.get(i)).attr('value') === cardId) {
                        console.log($(deckTitle.get(i)).attr('value'));
                        $(deckTitle.get(i)).parents(".deck-card").remove();
                        $("#deleteCardPassword").val('');
                    }
                }
        }).fail(function deleteCardFail() {
            console.log("fail to delete.");
            alert("비밀번호를 확인해주세요.");
            $("#deleteCardPassword").val('');
        });
    }

    function editDeckTitle(e) {
        var newDeckTitle = $(e.target).closest(".deck-header-name").val();
        var boardId = $(".board-header-area").attr("value");
        var deckId = $(e.target).closest(".deck-id").attr("id");
        var url = "/api/boards/" + boardId + "/" + deckId;

        console.log("new title : ", newDeckTitle);
        console.log("board : ", boardId);
        console.log("deck : ", deckId);

        $.ajax({
            type: 'put',
            url: url,
            contentType: 'text/html; charset=utf-8',
            data: newDeckTitle,
            dataType: 'json'
        }).done(function editDeckTitleSuccess() {
            console.log("edit success.");
        }).fail(function editDeckTitleFail() {
            console.log("edit fail.");
        });
    }

    function editCardTitle(e) {
        var newCardTitle = $(e.target).val();
        var boardId = $(".board-header-area").attr("value");
        var deckId = $(".hiddenDeckTitle").text();
        var cardId = $(".hiddenCardTitle").text();
        var url = "/api/boards/" + boardId + "/" + deckId + "/" + cardId;

        console.log("new title : ", newCardTitle);
        console.log("board : ", boardId);
        console.log("deck : ", deckId);
        console.log("card : ", cardId);

        $.ajax({
            type: 'put',
            url: url,
            contentType: 'text/html; charset=utf-8',
            data: newCardTitle,
            dataType: 'json'
        }).done(function editCardTitleSuccess() {
            console.log("edit success.");
            var deckTitle = $(".deck-cards-exist").find(".deck-card-title");

            for (var i = 0; i < deckTitle.length; i++) {
                if ($(deckTitle.get(i)).attr('value') === cardId) {
                    console.log($(deckTitle.get(i)).attr('value'));
                    $(deckTitle.get(i)).text(newCardTitle);
                }
            }

            console.log("deckTitle : ", deckTitle);
        }).fail(function editCardTitleFail() {
            console.log("edit fail.");
        });
    }

    function setDueDate(){

        var dueDate = $(".datepicker").val();
        console.log("due date type : ", dueDate.types);
        $(".current-due-date").text("~ " + dueDate);

    }

    return {
        "init" : init
    }

})(window);

String.prototype.format = function() {
    var args = arguments;
    return this.replace(/{(\d+)}/g, function(match, number) {
        return typeof args[number] != 'undefined'
            ? args[number]
            : match
            ;
    });
};

$(function(){
    BOARD.init();
});
