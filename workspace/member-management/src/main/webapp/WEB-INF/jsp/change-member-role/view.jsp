<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />


<div class="workspace-member-management">
    <!-- Tabs -->
    <%@ include file="../commons/tabs.jspf" %>
    
    <div class="panel panel-default">
        <div class="panel-body">
            <!-- Selected members -->
            <%@ include file="selected-members.jspf" %>
            
            <hr>
            
            <!-- Form -->
            <%@ include file="form.jspf" %>
        </div>
    </div>
</div>
