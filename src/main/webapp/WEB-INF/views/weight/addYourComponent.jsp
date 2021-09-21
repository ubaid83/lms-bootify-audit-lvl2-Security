<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />
<link
	href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css"
	rel="stylesheet">
<script
	src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>




			<jsp:include page="../common/topHeader.jsp" />


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
							<i class="fa fa-angle-right"></i> Add Component
						</div>
						<jsp:include page="../common/alert.jsp" />



						<!-- Results Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<%-- <h2>Students | ${fn:length(students)} Records found</h2> --%>
										<h2>Students | ${fn:length(students)} Records found |
											${courseName}</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_itemCount" style="display: none;">
										<div class="image_not_found">
											<i class="fa fa-newspaper-o"></i>
											<p>
												<label class=""></label>${fn:length(students)} Students
											</p>
										</div>
									</div>
									<div class="x_content">
										<form:form action="saveStudentComponent" method="post"
											modelAttribute="comp">
											<form:input path="courseId" type="hidden" />
											<form:input path="compName" type="hidden" />
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="compName" for="compName">Component Name</form:label>
														<form:select id="compName" path="compName"
															class="form-control">
															<form:option value="">Select Component</form:option>
															<c:forEach var="comps" items="${weightTypeList}"
																varStatus="status">
																<form:option value="${comps}">${comps}</form:option>
															</c:forEach>
														</form:select>


													</div>
												</div>
											</div>
											<div class="table-responsive">
												<table class="table  table-hover">
													<thead>
														<tr>
															<th>Sr. No.</th>


															<th>SAP IDs <i class="fa fa-sort" aria-hidden="true"
																style="cursor: pointer"></i></th>
															<th>Student Name <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Roll No. <i class="fa fa-sort"
																aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Assign Score <span style="color: red">*</span></th>
															<th>Assign Remarks <span style="color: red">*</span></th>
															<!-- 	<th>Action</th> -->
														</tr>
													</thead>
													<tfoot>
														<tr>
															<th></th>

															<th>SAP IDs</th>
															<th>Roll No.</th>
															<th>Student Name</th>
														</tr>
													</tfoot>
													<tbody>

														<c:forEach var="student" items="${students}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>


																<td><c:out value="${student.username}" /></td>
																<td><c:out
																		value="${student.firstname} ${student.lastname}" /></td>
																<td><c:out value="${student.rollNo}" /></td>
																<td><form:input path="score" class="form-control"
																		id="saveScore${student.username}"
																		value="${student.score}" type="number" min="0"
																		max="${cpWieghtage}" required="required" /></td>
																<td><form:input path="remarks"
																		id="saveRemarks${student.username}"
																		class="form-control" value="${student.remarks}"
																		type="text" required="required" /></td>
																<%-- <td><a href="#" id="like${student.username}"
																		class="likeClass"><i
																			class="fa fa-check-square fa-lg"></i></a></td> --%>

															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>
											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-group">

													<button id="submit" class="btn btn-large btn-primary"
														onclick="return clicked();"
														formaction="saveStudentComponent">Submit</button>


													<button id="cancel" class="btn btn-large btn-danger"
														formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
												</div>
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>

					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />
			<script>
				$("#courseIdForForum")
						.on(
								'change',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									window.location = '${pageContext.request.contextPath}/classParticipation?courseId='
											+ encodeURIComponent(selectedValue);
									return false;
								});

				$(".likeClass")
						.click(
								function() {
									$('#likeClass').click(function() {
										change(1);
									});
									console
											.log("called ........................................................000000.");

									var likeId = $(this).attr("id");

									var username = likeId.substr(4);
									console.log(username);

									var str1 = "saveScore".concat(username);
									console.log("str1" + str1);
									var score = $('#' + str1).val();
									console.log("Score is " + score);

									var str2 = "saveRemarks".concat(username);
									console.log("str2" + str2);
									var remarks = $('#' + str2).val();
									console.log("Remarks " + remarks);

									var courseid = $
									{
										courseId
									}
									;
									//var maxScore = ${cpWieghtage};

									//	alert(courseid);
									//alert('Course Id is '+courseid);

									//alert(score);
									//alert(remarks);

									//if ((score > 0) && (score<=maxScore)) {
									if ((score > 0)) {

										$
												.ajax({
													type : 'GET',
													url : '${pageContext.request.contextPath}/saveClassParticipation?'
															+ 'studentUsername='
															+ username
															+ '&score='
															+ score
															+ '&remarks='
															+ remarks
															+ '&courseId='
															+ courseid,

													success : function(data) {
														console
																.log("sucess messsgae e like "
																		+ likeId)
														alert("Marks Saved!");
													}

												});
									} else {
										alert("Marks should be greater than zero and less than or equal to assigned weight!");
									}

								});
			</script>


		</div>
	</div>
</body>
</html>

