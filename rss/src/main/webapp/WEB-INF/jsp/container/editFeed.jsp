<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="op" uri="http://www.osivia.org/jsp/taglib/osivia-portal" %>

<%@ page isELIgnored="false" %>

<portlet:actionURL name="modif" var="modif" copyCurrentRenderParameters="true" />
<portlet:actionURL name="del" var="delFeed" copyCurrentRenderParameters="true" />
<portlet:actionURL name="cancel" var="cancel" copyCurrentRenderParameters="true" />

<form:form action="${modif}" method="post" modelAttribute="form" enctype="multipart/form-data" role="form">

	<fieldset>
		<legend><op:translate key="LEGEND_EDIT" /></legend>
			<div class="form-group">
				<label>
					<op:translate key="URL_FEED" />
				</label>
				<p class="form-control-plaintext">${form.url}</p>
			</div>
			
			<div
				class="form-group required">
				<form:label path="displayName" cssClass="control-label">
					<op:translate key="NAME_TITLE" />
				</form:label>
				<c:set var="placeholder">
					<op:translate key="DISPLAYNAME_PLACEHOLDER" />
				</c:set>		
				<form:input path="displayName" type="text" cssClass="form-control" cssErrorClass="form-control is-invalid" placeholder="${placeholder}" />
				<form:errors path="displayName" cssClass="invalid-feedback" />
			</div>
	</fieldset>
		
	<div class="form-group">
		<form:label path="visual.upload" class="control-label"><op:translate key="PICTURE_SLIDER" /></form:label>		
		    <div>
			    <!-- Preview -->
			    <c:choose>
			    	<c:when test="${form.visual.updated}">
			        	<!-- Preview -->
			        	<jsp:useBean id="currentDate" class="java.util.Date" />
			        	<portlet:resourceURL id="visualPreview" var="previewUrl"><portlet:param name="ts" value="${currentDate.time}" /></portlet:resourceURL>
			        	<p class="form-control-static">
			        		<img src="${previewUrl}" alt="" class="img-fluid" style="max-height:250px; max-width: 250px;">
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
				
				<div class="d-flex">
					<div class="mr-2">
						<!-- Upload -->
			    		<label class="btn btn-outline-secondary btn-file btn-sm">
			   				<i class="halflings halflings-folder-open"></i>
			   				<span><op:translate key="PICTURE_UPLOAD" /></span>
				      		<form:input type="file" path="visual.upload" data-change-submit="${namespace}-preview"/>
				      	</label>
				     	<input type="submit" name="upload-visual" class="d-none" id="${namespace}-preview">
			         </div>                   
					<div>
				      	<!-- Delete -->
				      	<button type="submit" name="delete-visual" class="btn btn-outline-secondary btn-sm">
				     	 <i class="halflings halflings-trash"></i>
				       		<span class="sr-only"><op:translate key="DELETE" /></span>
				      	</button>
			      	</div>
		     	</div>			
					
			</div>
		</div>	

	<div class="text-right">
		<!-- Cancel -->
	        <!-- Cancel -->
	    <a href="${cancel}" class="btn btn-secondary">
	    	<span><op:translate key="CANCEL"/></span>
	    </a>
		<button type="submit" class="btn btn-primary">
			<op:translate key="MOD_FEED" />
		</button>
		<a href="${delFeed}" class="btn btn-primary"><span><op:translate
					key="DEL_FEED" /></span></a>
	</div>

</form:form>