<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>

<div>
    <input type="text" placeholder="Ajouter un flux" />
	<button type="submit" name="send" class="btn btn-primary"><op:translate key="ADD"/></button>
</div>


<div>
    <c:if test="${empty containers}">
        <p>
            <span><op:translate key="LIST_CONTAINER_NO_RESULT" /></span>
        </p>
    </c:if>
	
	<ol>
    	<c:forEach var="container" items="${containers}" varStatus="status">
    		<li>
	            <strong>${container.displayName}</strong>
    		</li>
	    </c:forEach>
	</ol>

</div>

<div>	
	<input type="submit" class="hidden" name="synchro" id="synchro">
</div>