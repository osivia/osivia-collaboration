<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

<%@ page isELIgnored="false" %>


<portlet:renderURL var="backUrl"/>
<portlet:actionURL name="submit" var="submitUrl" copyCurrentRenderParameters="true"/>
<portlet:resourceURL id="search" var="searchUrl"/>


<c:set var="contextPath" value="${pageContext.request.contextPath}"/>


<link rel="stylesheet" type="text/css" href="${contextPath}/css/image/style.min.css">
<script type="text/javascript" src="${contextPath}/js/image/editor.min.js"></script>


<div class="editor-image">
    <%--@elvariable id="documentForm" type="org.osivia.services.editor.image.portlet.model.EditorImageSourceDocumentForm"--%>
    <form:form action="${submitUrl}" method="post" modelAttribute="documentForm">
        <div class="form-group">
            <form:label path="filter" cssClass="control-label"><op:translate
                    key="EDITOR_IMAGE_DOCUMENT_FORM_FILTER_LABEL"/></form:label>
            <form:input path="filter" type="search" cssClass="form-control"/>
        </div>

        <div class="form-group">
            <label class="control-label"><op:translate key="EDITOR_IMAGE_DOCUMENT_FORM_DOCUMENTS_LABEL"/></label>
            <div data-search-url="${searchUrl}">
                <p class="text-center text-muted"><op:translate key="EDITOR_SEARCH_LOADING"/></p>
            </div>
        </div>

        <%--Buttons--%>
        <div class="text-right">
            <a href="${backUrl}" class="btn btn-default">
                <span><op:translate key="BACK"/></span>
            </a>
        </div>
    </form:form>
</div>
