<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />

<portlet:actionURL name="submit" var="submitUrl" />
<portlet:actionURL name="unlink" var="unlinkUrl" />

<portlet:resourceURL id="search" var="searchUrl" />

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="namespace"><portlet:namespace/></c:set>


<script type="text/javascript" src="${contextPath}/js/link/editor.min.js"></script>


<div class="editor-link" data-close-modal="${form.done}">
    <%--@elvariable id="form" type="org.osivia.services.editor.link.portlet.model.EditorLinkForm"--%>
    <form:form action="${submitUrl}" method="post" modelAttribute="form" role="form">
        <%--Done indicator--%>
        <form:hidden path="done" />
        <%--URL--%>
        <form:hidden path="url" />

        <%--URL type--%>
        <div class="form-group required">
            <form:label path="urlType"><op:translate key="EDITOR_URL" /></form:label>
            <c:forEach var="type" items="${urlTypes}" varStatus="status">
                <div class="form-check">
                    <form:radiobutton id="${namespace}-url-type-${status.index}" path="urlType" value="${type.id}" cssClass="form-check-input" />
                    <form:label for="${namespace}-url-type-${status.index}" path="urlType" cssClass="form-check-label"><op:translate key="${type.key}" /></form:label>

                    <div class="collapse ${type eq form.urlType ? 'show' : ''}">
                        <div class="pt-1 ${status.last ? '' : 'pb-2'}">
                            <c:choose>
                                <c:when test="${type.id eq 'MANUAL'}">
                                    <form:input path="manualUrl" cssClass="form-control" cssErrorClass="form-control is-invalid" />
                                    <form:errors path="manualUrl" cssClass="invalid-feedback" />
                                </c:when>

                                <c:when test="${type.id eq 'DOCUMENT'}">
                                    <c:set var="noResults"><op:translate key="SELECT2_NO_RESULTS" /></c:set>
                                    <c:set var="searching"><op:translate key="SELECT2_SEARCHING" /></c:set>
                                    <c:set var="loadingMore"><op:translate key="SELECT2_LOADING_MORE"/></c:set>

                                    <form:select path="documentWebId" cssClass="form-control select2" cssErrorClass="form-control is-invalid select2" data-url="${searchUrl}" data-no-results="${noResults}" data-searching="${searching}" data-loading-more="${loadingMore}">
                                        <c:if test="${not empty form.document}">
                                            <form:option value="${form.document.properties['ttc:webid']}" data-icon="${form.document.icon}">${form.document.title}</form:option>
                                        </c:if>
                                    </form:select>
                                    <form:errors path="documentWebId" cssClass="invalid-feedback" />
                                </c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <%--Text--%>
        <c:if test="${form.displayText}">
            <div class="form-group">
                <form:label path="text"><op:translate key="EDITOR_TEXT" /></form:label>
                <form:input path="text" cssClass="form-control" />
            </div>
        </c:if>

        <%--Title--%>
        <div class="form-group">
            <form:label path="title"><op:translate key="EDITOR_TITLE" /></form:label>
            <form:input path="title" cssClass="form-control" />
        </div>

        <%--Buttons--%>
        <div class="text-right">
            <%--Cancel--%>
            <button type="button" class="btn btn-default" data-dismiss="modal">
                <span><op:translate key="CANCEL" /></span>
            </button>

            <%--Remove--%>
            <a href="${unlinkUrl}" class="btn btn-default ${empty form.url ? 'disabled' : ''} ml-1">
                <span><op:translate key="EDITOR_UNLINK" /></span>
            </a>

            <%--Submit--%>
            <button type="submit" class="btn btn-primary ml-1">
                <span><op:translate key="VALIDATE" /></span>
            </button>
        </div>
    </form:form>
</div>
