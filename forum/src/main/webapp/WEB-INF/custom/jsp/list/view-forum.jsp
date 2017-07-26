<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<div class="forum-list">
    <c:if test="${not empty forums}">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <i class="glyphicons glyphicons-conversation"></i>
                    <span><op:translate key="LIST_TEMPLATE_SUB_FORUMS" /></span>
                </h3>
            </div>
            
            <ul class="list-group">
                <c:forEach var="forum" items="${forums}">
                    <!-- Document properties -->
                    <c:set var="vignetteUrl"><ttc:pictureLink document="${forum}" property="ttc:vignette" /></c:set>
                    <c:set var="description" value="${forum.properties['dc:description']}" />
                    
                    <li class="list-group-item">
                        <div class="media">
                            <!-- Vignette -->
                            <c:if test="${not empty vignetteUrl}">
                                <div class="media-left media-middle">
                                    <img src="${vignetteUrl}" alt="" class="media-object">
                                </div>
                            </c:if>
                            
                            <div class="media-body media-middle">
                                <!-- Title -->
                                <h3 class="h4 media-heading">
                                    <span><ttc:title document="${forum}" /></span>
                                </h3>
                                                        
                                <!-- Description -->
                                <c:if test="${not empty description}">
                                    <p class="pre-wrap">${description}</p>
                                </c:if>
                            </div>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </c:if>

    <c:if test="${not empty threads}">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <i class="glyphicons glyphicons-chat"></i>
                    <span><op:translate key="LIST_TEMPLATE_THREADS" /></span>
                </h3>
            </div>
            
            <ul class="list-group">
                <c:forEach var="thread" items="${threads}">
                    <!-- Document properties -->
                    <c:set var="url"><ttc:documentLink document="${thread}" /></c:set>
                    <c:set var="vignetteUrl"><ttc:pictureLink document="${thread}" property="ttc:vignette" /></c:set>
                    <c:set var="description" value="${thread.properties['dc:description']}" />
                    <c:set var="threadCreator" value="${thread.properties['dc:creator']}" />
                    <c:set var="threadDate" value="${thread.properties['dc:created']}" />
                    <c:set var="lastCommentAuthor" value="${thread.properties['ttcth:lastCommentAuthor']}" />
                    <c:set var="lastCommentDate" value="${thread.properties['ttcth:lastCommentDate']}" />
                    <c:set var="nbAnswers" value="${thread.properties['ttcth:nbComments']}" />
                
                    <li class="list-group-item">
                        <div class="media">
                            <c:if test="${not empty vignetteUrl}">
                                <div class="media-left media-middle">
                                    <img src="${vignetteUrl}" alt="" class="media-object">
                                </div>
                            </c:if>
                            
                            <div class="media-body media-middle">
                                <h3 class="h4 media-heading">
                                    <a href="${url}" class="no-ajax-link">${thread.title}</a>
                                </h3>
                            
                                <c:if test="${not empty description}">
                                    <p class="pre-wrap">${description}</p>
                                </c:if>
                                
                                <p class="text-muted">
                                    <span><op:translate key="TOPIC_STARTED" /></span>
                                    <span><op:formatRelativeDate value="${threadDate}" /></span>
                                    <span><op:translate key="BY" /></span>
                                    <span><ttc:user name="${threadCreator}" /></span>
                                    
                                    <c:if test="${nbAnswers gt 0}">
                                        <br>
                                    
                                        <span><op:translate key="LAST_ANSWER" /></span>
                                        <span><op:formatRelativeDate value="${lastCommentDate}" /></span>
                                        <span><op:translate key="BY" /></span>
                                        <span><ttc:user name="${lastCommentAuthor}" /></span>
                                    </c:if>
                                </p>
                            </div>
                            
                            <c:if test="${not empty nbAnswers}">
                                <div class="media-right media-middle">
                                    <div class="text-center">
                                        <div class="h2 media-heading">${nbAnswers}</div>
                                        <div>
                                            <c:choose>
                                                <c:when test="${nbAnswers gt 1}"><op:translate key="FORUM_ANSWERS" /></c:when>
                                                <c:otherwise><op:translate key="FORUM_ANSWER" /></c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </li>
                </c:forEach>
            </ul>
        </div>
    </c:if>
</div>
