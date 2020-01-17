<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>

<c:set var="namespace"><portlet:namespace/></c:set>
<portlet:actionURL name="save" var="save" copyCurrentRenderParameters="true" />
<portlet:actionURL name="cancel" var="cancel" copyCurrentRenderParameters="true" />

<form:form action="${save}" method="post" modelAttribute="form" enctype="multipart/form-data" role="form">

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
			
		<form:label path="visual.upload" class="control-label"><op:translate key="PICTURE_SLIDER" /></form:label>		
	    <div class="col-sm-9 col-lg-7">
		    <!-- Preview -->
		    <c:choose>
		    	<c:when test="${form.visual.updated}">
		        	<!-- Preview -->
		        	<jsp:useBean id="currentDate" class="java.util.Date" />
		        	<portlet:resourceURL id="visualPreview" var="previewUrl"><portlet:param name="ts" value="${currentDate.time}" /></portlet:resourceURL>
		        	<p class="form-control-static">
		        		<img src="${previewUrl}" alt="" class="img-responsive" style="max-height:250px; max-width: 250px;">
		       		 </p>
		        </c:when>
		                            
		       	<c:when test="${form.visual.deleted}">
		       	<!-- Deleted visual -->
		       		<p class="form-control-static text-muted">
		       			<span><op:translate key="DELETED_VISUAL" /></span>
		       		</p>
		       	</c:when>
		                        
		       	<c:when test="${empty form.visual.url}">
		       	<!-- No visual -->
		      		<p class="form-control-static text-muted">
		      			<span><op:translate key="NO_VISUAL" /></span>
		        	</p>
		       	</c:when>
		       	
		       			                            
		       	<c:otherwise>
		       	<!-- Visual -->
		       		<p class="form-control-static">
		       			<img src="${form.visual.url}" alt="" class="img-responsive">
		       		</p>
		     	 </c:otherwise>
		     </c:choose>
			
			<div class="d-flex flex-row">
				<!-- Upload -->
	    		<label class="btn btn-outline-secondary btn-file btn-sm">
	   				<i class="halflings halflings-folder-open"></i>
	   				<span><op:translate key="PICTURE_UPLOAD" /></span>
		      		<form:input type="file" path="visual.upload" data-change-submit="${namespace}-preview"/>
		      	</label>
		     	<input type="submit" name="upload-visual" class="d-none" id="${namespace}-preview">
		                            
		      	<!-- Delete -->
		      	<button type="submit" name="delete-visual" class="btn btn-outline-secondary btn-sm">
		     	 <i class="halflings halflings-trash"></i>
		       		<span class="sr-only"><op:translate key="DELETE" /></span>
		      	</button>
	     	</div>			
				
		</div>
		 <div class="float-right">
	        <!-- Cancel -->
	        <a href="${cancel}" class="btn btn-secondary">
	            <span><op:translate key="CANCEL"/></span>
	        </a>
	    	<button type="submit" name="save" class="btn btn-primary"><op:translate key="ADD_FEED"/></button>
		 </div>
</form:form>