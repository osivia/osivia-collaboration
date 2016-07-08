<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="workspace-member-management">
    <!-- Tabs -->
    <jsp:include page="tabs.jsp" />
    
    <!-- Members -->
    <jsp:include page="members.jsp" />
</div>
