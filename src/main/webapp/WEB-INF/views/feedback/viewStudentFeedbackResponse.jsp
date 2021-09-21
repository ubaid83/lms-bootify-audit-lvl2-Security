<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">





			<jsp:include page="../common/topHeader.jsp" />


			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
						
						<c:out value="${Program_Name}" />
						
						<sec:authorize access="hasRole('ROLE_STUDENT')">
						
						<i
						class="fa fa-angle-right"></i>
											
											<c:out value="${AcadSession}" />
											
											</sec:authorize>
						<br><br>
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> View Student Feedback
						</div>
						<jsp:include page="../common/alert.jsp" />

						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Feedback Allocation Details</h2>
										<ul class="nav navbar-right panel_toolbox">


											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>



										</ul>
										<div class="clearfix"></div>

									</div>


									<div class="x_content">

										<form:form action="addFeedback" method="post"
											modelAttribute="feedback">
											<div class="row">
												<div class="col-sm-4 column">

													<div class="form-group">
														<form:label path="feedbackName" for="feedbackName">Feedback Name :</form:label>
														${feedback.feedbackName}
													</div>
												</div>
												<div class="col-sm-4 column">

													<div class="form-group">
														<label>Student Name: </label>
														${feedbackquestion.firstname} ${feedbackquestion.lastname }
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-sm-4 column">


													<div class="form-group">
														<label>SAP ID :</label> ${feedbackquestion.username}
													</div>
												</div>
											</div>
											<div class="table-responsive">
												<table class="table table-striped table-hover"
													id="studentFeedbackTable" style="font-size: 12px">
													<thead>
														<tr>
															<th>Sr. No.</th>


															<th>Question</th>
															<th>Course Name</th>
															<th>Feedback Response</th>
														</tr>
													</thead>

													<tbody>

														<c:forEach var="student" items="${page}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>

																<td><c:out value="${student.description}" /></td>
																<td><c:out value="${student.courseName}" /></td>
																<td><c:out value="${student.answer}" />
 </td>

															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>




					</div>



				</div>

			</div>
			<!-- /page content -->




			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>





</body>
</html>
