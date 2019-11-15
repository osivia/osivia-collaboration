<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
 
<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="save" var="url" />


<form:form action="${url}" method="post" modelAttribute="settings" cssClass="form-horizontal">
    <!-- Label -->
    <div class="form-group">
        <form:label path="taskPath" cssClass="col-sm-3 control-label"><op:translate key="TASKPATH_LABEL" /></form:label>
        <div class="col-sm-9">
            <form:input path="taskPath" cssClass="form-control" />
        </div>
    </div>
    
    <!-- Buttons -->
    <div class="row">
        <div class="col-sm-offset-3 col-sm-9">
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
