<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<c:set var="namespace"><portlet:namespace /></c:set>


<div class="taskbar taskbar-normal no-ajax-link">
    <ul>
        <c:forEach items="${tasks}" var="task">
            <portlet:actionURL name="start" var="startURL">
                <portlet:param name="id" value="${task.id}" />
            </portlet:actionURL>

            <c:if test="${task.active}">
                <c:set var="activeTask" value="${task}" />
            </c:if>
            
        
            <li
                <c:if test="${task.active}">class="active"</c:if>
            >
                <a href="${startURL}">
                    <i class="${task.icon}"></i>
                    <span>${task.displayName}</span>
                </a>
            </li>
        </c:forEach>
    </ul>
</div>
