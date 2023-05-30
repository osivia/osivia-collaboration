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


<link rel="stylesheet" type="text/css" href="${contextPath}/css/link/style.min.css">
<script type="text/javascript" src="${contextPath}/js/link/editor.min.js"></script>


<div class="editor-link" data-close-modal="${form.done}">
    <%--@elvariable id="form" type="org.osivia.services.editor.link.portlet.model.EditorLinkForm"--%>
    <form:form action="${submitUrl}" method="post" modelAttribute="form" role="form">
        <%--Default submit--%>
        <input type="submit" name="save" class="hidden">

        <%--Done indicator--%>
        <form:hidden path="done"/>

        <%--URL--%>
        <spring:bind path="url">
            <div class="form-group required ${status.error ? 'has-error' : ''}">
                <form:label path="url" cssClass="control-label"><op:translate key="EDITOR_LINK_FORM_URL_LABEL"/></form:label>
                <p>
                    <form:input path="url" cssClass="form-control" />
                </p>
                <div>
                    <button type="submit" name="source-document" class="btn btn-default btn-sm">
                        <i class="glyphicons glyphicons-search"></i>
                        <span><op:translate key="EDITOR_LINK_FORM_SOURCE_DOCUMENT"/></span>
                    </button>
                </div>
                <form:errors path="url" cssClass="help-block"/>
            </div>
        </spring:bind>

        <%--Text--%>
        <c:if test="${form.displayText}">
            <div class="form-group">
                <form:label path="text" cssClass="control-label"><op:translate key="EDITOR_LINK_FORM_TEXT_LABEL" /></form:label>
                <form:input path="text" cssClass="form-control" />
                <p class="help-block"><op:translate key="EDITOR_LINK_FORM_TEXT_HELP"/></p>
            </div>
        </c:if>

        <%--Title--%>
        <div class="form-group">
            <form:label path="title" cssClass="control-label"><op:translate key="EDITOR_LINK_FORM_TITLE_LABEL" /></form:label>
            <form:input path="title" cssClass="form-control" />
        </div>

        <%--Force open in new window--%>
        <div class="form-group">
            <div class="checkbox">
                <label>
                    <form:checkbox path="forceNewWindow"/>
                    <span><op:translate key="EDITOR_LINK_FORM_FORCE_NEW_WINDOW_ACTION"/></span>
                </label>
                <span class="help-block"><op:translate key="EDITOR_LINK_FORM_FORCE_NEW_WINDOW_HELP"/></span>
            </div>
        </div>

        <%--Buttons--%>
        <div class="text-right">
            <%--Cancel--%>
            <button type="button" class="btn btn-default" data-dismiss="modal">
                <span><op:translate key="CANCEL" /></span>
            </button>

            <%--Remove--%>
            <a href="${unlinkUrl}" class="btn btn-default ${empty form.url ? 'disabled' : ''} ml-1">
                <span><op:translate key="EDITOR_LINK_FORM_UNLINK" /></span>
            </a>

            <%--Submit--%>
            <button type="submit" name="save" class="btn btn-primary ml-1">
                <span><op:translate key="VALIDATE" /></span>
            </button>
        </div>
    </form:form>
</div>
