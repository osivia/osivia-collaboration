<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>


<%@ page contentType="text/html" isELIgnored="false"%>

<portlet:actionURL name="save" var="saveUrl"/>
<portlet:actionURL name="cancel" var="cancelUrl"/>

<form:form action="${saveUrl}" method="post" enctype="multipart/form-data" modelAttribute="form" cssClass="form-horizontal" role="form">
	<spring:bind path="title">
		<div class="form-group ${status.error ? 'has-error has-feedback' : ''} required">
	    	<label class="col-sm-3 control-label" for="title"><op:translate key="CALENDAR_EDIT_TITLE" /></label>
	    	<div class="col-sm-9 ttc-form-control">
	    		<form:input path="title" cssClass="form-control"/>
	    		<c:if test="${status.error}">
	                <span class="form-control-feedback">
	                    <i class="glyphicons glyphicons-remove"></i>
	                </span>
	            </c:if>
	    		<form:errors path="title" cssClass="help-block" />
	    	</div>
	    </div>
    </spring:bind>
    <spring:bind path="dateGroup.*">
	    <div class="form-group required ${status.error ? 'has-error has-feedback' : ''}">
	    	<label class="col-sm-3 control-label" for="dateGroup.startDate"><op:translate key="CALENDAR_EDIT_DATE" /></label>
		    <div id="datepairGroup" class="col-sm-9">
		    	<div class="date-field">
		    		<form:input type="text" size="15" path="dateGroup.startDate" cssClass="form-control date start datepicker" />
		    		<div class="to-field"><form:errors path="dateGroup.startDate" cssClass="help-block" /></div>
		    	</div>
		    	
		    	<div class="date-field">
		    		<form:input type="text" size="12" path="dateGroup.startTime" cssClass="form-control time start ui-timepicker-input timepicker startTime" />
		    		<div class="to-field"><form:errors path="dateGroup.startTime" cssClass="help-block" /></div>
		    	</div>
		    	
		    	<div class="form-control-static to-field"><op:translate key="CALENDAR_EDIT_DATE_TO" /></div>
		    	<div class="date-field">
		    		<form:input type="text" size="15" path="dateGroup.endDate" cssClass="form-control date end datepicker" />
		    		<div class="to-field"><form:errors path="dateGroup.endDate" cssClass="help-block" /></div>
		    	</div>
		    	<div class="date-field">
		    		<form:input type="text" size="12" path="dateGroup.endTime" cssClass="form-control time end ui-timepicker-input timepicker endTime" />
		    		<div class="to-field"><form:errors path="dateGroup.endTime" cssClass="help-block" /></div>
		    	</div>
		    </div>
	    </div>
    </spring:bind>
    <div class="form-group">
    	<label class="col-sm-3 control-label"><op:translate key="CALENDAR_EDIT_DATE_OPTIONS" /></label>
	    <p id="hourOptions" class="col-sm-9">
	    	<form:checkbox path="allDay" cssClass="allDay"/><op:translate key="CALENDAR_EDIT_ALLDAY" />
	    	<!--<form:checkbox path="recurrence"/><op:translate key="CALENDAR_EDIT_RECURRENCE" />-->
	    </p>
    </div>
    <div class="form-group">
    	<label class="col-sm-3 control-label" for="location"><op:translate key="CALENDAR_EDIT_LOCATION" /></label>
    	<div class="col-sm-9 ttc-form-control">
    		<form:input path="location" cssClass="form-control"/>
    	</div>
    </div>
    <div class="form-group">
    	<label class="col-sm-3 control-label" for="description"><op:translate key="CALENDAR_EDIT_DESCRIPTION" /></label>
    	<div class="col-sm-9 ttc-form-control">
    		<form:textarea path="description" cssClass="form-control" rows="3"/>
    	</div>
    </div>
    <div class="form-group">
    	<label class="col-sm-3 control-label"><op:translate key="CALENDAR_EDIT_COLOR" /></label>
    	<div class="col-sm-9 ttc-form-control" style="display:flex">
        	<button type="button" id="selectedColor" onclick="selectColor(this,'${form.bckgColor}');" class="color-box glyphicons glyphicons-ok background-color-selected" style="background-color: #${form.bckgColor}; border-color: #${form.bckgColor};" ></button>
        	<div class="separator" ></div>
        	
        	<button type="button" onclick="selectColor(this,'5484ED');" class="color-box" style="background-color: #5484ED; border-color: #5484ED;" ></button>
        	<button type="button" onclick="selectColor(this,'A4BDFC');" class="color-box" style="background-color: #A4BDFC; border-color: #A4BDFC;" ></button>
        	<button type="button" onclick="selectColor(this,'46D600');" class="color-box" style="background-color: #46D600; border-color: #46D600;" ></button>
        	<button type="button" onclick="selectColor(this,'7AE7BF');" class="color-box" style="background-color: #7AE7BF; border-color: #7AE7BF;" ></button>
        	<button type="button" onclick="selectColor(this,'51b749');" class="color-box" style="background-color: #51b749; border-color: #51b749;" ></button>
        	<button type="button" onclick="selectColor(this,'fbd75b');" class="color-box" style="background-color: #fbd75b; border-color: #fbd75b;" ></button>
        	<button type="button" onclick="selectColor(this,'ffb878');" class="color-box" style="background-color: #ffb878; border-color: #ffb878;" ></button>
        	<button type="button" onclick="selectColor(this,'FF887C');" class="color-box" style="background-color: #FF887C; border-color: #FF887C;" ></button>
        	<button type="button" onclick="selectColor(this,'dc2127');" class="color-box" style="background-color: #dc2127; border-color: #dc2127;" ></button>
        	<button type="button" onclick="selectColor(this,'dbadff');" class="color-box" style="background-color: #dbadff; border-color: #dbadff;" ></button>
        	<button type="button" onclick="selectColor(this,'e1e1e1');" class="color-box" style="background-color: #e1e1e1; border-color: #e1e1e1;" ></button>
        </div>
        <form:hidden path="bckgColor"/>
    </div>
    <div class="row">
	    <div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">
	        <!-- Save -->
	        <button type="submit" name="save" class="btn btn-primary">
	            <i class="glyphicons glyphicons-floppy-disk"></i>
	            <span><op:translate key="SAVE"/></span>
	        </button>
	
	        <!-- Cancel -->
	        <a href="${cancelUrl}" class="btn btn-secondary">
	            <span><op:translate key="CANCEL"/></span>
	        </a>
	    </div>
	</div>
</form:form>