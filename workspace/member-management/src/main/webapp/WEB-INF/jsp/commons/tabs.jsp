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


<c:set var="namespace"><portlet:namespace /></c:set>


<div class="clearfix margin-paragraph">
    <div class="pull-left">
        <ul class="nav nav-pills" role="tablist">
            <li role="presentation" class="${tab eq 'members' ? 'active' : ''}">
                <a href="${membersUrl}">
                    <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_TAB_MEMBERS" /></span>
                </a>
            </li>
            
            <li role="presentation" class="${tab eq 'invitations' ? 'active' : ''}">
                <a href="${invitationsUrl}">
                    <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_TAB_INVITATIONS" /></span>
                    
                    <!-- Badge -->
                    <c:if test="${options.invitationsCount gt 0}">
                        <span class="badge">${options.invitationsCount}</span>
                    </c:if>
                </a>
            </li>
            
            <c:if test="${options.workspaceType ne 'INVITATION'}">
                <li role="presentation" class="${tab eq 'requests' ? 'active' : ''}">
                    <a href="${requestsUrl}">
                        <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_TAB_REQUESTS" /></span>
                        
                        <!-- Badge -->
                        <c:if test="${options.requestsCount gt 0}">
                            <span class="badge">${options.requestsCount}</span>
                        </c:if>
                    </a>
                </li>
            </c:if>
        </ul>
    </div>
    
    <c:if test="${not empty help}">
        <div class="text-right">
            <button type="button" class="btn btn-default" data-toggle="collapse" data-target="#${namespace}-help">
                <i class="glyphicons glyphicons-question-sign"></i>
                <span><op:translate key="WORKSPACE_MEMBER_MANAGEMENT_DISPLAY_HELP" /></span>
            </button>
        </div>
    </c:if>
</div>

<!-- Help -->
<c:if test="${not empty help}">
    <div id="${namespace}-help" class="collapse">
        <div class="panel panel-info">
            <div class="panel-body">${help}</div>
        </div>
    </div>
</c:if>
