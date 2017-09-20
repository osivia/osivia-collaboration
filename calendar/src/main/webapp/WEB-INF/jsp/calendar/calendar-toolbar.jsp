<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ page import="org.osivia.services.calendar.portlet.model.calendar.CalendarData" %>
<%@ page import="java.text.SimpleDateFormat;" %>


<portlet:renderURL var="viewDay">
    <portlet:param name="period" value="day" />
    <portlet:param name="date" value="${date}" />
</portlet:renderURL>
<portlet:renderURL var="viewWeek">
    <portlet:param name="period" value="week" />
    <portlet:param name="date" value="${date}" />
</portlet:renderURL>
<portlet:renderURL var="viewMonth">
    <portlet:param name="period" value="month" />
    <portlet:param name="date" value="${date}" />
</portlet:renderURL>

<!-- Toolbar -->
<div class="btn-toolbar" role="toolbar">

<% 
	CalendarData calendarData = (CalendarData)request.getAttribute("calendarData"); 
	SimpleDateFormat formater = new SimpleDateFormat("yyyy,MM,dd");
%>
    <div id="btn-period" class="btn-group pull-right" data-period="${calendarData.periodType.name}" 
    data-startdate="<%=formater.format(calendarData.getStartDate())%>">    
        <!-- Day -->
        <a href="${viewDay}" class="btn btn-default">
            <span><op:translate key="CALENDAR_DAY" /></span>
        </a>

        <!-- Week -->
        <a href="${viewWeek}" class="btn btn-default">
            <span><op:translate key="CALENDAR_WEEK" /></span>
        </a>

        <!-- Month -->
        <a href="${viewMonth}" class="btn btn-default">
            <span><op:translate key="CALENDAR_MONTH" /></span>
        </a>


        <!-- Planning -->
        <a href="#" class="btn btn-default active">
            <span><op:translate key="CALENDAR_PLANNING" /></span>
        </a>
    </div>
</div>