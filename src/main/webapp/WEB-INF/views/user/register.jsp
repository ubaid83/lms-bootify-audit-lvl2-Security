<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
    <jsp:include page="../common/newDashboardHeader.jsp" /> 

    <div class="d-flex adminPage" id="adminPage">
    <jsp:include page="../common/newAdminLeftNavBar.jsp"/>
    <jsp:include page="../common/rightSidebarAdmin.jsp" />


    <!-- DASHBOARD BODY STARTS HERE -->

    <div class="container-fluid m-0 p-0 dashboardWraper">

        <jsp:include page="../common/newAdminTopHeader.jsp" />

                <!-- SEMESTER CONTENT -->    
                        <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
                        
			<!-- page content: START -->
			<div class="right_col" role="main">
				<div class="dashboard_container">
					<div class="dashboard_container_spacing">
					
						<nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                        <li class="breadcrumb-item active" aria-current="page"> Create User</li>
                    </ol>
                </nav>
                
						<jsp:include page="../common/alert.jsp" />
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">

									<div class="customNavTab">
										<ul class="nav nav-tabs nav-justified" id="userTab" role="tablist">
											<li class="nav-item">
											<a class="nav-link active" href="#login-block"
												data-toggle="tab" role="tab">
												<i class="fas fa-user"></i> Student</a>
												</li>
											<li class="nav-item"><a class="nav-link" href="#register-block"
												data-toggle="tab"><i class="fas fa-user"></i> Faculty</a></li>
											<li class="nav-item"><a class="nav-link" href="#contact-block-admin"
												data-toggle="tab"><i class="fas fa-user"></i> Admin</a></li>
											<li class="nav-item"><a class="nav-link"
												href="#contact-block-coordinator" data-toggle="tab"><i
													class="fas fa-user"></i> Co-ordinator</a></li>
											<li class="nav-item"><a class="nav-link"
												href="#contact-block-areaIncharge" data-toggle="tab"><i
													class="fas fa-user"></i> Area-Incharge</a></li>
											<li class="nav-item"><a class="nav-link" href="#contact-block-dean"
												data-toggle="tab"><i class="fas fa-user"></i> Dean</a></li>
											<li class="nav-item"><a class="nav-link" href="#contact-block-hod"
												data-toggle="tab"><i class="fas fa-user"></i> HOD</a></li>
										</ul>
										<div class="tab-content">
											<div class="tab-pane fade show active" id="login-block" role="tabpanel">
												<!-- Login Block Form -->
												<div class="login-block-form">
													<!-- Heading -->
													<h4>Create Student Account</h4>
													<!-- Border -->
													<div class="bor bg-green"></div>
													<!-- Form -->
													<form:form cssClass="form" role="form" action="addUser"
														method="post" modelAttribute="user"
														id="registerStudentForm">
														<div class="row">
															<!-- Form Group -->
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentYear">Enrollment
																		Year</label>
																	<form:select id="enrollmentYear" path="enrollmentYear"
																		type="text" placeholder="Year" class="form-control">
																		<form:option value="">Select Enrollment Year</form:option>
																		<form:options items="${yearList}" />
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentMonth">Enrollment
																		Month</label>
																	<form:select id="enrollmentMonth"
																		path="enrollmentMonth" type="text" placeholder="Month"
																		class="form-control">
																		<form:option value="">Select Enrollment Month</form:option>
																		<form:option value="Jan">Jan</form:option>
																		<form:option value="Feb">February</form:option>
																		<form:option value="Mar">March</form:option>
																		<form:option value="Apr">April</form:option>
																		<form:option value="May">May</form:option>
																		<form:option value="Jun">June</form:option>
																		<form:option value="Jul">July</form:option>
																		<form:option value="Aug">August</form:option>
																		<form:option value="Sept">September</form:option>
																		<form:option value="Oct">October</form:option>
																		<form:option value="Nov">November</form:option>
																		<form:option value="Dec">December</form:option>
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="validityEndYear">Validity
																		End Year</label>
																	<form:select id="validityEndYear"
																		path="validityEndYear" type="text" placeholder="Year"
																		class="form-control">
																		<form:option value="">Select Validity End Year</form:option>
																		<form:options items="${endYearList}" />
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="validityEndMonth">Validity
																		End Month</label>
																	<form:select id="validityEndMonth"
																		path="validityEndMonth" type="text"
																		placeholder="Month" class="form-control">
																		<form:option value="">Select Validity End Month</form:option>
																		<form:option value="Jan">Jan</form:option>
																		<form:option value="Feb">February</form:option>
																		<form:option value="Mar">March</form:option>
																		<form:option value="Apr">April</form:option>
																		<form:option value="May">May</form:option>
																		<form:option value="Jun">June</form:option>
																		<form:option value="Jul">July</form:option>
																		<form:option value="Aug">August</form:option>
																		<form:option value="Sept">September</form:option>
																		<form:option value="Oct">October</form:option>
																		<form:option value="Nov">November</form:option>
																		<form:option value="Dec">December</form:option>
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="firstname">First
																		Name</label>
																	<form:input type="text" path="firstname"
																		cssClass="form-control" placeholder="Enter First Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="lastname">Last
																		Name</label>
																	<form:input type="text" path="lastname"
																		cssClass="form-control" placeholder="Enter Last Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="fatherName">Father
																		Name</label>
																	<form:input id="fatherName" path="fatherName"
																		type="text" placeholder="Father Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="motherName">Mother
																		Name</label>
																	<form:input id="motherName" path="motherName"
																		type="text" placeholder="Mother Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="program">Program <span style="color: red">*</span></label>
																	<form:select id="program" path="program.id" type="text"
																		placeholder="Program" class="form-control"
																		required="required">
																		<form:option value="">Select Program</form:option>
																		<form:options items="${programs}"
																			itemLabel="programName" itemValue="id" />
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<!-- Label -->
																	<label class="control-label" for="username">Username</label>
																	<!-- Input -->
																	<form:input type="text" path="username"
																		cssClass="form-control" placeholder="Enter Username" />
																</div>
																<div class="form-group hidden">
																	<form:hidden path="userRoles[0].role" value="ROLE_USER" />
																	<form:hidden path="userRoles[1].role"
																		value="ROLE_STUDENT" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="email">Email</label>
																	<form:input id="email" path="email" type="text"
																		placeholder="Email" class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="acadSession">Session</label>
																	<form:select id="acadSession" path="acadSession"
																		type="text" placeholder="Acad Session"
																		class="form-control">
																		<form:option value="">Select Acad Session</form:option>
																		<form:options items="${acadSessionList}" />
																	</form:select>
																</div>
															</div>
															<div class="clearfix"></div>
															<div class="col-sm-4">
																<div class="form-group">
																	<!-- Button -->
																	<button type="submit" class="btn btn-success">Register</button>
																	<button type="reset" class="btn btn-danger">Reset</button>
																</div>
															</div>
														</div>
													</form:form>
												</div>
											</div>
											<div class="tab-pane fade" id="register-block">
												<div class="register-block-form">
													<!-- Heading -->
													<h4>Create Faculty Account</h4>
													<!-- Border -->
													<div class="bor bg-lblue"></div>
													<!-- Form -->
													<form:form cssClass="form" role="form" action="addUser"
														method="post" modelAttribute="user"
														id="registerStudentForm">
														<div class="row">
															<!-- Form Group -->
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentYear">Enrollment
																		Year</label>
																	<form:select id="enrollmentYear" path="enrollmentYear"
																		type="text" placeholder="Year" class="form-control">
																		<form:option value="">Select Enrollment Year</form:option>
																		<form:options items="${yearList}" />
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentMonth">Enrollment
																		Month</label>
																	<form:select id="enrollmentMonth"
																		path="enrollmentMonth" type="text" placeholder="Month"
																		class="form-control">
																		<form:option value="">Select Enrollment Month</form:option>
																		<form:option value="Jan">Jan</form:option>
																		<form:option value="Feb">February</form:option>
																		<form:option value="Mar">March</form:option>
																		<form:option value="Apr">April</form:option>
																		<form:option value="May">May</form:option>
																		<form:option value="Jun">June</form:option>
																		<form:option value="Jul">July</form:option>
																		<form:option value="Aug">August</form:option>
																		<form:option value="Sept">September</form:option>
																		<form:option value="Oct">October</form:option>
																		<form:option value="Nov">November</form:option>

																		<form:option value="Dec">December</form:option>
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="firstname">First
																		Name</label>
																	<form:input type="text" path="firstname"
																		cssClass="form-control" placeholder="Enter First Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="lastname">Last
																		Name</label>
																	<form:input type="text" path="lastname"
																		cssClass="form-control" placeholder="Enter Last Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="fatherName">Father
																		Name</label>
																	<form:input id="fatherName" path="fatherName"
																		type="text" placeholder="Father Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="motherName">Mother
																		Name</label>
																	<form:input id="motherName" path="motherName"
																		type="text" placeholder="Mother Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<!-- Label -->
																	<label class="control-label" for="username">Username</label>
																	<!-- Input -->
																	<form:input type="text" path="username"
																		cssClass="form-control" placeholder="Enter Username" />
																</div>
																<div class="form-group hidden">
																	<form:hidden path="userRoles[0].role" value="ROLE_USER" />
																	<form:hidden path="userRoles[1].role"
																		value="ROLE_FACULTY" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="type">Faculty
																		Type <span style="color: red">*</span></label>
																	<form:select id="type" path="type" type="text"
																		placeholder="Faculty Type" class="form-control"
																		required="required">
																		<form:option value="">Select Faculty Type</form:option>
																		<form:option value="P">Permanent Faculty</form:option>
																		<form:option value="H">Vendor Faculty</form:option>
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="email">Email</label>
																	<form:input id="email" path="email" type="text"
																		placeholder="Email" class="form-control" />
																</div>
															</div>
															
															<div class="col-12">
																<div class="form-group">
																	<!-- Button -->
																	<button type="submit" class="btn btn-success">Register</button>
																	<button type="reset" class="btn btn-danger">Reset</button>
																</div>
															</div>
														</div>
													</form:form>
												</div>
											</div>
											<div class="tab-pane fade" id="contact-block-admin">
												<!-- Contact Block Form -->
												<div class="contact-block-form">
													<h4>Create Admin Account</h4>
													<!-- Border -->
													<div class="bor bg-purple"></div>
													<!-- Form -->
													<form:form cssClass="form" role="form" action="addUser"
														method="post" modelAttribute="user"
														id="registerStudentForm">
														<div class="row">
															<!-- Form Group -->
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentYear">Enrollment
																		Year</label>
																	<form:select id="enrollmentYear" path="enrollmentYear"
																		type="text" placeholder="Year" class="form-control">
																		<form:option value="">Select Enrollment Year</form:option>
																		<form:options items="${yearList}" />
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentMonth">Enrollment
																		Month</label>
																	<form:select id="enrollmentMonth"
																		path="enrollmentMonth" type="text" placeholder="Month"
																		class="form-control">
																		<form:option value="">Select Enrollment Month</form:option>
																		<form:option value="Jan">Jan</form:option>
																		<form:option value="Feb">February</form:option>
																		<form:option value="Mar">March</form:option>
																		<form:option value="Apr">April</form:option>
																		<form:option value="May">May</form:option>
																		<form:option value="Jun">June</form:option>
																		<form:option value="Jul">July</form:option>
																		<form:option value="Aug">August</form:option>
																		<form:option value="Sept">September</form:option>
																		<form:option value="Oct">October</form:option>
																		<form:option value="Nov">November</form:option>
																		<form:option value="Dec">December</form:option>
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="firstname">First
																		Name</label>
																	<form:input type="text" path="firstname"
																		cssClass="form-control" placeholder="Enter First Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="lastname">Last
																		Name</label>
																	<form:input type="text" path="lastname"
																		cssClass="form-control" placeholder="Enter Last Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="fatherName">Father
																		Name</label>
																	<form:input id="fatherName" path="fatherName"
																		type="text" placeholder="Father Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="motherName">Mother
																		Name</label>
																	<form:input id="motherName" path="motherName"
																		type="text" placeholder="Mother Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<!-- Label -->
																	<label class="control-label" for="username">Username</label>
																	<!-- Input -->
																	<form:input type="text" path="username"
																		cssClass="form-control" placeholder="Enter Username" />
																</div>
																<div class="form-group hidden">
																	<form:hidden path="userRoles[0].role" value="ROLE_USER" />
																	<form:hidden path="userRoles[1].role"
																		value="ROLE_ADMIN" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="email">Email</label>
																	<form:input id="email" path="email" type="text"
																		placeholder="Email" class="form-control" />
																</div>
															</div>
														
															<div class="col-12">
																<div class="form-group">
																	<!-- Button -->
																	<button type="submit" class="btn btn-success">Register</button>
																	<button type="reset" class="btn btn-danger">Reset</button>
																</div>
															</div>
														</div>
													</form:form>
												</div>
											</div>
											<div class="tab-pane fade" id="contact-block-coordinator">
												<!-- Contact Block Form -->
												<div class="contact-block-form">
													<h4>Create Co-ordinator Account</h4>
													<!-- Border -->
													<div class="bor bg-purple"></div>
													<!-- Form -->
													<form:form cssClass="form" role="form" action="addUser"
														method="post" modelAttribute="user"
														id="registerStudentForm">
														<div class="row">
															<!-- Form Group -->
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentYear">Enrollment
																		Year</label>
																	<form:select id="enrollmentYear" path="enrollmentYear"
																		type="text" placeholder="Year" class="form-control">
																		<form:option value="">Select Enrollment Year</form:option>
																		<form:options items="${yearList}" />
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentMonth">Enrollment
																		Month</label>
																	<form:select id="enrollmentMonth"
																		path="enrollmentMonth" type="text" placeholder="Month"
																		class="form-control">
																		<form:option value="">Select Enrollment Month</form:option>
																		<form:option value="Jan">Jan</form:option>
																		<form:option value="Feb">February</form:option>
																		<form:option value="Mar">March</form:option>
																		<form:option value="Apr">April</form:option>
																		<form:option value="May">May</form:option>
																		<form:option value="Jun">June</form:option>
																		<form:option value="Jul">July</form:option>
																		<form:option value="Aug">August</form:option>
																		<form:option value="Sept">September</form:option>
																		<form:option value="Oct">October</form:option>
																		<form:option value="Nov">November</form:option>

																		<form:option value="Dec">December</form:option>
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="firstname">First
																		Name</label>
																	<form:input type="text" path="firstname"
																		cssClass="form-control" placeholder="Enter First Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="lastname">Last
																		Name</label>
																	<form:input type="text" path="lastname"
																		cssClass="form-control" placeholder="Enter Last Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="fatherName">Father
																		Name</label>
																	<form:input id="fatherName" path="fatherName"
																		type="text" placeholder="Father Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="motherName">Mother
																		Name</label>
																	<form:input id="motherName" path="motherName"
																		type="text" placeholder="Mother Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<!-- Label -->
																	<label class="control-label" for="username">Username</label>
																	<!-- Input -->
																	<form:input type="text" path="username"
																		cssClass="form-control" placeholder="Enter Username" />
																</div>
																<div class="form-group hidden">
																	<form:hidden path="userRoles[0].role" value="ROLE_USER" />
																	<form:hidden path="userRoles[1].role"
																		value="ROLE_ADMIN" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="email">Email</label>
																	<form:input id="email" path="email" type="text"
																		placeholder="Email" class="form-control" />
																</div>
															</div>
															
															<div class="col-12">
																<div class="form-group">
																	<!-- Button -->
																	<button type="submit" class="btn btn-success">Register</button>
																	<button type="reset" class="btn btn-danger">Reset</button>
																</div>
															</div>
														</div>
													</form:form>
												</div>
											</div>
											<div class="tab-pane fade" id="contact-block-areaIncharge">
												<!-- Contact Block Form -->
												<div class="contact-block-form">
													<h4>Create Area-Incharge Account</h4>
													<!-- Border -->
													<div class="bor bg-purple"></div>
													<!-- Form -->
													<form:form cssClass="form" role="form" action="addUser"
														method="post" modelAttribute="user"
														id="registerStudentForm">
														<div class="row">
															<!-- Form Group -->
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentYear">Enrollment
																		Year</label>
																	<form:select id="enrollmentYear" path="enrollmentYear"
																		type="text" placeholder="Year" class="form-control">
																		<form:option value="">Select Enrollment Year</form:option>
																		<form:options items="${yearList}" />
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentMonth">Enrollment
																		Month</label>
																	<form:select id="enrollmentMonth"
																		path="enrollmentMonth" type="text" placeholder="Month"
																		class="form-control">
																		<form:option value="">Select Enrollment Month</form:option>
																		<form:option value="Jan">Jan</form:option>
																		<form:option value="Feb">February</form:option>
																		<form:option value="Mar">March</form:option>
																		<form:option value="Apr">April</form:option>
																		<form:option value="May">May</form:option>
																		<form:option value="Jun">June</form:option>
																		<form:option value="Jul">July</form:option>
																		<form:option value="Aug">August</form:option>
																		<form:option value="Sept">September</form:option>
																		<form:option value="Oct">October</form:option>
																		<form:option value="Nov">November</form:option>

																		<form:option value="Dec">December</form:option>
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="firstname">First
																		Name</label>
																	<form:input type="text" path="firstname"
																		cssClass="form-control" placeholder="Enter First Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="lastname">Last
																		Name</label>
																	<form:input type="text" path="lastname"
																		cssClass="form-control" placeholder="Enter Last Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="fatherName">Father
																		Name</label>
																	<form:input id="fatherName" path="fatherName"
																		type="text" placeholder="Father Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="motherName">Mother
																		Name</label>
																	<form:input id="motherName" path="motherName"
																		type="text" placeholder="Mother Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<!-- Label -->
																	<label class="control-label" for="username">Username</label>
																	<!-- Input -->
																	<form:input type="text" path="username"
																		cssClass="form-control" placeholder="Enter Username" />
																</div>
																<div class="form-group hidden">
																	<form:hidden path="userRoles[0].role" value="ROLE_USER" />
																	<form:hidden path="userRoles[1].role"
																		value="ROLE_ADMIN" />
																	<form:hidden path="userRoles[2].role" value="ROLE_CORD" />
																	<form:hidden path="userRoles[3].role"
																		value="ROLE_AREA_INCHARGE" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="email">Email</label>
																	<form:input id="email" path="email" type="text"
																		placeholder="Email" class="form-control" />
																</div>
															</div>
															<div class="clearfix"></div>
															<div class="col-12">
																<div class="form-group">
																	<!-- Button -->
																	<button type="submit" class="btn btn-success">Register</button>
																	<button type="reset" class="btn btn-danger">Reset</button>
																</div>
															</div>
														</div>
													</form:form>
												</div>
											</div>
											<div class="tab-pane fade" id="contact-block-dean">
												<!-- Contact Block Form -->
												<div class="contact-block-form">
													<h4>Create Dean Account</h4>
													<!-- Border -->
													<div class="bor bg-purple"></div>
													<!-- Form -->
													<form:form cssClass="form" role="form" action="addUser"
														method="post" modelAttribute="user"
														id="registerStudentForm">
														<div class="row">
															<!-- Form Group -->
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentYear">Enrollment
																		Year</label>
																	<form:select id="enrollmentYear" path="enrollmentYear"
																		type="text" placeholder="Year" class="form-control">
																		<form:option value="">Select Enrollment Year</form:option>
																		<form:options items="${yearList}" />
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentMonth">Enrollment
																		Month</label>
																	<form:select id="enrollmentMonth"
																		path="enrollmentMonth" type="text" placeholder="Month"
																		class="form-control">
																		<form:option value="">Select Enrollment Month</form:option>
																		<form:option value="Jan">Jan</form:option>
																		<form:option value="Feb">February</form:option>
																		<form:option value="Mar">March</form:option>
																		<form:option value="Apr">April</form:option>
																		<form:option value="May">May</form:option>
																		<form:option value="Jun">June</form:option>
																		<form:option value="Jul">July</form:option>
																		<form:option value="Aug">August</form:option>
																		<form:option value="Sept">September</form:option>
																		<form:option value="Oct">October</form:option>
																		<form:option value="Nov">November</form:option>

																		<form:option value="Dec">December</form:option>
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="firstname">First
																		Name</label>
																	<form:input type="text" path="firstname"
																		cssClass="form-control" placeholder="Enter First Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="lastname">Last
																		Name</label>
																	<form:input type="text" path="lastname"
																		cssClass="form-control" placeholder="Enter Last Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="fatherName">Father
																		Name</label>
																	<form:input id="fatherName" path="fatherName"
																		type="text" placeholder="Father Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="motherName">Mother
																		Name</label>
																	<form:input id="motherName" path="motherName"
																		type="text" placeholder="Mother Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<!-- Label -->
																	<label class="control-label" for="username">Username</label>
																	<!-- Input -->
																	<form:input type="text" path="username"
																		cssClass="form-control" placeholder="Enter Username" />
																</div>
																<div class="form-group hidden">
																	<form:hidden path="userRoles[0].role" value="ROLE_USER" />
																	<form:hidden path="userRoles[1].role" value="ROLE_DEAN" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="email">Email</label>
																	<form:input id="email" path="email" type="text"
																		placeholder="Email" class="form-control" />
																</div>
															</div>
														
															<div class="col-12">
																<div class="form-group">
																	<!-- Button -->
																	<button type="submit" class="btn btn-success">Register</button>
																	<button type="reset" class="btn btn-danger">Reset</button>
																</div>
															</div>
														</div>
													</form:form>
												</div>
											</div>
											<div class="tab-pane fade" id="contact-block-hod">
												<!-- Contact Block Form -->
												<div class="contact-block-form">
													<h4>Create HOD Account</h4>
													<!-- Border -->
													<div class="bor bg-purple"></div>
													<!-- Form -->
													<form:form cssClass="form" role="form" action="addUser"
														method="post" modelAttribute="user"
														id="registerStudentForm">
														<div class="row">
															<!-- Form Group -->
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentYear">Enrollment
																		Year</label>
																	<form:select id="enrollmentYear" path="enrollmentYear"
																		type="text" placeholder="Year" class="form-control">
																		<form:option value="">Select Enrollment Year</form:option>
																		<form:options items="${yearList}" />
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="enrollmentMonth">Enrollment
																		Month</label>
																	<form:select id="enrollmentMonth"
																		path="enrollmentMonth" type="text" placeholder="Month"
																		class="form-control">
																		<form:option value="">Select Enrollment Month</form:option>
																		<form:option value="Jan">Jan</form:option>
																		<form:option value="Feb">February</form:option>
																		<form:option value="Mar">March</form:option>
																		<form:option value="Apr">April</form:option>
																		<form:option value="May">May</form:option>
																		<form:option value="Jun">June</form:option>
																		<form:option value="Jul">July</form:option>
																		<form:option value="Aug">August</form:option>
																		<form:option value="Sept">September</form:option>
																		<form:option value="Oct">October</form:option>
																		<form:option value="Nov">November</form:option>

																		<form:option value="Dec">December</form:option>
																	</form:select>
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="firstname">First
																		Name</label>
																	<form:input type="text" path="firstname"
																		cssClass="form-control" placeholder="Enter First Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="lastname">Last
																		Name</label>
																	<form:input type="text" path="lastname"
																		cssClass="form-control" placeholder="Enter Last Name" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="fatherName">Father
																		Name</label>
																	<form:input id="fatherName" path="fatherName"
																		type="text" placeholder="Father Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="motherName">Mother
																		Name</label>
																	<form:input id="motherName" path="motherName"
																		type="text" placeholder="Mother Name"
																		class="form-control" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<!-- Label -->
																	<label class="control-label" for="username">Username</label>
																	<!-- Input -->
																	<form:input type="text" path="username"
																		cssClass="form-control" placeholder="Enter Username" />
																</div>
																<div class="form-group hidden">
																	<form:hidden path="userRoles[0].role" value="ROLE_USER" />
																	<form:hidden path="userRoles[1].role" value="ROLE_HOD" />
																</div>
															</div>
															<div class="col-md-3 col-sm-4">
																<div class="form-group">
																	<label class="control-label" for="email">Email</label>
																	<form:input id="email" path="email" type="text"
																		placeholder="Email" class="form-control" />
																</div>
															</div>
														
															<div class="col-12">
																<div class="form-group">
																	<!-- Button -->
																	<button type="submit" class="btn btn-success">Register</button>
																	<button type="reset" class="btn btn-danger">Reset</button>
																</div>
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
				</div>
			</div>
			<!-- /page content: END -->



                        </div>

                <!-- SIDEBAR START -->

                <!-- SIDEBAR END -->
        <jsp:include page="../common/newAdminFooter.jsp"/>	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

			