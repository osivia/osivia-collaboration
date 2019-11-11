<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects/>

<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<portlet:actionURL name="save-quota" var="saveUrl" copyCurrentRenderParameters="true" />
<portlet:actionURL name="refuse-quota" var="refuseUrl" copyCurrentRenderParameters="true" />
<portlet:actionURL name="cancel-quota" var="cancelUrl" copyCurrentRenderParameters="true">
</portlet:actionURL>


<form:form action="${saveUrl}" method="post" modelAttribute="updateForm">

    <spring:bind path="currentSize">
        <div class="form-group ">
            <form:label path="currentSize" cssClass="control-label"><op:translate key="QUOTA_CURRENT_SIZE_LABEL" /></form:label>
            <div class="form-control">
            	${updateForm.currentSize}
            </div>
        </div>
    </spring:bind>
    

    <spring:bind path="size">
        <div class="form-group required ${status.error ? 'has-error' : ''}">
            <form:label path="size" cssClass="control-label"><op:translate key="QUOTA_CHANGE_SIZE_LABEL" /></form:label>
            <form:input path="size" cssClass="form-control" />
            <form:errors path="size" cssClass="help-block" />
        </div>
    </spring:bind>
    
    <c:if test="${updateForm.stepRequest}">
	    <spring:bind path="message">
	        <div class="form-group ">
	            <form:label path="message" cssClass="control-label"><op:translate key="QUOTA_MESSAGE_LABEL" /></form:label>
	            <div class="form-control">
	            	${updateForm.message}
	            </div>
	        </div>
	    </spring:bind>    
    </c:if>
    
    <div class="form-group">
    	<c:choose>
    		<c:when test="${updateForm.stepRequest}">
		    	<button type="submit" class="btn btn-primary">
		            <span><op:translate key="CHANGE_AND_ACCEPT" /></span>
		        </button>
	            <a href="${refuseUrl}" class="btn btn-default">
		            <span><op:translate key="REFUSE" /></span>
		        </a>
    		</c:when>
    		<c:otherwise>
    			<button type="submit" class="btn btn-primary">
		            <span><op:translate key="CHANGE" /></span>
		        </button>
    		</c:otherwise>
    	</c:choose>
    


        <a href="${cancelUrl}" class="btn btn-default">
            <span><op:translate key="CANCEL" /></span>
        </a>
    </div>
</form:form>

