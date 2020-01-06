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
                    <ttc:title document="${document}" linkable="${(workspaceType.id eq 'PUBLIC') || (workspaceType.id eq 'PUBLIC_INVITATION')}" />
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
                <c:if test="${workspaceType.allowedInvitationRequests}">
                    <p>
                        <c:choose>
                            <c:when test="${empty memberStatus}">
                                <button type="button" onclick="$JQry('#${namespace}-confirmation-form').attr('action', '${createRequestUrl}');" class="btn btn-secondary btn-sm" data-toggle="modal" data-target="#${namespace}-confirmation">
                                    <span><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CREATION" /></span>
                                </button>
                            </c:when>
                            
                            <c:otherwise>
                                <span class="text-${memberStatus.color}">
                                    <i class="${memberStatus.icon}"></i>
                                    <span><op:translate key="${memberStatus.key}" /></span>
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </p>
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


<div id="${namespace}-confirmation" class="modal fade" tabindex="-1" role="dialog">
	<form id="${namespace}-confirmation-form" action="#" method="post">
	
	    <div class="modal-dialog" role="document">
	        <div class="modal-content">
	            <div class="modal-header">
	                <button type="button" class="close" data-dismiss="modal">
	                    <i class="glyphicons glyphicons-remove"></i>
	                    <span class="sr-only"><op:translate key="CLOSE" /></span>
	                </button>
	
	                <h4 class="modal-title"><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CONFIRMATION_TITLE" /></h4>
	            </div>
	        
	            <div class="modal-body">
	                <p><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CONFIRMATION_MESSAGE_1" /></p>
	
	                <p class="form-group required">
	                	<label class="control-label" for="userMessage"><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_INPUT_MESSAGE" /></label>
	                	
	                	<textarea id="${namespace}-message" class="form-control" name="userMessage" rows="4" onkeyup="if($JQry('#${namespace}-message').val() != '') {$JQry('#${namespace}-confirmation-submit').removeAttr('disabled')} else {$JQry('#${namespace}-confirmation-submit').attr('disabled','disabled')}"></textarea>
	                </p>
	            </div>
	            
	            <div class="modal-footer">
	                <button id="${namespace}-confirmation-submit"  type="submit" class="btn btn-primary" disabled="disabled">
	                    <i class="glyphicons glyphicons-inbox-out"></i>
	                    <span><op:translate key="LIST_TEMPLATE_WORKSPACE_MEMBER_REQUESTS_CONFIRM" /></span>
	                </button>
	                
	                <button type="button" class="btn btn-secondary" data-dismiss="modal">
	                    <span><op:translate key="CANCEL" /></span>
	                </button>
	            </div>
	        </div>
	    </div>
    </form>
</div>
