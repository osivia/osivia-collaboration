<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>


<portlet:actionURL name="submit" var="url"/>


<form:form action="${url}" method="post" modelAttribute="form">
    <%--Location--%>
    <%@include file="fragments/location.jspf" %>

    <%--Title--%>
    <c:set var="placeholder" scope="request"><op:translate key="DOCUMENT_EDITION_FORM_TITLE_PLACEHOLDER_NOTE"/></c:set>
    <%@include file="fragments/title.jspf" %>

    <%--Content--%>
    <c:set var="placeholder"><op:translate key="DOCUMENT_EDITION_FORM_NOTE_CONTENT_PLACEHOLDER"/></c:set>
    <div class="form-group">
        <form:label path="content"><op:translate key="DOCUMENT_EDITION_FORM_NOTE_CONTENT_LABEL"/></form:label>
        <form:textarea path="content" cssClass="form-control tinymce ${form.windowProperties.modal ? 'tinymce-simple' : 'tinymce-default'}" placeholder="${placeholder}"/>
    </div>

    <%--Buttons--%>
    <%@include file="fragments/buttons.jspf" %>
</form:form>
