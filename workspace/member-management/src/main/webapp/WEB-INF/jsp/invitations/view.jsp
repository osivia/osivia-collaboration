<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="workspace-member-management">
    <!-- Tabs -->
    <jsp:include page="../commons/tabs.jsp" />
    
    <!-- Invitations creation -->
    <jsp:include page="creation.jsp" />
    
    <!-- Invitations list -->
    <jsp:include page="list.jsp" />
    
    <!-- Purge -->
    <jsp:include page="purge.jsp" />
</div>
