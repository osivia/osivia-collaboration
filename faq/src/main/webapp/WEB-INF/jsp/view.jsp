<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<!-- Summary -->
<div class="panel panel-default">
    <div class="panel-heading">
        <i class="glyphicons halflings uni-bookmark"></i>
        <span>${faq.title} - <op:translate key="SUMMARY" /></span>
    </div>
    
    <div class="list-group">
        <c:forEach var="summaryQuestion" items="${questions}">
            <portlet:renderURL var="url">
                <portlet:param name="curItemPath" value="${summaryQuestion.path}" />
            </portlet:renderURL>

            <a href="${url}" class="list-group-item">
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
    <div>${question.message}</div>
    
    <c:if test="${not empty question.attachments}">
        <hr>
    
        <div class="panel panel-default">
            <div class="panel-heading">
                <i class="glyphicons halflings uni-paperclip"></i>
                <span><op:translate key="ATTACHMENTS" /></span>
            </div>
            
            <div class="list-group">
                <c:forEach var="attachment" items="${question.attachments}">
                    <a href="${attachment.url}" class="list-group-item">
                        <i class="glyphicons halflings file"></i>
                        <span>${attachment.name}</span>
                    </a>
                </c:forEach>
            </div>
        </div>
    </c:if>
</c:if>
