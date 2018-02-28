<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ page isELIgnored="false"%>


<script src="/osivia-services-widgets/js/bxslider-fragment-integration.js"></script>


<div class="bxslider-container">
    <ul class="list-unstyled bxfgtSlider clearfix" data-timer="${timer}">
        <c:forEach var="document" items="${documents}" varStatus="status">
            <!-- Document properties -->
            <c:set var="imageUrl"><ttc:pictureLink document="${document}" property="annonce:image" /></c:set>
            <c:set var="publicationDate"><fmt:formatDate value="${document.properties['ttc:publicationDate']}" type="date" dateStyle="long" /></c:set>
            <c:set var="resume"><ttc:transform document="${document}" property="annonce:resume"/></c:set>


            <li class="bxslider-slide">
                <article>
                    <div class="row">
                        <c:if test="${not empty imageUrl}">
                            <div class="col-md-4 col-lg-3">
                                <!-- Image -->
                                <img src="${imageUrl}" alt="" class="img-responsive pull-right">
                            </div>
                        </c:if>
                        
                        <div
                            <c:choose>
                                <c:when test="${not empty imageUrl}">class="col-md-8 col-lg-9"</c:when>
                                <c:otherwise>class="col-md-12"</c:otherwise>
                            </c:choose>
                        >
                            <!-- Title -->
                            <h3 class="h4">
                                <span><ttc:title document="${document}" /></span>
                            </h3>
                            
                            <c:if test="${not empty publicationDate}">
	                            <!-- Date -->
	                            <p class="text-muted">
	                                <span>${publicationDate}</span>
	
	                            </p>
                            </c:if>
                        
                            <!-- Resume -->
                            <div>${resume}</div>
                        </div>
                    </div>
				</article>
			</li>
		</c:forEach>
	</ul>
</div>
					