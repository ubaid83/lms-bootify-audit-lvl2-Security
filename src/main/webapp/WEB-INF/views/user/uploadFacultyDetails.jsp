<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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

						<nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                        <li class="breadcrumb-item" aria-current="page">
                            <c:out value="${Program_Name}" />
                        </li>
                        <sec:authorize access="hasRole('ROLE_STUDENT')">
                            <c:out value="${AcadSession}" />
                        </sec:authorize>
                        <li class="breadcrumb-item active" aria-current="page"> Upload Faculty Details</li>
                    </ol>
                </nav>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">

										<h5 class="text-center border-bottom pb-2"> Upload Faculty Details</h5>

										<form:form action="uploadFacultyDetails" method="post"
											modelAttribute="facultyDetails" enctype="multipart/form-data">
											

											<div class="row">
												
												
												<div class="col-md-4 col-sm12">
													<div class="form-group">
														<label for="file"> Upload Faculty Details
															File(Excel):</label> <input id="file" name="file" type="file"
															class="form-control" required="required"/>
													</div>
												</div>
												
													
												
													<div class="col-12">
														<div class="form-group">


															<label class="control-label" for="courses">Excel
																Format: </label>
															<p>username | experience | overview | designation | dob | mobile | email</p>
															
															<br>
															<p>
																<a href="resources/templates/Faculty_Details_Upload_Template.xlsx"><font
																	color="red">Download a sample template</font></a>
															</p>



														</div>
													</div>
							

												<div class="col-12">
													<div class="form-group">

														<button id="submit" class="btn btn-large btn-primary"
															formaction="uploadFacultyDetails">Upload</button>

														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
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
	
	
	
	
	
	
	
	

			
