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
				$item = ui.item,
				$activeInput = $item.find("input[type=hidden][name$=active]"),
				$updatedInput = $item.find("input[type=hidden][name$=updated]"),
				$sortedInput = $item.find("input[type=hidden][name$=sorted]"),
				$ul = $item.closest("ul"),
				active = $ul.hasClass("active-tasks");
			
			$activeInput.val(active);
			
			if (!$target.is($ul)) {
				$updatedInput.val(true);
			}
			
			$sortedInput.val(true);
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
	
	
	// Auto submit when checkbox is checked
	$JQry(".workspace-edition input[type=checkbox][name='editorial.displayed']").change(function(event) {
		var $target = $JQry(event.target),
			$formGroup = $target.closest(".form-group"),
			$submit = $formGroup.find("input[type=submit][name='create-editorial']");
		
		if (!$formGroup.data("created")) {
			$submit.click();
		}
	});
	
});
