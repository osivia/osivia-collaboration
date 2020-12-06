<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false"%>


<c:set var="url"><ttc:documentLink document="${document}" displayContext="download" /></c:set>
<c:set var="name" value="${document.properties['file:content']['name']}" />
<c:set var="size" value="${document.properties['file:content']['length']}" />
<c:set var="mimeType" value="${document.properties['file:content']['mime-type']}" />


<div class="audio d-flex my-auto">
    <!-- Audio player -->
    <c:choose>
        <c:when test="${(mimeType eq 'audio/mpeg') or (mimeType eq 'audio/ogg') or (mimeType eq 'audio/wav')}">
            <div class="mx-auto d-flex p-2">
                <audio src="${url}" controls="controls" preload="metadata">
                    <source src="${url}" type="${mimeType}">
                </audio>
            </div>
        </c:when>
        
        <c:otherwise>
            <div class="alert alert-info">
                <i class="glyphicons glyphicons-info-sign"></i>
                <span><op:translate key="MESSAGE_AUDIO_CANNOT_BE_PLAYED" /></span>
            </div>
        </c:otherwise>
    </c:choose>
</div>
