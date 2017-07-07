<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page isELIgnored="false"%>


<div class="bxslider-container">
    <ul class="list-unstyled bxslider bxslider-default clearfix" data-pause="${timer}">
        <c:forEach var="document" items="${documents}" varStatus="status">
            <li class="bxslider-slide">
                <article class="clearfix">
                	<!-- To use in included jsp -->
					<c:set var="description" value="${document.properties['dc:description']}" />
					<c:set var="thumbnailUrl"><ttc:pictureLink document="${document}" property="ttc:vignette" /></c:set>
					<c:set var="date" value="${document.properties['dc:issued']}" />
					<c:if test="${empty date}">
						<c:set var="date" value="${document.properties['dc:modified']}" />
					</c:if>
					<c:if test="${empty date}">
						<c:set var="date" value="${document.properties['dc:created']}" />
					</c:if>
					
					<div class="media">
						<c:if test="${not empty thumbnailUrl}">
							<div class="media-left">
								<img src="${thumbnailUrl}" alt="" class="media-object">
							</div>
						</c:if>
					
						<div class="media-body">
							<!-- Title -->
							<h3 class="h4 media-heading">
                                <span><ttc:title document="${document}" /></span>
							</h3>
							
					        <!-- Resume -->
					    	<p>
                                <span>${description}</span>
                            </p>		
					
							<!-- Date -->
							<p class="text-muted">
								<span><fmt:formatDate value="${date}" type="date" dateStyle="long" /></span>
							</p>
						</div>
					</div>                	
                </article>
            </li>
        </c:forEach>
    </ul>
</div>
