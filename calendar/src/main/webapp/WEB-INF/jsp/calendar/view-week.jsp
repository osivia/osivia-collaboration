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
<div id="scheduler_here" class="dhx_cal_container" data-url="${initData}" 
	data-selecteddate="${calendardata.selectedDate}"
	data-period="${calendarData.periodType.name}" 
    data-startdate="<%=formater.format(calendarData.getStartDate())%>">
    <div class="dhx_cal_navline">
        <button type="button" class="dhx_cal_prev_button" data-url="${previousUrl}" >&nbsp;</button>
        <button type="button" class="dhx_cal_next_button" data-url="${nextUrl}">&nbsp;</button>
        <button type="button" class="dhx_cal_today_button" data-url="${todayUrl}">
        	<op:translate key="CALENDAR_TODAY" />
        </button>
        <div class="dhx_cal_date"></div>
        <button type="button" class="dhx_cal_tab" id="day_tab" style="right:204px;" data-url="${loadData}"><op:translate key="CALENDAR_DAY" /></button>
        <button type="button" class="dhx_cal_tab" id="week_tab" style="right:140px;" ><op:translate key="CALENDAR_WEEK" /></button>
        <button type ="button" class="dhx_cal_tab" id="month_tab" style="right:76px;" ><op:translate key="CALENDAR_MONTH" /></button>
        <a href="${viewPlanning}" class="btn btn-default">
                    <span><op:translate key="CALENDAR_PLANNING" /></span>
        </a>
    </div>
    <div class="dhx_cal_header"></div>
    <div class="dhx_cal_data portlet-filler"></div><!-- Ajout de la classe portlet-filler pour que la hauteur soit définie automatiquement avec la place restante -->       
</div>