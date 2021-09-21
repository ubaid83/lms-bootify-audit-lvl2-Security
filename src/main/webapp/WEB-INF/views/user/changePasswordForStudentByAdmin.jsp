<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>



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
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item active" aria-current="page">Student Profile</li>
						</ol>
					</nav>

						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">
										<h5 class="text-center border-bottom pb-2">Change Password</h5>


									<div class="x_content">
										<form:form action="changePasswordStudentByAdmin" id="" method="post"
											modelAttribute="user">

											<%-- <form:input path="username" type="hidden" />
											<form:input path="id" type="hidden" /> --%>

											<div class="row">
												<div class="col-md-4 col-12">
													<div class="form-group">
													<label><i class="fa fa-key"></i> <span style="color: red">*</span></label>
														<form:input type="text" path="username" cssClass="form-control"
															placeholder="Username/SAP ID" id="username"
															required="required" />
														

													</div>
												</div>

												<div class="col-md-4 col-12">
													<div class="form-group">
													<label><i class="fa fa-lock"></i> <span style="color: red">*</span></label>
														<form:password path="newPassword" cssClass="form-control"
															placeholder="Enter New Password" id="password"
															required="required" />
														
													</div>
												</div>
												<div class="col-md-4 col-12">
													<div class="form-group">
													<label><i class="fa fa-lock"></i> <span style="color: red">*</span></label>
														<form:password path="reenterPassword"
															name="reenterPassword"
															placeholder="Re Enter New Password" id="reenterPassword"
															class="form-control" required="required" />
														
													</div>
												</div>

											</div>
											
											<div class="col-12 text-left">
											<button id="submit" class="btn btn-danger"
												formaction="changePasswordStudentByAdmin" formnovalidate="formnovalidate">Change
												Password Details</button>

											<button id="cancel" class="btn btn-danger"
												formaction="homepage" formnovalidate="formnovalidate">BACK</button>
												</div>
												
										</form:form>
									</div>
							</div>
						</div>

			<!-- /page content: END -->

                        
                        




                        </div>

                <!-- SIDEBAR START -->

                <!-- SIDEBAR END -->
        <jsp:include page="../common/newAdminFooter.jsp"/>

        
			