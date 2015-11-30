// Taskbar functions

$JQry(function() {
	
	// Update layout
	$JQry(".taskbar-container").each(function(index, element) {
		var $container = $JQry(element),
			$row = $container.find(".portlet-container").closest(".row");

		
		// Update styles
		updateTaskbarStyles($container, $row);
		$container.addClass("taskbar-absolute");
		
		
		// Affix
		if ($container.hasClass("taskbar-affix")) {
			var $window = $JQry(window),
				$body = $JQry("body"),
				$pageContent = $JQry("#page-content"),
				$navbarAffix = $pageContent.find(".content-navbar-affix"),
				navbarHeight = Math.round(($navbarAffix.length > 0) ? $navbarAffix.outerHeight(true) : 0);
			
			$container.affix({
				offset: {
					top: function() {
						var top = null;
						
						if (document.body.clientWidth >= 768) {
							top = Math.round($container.parent().offset().top - navbarHeight);
						}
						
						return top;
					},
					bottom: function() {
						var bottom = null;
						
						if (document.body.clientWidth >= 768) {
							var breakpoint = Math.round($pageContent.offset().top + $pageContent.height() - $row.height() - navbarHeight);
							
							if ($window.scrollTop() > breakpoint) {
								bottom = Math.round($body.height() - $pageContent.offset().top - $pageContent.height());
							}
						}

						return bottom;
					}
				}
			});
			
			$container.on("affix.bs.affix", function(event) {
				$container.css({
					top: navbarHeight
				});
			});
			
			$container.on("affix-top.bs.affix", function(event) {
				$container.css({
					top: "auto"
				});
			});
		}
	});
	

	// Sortable
	$JQry(".taskbar-sortable").sortable({
		connectWith: ".taskbar-sortable",
		cursor: "move",
		forcePlaceholderSize: true,
		placeholder: "list-sortable-placeholder bg-info",
		tolerance: "pointer",
		
		update: function(event, ui) {
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
		var $container = $JQry(element),
			$row = $container.find(".portlet-container").closest(".row");
		
		// Update styles
		updateTaskbarStyles($container, $row);
	});
});


/**
 * Update taskbar CSS styles.
 * 
 * @param $container taskbar container
 * @param $row taskbar content row
 */
function updateTaskbarStyles($container, $row) {
	$container.parent().css({
		"min-height": ((document.body.clientWidth >= 768) ? $row.height() : "auto")
	});
	$container.next().css({
		"padding-left": ((document.body.clientWidth >= 768) ? $row.width() : 0)
	});
}
