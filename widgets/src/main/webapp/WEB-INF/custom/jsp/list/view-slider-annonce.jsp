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
					<c:set var="imageURL">
						<ttc:pictureLink document="${doc}" property="annonce:image" />
					</c:set>
					<c:set var="date" value="${doc.properties['dc:issued']}" />
					<ttc:documentLink document="${doc}" var="link" />
					<c:set var="resume" value="${doc.properties['annonce:resume']}" />



					<div class="media">
						<c:if test="${not empty imageURL}">
							<div class="media-left">
								<img src="${imageURL}" alt="" class="media-object">
							</div>
						</c:if>
					
						<div class="media-body">
							<!-- Title -->
							<h3 class="media-heading">
					            <a href="${link.url}"
					                <c:if test="${link.external}">target="_blank"</c:if>> <span>${doc.title}</span>
					            </a>
					
					            <c:if test="${link.external}">
					                <i class="glyphicons halflings new_window"></i>
					            </c:if>
					            
					
					        </h3>
					        
					        <!-- Resume -->
					    	<p class="lead">${resume}</p>
					        
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
					