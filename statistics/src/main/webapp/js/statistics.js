// Statistics functions

$JQry(function() {
	
	var options = {
		// Global
		
		// Boolean - Whether to animate the chart
		animation: true,
		// Number - Number of animation steps
		animationSteps: 60,
		// String - Animation easing effect
		animationEasing: "easeOutQuart",
		// Boolean - If we should show the scale at all
		showScale: true,
		// String - Colour of the scale line
		scaleLineColor: "rgba(0, 0, 0, .1)",
		// Number - Pixel width of the scale line
		scaleLineWidth: 1,
		// Boolean - Whether to show labels on the scale
		scaleShowLabels: true,
		// Interpolated JS string - can access value
		scaleLabel: "<%=value %>",
		// Boolean - Whether the scale should stick to integers, not floats even if drawing space is there
		scaleIntegersOnly: true,
		// Boolean - Whether the scale should start at zero, or an order of magnitude down from the lowest value
		scaleBeginAtZero: true,
		// String - Scale label font declaration for the scale label
		scaleFontFamily: "'Helvetica', 'Arial', sans-serif",
		// Number - Scale label font size in pixels
		scaleFontSize: 14,
		// String - Scale label font weight style
		scaleFontStyle: "normal",
		// String - Scale label font colour
		scaleFontColor: "#666",
		// Boolean - whether or not the chart should be responsive and resize when the browser does
		responsive: true,
		// Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
		maintainAspectRatio: false,
		// Boolean - Determines whether to draw tooltips on the canvas or not
		showTooltips: true,
		// Array - Array of string names to attach tooltip events
		tooltipEvents: ["mousemove", "touchstart", "touchmove"],
		// String - Tooltip background colour
		tooltipFillColor: "#000",
		// String - Tooltip label font declaration for the scale label
		tooltipFontFamily: "'Helvetica', 'Arial', sans-serif",
		// Number - Tooltip label font size in pixels
		tooltipFontSize: 12,
		// String - Tooltip font weight style
		tooltipFontStyle: "normal",
		// String - Tooltip label font colour
		tooltipFontColor: "#fff",
		// String - Tooltip title font declaration for the scale label
		tooltipTitleFontFamily: "'Helvetica', 'Arial', sans-serif",
		// Number - Tooltip title font size in pixels
		tooltipTitleFontSize: 12,
		// String - Tooltip title font weight style
		tooltipTitleFontStyle: "normal",
		// String - Tooltip title font colour
		tooltipTitleFontColor: "#fff",
		// Number - pixel width of padding around tooltip text
		tooltipYPadding: 6,
		// Number - pixel width of padding around tooltip text
		tooltipXPadding: 12,
		// Number - Size of the caret on the tooltip
		tooltipCaretSize: 5,
		// Number - Pixel radius of the tooltip border
		tooltipCornerRadius: 4,
		// Number - Pixel offset from point x to tooltip edge
		tooltipXOffset: 10,
		// String - Template string for single tooltips
		tooltipTemplate: "<% if (label) { %><%=label %> : <% } %><%=value %>",
		// String - Template string for multiple tooltips
		multiTooltipTemplate: "<%=value %>",
		// Function - Will fire on animation progression
		onAnimationProgress: function(){},
		// Function - Will fire on animation completion
		onAnimationComplete: function(){},
		
		
		// Bar
		
		// Boolean - Whether grid lines are shown across the chart
		scaleShowGridLines: true,
		// String - Colour of the grid lines
		scaleGridLineColor : "rgba(0, 0, 0, .05)",
		// Number - Width of the grid lines
		scaleGridLineWidth : 1,
		// Boolean - Whether to show horizontal lines (except X axis)
		scaleShowHorizontalLines: true,
		// Boolean - Whether to show vertical lines (except Y axis)
		scaleShowVerticalLines: false,
		// Boolean - If there is a stroke on each bar
		barShowStroke: false,
		// Number - Pixel width of the bar stroke
		barStrokeWidth : 2,
		// Number - Spacing between each of the X value sets
		barValueSpacing : 12,
		// Number - Spacing between data sets within X values
		barDatasetSpacing : 5,
		// String - A legend template
	    legendTemplate : "<ul class=\"<%=name.toLowerCase() %>-legend\"><% for (var i=0; i<datasets.length; i++) { %><li><span style=\"background-color: <%=datasets[i].fillColor %>\"></span><% if (datasets[i].label) { %><%=datasets[i].label%><% } %></li><% } %></ul>"
	};
	
	
	$JQry("canvas.chart").each(function(index, element) {
		var chartContext = element.getContext("2d"),
			$chart = $JQry(element),
			url = $chart.data("url"),
			xhttp = new XMLHttpRequest();
		
		xhttp.onreadystatechange = function() {
			if (xhttp.readyState == 4 && xhttp.status == 200) {
				var data = JSON.parse(xhttp.responseText);
				
				if ($chart.hasClass("bar-chart")) {
					new Chart(chartContext).Bar(data, options);
				}
		    }
		};
		xhttp.open("GET", url, true);
		xhttp.send();
	});

});
