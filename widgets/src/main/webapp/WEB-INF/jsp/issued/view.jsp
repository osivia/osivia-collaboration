<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />


<portlet:actionURL name="save" var="url" />


<form:form action="${url}" method="post" modelAttribute="form">
    <fieldset>
        <legend>
            <i class="glyphicons glyphicons-calendar"></i>
            <span><op:translate key="UPDATE_ISSUED_DATE_LEGEND" /></span>
        </legend>

        <!-- Issued date -->
        <c:set var="placeholder"><op:translate key="ISSUED_DATE_PLACEHOLDER" /></c:set>
        <spring:bind path="date">
            <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                <form:label path="date" cssClass="control-label"><op:translate key="ISSUED_DATE_LABEL" /></form:label>
                <form:input path="date" type="date" cssClass="form-control" placeholder="${placeholder}" />
                <c:if test="${status.error}">
                    <span class="form-control-feedback">
                        <i class="glyphicons glyphicons-remove"></i>
                    </span>
                </c:if>
                <form:errors path="date" cssClass="help-block" />
            </div>
        </spring:bind>
        
        <!-- Buttons -->
        <div>
            <!-- Save -->
            <button type="submit" class="btn btn-primary">
                <span><op:translate key="UPDATE_ACTION" /></span>
            </button>
            
            <!-- Cancel -->
            <button type="button" class="btn btn-default" data-dismiss="modal">
                <span><op:translate key="CANCEL" /></span>
            </button>
        </div>
    </fieldset>
</form:form>
