<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>

<portlet:actionURL name="del" var="del" copyCurrentRenderParameters="true"/>
<portlet:actionURL name="modif" var="modif" copyCurrentRenderParameters="true" />
<portlet:renderURL var="cancelUrl">
	<portlet:param name="view" value="container" />
</portlet:renderURL>

<form:form action="${modif}" method="post" modelAttribute="form">

		<spring:bind path="name">
			<div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">    			
   				<form:label path="name" cssClass="control-label"><op:translate key="NAME"/></form:label>
          		<form:input path="name" type="text" cssClass="form-control" placeholder="ex: Toutatice" />
            	<form:errors path="name" cssClass="help-block" />
      		</div>
		</spring:bind>
		
	    <div>
	        <!-- Cancel -->
	        <a href="${cancelUrl}" class="btn btn-default">
	            <span><op:translate key="CANCEL"/></span>
	        </a>
	    	<a href="${del}" class="btn btn-primary"><op:translate key="DEL_CONTAINER"/></a>
	    	<button type="submit" class="btn btn-primary"><op:translate key="MOD_CONTAINER"/></button>
	 	</div>
		
</form:form>