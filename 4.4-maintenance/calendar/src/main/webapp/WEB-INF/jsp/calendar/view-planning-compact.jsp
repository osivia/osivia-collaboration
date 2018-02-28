<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="calendar planning-calendar">
    <div class="table">
        <!-- Header -->
        <div class="table-row table-header">
            <div class="row">
                <!-- Time -->
                <div class="col-sm-6 col-md-5 col-lg-4">
                    <span><op:translate key="CALENDAR_TIME" /></span>
                </div>
                
                <!-- Event -->
                <div class="col-sm-6 col-md-7 col-lg-8">
                    <span><op:translate key="CALENDAR_EVENT" /></span>
                </div>
            </div>
        </div>
        
        <!-- Body -->
        <c:forEach var="dailyEvents" items="${eventsData.mappedEvents}">
            <div class="table-row">
                <!-- Date -->
                <div class="media">
                    <div class="media-left media-middle">
                        <div class="day-of-month">
                            <p>${dailyEvents.key.dayOfMonth}</p>
                        </div>
                    </div>
                    
                    <div class="media-body media-middle">
                        <p>
                            <span>${dailyEvents.key.dayOfWeek}</span>
                            <br>
                            <small>${dailyEvents.key.month}</small>
                        </p>
                    </div>
                </div>
                
                <!-- Daily events -->
                <c:forEach var="event" items="${dailyEvents.value}">
                    <div class="row">
                        <!-- Time -->
                        <div class="col-sm-6 col-md-5 col-lg-4">
                            <span>${event.time}</span>
                        </div>
                        
                        <!-- Event -->
                        <div class="col-sm-6 col-md-7 col-lg-8">
                            <p>
                                <a href="${event.viewURL}" class="no-ajax-link">
                                    <span>${event.title}</span>
                                </a>
                            </p>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:forEach>
    </div>
    
    
    <c:if test="${not empty eventsData.lastDate}">
        <c:set var="lastDate"><fmt:formatDate value="${eventsData.lastDate}" type="date" dateStyle="full" /></c:set>
        
        <p class="text-muted">
            <span><op:translate key="CALENDAR_LAST_DATE_MESSAGE" args="${lastDate}" /></span>
        </p>
    </c:if>
</div>
