function printCell()
{
	var divScheduler = $JQry("div#scheduler_here");
	var viewEventUrl = divScheduler.data("url-viewevent");
	
	// Variable ajoutée pour corriger un bug dans le composant dhtmlx scheduler
	// En cliquant rapidement (moins de 500ms entre chaque clic) pour créer plusieurs événements, ceux-ci étaient créées mais non enregistrés en base
	// L'objectif est de ne plus les créer, en les filtrant lors de l'appel à l'écouter onBeforeEventCreated
	var last_attached_event;
	
	//Modifier onTemplatesReady doit se faire avant l'appel à scheduler.init
	scheduler.attachEvent("onTemplatesReady", function(){
	    scheduler.templates.event_text=function(start,end,event){
	        return "<a href='" + viewEventUrl+"&doc_id="+event.doc_id + "' class='event_title no-ajax-link' onclick='this.href=addScrollParam(this.href,null);'>" + event.text + "</a>";
	    };
	    scheduler.templates.event_bar_text=function(start,end,event){
	        return "<a href='" + viewEventUrl+"&doc_id="+event.doc_id + "' class='event_title no-ajax-link' onclick='this.href=addScrollParam(this.href,null);'>" + event.text + "</a>";
	    };
	    scheduler.templates.event_class = function(start,end,ev){
	    	var evClass;
	    	
	    	if (ev.extraClass == undefined) {
	    		evClass = divScheduler.data("color-main-agenda");
	    	} else {
	    		evClass = ev.extraClass;
	    	}
	    	
	    	if (ev.readonly) {
	    		evClass += " readonly";
	    	}
	    	
		    return evClass;
		}
	});
		
}

function saveEvent(ev)
{
	var divSched = $JQry("div#scheduler_here");
	var urlDragNDrop = divSched.data("url-dragndrop");
	var timezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
	// AJAX parameters
	var container = null;
	var options = {
		method : "post",
		postBody : addScrollParam("timezone="+timezone+"&start=" + ev.start_date.toISOString() + "&end=" + ev.end_date.toISOString() 
		+ "&doc_id=" + ev.doc_id+"&title="+ev.text,divSched.data("period"))
	};
	var callerId = null;
	directAjaxCall(container, options, urlDragNDrop, null, callerId);
}

function addScrollParam(url, targetPeriod) {
	var divScheduler = $JQry("div#scheduler_here");
	var urlReturn;

	if (!divScheduler.data("add-scroll-param")) {
		if (divScheduler.data('period') == "month") {
			urlReturn = url + "&scrollViewMonth="
					+ $JQry("div.dhx_cal_data")[0].scrollTop;
		} else if ((divScheduler.data('period') == "day" || divScheduler
				.data('period') == "week")) {
			urlReturn = url
					+ "&scrollViewDayWeek="
					+ $JQry("div.dhx_cal_data")[0].scrollTop
					+ "&hourPosition="
					+ (24 * $JQry("div.dhx_cal_data")[0].scrollTop / $JQry("div.dhx_cal_data")[0].scrollHeight)
							.toFixed(4);
		} else {
			urlReturn = url;
		}

		divScheduler.data("add-scroll-param", true);
	} else {
		urlReturn = url;
	}

	return urlReturn;
}
