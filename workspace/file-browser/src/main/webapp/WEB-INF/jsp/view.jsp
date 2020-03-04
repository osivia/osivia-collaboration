<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

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
