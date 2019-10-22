<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>

<portlet:actionURL name="add" var="add" />

<div>
    <c:if test="${empty containers}">
        <p>
            <span><op:translate key="LIST_CONTAINER_NO_RESULT" /></span>
        </p>
    </c:if>
	
	<ol>
    	<c:forEach var="feed" items="${feeds}" varStatus="status">
    		<c:set var="url"><ttc:documentLink document="${feed}" /></c:set>
    		<li>
	            <a href="url">${feed.name}</strong>
    		</li>
	    </c:forEach>
	</ol>
</div>

<div>
	<a href="${addUrl}" class="btn btn-primary"><op:translate key="ADDFEED"/></a>
</div>
<div>
	<button type="submit" name="synchro" class="btn btn-primary"><op:translate key="SYNCHRO"/></button>
</div>