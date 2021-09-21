<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
	
	<!-- LIBRARY AND COUNSELOR PAGE -->
	<sec:authorize access="hasAnyRole('ROLE_EXAM','ROLE_LIBRARIAN')">
	
	
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>
			<jsp:include page="../common/topHeaderLibrian.jsp" />



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
							<i class="fa fa-angle-right"></i> Announcement Details
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Announcement Details</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="addAnnouncementForm" id="addAnnouncement"
											method="post" modelAttribute="announcement">


											<form:input path="id" type="hidden" />
											<form:input path="courseId" type="hidden" />
											<form:input path="programId" type="hidden" />
											<form:input path="announcementType" type="hidden" />

											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="subject" for="subject">Announcement Title:</form:label>
														${announcement.subject}
													</div>
												</div>
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="announcementSubType"
															for="announcementSubType">Announcement Subtype:</form:label>
														${announcement.announcementSubType}
													</div>
												</div>
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="startDate" for="startDate">Display From:</form:label>
														${announcement.startDate}
													</div>
												</div>
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="endDate" for="endDate">Display Until:</form:label>
														${announcement.endDate}
													</div>
												</div>
												<c:if test="${not empty announcement.acadSession}">


													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label path="acadSession" for="acadSession">Acad Session:</form:label>
															${announcement.acadSession}
														</div>
													</div>
													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label path="acadYear" for="acadYear">Acad Year:</form:label>
															${announcement.acadYear}
														</div>
													</div>
												</c:if>
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="campusName" for="campusName">Campus:</form:label>
														${announcement.campusName}
													</div>
												</div>
											</div>

											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="sendEmailAlert" for="sendEmailAlert">Send Email Alert for New Announcement:</form:label>
														${announcement.sendEmailAlert}
													</div>
												</div>
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="sendEmailAlertToParents"
															for="sendEmailAlertToParents">Send Email Alert to Parents:</form:label>
														${announcement.sendEmailAlertToParents}
													</div>
												</div>
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="sendSmsAlert" for="sendSmsAlert">Send SMS Alert for New Announcement:</form:label>
														${announcement.sendSmsAlert}
													</div>
												</div>
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="sendSmsAlertToParents"
															for="sendSmsAlertToParents">Send SMS Alert to Parents:</form:label>
														${announcement.sendSmsAlertToParents}
													</div>
												</div>
												<c:if test="${announcement.filePath ne null}">
													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<%-- <form:label path="sendSmsAlert" for="sendSmsAlert"> : </form:label> --%>

															<a class="text-reset" href="sendAnnouncementFile?id=${announcement.id}"
																target="_blank" onclick="return preview(this.href); "><strong>View
																Announcement File</strong></a>



														</div>
													</div>

												</c:if>
											</div>

											<div class="row">
												<div class="col-sm-12 column">
													<form:label path="description" for="description">
														<a data-toggle="collapse" href="#description"
															aria-expanded="true" aria-controls="description">
															Announcement Details: (Expand/Collapse) </a>
													</form:label>
													<div id="description" class="collapse"
														style="border-style: solid; border-width: 1px; padding: 10px; margin-top: 10px">${announcement.description}</div>
												</div>
											</div>
											<br>

											<div class="row">
												<div class="col-sm-8 column">
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
																
																<button class="btn btn-large btn-danger" type="button" name="back" onclick="history.back()">back</button>


														</sec:authorize>

													</div>
												</div>
											</div>


										</form:form>
									</div>
								</div>
							</div>
						</div>

						<!-- Results Panel -->

					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>
	</div>





</body>
</html>
	
	
	
	</sec:authorize>	
	<!-- FACULTY AND STUDENT PAGE -->

<sec:authorize access="hasRole('ROLE_FACULTY') || hasRole('ROLE_STUDENT')">
<jsp:include page="../common/newDashboardHeader.jsp" /> 
</sec:authorize>

<sec:authorize access="hasRole('ROLE_FACULTY') || hasRole('ROLE_STUDENT')">
<div class="d-flex" id="facultyAssignmentPage">
</sec:authorize>
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
		<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
		<jsp:include page="../common/rightSidebarFaculty.jsp"/>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftNavbar.jsp" />
		<jsp:include page="../common/newLeftSidebar.jsp" />
		<jsp:include page="../common/rightSidebar.jsp" />
	</sec:authorize>
<!-- DASHBOARD BODY STARTS HERE -->


<sec:authorize access="hasRole('ROLE_FACULTY') || hasRole('ROLE_STUDENT')">

<div class="container-fluid m-0 p-0 dashboardWraper">

	
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newTopHeaderFaculty.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newTopHeader.jsp" />
	</sec:authorize>
     
     <div class="container mt-5">
                <div class="row">
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
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
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>
	</sec:authorize>
