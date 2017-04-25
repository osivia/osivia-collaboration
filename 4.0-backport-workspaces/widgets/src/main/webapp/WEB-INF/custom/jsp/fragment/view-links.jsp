<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />


<div class="links">
    <c:choose>
        <c:when test="${empty template}">
            <ttc:include page="view-links-default.jsp" />
        </c:when>
        
        <c:otherwise>
            <ttc:include page="view-links-${fn:toLowerCase(template)}.jsp" />
        </c:otherwise>
    </c:choose>
</div>
