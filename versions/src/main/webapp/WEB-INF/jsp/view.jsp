<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div>
    <!-- Versions list -->
    <jsp:include page="list.jsp" />
    <!-- Create a version of current document -->
   <%--  <jsp:include page="create.jsp" />  --%>
</div>
