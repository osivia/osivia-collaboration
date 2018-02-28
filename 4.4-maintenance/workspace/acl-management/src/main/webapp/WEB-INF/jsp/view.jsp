<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="close" var="closeUrl" />


<div class="workspace-acl-management">
    <!-- Title -->
    <h3>
        <i class="glyphicons glyphicons-shield"></i>
        <span><op:translate key="ACL_MANAGEMENT_TITLE" /></span>
    </h3>

    <!-- Member list -->
    <jsp:include page="list.jsp" />
    
    <!-- Add members -->
    <jsp:include page="add.jsp" />
    
    <div>
        <!-- Close -->
        <a href="${closeUrl}" class="btn btn-default no-ajax-link">
            <span><op:translate key="CLOSE" /></span>
        </a>
    </div>
</div>
