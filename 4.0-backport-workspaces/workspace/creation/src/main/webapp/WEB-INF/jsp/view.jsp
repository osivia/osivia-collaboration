<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="save" var="saveUrl" />


<form:form action="${saveUrl}" method="post" modelAttribute="form" cssClass="form-horizontal" role="form">
    <fieldset>
        <legend>
            <span><op:translate key="WORKSPACE_CREATION_LEGEND" /></span>
        </legend>

        <!-- Title -->
        <c:set var="placeholder"><op:translate key="WORKSPACE_TITLE_PLACEHOLDER" /></c:set>
        <spring:bind path="title">
            <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                <form:label path="title" cssClass="col-sm-3 control-label"><op:translate key="WORKSPACE_TITLE" /></form:label>
                <div class="col-sm-9">
                    <form:input path="title" cssClass="form-control" placeholder="${placeholder}" />
                    <c:if test="${status.error}">
                        <span class="form-control-feedback">
                            <i class="glyphicons glyphicons-remove"></i>
                        </span>
                    </c:if>
                    <form:errors path="title" cssClass="help-block" />
                </div>
            </div>
        </spring:bind>
        
        <!-- Description -->
        <c:set var="placeholder"><op:translate key="WORKSPACE_DESCRIPTION_PLACEHOLDER" /></c:set>
        <div class="form-group">
            <form:label path="description" cssClass="col-sm-3 control-label" ><op:translate key="WORKSPACE_DESCRIPTION" /></form:label>
            <div class="col-sm-9">
                <form:textarea path="description" cssClass="form-control" placeholder="${placeholder}" />
            </div>
        </div>
        
        <!-- Buttons -->
        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <!-- Save -->
                <button type="submit" class="btn btn-primary">
                    <i class="glyphicons glyphicons-floppy-disk"></i>
                    <span><op:translate key="SAVE" /></span>
                </button>
                
                <!-- Cancel -->
                <button type="button" class="btn btn-default" onclick="closeFancybox()">
                    <span><op:translate key="CANCEL" /></span>
                </button>
            </div>
        </div>
    </fieldset>
</form:form>
