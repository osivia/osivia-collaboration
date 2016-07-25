<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:renderURL var="membersUrl">
    <portlet:param name="tab" value="members" />
</portlet:renderURL>

<portlet:renderURL var="invitationsUrl">
    <portlet:param name="tab" value="invitations" />
</portlet:renderURL>

<portlet:renderURL var="requestsUrl">
    <portlet:param name="tab" value="requests" />
</portlet:renderURL>


<ul class="nav nav-pills">
    <li role="presentation" class="${tab eq 'members' ? 'active' : ''}">
        <a href="${membersUrl}">
            <span><op:translate key="TAB_MEMBERS" /></span>
        </a>
    </li>
    
    <li role="presentation" class="${tab eq 'invitations' ? 'active' : ''}">
        <a href="${invitationsUrl}">
            <span><op:translate key="TAB_INVITATIONS" /></span>
            
            <!-- Badge -->
            <c:if test="${options.invitationsCount gt 0}">
                <span class="badge">${options.invitationsCount}</span>
            </c:if>
        </a>
    </li>
    
    <%-- <li role="presentation" class="${tab eq 'requests' ? 'active' : ''}">
        <a href="${requestsUrl}">
            <span><op:translate key="TAB_REQUESTS" /></span>
            
            <!-- Badge -->
            <c:if test="${options.requestsCount gt 0}">
                <span class="badge">${options.requestsCount}</span>
            </c:if>
        </a>
    </li> --%>
</ul>
