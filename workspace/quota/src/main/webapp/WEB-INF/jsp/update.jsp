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

<portlet:actionURL name="cancel-quota" var="cancelUrl" copyCurrentRenderParameters="true">
</portlet:actionURL>


<form:form action="${saveUrl}" method="post" modelAttribute="updateForm">
    <spring:bind path="size">
        <div class="form-group required ${status.error ? 'has-error' : ''}">
            <form:label path="size" cssClass="control-label"><op:translate key="QUOTA_CHANGE_SIZE_LABEL" /></form:label>
            <form:select path="size" cssClass="form-control">
                <c:forEach var="size" items="${options.sizes}">
                    <form:option value="${size}"><op:translate key="QUOTA_SIZE_${size}" /></form:option>
                </c:forEach>
            </form:select>
            <form:errors path="size" cssClass="help-block" />
        </div>
    </spring:bind>
    
    <div class="form-group">
        <button type="submit" class="btn btn-primary">
            <span><op:translate key="CHANGE" /></span>
        </button>
        
        <a href="${cancelUrl}" class="btn btn-default">
            <span><op:translate key="CANCEL" /></span>
        </a>
    </div>
</form:form>

