// Statistics functions

$JQry(function() {

	$JQry(".statistics canvas.chart").each(function(index, element) {
		var $element = $JQry(element);
		var $table = $element.closest(".statistics").find("table");
		var context = element.getContext("2d");
		
		jQuery.ajax({
			url: $element.data("url"),
			async: true,
			dataType: "json",
			success : function(data, status, xhr) {
				new Chart(context, {
					type : data["type"],
					data : data["data"],
					options : data["options"]
				});
				
				$table.append(data["table"]);
			}
		});
	});

});
