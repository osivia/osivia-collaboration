<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>


<portlet:actionURL name="submit" var="url"/>


<form:form action="${url}" method="post" modelAttribute="form">
    <%--Location--%>
    <%@include file="fragments/location.jspf" %>

    <%--Title--%>
    <c:set var="placeholder" scope="request"><op:translate key="DOCUMENT_EDITION_FORM_TITLE_PLACEHOLDER_FOLDER"/></c:set>
    <%@include file="fragments/title.jspf" %>

    <%--Buttons--%>
    <%@include file="fragments/buttons.jspf" %>
</form:form>
