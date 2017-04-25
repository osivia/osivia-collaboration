<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="save" var="saveUrl" />

<portlet:actionURL name="delete" var="deleteUrl" />


<c:set var="namespace"><portlet:namespace /></c:set>


<c:choose>
    <c:when test="${options.type eq 'Room'}"><c:set var="fragment"><op:translate key="WORKSPACE_EDITION_ROOM_FRAGMENT" /></c:set></c:when>
    <c:otherwise><c:set var="fragment"><op:translate key="WORKSPACE_EDITION_WORKSPACE_FRAGMENT" /></c:set></c:otherwise>
</c:choose>


<div class="workspace-edition">
    <form:form action="${saveUrl}" method="post" modelAttribute="editionForm" cssClass="form-horizontal" role="form">
        <!-- Task creation form -->
        <form:hidden path="taskCreationForm.title" />
        <form:hidden path="taskCreationForm.description" />
        <form:hidden path="taskCreationForm.type" />
        <form:hidden path="taskCreationForm.valid" />

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
                                <ul class="list-sortable workspace-edition-sortable active-tasks">
                                    <c:forEach var="task" items="${editionForm.tasks}" varStatus="varStatus">
                                        <c:if test="${task.active}">
                                            <li>
                                                <form:hidden path="tasks[${varStatus.index}].active" />
                                                <form:hidden path="tasks[${varStatus.index}].order" />
                                            
                                                <i class="${task.icon}"></i>
                                                <span>${task.displayName}</span>
                                            </li>
                                        </c:if>
                                    </c:forEach>
                                </ul>
                            </div>
                            <div class="panel-footer">
                                <!-- Create -->
                                <c:set var="title"><op:translate key="WORKSPACE_CREATE_TASK_MODAL_TITLE" /></c:set>
                                <button type="button" name="open-task-creation" class="btn btn-default" data-load-url="${taskCreationUrl}" data-title="${title}">
                                    <i class="glyphicons glyphicons-plus"></i>
                                    <span><op:translate key="WORKSPACE_CREATE_TASK" /></span>
                                </button>
                                <input id="${namespace}-create-task" type="submit" name="create" class="hidden">
                            </div>
                        </div>
                    </div>
                    
                    <div class="col-sm-6">
                        <div class="panel panel-default">
                            <div class="panel-heading"><op:translate key="WORKSPACE_IDLE_TASKS" /></div>
                            <div class="panel-body">
                                <p class="text-muted"><op:translate key="WORKSPACE_IDLE_TASKS_HELP" /></p>
                                <ul class="list-sortable workspace-edition-sortable idle-tasks">
                                    <c:forEach var="task" items="${editionForm.tasks}" varStatus="varStatus">
                                        <c:if test="${not task.active}">
                                            <li>
                                                <form:hidden path="tasks[${varStatus.index}].active" />
                                                <form:hidden path="tasks[${varStatus.index}].order" />
                                            
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
</div>
