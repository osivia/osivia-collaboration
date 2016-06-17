<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="save" var="saveUrl" />

<portlet:actionURL name="create" var="createUrl" />

<portlet:actionURL name="delete" var="deleteUrl" />


<c:set var="namespace"><portlet:namespace /></c:set>


<c:choose>
    <c:when test="${form.type eq 'Room'}"><c:set var="fragment"><op:translate key="WORKSPACE_EDITION_ROOM_FRAGMENT" /></c:set></c:when>
    <c:otherwise><c:set var="fragment"><op:translate key="WORKSPACE_EDITION_WORKSPACE_FRAGMENT" /></c:set></c:otherwise>
</c:choose>


<form:form action="${saveUrl}" method="post" modelAttribute="form" cssClass="form-horizontal" role="form">
    <!-- Title -->
    <c:set var="placeholder"><op:translate key="WORKSPACE_TITLE_PLACEHOLDER" args="${fragment}" /></c:set>
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
    <c:set var="placeholder"><op:translate key="WORKSPACE_DESCRIPTION_PLACEHOLDER" args="${fragment}" /></c:set>
    <div class="form-group">
        <form:label path="description" cssClass="col-sm-3 col-lg-2 control-label" ><op:translate key="WORKSPACE_DESCRIPTION" /></form:label>
        <div class="col-sm-9 col-lg-10">
            <form:textarea path="description" cssClass="form-control" placeholder="${placeholder}" />
        </div>
    </div>
    
    <!-- Tasks -->
    <div class="form-group">
        <input type="submit" name="sort" class="hidden">
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
                        <div class="panel-footer">
                            <!-- Create -->
                            <button type="button" class="btn btn-default" data-toggle="modal" data-target="#${namespace}-create-modal">
                                <i class="glyphicons glyphicons-plus"></i>
                                <span><op:translate key="WORKSPACE_CREATE_TASK" /></span>
                            </button>
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
            <hr>

            <!-- Save -->
            <button type="submit" name="save" class="btn btn-primary">
                <i class="glyphicons glyphicons-floppy-disk"></i>
                <span><op:translate key="SAVE" /></span>
            </button>
            
            <!-- Cancel -->
            <button type="submit" name="cancel" class="btn btn-default">
                <span><op:translate key="CANCEL" /></span>
            </button>
            
            <div class="pull-right">
                <!-- Delete -->
                <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#${namespace}-delete-modal">
                    <i class="glyphicons glyphicons-bin"></i>
                    <span><op:translate key="DELETE" /></span>
                </button>
            </div>
        </div>
    </div>
</form:form>


<!-- Create task modal -->
<div id="${namespace}-create-modal" class="modal ${createTaskForm.hasErrors ? 'opened' : ''}" role="dialog">
    <div class="modal-dialog">
        <form:form action="${createUrl}" method="post" modelAttribute="createTaskForm" cssClass="form-horizontal" role="form">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <i class="glyphicons glyphicons-remove"></i>
                        <span class="sr-only"><op:translate key="CLOSE" /></span>
                    </button>
                    
                    <h4 class="modal-title">
                        <i class="glyphicons glyphicons-plus"></i>
                        <span><op:translate key="WORKSPACE_CREATE_TASK_MODAL_TITLE" /></span>
                    </h4>
                </div>
                
                <div class="modal-body">
                    <!-- Title -->
                    <c:set var="placeholder"><op:translate key="WORKSPACE_CREATE_TASK_TITLE_PLACEHOLDER" /></c:set>
                    <spring:bind path="createdTitle">
                        <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
                            <form:label path="createdTitle" cssClass="col-sm-3 control-label"><op:translate key="WORKSPACE_CREATE_TASK_TITLE" /></form:label>
                            <div class="col-sm-9">
                                <form:input path="createdTitle" cssClass="form-control" placeholder="${placeholder}" />
                                <c:if test="${status.error}">
                                    <span class="form-control-feedback">
                                        <i class="glyphicons glyphicons-remove"></i>
                                    </span>
                                </c:if>
                                <form:errors path="createdTitle" cssClass="help-block" />
                            </div>
                        </div>
                    </spring:bind>
                    
                    <!-- Description -->
                    <c:set var="placeholder"><op:translate key="WORKSPACE_CREATE_TASK_DESCRIPTION_PLACEHOLDER" /></c:set>
                    <div class="form-group">
                        <form:label path="createdDescription" cssClass="col-sm-3 control-label"><op:translate key="WORKSPACE_CREATE_TASK_DESCRIPTION" /></form:label>
                        <div class="col-sm-9">
                            <form:textarea path="createdDescription" cssClass="form-control" placeholder="${placeholder}" />
                        </div>
                    </div>
                    
                    <!-- Type -->
                    <spring:bind path="createdType">
                        <div class="form-group required ${status.error ? 'has-error' : ''}">
                            <form:label path="createdType" cssClass="col-sm-3 control-label"><op:translate key="WORKSPACE_CREATE_TASK_TYPE" /></form:label>
                            <div class="col-sm-9">
                                <c:forEach var="type" items="${createTaskForm.types}">
                                    <div class="radio">
                                        <label>
                                            <form:radiobutton path="createdType" value="${type.key.name}" />
                                            <i class="${type.key.glyph}"></i>
                                            <span>${type.value}</span>
                                        </label>
                                    </div>
                                </c:forEach>
                                <form:errors path="createdType" cssClass="help-block" />
                            </div>
                        </div>
                    </spring:bind>
                </div>
                
                <div class="modal-footer">
                    <div class="row">
                        <div class="col-sm-offset-3 col-sm-9">
                            <button type="submit" class="btn btn-primary" data-dismiss="modal">
                                <i class="glyphicons glyphicons-plus"></i>
                                <span><op:translate key="CREATE" /></span>
                            </button>
                            
                            <button type="button" class="btn btn-default" data-dismiss="modal">
                                <span><op:translate key="CANCEL" /></span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </form:form>
    </div>
</div>


<!-- Delete confirmation modal -->
<div id="${namespace}-delete-modal" class="modal fade" role="dialog">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <i class="glyphicons glyphicons-remove"></i>
                    <span class="sr-only"><op:translate key="CLOSE" /></span>
                </button>
                
                <h4 class="modal-title"><op:translate key="WORKSPACE_DELETE_MODAL_TITLE" args="${fragment}" /></h4>
            </div>
            
            <div class="modal-body">
                <p><op:translate key="WORKSPACE_DELETE_MODAL_MESSAGE" args="${fragment}" /></p>
            </div>
            
            <div class="modal-footer">
                <a href="${deleteUrl}" class="btn btn-danger" data-dismiss="modal">
                    <i class="glyphicons glyphicons-bin"></i>
                    <span><op:translate key="DELETE" /></span>
                </a>
                
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    <span><op:translate key="CANCEL" /></span>
                </button>
            </div>
        </div>
    </div>
</div>
