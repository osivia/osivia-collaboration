<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>


<portlet:actionURL name="submit" var="url"/>


<form:form action="${url}" method="post" enctype="multipart/form-data" modelAttribute="form">
    <c:if test="${not form.creation}">
        <%--Title--%>
        <%@include file="fragments/title.jspf" %>
    </c:if>

    <%--File--%>
    <%@include file="fragments/file.jspf" %>

    <%--Buttons--%>
    <%@include file="fragments/buttons.jspf" %>
</form:form>
