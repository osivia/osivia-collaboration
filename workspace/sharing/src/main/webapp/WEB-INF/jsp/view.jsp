<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="save" var="url" />


<form:form action="${url}" method="post" modelAttribute="form" role="form">

    <!-- Active indicator -->
    <div class="form-group">
        <div class="checkbox">
            <label>
                <form:checkbox path="active" />
                <span><op:translate key="ACTIVE_CHECKBOX" /></span>
            </label>
        </div>
    </div>
    
    
    <!-- Buttons -->
    <div>
        <!-- Save -->
        <button type="submit" class="btn btn-primary">
            <i class="glyphicons glyphicons-floppy-disk"></i>
            <span><op:translate key="SAVE"/></span>
        </button>
        
        <!-- Cancel -->
        <button type="button" class="btn btn-default" data-dismiss="modal">
            <span><op:translate key="CANCEL" /></span>
        </button>
    </div>

</form:form>
