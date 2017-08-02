<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>

<%@ page isELIgnored="false"%>

<c:set var="publicationDate" value="${document.properties['ttc:publicationDate']}" />
<c:set var="issued" value="${document.properties['dc:issued']}" /> 

<c:set var="imageUrl"><ttc:pictureLink document="${document}" property="annonce:image" /></c:set>
<c:set var="content"><ttc:transform document="${document}" property="note:note" /></c:set>


<article class="annonce">
    <!-- Title -->
    <h3 class="hidden">${document.title}</h3>
    
    <!-- Date -->
    <p class="text-muted">
	    <c:choose>
	    	<c:when test="${not empty issued}">
	        	<span><op:translate key="DOCUMENT_METADATA_PUBLISHED_ON" /></span>
		        <span>
		        	<fmt:formatDate value="${issued}" type="date" dateStyle="long" />
		        </span>
	    	</c:when>
	    	<c:otherwise>
	    		<c:if test="${not empty publicationDate}">
		        	<span><op:translate key="DOCUMENT_METADATA_PUBLISHED_ON" /></span>
			        <span>
			        	<fmt:formatDate value="${publicationDate}" type="date" dateStyle="long" />
			        </span>    				
	    		</c:if>
	    	</c:otherwise>
	    </c:choose>
    </p>
    
    <div class="row">
        <c:if test="${not empty imageUrl}">
            <div class="col-md-4 col-lg-3">
                <div class="thumbnail">
                    <!-- Image -->
                    <img src="${imageUrl}" alt="" class="img-responsive center-block">
                </div>
            </div>
        </c:if>
        
        <div
            <c:choose>
                <c:when test="${not empty imageUrl}">class="col-md-8 col-lg-9"</c:when>
                <c:otherwise>class="col-md-12"</c:otherwise>
            </c:choose>
        >
            <!-- Content -->
            <div>${content}</div>
        </div>
    </div>
</article>
