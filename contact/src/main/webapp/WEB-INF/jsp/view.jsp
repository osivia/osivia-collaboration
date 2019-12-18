<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>

<portlet:defineObjects />
<jsp:useBean id="currentdate" class="java.util.Date"/>


<portlet:actionURL var="submitUrl" name="submit"/>
<portlet:resourceURL var="captchaURL" id="captcha">
	<portlet:param name="ts" value="${currentdate.time}"/>
</portlet:resourceURL>

<c:choose>
    <c:when test="${form.sent}">
        <op:translate key="MAIL_SENT"/>
    </c:when>
    <c:otherwise>
            <form:form modelAttribute="form" action="${submitUrl}" method="POST">
    			
    			<spring:bind path="from">
					<div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">    			
	        			<form:label path="from" cssClass="control-label">${form.fromLabel}</form:label>
          				<form:input type="email" path="from" cssClass="form-control" placeholder="exemple@mail.com" />
            			<form:errors path="from" cssClass="help-block" />
      				</div>
				</spring:bind>
        		
        		<spring:bind path="nom">
    				<div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
        				<form:label path="nom" cssClass="control-label">${form.nomLabel}</form:label>
          				<form:input path="nom" cssClass="form-control"/>
            			<form:errors path="nom" cssClass="help-block" />
	  				</div>      				  
				</spring:bind>
				
	        	<spring:bind path="object">
	    			<div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
	        			<form:label path="object" cssClass="control-label">${form.objectLabel}</form:label>
          				<form:input path="object" cssClass="form-control"/>
           				<form:errors path="object" cssClass="help-block" />
	  				</div>
				</spring:bind>			

	        	<spring:bind path="body">
	    			<div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
	        			<form:label path="body" cssClass="control-label">${form.bodyLabel}</form:label>
          			  	<form:textarea path="body" cssClass="form-control tinymce tinymce-simple"/>
            			<form:errors path="body" cssClass="help-block" />
	      			</div>      				  
				</spring:bind>
               
               <c:if test="${not form.captchaValidate}">
		        	<spring:bind path="captcha">
		    			<div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
		        			<form:label path="captcha" cssClass="control-label"><op:translate key="CAPTCHA"/></form:label>
	        				<div class="row">
	        				<div class="col-sm-6">
		        				<div class="thumbnail">
		        					<img alt="captcha image" src="${captchaURL}">
		        				</div>
	        				</div>
	        				<div class="col-sm-6">
	        				<button type="button" onclick="$JQry('#reload').click()" class="btn btn-default">
	        					<i class="glyphicons glyphicons-refresh"></i>
	        				</button>
	        				</div>

	        				</div>
       						<form:input class="form-control" path="captcha" type="text" autocomplete="off" placeholder="Veuillez saisir le code" />	
							<form:errors path="captcha" cssClass="help-block" />				
		      			</div>      				  
					</spring:bind>                
            	</c:if>
                                
                <div>
					<button type="submit" name="send" class="btn btn-primary"><op:translate key="SEND"/></button>
                </div>
                <input type="submit" class="hidden" name="reload" id="reload">
        </form:form>
    </c:otherwise>
</c:choose>