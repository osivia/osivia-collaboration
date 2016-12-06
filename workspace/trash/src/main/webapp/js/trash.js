$JQry(function() {
	
	$JQry(".trash ul.selectable").selectable({
		cancel: "a, button",
		filter: "> li",

		selecting: function(event, ui) {
			var $selecting = $JQry(ui.selecting);
			
			if (!$selecting.hasClass("bg-primary")) {
				$selecting.addClass("bg-info");
			}
		},
		
		selected: function(event, ui) {
			$JQry(ui.selected).addClass("bg-primary").removeClass("bg-info");
		},
		
		unselecting: function(event, ui) {
			$JQry(ui.unselecting).removeClass("bg-primary bg-info");
		},
		
		unselected: function(event, ui) {
			$JQry(ui.unselected).removeClass("bg-primary");
		},
		
		stop: function(event, ui) {
			var $target = $JQry(event.target),
				$selectable = $target.closest("ul.selectable");
			
			$selectable.children().each(function(index, element) {
				var $element = $JQry(element),
					$input = $element.find("input[type=hidden][name$=selected]");
				
				$input.val($element.hasClass("ui-selected"));
			});
			
			// Update toolbar
			updateTrashToolbar($selectable);
		}
	});
	
	
	$JQry(".trash .table-header .contextual-toolbar .unselect").click(function(event) {
		var $target = $JQry(event.target),
			$table = $target.closest(".table"),
			$selectable = $table.find("ul.selectable"),
			$selected = $selectable.children(".ui-selected");
		
		$selected.each(function(index, element) {
			var $element = $JQry(element);
			
			$element.removeClass("ui-selected bg-primary");
		});
		
		updateTrashToolbar($selectable);
	});

	
	$JQry(".trash .table button.location").popover({
		container: ".popover-container",
		content: function() {
			var $this = $JQry(this),
				$row = $this.closest(".table-row"),
				$table = $row.closest(".table"),
				result;
			
			jQuery.ajax({
				url: $table.data("location-url"),
				async: false,
				cache: true,
				headers: {
					"Cache-Control": "max-age=86400, public"
				},
				data: {
					path: $row.data("location-path")
				},
				dataType: "html",
				success : function(data, status, xhr) {
					result = data;
				}
			});
			
			return result;
		},
		html: true,
		placement: "bottom",
		trigger: "manual"
	});
	
	
	$JQry(".trash .table button.location").click(function(event) {
		var $target = $JQry(event.target),
			$button = $target.closest("button");
		
		// Hide other popovers
		$JQry(".trash .table button.location").not($button).popover("hide");
		
		$button.popover("toggle");
	});
	
});


/**
 * Update trash toolbar.
 * 
 * @param $selectable selectable UL jQuery object
 */
function updateTrashToolbar($selectable) {
	var $selected = $selectable.children(".ui-selected"),
		$table = $selectable.closest(".table"),
		$toolbar = $table.find(".table-header .contextual-toolbar"),
		$text = $toolbar.find(".navbar-text");
	
	if ($selected.length) {
		jQuery.ajax({
			url: $text.data("url"),
			async: false,
			cache: true,
			headers: {
				"Cache-Control": "max-age=86400, public"
			},
			data: {
				count: $selected.length
			},
			dataType: "json",
			success : function(data, status, xhr) {
				var message;
				
				if ("success" == status) {
					message = data["message"];
				} else {
					message = "";
				}
				
				$text.text(message);
			}
		});
		
		$toolbar.addClass("in");
	} else {
		$toolbar.removeClass("in");
	}
}
