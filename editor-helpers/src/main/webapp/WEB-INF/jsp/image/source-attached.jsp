<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:actionURL name="submit" var="submitUrl" copyCurrentRenderParameters="true"/>
<portlet:renderURL var="backUrl"/>

<c:set var="contextPath" value="${pageContext.request.contextPath}" />


<link rel="stylesheet" href="${contextPath}/css/image/image.css" />


<div class="editor-image">
    <%--@elvariable id="attachedForm" type="org.osivia.services.editor.image.portlet.model.EditorImageSourceAttachedForm"--%>
    <form:form action="${submitUrl}" method="post" modelAttribute="attachedForm">

        <%--Buttons--%>
        <div class="text-right">
            <a href="${backUrl}" class="btn btn-outline-secondary">
                <span><op:translate key="BACK"/></span>
            </a>
        </div>
    </form:form>
</div>
