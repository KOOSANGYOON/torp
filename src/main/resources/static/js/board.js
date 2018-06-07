var BOARD = (function (window){

    'use strict';

    var deckTemplate = Handlebars.compile(Template.deck),
        cardTemplate = Handlebars.compile(Template.card),
        commentTemplate = Handlebars.compile(Template.comment);

    function init(){

        $("#modal").modal();
        $("#warning-modal").modal();
        $("#warnNotOwner").modal();
        $(".close-moadl").on("click", closeModal)
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

    }

    function closeModal(e){


        var modalName = $(e.target).closest(".modal").attr('id');

        if(modalName === "modal") {

            $("#modal").modal("close");

        }else if(modalName === "warning-modal") {

            $("#warning-modal").modal("close");
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
                var card = cardTemplate({"value":data.title});
                var $deckWrapper = $(eventTarget.closest(".deck-wrapper"));
                $deckWrapper.find(".deck-cards-exist").append(card);

                $(eventTarget).parents(".add-card-form").find(".card-title").val("");
                $(eventTarget).parents(".card-composer").find("a.add-card-btn").css('display', 'block');
        }).fail(function makeCardFail() {       //ajax fail
            console.log("make card fail.");
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

        var url = $(".add-deck-form").attr("action");
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
                $(".add-deck-area").before(deck);
                $("#add-deck").val("");
                $(".add-deck-btn").css('display','block');
                location.reload();      //이 부분은 무조건 수정해야 합니다.
        }).fail(function makeDeckFail() {
            console.log("make deck fail.");
        });
        return false;
    }

    function cancelDeck(){

        $(".add-deck-btn").css('display','block');
        $(".add-deck-form").css('display','none');

    }

    function openCardModal(e){

        $("#modal").modal('open');

        var boardId = $(".board-header-area").attr("value");
        var cardId = $(e.target).closest(".deck-card").attr("value");
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

                $(commentTemplate({"comment-contents":commentContent, "current-time":currentTime,
                    "writer-name":writerSection})).appendTo(".comments");

                $(".comment-contents").val("");
        }).fail(function addCommentFail() {
            console.log("add comment fail.");
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
            return;
        });



    }

    function setDueDate(){

        var dueDate = $(".datepicker").val();
        $(".current-due-date").text(dueDate);

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
