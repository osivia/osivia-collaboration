<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>


<portlet:defineObjects />


<portlet:actionURL var="envoiMail"  />

<c:choose>
    <c:when test="${form.sent}">
        <op:translate key="MAIL_SENT"/>
    </c:when>
    <c:otherwise>
        <form:form modelAttribute="form" action="${envoiMail}" method="POST">
                <div class="form-group col-sm-12 <c:if test="${form.fromError}">has-error</c:if>">
                    <label class="control-label" name="from">${form.fromLabel}<span class="text-danger">*</span> : </label>
                    <input type="email" class="form-control" placeholder="exemple@mail.com" name="from">
                </div>
                
                <div class="form-group col-sm-12 <c:if test="${form.nomError}">has-error</c:if>">
                    <form:label path="nom" cssClass="control-label">${form.nomLabel}<span class="text-danger">*</span> : </form:label>
                    <form:input path="nom" cssClass="form-control" />
                </div>
            
                <div class="form-group col-sm-12 <c:if test="${form.objectError}">has-error</c:if>">
                    <form:label path="object" cssClass="control-label">${form.objectLabel}<span class="text-danger">*</span> : </form:label>
                    <form:input path="object" cssClass="form-control" />
                </div>
            
                <div class="form-group col-sm-12 <c:if test="${form.bodyError}">has-error</c:if>">
                    <form:label path="body" cssClass="control-label">${form.bodyLabel}<span class="text-danger">*</span> : </form:label>
                    <form:textarea path="body" cssClass="form-control"/>
                </div>
                
                <div class="col-sm-12">
                    <input type="submit" value="Envoyer" class="btn btn-default"/>
                </div>
        </form:form>
    </c:otherwise>
</c:choose>