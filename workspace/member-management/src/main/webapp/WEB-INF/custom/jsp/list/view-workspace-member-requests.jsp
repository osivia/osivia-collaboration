<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<c:set var="namespace"><portlet:namespace /></c:set>


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

                <!-- Type -->
                <c:if test="${not empty workspaceType}">
                    <p>
                        <span class="label label-${workspaceType.color}">
                            <i class="${workspaceType.icon}"></i>
                            <span><op:translate key="LIST_TEMPLATE_${workspaceType.key}" /></span>
                        </span>
                    </p>
                </c:if>
                
                <!-- Description -->
                <c:if test="${not empty description}">
                    <p class="text-pre-wrap">${description}</p>
                </c:if>
                
                <!-- Action -->
                <p class="small">
                    <c:choose>
                        <c:when test="${empty memberStatus}">
                            <a href="javascript:;" onclick="$JQry('#${namespace}-confirmation-button').attr('href', '${createRequestUrl}');" data-toggle="modal" data-target="#${namespace}-confirmation">
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
                </p>
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


<div id="${namespace}-confirmation" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog modal-sm" role="document">
        <div class="modal-content">
            <div class="modal-body">
                <p class="text-center"><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CONFIRMATION_MESSAGE_1" /></p>
                <p class="text-center"><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CONFIRMATION_MESSAGE_2" /></p>
                <p class="text-center">
                    <a id="${namespace}-confirmation-button" href="#" class="btn btn-default">
                        <span><op:translate key="YES" /></span>
                    </a>
                    
                    <button type="button" class="btn btn-default" data-dismiss="modal">
                        <span><op:translate key="NO" /></span>
                    </button>
                </p>
            </div>
        </div>
    </div>
</div>
