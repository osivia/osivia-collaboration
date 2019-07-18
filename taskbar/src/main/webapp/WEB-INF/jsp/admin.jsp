<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="save" var="saveUrl" />


<form:form modelAttribute="settings" action="${saveUrl}" method="post" cssClass="form-horizontal" role="form">
    <!-- Order -->
    <input type="hidden" name="order" value="${order}">
    
    <!-- Taskbar view -->
    <div class="form-group">
        <form:label path="view" cssClass="control-label col-sm-2"><op:translate key="TASKBAR_VIEW" /></form:label>
        <div class="col-sm-10">
            <form:select path="view" cssClass="form-control">
                <c:forEach var="view" items="${views}">
                    <c:set var="label"><op:translate key="${view.key}" /></c:set>
                    <form:option value="${view}" label="${label}"></form:option>
                </c:forEach>
            </form:select>
        </div>
    </div>
    
    <!-- Tasks -->
    <div class="form-group">
        <label class="control-label col-sm-2"><op:translate key="TASKS" /></label>
        <div class="col-sm-10">
            <div class="row">
                <!-- Ordered tasks -->
                <div class="col-xs-6">
                    <div class="panel panel-primary">
                        <div class="panel-heading"><op:translate key="ORDERED_TASKS" /></div>
                        
                        <div class="panel-body">
                            <p class="text-muted"><op:translate key="ORDERED_TASKS_HELP" /></p>
                            
                            <ul class="list-sortable taskbar-ordered-tasks taskbar-sortable">
                                <c:forEach var="item" items="${orderedItems}">
                                    <li data-id="${item.id}">
                                        <i class="${item.icon}"></i>
                                        <span><op:translate key="${item.key}" /></span>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
                
                <!-- Available tasks -->
                <div class="col-xs-6">
                    <div class="panel panel-default">
                        <div class="panel-heading"><op:translate key="AVAILABLE_TASKS" /></div>
                        
                        <div class="panel-body">
                            <p class="text-muted"><op:translate key="AVAILABLE_TASKS_HELP" /></p>
                            
                            <ul class="list-sortable taskbar-sortable">
                                <c:forEach var="item" items="${availableItems}">
                                    <li data-id="${item.id}">
                                        <i class="${item.icon}"></i>
                                        <span><op:translate key="${item.key}" /></span>
                                    </li>
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
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-primary">
                <i class="glyphicons glyphicons-floppy-disk"></i>
                <span><op:translate key="SAVE" /></span>
            </button>
            
            <button type="button" class="btn btn-secondary" onclick="closeFancybox()">
                <span><op:translate key="CANCEL" /></span>
            </button>
        </div>
    </div>
</form:form>
