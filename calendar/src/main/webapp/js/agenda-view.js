/**
 * Il y a 2 façons d'affecter la position de l'ascenseur en vue jour et semaine:
 * - soit via $JQry("div.dhx_cal_data")[0].scrollTop après l'appel de scheduler.setCurrentView()
 * - soit via scheduler.config.scroll_hour avant l'appel à scheduler.init(...), mais dans ce cas, 
 *   il faut lui affecter la valeur 24*$JQry("div.dhx_cal_data")[0].scrollTop/$JQry("div.dhx_cal_data")[0].scrollHeight
 *   
 * J'ai utilisé la 1ère façon, car elle peut être faite après scheduler.init
 * Un point important concerne l'affectation de la classe flexbox aux parents du div.dhx_cal_data
 * Que ce soit scheduler.init, scheduler.updateView ou scheduler.setCurrentView, ces 3 méthodes suppriment l'attribut class du div.dhx_cal_data
 * Pour que le scheduler prenne toute la place restante, on utilise la classe flexbox
 * On affecte cette classe au div.dhx_cal_data ainsi qu'à tous ses parents après chaque appel aux méthodes scheduler.init, scheduler.updateView et scheduler.setCurrentView
 */
function dataLoading()
{
	var divScheduler = $JQry("div#scheduler_here");
	var urLoad = divScheduler.data("url");
	//console.log("Scroll before dataLoading : "+scheduler.config.scroll_hour);
	//la méthode init supprime la classe portlet-filler du div dhx_cal_data. il faut donc le remettre pour que le composant s'affiche sur la hauteur disponible
	//on ajoute des class flexbox aux composants parents uniquement si div#maximized est présent
	var maximized = $JQry("div#maximized");
	if (maximized.length > 0 ) $JQry("div.dhx_cal_data").parentsUntil(".flexbox").addClass("flexbox");
	scheduler.updateView();
	setScrollPosition();
	//la méthode updateView supprime la classe portlet-filler du div dhx_cal_data. il faut donc le remettre pour que le composant s'affiche sur la hauteur disponible
	if (maximized.length > 0) $JQry("div.dhx_cal_data").parentsUntil(".flexbox").addClass("flexbox");
		
	jQuery.ajax({
		url: urLoad,
		data: {
			start: scheduler.getState().min_date,
			end: scheduler.getState().max_date
		},
		cache: false,
		dataType: "json",
		success : function(data, status, xhr) {
			scheduler.clearAll();
			scheduler.parse(data, "json");
			scheduler.updateView();
			setScrollPosition();
		},
		error : function(data, status, xhr) {
			console.log( "Erreur lors de l'appel Ajax, status: "+status+ ", data: "+data);
		}
	});
}

function setScrollPosition()
{
	var divScheduler = $JQry("div#scheduler_here");
	var scrollViewDayWeek = divScheduler.data("scrollviewdayweek");
	var scrollViewMonth = divScheduler.data("scrollviewmonth");
	if(divScheduler.data("period")=="week" || divScheduler.data("period")=="day")
	{
		if ("-1" != scrollViewDayWeek && scrollViewDayWeek != "null") 
		{
			//console.log("Before scrollTop:"+$JQry("div.dhx_cal_data")[0].scrollTop+" ,scrollHeight:"+$JQry("div.dhx_cal_data")[0].scrollHeight+" ,scrollViewDayWeek:"+scrollViewDayWeek);
			$JQry("div.dhx_cal_data")[0].scrollTop=scrollViewDayWeek;
			//console.log("After scrollTop:"+$JQry("div.dhx_cal_data")[0].scrollTop+" ,scrollHeight:"+$JQry("div.dhx_cal_data")[0].scrollHeight);
		}
	}
	else if (divScheduler.data("period")=="month")
	{
		if ("-1" != scrollViewMonth &&  scrollViewMonth != "null") 
		{
			$JQry("div.dhx_cal_data")[0].scrollTop=scrollViewMonth;
		}
	}
}

function removeEvent(ev)
{
	var divSched = $JQry("div#scheduler_here");
	var urlRemove = divSched.data("url-remove");
	// AJAX parameters
	var container = null;
	var options = {
		method : "post",
		postBody : addScrollParam("doc_id=" + ev.doc_id,divSched.data("period"))
	};
	var callerId = null;
	directAjaxCall(container, options, urlRemove, null, callerId);
}

function initScheduler(backFromPlanning)
{
	var divScheduler = $JQry("div#scheduler_here");
	var maximized = $JQry("div#maximized");
	if (maximized.length > 0){
		$JQry("div.dhx_cal_data").removeClass("portlet-filler");
		$JQry("div.dhx_cal_data").addClass("portlet-filler");
	}
	if (!backFromPlanning) 
	{
		//scheduler.xy.nav_height = 0;//Hauteur à zéro de la barre de navigation (dhx_cal_navline) du scheduler. Pour que la ligne ne soit pas affichée, il faut ajouter un display:none en css 
		scheduler.config.xml_date="%Y-%m-%d %H:%i";
		scheduler.config.mark_now = true;//affiche un liseret rouge à l'endroit de l'heure et du jour actuel
				
		scheduler.config.now_date = new Date();//J'ai remaqué qu'il faut mettre aussi cette ligne pour afficher le liseret rouge de l'heure actuelle	
		scheduler.config.day_date = "%D %j %F"; 
		
		scheduler.config.scroll_hour = new Date().getHours();//Pour positionner l'ascenseur à la position de l'heure actuelle
		scheduler.config.preserve_scroll = false;//Scroll position is set via Pour réafficher l'ascenseur à la bonne position après une action, il faut impérativement mettre preserve_scroll = false
		scheduler.config.wai_aria_attributes = true;//accessibilité
		//scheduler.config.server_utc = true;
		scheduler.config.resize_month_events = true;
		scheduler.config.resize_month_timed = true;
		scheduler.config.drag_create = true;
		scheduler.config.first_hour =0;
		scheduler.config.last_hour = 24;
		//icons_select is the action's bar on the left of each event when we simple clic on it
		//edit and delete are the two only options we want to have
		scheduler.config.icons_select = ['icon_edit', 'icon_delete'];
		//icons_edit is the action's bar on the left of each event when the event is in edit mode
		scheduler.config.icons_edit = ['icon_save', 'icon_cancel'];
		//Set the height of the dhx_cal_navline
		scheduler.xy.nav_height = 45;
	}
	scheduler.init('scheduler_here',new Date(divScheduler.data("startdate")),divScheduler.data("period"));
	//Chargement des données
	dataLoading();
	//console.log( "Init Scheduler terminé! Period: "+divScheduler.data("period") + " ,StartDate: " +divScheduler.data("startdate"));
	
}

/**
 * Chargement du calendrier au chargement de la portlet
 */
$JQry(window).load(function() {
	var divScheduler = $JQry("div#scheduler_here");
	var viewEventUrl = divScheduler.data("url-viewevent");
	
	// Variable ajoutée pour corriger un bug dans le composant dhtmlx scheduler
	// En cliquant rapidement (moins de 500ms entre chaque clic) pour créer plusieurs événements, ceux-ci étaient créées mais non enregistrés en base
	// L'objectif est de ne plus les créer, en les filtrant lors de l'appel à l'écouter onBeforeEventCreated
	var last_attached_event;
	
	if (divScheduler!= null && null != divScheduler.data("period"))
	{
		//printCell is in agenda-view-cell.js
		//Important: do printCell before initScheduler
		printCell();
		
		initScheduler(false);
		
		//To display or hide menu bar on the event's left when the user click on the event
		scheduler.attachEvent("onClick", function(id){
			if (isReadonly(id)) {
				return false;
			} else {
			    var divScheduler = $JQry("div#scheduler_here");
				var isEditableUrl = divScheduler.data("url-eventeditable");
				var boolReturn = false;
			    jQuery.ajax({
					url: isEditableUrl,
					data: {
						id: id
					},
					async: false,
					cache: false,
					dataType: "json",
					success : function(data, status, xhr) {
						if (data)
						{
							boolReturn = true;
						} else
						{
							boolReturn = false;
						}
					},
					error : function(data, status, xhr) {
						console.log( "Erreur lors de l'appel Ajax, status: "+status+ ", data: "+data);
					}
				});
			    return boolReturn;
			}
		});
		
		scheduler.attachEvent("onDblClick", function(id,ev){
			var divScheduler = $JQry("div#scheduler_here");
			var event = scheduler.getEvent(id);
			if (event.doc_id != undefined)
			{
				var viewEventUrl = divScheduler.data("url-viewevent");
				var options = {
						method : "post",
						postBody: addScrollParam("doc_id=" + event.doc_id,divScheduler.data("period"))
					};
				directAjaxCall(null, options, viewEventUrl, null, null);
				return false;
			} else
			{
				return true;
			}
		});
		
		scheduler.attachEvent("onBeforeDrag", function(id) {
			return !isReadonly(id);
		});
		
		scheduler.attachEvent("onDragEnd", function(){
			last_attached_event = "onDragEnd";
		});

		scheduler.attachEvent("onEventDeleted", function (id) {
			last_attached_event = "onEventDeleted";
		});

		scheduler.attachEvent("onBeforeEventCreated", function (e){
			return !(last_attached_event == "onDragEnd");
		});
		
		scheduler.attachEvent("onEventAdded", function (id, ev) {
			//console.log("onEventAdded, id="+id+" , ev="+ev.text);
			var divSched = $JQry("div#scheduler_here");
			if (divSched.data("period")!="month")
			{
				saveEvent(ev);
			}
			last_attached_event = "onEventAdded";
		});
		
		scheduler.attachEvent("onEventChanged", function (id, ev) {
			//console.log("onEventChanged, id="+id+" , ev="+ev.text);
			saveEvent(ev);
		});
		
		scheduler.attachEvent("onEventSave", function (id, ev) {
			last_attached_event = "onEventSave";
			//console.log("onEventSave, id="+id+" , ev="+ev.text);
			var divSched = $JQry("div#scheduler_here");
			if (divSched.data("period")!="month")
			{
				saveEvent(ev);
			}
			return true;
		});
		
		scheduler.attachEvent("onBeforeEventDelete", function (id,ev) {
			//console.log("onBeforeEventDelete, id="+id+" , ev="+ev.text);
			removeEvent(ev);
			return true;
		});
		
		/*scheduler.renderEvent = function(container, ev) {
			var containerWidth = container.style.width;
			var containerHeight = container.style.height;
			
			var cntWidth = parseInt(containerWidth.substring(0,containerWidth.length-2),10)-2;
			var cntHeight = Math.max(parseInt(containerHeight.substring(0,containerWidth.length-2),10)-29,11);
			console.log("height:"+cntHeight);
			
		    // move section
		    var html = "<div class='dhx_header' style='background:"+ev.color+"'></div>";
		    html+= "<div class='dhx_event_move dhx_title' style='background:"+ev.color+"'>"
		    	+scheduler.templates.event_header(ev.start_date, ev.end_date, ev)
		    	+"</div>";
		    // container for event's content
		    html+= "<div class='dhx_event_move dhx_body' style='background:"+ev.color+";height:"+cntHeight+"px;width:"+(cntWidth-8)+"px'>";
		    html += "<a class='event_title' href='"+ev.view_url+"'>";
		    html += scheduler.templates.event_text(ev.start_date,ev.end_date,ev)+
		    "</a>" + "</div>";
		 
		    // resize section
		    html += "<div class='dhx_event_resize  dhx_footer' style='width: " +
		    containerWidth + ";background-color:"+ev.color+";height:5px;width:"+cntWidth+"px'></div>";
		 
		    container.innerHTML = html;
		    return true; //required, true - display a custom form, false - the default form
		};*/
		
		var fix_date = function(date) {  // 17:48:56 -> 17:30:00  et  17:05:41 -> 17:00:00
			date = new Date(date);
			if (date.getMinutes() > 30)
			   date.setMinutes(30);
			else
			   date.setMinutes(0);
			date.setSeconds(0);
			return date;
		};

		var event_step = 60;
		//Evenement created by single click
		scheduler.attachEvent("onEmptyClick", function(date, native_event){
			if (last_attached_event != "onDragEnd")
			{
				var fixed_date = fix_date(date);
				scheduler.addEventNow(fixed_date, scheduler.date.add(fixed_date, event_step, "minute"));
			}
		});
	}
	
	
	function isReadonly(id){
		if (!id) {
			return false;
		} else {
			return scheduler.getEvent(id).readonly;
		}
	}
});

$JQry(function() {
	var divScheduler = $JQry("div#scheduler_here");
	if (divScheduler != null && null != divScheduler.data("period"))
	{
		if (document.readyState === "complete") {
			initScheduler(true);
		}
	}
});
