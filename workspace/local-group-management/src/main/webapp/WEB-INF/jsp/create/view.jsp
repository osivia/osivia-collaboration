<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="cancel" var="cancelUrl" copyCurrentRenderParameters="true" />


<div class="workspace-local-group-management">
    <div class="panel panel-default">
        <div class="panel-body">
            <div>
                <a href="${cancelUrl}" class="btn btn-secondary btn-sm">
                    <i class="glyphicons glyphicons-arrow-left"></i>
                    <span><op:translate key="BACK_TO_SUMMARY" /></span>
                </a>
            </div>
        
            <hr>
        
            <div class="portlet-filler">
                <%@ include file="form.jspf" %>
            </div>
        </div>
    </div>
</div>
