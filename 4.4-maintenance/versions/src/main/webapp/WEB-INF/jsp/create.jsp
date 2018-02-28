<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>

<portlet:actionURL name="create" var="createUrl" />

<div class="well">
    <form:form action="${createUrl}" method="post" modelAttribute="creationForm">
        <fieldset>
            <legend><op:translate key="CREATE_VERSION" /></legend>

            <!-- Comment -->
                <c:set var="placeholder"><op:translate key="COMMENT_VERSION" /></c:set>
                <div class="form-group">
                    <form:label path="comment" cssClass="sr-only">${placeholder}</form:label>
                    <form:input path="comment" cssClass="form-control" placeholder="${placeholder}" />
                </div>
            
            <!-- Buttons -->
                <div>
                    <!-- Save -->
                    <button type="submit" name="create" class="btn btn-primary">
                        <i class="glyphicons glyphicons-floppy-disk"></i>
                        <span><op:translate key="CREATE" /></span>
                    </button>
                    
                </div>
        </fieldset>
    </form:form>
</div>
