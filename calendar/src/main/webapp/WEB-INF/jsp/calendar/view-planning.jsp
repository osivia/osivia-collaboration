<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="calendar planning-calendar">
    <!-- Toolbar -->
    <jsp:include page="calendar-toolbar.jsp" />

    <!-- Planning -->
    <div class="table">
        <!-- Header -->
        <div class="table-row table-header">
            <div class="row">
                <!-- Date -->
                <div class="col-sm-4 col-md-3 col-lg-2">
                    <span><op:translate key="CALENDAR_DATE" /></span>
                </div>
                
                <div class="col-sm-8 col-md-9 col-lg-10">
                    <div class="row">
                        <!-- Time -->
                        <div class="col-sm-6 col-md-4 col-lg-2">
                            <span><op:translate key="CALENDAR_TIME" /></span>
                        </div>
                        
                        <!-- Event -->
                        <div class="col-sm-6 col-md-8 col-lg-10">
                            <span><op:translate key="CALENDAR_EVENT" /></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <!-- Body -->
        <c:forEach var="dailyEvents" items="${eventsData.mappedEvents}">
            <div class="table-row">
                <div class="row">
                    <!-- Date -->
                    <div class="col-sm-4 col-md-3 col-lg-2">
                        <div class="media">
                            <div class="media-left media-middle">
                                <div class="day-of-month">${dailyEvents.key.dayOfMonth}</div>
                            </div>
                            
                            <div class="media-body media-middle">
                                <span>${dailyEvents.key.dayOfWeek}</span>
                                <br>
                                <small>${dailyEvents.key.month}</small>
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-sm-8 col-md-9 col-lg-10">
                        <!-- Daily events -->
                        <c:forEach var="event" items="${dailyEvents.value}">
                            <div class="row">
                                <!-- Time -->
                                <div class="col-sm-6 col-md-4 col-lg-2">
                                    <span>${event.time}</span>
                                </div>
                                
                                <!-- Event -->
                                <div class="col-sm-6 col-md-8 col-lg-10">
                                    <p>
                                        <a href="${event.viewURL}" class="no-ajax-link">
                                            <span>${event.title}</span>
                                        </a>
                                    </p>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </c:forEach>
        
        <%-- <c:forEach var="event" items="${eventsData.events}">
            <div class="table-row">
                <div class="row">
                    <!-- Date -->
                    <div class="col-sm-4 col-md-3 col-lg-2">
                        <div class="media">
                            <div class="media-left media-middle">
                                <span class="day-of-month">${event.header.dayOfMonth}</span>
                            </div>
                            
                            <div class="media-body media-middle">
                                <span>${event.header.dayOfWeek}</span>
                                <br>
                                <small>${event.header.month}</small>
                            </div>
                        </div>
                    </div>
                    
                    <!-- Time -->
                    <div class="col-sm-4 col-md-3 col-lg-2">
                        <span>${event.time}</span>
                    </div>
                    
                    <!-- Event -->
                    <div class="col-sm-4 col-md-6 col-lg-8">
                        <a href="${event.viewURL}" class="no-ajax-link">
                            <span>${event.title}</span>
                        </a>
                    </div>
                </div>
            </div>
        </c:forEach> --%>
    </div>
</div>
