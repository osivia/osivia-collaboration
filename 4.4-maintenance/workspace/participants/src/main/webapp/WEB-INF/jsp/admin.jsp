<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="save" var="url" />


<form:form modelAttribute="configuration" action="${url}" method="post" cssClass="form-horizontal" role="form">
    <!-- View -->
    <div class="form-group">
        <form:label path="view" cssClass="control-label col-sm-2"><op:translate key="WORKSPACE_PARTICIPANTS_VIEW" /></form:label>
        <div class="col-sm-10">
            <form:select path="view" cssClass="form-control">
                <c:forEach var="view" items="${views}">
                    <c:set var="label"><op:translate key="${view.key}" /></c:set>
                    <form:option value="${view}" label="${label}"></form:option>
                </c:forEach>
            </form:select>
        </div>
    </div>
    
    <!-- Display -->
    <div class="form-group">
        <form:label path="display" cssClass="control-label col-sm-2"><op:translate key="WORKSPACE_PARTICIPANTS_DISPLAY" /></form:label>
        <div class="col-sm-10">
            <c:forEach var="group" items="${configuration.display}">
                <div class="checkbox">
                    <label>
                        <form:checkbox path="display['${group.key}']" />
                        <span><op:translate key="WORKSPACE_PARTICIPANTS_DISPLAY_${group.key.key}" /></span>
                    </label>
                </div>
            </c:forEach>
        </div>
    </div>
    
    <!-- Max -->
    <div class="form-group">
        <form:label path="max" cssClass="control-label col-sm-2"><op:translate key="WORKSPACE_PARTICIPANTS_MAX" /></form:label>
        <div class="col-sm-10">
            <form:input path="max" type="number" cssClass="form-control" />
        </div>
    </div>
    
    <!-- Buttons -->
    <div class="form-group">
        <div class="col-sm-offset-2 col-sm-10">
            <button type="submit" class="btn btn-primary">
                <i class="glyphicons glyphicons-floppy-disk"></i>
                <span><op:translate key="SAVE" /></span>
            </button>
            
            <button type="button" class="btn btn-default" onclick="closeFancybox()">
                <span><op:translate key="CANCEL" /></span>
            </button>
        </div>
    </div>
</form:form>
