<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>


<portlet:renderURL var="backUrl"/>
<portlet:resourceURL id="search" var="searchUrl"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="namespace"><portlet:namespace/></c:set>


<link rel="stylesheet" type="text/css" href="${contextPath}/css/link/style.min.css">
<script type="text/javascript" src="${contextPath}/js/link/editor.min.js"></script>


<div class="editor-link">
    <form onsubmit="return false;">
        <div class="form-group">
            <label for="${namespace}-filter" class="control-label"><op:translate key="EDITOR_LINK_DOCUMENT_FORM_FILTER_LABEL"/></label>
            <input id="${namespace}-filter" type="search" name="filter" class="form-control"/>
        </div>

        <div class="form-group">
            <label class="control-label"><op:translate key="EDITOR_LINK_DOCUMENT_FORM_DOCUMENTS_LABEL"/></label>
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
    </form>
</div>
