<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op" %>

<%@ page contentType="text/html" isELIgnored="false"%>


<portlet:defineObjects />



<c:if test="${not empty questions}">
	<h3>
	    <i class="glyphicons glyphicons-hash"></i>
	    <span>${faq.title}</span>
	    <span>-</span>
	    <span><op:translate key="SUMMARY" /></span>
	</h3>
	    
    <div class="panel-group" role="tablist" aria-multiselectable="true" id="faqAccordion">
        <c:forEach var="summaryQuestion" items="${questions}">
	        <div class="panel panel-default">
	           <div class="panel-heading" role="tab" data-toggle="collapse" data-target="#${summaryQuestion.id}" aria-expanded="false" aria-controls="${summaryQuestion.id}"
                        data-parent="#faqAccordion">
		           <h4 class="panel-title">
				        ${summaryQuestion.title}
		           </h4>
	           </div>
		        <div id="${summaryQuestion.id}" class="panel-collapse collapse" role="tabpanel">
		          <div class="panel-body">
		              ${summaryQuestion.properties['note:note']}
		              <c:if test="${not empty summaryQuestion.attachments}">
					        <hr>
				                <h5 class="panel-title">
				                    <i class="glyphicons glyphicons-paperclip"></i>
				                    <span><op:translate key="ATTACHMENTS" /></span>
				                </h5>
					            <div class="list-group">
					                <c:forEach var="attachment" items="${summaryQuestion.attachments}">
					                    <a href="${attachment.url}" class="list-group-item">
					                        <span>${attachment.name}</span>
					                    </a>
					                </c:forEach>
					            </div>
					    </c:if>
					    <hr>
					    <c:if test="${not empty summaryQuestion.properties['url']}">
                            <a class="pull-right no-ajax-link" role="button"  href="${summaryQuestion.properties['url']}">
                                 <span><op:translate key="DETAIL" /></span>
                            </a>
                        </c:if>
		          </div>
		        </div>
	        </div>
        </c:forEach>
    </div>
</c:if>

<c:if test="${not empty question}">
    ${question.properties['note:note']}
    <c:if test="${not empty question.attachments}">
          <hr>
              <h5 class="panel-title">
                  <i class="glyphicons glyphicons-paperclip"></i>
                  <span><op:translate key="ATTACHMENTS" /></span>
              </h5>
              <div class="list-group">
                  <c:forEach var="attachment" items="${question.attachments}">
                      <a href="${attachment.url}" class="list-group-item">
                          <span>${attachment.name}</span>
                      </a>
                  </c:forEach>
              </div>
      </c:if>
</c:if>
