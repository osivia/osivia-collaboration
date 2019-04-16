<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="drop" copyCurrentRenderParameters="true" var="dropUrl" />

<portlet:resourceURL id="toolbar" var="toolbarUrl" />


<div class="file-browser ${form.uploadable ? 'file-browser-drop-zone' : ''}" data-drop-url="${dropUrl}" data-toolbar-url="${toolbarUrl}">
    <div class="panel panel-default">
        <div class="panel-body">
            <%@ include file="toolbar.jspf" %>
        
            <jsp:include page="${view}.jsp" />
            
            <c:if test="${form.uploadable}">
                <%@ include file="upload.jspf" %>
            </c:if>
        </div>
    </div>
</div>
