<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="more" var="moreUrl" />


<div class="workspace-participants workspace-participants-compact">
    <div class="clearfix">
        <c:forEach var="group" items="${participants.groups}">
            <c:if test="${not empty group.members}">
                <c:set var="count" value="${fn:length(group.members) + group.more}" />
            
                <ul class="list-inline">
                    <li>
                        <c:choose>
                            <c:when test="${count eq 1}" >
                                <span><op:translate key="GROUP_${group.role.key}_UNIQUE" /></span>
                            </c:when>
                            
                            <c:otherwise>
                                <span><op:translate key="GROUP_${group.role.key}_MULTIPLE" args="${count}" /></span>
                            </c:otherwise>
                        </c:choose>
                    </li>
                    
                    <c:forEach var="member" items="${group.members}" varStatus="status">
                        <li>
                            <a href="${member.url}" class="thumbnail no-ajax-link" title="${member.displayName}" data-toggle="tooltip" data-placement="bottom">
                                <img src="${member.avatarUrl}" alt="" class="text-middle">
                            </a>
                        </li>
                    </c:forEach>
                    
                    <c:if test="${group.more gt 0}">
                        <li>
                            <a href="${moreUrl}">
                                <span><op:translate key="WORKSPACE_PARTICIPANTS_MORE" args="${group.more}" /></span>
                            </a>
                        </li>
                    </c:if>
                </ul>
            </c:if>
        </c:forEach>
    </div>
</div>
