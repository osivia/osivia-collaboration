<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page isELIgnored="false" %>


<div class="media">
    <div class="pull-left">
        <!-- Avatar -->
        <p>
            <c:choose>
                <c:when test="${not empty post.person.avatar.url}">
                    <img src="${post.person.avatar.url}" alt="${post.author}" class="media-object img-responsive center-block">
                </c:when>
                
                <c:otherwise>
                    <div class="h2 text-center text-muted">
                        <i class="glyphicons glyphicons-user"></i>
                    </div>
                </c:otherwise>
            </c:choose>
        </p>
    </div>
    
    <div>
        <p>
            <!-- Author -->
            <c:choose>
                <c:when test="${not empty post.profileURL and not empty post.person.displayName}">
                    <a href="${post.profileURL}">
                        <strong>${post.person.displayName}</strong>
                    </a>
                </c:when>
            
                <c:when test="${not empty post.profileURL}">
                    <a href="${post.profileURL}">
                        <strong>${post.author}</strong>
                    </a>
                </c:when>
            
                <c:when test="${not empty post.person.displayName}">
                    <strong>${post.person.displayName}</strong>
                </c:when>
                
                <c:otherwise>
                    <strong>${post.author}</strong>
                </c:otherwise>
            </c:choose>

            <!-- Date -->
            <span class="text-muted">
                <span>&ndash;</span>
                <span><fmt:formatDate value="${post.date}" type="both" dateStyle="long" timeStyle="short" /></span>
            </span>
        </p>
    
        <div class="panel panel-default">
            <div class="panel-body">
                <!-- Message -->
                <p>${post.message}</p>
                
                <!-- Attachment -->
                <c:if test="${not empty post.attachmentURL}">
                    <p>
                        <i class="halflings halflings-glyph-paperclip"></i>
                        <a href="${post.attachmentURL}">${post.attachmentName}</a>
                    </p>
                </c:if>
                
                <!-- Buttons -->
                <div class="text-right">
                    <div class="btn-group btn-group-sm">
                        <c:if test="${thread.commentable}">
                            <a href="#${namespace}-${post.id}-reply-form" class="btn btn-default no-ajax-link" data-toggle="collapse">
                                <i class="halflings halflings-comments"></i>
                                <span>${replyTitle}</span>
                            </a>
                        </c:if>
                        
                        <c:if test="${post.deletable}">
                            <a href="#${namespace}-delete-fancybox" onclick="selectDelete(this, '${post.id}')" class="btn btn-default fancybox_inline no-ajax-link">
                                <i class="halflings halflings-trash"></i>
                                <span>${deleteTitle}</span>
                            </a>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>
        
        
        <!-- Reply form -->
        <div id="${namespace}-${post.id}-reply-form" class="collapse">
            <c:set var="root" value="false" scope="request" />
            <c:set var="parentId" value="${post.id}" scope="request" />
            
            <jsp:include page="reply-form.jsp" />
        </div>
        
        
        <!-- Children -->
        <c:if test="${not empty post.children}">
            <c:forEach var="child" items="${post.children}">
                <c:set var="post" value="${child}" scope="request" />
                
                <jsp:include page="display-post.jsp" />
            </c:forEach>
        </c:if>
    </div>
</div>
