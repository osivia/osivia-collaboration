<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page isELIgnored="false"%>


<script src="/osivia-services-widgets/js/bxslider-fragment-integration.js"></script>


<div class="bxslider-container">
    <ul class="list-unstyled bxfgtSlider clearfix" data-timer="${timer}">
        <c:forEach var="document" items="${documents}" varStatus="status">
            <li class="bxslider-slide">
                <article class="clearfix">
                	<!-- To use in included jsp -->
                	<c:set var="doc" value="${document}" scope="request" />
					<c:set var="description" value="${doc.properties['dc:description']}" />
					<c:set var="thumbnailURL"><ttc:pictureLink document="${doc}" property="ttc:vignette" /></c:set>
					<c:set var="date" value="${doc.properties['dc:issued']}" />
					<c:if test="${empty date}">
						<c:set var="date" value="${doc.properties['dc:modified']}" />
					</c:if>
					<c:if test="${empty date}">
						<c:set var="date" value="${doc.properties['dc:created']}" />
					</c:if>
					
					<div class="media">
						<c:if test="${not empty thumbnailURL}">
							<div class="media-left">
								<img src="${thumbnailURL}" alt="" class="media-object">
							</div>
						</c:if>
					
						<div class="media-body">
							<!-- Title -->
							<h3 class="media-heading">
                                <ttc:title document="${doc}" />
							</h3>
							
					        <!-- Resume -->
					    	<p class="lead">${description}</p>		
					
							<!-- Date -->
							<p class="text-muted">
								<fmt:formatDate value="${date}" type="date" dateStyle="long" />
							</p>
					
						</div>
					</div>                	
                	
                	
                </article>
            </li>
        </c:forEach>
    </ul>
</div>
