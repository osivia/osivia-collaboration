<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="calendar weekly-calendar">
    <!-- Toolbar -->
    <jsp:include page="calendar-toolbar.jsp" />
    
    <!-- Week -->
    <div class="row">
        <div class="col-xs-12">
            <table class="table">
                <!-- Header -->
                <thead>
                    <tr>
                        <td class="fixed-column"></td>
                        <td>
                            <div class="scollbar-spacer">
                                <table class="table">
                                    <thead>
                                        <tr>
                                            <c:forEach var="calendarHeader" items="${calendarData.headers}">
                                                <portlet:renderURL var="viewDay">
                                                    <portlet:param name="period" value="day" />
                                                    <portlet:param name="date" value="${calendarHeader.dateParameter}" />
                                                </portlet:renderURL>
                                            
                                            
                                                <th
                                                    <c:if test="${calendarHeader.today}">class="today"</c:if>
                                                >
                                                    <div class="text-center text-overflow">
                                                        <a href="${viewDay}">${calendarHeader.name}</a>
                                                    </div>
                                                </th>
                                            </c:forEach>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <c:forEach var="weekDay" begin="1" end="7">
                                                <c:set var="key" value="week-day-${weekDay}" />
                                            
                                                <td>
                                                    <c:forEach var="event" items="${eventsData.mappedEvents[key]}">
                                                        <c:remove var="eventClass" />
                                                        <c:if test="${not event.begin}">
                                                            <c:set var="eventClass" value="has-previous" />
                                                        </c:if>
                                                        <c:if test="${not event.end}">
                                                            <c:set var="eventClass" value="has-next ${eventClass}" />
                                                        </c:if>
                                                    
                                                        <div class="daily-event bg-primary ${eventClass}">
                                                            <a href="${event.viewURL}" class="no-ajax-link">
                                                                <span>${event.title}</span>
                                                            </a>
                                                        </div>
                                                    </c:forEach>
                                                </td>
                                            </c:forEach>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </td>
                    </tr>
                </thead>
                
                <!-- Body -->
                <tbody>
                    <tr>
                        <td colspan="2">
                            <div class="wrapper">
                                <div>
                                    <!-- Grid -->
                                    <table class="table table-bordered">
                                        <tbody>
                                            <c:forEach var="hour" begin="0" end="23">
                                                <c:forEach begin="1" end="2" varStatus="status">
                                                    <tr
                                                        <c:if test="${status.first}">class="middle-row"</c:if>
                                                    >
                                                        <c:if test="${status.first}">
                                                            <th rowspan="2" class="fixed-column text-right">
                                                                <span>${hour}:00</span>
                                                            </th>
                                                        </c:if>
                                                        
                                                        <c:forEach var="day" begin="1" end="7">
                                                            <td
                                                                <c:if test="${day eq calendarData.today}">class="today"</c:if>
                                                            ></td>
                                                        </c:forEach>
                                                    </tr>
                                                </c:forEach>                                                
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                
                                    <!-- Events -->
                                    <div class="events-container">
                                        <c:forEach var="event" items="${eventsData.events}">
                                            <c:remove var="eventClass" />
                                            <c:if test="${not event.begin}">
                                                <c:set var="eventClass" value="has-previous" />
                                            </c:if>
                                            <c:if test="${not event.end}">
                                                <c:set var="eventClass" value="has-next ${eventClass}" />
                                            </c:if>
                                        
                                            <div class="event bg-primary ${eventClass}" style="top: ${event.top}; left: ${event.left}; height: ${event.height}; width: ${event.width};">
                                                <a href="${event.viewURL}" class="no-ajax-link">
                                                    <span class="sr-only">${event.time}</span>
                                                    <span>${event.title}</span>
                                                </a>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </div>
                        </td>
                    </tr>
                    
                    <tr>
                        <td colspan="2"></td>
                    </tr>
                </tbody>
            </table>  
        </div>
    </div>
</div>


<script type="text/javascript">
// Scroll calendar
$JQry(".weekly-calendar .wrapper").scrollTop("${calendarData.autoScroll}");
</script>
