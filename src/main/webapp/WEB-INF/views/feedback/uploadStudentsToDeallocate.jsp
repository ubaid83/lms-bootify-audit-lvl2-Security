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
                        <li class="breadcrumb-item active" aria-current="page"> De-Allocate Feedback</li>
                    </ol>
                </nav>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">

										<h5 class="border-bottom text-center pb-2">De-Allocate Feedback</h5>
										
										<%-- <c:if test="${showProceed}">
										<div class="col-12 text-right">
											<a href="addStudentFeedbackForm"><i
												class="btn btn-large btn-primary">Proceed
													to allocate students</i></a>
													</div>
										</c:if> --%>

										<form:form action="uploadStudentsToDeallocate" method="post"
											modelAttribute="feedback" enctype="multipart/form-data">
											<form:input type="hidden" value="${feedback.id}" path="id"/>
											<div class="row">
												
												<div class="col-12">
													<div class="form-group">
														<label for="file">Upload
															File(Excel):</label> <input id="file" name="file" type="file"
															class="form-control" />
													</div>
												</div>
												
													<div class="col-12">
														<div class="form-group">
															<label class="control-label" for="courses">Excel
																Format: </label>
															<p>SAPID</p>
															<p>
																<b>Note:</b>
															<ul>
																<li>SAPID is student's SAPIDs</li>
																<li>Keep those student's SAPId in template which you want to de-allocate.</li>
															</ul>
															<br>
															<p>
																<a class="text-danger" href="downloadDeallocateFeedbackTemplate?feedbackId=${feedback.id}">Download a sample template</a>
															</p>
														</div>
													</div>
										
										
												</div>
												<div class="row">

													<div class="col-sm-12 column">
														<div class="form-group">

															<button id="submit" class="btn btn-large btn-success"
																formaction="uploadStudentsToDeallocate">Upload</button>

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