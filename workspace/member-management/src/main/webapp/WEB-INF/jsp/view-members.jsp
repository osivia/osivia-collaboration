<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="workspace-member-management">
    <!-- Tabs -->
    <jsp:include page="tabs.jsp" />
    
    <!-- Title -->
    <h3>
        <span><op:translate key="TAB_MEMBERS_TITLE" /></span>
    </h3>
    
    <!-- Help -->
    <p class="text-muted">
        <span><op:translate key="TAB_MEMBERS_HELP" /></span>
    </p>
    
    <!-- Members -->
    <jsp:include page="members.jsp" />
</div>
