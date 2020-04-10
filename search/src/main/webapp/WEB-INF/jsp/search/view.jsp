<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
 
<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="search" var="url" />


<form:form action="${url}" modelAttribute="form" cssClass="form-inline" role="search">
    <div class="form-group">
        <form:label path="query" cssClass="sr-only"><op:translate key="SEARCH_LABEL" /></form:label>
        <div class="input-group">
            <form:input path="query" cssClass="form-control" />
            <div class="input-group-btn">
                <button type="submit" class="btn btn-link">
                    <i class="glyphicons glyphicons-search"></i>
                    <span class="sr-only"><op:translate key="SEARCH_SUBMIT" /></span>
                </button>
            </div>
        </div>
    </div>
</form:form>
