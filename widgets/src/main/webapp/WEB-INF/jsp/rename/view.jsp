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
            <i class="glyphicons glyphicons-basic-square-edit"></i>
            <span><op:translate key="RENAME_LEGEND" /></span>
        </legend>

        <%--Title--%>
        <c:set var="placeholder"><op:translate key="TITLE_PLACEHOLDER" /></c:set>
        <spring:bind path="title">
            <div class="form-group required">
                <form:label path="title"><op:translate key="TITLE_LABEL" /></form:label>
                <form:input path="title" cssClass="form-control ${status.error ? 'is-invalid' : ''}" placeholder="${placeholder}" />
                <form:errors path="title" cssClass="invalid-feedback" />
            </div>
        </spring:bind>
        
        <%--Buttons--%>
        <div>
            <button type="submit" class="btn btn-primary">
                <span><op:translate key="RENAME_ACTION" /></span>
            </button>
            
            <button type="button" class="btn btn-secondary" data-dismiss="modal">
                <span><op:translate key="CANCEL" /></span>
            </button>
        </div>
    </fieldset>
</form:form>
