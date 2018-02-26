$JQry( function() {
	var previousIndex = -1;
	
	$JQry( "ul.selectable" ).selectable({
		cancel: "a, li.list-group-item-warning",
		filter: "li",
		
		stop: function(event, ui) {
			var $target = $JQry(event.target),
			$selectable = $target.closest(".selectable");
			$selectable.removeClass("remove-hover");
			
			var $result = $JQry( "#selectResult" ).empty();
			var temp = "";
			$JQry( ".ui-selected", this ).each(function() {
				var index = $JQry( "ul.selectable li" ).index( this );
				if (index > -1){
					temp+= "#" + ( index ) ;
				}
			});
			$result.val(temp);
			if (temp.length>0)
			{
				showBin();
			} else
				{
				hideBin();
			}
		},
		selected: function(event, ui) {
			$JQry(ui.selected).addClass("bg-primary").removeClass("bg-info");
		},
		selecting: function(event, ui) {
			var $selecting = $JQry(ui.selecting),
			$selectable = $selecting.closest(".selectable"),
			$selectee = $selectable.find(".data"),
			currentIndex = $selectee.index(ui.selecting);
			
			$selectable.addClass("remove-hover");
			if (event.shiftKey && previousIndex > -1) {
				$selectee.slice(Math.min(previousIndex, currentIndex), Math.max(previousIndex, currentIndex) + 1).addClass("ui-selected bg-primary");
			} else {
				$selecting.addClass("bg-info");
				previousIndex = currentIndex;
			}
		},
		unselected: function(event, ui) {
			if (!event.shiftKey) {
				$JQry(ui.unselected).removeClass("bg-primary");
			}
		},
		
		unselecting: function(event, ui) {
			$JQry(ui.unselecting).removeClass("bg-primary bg-info");
		}
	});

	
} );


function showBin()
{
	$JQry("button#putInBin").show();
}

function hideBin()
{
	$JQry("button#putInBin").hide();
}