<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<c:set var="currentDate"><fmt:formatDate value="${dayForViewDay}" pattern="yyyy,MM,dd" /></c:set>

<portlet:renderURL var="viewDay">
    <portlet:param name="period" value="day" />
    <portlet:param name="date" value="${currentDate}" />
</portlet:renderURL>
<portlet:renderURL var="viewWeek">
    <portlet:param name="period" value="week" />
    <portlet:param name="date" value="${currentDate}" />
</portlet:renderURL>
<portlet:renderURL var="viewMonth">
    <portlet:param name="period" value="month" />
    <portlet:param name="date" value="${currentDate}" />
</portlet:renderURL>


<c:set var="startDate"><fmt:formatDate value="${calendarData.startDate}" pattern="yyyy,MM,dd" /></c:set>


<!-- Toolbar -->
<div class="btn-toolbar" role="toolbar">

    <div id="btn-period" class="btn-group dropdown pull-right" data-period="${calendarData.periodType.name}" data-startdate="${startDate}">
    	<button type="button" class="btn btn-secondary btn-sm dropdown-toggle" data-toggle="dropdown">
	        <span><op:translate key="CALENDAR_PLANNING" /></span>
	        <span class="caret"></span>
	    </button>
    	<ul class="dropdown-menu dropdown-menu-right">
			<li>  
		        <!-- Day -->
		        <a href="${viewDay}" class="dropdown-item">
		            <span><op:translate key="CALENDAR_DAY" /></span>
		        </a>
			</li>
			<li>
		        <!-- Week -->
		        <a href="${viewWeek}" class="dropdown-item">
		            <span><op:translate key="CALENDAR_WEEK" /></span>
		        </a>
			</li>
			<li>
		        <!-- Month -->
		        <a href="${viewMonth}" class="dropdown-item">
		            <span><op:translate key="CALENDAR_MONTH" /></span>
		        </a>
			</li>
			<li>
		        <!-- Planning -->
		        <a href="#" class="dropdown-item">
		            <span><op:translate key="CALENDAR_PLANNING" /></span>
		        </a>
		    </li>
		</ul>
    </div>
</div>
