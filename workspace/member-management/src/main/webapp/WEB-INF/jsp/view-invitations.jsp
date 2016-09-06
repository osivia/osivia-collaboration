<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="workspace-member-management">
    <!-- Tabs -->
    <jsp:include page="tabs.jsp" />
    
    <!-- Title -->
    <h3>
        <span><op:translate key="TAB_INVITATIONS_TITLE" /></span>
    </h3>
    
    <!-- Help -->
    <p class="text-muted">
        <span><op:translate key="TAB_INVITATIONS_HELP" /></span>
    </p>

    <!-- Invitations -->
    <jsp:include page="invitations.jsp" />
    
    <!-- Create invitations -->
    <jsp:include page="create-invitations.jsp" />
</div>
