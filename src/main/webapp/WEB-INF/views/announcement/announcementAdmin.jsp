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
									<li class="breadcrumb-item"><c:out value="${Program_Name}" /></li>
									<sec:authorize access="hasRole('ROLE_STUDENT')">

								<c:out value="${AcadSession}" />

							</sec:authorize>
										<li class="breadcrumb-item active" aria-current="page">Announcement Details</li>
								</ol>
							</nav>
						<jsp:include page="../common/alert.jsp" />
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">
								<div class="x_panel">

									<div class="text-center border-bottom pb-2">
										<h5>Announcement Details</h5>
									</div>

									<div class="x_content">
										<form:form action="addAnnouncementForm" id="addAnnouncement"
											method="post" modelAttribute="announcement">


											<form:input path="id" type="hidden" />
											<form:input path="courseId" type="hidden" />
											<form:input path="programId" type="hidden" />
											<form:input path="announcementType" type="hidden" />

											<div class="row mt-3">
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="subject" for="subject"><strong>Announcement Title:</strong></form:label>
														<span class="font-italic">${announcement.subject}</span>
													</div>
												</div>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="announcementSubType"
															for="announcementSubType"><strong>Announcement Subtype:</strong></form:label>
														<span class="font-italic">${announcement.announcementSubType}</span>
													</div>
												</div>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="startDate" for="startDate"><strong>Display From:</strong></form:label>
														<span class="font-italic">${announcement.startDate}</span>
													</div>
												</div>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="endDate" for="endDate"><strong>Display Until:</strong></form:label>
														<span class="font-italic">${announcement.endDate}</span>
													</div>
												</div>
												<c:if test="${not empty announcement.acadSession}">


													<div class="col-md-4 col-sm-6">
														<div class="form-group">
															<form:label path="acadSession" for="acadSession"><strong>Acad Session:</strong></form:label>
															<span class="font-italic">${announcement.acadSession}</span>
														</div>
													</div>
													<div class="col-md-4 col-sm-6">
														<div class="form-group">
															<form:label path="acadYear" for="acadYear"><strong>Acad Year:</strong></form:label>
															<span class="font-italic">${announcement.acadYear}</span>
														</div>
													</div>
												</c:if>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="campusName" for="campusName"><strong>Campus:</strong></form:label>
														<span class="font-italic">${announcement.campusName}</span>
													</div>
												</div>
											</div>
<hr/>
											<div class="row">
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="sendEmailAlert" for="sendEmailAlert"><strong>Send Email Alert for New Announcement:</strong></form:label>
														<span class="font-italic">${announcement.sendEmailAlert}</span>
													</div>
												</div>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="sendEmailAlertToParents"
															for="sendEmailAlertToParents"><strong>Send Email Alert to Parents:</strong></form:label>
														<span class="font-italic">${announcement.sendEmailAlertToParents}</span>
													</div>
												</div>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="sendSmsAlert" for="sendSmsAlert"><strong>Send SMS Alert for New Announcement:</strong></form:label>
														<span class="font-italic">${announcement.sendSmsAlert}</span>
													</div>
												</div>
												<div class="col-md-4 col-sm-6">
													<div class="form-group">
														<form:label path="sendSmsAlertToParents"
															for="sendSmsAlertToParents"><strong>Send SMS Alert to Parents:</strong></form:label>
														<span class="font-italic">${announcement.sendSmsAlertToParents}</span>
													</div>
												</div>
												<c:if test="${announcement.filePath ne null}">
													<div class="col-md-4 col-sm-6">
														<div class="form-group">
															<%-- <form:label path="sendSmsAlert" for="sendSmsAlert"> : </form:label> --%>

															<a class="text-reset" href="sendAnnouncementFile?id=${announcement.id}"
																target="_blank" onclick="return preview(this.href); "><strong>View
																Announcement File</strong></a>



														</div>
													</div>

												</c:if>
											</div>
												
													<p class="bg-secondary p-1 text-center text-white cursor-pointer"  data-toggle="collapse" data-target="#description"
															aria-expanded="true" aria-controls="description">
															Announcement Details: (Expand/Collapse)
													</p>
													<div id="description" class="collapse p-1">
													${announcement.description}</div>
												

											<div class="col-12 text-center mt-3">
													<div class="form-group">
														<sec:authorize
															access="hasAnyRole('ROLE_LIBRARIAN', 'ROLE_EXAM')">
															<button id="cancel" class="btn btn-large btn-danger"
																formaction="searchAnnouncementForLibrarian"
																formnovalidate="formnovalidate">Back</button>
														</sec:authorize>
														<sec:authorize access="hasRole('ROLE_COUNSELOR')">
															<button id="cancel" class="btn btn-large btn-danger"
																formaction="searchAnnouncementForCounselor"
																formnovalidate="formnovalidate">Back</button>
														</sec:authorize>
														<sec:authorize
															access="hasAnyRole('ROLE_STUDENT', 'ROLE_FACULTY', 'ROLE_ADMIN')">
															<!-- <button id="cancel" class="btn btn-large btn-danger"
																 onclick="history.go(-1);"
																>Back</button> -->
																
																<button class="btn btn-large btn-danger" type="button" name="back"  onclick="history.back()">Back</button>


														</sec:authorize>

													</div>
											</div>


										</form:form>
									</div>
								</div>
							</div>
						</div>
			<!-- /page content: END -->
                     
                   
                    </div>
                                                
                                                <!-- SIDEBAR START -->

                        <!-- SIDEBAR END -->
                <jsp:include page="../common/newAdminFooter.jsp"/>
	



                        
           
                   
                   