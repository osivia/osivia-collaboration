
$JQry(document).ready(function() {
	
	$JQry(".forum > .collapse").on("shown.bs.collapse", function() {
		var $this = $JQry(this);
		var offset = $this.offset();
		
		$JQry("html, body").animate({
			scrollTop : Math.max((offset.top - 50), 0)
		}, "fast");
	});
	
	$JQry(".forum .collapse").on("show.bs.collapse", function() {
		$JQry(".forum .collapse.in").collapse("hide");
	});
	
});


function cancelReply(source) {
	$JQry(source).closest(".collapse").collapse("hide");
}


function selectDelete(source, id) {
	var $forum = $JQry(source).closest(".forum");
	var $input = $forum.find(".delete-fancybox").find("input[name=id]");
	$input.val(id);
}
