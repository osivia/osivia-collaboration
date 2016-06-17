$JQry(function() {

	// Sortable
	$JQry(".workspace-creation-sortable").sortable({
		connectWith : ".workspace-creation-sortable",
		cursor : "move",
		forcePlaceholderSize : true,
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
	$JQry(".workspace-creation-sortable").disableSelection();
	
	
	// Open modal on load
	$JQry(".modal.opened").each(function(index, element) {
		var $element = $JQry(element);
		
		$element.modal("show");
		
		$element.removeClass("opened");
	});
	
});
