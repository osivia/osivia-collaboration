<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:actionURL name="add" var="addUrl">
    <portlet:param name="sort" value="${sort}" />
    <portlet:param name="alt" value="${alt}" />
</portlet:actionURL>


<div class="panel panel-default">
    <div class="panel-body">
        <form:form action="${addUrl}" method="post" modelAttribute="addForm" role="form">
            <fieldset>
                <legend><op:translate key="ADD_ACL_ENTRIES_LEGEND" /></legend>
            
                <div class="row">
                    <div class="col-sm-8">
                        <!-- ACL entry identifiers selector -->
                        <c:set var="placeholder"><op:translate key="ADD_ACL_ENTRIES_PLACEHOLDER" /></c:set>
                        <c:set var="noResults"><op:translate key="SELECT2_NO_RESULTS" /></c:set>
                        <spring:bind path="identifiers">
                            <div class="form-group ${status.error ? 'has-error' : ''}">
                                <form:label path="identifiers" cssClass="control-label"><op:translate key="ADD_ACL_ENTRIES_LABEL" /></form:label>
                                <form:select path="identifiers" cssClass="form-control select2" multiple="multiple" data-placeholder="${placeholder}" data-no-results="${noResults}">
                                    <c:forEach var="record" items="${addForm.records}">
                                        <option value="${record.id}" data-type="${record.type}" data-displayname="${record.displayName}" data-avatar="${record.avatar}" data-extra="${record.extra}">${record.displayName} - ${record.id} - ${record.extra}</option>
                                    </c:forEach>
                                </form:select>
                                <form:errors path="identifiers" cssClass="help-block" />
                            </div>
                        </spring:bind>
                    </div>
                    
                    <div class="col-sm-4">
                        <!-- Role -->
                        <div class="form-group">
                            <form:label path="role" cssClass="control-label"><op:translate key="ADD_ACL_ROLE_LABEL" /></form:label>
                            <form:select path="role" cssClass="form-control">
                                <c:forEach var="role" items="${roles}">
                                    <form:option value="${role.id}">${role.displayName}</form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                </div>
                
                <!-- Buttons -->
                <spring:bind path="*">
                    <div class="collapse ${status.error ? 'in' : ''}">
                        <!-- Save -->
                        <button type="submit" name="save" class="btn btn-primary">
                            <i class="glyphicons glyphicons-floppy-disk"></i>
                            <span><op:translate key="SAVE" /></span>
                        </button>
                        
                        <!-- Cancel -->
                        <button type="submit" name="cancel" class="btn btn-default">
                            <span><op:translate key="CANCEL" /></span>
                        </button>
                    </div>
                </spring:bind>
            </fieldset>
        </form:form>
    </div>
</div>
