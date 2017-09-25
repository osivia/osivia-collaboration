$JQry(function() {
	
	// Sortable
	$JQry(".taskbar-sortable").sortable({
		connectWith : ".taskbar-sortable",
		cursor : "move",
		forcePlaceholderSize : true,
		placeholder : "list-sortable-placeholder bg-info",
		tolerance : "pointer",
		
		update : function(event, ui) {
			var $target = $JQry(event.target),
				$form = $target.closest("form"),
				$input = $form.find("input[name=order]");
				$orderedTasks = $form.find(".taskbar-ordered-tasks"),
				order = "";
			
			$orderedTasks.find("li").each(function(index, element) {
				if (index > 0) {
					order += "|";
				}
				order += $JQry(element).data("id");
			});
			
			// Update input value
			$input.val(order);

			// Update fancybox
			parent.$JQry.fancybox.update();
		}
	});
	$JQry(".taskbar-sortable").disableSelection();
	
});
