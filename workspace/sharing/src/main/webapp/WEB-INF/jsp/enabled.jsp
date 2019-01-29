<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="save" var="url" />


<c:set var="namespace"><portlet:namespace /></c:set>


<form:form action="${url}" method="post" modelAttribute="form" role="form">
    <fieldset>
        <legend>
            <span><op:translate key="SHARING_LEGEND" /></span>
        </legend>
    
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
            <form:label for="${namespace}-link-url" path="link.url" cssClass="control-label"><op:translate key="SHARING_LINK_URL_LABEL" /></form:label>
            <div class="input-group">
                <form:input id="${namespace}-link-url" path="link.url" readonly="true" cssClass="form-control" />
                <span class="input-group-btn">
                    <button type="button" class="btn btn-default" data-clipboard-target="#${namespace}-link-url">
                        <i class="glyphicons glyphicons-copy"></i>
                        <span><op:translate key="SHARING_LINK_URL_COPY" /></span>
                    </button>
                </span>
            </div>
        </div>
        
        <!-- Permission -->
        <div class="form-group">
            <form:label path="link.permission" cssClass="control-label"><op:translate key="SHARING_LINK_PERMISSION_LABEL" /></form:label>
            <form:select path="link.permission" cssClass="form-control" onchange="$JQry('#update-sharing-link-permissions').click();">
                <c:forEach var="permission" items="${permissions}">
                    <form:option value="${permission}"><op:translate key="${permission.key}" /></form:option>
                </c:forEach>
            </form:select>
            <input id="update-sharing-link-permissions" type="submit" name="update-permissions" class="hidden">
        </div>
        
        <!-- Users -->
        <div class="form-group">
            <label class="control-label"><op:translate key="SHARING_USERS_LABEL" /></label>
            <c:choose>
                <c:when test="${empty form.users}">
                    <p class="text-muted"><op:translate key="SHARING_NO_USER_MESSAGE" /></p>
                </c:when>
                
                <c:otherwise>
                    <table class="table">
                        <tbody>
                            <c:forEach var="user" items="${form.users}">
                                <portlet:actionURL name="remove-user" var="url">
                                    <portlet:param name="user" value="${user}"/>
                                </portlet:actionURL>
                            
                                <tr>
                                    <td>
                                        <span><ttc:user name="${user}" linkable="false" /></span>
                                    </td>
                                    
                                    <td class="text-right">
                                        <a href="${url}">
                                            <small><op:translate key="REMOVE_USER" /></small>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </fieldset>
</form:form>


<%@ include file="close.jspf" %>
