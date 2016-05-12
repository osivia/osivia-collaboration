<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<!-- Summary -->
<div class="panel panel-default">
    <div class="panel-heading">
        <i class="glyphicons glyphicons-list"></i>
        <span>${faq.title}</span>
        <span>-</span>
        <span><op:translate key="SUMMARY" /></span>
    </div>
    
    <div class="list-group">
        <c:forEach var="summaryQuestion" items="${questions}">
            <portlet:renderURL var="url">
                <portlet:param name="curItemPath" value="${summaryQuestion.path}" />
            </portlet:renderURL>

            <a href="${url}" class="list-group-item no-ajax-link">
                <c:choose>
                    <c:when test="${summaryQuestion.id eq question.id}">
                        <strong class="text-primary">${summaryQuestion.title}</strong>
                    </c:when>
                    
                    <c:otherwise>
                        <span>${summaryQuestion.title}</span>
                    </c:otherwise>
                </c:choose>
            </a>
        </c:forEach>
    </div>
</div>


<!-- Current question -->
<c:if test="${not empty question}">
    <p class="lead">${question.title}</p>
    
    <p>${question.message}</p>
    
    <c:if test="${not empty question.attachments}">
        <hr>
    
        <div class="panel panel-default">
            <div class="panel-heading">
                <h3 class="panel-title">
                    <i class="glyphicons glyphicons-paperclip"></i>
                    <span><op:translate key="ATTACHMENTS" /></span>
                </h3>
            </div>
            
            <div class="list-group">
                <c:forEach var="attachment" items="${question.attachments}">
                    <a href="${attachment.url}" class="list-group-item">
                        <span>${attachment.name}</span>
                    </a>
                </c:forEach>
            </div>
        </div>
    </c:if>
</c:if>
