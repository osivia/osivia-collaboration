

$JQry(function() {
	$JQry(".quota .asynchronous").each(function(index, element) {
		var $element = $JQry(element);

		setTimeout(refresh, 3000, $element);

	});
});


function refresh( element) {

	var $quota = element.parent().children(".inner-quota");

	jQuery.ajax({
		url: element.data("url"),
		async: true,
		dataType: "html",
		success : function(data, status, xhr) {
			$quota.html(data);
			
			// Avoid following calls
			element.removeClass("asynchronous");
		}
	});

}

