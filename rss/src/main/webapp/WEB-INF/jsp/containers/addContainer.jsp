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

		<spring:bind path="name">
			<div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">    			
   				<form:label path="name" cssClass="control-label"><op:translate key="NAME"/></form:label>
          		<form:input path="name" type="text" cssClass="form-control" placeholder="ex: Toutatice" />
            	<form:errors path="name" cssClass="help-block" />
      		</div>
		</spring:bind>
		
		<spring:bind path="path">
			<div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">    			
   				<form:label path="path" cssClass="control-label"><op:translate key="PATH"/></form:label>
          		<form:input path="path" type="text" cssClass="form-control" placeholder="/default-domain/workspaces" />
            	<form:errors path="path" cssClass="help-block" />
      		</div>
		</spring:bind>		
		
	    <div class="col-sm-offset-4 col-sm-8 col-lg-offset-5 col-lg-7">
	        <!-- Cancel -->
	        <a href="${cancelUrl}" class="btn btn-default">
	            <span><op:translate key="CANCEL"/></span>
	        </a>
	    	<button type="submit" name="add" class="btn btn-primary"><op:translate key="ADD_CONTAINER"/></button>
	 	</div>
		
</form:form>