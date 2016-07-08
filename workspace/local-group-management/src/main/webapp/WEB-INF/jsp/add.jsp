<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:actionURL name="add" var="addUrl" />


<div class="well">
    <form:form action="${addUrl}" method="post" modelAttribute="addForm" role="form">
        <fieldset>
            <legend><op:translate key="ADD_LOCAL_GROUP_LEGEND" /></legend>

            <!-- Display name -->
            <c:set var="placeholder"><op:translate key="LOCAL_GROUP_DISPLAY_NAME_PLACEHOLDER" /></c:set>
            <spring:bind path="displayName">
                <div class="form-group ${status.error ? 'has-error has-feedback' : ''}">
                    <form:label path="displayName" cssClass="sr-only"><op:translate key="LOCAL_GROUP_DISPLAY_NAME" /></form:label>
                    <form:input path="displayName" cssClass="form-control" placeholder="${placeholder}" />
                    <c:if test="${status.error}">
                        <span class="form-control-feedback">
                            <i class="glyphicons glyphicons-remove"></i>
                        </span>
                    </c:if>
                    <form:errors path="displayName" cssClass="help-block" />
                </div>
            </spring:bind>
            
            <!-- Buttons -->
            <spring:bind path="*">
                <div class="collapse ${status.error ? 'in' : ''}">
                    <!-- Save -->
                    <button type="submit" name="save" class="btn btn-primary">
                        <i class="glyphicons glyphicons-floppy-disk"></i>
                        <span><op:translate key="SAVE" /></span>
                    </button>
                    
                    <!-- Cancel -->
                    <button type="submit" name="cancel" class="btn btn-default">
                        <span><op:translate key="CANCEL" /></span>
                    </button>
                </div>
            </spring:bind>
        </fieldset>
    </form:form>
</div>