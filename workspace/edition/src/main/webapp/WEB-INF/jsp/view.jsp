<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="save" var="saveUrl" />


<form:form action="${saveUrl}" method="post" modelAttribute="form" cssClass="form-horizontal" role="form">
    <!-- Title -->
    <c:set var="placeholder"><op:translate key="WORKSPACE_TITLE_PLACEHOLDER" /></c:set>
    <spring:bind path="title">
        <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
            <form:label path="title" cssClass="col-sm-3 col-lg-2 control-label"><op:translate key="WORKSPACE_TITLE" /></form:label>
            <div class="col-sm-9 col-lg-10">
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
        <form:label path="description" cssClass="col-sm-3 col-lg-2 control-label" ><op:translate key="WORKSPACE_DESCRIPTION" /></form:label>
        <div class="col-sm-9 col-lg-10">
            <form:textarea path="description" cssClass="form-control" placeholder="${placeholder}" />
        </div>
    </div>
    
    <!-- Tasks -->
    <div class="form-group">
        <label class="col-sm-3 col-lg-2 control-label"><op:translate key="WORKSPACE_TASKS" /></label>
        <div class="col-sm-9 col-lg-10">
            <div class="row">
                <div class="col-sm-6">
                    <div class="panel panel-primary">
                        <div class="panel-heading"><op:translate key="WORKSPACE_ACTIVE_TASKS" /></div>
                        <div class="panel-body">
                            <p class="text-muted"><op:translate key="WORKSPACE_ACTIVE_TASKS_HELP" /></p>
                            <ul class="list-sortable workspace-creation-sortable active-tasks">
                                <c:forEach var="task" items="${form.tasks}" varStatus="status">
                                    <c:if test="${task.active}">
                                        <li>
                                            <form:hidden path="tasks[${status.index}].active" />
                                            <form:hidden path="tasks[${status.index}].order" />
                                        
                                            <i class="${task.icon}"></i>
                                            <span>${task.displayName}</span>
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
                
                <div class="col-sm-6">
                    <div class="panel panel-default">
                        <div class="panel-heading"><op:translate key="WORKSPACE_IDLE_TASKS" /></div>
                        <div class="panel-body">
                            <p class="text-muted"><op:translate key="WORKSPACE_IDLE_TASKS_HELP" /></p>
                            <ul class="list-sortable workspace-creation-sortable idle-tasks">
                                <c:forEach var="task" items="${form.tasks}" varStatus="status">
                                    <c:if test="${not task.active}">
                                        <li>
                                            <form:hidden path="tasks[${status.index}].active" />
                                            <form:hidden path="tasks[${status.index}].order" />
                                        
                                            <i class="${task.icon}"></i>
                                            <span>${task.displayName}</span>
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Buttons -->
    <div class="form-group">
        <div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">
            <!-- Save -->
            <button type="submit" name="save" class="btn btn-primary">
                <i class="glyphicons glyphicons-floppy-disk"></i>
                <span><op:translate key="SAVE" /></span>
            </button>
            
            <!-- Cancel -->
            <button type="submit" name="cancel" class="btn btn-default">
                <span><op:translate key="CANCEL" /></span>
            </button>
        </div>
    </div>
</form:form>
