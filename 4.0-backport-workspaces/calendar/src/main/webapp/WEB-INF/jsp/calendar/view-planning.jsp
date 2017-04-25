<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="calendar planning-calendar">
    <!-- Toolbar -->
    <jsp:include page="calendar-toolbar.jsp" />

    <!-- Planning -->
    <div class="row">
        <div class="col-xs-12">
            <div class="table-responsive">
                <table class="table table-hover">
                    <!-- Header -->
                    <thead>
                        <tr>
                            <th class="fixed-column"><op:translate key="CALENDAR_DATE" /></th>
                            <th class="fixed-column"><op:translate key="CALENDAR_TIME" /></th>
                            <th><op:translate key="CALENDAR_EVENT" /></th>
                        </tr>
                    </thead>

                    <!-- Body -->
                    <tbody>
                        <c:forEach var="event" items="${eventsData.events}">
                            <tr>
                                <!-- Date -->
                                <td>
                                    <div class="clearfix">
                                        <div class="day-of-month pull-left">${event.header.dayOfMonth}</div>
                                        <div>
                                            <div>${event.header.dayOfWeek}</div>
                                            <div class="small">${event.header.month}</div>
                                        </div>
                                    </div>
                                </td>
                                
                                <!-- Time -->
                                <td>
                                    <c:if test="${not event.begin}">
                                        <i class="glyphicons glyphicons-arrow-left"></i>
                                    </c:if>
                                    
                                    <span>${event.time}</span>
                                    
                                    <c:if test="${not event.end}">
                                        <i class="glyphicons glyphicons-arrow-right"></i>
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
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
