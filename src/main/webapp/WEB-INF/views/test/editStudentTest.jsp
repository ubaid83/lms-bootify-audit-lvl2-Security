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

<jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newTopHeaderFaculty.jsp" />
     
     <div class="container mt-5">
                <div class="row">
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
          <!-- page content: START -->
          
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Add Subjective Test Question</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							
						</div>
						</div>

						<!-- Results Panel -->

						
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="x_title">
										<h2>Search Test To Update</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="editStudentTest" method="post"
											modelAttribute="test">



											<div class="col-sm-6 col-md-4 col-xs-12 column">
												<div class="form-group">
													<form:label path="courseId" for="courseId">Course</form:label>
													<form:select id="courseId" path="courseId"
														class="form-control">
														<form:option value="">Select Course</form:option>
														<c:forEach var="course" items="${allCourses}"
															varStatus="status">
															<form:option value="${course.id}">${course.courseName}</form:option>
														</c:forEach>
													</form:select>
												</div>
											</div>

											<div class="col-sm-6 col-md-4 col-xs-12 column">
												<div class="form-group">
													<form:label path="id" for="id">Test</form:label>
													<form:select id="testid" path="id" class="form-control">
														<form:option value="">Select Test</form:option>

													</form:select>
												</div>
											</div>












											<%-- <div class="col-sm-6 col-md-4 col-xs-12 column">
					<div class="form-group">
						<form:label path="username" for="username">Student User Name</form:label>
						<form:input path="username" type="text" class="form-control" />
					</div>
				</div --%>


											<div class="col-sm-12 column">
												<div class="form-group">
													<button id="submit" name="submit"
														class="btn btn-large btn-primary">Search</button>
													<input id="reset" type="reset" class="btn btn-danger">
													<button id="cancel" name="cancel" class="btn btn-danger"
														formnovalidate="formnovalidate">Cancel</button>
												</div>
											</div>

										</form:form>
									</div>
								</div>
								</div>
							

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>
			<jsp:include page="../common/topHeader.jsp" />
			<%-- <jsp:include page="../common/alert.jsp" /> --%>


			<!-- page content: START -->
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
						
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a> <i class="fa fa-angle-right"></i>
							Edit Student Test
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									
								</div>
							</div>
						</div>
						<c:choose>
							<c:when test="${page.rowCount > 0}">
								<!-- Results Panel -->
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>Allocated Students | ${page.rowCount} Records Found</h2>
												<ul class="nav navbar-right panel_toolbox">
													<li><a class="collapse-link"><i
															class="fa fa-chevron-up"></i></a></li>
													<li><a class="close-link"><i class="fa fa-close"></i></a></li>
												</ul>
												<div class="clearfix"></div>
											</div>
											<div class="x_content">
												<div class="table-responsive">
													<table class="table table-hover">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Student User Name</th>
																<th>Faculty ID</th>
																<th>Course</th>
																<th>Test Name</th>



																<th>Save MaxScore</th>
																<th>Save Pass Score</th>

																<th>Start Date</th>
																<th>End Date</th>

																<!-- <th>Actions</th> -->
															</tr>
														</thead>
														<tbody>

															<c:forEach var="test" items="${page.pageItems}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${test.username}" /></td>
																	<td><c:out value="${test.createdBy}" /></td>
																	<td><c:out value="${test.course.courseName}" /></td>
																	<td><c:out value="${test.testName}" /></td>

																	<td><a href="#" class="editable" id="maxScore"
																		data-type="text" data-pk="${test.id}"
																		data-url="saveMaxScore" data-title="Enter Score">${test.maxScore}</a>
																	</td>

																	<td><a href="#" class="editable" id="passScore"
																		data-type="textarea" data-pk="${test.id}"
																		data-url="savePassScore" data-title="Enter Score">${test.passScore}</a>

																	</td>

																	<td><a href="#" class="editable" id="date"
																		data-type="date" data-pk="${test.id}"
																		data-url="saveStartDate" data-title="Enter Date">${test.startDate}</a>

																	</td>
																	<td><a href="#" class="editable" id="date"
																		data-type="text" data-pk="${test.id}"
																		data-url="saveEndDate" data-title="Enter Date">${test.endDate}</a></td>


																</tr>
															</c:forEach>

														</tbody>
													</table>
												</div>
											</div>
										</div>
									</div>
								</div>

							</c:when>
						</c:choose>

						<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="editStudentTest" />
						</jsp:include>

					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>

	<script>
		$(document)
				.ready(
						function() {

							$("#courseId")
									.on(
											'change',
											function() {
												var courseid = $(this).val();
												if (courseid) {
													$
															.ajax({
																type : 'GET',
																url : '/getTestByCourse?'
																		+ 'courseId='
																		+ courseid,
																success : function(
																		data) {
																	var json = JSON
																			.parse(data);
																	var optionsAsString = "";

																	$('#testid')
																			.find(
																					'option')
																			.remove();
																	console
																			.log(json);
																	for (var i = 0; i < json.length; i++) {
																		var idjson = json[i];
																		console
																				.log(idjson);

																		for ( var key in idjson) {
																			console
																					.log(key
																							+ ""
																							+ idjson[key]);
																			optionsAsString += "<option value='" +key + "'>"
																					+ idjson[key]
																					+ "</option>";
																		}
																	}
																	console
																			.log("optionsAsString"
																					+ optionsAsString);

																	$('#testid')
																			.append(
																					optionsAsString);

																}
															});
												} else {
													alert('Error no course');
												}

											});

						});
	</script>




</body>
</html>
	