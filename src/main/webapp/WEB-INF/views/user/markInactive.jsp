<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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
        
            
            
            <!-- page content: START -->
            <div class="right_col" role="main">
                
                <div class="dashboard_container">
                    
                    <div class="dashboard_container_spacing">
                        
                        <div class="breadcrumb">
							<a href="index.html">Dashboard</a>
							<i class="fa fa-angle-right"></i>  
							Make Inactive
						</div>
                        <jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
                        <c:choose>
		<c:when test="${page.rowCount > 0}">
                        
						<!-- Results Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>Course List | ${page.rowCount} Records Found</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                            <li><a class="close-link"><i class="fa fa-close"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
										<div class="table-responsive">
							<table class="table table-hover"
								style="font-size: 12px">
								<thead>
									<tr>
										<th>Sr. No.</th>
										<th>Course Name</th>
										<th>Academic Month</th>
										<th>Academic Year</th>
										<th>Actions</th>
									</tr>
								</thead>
								<tbody>

									<c:forEach var="course" items="${page.pageItems}"
										varStatus="status">
										<tr>
											<td><c:out value="${status.count}" /></td>
											
											<td><c:out value="${course.courseName}" /></td>
											<td><c:out value="${course.acadMonth}" /></td>
											<td><c:out value="${course.acadYear}" /></td>
											<td><c:url value="viewCourse" var="detailsUrl">
													<c:param name="courseId" value="${course.id}" />
												</c:url> <a href="${detailsUrl}" title="Details"><i
													class="fa fa-info-circle fa-lg"></i></a>&nbsp;
													 <c:url value="makeCourseInactive" var="inactiveurl">
													 <c:param name="id" value="${course.id}" />
												</c:url> 
													<a href="${inactiveurl}" title="Make In Active"
														onclick="return confirm('Are you sure you want to make  this record Inactive?')"><i
														class="fa fa-pencil-square-o fa-lg"></i></a>
													
													</td>
										</tr>
									</c:forEach>


								</tbody>
							</table>
						</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        </c:when></c:choose>
                        <c:choose>
		<c:when test="${page1.rowCount > 0}">
                        
						<!-- Results Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h2>Tests List | ${page1.rowCount} Records Found</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                            <li><a class="close-link"><i class="fa fa-close"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
                                    <div class="x_content">
										<div class="table-responsive">
							<table class="table table-hover"
								style="font-size: 12px">
								<thead>
									<tr>
										<th>Sr. No.</th>
										<th>Name</th>
										<th>Course</th>
										<th>Academic Month</th>
										<th>Academic Year</th>
										<th>Start Date</th>
										
										<th>End Date</th>
										<th>Actions</th>
									</tr>
								</thead>
								<tbody>

									<c:forEach var="test" items="${page1.pageItems}"
										varStatus="status">
										<tr>
											<td><c:out value="${status.count}" /></td>
											<td><c:out value="${test.testName}" /></td>
											<td><c:out value="${test.courseName}" /></td>
											<td><c:out value="${test.acadMonth}" /></td>
											<td><c:out value="${test.acadYear}" /></td>
											<td><c:out
													value="${fn:replace(test.startDate, 
                                'T', ' ')}"></c:out></td>
											<td><c:out
													value="${fn:replace(test.endDate, 
                                'T', ' ')}"></c:out></td>
											
											<td><%-- <c:url value="viewTestDetails" var="detailsUrl">
													<c:param name="testId" value="${test.id}" />
												</c:url> <a href="${detailsUrl}" title="Details"><i
													class="fa fa-info-circle fa-lg"></i></a>&nbsp; --%>
													 
													 <c:url value="makeTestInactive" var="inactiveurl">
													 <c:param name="id" value="${test.id}" />
												</c:url> 
													<a href="${inactiveurl}" title="Make In Active"
														onclick="return confirm('Are you sure you want to make  this record Inactive?')"><i
														class="fa fa-pencil-square-o fa-lg"></i></a>
													
													</td>
										</tr>
									</c:forEach>


								</tbody>
							</table>
						</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        </c:when></c:choose>
                        
                        
                        
                        <c:choose>
													<c:when test="${page2.rowCount > 0}">
														<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>Assignments | ${page2.rowCount} Records Found</h2>
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
																		<th>Session Month</th>
																		<th>Session Year</th>
																		<th>Course</th>
																		<th>Assignment Name</th>
																		
																		<th>Assignment Details</th>
																		
																		<th>Make Inactive </th>
																		
																	</tr>
																</thead>
																<tbody>

																	<c:forEach var="assignment" items="${page2.pageItems}"
																		varStatus="status">
																		<tr>
																			<td><c:out value="${status.count}" /></td>
																			<td><c:out value="${assignment.acadMonth}" /></td>
																			<td><c:out value="${assignment.acadYear}" /></td>
																			<td><c:out
																					value="${assignment.course.courseName}" /></td>
																			<td><c:out value="${assignment.assignmentName}" /></td>
																			
																			
																			
																			<td><a href="#"
																				onClick="showModal('${assignment.id}', '${assignment.assignmentName}');">Assignment
																					Details</a></td>
																			



																			
																			<td>
																			<c:url value="makeAssignmentInactive" var="inactiveurl">
													 <c:param name="id" value="${assignment.id}" />
												</c:url> 
													<a href="${inactiveurl}" title="Make In Active"
														onclick="return confirm('Are you sure you want to make  this record Inactive?')"><i
														class="fa fa-pencil-square-o fa-lg"></i></a>
																			
																			</td>
																		</tr>
																	</c:forEach>

																</tbody>
															</table>
														</div>
														<br>
</div></div></div></div>
													</c:when>
												</c:choose>
						<!-- Results Panel -->
                        
                        <c:choose>
							<c:when test="${page3.rowCount > 0}">
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>Announcements | ${page3.rowCount} Records Found</h2>
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
																<th>Announcement Title</th>
																<th>Make In Active</th>
															</tr>
														</thead>
														<tbody>

															<c:forEach var="announcement" items="${page3.pageItems}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out value="${announcement.subject}" /></td>
																	
																	
																	
																	
																	<td>
																	
																	<c:url value="makeAnnouncementInactive" var="inactiveurl">
													 <c:param name="id" value="${announcement.id}" />
												</c:url> 
													<a href="${inactiveurl}" title="Make In Active"
														onclick="return confirm('Are you sure you want to make  this record Inactive?')"><i
														class="fa fa-pencil-square-o fa-lg"></i></a></td>
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
                    </div>
                    
                    
                        
                </div>
                
            </div>
            <!-- /page content: END -->
            
            
            <jsp:include page="../common/footer.jsp" />
            
        </div>
    </div>


    
    
    
</body>
</html>
