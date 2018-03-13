<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="save" var="saveUrl" />
<portlet:actionURL name="delete" var="deleteUrl" />


<jsp:useBean id="currentDate" class="java.util.Date" />


<c:set var="namespace"><portlet:namespace /></c:set>


<c:choose>
    <c:when test="${editionForm.document.type eq 'Room'}"><c:set var="fragment"><op:translate key="WORKSPACE_EDITION_ROOM_FRAGMENT" /></c:set></c:when>
    <c:otherwise><c:set var="fragment"><op:translate key="WORKSPACE_EDITION_WORKSPACE_FRAGMENT" /></c:set></c:otherwise>
</c:choose>


<div class="workspace-edition">
    <form:form id="${namespace}-workspace-edition-form" action="${saveUrl}" method="post" enctype="multipart/form-data" modelAttribute="editionForm" cssClass="form-horizontal" role="form">
        <div class="row">
            <div class="col-lg-8">
                <!-- Title -->
                <c:set var="placeholder"><op:translate key="WORKSPACE_TITLE_PLACEHOLDER" args="${fragment}" /></c:set>
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
                <c:set var="placeholder"><op:translate key="WORKSPACE_DESCRIPTION_PLACEHOLDER" args="${fragment}" /></c:set>
                <spring:bind path="description">
                    <div class="form-group ${editionForm.root ? 'required' : ''} ${status.error ? 'has-error' : ''}">
                        <form:label path="description" cssClass="col-sm-3 control-label" ><op:translate key="WORKSPACE_DESCRIPTION" /></form:label>
                        <div class="col-sm-9">
                            <form:textarea path="description" cssClass="form-control" rows="3" placeholder="${placeholder}" />
                            <form:errors path="description" cssClass="help-block" />
                        </div>
                    </div>
                </spring:bind>
                
                <!-- Template -->
                <c:if test="${editionForm.root and not empty editionForm.templates}">
                    <div class="form-group">
                        <form:label path="template" cssClass="col-sm-3 control-label"><op:translate key="WORKSPACE_TEMPLATE" /></form:label>
                        <div class="col-sm-9">
                            <form:select path="template" cssClass="form-control">
                                <form:option value=""><op:translate key="WORKSPACE_DEFAULT_TEMPLATE"/></form:option>
                                <c:forEach var="template" items="${editionForm.templates}">
                                    <form:option value="${template.key}">${template.value}</form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                </c:if>
                
                <!-- Workspace type -->
                <c:if test="${editionForm.root}">
                    <spring:bind path="workspaceType">
                        <div class="form-group ${editionForm.admin or not editionForm.initialWorkspaceType.portalAdministratorRestriction ? 'required' : ''} ${status.error ? 'has-error' : ''}">
                            <form:label path="workspaceType" cssClass="col-sm-3 control-label"><op:translate key="WORKSPACE_TYPE" /></form:label>
                            <div class="col-sm-9">
                                <c:choose>
                                    <c:when test="${editionForm.admin or not editionForm.initialWorkspaceType.portalAdministratorRestriction}">
                                        <!-- Editable workspace type -->
                                        <c:forEach var="type" items="${editionForm.workspaceTypes}">
                                            <div class="radio">
                                                <label>
                                                    <form:radiobutton path="workspaceType" value="${type.id}" />
                                                    <span class="label label-${type.color}">
                                                        <i class="${type.icon}"></i>
                                                        <span><op:translate key="${type.key}" /></span>
                                                    </span>
                                                </label>
                                                <p class="text-muted">
                                                    <span><op:translate key="${type.key}_HELP" /></span>
                                                </p>
                                            </div>
                                        </c:forEach>
                                        <form:errors path="workspaceType" cssClass="help-block" />
                                    </c:when>
                                    
                                    <c:otherwise>
                                        <!-- Fixed workspace type -->
                                        <p class="form-control-static">
                                            <span class="label label-${editionForm.initialWorkspaceType.color}">
                                                <i class="${editionForm.initialWorkspaceType.icon}"></i>
                                                <span><op:translate key="${editionForm.initialWorkspaceType.key}" /></span>
                                            </span>
                                        </p>
                                        <p class="text-muted">
                                            <span><op:translate key="${editionForm.initialWorkspaceType.key}_HELP" /></span>
                                        </p>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </spring:bind>
                    
                    <!-- Invitations -->
                    <div class="form-group">
                        <form:label path="workspaceType" cssClass="col-sm-3 control-label"><op:translate key="WORKSPACE_INVITATIONS" /></form:label>
                        <div class="col-sm-9">
                            <div class="checkbox">
                                <label>
                                    <form:checkbox path="allowedInvitationRequests" />
                                    <span><op:translate key="WORKSPACE_ALLOWED_INVITATION_REQUESTS_ACTION" /></span>
                                </label>
                                <p class="text-muted">
                                    <span><op:translate key="WORKSPACE_ALLOWED_INVITATION_REQUESTS_HELP" /></span>
                                </p>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
            
            <div class="col-lg-4">
                <!-- Visual -->
                <div class="form-group">
                    <form:label path="visual.upload" cssClass="col-sm-3 col-lg-5 control-label"><op:translate key="WORKSPACE_VISUAL" /></form:label>
                    <div class="col-sm-9 col-lg-7">
                        <!-- Preview -->
                        <c:choose>
                            <c:when test="${editionForm.visual.updated}">
                                <!-- Preview -->
                                <portlet:resourceURL id="visualPreview" var="previewUrl">
                                    <portlet:param name="ts" value="${currentDate.time}" />
                                </portlet:resourceURL>
                                <p class="form-control-static">
                                    <img src="${previewUrl}" alt="" class="img-responsive">
                                </p>
                            </c:when>
                            
                            <c:when test="${editionForm.visual.deleted}">
                                <!-- Deleted visual -->
                                <p class="form-control-static text-muted">
                                    <span><op:translate key="WORKSPACE_DELETED_VISUAL" /></span>
                                </p>
                            </c:when>
                        
                            <c:when test="${empty editionForm.visual.url}">
                                <!-- No visual -->
                                <p class="form-control-static text-muted">
                                    <span><op:translate key="WORKSPACE_NO_VISUAL" /></span>
                                </p>
                            </c:when>
                            
                            <c:otherwise>
                                <!-- Visual -->
                                <p class="form-control-static">
                                    <img src="${editionForm.visual.url}" alt="" class="img-responsive">
                                </p>
                            </c:otherwise>
                        </c:choose>
                    
                        <div>
                            <!-- Upload -->
                            <label class="btn btn-sm btn-default btn-file">
                                <i class="halflings halflings-folder-open"></i>
                                <span><op:translate key="WORKSPACE_IMAGE_UPLOAD" /></span>
                                <form:input type="file" path="visual.upload" />
                            </label>
                            <input type="submit" name="upload-visual" class="hidden">
                            
                            <!-- Delete -->
                            <button type="submit" name="delete-visual" class="btn btn-sm btn-default">
                                <i class="halflings halflings-trash"></i>
                                <span class="sr-only"><op:translate key="WORKSPACE_IMAGE_DELETE" /></span>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
            
        <!-- Tasks -->
        <spring:bind path="tasks">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <input type="submit" name="sort" class="hidden">
                <label class="col-sm-3 col-lg-2 control-label"><op:translate key="WORKSPACE_TASKS" /></label>
                <div class="col-sm-9 col-lg-10">
                    <div class="row">
                        <div class="col-sm-6">
                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><op:translate key="WORKSPACE_ACTIVE_TASKS" /></h3>
                                </div>
                                
                                <div class="panel-body">
                                    <p class="text-muted"><op:translate key="WORKSPACE_ACTIVE_TASKS_HELP" /></p>
                                    <ul class="list-sortable workspace-edition-sortable active-tasks">
                                        <c:forEach var="task" items="${editionForm.tasks}" varStatus="varStatus">
                                            <c:if test="${task.active}">
                                                <li>
                                                    <div class="media ${task.custom ? 'text-info' : ''}">
                                                        <form:hidden path="tasks[${varStatus.index}].active" />
                                                        <form:hidden path="tasks[${varStatus.index}].order" />
                                                        <form:hidden path="tasks[${varStatus.index}].updated" />
                                                        <form:hidden path="tasks[${varStatus.index}].sorted" />
                                                    
                                                        <div class="media-left">
                                                            <i class="${task.icon}"></i>
                                                        </div>
                                                        
                                                        <div class="media-body">
                                                            <span>${task.displayName}</span>
                                                            
                                                            <c:if test="${task.custom}">
                                                                <small><op:translate key="WORKSPACE_CUSTOM_TASK_INDICATOR" /></small>
                                                            </c:if>
                                                        </div>
                                                        
                                                        <div class="media-right">
                                                            <portlet:actionURL name="hide" var="url">
                                                                <portlet:param name="index" value="${varStatus.index}" />
                                                            </portlet:actionURL>
                                                            
                                                            <a href="${url}" class="small">
                                                                <span><op:translate key="WORKSPACE_HIDE_TASK" /></span>
                                                            </a>
                                                        </div>
                                                    </div>
                                                </li>
                                            </c:if>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                        </div>
                        
                        <div class="col-sm-6">
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    <h3 class="panel-title"><op:translate key="WORKSPACE_IDLE_TASKS" /></h3>
                                </div>
                                
                                <div class="panel-body">
                                    <p class="text-muted"><op:translate key="WORKSPACE_IDLE_TASKS_HELP" /></p>
                                    <ul class="list-sortable workspace-edition-sortable idle-tasks">
                                        <c:forEach var="task" items="${editionForm.tasks}" varStatus="varStatus">
                                            <c:if test="${not task.active}">
                                                <li>
                                                    <div class="media ${task.custom ? 'text-info' : ''}">
                                                        <form:hidden path="tasks[${varStatus.index}].active" />
                                                        <form:hidden path="tasks[${varStatus.index}].order" />
                                                        <form:hidden path="tasks[${varStatus.index}].updated" />
                                                        <form:hidden path="tasks[${varStatus.index}].sorted" />
                                                        
                                                        <div class="media-left">
                                                            <i class="${task.icon}"></i>
                                                        </div>
                                                        
                                                        <div class="media-body">
                                                            <span>${task.displayName}</span>
                                                            
                                                            <c:if test="${task.custom}">
                                                                <small><op:translate key="WORKSPACE_CUSTOM_TASK_INDICATOR" /></small>
                                                            </c:if>
                                                        </div>
                                                        
                                                        <div class="media-right">
                                                            <portlet:actionURL name="show" var="url">
                                                                <portlet:param name="index" value="${varStatus.index}" />
                                                            </portlet:actionURL>
                                                            
                                                            <a href="${url}" class="small">
                                                                <span><op:translate key="WORKSPACE_SHOW_TASK" /></span>
                                                            </a>
                                                        </div>
                                                    </div>
                                                </li>
                                            </c:if>
                                        </c:forEach>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <form:errors path="tasks" cssClass="help-block" />
                </div>
            </div>
        </spring:bind>
        
        <!-- Editorial -->
        <spring:bind path="editorial.displayed">
            <div class="form-group ${status.error ? 'has-error' : ''}" data-created="${not empty editionForm.editorial.document}">
                <input type="submit" name="create-editorial" class="hidden">
                <form:label path="editorial.displayed" cssClass="col-sm-3 col-lg-2 control-label"><op:translate key="WORKSPACE_EDITORIAL" /></form:label>
                <div class="col-sm-9 col-lg-10">
                    <div class="checkbox">
                        <label>
                            <form:checkbox path="editorial.displayed" />
                            <span><op:translate key="WORKSPACE_EDITORIAL_DISPLAY_CHECKBOX" /></span>
                        </label>
                    </div>
                    
                    <c:if test="${not empty editionForm.editorial.document}">
                        <p class="form-control-static">
                            <c:set var="editionUrl"><ttc:documentLink document="${editionForm.editorial.document}" /></c:set>
                                
                            <a href="${editionUrl}" class="btn btn-default no-ajax-link">
                                <span><op:translate key="WORKSPACE_EDITORIAL_EDIT" /></span>
                            </a>
                        
                            <a href="#${namespace}-editorial-preview" data-toggle="collapse" class="btn btn-default no-ajax-link">
                                <span><op:translate key="WORKSPACE_EDITORIAL_PREVIEW_DISPLAY" /></span>
                            </a>
                        </p>
                        
                        <div id="${namespace}-editorial-preview" class="panel panel-info collapse">
                            <div class="panel-body">
                                <div><ttc:transform document="${editionForm.editorial.document}" property="note:note" /></div>
                            </div>
                        </div>
                    </c:if>
                    
                    <form:errors path="editorial.displayed" cssClass="help-block" />
                </div>
            </div>
        </spring:bind>

        <!-- Buttons -->
        <div>
            <div class="row">
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
                    
                    <div class="pull-right">
                        <!-- Delete -->
                        <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#${namespace}-delete-modal">
                            <i class="glyphicons glyphicons-bin"></i>
                            <span><op:translate key="DELETE" /></span>
                        </button>
                    </div>
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
                    <p>
                        <span><op:translate key="WORKSPACE_DELETE_MODAL_MESSAGE" args="${fragment}" /></span>
                    </p>
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
