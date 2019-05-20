$JQry(function() {

	$JQry(".trash button.location").popover({
		container: ".popover-container",
		content: function() {
			var $this = $JQry(this);
			var $row = $this.closest(".portal-table-row");
			var $trash = $row.closest(".trash");
			var result;
			
			jQuery.ajax({
				url: $trash.data("location-url"),
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
	
	
	$JQry(".trash button.location").click(function(event) {
		var $target = $JQry(event.target);
		var $button = $target.closest("button");
		
		// Hide other popovers
		$JQry(".trash button.location").not($button).popover("hide");
		
		$button.popover("toggle");
	});

});
