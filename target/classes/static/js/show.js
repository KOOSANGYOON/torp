console.log("hihi");

$("#show-sideBar-button").click(showSideBar);

function showSideBar(e) {
	console.log("haha");
	e.preventDefault();
	
	var i = $(".container-sideBar").attr("display");
	
	if (i === "visible") {
		console.log(i);
		$(".container-sideBar").attr("display", "unvisible");
		$(".container-sideBar").animate({width: "0px"}, 300);
	}else {
		console.log(i);
		$(".container-sideBar").animate({width: "300px"}, 300);
		$(".container-sideBar").attr("display", "visible");
	}
}