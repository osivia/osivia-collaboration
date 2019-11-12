<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c"       uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form"    uri="http://www.springframework.org/tags/form"  %>
<%@ taglib prefix="op"      uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>


<portlet:renderURL var="addUrl">
	<portlet:param name="view" value="add" />
</portlet:renderURL>
<portlet:actionURL name="synchro" var="synchro" />

<form:form action="${synchro}" method="post" modelAttribute="form">
	<div>
		<c:if test="${empty containers}">
			<p>
				<span><op:translate key="LIST_CONTAINER_NO_RESULT" /></span>
			</p>
		</c:if>

		<ol>
			<c:forEach var="feed" items="${feeds}" varStatus="status">
				<li>${feed.displayName}</li>
			</c:forEach>
		</ol>
	</div>

	<div>
		<a href="${addUrl}" class="btn btn-primary"><op:translate
				key="ADDFEED" /></a>
		<button type="submit" name="synchro" class="btn btn-primary">
			<op:translate key="SYNCHRO" />
		</button>
	</div>
</form:form>