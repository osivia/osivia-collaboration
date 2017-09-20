function dataLoading()
{
	scheduler.clearAll();
	
	var buttonDay = $JQry("button#day_tab");
	var urlDay = buttonDay.data("url");
	scheduler.clearAll();
	jQuery.ajax({
		url: urlDay,
		data: {
			start: scheduler.getState().min_date,
			end: scheduler.getState().max_date
		},
		cache: false,
		dataType: "json",
		success : function(data, status, xhr) {
			console.log("Succes : "+data );
			scheduler.parse(data, "json");
		},
		error : function(data, status, xhr) {
			console.log( "Erreur lors de l'appel Ajax, status: "+status+ ", data: "+data);
		}
	});
}

function initScheduler(backFromPlanning)
{
	if (backFromPlanning) 
	{
		//Faire le scheduler.clearAll() permet d'avoir un calcul de la hauteur du div.dhx_cal_data correct, sinon, le composant définit la hauteur à zéro pour ce div
		scheduler.clearAll();
	} else
	{
	//scheduler.xy.nav_height = 0;//Hauteur à zéro de la barre de navigation (dhx_cal_navline) du scheduler. Pour que la ligne ne soit pas affichée, il faut ajouter un display:none en css 
	scheduler.config.xml_date="%Y-%m-%d %H:%i";
	scheduler.config.mark_now = true;//affiche un liseret rouge à l'endroit de l'heure et du jour actuel
			
	scheduler.config.now_date = new Date();//J'ai remaqué qu'il faut mettre aussi cette ligne pour afficher le liseret rouge de l'heure actuelle	
	scheduler.config.day_date = "%D %j %F"; 
	
	scheduler.config.scroll_hour = new Date().getHours();//Pour positionner l'ascenseur à la position de l'heure actuelle
	scheduler.config.preserve_scroll = false;//Pour conserver la position de l'ascenseur en navigant avec précédent ou suivant ou en passant de la vue Planning à une vue Jour/Semaine ou Mois
	scheduler.config.wai_aria_attributes = true;//pour pouvoir être reconnu par les lecteurs d'écran (accessibilité)
	}
	
	var divScheduler = $JQry("div#scheduler_here");
	
	scheduler.init('scheduler_here',new Date(divScheduler.data("startdate")),divScheduler.data("period"));
	if (backFromPlanning)
	{
		//la méthode init supprime la classe portlet-filler du div dhx_cal_data. il faut donc le remettre pour que le composant s'affiche sur la hauteur disponible
		$JQry("div.dhx_cal_data").addClass("portlet-filler");
	}
	
	//Chargement des données
	dataLoading();
	console.log( "Init Scheduler terminé! Period: "+divScheduler.data("period") + " ,StartDate: " +divScheduler.data("startdate"));
	
	$JQry("button.dhx_cal_prev_button").click(function(event) {
		var d = scheduler.getState().date;
		var period = scheduler.getState().mode;
		if (period == "day")
		{
			d.setDate(d.getDate()-1);
		}
		else if (period == "week")
		{
			d.setDate(d.getDate() -7);
		}
		else if (period == "month")
		{
			d.setMonth(d.getMonth() -1);
		}
		scheduler.setCurrentView(d,period);
		
		dataLoading(scheduler.getState().min_date, scheduler.getState().max_date);
	});
	
	
	$JQry("button.dhx_cal_next_button").click(function(event) {
		var d = scheduler.getState().date;
		var period = scheduler.getState().mode;
		if (period == "day")
		{
			d.setDate(d.getDate()+1);
		}
		else if (period == "week")
		{
			d.setDate(d.getDate() +7);
		}
		else if (period == "month")
		{
			d.setMonth(d.getMonth() +1);
		}
		scheduler.setCurrentView(d,period);
		dataLoading(scheduler.getState().min_date, scheduler.getState().max_date);
	});
	
	$JQry("button.dhx_cal_today_button").click(function(event) {
		var period = scheduler.getState().mode;
		scheduler.setCurrentView(new Date(),period);
		dataLoading();
	});
	
	$JQry("button#day_tab").click(function(event) {
		scheduler.setCurrentView(scheduler.getState().date,"day");
		dataLoading();
	});
	
	$JQry("button#week_tab").click(function(event) {
		scheduler.setCurrentView(scheduler.getState().date,"week");
		dataLoading();
	});
	
	$JQry("button#month_tab").click(function(event) {
		scheduler.setCurrentView(scheduler.getState().date,"month");
		dataLoading();
	});
}

/**
 * Chargement du calendrier au chargement de la portlet
 */
$JQry(window).load(function() {
	if (document.getElementById("scheduler_here") != null)
	{
		initScheduler(false);
	}
});

$JQry(function() {
	if (document.getElementById("scheduler_here") != null)
	{
		if (document.readyState === "complete") {
			initScheduler(true);
		}
	}
});
