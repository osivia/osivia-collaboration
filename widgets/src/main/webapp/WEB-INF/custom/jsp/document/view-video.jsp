<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false"%>


<c:set var="url"><ttc:documentLink document="${document}" displayContext="download" /></c:set>
<c:set var="name" value="${document.properties['file:content']['name']}" />
<c:set var="size" value="${document.properties['file:content']['length']}" />
<c:set var="mimeType" value="${document.properties['file:content']['mime-type']}" />


<div class="video  d-flex my-auto p-2">
    <!-- Video player -->
    <c:choose>
        <c:when test="${(mimeType eq 'video/mp4') or (mimeType eq 'video/webm') or (mimeType eq 'video/ogg')}">
            <div class="mx-auto embed-responsive embed-responsive-16by9">
                <video controls="controls" preload="metadata" class="embed-responsive-item">
                    <source src="${url}" type="${mimeType}">
                    <op:translate key="MESSAGE_VIDEO_CANNOT_BE_PLAYED" />
                </video>
            </div>
        </c:when>
        
        <c:otherwise>
            <div class="alert alert-info">
                <i class="glyphicons glyphicons-info-sign"></i>
                <span><op:translate key="MESSAGE_VIDEO_CANNOT_BE_PLAYED" /></span>
            </div>
        </c:otherwise>
    </c:choose>
</div>
