<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="submit" var="url" />


<div class="calendar calendar-synchronization-edition" data-close-modal="${form.done}">
    <form:form action="${url}" method="post" modelAttribute="form" role="form">
        <form:hidden path="sourceId" />
        <form:hidden path="done" />
    
        <!-- URL -->
        <%@ include file="url.jspf" %>
        
        <!-- Display name -->
        <%@ include file="display-name.jspf" %>
        
        <!-- Color -->
        <%@ include file="color.jspf" %>
        
        <!-- Buttons -->
        <%@ include file="buttons.jspf" %>
    </form:form>
</div>
