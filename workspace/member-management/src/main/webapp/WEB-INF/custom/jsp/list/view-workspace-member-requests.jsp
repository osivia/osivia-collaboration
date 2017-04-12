<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<ul class="list-unstyled">
    <c:forEach var="document" items="${documents}" varStatus="status">
        <c:set var="vignetteUrl"><ttc:pictureLink document="${document}" property="ttc:vignette" /></c:set>
        <c:set var="description" value="${document.properties['dc:description']}" />
        <c:set var="workspaceType" value="${document.properties['workspaceType']}" />
        <c:set var="memberStatus" value="${document.properties['memberStatus']}" />
    
        <portlet:actionURL name="createRequest" var="createRequestUrl">
            <portlet:param name="id" value="${document.properties['webc:url']}" />
        </portlet:actionURL>
    
    
        <li class="media">
            <!-- Vignette -->
            <c:if test="${not empty vignetteUrl}">
                <div class="media-left media-middle">
                    <img src="${vignetteUrl}" alt="" class="media-object">
                </div>
            </c:if>
            
            <div class="media-body media-middle">
                <!-- Title -->
                <h3 class="h4 media-heading">
                    <ttc:title document="${document}" linkable="${(workspaceType.id eq 'PUBLIC')}" />
                </h3>
                
                
                <div class="clearfix">
                    <!-- Type -->
                    <c:if test="${not empty workspaceType}">
                        <p class="pull-left">
                            <span class="label label-${workspaceType.color}">
                                <i class="${workspaceType.icon}"></i>
                                <span><op:translate key="LIST_TEMPLATE_${workspaceType.key}" /></span>
                            </span>
                        </p>
                    </c:if>

                    <!-- Action -->
                    <div class="pull-right">
                        <c:choose>
                            <c:when test="${empty memberStatus}">
                                <a href="${createRequestUrl}" class="btn btn-default btn-sm">
                                    <span><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CREATION" /></span>
                                </a>
                            </c:when>
                            
                            <c:otherwise>
                                <p class="text-${memberStatus.color}">
                                    <i class="${memberStatus.icon}"></i>
                                    <span><op:translate key="${memberStatus.key}" /></span>
                                </p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
                
                <!-- Description -->
                <c:if test="${not empty description}">
                    <p class="text-pre-wrap">${description}</p>
                </c:if>
                
                
            </div>
        </li>
    </c:forEach>
    
    
    <c:if test="${empty documents}">
        <li>
            <p class="text-center">
                <span class="text-muted"><op:translate key="LIST_NO_ITEMS" /></span>
            </p>
        </li>
    </c:if>
</ul>