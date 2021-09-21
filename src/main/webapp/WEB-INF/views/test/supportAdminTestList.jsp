<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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

			<jsp:include page="../common/topHeader.jsp">
				<jsp:param value="Test" name="activeMenu" />
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
							<i class="fa fa-angle-right"></i> Tests
						</div>
						<jsp:include page="../common/alert.jsp" />

						

								<!-- Results Panel -->
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>Tests List | ${page.rowCount} Records Found</h2>
												<ul class="nav navbar-right panel_toolbox">
													<li><a class="collapse-link"><i
															class="fa fa-chevron-up"></i></a></li>
													
												</ul>
												<div class="clearfix"></div>
											</div>
											<c:choose>
							<c:when test="${page.rowCount > 0}">
											<div class="x_content">
												<div class="table-responsive">
													<table class="table table-hover" style="font-size: 12px">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Name</th>
																<th>Course</th>
																<th>Test Type</th>
																<th>Academic Month</th>
																<th>Academic Year</th>
																<th>Start Date</th>
																<!-- <th>Due Date</th> -->
																<th>End Date</th>
																
															</tr>
														</thead>
														<tbody>

															<c:forEach var="test" items="${page.pageItems}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${test.testName}" /></td>
																	<td><c:out value="${test.courseName}" /></td>
																	<td><c:out value="${test.testType}" /></td>
																	<td><c:out value="${test.acadMonth}" /></td>
																	<td><c:out value="${test.acadYear}" /></td>
																	<td><c:out
																			value="${fn:replace(test.startDate, 
                                'T', ' ')}"></c:out></td>
																	<td><c:out
																			value="${fn:replace(test.endDate, 
                                'T', ' ')}"></c:out></td>
																	<%-- <td><c:out
																			value="${fn:replace(test.dueDate, 
                                'T', ' ')}"></c:out></td> --%>
																	<%-- <td><c:if
																			test="${test.showResultsToStudents eq 'Y' }"> Already Shown
</c:if> <c:if test="${test.showResultsToStudents ne 'Y' }">


																			<div class="col-sm-12 column" style="float: right;"
																				id="show">


																				<a href="#" id="show${test.id}" class="showClass"
																					onclick='document.getElementById(this.id).removeAttribute("href");'>
																					Show Now </a>


																			</div>
																		</c:if></td> --%>
																	
																	
																</tr>
															</c:forEach>


														</tbody>
													</table>
												</div>
												
											</div>
											</c:when>
						</c:choose>
						<c:url value="/checkUserCourse" var="goBack">
													<c:param name="username" value="${username}"></c:param>
													</c:url>
													<a href="${goBack}" class="btn btn-success" >Back</a>
										</div>
									</div>
								</div>
							
						<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="testList" />
						</jsp:include>

					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />
			
		</div>
	</div>





</body>
</html>
