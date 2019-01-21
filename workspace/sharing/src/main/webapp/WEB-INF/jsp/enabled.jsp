<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="save" var="url" />


<c:set var="namespace"><portlet:namespace /></c:set>


<form:form action="${url}" method="post" modelAttribute="form" role="form">
    <div class="form-group">
        <div class="clearfix">
            <div class="pull-left">
                <p class="form-control-static"><op:translate key="ENABLED_SHARING_HELP_MESSAGE" /></p>
            </div>
            
            <div class="pull-right">
                <button type="button" class="btn btn-default" data-toggle="collapse" data-target="#${namespace}-disable-confirmation">
                    <span><op:translate key="DISABLE_SHARING" /></span>
                </button>
            </div>
        </div>
    </div>
    
    <div id="${namespace}-disable-confirmation" class="collapse">
        <div class="panel panel-danger">
            <div class="panel-body">
                <p class="text-danger">
                    <span class="text-pre-wrap"><op:translate key="DISABLE_SHARING_CONFIRMATION_MESSAGE" /></span>
                </p>
                
                <div>
                    <button type="submit" name="disable" class="btn btn-danger">
                        <span><op:translate key="CONFIRM" /></span>
                    </button>
                    
                    <button type="button" class="btn btn-default" data-toggle="collapse" data-target="#${namespace}-disable-confirmation">
                        <span><op:translate key="CANCEL" /></span>
                    </button>
                </div>
            </div>
        </div>
    </div>
    
    <!-- URL -->
    <div class="form-group">
        <form:label path="link.url" cssClass="control-label"><op:translate key="SHARING_LINK_URL_LABEL" /></form:label>
        <form:input path="link.url" readonly="true" cssClass="form-control" />
    </div>
    
    <!-- Permission -->
    <div class="form-group">
        <form:label path="link.permission" cssClass="control-label"><op:translate key="SHARING_LINK_PERMISSION_LABEL" /></form:label>
        <form:select path="link.permission" cssClass="form-control">
            <c:forEach var="permission" items="${permissions}">
                <form:option value="${permission.id}"><op:translate key="${permission.key}" /></form:option>
            </c:forEach>
        </form:select>
    </div>
</form:form>