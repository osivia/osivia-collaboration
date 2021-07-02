<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>

<portlet:actionURL name="submit" var="submitUrl"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<link rel="stylesheet" type="text/css" href="${contextPath}/css/image/style.min.css">
<script type="text/javascript" src="${contextPath}/js/image/editor.min.js"></script>


<div class="editor-image" data-close-modal="${form.done}">
    <%--@elvariable id="form" type="org.osivia.services.editor.image.portlet.model.EditorImageForm"--%>
    <form:form action="${submitUrl}" method="post" modelAttribute="form">
        <%--Done indicator--%>
        <form:hidden path="done"/>

        <%--URL--%>
        <spring:bind path="url">
            <div class="form-group required ${status.error ? 'has-error' : ''}">
                <form:label path="url" cssClass="control-label"><op:translate key="EDITOR_IMAGE_FORM_URL_LABEL"/></form:label>
                <p>
                    <form:input path="url" cssClass="form-control" />
                </p>
                <c:choose>
                    <c:when test="${form.creation}">
                        <p class="help-block"><op:translate key="EDITOR_IMAGE_NO_ATTACHED_IN_CREATION_HELP"/></p>
                    </c:when>
                    <c:when test="${not empty form.availableSourceTypes}">
                        <div>
                            <c:forEach var="type" items="${form.availableSourceTypes}">
                                <button type="submit" name="source-${type.id}" class="btn btn-default btn-sm">
                                    <i class="${type.icon}"></i>
                                    <span><op:translate key="${type.key}"/></span>
                                </button>
                            </c:forEach>
                        </div>
                    </c:when>
                </c:choose>
                <form:errors path="url" cssClass="help-block"/>
            </div>
        </spring:bind>

        <%--Alternate text--%>
        <div class="form-group">
            <form:label path="alt" cssClass="control-label"><op:translate key="EDITOR_IMAGE_FORM_ALT_LABEL"/></form:label>
            <form:input path="alt" cssClass="form-control"/>
            <p class="help-block"><op:translate key="EDITOR_IMAGE_FORM_ALT_HELP"/></p>
        </div>

        <div class="row">
            <div class="col-xs-6">
                <%--Height--%>
                <div class="form-group">
                    <form:label path="height" cssClass="control-label"><op:translate key="EDITOR_IMAGE_FORM_HEIGHT_LABEL"/></form:label>
                    <form:input path="height" type="number" min="1" cssClass="form-control"/>
                </div>
            </div>

            <div class="col-xs-6">
                <%--Width--%>
                <div class="form-group">
                    <form:label path="width" cssClass="control-label"><op:translate key="EDITOR_IMAGE_FORM_WIDTH_LABEL"/></form:label>
                    <form:input path="width" type="number" min="1" cssClass="form-control"/>
                </div>
            </div>
        </div>

        <%--Buttons--%>
        <div class="text-right">
            <button type="button" class="btn btn-default" data-dismiss="modal">
                <span><op:translate key="CANCEL"/></span>
            </button>

            <button type="submit" name="save" class="btn btn-primary ml-1">
                <span><op:translate key="VALIDATE"/></span>
            </button>
        </div>
    </form:form>
</div>
