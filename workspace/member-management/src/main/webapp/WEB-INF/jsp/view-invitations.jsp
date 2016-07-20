<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="workspace-member-management">
    <!-- Tabs -->
    <jsp:include page="tabs.jsp" />

    <!-- Invitations -->
    <jsp:include page="invitations.jsp" />
    
    <!-- Create invitations -->
    <jsp:include page="create-invitations.jsp" />
    
    <!-- Invitations history -->
    <jsp:include page="invitations-history.jsp" />
</div>
