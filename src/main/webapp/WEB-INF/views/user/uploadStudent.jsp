<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


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
                        <li class="breadcrumb-item active" aria-current="page"> Upload Students</li>
                    </ol>
                </nav>
		

						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">


										<h5 class="text-center border-bottom pb-2">Upload Students for a New Program</h5>
	
										<form:form action="uploadStudent" id="uploadStudent"
											method="post" modelAttribute="user"
											enctype="multipart/form-data">



											<div class="row">
												<div class="col-md-4 col-sm-12">
													
														<div class="form-group">
															<label class="control-label" for="program">Program <span style="color: red">*</span></label>
															<form:select id="program" path="programId" type="text"
																placeholder="Program" class="form-control"
																required="required">
																<form:option value="">Select Program</form:option>
																<form:options items="${programs}"
																	itemLabel="programName" itemValue="id" />
															</form:select>
														</div>

														<div class="form-group">
															<label for="abbr">Select Student File as per
																template</label> <input id="file" name="file" type="file"
																class="form-control" required="required"/>
														</div>
														<div id=fileSize></div>
														<br>
														<div class="radio">
															<label> <form:radiobutton path="upsert" value="0"
																	class="radio" checked="true" name="upsert" /> Do not
																insert User Record if Username already exists
															</label>
														</div>
														<div class="radio">
															<label> <form:radiobutton path="upsert" value="1"
																	class="radio" name="upsert" /> Update User Record if
																username already exists
															</label>
														</div>
												</div>
												
												<div class="col-sm-12">
													<div class="form-group">

														<button id="submit" class="btn btn-large btn-primary"
															formaction="uploadStudent">Upload</button>
														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
												
												<div class="col-12">
													<strong>Template Format:</strong> 
													
													<p>Student No(UserName) | Password | First Name | Last Name | Father Name | Mother
													Name | Email | Mobile | Enrollment Month | Enrollment Year
													| Validity End Month | Validity End Year | Acad Session</p>
													
													<a class="text-danger" href="resources/templates/User_Upload_Template.xlsx">Download
														a sample template</a>
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









	



