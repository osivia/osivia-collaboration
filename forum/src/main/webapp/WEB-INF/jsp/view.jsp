<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc"%>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="delete" var="deleteActionURL" />


<c:set var="namespace" scope="request"><portlet:namespace /></c:set>

<c:set var="addTitle"><op:translate key="ADD_POST" /></c:set>
<c:set var="replyTitle" scope="request"><op:translate key="REPLY" /></c:set>
<c:set var="deleteTitle" scope="request"><op:translate key="DELETE" /></c:set>


<div class="forum forum-thread">
    <div class="media">
        <div class="media-left">
            <!-- Avatar -->
            <p>
                <c:choose>
                    <c:when test="${not empty thread.profileUrl and not empty thread.person.avatar.url}">
                        <a href="${thread.profileUrl}">
                            <img src="${thread.person.avatar.url}" alt="${thread.author}" class="media-object img-responsive">
                        </a>
                    </c:when>

                    <c:when test="${not empty thread.profileUrl}">
                        <a href="${thread.profileUrl}">
                            <i class="glyphicons glyphicons-user"></i>
                        </a>
                    </c:when>

                    <c:when test="${not empty thread.person.avatar.url}">
                        <img src="${thread.person.avatar.url}" alt="${thread.author}" class="media-object img-responsive">
                    </c:when>

                    <c:otherwise>
                        <span class="text-muted">
                            <i class="glyphicons glyphicons-user"></i>
                        </span>
                    </c:otherwise>
                </c:choose>
            </p>
        </div>

        <div class="media-body">
            <p>
                <!-- Author -->
                <c:choose>
                    <c:when test="${not empty thread.profileUrl and not empty thread.person.displayName}">
                        <a href="${thread.profileUrl}">
                            <strong>${thread.person.displayName}</strong>
                        </a>
                    </c:when>

                    <c:when test="${not empty thread.profileUrl}">
                        <a href="${thread.profileUrl}">
                            <strong>${thread.author}</strong>
                        </a>
                    </c:when>

                    <c:when test="${not empty thread.person.displayName}">
                        <strong>${thread.person.displayName}</strong>
                    </c:when>

                    <c:otherwise>
                        <strong>${thread.author}</strong>
                    </c:otherwise>
                </c:choose>

                <!-- Date -->
                <span class="text-muted">
                    <span>&ndash;</span>
                    <span><op:formatRelativeDate value="${thread.date}" /></span>
                </span>
            </p>

            <div class="panel panel-default">
                <!-- Message -->
                <div class="panel-body no-ajax-link">
                    <div><ttc:transform document="${thread.document}" property="ttcth:message" /></div>
                </div>

                <!-- Attachements -->
                <c:if test="${not empty thread.attachments}">
                    <div class="list-group">
                        <c:forEach var="attachment" items="${thread.attachments}">
                            <a href="${attachment.url}" class="list-group-item">
                                <i class="glyphicons glyphicons-paperclip"></i>
                                <span>${attachment.name}</span>
                            </a>
                        </c:forEach>
                    </div>
                </c:if>

                <!-- Buttons -->
                <c:if test="${thread.commentable}">
                    <div class="panel-body">
                        <div class="text-right">
                            <div class="btn-group">
                                <a href="#${namespace}-add-post-form" class="btn btn-default no-ajax-link" data-toggle="collapse">
                                    <i class="halflings halflings-plus"></i>
                                    <span>${addTitle}</span>
                                </a>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>


    <c:forEach var="child" items="${posts}">
        <c:set var="post" value="${child}" scope="request" />
        <jsp:include page="display-post.jsp" />
    </c:forEach>


    <!-- Buttons -->
    <c:if test="${not empty posts and thread.commentable}">
        <div class="btn-toolbar">
            <div class="btn-group pull-right">
                <a href="#${namespace}-add-post-form" class="btn btn-default no-ajax-link" data-toggle="collapse" data-parent="#${namespace}-forum">
                    <i class="halflings halflings-plus"></i>
                    <span>${addTitle}</span>
                </a>
            </div>
        </div>
    </c:if>


    <!-- Add post form -->
    <div id="${namespace}-add-post-form" class="collapse">
        <c:set var="root" value="true" scope="request" />
        <c:remove var="parentId" scope="request" />
        <jsp:include page="reply-form.jsp" />
    </div>


    <!-- Delete confirmation fancybox -->
    <div class="hidden">
        <div id="${namespace}-delete-fancybox" class="delete-fancybox">
            <form action="${deleteActionURL}" method="post" role="form">
                <input type="hidden" name="id">

                <p class="help-block">
                    <span><op:translate key="COMMENT_SUPPRESSION_CONFIRM_MESSAGE" /></span>
                </p>

                <div class="text-center">
                    <button type="submit" class="btn btn-warning">
                        <i class="halflings halflings-alert"></i>
                        <span><op:translate key="YES" /></span>
                    </button>

                    <button type="button" class="btn btn-default" onclick="closeFancybox()">
                        <op:translate key="NO" />
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
