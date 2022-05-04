<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>


<portlet:actionURL name="submit" var="url"/>

<%--@elvariable id="form" type="org.osivia.services.edition.portlet.model.FilesCreationForm"--%>
<form:form action="${url}" method="post" enctype="multipart/form-data" modelAttribute="form">
    <%--Files--%>
    <%@include file="fragments/files.jspf" %>

    <%--Buttons--%>
    <%@include file="fragments/buttons.jspf" %>
</form:form>
