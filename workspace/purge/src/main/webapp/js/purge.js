$JQry( function() {
	$JQry( "ul.selectable" ).selectable({
		cancel: "a",
		stop: function() {
			var $result = $JQry( "#selectResult" ).empty();
			var temp = "";
			$JQry( ".ui-selected", this ).each(function() {
				var index = $JQry( "ul.selectable li" ).index( this );
				temp+= "#" + ( index ) ;
			});
			$result.val(temp);
		}
	});
} );