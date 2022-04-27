<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>


<portlet:actionURL name="submit" var="url"/>


<%--@elvariable id="form" type="org.osivia.services.edition.portlet.model.FolderEditionForm"--%>
<form:form action="${url}" method="post" modelAttribute="form">
    <%--Title--%>
    <%@include file="fragments/title.jspf"%>

    <%--Buttons--%>
    <%@include file="fragments/buttons.jspf"%>
</form:form>
