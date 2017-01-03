// Taskbar functions


// Taskbar affix top value
var taskbarAffixTop;


$JQry(function() {
	
	// Update layout
	$JQry(".taskbar-container").each(function(index, element) {
		var $element = $JQry(element),
			loaded = $element.data("loaded");

		
		// Update styles
		updateTaskbarStyles($element);
		$element.addClass("taskbar-absolute");
		
		
		// Affix
		if (!loaded && $element.hasClass("taskbar-affix")) {
			var $pageContent = $JQry("#page-content"),
				$navbarAffix = $pageContent.find(".content-navbar-affix-container"),
				navbarHeight = Math.round(($navbarAffix.length > 0) ? $navbarAffix.outerHeight(true) : 0);
			
			updateTaskbarAffixValues($element);
			
			$element.affix({
				offset : {
					top : function() {
						return taskbarAffixTop;
					}
				}
			});
			
			$element.on("affix.bs.affix", function(event) {
				$element.css({
					top : navbarHeight
				});
			});
			
			$element.on("affix-top.bs.affix", function(event) {
				$element.css({
					top : "auto"
				});
			});
			
			$element.data("loaded", true);
		}
	});
	

	// Sortable
	$JQry(".taskbar-sortable").sortable({
		connectWith : ".taskbar-sortable",
		cursor : "move",
		forcePlaceholderSize : true,
		placeholder : "list-sortable-placeholder bg-info",
		tolerance : "pointer",
		
		update : function(event, ui) {
			var $form = $JQry(this).closest("form"),
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


$JQry(window).resize(function() {
	$JQry(".taskbar-container").each(function(index, element) {
		var $element = $JQry(element);
		
		// Update affix
		updateTaskbarAffixValues($element);
		$element.affix("checkPosition");
		
		// Update styles
		updateTaskbarStyles($element);
	});
});


/**
 * Update taskbar CSS styles.
 * 
 * @param $container taskbar container
 */
function updateTaskbarStyles($container) {
	var $row = $container.find(".portlet-container").closest(".row"),
		windowWidth = $JQry(window).width();

	$container.parent().css({
		"min-height": ((windowWidth >= 768) ? $row.height() : "auto")
	});
	$container.next().css({
		"padding-left": ((windowWidth >= 768) ? $container.width() : 0)
	});
}


/**
 * Update taskbar affix values.
 * 
 * @param $container affix container
 */
function updateTaskbarAffixValues($container) {
	var $row = $container.find(".portlet-container").closest(".row"),
		$window = $JQry(window),
		$pageContent = $JQry("#page-content"),
		$navbarAffix = $pageContent.find(".content-navbar-affix-container"),
		navbarHeight = Math.round(($navbarAffix.length > 0) ? $navbarAffix.outerHeight(true) : 0);
	
	if ($JQry(window).width() >= 768) {
		taskbarAffixTop = Math.round($container.parent().offset().top - navbarHeight);
	} else {
		taskbarAffixTop = 1;
	}
}
