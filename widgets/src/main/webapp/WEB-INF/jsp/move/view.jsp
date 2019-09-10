<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>


<portlet:actionURL name="move" var="moveUrl"/>

<portlet:resourceURL id="browse" var="browseUrl">
    <portlet:param name="cmsBasePath" value="${form.basePath}"/>
    <portlet:param name="cmsNavigationPath" value="${form.navigationPath}"/>
    <portlet:param name="live" value="true"/>
    <portlet:param name="ignoredPaths" value="${fn:join(form.ignoredPaths, ',')}"/>
    <portlet:param name="acceptedTypes" value="${fn:join(form.acceptedTypes, ',')}"/>
    <portlet:param name="excludedTypes" value="${fn:join(form.excludedTypes, ',')}"/>
</portlet:resourceURL>


<form:form action="${moveUrl}" method="post" modelAttribute="form" role="form">
    <fieldset>
        <legend>
            <i class="glyphicons glyphicons-basic-block-move"></i>
            <span><op:translate key="MOVE_LEGEND"/></span>
        </legend>

        <%--Target path--%>
        <spring:bind path="targetPath">
            <div class="form-group required selector">
                <form:label path="targetPath"><op:translate key="MOVE_TARGET_PATH_LABEL"/></form:label>
                <form:hidden path="targetPath" cssClass="selector-value form-control ${status.error ? 'is-invalid' : ''}"/>
                <div class="fancytree fancytree-selector fixed-height p-2 border rounded ${status.error ? 'border-danger' : ''}" data-lazyloadingurl="${browseUrl}"></div>
                <form:errors path="targetPath" cssClass="invalid-feedback"/>
            </div>
        </spring:bind>

        <%--Buttons--%>
        <div class="text-right">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">
                <span><op:translate key="CANCEL"/></span>
            </button>

            <button type="submit" class="btn btn-primary">
                <span><op:translate key="MOVE_ACTION"/></span>
            </button>
        </div>
    </fieldset>
</form:form>
