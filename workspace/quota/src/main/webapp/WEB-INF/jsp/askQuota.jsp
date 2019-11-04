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


<portlet:actionURL name="ask" var="askUrl" copyCurrentRenderParameters="true" />

<portlet:actionURL name="cancel" var="cancelUrl" copyCurrentRenderParameters="true">
</portlet:actionURL>


<form:form action="${askUrl}" method="post" modelAttribute="askForm">
    <spring:bind path="requestMsg">
        <div class="form-group required ${status.error ? 'has-error' : ''}">
            <form:label path="requestMsg" cssClass="control-label"><op:translate key="QUOTA_CHANGE_MSG_LABEL" /></form:label>
            <form:textarea path="requestMsg" cssClass="form-control" />
        </div>
    </spring:bind>
    
    <div class="form-group">
        <button type="submit" class="btn btn-primary">
            <span><op:translate key="ASK" /></span>
        </button>
        
        <a href="${cancelUrl}" class="btn btn-default">
            <span><op:translate key="CANCEL" /></span>
        </a>
    </div>
</form:form>

