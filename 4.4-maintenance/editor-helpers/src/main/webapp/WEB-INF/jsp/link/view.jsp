<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<portlet:actionURL name="submit" var="submitUrl" />


<div class="editor-link" data-close-modal="${form.done}">
    <form:form action="${submitUrl}" method="post" modelAttribute="form" cssClass="form-horizontal" role="form">
        <!-- Done indicator -->
        <form:hidden path="done" />
    
        <!-- Link value -->
        <div class="form-group">
            <form:label path="link" cssClass="col-sm-3 control-label"><op:translate key="EDITOR_LINK_VALUE" /></form:label>
            <div class="col-sm-9">
                <form:input path="link" cssClass="form-control" />
            </div>
        </div>
        
        <!-- Buttons -->
        <div class="form-group">
            <div class="col-sm-offset-3 col-sm-9">
                <!-- Submit -->
                <button type="submit" class="btn btn-primary">
                    <span><op:translate key="OK" /></span>
                </button>
                
                <!-- Cancel -->
                <button type="button" class="btn btn-default" data-dismiss="modal">
                    <span><op:translate key="CANCEL" /></span>
                </button>
            </div>
        </div>
    
    </form:form>
</div>
