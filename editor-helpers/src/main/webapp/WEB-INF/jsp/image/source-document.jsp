<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

<%@ page isELIgnored="false" %>


<portlet:actionURL name="submit" var="submitUrl" copyCurrentRenderParameters="true"/>
<portlet:renderURL var="backUrl"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<link rel="stylesheet" href="${contextPath}/css/image/image.css" />


<div class="editor-image">
    <%--@elvariable id="documentForm" type="org.osivia.services.editor.image.portlet.model.EditorImageSourceDocumentForm"--%>
    <form:form action="${submitUrl}" method="post" modelAttribute="documentForm">
        <fieldset>
            <legend><op:translate key="EDITOR_IMAGE_DOCUMENT_FORM_LEGEND"/></legend>

            <div class="form-group">
                <form:label path="filter" cssClass="control-label"><op:translate key="EDITOR_IMAGE_DOCUMENT_FORM_FILTER_LABEL"/></form:label>
                <p>
                    <form:input path="filter" cssClass="form-control"/>
                </p>
                <button type="submit" class="btn btn-default btn-sm">
                    <span><op:translate key="EDITOR_IMAGE_DOCUMENT_FORM_FILTER_SUBMIT"/></span>
                </button>
            </div>

            <div class="form-group">
                <form:label path="documents" cssClass="control-label"><op:translate key="EDITOR_IMAGE_DOCUMENT_FORM_DOCUMENTS_LABEL"/></form:label>
                <c:choose>
                    <c:when test="${empty documentForm.documents}">
                        <p class="form-control-plaintext">
                            <span class="text-muted"><op:translate key="EDITOR_IMAGE_DOCUMENT_FORM_DOCUMENTS_EMPTY"/></span>
                        </p>
                    </c:when>

                    <c:otherwise>
                        <div class="row">
                            <c:forEach var="document" items="${documentForm.documents}">
                                <ttc:documentLink document="${document}" picture="true" displayContext="Medium" var="imageLink" />
                                <portlet:actionURL name="select" var="selectUrl" copyCurrentRenderParameters="true">
                                    <portlet:param name="path" value="${document.path}"/>
                                </portlet:actionURL>

                                <div class="col-xs-6">
                                    <a href="${selectUrl}" class="thumbnail" title="${document.title}">
                                        <img src="${imageLink.url}" alt="${document.title}">
                                    </a>
                                </div>
                            </c:forEach>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </fieldset>

        <%--Buttons--%>
        <div class="text-right">
            <a href="${backUrl}" class="btn btn-default">
                <span><op:translate key="BACK"/></span>
            </a>
        </div>
    </form:form>
</div>
