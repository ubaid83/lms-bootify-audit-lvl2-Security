<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<sec:authorize access="hasAnyRole('ROLE_FACULTY', 'ROLE_STUDENT')">;
<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

		<%
			boolean isEdit = "true".equals((String) request
					.getAttribute("edit"));
		%>

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

					<!-- page content: START -->

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /></li>
							<sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
							<li class="breadcrumb-item active" aria-current="page">
								Portal Feedback</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
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

											<%-- <sec:authorize access="hasRole('ROLE_FACULTY')"> --%>

											<c:set var="count" value="0" scope="page" />

											<c:forEach var="yesNoQuestion"
												items="${portalFeedback.yesNoQuestionList}"
												varStatus="status">
												<c:set var="count" value="${count + 1}" scope="page" />
												<c:set var="countRate" value="${countRate + 1}" scope="page" />
												<div class="row">
													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:input type="hidden"
																path="yesNoQuestionList[${status.index}].id"
																value="${yesNoQuestion.id}" />
															<form:label
																path="yesNoQuestionList[${status.index}].question"
																for="">${count}. ${yesNoQuestion.question} <span
																	style="color: red">*</span>
															</form:label>
															<br>
															<form:select
																path="yesNoQuestionList[${status.index}].portalFeedbackResponse.answer"
																required="required" class="form-control">
																<form:option value="Yes">Yes</form:option>
																<form:option value="No">No</form:option>
															</form:select>
														</div>
													</div>
												</div>
											</c:forEach>

											<c:set var="count" value="${count + 1}" scope="page" />
											<label>${count}. Rate your experience on the
												following activities: (Scale of 1 to 5, 5 being the highest)</label>

											<br />
											<c:forEach var="ratingQuestion"
												items="${portalFeedback.ratingQuestionList}"
												varStatus="loop">
												<c:set var="countRate" value="${countRate + 1}" scope="page" />
												<div class="row">
													<div class="col-sm-12 col-md-12 col-xs-12">
														<div class="col-sm-6 col-md-4 col-xs-12 column">
															<div class="form-group">
																<form:input type="hidden"
																	path="ratingQuestionList[${loop.index}].id"
																	value="${ratingQuestion.id}" />
																<form:label
																	path="ratingQuestionList[${loop.index}].question"
																	for="">&#${loop.index + 97}) ${ratingQuestion.question} <span
																		style="color: red">*</span>
																</form:label>
																<br>
																<form:select
																	path="ratingQuestionList[${loop.index}].portalFeedbackResponse.answer"
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

											<c:set var="count" value="${count + 1}" scope="page" />
											<c:forEach var="commentQuestion"
												items="${portalFeedback.commentQuestionList}"
												varStatus="status">
												<div class="row">
													<div class="col-sm-12 col-md-12 col-xs-12 column">
														<div class="form-group">
															<form:input type="hidden"
																path="commentQuestionList[${status.index}].id"
																value="${commentQuestion.id}" />
															<form:label
																path="commentQuestionList[${status.index}].question"
																for="">${count}. ${commentQuestion.question}</form:label>
															<form:textarea class="form-group"
																path="commentQuestionList[${status.index}].portalFeedbackResponse.answer"
																name="editor1" id="editor1" rows="10" cols="80" />
														</div>
													</div>
												</div>
											</c:forEach>

											<%-- </sec:authorize> --%>


											<div>
												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" class="btn btn-large btn-primary"
															formaction="createPortalFeedbackResponse">SUBMIT
															FEEDBACK</button>
														<input id="reset" type="reset" class="btn btn-danger">
													</div>
												</div>
											</div>


										</form:form>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Results Panel -->



					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />


				<script type="text/javascript">
					CKEDITOR
							.replace(
									'editor1',
									{
										extraPlugins : 'mathjax',
										mathJaxLib : 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML'
									});
				</script>
				</sec:authorize>
				
<sec:authorize access="hasRole('ROLE_SUPPORT_ADMIN')">
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
			<jsp:include page="../common/topHeaderLibrian.jsp">
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
						<!-- Input Form Panel -->
						<div class="row">
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
											
											<%-- <sec:authorize access="hasRole('ROLE_FACULTY')"> --%>
											
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
																<form:label path="yesNoQuestionList[${status.index}].question" for="">${count}. ${yesNoQuestion.question} <span style="color: red">*</span></form:label>
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
																	<form:label path="ratingQuestionList[${loop.index}].question" for="">&#${loop.index + 97}) ${ratingQuestion.question} <span style="color: red">*</span></form:label>
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
										
											<%-- </sec:authorize> --%>
											

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
						</div>

						<!-- Results Panel -->


					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

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
</sec:authorize>