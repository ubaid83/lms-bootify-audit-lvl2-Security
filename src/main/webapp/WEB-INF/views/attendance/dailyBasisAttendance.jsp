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
                                <li class="breadcrumb-item active" aria-current="page">Daily Attendance</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Daily Attendance</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="" id="" method="post"
											modelAttribute="attendance">
											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-group">
													<form:label path="startDate" for="startDate">Start Date <span style="color: red">*</span></form:label>
														
														<div class='input-group date' >
															<form:input id="startDate" path="startDate" type="text"
																placeholder="Start Date" class="form-control"
																required="required" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
														
														
												</div>
											</div>

											<div class="col-sm-12 column">
												<div class="form-group">
													<form:button id="submit" name="submit"
														class="btn btn-large btn-primary" formaction="">Search</form:button>
													<input id="reset" type="reset" class="btn btn-danger">
													<button id="cancel" name="cancel" class="btn btn-danger"
														formnovalidate="formnovalidate">Cancel</button>
												</div>
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>
						</div>

						<!-- Results Panel -->

						
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>My Attendance</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
											<li><a class="close-link"><i class="fa fa-close"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">
										<form:form action="viewAttendance" id="listOfAttendanceMarked"
											method="post" modelAttribute="attendance">
											<div class="col-sm-12 column">
												<fieldset>
													

													<div class="table-responsive">
														<table id="attendanceTable" class="table table-hover">
															<thead>
																<tr>
																	<th>Sr. No.</th>
																	<th>Course</th>
																	<th>Status</th>

																</tr>
															</thead>

															<tbody>

																<c:forEach var="attendance"
																	items="${dailyAttendanceList}" varStatus="status">
																	<tr>
																		<td><c:out value="${status.count}" /></td>
																		<td><c:out value="${attendance.courseName} " /></td>
																		<td><c:out value=" " />Present</td>

																		<td>
																</c:forEach>
															</tbody>

														</table>
													</div>

													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">

															<button id="cancel" class="btn btn-large btn-danger"
																formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
														</div>
													</div>

												</fieldset>
											</div>
										</form:form>
									</div>
								</div>
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

	













