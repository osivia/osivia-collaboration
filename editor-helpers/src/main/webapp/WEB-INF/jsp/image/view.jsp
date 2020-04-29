<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>

<c:set var="namespace"><portlet:namespace/></c:set>
<c:set var="contextPath" value="${pageContext.request.contextPath}" />

<portlet:actionURL name="submit" var="submitUrl"/>


<link rel="stylesheet" href="${contextPath}/css/image/image.css" />


<div class="editor-image" data-close-modal="${form.done}">
    <%--@elvariable id="form" type="org.osivia.services.editor.image.portlet.model.EditorImageForm"--%>
    <form:form action="${submitUrl}" method="post" modelAttribute="form">
        <form:hidden path="done"/>

        <%--URL--%>
        <div class="form-group required">
            <form:label path="url"><op:translate key="EDITOR_IMAGE_FORM_URL_LABEL"/></form:label>
            <form:input path="url" cssClass="form-control" cssErrorClass="form-control is-invalid"/>
            <c:if test="${not empty form.availableSourceTypes}">
                <div>
                    <c:forEach var="type" items="${form.availableSourceTypes}" varStatus="status">
                        <button type="submit" name="source-${type.id}" class="btn btn-outline-secondary btn-sm mt-2 ${status.last ? '' : 'mr-1'}">
                            <span><op:translate key="${type.key}"/></span>
                        </button>
                    </c:forEach>
                </div>
            </c:if>
            <form:errors path="url" cssClass="invalid-feedback"/>
        </div>

        <%--Alternate text--%>
        <div class="form-group">
            <form:label path="alt"><op:translate key="EDITOR_IMAGE_FORM_ALT_LABEL"/></form:label>
            <form:input path="alt" cssClass="form-control"/>
        </div>

        <%--Buttons--%>
        <div class="text-right">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">
                <span><op:translate key="CANCEL"/></span>
            </button>

            <button type="submit" name="save" class="btn btn-primary ml-1">
                <span><op:translate key="VALIDATE"/></span>
            </button>
        </div>
    </form:form>
</div>
