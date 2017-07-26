<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<%@ page contentType="text/html" isELIgnored="false" %>


<portlet:actionURL name="save" var="saveUrl"/>


<div class="forum-edition">
    <form:form action="${saveUrl}" method="post" enctype="multipart/form-data" modelAttribute="form" cssClass="form-horizontal" role="form">
        <div class="row">
            <div class="col-lg-8">
                <!-- Title -->
                <%@ include file="title.jspf" %>

                <!-- Description -->
                <%@ include file="description.jspf" %>
            </div>

            <div class="col-lg-4">
                <!-- Vignette -->
                <%@ include file="vignette.jspf" %>
            </div>
        </div>

        <!-- Buttons -->
        <%@ include file="buttons.jspf" %>
    </form:form>
</div>
