<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>


<portlet:actionURL name="submit" var="url"/>


<%--@elvariable id="form" type="org.osivia.services.edition.portlet.model.NoteEditionForm"--%>
<form:form action="${url}" method="post" modelAttribute="form">
    <%--Title--%>
    <%@include file="fragments/title.jspf"%>

    <%--Content--%>
    <c:set var="placeholder"><op:translate key="DOCUMENT_EDITION_FORM_NOTE_CONTENT_PLACEHOLDER"/></c:set>
    <div class="form-group">
        <form:label path="content" cssClass="control-label"><op:translate key="DOCUMENT_EDITION_FORM_NOTE_CONTENT_LABEL"/></form:label>
        <form:textarea path="content" cssClass="form-control tinymce tinymce-simple" placeholder="${placeholder}"/>
    </div>

    <%--Buttons--%>
    <%@include file="fragments/buttons.jspf"%>
</form:form>
