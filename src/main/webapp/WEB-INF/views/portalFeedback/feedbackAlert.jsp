<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />
<link
	href="<c:url value="/resources/css/froala/froala_editor.min.css" />"
	rel="stylesheet">
<link
	href="<c:url value="/resources/css/froala/froala_style.min.css" />"
	rel="stylesheet">
<link
	href="<c:url value="/resources/css/froala/froala_content.min.css" />"
	rel="stylesheet">

<link href="<c:url value="/resources/css/froala/themes/dark.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/css/froala/themes/grey.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/css/froala/themes/red.min.css" />"
	rel="stylesheet">
<link
	href="<c:url value="/resources/css/froala/themes/royal.min.css" />"
	rel="stylesheet">
<link href="<c:url value="/resources/css/froala/themes/blue.min.css" />"
	rel="stylesheet">



<%
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
%>
<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>
			<jsp:include page="../common/topHeader.jsp">
				<jsp:param value="Forum" name="activeMenu" />
			</jsp:include>


			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">

							<c:out value="${Program_Name}" />

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />

							</sec:authorize>

							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Portal Feedback
						</div>
						<jsp:include page="../common/alert.jsp" />
						
						<c:if test="${feedbackSuccess}">
							<div class="alert alert-success alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4><strong>Success!</strong> ${feedbackSuccessMessage}</h4>
							</div>
							<!-- <script>
								window.setTimeout(function() { $('.alert-success').alert('close'); }, 60000);
							</script> -->
						</c:if>
						<c:if test="${feedbackNote}">
							<div class="alert alert-warning alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4><strong>Note!</strong> ${feedbackNoteMessage}</h4>
							</div>
							<!-- <script>
								//window.setTimeout(function() { $('.alert-warning').alert('close'); }, 5000);
								
								$('.alert-warning').alert('close');
							</script> -->
						</c:if>
						
						<!-- Input Form Panel -->
						<%-- <div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										
										<h2>Portal Feedback Form</h2>

										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										
										<div class="clearfix"></div>
										
									</div>

									<div class="x_content">
										<form:form action="createPortalFeedbackResponse" method="post"
											modelAttribute="portalFeedback" enctype="multipart/form-data">

											<form:input path="id" type="hidden" />
											
											<sec:authorize access="hasRole('ROLE_FACULTY')">
											
												<c:set var="count" value="0" scope="page" />
											
												<c:forEach var="yesNoQuestion" items="${portalFeedback.yesNoQuestionList}"
															varStatus="status">
													<c:set var="count" value="${count + 1}" scope="page"/>
													<c:set var="countRate" value="${countRate + 1}" scope="page"/>
													<div class="row">
														<div class="col-sm-6 col-md-4 col-xs-12 column">
															<div class="form-group">
																<form:input type="hidden"
																		path="yesNoQuestionList[${status.index}].id"
																		value="${yesNoQuestion.id}" />
																<form:label path="yesNoQuestionList[${status.index}].question" for="">${count}. ${yesNoQuestion.question}</form:label>
																<br>
																<form:select path="yesNoQuestionList[${status.index}].portalFeedbackResponse.answer"
																	required="required" class="form-control">
																		<form:option value="Yes">Yes</form:option>
																		<form:option value="No">No</form:option>
																</form:select>
															</div>
														</div>
													</div>
												</c:forEach>
												
												<c:set var="count" value="${count + 1}" scope="page"/>
												<label>${count}. Rate your experience on the following activities: (Scale of 1 to 5, 5 being the highest)</label>
												
												<br/>
												<c:forEach var="ratingQuestion" items="${portalFeedback.ratingQuestionList}"
															varStatus="loop">
													<c:set var="countRate" value="${countRate + 1}" scope="page"/>
													<div class="row">
														<div class="col-sm-12 col-md-12 col-xs-12">
															<div class="col-sm-6 col-md-4 col-xs-12 column">
																<div class="form-group">
																	<form:input type="hidden"
																		path="ratingQuestionList[${loop.index}].id"
																		value="${ratingQuestion.id}" />
																	<form:label path="ratingQuestionList[${loop.index}].question" for="">&#${loop.index + 97}) ${ratingQuestion.question}</form:label>
																	<br>
																	<form:select path="ratingQuestionList[${loop.index}].portalFeedbackResponse.answer"
																	required="required" class="form-control">
																		<form:option value="5">5</form:option>
																		<form:option value="4">4</form:option>
																		<form:option value="3">3</form:option>
																		<form:option value="2">2</form:option>
																		<form:option value="1">1</form:option>
																	</form:select>
																</div>
															</div>
														</div>
													</div>
												</c:forEach>
												
												<c:set var="count" value="${count + 1}" scope="page"/>
												<c:forEach var="commentQuestion" items="${portalFeedback.commentQuestionList}"
															varStatus="status">
													<div class="row">
														<div class="col-sm-12 col-md-12 col-xs-12 column">
															<div class="form-group">
																<form:input type="hidden"
																		path="commentQuestionList[${status.index}].id"
																		value="${commentQuestion.id}" />
																<form:label path="commentQuestionList[${status.index}].question" for="">${count}. ${commentQuestion.question}</form:label>
																<form:textarea class="form-group" path="commentQuestionList[${status.index}].portalFeedbackResponse.answer" name="editor1" id="editor1" rows="10" cols="80" />
															</div>
														</div>
													</div>
												</c:forEach>
										
											</sec:authorize>
											

											<div>
												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" class="btn btn-large btn-primary"
															formaction="createPortalFeedbackResponse">SUBMIT FEEDBACK</button>
														<input id="reset" type="reset" class="btn btn-danger">
													</div>
												</div>
											</div>


										</form:form>
									</div>
								</div>
							</div>
						</div> --%>

						<!-- Results Panel -->


					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>





</body>
<!-- <script src="//cdn.ckeditor.com/4.8.0/standard/ckeditor.js"></script>
 
<script>
                // Replace the <textarea id="editor1"> with a CKEditor
                // instance, using default configuration.
                CKEDITOR.replace( 'editor1' );
            </script> -->
            
<script type="text/javascript" src="//cdn.ckeditor.com/4.8.0/standard-all/ckeditor.js"></script>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script>

<script type="text/javascript">
	CKEDITOR.replace( 'editor1' , {
			extraPlugins: 'mathjax',
			mathJaxLib: 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML'
		});
		
</script>

</html>
