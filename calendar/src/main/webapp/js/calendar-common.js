$JQry(function() {
	$JQry(".calendar").each(function(index, element) {
		var $element = $JQry(element);
		
		calendarColorPicker($element);
	});
});


function calendarColorPicker($calendar) {
	if (!$calendar.data("loaded-common")) {
		$calendar.find("a.calendar-color-container[data-color]").click(function(event) {
			var $target = $JQry(event.target),
				$a = $target.closest("a"),
				$dropdownMenu = $a.closest(".dropdown-menu"),
				$dropdown = $dropdownMenu.closest(".dropdown"),
				$formGroup = $dropdown.closest(".form-group"),
				$input = $formGroup.find("input[type=hidden][name=color]");
			
			// Remove active class
			$dropdownMenu.find(".calendar-color-container").removeClass("active");
			
			// Update input value
			$input.val($a.data("color"));
			
			// Update dropdown button classes
			$dropdown.find(".dropdown-toggle .calendar-color-container").removeClass().addClass($a.attr("class"));
			
			// Add active class
			$a.addClass("active");
		});
		
		$calendar.find(".color-picker").on("hidden.bs.dropdown", function(event) {
			// Destroy tooltip
			$JQry("body > .tooltip").remove();
		});
		
		
		$calendar.data("loaded-common", true);
	}
}
