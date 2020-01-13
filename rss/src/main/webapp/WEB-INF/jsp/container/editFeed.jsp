<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>

<portlet:actionURL name="modif" var="modif" copyCurrentRenderParameters="true" />
<portlet:actionURL name="del" var="delFeed" copyCurrentRenderParameters="true" />
<portlet:renderURL var="cancelFeed">
	<portlet:param name="view" value="container" />
</portlet:renderURL>

<form:form action="${modif}" method="post" modelAttribute="form">

	<fieldset>
		<legend align="left">Edition du flux ${form.url} :</legend>
		<spring:bind path="displayName">
			<div
				class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
				<form:label path="displayName" cssClass="control-label">
					<op:translate key="NAME_TITLE" />
				</form:label>
				<form:input path="displayName" type="text" cssClass="form-control" />
				<form:errors path="displayName" cssClass="help-block" />
			</div>
			
		</spring:bind>
		
<%-- 		<spring:bind path="image">
			<div c
		</spring:bind>  --%>

	</fieldset>

	<div class="float-right">
		<!-- Cancel -->
		<a href="${cancelFeed}" class="btn btn-default"> <span><op:translate
					key="CANCEL" /></span>
		</a>
		<button type="submit" class="btn btn-primary">
			<op:translate key="MOD_FEED" />
		</button>
		<a href="${delFeed}" class="btn btn-primary"><span><op:translate
					key="DEL_FEED" /></span></a>
	</div>

</form:form>