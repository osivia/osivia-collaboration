<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>

<portlet:actionURL name="submit" var="submitUrl" copyCurrentRenderParameters="true"/>
<portlet:renderURL var="backUrl"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />
<c:set var="namespace"><portlet:namespace/></c:set>


<link rel="stylesheet" type="text/css" href="${contextPath}/css/image/style.min.css">
<script type="text/javascript" src="${contextPath}/js/image/editor.min.js"></script>


<div class="editor-image">
    <%--@elvariable id="attachedForm" type="org.osivia.services.editor.image.portlet.model.EditorImageSourceAttachedForm"--%>
    <form:form action="${submitUrl}" method="post" enctype="multipart/form-data" modelAttribute="attachedForm">
        <%--Add--%>
        <spring:bind path="upload">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:label path="upload" cssClass="control-label"><op:translate
                        key="EDITOR_IMAGE_ATTACHED_UPLOAD_LABEL"/></form:label>
                <form:input path="upload" type="file" data-change-submit="${namespace}-upload"/>
                <form:errors path="upload" cssClass="help-block"/>
            </div>
        </spring:bind>

        <%--Select--%>
        <div class="form-group">
            <label class="control-label"><op:translate key="EDITOR_IMAGE_ATTACHED_SELECT_LABEL"/></label>
            <c:choose>
                <c:when test="${empty attachedForm.attachedImages}">
                    <p class="form-control-static text-muted"><op:translate key="EDITOR_IMAGE_ATTACHED_SELECT_EMPTY"/></p>
                </c:when>

                <c:otherwise>
                    <div class="row">
                        <c:forEach var="attachedImage" items="${attachedForm.attachedImages}" varStatus="status">
                            <div class="col-xs-6">
                                <div class="thumbnail">
                                    <img src="${attachedImage.url}">

                                    <div class="caption">
                                        <h3 class="h5 text-center">${attachedImage.fileName}</h3>
                                        <div class="text-center">
                                            <%--Select--%>
                                            <portlet:actionURL var="url" name="select" copyCurrentRenderParameters="true">
                                                <portlet:param name="index" value="${attachedImage.index}"/>
                                            </portlet:actionURL>
                                            <a href="${url}" class="btn btn-primary btn-sm">
                                                <span><op:translate key="SELECT"/></span>
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <c:if test="${status.count % 2 eq 0}">
                                <div class="clearfix"></div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>

        </div>

        <%--Buttons--%>
        <div class="text-right">
            <a href="${backUrl}" class="btn btn-default">
                <span><op:translate key="BACK"/></span>
            </a>
        </div>

        <input id="${namespace}-upload" type="submit" name="upload" class="hidden">
    </form:form>
</div>
