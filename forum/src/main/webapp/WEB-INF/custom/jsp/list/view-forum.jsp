<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false" %>


<div class="forum-list">
    <ul class="list-unstyled">
        <c:forEach var="document" items="${documents}">
            <!-- Document properties -->
            <c:set var="url"><ttc:documentLink document="${document}" /></c:set>
            <c:set var="vignetteURL"><ttc:pictureLink document="${document}" property="ttc:vignette" /></c:set>
            <c:set var="description" value="${document.properties['dc:description']}" />
            
	        <!-- Author -->
	        <c:set var="threadCreator" value="${document.properties['dc:creator']}" />
	        <!-- Date -->
	        <c:set var="threadDate" value="${document.properties['dc:created']}" />
	        
	        <!-- comment author -->
	        <c:set var="lastCommentAuthor" value="${document.properties['ttcth:lastCommentAuthor']}" />
	        <!-- comment date -->
	        <c:set var="lastCommentDate" value="${document.properties['ttcth:lastCommentDate']}" />
        
            <c:set var="nbAnswers" value="${document.properties['ttcth:nbComments']}" />
        
        
            <li>
                <div class="panel panel-default">
                    <div class="panel-body">
                        <div class="media">
                            <div class="media-left media-middle">
                                <div class="text-center">
                                    <div class="media-heading h2">
                                        <c:choose>
                                            <c:when test="${empty nbAnswers}">-</c:when>
                                            <c:otherwise>${nbAnswers}</c:otherwise>
                                        </c:choose>
                                    </div>
                                    <div>
                                        <c:choose>
                                            <c:when test="${nbAnswers gt 1}"><op:translate key="FORUM_ANSWERS" /></c:when>
                                            <c:otherwise><op:translate key="FORUM_ANSWER" /></c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </div>
                            
                            <div class="media-body">
                                <h3 class="media-heading h4">
                                    <a href="${url}" class="no-ajax-link">${document.title}</a>
                                </h3>
                                
                                <div class="media">
                                    <c:if test="${not empty vignetteURL}">
                                        <div class="media-left">
                                            <img src="${vignetteURL}" alt="" class="media-object">
                                        </div>
                                    </c:if>
                                    
                                    <div class="media-body">
                                        <c:if test="${not empty description}">
                                            <p>${description}</p>
                                        </c:if>
                                        
										<div class="small">
				                            <span><op:translate key="TOPIC_STARTED" /></span>
                                            <span><op:formatRelativeDate value="${threadDate}" /></span>
				                            <span><op:translate key="BY" /></span>
				                            <span><ttc:user name="${threadCreator}" linkable="false" /></span>
				                        </div>    
				                        
				                                            
				                        <c:if test="${nbAnswers gt 0}">
					                        <div class="small">
					                            <span><op:translate key="LAST_ANSWER" /></span>
                                                <span><op:formatRelativeDate value="${lastCommentDate}" /></span>
					                            <span><op:translate key="BY" /></span>
					                            <span><ttc:user name="${lastCommentAuthor}" linkable="false" /></span>
					                        </div>
				                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>     
                    </div>
                </div>
            </li>
        </c:forEach>
    </ul>
</div>
