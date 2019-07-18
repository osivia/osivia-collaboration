<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="save" var="saveActionURL" />


<form:form modelAttribute="configuration" action="${saveActionURL}" method="post" cssClass="form-horizontal" role="form">
    <!-- CMS path -->
    <div class="form-group">
        <form:label path="cmsPath" cssClass="control-label col-sm-3"><op:translate key="CALENDAR_CMS_PATH" /></form:label>
        <div class="col-sm-9">
            <form:input path="cmsPath" cssClass="form-control" />
        </div>
    </div>
    
    <!-- Default view -->
    <div class="form-group">
        <form:label path="periodTypeName" cssClass="control-label col-sm-3"><op:translate key="CALENDAR_DEFAULT_VIEW" /></form:label>
        <div class="col-sm-9">
            <form:select path="periodTypeName" cssClass="form-control">
                <form:option value=""></form:option>
                <c:forEach var="periodType" items="${periodTypes}">
                    <form:option value="${periodType.key}">${periodType.value}</form:option>
                </c:forEach>
            </form:select>
        </div>
    </div>
    
    <!-- Compact view -->
    <div class="form-group">
        <form:label path="compactView" cssClass="control-label col-sm-3"><op:translate key="CALENDAR_PLANNING" /></form:label>
        <div class="col-sm-9">
            <div class="checkbox">
                <label>
                    <form:checkbox path="compactView" />
                    <span><op:translate key="CALENDAR_COMPACT_VIEW" /></span>
                </label>
            </div>
            <span class="help-block"><op:translate key="CALENDAR_COMPACT_VIEW_HELP" /></span>
        </div>
    </div>
    
    <!-- Read only indicator -->
    <div class="form-group">
        <form:label path="readOnly" cssClass="control-label col-sm-3"><op:translate key="CALENDAR_READ_ONLY" /></form:label>
        <div class="col-sm-9">
            <div class="checkbox">
                <label>
                    <form:checkbox path="readOnly" />
                    <span><op:translate key="CALENDAR_READ_ONLY_CHECKBOX" /></span>
                </label>
            </div>
            <span class="help-block"><op:translate key="CALENDAR_READ_ONLY_HELP" /></span>
        </div>
    </div>
    
    <!-- Integration indicator -->
    <div class="form-group">
        <form:label path="integration" cssClass="control-label col-sm-3"><op:translate key="CALENDAR_INTEGRATION" /></form:label>
        <div class="col-sm-9">
            <div class="checkbox">
                <label>
                    <form:checkbox path="integration" />
                    <span><op:translate key="CALENDAR_INTEGRATION_CHECKBOX" /></span>
                </label>
            </div>
        </div>
    </div>
    
    <!-- Buttons -->
    <div class="form-group">
        <div class="col-sm-offset-3 col-sm-9">
            <button type="submit" class="btn btn-primary">
                <i class="glyphicons glyphicons-floppy-disk"></i>
                <span><op:translate key="SAVE" /></span>
            </button>
            <button type="button" class="btn btn-secondary" onclick="closeFancybox()"><op:translate key="CANCEL" /></button>
        </div>
    </div>
</form:form>
