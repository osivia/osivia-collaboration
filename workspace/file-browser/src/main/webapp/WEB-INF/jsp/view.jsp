<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects/>

<portlet:actionURL name="drop" copyCurrentRenderParameters="true" var="dropUrl"/>

<portlet:resourceURL id="toolbar" var="toolbarUrl"/>
<portlet:resourceURL id="location" var="locationUrl"/>


<div class="file-browser position-relative ${form.uploadable ? 'file-browser-drop-zone' : ''}" data-drop-url="${dropUrl}"
     data-toolbar-url="${toolbarUrl}" data-location-url="${locationUrl}">
    <%@ include file="toolbar.jspf" %>

    <jsp:include page="${view}.jsp"/>

    <c:if test="${form.uploadable}">
        <%@ include file="upload.jspf" %>
    </c:if>
</div>
