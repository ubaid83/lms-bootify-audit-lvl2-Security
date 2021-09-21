<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>




<jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex dataTableBottom tabPage paddingFix1" id="facultyAssignmentPage">
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
                        
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                
                                <c:if test="${UserRole=='ROLE_STUDENT'}">
								<li>
								<c:out value="${AcadSession}" />
								</li>
							</c:if>
                                
                                <li class="breadcrumb-item active" aria-current="page"> Reports</li>
                            </ol>
                        </nav>

						<jsp:include page="../common/alert.jsp" />

						<div class="card bg-white border">
							<div class="card-body">
										<h5 class="text-center border-bottom pb-2">Reports</h5>

										<form:form action="createReportsForm" method="post"
											modelAttribute="charts" enctype="multipart/form-data">


											<form:input path="courseId" type="hidden" />
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="courseId" for="courseId">Course:</form:label>
														<form:select id="courseIdForForum" path="courseId"
															class="form-control">
															<form:option value="">Select Course</form:option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>
											</div>
										</form:form>
							</div>
						</div>

						<div class="card bg-white border">
							<div class="card-body">
							
							
							
                            <nav aria-label="breadcrumb">
                                <ol class="breadcrumb">
                                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                    <li class="breadcrumb-item active" aria-current="page">Attendance</li>
                                </ol>
                            </nav>

                            <section class="searchAssign">
                                <ul class="row nav nav-tabs text-center" id="myTab" role="tablist">
                                    <li class="nav-item col-lg-6 col-md-6 col-sm-12 mt-3 link-one">
                                        <a class="nav-link active" id="summaryReport" data-toggle="tab" href="#login-block" role="tab">
                                            <div class="border hoverDiv">
                                                <div class="row asset1">
                                                    <div class="col-12 ">
                                                        <i class="fas fa-file-signature fa-2x pb-2 text-white"></i>
                                                    </div>
                                                    <div class="col-12">
                                                        <p>ASSIGNMENTS</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </a>
                                    </li>
                                    <li class="nav-item col-lg-6 col-md-6 col-sm-12 mt-3 link-two" id="link-two">
                                        <a class="nav-link" id="tabularData" data-toggle="tab" href="#register-block" role="tab">
                                            <div class="border hoverDiv">
                                                <div class="row asset1">
                                                    <div class="col-12 ">
                                                        <i class="fas fa-file-alt fa-2x pb-2 text-white"></i>
                                                    </div>
                                                    <div class="col-12">
                                                        <p>TESTS</p>
                                                    </div>
                                                </div>
                                            </div>
                                        </a>
                                    </li>
                                </ul>
                                <div class="tab-content" id="myTabContent">

                                    <div class="tab-pane fade show active" id="login-block" role="tabpanel" aria-labelledby="home-tab">
                                        <div class="monthlySummary mt-5">
											<!-- ASSIGNMENT CONTENT -->
											<div class="login-block-form">

													<form:form action="createReportsForm" method="post"
														modelAttribute="charts" enctype="multipart/form-data">


														<form:input path="courseId" type="hidden" />

														<div class="table-responsive testAssignTable">
															<table class="table table-striped table-hover">
																<thead>
																	<tr>
																		<th>Sr. No.</th>


																		<th>Assignment</th>
																		<%-- <th><a href="create3DStackedBarChartForStudentAssignmentForFaculty?courseId=${courseId}">Download Assignment Report of All Students</a></th> --%>
																	<c:choose>
																			<c:when test="${not empty courseId}">
       																				<th><a
																			href="create3DStackedBarChartForStudentAssignmentForFaculty?courseId=${courseId}">Download
																				Assignment Report of All Students</a></th>
																			</c:when>
																			<c:otherwise>
       																				<th>Select Course to Download Assignment Report of All Students</th>
																			</c:otherwise>
																		</c:choose>
																	</tr>
																</thead>

																<tbody>

																	<c:forEach var="assignment" items="${assignmentList}"
																		varStatus="status">
																		<tr>
																			<td><c:out value="${status.count}" /></td>

																			<td><c:out value="${assignment.assignmentName}" /></td>

																			<td><a target="_blank"
																				href="createSimpleLineChartForAssignment?courseId=${courseId}&assignmentId=${assignment.id}">
																					View Line chart</a></td>
																					<td><a target="_blank"
																				href="createLineChartForStdDeviationAssignment?courseId=${courseId}&assignmentId=${assignment.id}">
																					View Standard Deviation chart</a></td>
																			<td><a target="_blank"
																				href="create3DBarChartForAssignment?courseId=${courseId}&assignmentId=${assignment.id}">
																					View Bar Chart</a></td>
																			<%-- <td><a target="_blank"
																				href="create3DBarChartForAssignment?courseId=${courseId}&assignmentId=${assignment.id}">Report
																					Pie Chart (Pass/Fail)</a></td> --%>
																		</tr>
																	</c:forEach>
																</tbody>
															</table>
														</div>
													</form:form>
												</div>

										</div>
                                    </div>

                                    <div class="tab-pane fade" id="register-block" role="tabpanel" aria-labelledby="profile-tab">
                                        <div class="monthlySummary mt-5">
                                            <!-- TEST CONTENT -->
                                            <div class="register-block-form">

													<form:form action="createReportsForm" method="post"
														modelAttribute="charts" enctype="multipart/form-data">


														<form:input path="courseId" type="hidden" />

														<div class="table-responsive testAssignTable">
															<table class="table table-striped table-hover">
																<thead>
																	<tr>
																		<th>Sr. No.</th>


																		<th>Tests</th>
																		<%-- <th><a href="create3DStackedBarChartForStudentTestForFaculty?courseId=${courseId}">Download Test Report of All Students</a></th> --%>
																	<c:choose>
																			<c:when test="${not empty courseId}">
       																				<th><a
																				href="create3DStackedBarChartForStudentTestForFaculty?courseId=${courseId}">Download
																					Test Report of All Students</a></th>
																			</c:when>
																			<c:otherwise>
       																				<th>Select Course to Download Test Report of All Students</th>
																			</c:otherwise>
																		</c:choose>
																	</tr>
																</thead>

																<tbody>

																	<c:forEach var="test" items="${testList}"
																		varStatus="status">
																		<tr>
																			<td><c:out value="${status.count}" /></td>

																			<td><c:out value="${test.testName}" /></td>

																			<td><a target="_blank"
																				href="createSimpleLineChartForTest?courseId=${courseId}&testId=${test.id}">
																					View Line chart</a></td>
																			<td><a target="_blank"
																				href="create3DBarChartForTest?courseId=${courseId}&testId=${test.id}">
																					View Bar Chart</a></td>
																					<td><a target="_blank"
																				href="createLineChartForStdDeviationTest?courseId=${courseId}&testId=${test.id}">
																					View Standard Deviation Chart</a></td>
																			<td><a target="_blank"
																				href="create3DPieChartForCourseTest?courseId=${courseId}&testId=${test.id}">View
																					Pie Chart (Pass/Fail)</a></td>
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
                            </section>


										
							</div>
						</div>
			<!-- /page content -->
                      
                   
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>




			<script>

				$("#courseIdForForum")
						.on(
								'change',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									window.location = '${pageContext.request.contextPath}/createReportsForm?courseId='
											+ encodeURIComponent(selectedValue);
									return false;
								});
			</script>
		