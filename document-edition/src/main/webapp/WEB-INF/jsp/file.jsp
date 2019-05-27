<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>


<portlet:actionURL name="save" var="url"/>


<form:form action="${url}" method="post" modelAttribute="form">
    <%--Title--%>
    <%@include file="fragments/title.jspf"%>

    <%--Buttons--%>
    <%@include file="fragments/buttons.jspf"%>
</form:form>
