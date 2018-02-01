<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/portlet_2_0" prefix="portlet"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.osivia.org/jsp/taglib/osivia-portal" prefix="op"%>
<%@ taglib uri="http://www.toutatice.fr/jsp/taglib/toutatice" prefix="ttc"%>

<%@ page contentType="text/html" isELIgnored="false"%>

<portlet:defineObjects />

<portlet:actionURL name="save" var="saveUrl" />
<portlet:resourceURL id="search" var="searchUrl" />

<div class="pins-management">
	<form:form action="${saveUrl}" method="post" enctype="multipart/form-data" modelAttribute="form" cssClass="form-horizontal" role="form">

		<c:choose>
			<c:when test="${!form.canPin}">
				<span class="text-muted"><op:translate key="WORKSPACE_CANNOT_PIN_DOCUMENT" /></span>
			</c:when>
			<c:otherwise>

				<spring:bind path="listPins">
					<div class="form-group ${status.error ? 'has-error' : ''}">
						<input type="submit" name="sort" class="hidden"> <label class="col-sm-3 col-lg-2 control-label"><op:translate key="WORKSPACE_PINS" /></label>
						<div class="col-sm-9 col-lg-10">

							<p class="text-muted form-control-static">
								<op:translate key="WORKSPACE_PINS_HELP" />
							</p>
							<ul class="list-sortable pins-edition-sortable active-pins">
								<c:forEach var="pinned" items="${form.listPins}" varStatus="varStatus">
									<li>
										<div class="media ${pinned.inBin ? 'font-italic' :''}">
											<form:hidden path="listPins[${varStatus.index}].inBin" />
											<form:hidden path="listPins[${varStatus.index}].deleted" />
											<form:hidden path="listPins[${varStatus.index}].order" />

											<div class="media-left">
												<i class="${pinned.icon}"></i>
											</div>

											<div class="media-body">
												<c:choose>
													<c:when test="${pinned.inBin}">
														<span class="text-muted">${pinned.title}</span>
														<span class="text-muted text-italic"><op:translate key="DOCUMENT_IN_BIN" /></span>
													</c:when>
													<c:otherwise>
														<span>${pinned.title}</span>
													</c:otherwise>
												</c:choose>
											</div>

											<div class="media-right">
												<portlet:actionURL name="delete" var="url">
													<portlet:param name="index" value="${varStatus.index}" />
												</portlet:actionURL>

												<a href="${url}" class="small"> <span><op:translate key="WORKSPACE_DELETE_PIN" /></span>
												</a>
											</div>
										</div>
									</li>
								</c:forEach>
							</ul>

							<form:errors path="listPins" cssClass="help-block" />
						</div>
					</div>
				</spring:bind>

				<div class="form-group">
					<c:set var="noResults">
						<op:translate key="SELECT2_NO_RESULTS" />
					</c:set>
					<c:set var="searching">
						<op:translate key="SELECT2_SEARCHING" />
					</c:set>
					<c:set var="loadingMore">
						<op:translate key="SELECT2_LOADING_MORE" />
					</c:set>
					<c:set var="placeholder">
						<op:translate key="PIN_SEARCH_DOCUMENT_PLACEHOLDER" />
					</c:set>
					<label class="col-sm-3 col-lg-2 control-label"> <op:translate key="ADD_DOCUMENTS_TO_PIN" />
					</label>
					<spring:bind path="documentWebId">
						<div class="col-sm-9 col-lg-10 ${status.error ? 'has-error' : ''}">
							<p class="text-muted form-control-static">
								<op:translate key="HELP_ADD_DOCUMENTS_TO_PIN" />
							</p>
							<div class="input-group">
								<form:select path="documentWebId" cssClass="form-control select2" data-placeholder="${placeholder}" data-url="${searchUrl}"
									data-no-results="${noResults}" data-searching="${searching}" data-loading-more="${loadingMore}">
									<c:if test="${not empty form.document}">
										<form:option value="${form.document.properties['ttc:webid']}" data-icon="${form.document.icon}">${form.document.title}</form:option>
									</c:if>
								</form:select>
							</div>

							<form:errors path="documentWebId" cssClass="help-block" />

							<input type="submit" name="addPin" class="hidden" />

						</div>
					</spring:bind>
				</div>

				<!-- Buttons -->

				<div class="form-group">
					<div class="col-sm-offset-3 col-sm-9 col-lg-offset-2 col-lg-10">

						<form:hidden path="toSave" cssClass="warning-to-save" />

						<div class="group-save-warning collapse ${form.toSave? 'in' : ''}">
							<div class="alert alert-warning">
								<i class="glyphicons glyphicons-alert"></i> <span><op:translate key="WORKSPACE_PINS_SAVE_MESSAGE" /></span>
							</div>
						</div>
						<!-- Save -->
						<button type="submit" name="save" class="btn btn-primary">
							<i class="glyphicons glyphicons-floppy-disk"></i> <span><op:translate key="SAVE" /></span>
						</button>

						<!-- Cancel -->
						<button type="submit" name="cancel" class="btn btn-default">
							<span><op:translate key="CANCEL" /></span>
						</button>
					</div>
				</div>
			</c:otherwise>
		</c:choose>

	</form:form>
</div>