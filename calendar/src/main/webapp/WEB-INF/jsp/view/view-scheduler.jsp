<%@page import="org.codehaus.jackson.map.introspect.BasicClassIntrospector.GetterMethodFilter"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@page import="java.util.Date"%>
<%@ page import="org.osivia.services.calendar.view.portlet.model.calendar.CalendarData" %>
<%@ page import="org.apache.commons.lang.time.DateUtils" %>
<%@ page import="java.text.SimpleDateFormat" %>


<%@ page contentType="text/html" isELIgnored="false"%>

<%
    CalendarData calendarData = (CalendarData)request.getAttribute("calendarData");
    SimpleDateFormat formater = new SimpleDateFormat("yyyy,MM,dd");
%>
<portlet:defineObjects />
<portlet:resourceURL id="initSchedulerData" var="initData" >
</portlet:resourceURL >
<portlet:resourceURL id="loadData" var="loadData" >
</portlet:resourceURL >
<portlet:resourceURL id="isEventEditable" var="isEventEditable" >
</portlet:resourceURL >
<portlet:actionURL name="synchronize" var="synchronize" >
    <portlet:param name="period" value="${calendarData.periodType.name}" />
    <portlet:param name="date" value="${calendarData.startDateToString}" />
</portlet:actionURL >
<portlet:actionURL name="dragndrop" var="dragndrop" >
    <portlet:param name="period" value="${calendarData.periodType.name}" />
    <portlet:param name="date" value="${calendarData.startDateToString}" />
</portlet:actionURL >
<portlet:actionURL name="remove" var="remove" >
    <portlet:param name="period" value="${calendarData.periodType.name}" />
    <portlet:param name="date" value="${calendarData.startDateToString}" />
</portlet:actionURL >
<portlet:actionURL name="viewEvent"  var="viewEvent">
</portlet:actionURL>

<portlet:renderURL var="next">
    <portlet:param name="period" value="${calendarData.periodType.name}" />
    <c:choose>
        <c:when test="${'day' eq calendarData.periodType.name}">
            <portlet:param name="date" value="<%=formater.format(DateUtils.addDays(calendarData.getStartDate(),1))%>" />
        </c:when>
        <c:when test="${'week' eq calendarData.periodType.name}">
            <portlet:param name="date" value="<%=formater.format(DateUtils.addDays(calendarData.getStartDate(),7))%>" />
        </c:when>
        <c:when test="${'month' eq calendarData.periodType.name}">
            <portlet:param name="date" value="<%=formater.format(DateUtils.addMonths(calendarData.getStartDate(),1))%>" />
        </c:when>
    </c:choose>
</portlet:renderURL>
<portlet:renderURL var="previous">
    <portlet:param name="period" value="${calendarData.periodType.name}" />
    <c:choose>
        <c:when test="${'day' eq calendarData.periodType.name}">
            <portlet:param name="date" value="<%=formater.format(DateUtils.addDays(calendarData.getStartDate(),-1))%>" />
        </c:when>
        <c:when test="${'week' eq calendarData.periodType.name}">
            <portlet:param name="date" value="<%=formater.format(DateUtils.addDays(calendarData.getStartDate(),-7))%>" />
        </c:when>
        <c:when test="${'month' eq calendarData.periodType.name}">
            <portlet:param name="date" value="<%=formater.format(DateUtils.addMonths(calendarData.getStartDate(),-1))%>" />
        </c:when>
    </c:choose>
</portlet:renderURL>
<portlet:renderURL var="today">
    <portlet:param name="period" value="${calendarData.periodType.name}" />
</portlet:renderURL>
<portlet:renderURL var="viewDay">
    <portlet:param name="period" value="day" />
    <portlet:param name="date" value="<%=formater.format(request.getAttribute("dayForViewDay"))%>" />
</portlet:renderURL>
<portlet:renderURL var="viewWeek">
    <portlet:param name="period" value="week" />
    <portlet:param name="date" value="<%=formater.format(request.getAttribute("dayForViewWeek"))%>" />
</portlet:renderURL>
<portlet:renderURL var="viewMonth">
    <portlet:param name="period" value="month" />
    <portlet:param name="date" value="<%=formater.format(request.getAttribute("dayForViewMonth"))%>" />
</portlet:renderURL>
<portlet:renderURL var="viewPlanning">
    <portlet:param name="period" value="planning" />
    <portlet:param name="date" value="<%=formater.format(request.getAttribute("dayForViewDay"))%>" />
</portlet:renderURL>

<c:set var="namespace"><portlet:namespace /></c:set>


<!-- Vue semaine -->
<div id="scheduler_here" class="dhx_cal_container"
     data-period="${calendarData.periodType.name}"
     data-startdate="${calendarData.startDate.time}"
     data-url="${loadData}"
     data-url-dragndrop="${dragndrop}"
     data-url-remove="${remove}"
     data-url-viewevent="${viewEvent}"
     data-scrollviewdayweek="${calendarData.scrollViewDayWeek}"
     data-scrollviewmonth="${calendarData.scrollViewMonth}"
     data-url-eventeditable="${isEventEditable}"
     data-url-synchronize="${synchronize}"
     data-color-main-agenda="${calendarData.agendaBackgroundColor}"
     data-read-only="${calendarData.readOnly}"
     data-namespace="${namespace}">
    <div class="dhx_cal_navline btn-toolbar" role="toolbar">
        <div class="btn-group grp-btn-next-prev">
            <!-- Previous period -->
            <a href="${previous}" onclick="this.href=addScrollParam(this.href,null);" class="dhx_cal_prev_button btn btn-default btn-sm">
                <i class="halflings halflings-chevron-left"></i>
            </a>
            <!-- Next period -->
            <a href="${next}" onclick="this.href=addScrollParam(this.href,null);" class="dhx_cal_next_button btn btn-default btn-sm">
                <i class="halflings halflings-chevron-right"></i>
            </a>
        </div>
        <!-- Today -->
        <a href="${today}" onclick="this.href=addScrollParam(this.href,null);" class="dhx_cal_today_button btn btn-default btn-sm">
            <span><op:translate key="CALENDAR_TODAY" /></span>
        </a>



        <div id="btn-period" class="period-button dropdown pull-right">
            <button type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown">
                <span><op:translate key="${calendarData.periodType.internationalizationKey}" ></op:translate></span>
                <span class="caret"></span>
            </button>

            <ul class="dropdown-menu dropdown-menu-right">
                <li>
                    <a href="${viewDay}" class="dropdown-item" onclick="this.href=addScrollParam(this.href,'day');">
                        <op:translate key="CALENDAR_DAY" />
                    </a>
                </li>
                <li>
                    <a href="${viewWeek}" class="dropdown-item" onclick="this.href=addScrollParam(this.href,'week');">
                        <op:translate key="CALENDAR_WEEK" />
                    </a>
                </li>
                <li>
                    <a href="${viewMonth}" class="dropdown-item" onclick="this.href=addScrollParam(this.href,'month');">
                        <op:translate key="CALENDAR_MONTH" />
                    </a>
                </li>
                <li>
                    <a href="${viewPlanning}" class="dropdown-item">
                        <op:translate key="CALENDAR_PLANNING" />
                    </a>
                </li>
            </ul>
        </div>

        <div class="dhx_cal_date hidden-xs"></div>

    </div>
    <div class="dhx_cal_header small"></div>
    <div class="dhx_cal_data small"></div><!-- Ajout de la classe portlet-filler pour que la hauteur soit définie automatiquement avec la place restante -->
</div>