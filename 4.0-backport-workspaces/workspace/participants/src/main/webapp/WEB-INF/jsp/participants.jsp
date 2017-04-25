<%@ include file="/WEB-INF/jsp/include.jsp" %>

<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>


<portlet:defineObjects />

<div class="workspace-participants clearfix">
    <!-- Members -->
    <c:if test="${not empty members}">
        <p class="pull-left">
            <span class="text-muted">
                <c:choose>
                    <c:when test="${fn:length(members) eq 1}"><op:translate key="WORKSPACE_PARTICIPANTS_ONE_MEMBER" /></c:when>
                    <c:otherwise><op:translate key="WORKSPACE_PARTICIPANTS_MEMBERS" args="${fn:length(members)}" /></c:otherwise>
                </c:choose>
            </span>
            
            <c:forEach var="memberitem" items="${members}">
            	<c:set var="member" value="${memberitem.member}" />
                <a href="${memberitem.card.url}" class="no-ajax-link" title="${member.displayName}" data-toggle="tooltip" data-placement="bottom">
                    <span>
                        <img src="${member.avatar.url}" alt="${member.displayName}">
                    </span>
                </a>
            </c:forEach>
        </p>
    </c:if>
</div>
