<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="op"  uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>
<%@ taglib prefix="ttc" uri="http://www.toutatice.fr/jsp/taglib/toutatice" %>

<%@ page isELIgnored="false" %>

<portlet:renderURL var="addUrl">
	<portlet:param name="view" value="add" />
</portlet:renderURL>

<div>
    <c:if test="${empty containers}">
        <p>
            <span><op:translate key="LIST_NO_RESULT" /></span>
        </p>
    </c:if>

	<ol>
		<c:forEach var="container" items="${containers}" varStatus="status">
			<li><ttc:title document="${container.document}" /></li>
		</c:forEach>
	</ol>
</div>

<div>
	<a href="${addUrl}" class="btn btn-primary"><op:translate key="ADDCONTAINER"/></a>
</div>