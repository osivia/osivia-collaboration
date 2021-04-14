<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>


<portlet:renderURL var="backUrl"/>
<portlet:actionURL name="submit" var="submitUrl" copyCurrentRenderParameters="true"/>
<portlet:resourceURL id="search" var="searchUrl"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<link rel="stylesheet" type="text/css" href="${contextPath}/css/link/style.min.css">
<script type="text/javascript" src="${contextPath}/js/link/editor.min.js"></script>


<div class="editor-link">
    <%--@elvariable id="documentForm" type="org.osivia.services.editor.link.portlet.model.EditorLinkSourceDocumentForm"--%>
    <form:form action="${submitUrl}" method="post" modelAttribute="documentForm">
        <div class="form-group">
            <form:label path="filter" cssClass="control-label"><op:translate key="EDITOR_LINK_DOCUMENT_FORM_FILTER_LABEL"/></form:label>
            <form:input path="filter" type="search" cssClass="form-control"/>
            <c:forEach var="scope" items="${documentForm.availableScopes}">
                <div class="radio">
                    <label>
                        <form:radiobutton path="scope" value="${scope}"/>
                        <span><op:translate key="${scope.key}"/></span>
                    </label>
                </div>
            </c:forEach>
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
    </form:form>
</div>
