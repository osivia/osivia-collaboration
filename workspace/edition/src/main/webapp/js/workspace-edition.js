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
				$form = $target.closest("form"),
				$activeTasks = $form.find("ul.active-tasks > li"),
				$idleTasks = $form.find("ul.idle-tasks > li");
			
			$activeTasks.each(function(index, element) {
				var $element = $JQry(element),
					$activeInput = $element.find("input[type=hidden][name$=active]");
					$orderInput = $element.find("input[type=hidden][name$=order]");
				
				$activeInput.val(true);
				$orderInput.val(index + 1);
			});
			
			$idleTasks.find("input[type=hidden][name$=active]").val(false);
		}		
	});
	$JQry(".workspace-creation-sortable").disableSelection();
	
});
