<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="workspace-participants workspace-participants-full">
    <c:forEach var="group" items="${participants.groups}">
        <c:if test="${not empty group.members}">
            <div class="row">
                <div class="col-lg-2">
                    <h3 class="h4">
                        <span><op:translate key="GROUP_${group.role.key}" /></span>
                    </h3>
                </div>
                
                <div class="col-lg-10">
                    <ul class="list-unstyled">
                        <c:forEach var="member" items="${group.members}" varStatus="status">
                        	                        	
                        	<c:set var="profession" value="${member.nxProfile.properties['ttc_userprofile:profession']}" />
                        	<c:set var="institution" value="${member.nxProfile.properties['ttc_userprofile:institution']}" />
                        
                            <li>
                                <div class="panel panel-primary">
                                    <div class="panel-heading">
                                        <div class="thumbnail">
                                            <img src="${member.avatarUrl}" alt="" class="text-middle">
                                        </div>
                                    </div>
                                
                                    <div class="panel-body">
                                        <h4 class="text-center text-overflow no-ajax-link">
                                        	<ttc:user name="${member.id}" hideAvatar="true" linkable="${participants.showPrivacyData}"/>
                                        </h4>
                                            
                                        <c:if test="${not empty member.email && participants.showPrivacyData}">
                                            <p class="small text-center text-overflow">
                                                <a href="mailto:${member.email}" class="no-ajax-link">
                                                    <span>${member.email}</span>
                                                </a>
                                            </p>
                                        </c:if>
                                        
                                        <p class="small text-center text-muted text-overflow">
                                			<c:if test="${not empty profession}">
                                				<span>${profession}</span>
                                			</c:if>
                                			<c:if test="${not empty profession and not empty institution}">
                                				<span>-</span>
                                			</c:if>
                                			<c:if test="${not empty institution}">
                                				<span>${institution}</span>
                                			</c:if>
                                            <c:if test="${(not empty profession or not empty institution) and not empty member.joinedDate}">
                                                <br>
                                            </c:if>
                                            <c:if test="${not empty member.joinedDate}">
                                                <span><op:translate key="WORKSPACE_PARTICIPANTS_MEMBER_SINCE" /></span>
                                                <span><fmt:formatDate value="${member.joinedDate}" type="date" dateStyle="long" /></span>
                                            </c:if>
                                        </p>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </c:if>
    </c:forEach>
</div>
