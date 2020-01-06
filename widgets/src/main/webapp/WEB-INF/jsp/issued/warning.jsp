<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />


<p class="text-center">
    <span><op:translate key="DOCUMENT_NOT_PUBLISHED_WARNING" /></span>
</p>


<div class="text-center">
    <!-- Close -->
    <button type="button" class="btn btn-secondary" data-dismiss="modal">
        <span><op:translate key="CLOSE" /></span>
    </button>
</div>
