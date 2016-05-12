<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="calendar daily-calendar">
    <!-- Toolbar -->
    <jsp:include page="calendar-toolbar.jsp" />
    
    <!-- Day -->
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
                                            <th class="text-center">${calendarData.header}</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <c:forEach var="event" items="${eventsData.mappedEvents['']}">
                                                    <c:remove var="eventClass" />
                                                    <c:if test="${not event.begin}">
                                                        <c:set var="eventClass" value="has-previous" />
                                                    </c:if>
                                                    <c:if test="${not event.end}">
                                                        <c:set var="eventClass" value="has-next ${eventClass}" />
                                                    </c:if>
                                                
                                                    <div class="daily-event bg-primary ${eventClass}">
                                                        <c:if test="${not event.begin}">
                                                            <i class="glyphicons glyphicons-arrow-left"></i>
                                                        </c:if>
                                                    
                                                        <span>${event.time}</span>
                                                        
                                                        <c:if test="${not event.end}">
                                                            <i class="glyphicons glyphicons-arrow-right"></i>
                                                        </c:if>
                                                        
                                                        <span> - </span>
                                                        
                                                        <a href="${event.viewURL}" class="no-ajax-link">
                                                            <span>${event.title}</span>
                                                        </a>
                                                    </div>
                                                </c:forEach>
                                            </td>                                           
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
                                                <tr class="middle-row">
                                                    <th rowspan="2" class="fixed-column text-right">
                                                        <span>${hour}:00</span>
                                                    </th>
                                                    <td></td>
                                                </tr>
                                                <tr>
                                                    <td></td>
                                                </tr>
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
$JQry(".daily-calendar .wrapper").scrollTop("${calendarData.autoScroll}");
</script>
