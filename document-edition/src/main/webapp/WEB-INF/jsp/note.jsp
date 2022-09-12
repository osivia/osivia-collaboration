<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

<%@ page isELIgnored="false" %>


<portlet:defineObjects/>

<portlet:actionURL name="submit" var="url"/>


<%--@elvariable id="form" type="org.osivia.services.edition.portlet.model.NoteEditionForm"--%>
<form:form action="${url}" method="post" enctype="multipart/form-data" modelAttribute="form">
    <%--Title--%>
    <%@ include file="fragments/title.jspf" %>

    <%--Content--%>
    <%@ include file="fragments/note-content.jspf" %>

    <%--Attachments--%>
    <%@ include file="fragments/attachments.jspf" %>

    <c:if test="${not form.creation}">
        <%--Metadata--%>
        <%@ include file="fragments/metadata.jspf" %>
    </c:if>

    <%--Buttons--%>
    <%@ include file="fragments/buttons.jspf" %>
</form:form>
