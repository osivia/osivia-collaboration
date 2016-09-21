<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<ul class="list-unstyled">
    <c:forEach var="document" items="${documents}" varStatus="status">
        <c:set var="vignetteUrl"><ttc:pictureLink document="${document}" property="ttc:vignette" /></c:set>
        <c:set var="description" value="${document.properties['dc:description']}" />
        <c:set var="memberStatus" value="${document.properties['memberStatus']}" />
    
        <portlet:actionURL name="createRequest" var="createRequestUrl">
            <portlet:param name="id" value="${document.properties['webc:url']}" />
        </portlet:actionURL>
    
    
        <li>
            <div class="media">
                <!-- Vignette -->
                <c:if test="${not empty vignetteUrl}">
                    <div class="media-left">
                        <img src="${vignetteUrl}" alt="" class="media-object">
                    </div>
                </c:if>
                
                <div class="media-body media-middle">
                    <!-- Title -->
                    <h3 class="h4 media-heading"><ttc:title document="${document}" linkable="${memberStatus.id eq 'member'}" /></h3>
                    
                    <!-- Description -->
                    <div>${description}</div>
                </div>
                
                <!-- Status -->
                <div class="media-right media-middle hidden-xs">
                    <c:choose>
                        <c:when test="${empty memberStatus}">
                            <a href="${createRequestUrl}" class="btn btn-default">
                                <span><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CREATION" /></span>
                            </a>
                        </c:when>
                        
                        <c:otherwise>
                            <span class="text-nowrap text-${memberStatus.color}">
                                <i class="${memberStatus.icon}"></i>
                                <span><op:translate key="${memberStatus.key}" /></span>
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <div class="media visible-xs">
                <div class="media-body">
                    <c:choose>
                        <c:when test="${empty memberStatus}">
                            <a href="${createRequestUrl}" class="btn btn-default">
                                <span><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CREATION" /></span>
                            </a>
                        </c:when>
                        
                        <c:otherwise>
                            <span class="text-${memberStatus.color}">
                                <i class="${memberStatus.icon}"></i>
                                <span><op:translate key="${memberStatus.key}" /></span>
                            </span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            
            <c:if test="${not status.last}">
                <hr>
            </c:if>
        </li>
    </c:forEach>
</ul>