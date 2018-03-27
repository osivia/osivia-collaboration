$JQry(function() {
	
    //Initialize input widgets
	$JQry("input.datepicker").datepicker({
			'dateFormat': 'dd/mm/yy',
			'autoclose': true
	});
    
    $JQry("input.timepicker").timepicker({ 
    	'timeFormat': 'H:i',
    	'showDuration': true
    });

    // initialize datepair
    $JQry("#datepairGroup").datepair({
    	parseDate: function(input){
            return $JQry(input).datepicker('getDate');
        },
        updateDate: function(input, dateObj){
          return $JQry(input).datepicker('setDate', dateObj);
        }
    });
});



function selectColor(element,colorid)
{
	var elts = document.getElementsByClassName("color-box glyphicons glyphicons-ok background-color-selected");
	for (i = 0; i < elts.length; i++) { 
		elts[i].className = "color-box";
	}
	//var children = element.children;
	element.className = "color-box glyphicons glyphicons-ok background-color-selected";
	document.getElementById("bckgColor").value = colorid;
}