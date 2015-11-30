<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="calendar monthly-calendar">
    <!-- Toolbar -->
    <jsp:include page="calendar-toolbar.jsp" />
    
    <!-- Month -->
    <table class="table table-bordered">
        <!-- Header -->
        <thead>
            <tr>
                <c:forEach var="calendarHeader" items="${calendarData.headers}">
                    <th class="text-center">
                        <div class="text-overflow">
                            <span>${calendarHeader}</span>
                        </div>
                    </th>
                </c:forEach>
            </tr>
        </thead>
        
        <!-- Body -->
        <tbody>
            <c:forEach var="week" items="${calendarData.weeks}">
                <tr>
                    <c:forEach var="day" items="${week.days}" varStatus="dayStatus">
                        <portlet:renderURL var="viewDay">
                            <portlet:param name="period" value="day" />
                            <portlet:param name="date" value="${day.dateParameter}" />
                        </portlet:renderURL>
                        
                        <portlet:renderURL var="viewWeek">
                            <portlet:param name="period" value="week" />
                            <portlet:param name="date" value="${day.dateParameter}" />
                        </portlet:renderURL>
                        
                        <c:set var="key" value="${week.name}:${day.name}" />
                    

                        <td
                            <c:if test="${day.today}">class="today"</c:if>
                        >
                            <div
                                <c:if test="${not day.currentMonth}">class="text-muted"</c:if>
                            >
                                <div class="text-right clearfix">
                                    <a href="${viewDay}" class="btn btn-link btn-xs day">
                                        <span>${day.name}</span>
                                        
                                        <c:if test="${not empty day.monthDisplay}">
                                            <span class="hidden-xs">${day.monthDisplay}</span>
                                        </c:if>
                                    </a>
                                    
                                    <c:if test="${dayStatus.first}">
                                        <a href="${viewWeek}" class="label label-default pull-left hidden-xs">
                                            <span>${week.name}</span>
                                        </a>
                                    </c:if>
                                </div>
                                
                                <div class="clearfix">
                                    <c:forEach var="event" items="${eventsData.mappedEvents[key]}" varStatus="eventStatus">
                                        <c:remove var="eventClass" />
                                        <c:if test="${not event.begin}">
                                            <c:set var="eventClass" value="has-previous" />
                                        </c:if>
                                        <c:if test="${not event.end}">
                                            <c:set var="eventClass" value="has-next ${eventClass}" />
                                        </c:if>
                                    
                                        <div class="event bg-primary ${eventClass}">
                                            <a href="${event.viewURL}" class="no-ajax-link">
                                                <span>${event.time}</span>
                                                <span>${event.title}</span>
                                            </a>
                                        </div>
                                    </c:forEach>
                                    
                                    <c:if test="${fn:length(eventsData.mappedEvents[key]) > 2}">
                                        <div class="hidden-xs">
                                            <a href="${viewDay}" class="btn btn-default btn-xs">
                                                <i class="glyphicons more"></i>
                                            </a>
                                        </div>
                                    </c:if>
                                </div>
                            </div>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</div>
