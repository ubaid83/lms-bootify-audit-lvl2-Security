<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
	
	

    <jsp:include page="../common/newDashboardHeader.jsp" /> 

    <div class="d-flex adminPage tabPage dataTableBottom" id="adminPage">
    <jsp:include page="../common/newAdminLeftNavBar.jsp"/>
    <jsp:include page="../common/rightSidebarAdmin.jsp" />

    

    <!-- DASHBOARD BODY STARTS HERE -->

    <div class="container-fluid m-0 p-0 dashboardWraper">

        <jsp:include page="../common/newAdminTopHeader.jsp" />

                <!-- SEMESTER CONTENT -->    
                        <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
                        
                        <!-- page content: START -->
	
						<nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
                                                <c:out value="${AcadSession}" />
                                          </sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Search Course Enrollments</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="card border bg-white">
							<div class="card-body">
								<div class="x_panel">
										<h5 class="text-center border-bottom pb-2">Search Course Enrollments</h5>


									<div class="x_content">
										<form:form action="searchUserCourse" method="post"
											modelAttribute="userCourse">



											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadMonth" for="acadMonth">Academic Month</form:label>
														<form:select id="acadMonth" path="acadMonth"
															class="form-control">
															<option value="" selected disabled hidden>Select Acad Month</option>
															<form:option value="Jan">Jan</form:option>
															<form:option value="Feb">Feb</form:option>
															<form:option value="Mar">Mar</form:option>
															<form:option value="Apr">Apr</form:option>
															<form:option value="May">May</form:option>
															<form:option value="June">June</form:option>
															<form:option value="July">July</form:option>
															<form:option value="Aug">Aug</form:option>
															<form:option value="Sept">Sept</form:option>
															<form:option value="Oct">Oct</form:option>
															<form:option value="Nov">Nov</form:option>

															<form:option value="Dec">Dec</form:option>
														</form:select>
													</div>
												</div>
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadYear" for="acadYear">Academic Year</form:label>
														<form:select id="acadYear" path="acadYear"
															class="form-control">
															<option value="" selected disabled hidden>Select Academic Year</option>
															<form:options items="${acadYears}" />
														</form:select>
													</div>
												</div>

												<%-- <div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="courseId" for="course">Course</form:label>
														<form:select id="course" path="courseId"
															placeholder="Course" class="form-control">
															<form:option value="">Select Course</form:option>
															<form:options items="${courseList}"
																itemLabel="courseName" itemValue="id" />
														</form:select>
													</div>
												</div> --%>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<label class="control-label" for="courses">Courses</label>
														<form:select id="courses" path="courseId" type="text"
															placeholder="course" class="form-control"
															>
															<option value="" selected disabled hidden>Select Course</option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>

												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
													<form:label path="username" for="username">SAPID</form:label>
														<form:input id="username" path="username" type="text"
															placeholder="User Name (Student/Faculty)"
															class="form-control" />
													</div>
												</div>
												
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="acadSession" for="acadSession">Academic Session</form:label>
														<form:select id="acadSession" path="acadSession"
															class="form-control">
															<option value="" selected disabled hidden>Select Acad Session</option>
															<form:options items="${acadSessionList}" />
														</form:select>
													</div>
												</div>
												

												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-primary">Search</button>
														<input id="reset" type="reset" class="btn btn-danger">
														<button id="cancel" name="cancel" class="btn btn-danger"
															formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
											</div>

										</form:form>
									</div>
								</div>
							</div>
						</div>
						<c:choose>
							<c:when test="${page.rowCount > 0}">
								<!-- Results Panel -->
								<div class="card border bg-white">
									<div class="card-body">
												<h5 class="text-center border-bottom pb-2">
													&nbsp;Enrollment List <font size="2px"> |
														${page.rowCount} Records Found &nbsp; </font>
												</h5>

											<div class="x_content">
												<div class="table-responsive testAssignTable">
													<table class="table table-striped table-hover">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Acad Month</th>
																<th>Acad Year</th>
																<th>Acad Session</th>
																<th>Course</th>
																<th>User Name</th>
																<th>Role</th>
																<th>Actions</th>
															</tr>
														</thead>
														<tbody>

															<c:forEach var="userCourse" items="${page.pageItems}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${userCourse.acadMonth}" /></td>
																	<td><c:out value="${userCourse.acadYear}" /></td>
																	<td><c:out value="${userCourse.acadSession}" /></td>
																	<td><c:out value="${userCourse.courseName}" /></td>
																	<td><c:out value="${userCourse.username}" /></td>
																	<td><c:out value="${userCourse.role}" /></td>
																	
																		<%-- <c:url value="/addCourseForm" var="editurl">
									  <c:param name="courseId" value="${course.id}" />
									</c:url> --%> <c:url value="deleteUserCourse"
																			var="deleteUserCourseurl">
																			<c:param name="courseId"
																				value="${userCourse.courseId}" />
																			<c:param name="username"
																				value="${userCourse.username}" />
																			<c:param name="role"
																				value="${userCourse.role}" />
																		</c:url>  
																		<td>
																		<a
																		href="${deleteUserCourseurl}" title="Delete"
																		onclick="return confirm('Are you sure you want to delete this record?')"><i
																			class="fas fa-trash"></i></a>


																	</td>
																</tr>
															</c:forEach>


														</tbody>
													</table>
												</div>
											</div>
									</div>
								</div>
							</c:when>
						</c:choose>

			<!-- /page content: END -->

                        
                        




                        </div>

                <!-- SIDEBAR START -->

                <!-- SIDEBAR END -->
        <jsp:include page="../common/newAdminFooter.jsp"/>

        


			