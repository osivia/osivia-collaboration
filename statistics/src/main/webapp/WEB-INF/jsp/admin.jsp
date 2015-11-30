<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page language="java" pageEncoding="UTF-8" isELIgnored="false" %>


<portlet:defineObjects />

<portlet:actionURL name="save" var="url" />


<form:form action="${url}" method="post" modelAttribute="configuration" cssClass="form-horizontal" role="form">
    
    <!-- Periods number -->
    <div class="form-group">
        <form:label path="number" cssClass="col-sm-3 control-label">Nombre de p&eacute;riodes</form:label>
        <div class="col-sm-9">
            <form:input path="number" type="number" cssClass="form-control" />
        </div>
    </div>
    
    <!-- Request -->
    <div class="form-group">
        <form:label path="request" cssClass="col-sm-3 control-label">Requ&ecirc;te</form:label>
        <div class="col-sm-9">
            <form:textarea path="request" rows="10" cssClass="form-control" />
            <div class="help-block">La requ&ecirc;te doit respecter la syntaxe BeanShell.</div>
        </div>
    </div>
    
    <!-- Version -->
    <div class="form-group">
        <label class="col-sm-3 control-label">Version</label>
        <div class="col-sm-9">
            <c:forEach var="version" items="${versions}">
                <div class="radio">
                    <label>
                        <form:radiobutton path="version" value="${version}" />
                        <span>${version.label}</span>
                    </label>
                </div>
            </c:forEach>
        </div>
    </div>
    
    <!-- Buttons -->
    <div class="form-group">
        <div class="col-sm-offset-3 col-sm-9">
            <button type="submit" class="btn btn-primary">
                <i class="glyphicons glyphicons-floppy-disk"></i>
                <span>Enregistrer</span>
            </button>
            
            <button type="button" class="btn btn-default" onclick="closeFancybox()">
                <span>Annuler</span>
            </button>
        </div>
    </div>
    
</form:form>
