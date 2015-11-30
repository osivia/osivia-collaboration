<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="calendar planning-calendar">
    <div class="row">
        <div class="col-xs-12">
            <table class="table table-hover">
                <!-- Header -->
                <thead>
                    <tr>
                        <th><op:translate key="CALENDAR_TIME" /></th>
                        <th><op:translate key="CALENDAR_EVENT" /></th>
                    </tr>
                </thead>
                
                <!-- Body -->
                <tbody>
                    <c:forEach var="eventsMap" items="${eventsData.mappedEvents}">
                        <c:set var="eventHeader" value="${eventsMap.key}" />
                    
                        <!-- Date -->
                        <tr>
                            <td colspan="2" class="date">
                                <div class="clearfix">
                                    <div class="day-of-month pull-left">${eventHeader.dayOfMonth}</div>
                                    <div>
                                        <div>${eventHeader.dayOfWeek}</div>
                                        <div class="small">${eventHeader.month}</div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        
                        <!-- Events -->
                        <c:forEach var="event" items="${eventsMap.value}">
                            <tr>
                                <!-- Time -->
                                <td>
                                    <c:if test="${not event.begin}">
                                        <i class="glyphicons left_arrow"></i>
                                    </c:if>
                                    
                                    <span>${event.time}</span>
                                    
                                    <c:if test="${not event.end}">
                                        <i class="glyphicons right_arrow"></i>
                                    </c:if>
                                </td>
                                
                                <!-- Title -->
                                <td>
                                    <a href="${event.viewURL}" class="no-ajax-link">
                                        <span>${event.title}</span>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
