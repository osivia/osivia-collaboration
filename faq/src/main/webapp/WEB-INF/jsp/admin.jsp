<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>%>

<%@ page isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="save" var="saveAdminURL" />


<form action="${saveAdminURL}" method="post" class="form-horizontal" role="form">
    <div class="form-group">
        <label for="path" class="control-label col-sm-3"><op:translate key="PATH" /></label>
        <div class="col-sm-9">
            <input id="path" type="text" name="path" value="${path}" class="form-control">
        </div>
    </div>
    
    <div class="form-group">
        <div class="col-sm-offset-3 col-sm-9">
            <button type="submit" class="btn btn-primary">
                <i class="glyphicons glyphicons-floppy-disk"></i>
                <span><op:translate key="SAVE" /></span>
            </button>
            
            <button type="button" class="btn btn-secondary" onclick="closeFancybox()">
                <span><op:translate key="CANCEL" /></span>
            </button>
        </div>
    </div>
</form>
