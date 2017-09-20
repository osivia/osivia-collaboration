<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ page import="org.osivia.services.calendar.portlet.model.calendar.CalendarData" %>
<%@ page import="java.text.SimpleDateFormat;" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:resourceURL id="initSchedulerData" var="initData" >
</portlet:resourceURL >
<portlet:resourceURL id="loadData" var="loadData" >
</portlet:resourceURL >
<portlet:resourceURL id="dragndrop" var="dragndrop" >
</portlet:resourceURL >
<portlet:renderURL var="viewPlanning">
    <portlet:param name="period" value="planning" />
    <portlet:param name="date" value="${date}" />
</portlet:renderURL>
 
 <c:set var="namespace"><portlet:namespace /></c:set>
 
 <% 
	CalendarData calendarData = (CalendarData)request.getAttribute("calendarData"); 
	SimpleDateFormat formater = new SimpleDateFormat("yyyy,MM,dd");
%>
 
<!-- Vue semaine -->
<div id="scheduler_here" class="dhx_cal_container" style="font-family:inherit;"data-url="${initData}" 
	data-selecteddate="${calendardata.selectedDate}"
	data-period="${calendarData.periodType.name}" 
    data-startdate="<%=formater.format(calendarData.getStartDate())%>"
    data-url="${loadData}"
    data-url-dragndrop="${dragndrop}">
    <div class="dhx_cal_navline btn-toolbar" role="toolbar"">
    	<!-- Previous period -->
    	<button type="button" class="dhx_cal_prev_button btn btn-default halflings halflings-chevron-left" style="position:initial;height:initial;text-align:center;"></button>
        <!-- Next period -->
        <button type="button" class="dhx_cal_next_button btn btn-default halflings halflings-chevron-right" style="position:initial;height:initial;text-align:center;"></button>
        <!-- Today -->
        <button type="button" class="dhx_cal_today_button btn btn-default" style="font-family:inherit;width:initial;height:initial;">
        	<op:translate key="CALENDAR_TODAY" />
        </button>
        
        <div class="dhx_cal_date" style="top:0px;font-family:inherit;"></div>
        <div id="btn-period" class="btn-group pull-right" style="top: 0px;position:relative;">
	        <button type="button" class="btn btn-default" id="day_tab"><op:translate key="CALENDAR_DAY" /></button>
	        <button type="button" class="btn btn-default" id="week_tab" ><op:translate key="CALENDAR_WEEK" /></button>
	        <button type ="button" class="btn btn-default" id="month_tab" ><op:translate key="CALENDAR_MONTH" /></button>
	        <a href="${viewPlanning}" class="btn btn-default">
	                    <span><op:translate key="CALENDAR_PLANNING" /></span>
	        </a>
    	</div>
    </div>
    <div class="dhx_cal_header"></div>
    <div class="dhx_cal_data portlet-filler" style="font-family:inherit;"></div><!-- Ajout de la classe portlet-filler pour que la hauteur soit définie automatiquement avec la place restante -->       
</div>