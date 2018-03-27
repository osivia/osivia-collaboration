$JQry(function() {
	$JQry(".calendar.calendar-edition").each(function(index, element) {
		var $element = $JQry(element);
		
		if (!$element.data("loaded-edition")) {
			$element.find(".synchronization-source button[data-url]").click(function(event) {
				var $target = $JQry(event.target),
					$button = $target.closest("button");
				
				jQuery.ajax({
	                url: $button.data("url"),
	                async: true,
	                cache: true,
	                headers: {
	                    "Cache-Control": "max-age=86400, public"
	                },
	                dataType: "text",
	                success : function(data, status, xhr) {
	                    var $modal = $JQry("#osivia-modal"),
	                    	json = JSON.parse(data);

	                    $modal.data("load-url", json.url);
	                    $modal.data("load-callback-function", "synchronizationSourceEditionLoadCallback");
	                    $modal.data("callback-function", "synchronizationSourceEditionCallback");
	                    $modal.data("title", json.title);

	                    $modal.modal("show");
	                }
	            });
			});

			
			$element.data("loaded-edition", true);
		}
	});
});


/**
 * 
 * @returns
 */
function synchronizationSourceEditionLoadCallback() {
	$calendar = $JQry(".calendar.calendar-synchronization-edition");
	
	calendarColorPicker($calendar);
}


/**
 * Synchronization source edition modal callback.
 */
function synchronizationSourceEditionCallback() {
	var $modal = $JQry("#osivia-modal"),
		$form = $modal.find("form"),
		sourceId = $form.find("input[name=sourceId]").val(),
	    done = $form.find("input[name=done]").val(),
	    url = $form.find("input[name=url]").val(),
	    color = $form.find("input[name=color]").val(),
	    displayName = $form.find("input[name=displayName]").val(),
	    $synchronizationSource;
	
	if (done === "true") {
		if (sourceId) {
			$synchronizationSource = $JQry(".synchronization-source[data-source-id='" + sourceId + "']");
		} else {
			$synchronizationSource = $JQry(".synchronization-source:not([data-source-id])");
		}
		
		// Inject values
		$synchronizationSource.find("input[name$=url]").val(url);
		$synchronizationSource.find("input[name$=color]").val(color);
		$synchronizationSource.find("input[name$=displayName]").val(displayName);
		
		// Submit
		$synchronizationSource.find("input[type=submit]").click();
	}
}
