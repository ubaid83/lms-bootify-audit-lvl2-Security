<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
	<jsp:include page="../common/newDashboardHeader.jsp" /> 
<%
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
%>


<div class="d-flex adminPage" id="adminPage">
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
                        <li class="breadcrumb-item" aria-current="page">
                            <c:out value="${Program_Name}" />
                        </li>
                        <sec:authorize access="hasRole('ROLE_STUDENT')">
                            <c:out value="${AcadSession}" />
                        </sec:authorize>
                        <li class="breadcrumb-item active" aria-current="page"> Add Faculty Details</li>
                    </ol>
                </nav>
						
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">

										<h5 class="text-center border-bottom pb-2">Add Faculty Details</h5>
										
										



										<form:form action="addFacultyDetails" id="" method="post"
											modelAttribute="facultyDetails" enctype="multipart/form-data">


											<%
												if (isEdit) {
											%>
											<form:input type="hidden" path="id" />
											<%
												}
											%>

											<div class="row">
											<div class="col-12 mt-3 mb-3">
										<a href="uploadFacultyDetailsForm"><i
											class="btn btn-large btn-dark">Upload
												Faculty Details</i></a> 
										<a href="viewFacultyDetails"><i
											class="btn btn-large btn-dark">View
												All Faculty Details</i></a>
												</div>
												
												<div class="col-md-4 col-sm-12">
													<div class="form-group">
														<form:label path="username" for="username">Faculty Username</form:label>
														<form:select id="username" path="username"
															class="form-control">
															<form:option value="">Select Faculty</form:option>

															<c:forEach var="faculty" items="${finalList}"
																varStatus="status">
																<form:option value="${faculty}">${faculty}</form:option>
															</c:forEach>

														</form:select>
													</div>
												</div>

												<div class="col-md-4 col-sm-12">
													<div class="form-group">
														<form:label path="experience" for="experience">Experience (No. of Years) <span style="color: red">*</span></form:label>
														<form:input path="experience" type="number"
															class="form-control" required="required" />
													</div>
												</div>
												<div class="col-md-4 col-sm-12">
													<div class="form-group">

														<form:label path="dob" for="dob">Date of Birth <span style="color: red">*</span></form:label>
														<div class='input-group date'>
															<form:input id="dob" path="dob" type="text"
																class="form-control" required="required" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>
													</div>
												</div>

												
													<div class="col-md-4 col-sm-12">
														<div class="form-group">
															<form:label path="designation" for="designation">Designation <span style="color: red">*</span></form:label>
															<form:input path="designation" type="text"
																class="form-control" required="required" />
														</div>
													</div>
													
													<div class="col-md-4 col-sm-12">
														<div class="form-group">
															<form:label path="mobile" for="mobile">Contact Number <span style="color: red">*</span></form:label>
															<form:input path="mobile" type="number"
																class="form-control" required="required" />
														</div>
													</div>
											

													<div class="col-md-4 col-sm-12">
														<div class="form-group">
															<form:label path="email" for="email">Email ID</form:label>
															<form:input path="email" class="form-control" value=""
																type="text" />
														</div>
													</div>
													
													<div class="col-12">
													<form:label path="overview" for="overview">Experince / Overview</form:label>
													<form:textarea class="form-control" path="overview"
														id="overview" rows="10" placeholder="Experince / Overview" />
														
														</div>


												</div>

												<hr>
												<div class="row">

													<div class="col-sm-12">
														<div class="form-group">
															<%
																if (isEdit) {
															%>


															<button id="submit" class="btn btn-large btn-success mt-2"
																formaction="updateFacultyDetails">Update
																Details</button>
															<%
																} else {
															%>


															<button id="submit" class="btn btn-large btn-success mt-2"
																formaction="addFacultyDetails">Submit Details</button>

															<%
																}
															%>

															<button id="cancel" class="btn btn-danger mt-2"
																formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
															<input id="reset" type="reset" class="btn btn-dark mt-2">
														</div>
													</div>
												</div>
										</form:form>
						</div>
						</div>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->

	        <!-- SIDEBAR END -->
	<jsp:include page="../common/newAdminFooter.jsp"/>
	
	


			