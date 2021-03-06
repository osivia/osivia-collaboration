<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:renderURL var="back" />


<p class="text-danger">
    <i class="glyphicons glyphicons-remove-sign"></i>
    <span>${message}</span>
</p>


<a href="${back}" class="btn btn-secondary">
    <i class="glyphicons glyphicons-arrow-left"></i>
    <span><op:translate key="BACK" /></span>
</a>
