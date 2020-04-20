<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false"%>


<c:set var="url"><ttc:documentLink document="${document}" picture="true" /></c:set>
<c:set var="previewUrl"><ttc:documentLink document="${document}" picture="true" displayContext="Medium" /></c:set>


<div class="my-auto picture">
    <!-- Preview -->
    <p><img src="${previewUrl}" alt="${name}" class="img-fluid rounded mx-auto d-block"></p>
</div>
