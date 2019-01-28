<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="save" var="url" />


<form:form action="${url}" method="post" modelAttribute="form" role="form">
    <fieldset>
        <legend>
            <span><op:translate key="SHARING_LEGEND" /></span>
        </legend>
    
        <div class="clearfix">
            <div class="pull-left">
                <p class="form-control-static"><op:translate key="DISABLED_SHARING_HELP_MESSAGE" /></p>
            </div>
            
            <div class="pull-right">
                <button type="submit" name="enable" class="btn btn-primary">
                    <span><op:translate key="ENABLE_SHARING" /></span>
                </button>
            </div>
        </div>
    </fieldset>
</form:form>


<%@ include file="close.jspf" %>
