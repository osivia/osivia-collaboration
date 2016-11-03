<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false"%>


<c:set var="url"><ttc:documentLink document="${document}" picture="true" /></c:set>
<c:set var="previewUrl"><ttc:documentLink document="${document}" picture="true" displayContext="Medium" /></c:set>


<!-- Download menubar item -->
<ttc:addMenubarItem id="DOWNLOAD" labelKey="DOWNLOAD" order="20" url="${url}" glyphicon="glyphicons glyphicons-download-alt" />


<div class="picture">
    <!-- Preview -->
    <p><img src="${previewUrl}" alt="${name}" class="img-responsive"></p>
</div>
