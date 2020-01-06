<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>

<portlet:actionURL name="add" var="add" copyCurrentRenderParameters="true" />
<portlet:renderURL var="cancelUrl">
	<portlet:param name="view" value="container" />
</portlet:renderURL>

<form:form action="${add}" method="post" modelAttribute="form">

		<spring:bind path="url">
			<div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">    			
   				<form:label path="url" cssClass="control-label"><op:translate key="URL"/></form:label>
          		<form:input path="url" type="text" cssClass="form-control" placeholder="https://www.lemonde.fr/rss/une.xml" />
            	<form:errors path="url" cssClass="help-block" />
      		</div>
		</spring:bind>
		
		<spring:bind path="displayName">
			<div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">    			
   				<form:label path="displayName" cssClass="control-label"><op:translate key="NAME_TITLE"/></form:label>
          		<form:input path="displayName" type="text" cssClass="form-control" />
            	<form:errors path="displayName" cssClass="help-block" />
      		</div>
		</spring:bind>	
			
	    <div>
	        <!-- Cancel -->
	        <a href="${cancelUrl}" class="btn btn-default">
	            <span><op:translate key="CANCEL"/></span>
	        </a>
	    	<button type="submit" name="add" class="btn btn-primary"><op:translate key="ADD_FEED"/></button>
	 	</div>
		
</form:form>