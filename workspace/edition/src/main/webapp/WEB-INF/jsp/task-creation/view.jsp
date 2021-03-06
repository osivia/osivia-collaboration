<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="save" var="saveUrl" />


<div class="workspace-task-creation">
    <form:form action="${saveUrl}" method="post" modelAttribute="taskCreationForm" cssClass="form-horizontal" role="form">
        <!-- Title -->
        <c:set var="placeholder"><op:translate key="WORKSPACE_CREATE_TASK_TITLE_PLACEHOLDER" /></c:set>
        <spring:bind path="title">
            <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                <form:label path="title" cssClass="col-sm-3 control-label"><op:translate key="WORKSPACE_CREATE_TASK_TITLE" /></form:label>
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
        <c:set var="placeholder"><op:translate key="WORKSPACE_CREATE_TASK_DESCRIPTION_PLACEHOLDER" /></c:set>
        <div class="form-group">
            <form:label path="description" cssClass="col-sm-3 control-label"><op:translate key="WORKSPACE_CREATE_TASK_DESCRIPTION" /></form:label>
            <div class="col-sm-9">
                <form:textarea path="description" cssClass="form-control" rows="3" placeholder="${placeholder}" />
            </div>
        </div>
        
        <!-- Type -->
        <spring:bind path="type">
            <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                <form:label path="type" cssClass="col-sm-3 control-label"><op:translate key="WORKSPACE_CREATE_TASK_TYPE" /></form:label>
                <div class="col-sm-9">
                    <c:forEach var="type" items="${types}">
                        <div class="radio">
                            <label>
                                <form:radiobutton path="type" value="${type.value.name}" />
                                <i class="${type.value.icon}"></i>
                                <span>${type.key}</span>
                            </label>
                        </div>
                    </c:forEach>
                    <form:errors path="type" cssClass="help-block" />
                </div>
            </div>
        </spring:bind>
        
        <!-- Buttons -->
        <div class="row">
            <div class="col-sm-offset-3 col-sm-9">
                <!-- Save -->
                <button type="submit" class="btn btn-primary">
                    <span><op:translate key="VALIDATE" /></span>
                </button>
                
                <!-- Cancel -->
                <button type="button" class="btn btn-secondary" data-dismiss="modal">
                    <span><op:translate key="CANCEL" /></span>
                </button>
            </div>
        </div>
    </form:form>
</div>
