<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false"%>


<c:set var="url"><ttc:documentLink document="${document}" picture="true" /></c:set>
<c:set var="thumbnailUrl"><ttc:documentLink document="${document}" picture="true" displayContext="Medium" /></c:set>
<c:set var="name" value="${document.properties['file:content']['name']}" />
<c:set var="size" value="${document.properties['file:content']['length']}" />
<c:set var="description" value="${document.properties['dc:description']}" />


<!-- Download menubar item -->
<ttc:addMenubarItem id="DOWNLOAD" labelKey="DOWNLOAD" order="20" url="${url}" glyphicon="glyphicons glyphicons-download-alt" />


<div class="picture">
    <!-- Description -->
    <c:if test="${not empty description}">
        <p>${description}</p>
    </c:if>
    
    <p>
        <!-- Title -->
        <i class="${document.type.glyph}"></i>
        <a href="${url}" class="no-ajax-link">${name}</a>
        
        <!-- Size -->
        <span>(<ttc:fileSize size="${size}" />)</span>
    </p>

    <!-- Thumbnail -->
    <img src="${thumbnailUrl}" alt="${name}" class="img-thumbnail">
</div>
