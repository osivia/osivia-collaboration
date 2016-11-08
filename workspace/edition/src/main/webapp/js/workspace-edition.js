$JQry(function() {

	// Sortable
	$JQry("ul.workspace-edition-sortable").sortable({
		cancel: "a",
		connectWith : "ul.workspace-edition-sortable",
		cursor : "move",
		forcePlaceholderSize : true,
		items: "li",
		placeholder : "list-sortable-placeholder bg-info",
		tolerance : "pointer",
		
		update : function(event, ui) {
			var $target = $JQry(event.target),
				$formGroup = $target.closest(".form-group"),
				$activeTasks = $formGroup.find("ul.active-tasks > li"),
				$idleTasks = $formGroup.find("ul.idle-tasks > li");

			$activeTasks.find("input[type=hidden][name$=active]").val(true);
			$idleTasks.find("input[type=hidden][name$=active]").val(false);
		},
		
		stop : function(event, ui) {
			var $target = $JQry(event.target),
				$formGroup = $target.closest(".form-group"),
				$activeTasks = $formGroup.find("ul.active-tasks > li")
				$submit = $formGroup.find("input[type=submit][name=sort]");
				
			$activeTasks.each(function(index, element) {
				var $element = $JQry(element),
					$orderInput = $element.find("input[type=hidden][name$=order]");
				
				$orderInput.val(index + 1);
			});
			
			$submit.click();
		}
	});
	$JQry(".workspace-edition-sortable").disableSelection();
	
	
	// Auto upload vignette for preview generation
	$JQry(".workspace-edition input[type=file][name='vignette.upload']").change(function(event) {
		var $target = $JQry(event.target),
			$formGroup = $target.closest(".form-group"),
			$submit = $formGroup.find("input[type=submit][name='upload-vignette']");
		
		$submit.click();
	});
	
	
	// Auto upload banner for preview generation
	$JQry(".workspace-edition input[type=file][name='banner.upload']").change(function(event) {
		var $target = $JQry(event.target),
			$formGroup = $target.closest(".form-group"),
			$submit = $formGroup.find("input[type=submit][name='upload-banner']");
		
		$submit.click();
	});
	
});
