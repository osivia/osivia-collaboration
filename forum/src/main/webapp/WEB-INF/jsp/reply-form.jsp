<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<portlet:actionURL name="add" var="addActionURL">
    <c:if test="${not empty parentId}">
        <portlet:param name="parentId" value="${parentId}" />
    </c:if>
</portlet:actionURL>


<div class="well">
    <form:form modelAttribute="replyForm" action="${addActionURL}" method="post" enctype="multipart/form-data" role="form">
        <fieldset>
            <legend>
                <c:choose>
                    <c:when test="${root}"><op:translate key="ADD_POST" /></c:when>
                    <c:otherwise><op:translate key="REPLY" /></c:otherwise>
                </c:choose>
            </legend>

            <div class="form-group reply-content">
                <form:label path="content" class="control-label"><op:translate key="CONTENT" /></form:label>
                <form:textarea path="content" rows="4" class="form-control" />
            </div>
            
            <div class="form-group">
                <form:label path="attachment" class="control-label"><op:translate key="ATTACHMENT" /></form:label>
                <form:input type="file" path="attachment" />
            </div>
            
            <button type="submit" class="btn btn-primary">
                <i class="halflings halflings-comments"></i>
                <span><op:translate key="REPLY" /></span>
            </button>
            
            <button type="button" class="btn btn-default" onclick="cancelReply(this)"><op:translate key="CANCEL" /></button>
        </fieldset>
    </form:form>
</div>
