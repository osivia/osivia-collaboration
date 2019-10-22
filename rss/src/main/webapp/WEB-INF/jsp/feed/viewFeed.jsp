<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>

<portlet:actionURL name="add" var="add" />

<form:form action="${add}" method="post" modelAttribute="form">

	<spring:bind path="url">
		<div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">    			
			<form:label path="url" cssClass="control-label"><op:translate key="NAME"/></form:label>
       		<form:input type="text" path="url" cssClass="form-control" placeholder="https://www.lemonde.fr/rss/une.xml" />
         	<form:errors path="name" cssClass="help-block" />
   		</div>
	</spring:bind>

	<div class="row">
		<div class="col-lg-offset-8 col-lg-4">
		   	<button type="submit" name="add" class="btn btn-primary"><op:translate key="ADD"/></button>
		</div>
	</div>
	
	<div>
		<fieldset>
			<Legend>Liste des flux</Legend>
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
		</fieldset>
	</div>
	
	<div>
		<button type="submit" name="synchro" class="btn btn-primary"><op:translate key="SYNCHRO"/></button>
	</div>

</form:form>