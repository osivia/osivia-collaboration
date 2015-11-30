<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<portlet:actionURL name="today" var="actionToday">
    <portlet:param name="period" value="${calendarData.periodType.name}" />
</portlet:actionURL>
<portlet:actionURL name="previous" var="actionPrevious">
    <portlet:param name="period" value="${calendarData.periodType.name}" />
    <portlet:param name="date" value="${date}" />
</portlet:actionURL>
<portlet:actionURL name="next" var="actionNext">
    <portlet:param name="period" value="${calendarData.periodType.name}" />
    <portlet:param name="date" value="${date}" />
</portlet:actionURL>

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
<portlet:renderURL var="viewPlanning">
    <portlet:param name="period" value="planning" />
    <portlet:param name="date" value="${date}" />
</portlet:renderURL>


<!-- Toolbar -->
<div class="btn-toolbar" role="toolbar">
    <div class="btn-group">
        <!-- Today -->
        <a href="${actionToday}" class="btn btn-default">
            <span><op:translate key="CALENDAR_TODAY" /></span>
        </a>
    </div>
    
    <div class="btn-group">
        <!-- Previous period -->
        <a href="${actionPrevious}" class="btn btn-default">
            <i class="halflings halflings-chevron-left"></i>
        </a>
    
        <!-- Selected date -->
        <a href="#" class="btn btn-default disabled">
            <span>${calendarData.displayDate}</span>
        </a>
        
        <!-- Next period -->
        <a href="${actionNext}" class="btn btn-default">
            <i class="halflings halflings-chevron-right"></i>
        </a>
    </div>

    <div class="btn-group pull-right">    
        <!-- Day -->
        <c:choose>
            <c:when test="${'day' eq calendarData.periodType.name}">
                <a href="#" class="btn btn-default active">
                    <span><op:translate key="CALENDAR_DAY" /></span>
                </a>
            </c:when>
            
            <c:otherwise>
                <a href="${viewDay}" class="btn btn-default">
                    <span><op:translate key="CALENDAR_DAY" /></span>
                </a>
            </c:otherwise>
        </c:choose>
        

        <!-- Week -->
        <c:choose>
            <c:when test="${'week' eq calendarData.periodType.name}">
                <a href="#" class="btn btn-default active">
                    <span><op:translate key="CALENDAR_WEEK" /></span>
                </a>
            </c:when>
            
            <c:otherwise>
                <a href="${viewWeek}" class="btn btn-default">
                    <span><op:translate key="CALENDAR_WEEK" /></span>
                </a>
            </c:otherwise>
        </c:choose>

        <!-- Month -->
        <c:choose>
            <c:when test="${'month' eq calendarData.periodType.name}">
                <a href="#" class="btn btn-default active">
                    <span><op:translate key="CALENDAR_MONTH" /></span>
                </a>
            </c:when>
            
            <c:otherwise>
                <a href="${viewMonth}" class="btn btn-default">
                    <span><op:translate key="CALENDAR_MONTH" /></span>
                </a>
            </c:otherwise>
        </c:choose>

        <!-- Planning -->
        <c:choose>
            <c:when test="${'planning' eq calendarData.periodType.name}">
                <a href="#" class="btn btn-default active">
                    <span><op:translate key="CALENDAR_PLANNING" /></span>
                </a>
            </c:when>
            
            <c:otherwise>
                <a href="${viewPlanning}" class="btn btn-default">
                    <span><op:translate key="CALENDAR_PLANNING" /></span>
                </a>
            </c:otherwise>
        </c:choose>
    </div>
</div>