$JQry(function() {
	
	$JQry("select[name=scope]").change(function(event) {
		var $target = $JQry(event.target);
		var $form = $target.closest("form");
		var $submit = $form.find("button[type=submit]");

		$submit.click();
	});
	
});
