$JQry(function() {

	// Sortable
	$JQry(".workspace-edition-sortable").sortable({
		connectWith : ".workspace-edition-sortable",
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
	$JQry(".workspace-edition-sortable").disableSelection();
	
	
	// Auto upload vignette for preview generation
	$JQry(".workspace-edition input[type=file][name='vignette.upload']").change(function(event) {
		var $target = $JQry(event.target),
			$formGroup = $target.closest(".form-group"),
			$submit = $formGroup.find("input[type=submit][name='upload-vignette']");
		
		$submit.click();
	});
	
	
	// Auto hide modal when task creation is successfully completed
	$JQry("#osivia-modal .workspace-task-creation").each(function(index, element) {
		var $element = $JQry(element),
			$modal = $JQry("#osivia-modal");
		
		if ($element.data("close-modal")) {
			$modal.modal("hide");
		}
	});
	
});


/**
 * Create workspace task.
 * 
 * @param args arguments: target.id|submit.id
 */
function createWorkspaceTask(args) {
	var array = args.split("|"),
		$modal = $JQry("#osivia-modal"),
		$source = $modal.find("form#taskCreationForm"),
		$target = $JQry("#" + array[0]),
		$submit = $JQry("#" + array[1]);
	
	if ($source.find("input[name=valid]").val()) {
		// Title
		$target.find("input[name='taskCreationForm.title']").val($source.find("input[name=title]").val());
		// Description
		$target.find("input[name='taskCreationForm.description']").val($source.find("textarea[name=description]").val());
		// Type
		$target.find("input[name='taskCreationForm.type']").val($source.find("input[name=type]:checked").val());
		// Valid indicator
		$target.find("input[name='taskCreationForm.valid']").val($source.find("input[name=valid]").val());
		
		$submit.click();
	}
}
