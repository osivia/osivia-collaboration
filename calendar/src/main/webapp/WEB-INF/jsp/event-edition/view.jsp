<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="save" var="saveUrl"/>


<div class="calendar calendar-event-edition">
    <form:form action="${saveUrl}" method="post" enctype="multipart/form-data" modelAttribute="form" cssClass="form-horizontal" role="form">
        <!-- Title -->
        <%@ include file="title.jspf" %>

        <!-- Dates -->
        <%@ include file="dates.jspf" %>

        <!-- Location -->
        <%@ include file="location.jspf" %>
        
        <!-- Color -->
        <%@ include file="color.jspf" %>
        
        <!-- Description -->
        <%@ include file="description.jspf" %>
        
        <!-- Attachments -->
        <%@ include file="attachments.jspf" %>

        <!-- Buttons -->
        <%@ include file="buttons.jspf" %>
    </form:form>
</div>
