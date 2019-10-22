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
          		<form:input type="text" path="url" cssClass="form-control" placeholder="https://www.lemonde.fr/rss/une.xml" />
            	<form:errors path="url" cssClass="help-block" />
      		</div>
		</spring:bind>
		
		<spring:bind path="functId">
			<div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">    			
   				<form:label path="functId" cssClass="control-label"><op:translate key="FUNCTID"/></form:label>
          		<form:input type="text" path="functId" cssClass="form-control" placeholder="ex: Région" />
            	<form:errors path="functId" cssClass="help-block" />
      		</div>
		</spring:bind>		
		
	    <div class="col-sm-offset-4 col-sm-8 col-lg-offset-5 col-lg-7">
	        <!-- Cancel -->
	        <a href="${cancelUrl}" class="btn btn-default">
	            <span><op:translate key="CANCEL"/></span>
	        </a>
	    	<button type="submit" name="add" class="btn btn-primary"><op:translate key="ADDCONTAINER"/></button>
	 	</div>
		
</form:form>