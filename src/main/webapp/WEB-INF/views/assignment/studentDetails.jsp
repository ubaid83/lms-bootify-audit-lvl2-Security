<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
%>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">



	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<header class="container-fluid sticky-top">
	<nav class="navbar navbar-expand-lg navbar-light p-0">
		<!--                     <i class="fas fa-bars fa-2x mr-3 leftnav-btn" data-toggle="modal" data-target="#leftnav"></i> -->
		<a class="navbar-brand" href="homepage"> <c:choose>
				<c:when test="${instiFlag eq 'nm'}">
					<img src="<c:url value="/resources/images/logo.png" />"
						class="logo" title="NMIMS logo" alt="NMIMS logo" />
				</c:when>
				<c:otherwise>
					<img src="<c:url value="/resources/images/svkmlogo.png" />"
						class="logo" title="SVKM logo" alt="SVKM logo" />
				</c:otherwise>
			</c:choose>

		</a>
		<button class="adminNavbarToggler" type="button"
			data-toggle="collapse" data-target="#adminNavbarCollapse">
			<i class="fas fa-bars"></i>
		</button>

		<div class="collapse navbar-collapse" id="adminNavbarCollapse">
			<ul class="navbar-nav ml-auto">

					<li id="program" class="nav-item active" data-toggle="tooltip"
							data-placement="bottom" title="My Program"><a
							class="nav-link" href="homepage"><i class="fas fa-home"></i>
								<span>Home</span></a></li>

			</ul>
		</div>
	</nav>
</header>

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">

			<!-- page content: START -->
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a
						href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
					<li class="breadcrumb-item" aria-current="page"><c:out
							value="${Program_Name}" /></li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			
			



						

				<!-- Input Form Panel -->
				<div class="card bg-white border">

<div class="table-responsive mt-3 mb-3 testAssignTable">

<table class="table table-hover" >
															<thead>
																<tr>
																	
																	<th>SA Id</th>
																	<th>Username</th>
																	<th>StudentName</th>
																	<th>Submission Status</th>
																	
																	<th>Submission Date</th>
																	<th>File Path</th>
																	<th> IsAcceptDisclaimer </th>
																	<th> AcceptDisclaimerDate </th>
																
																	<th>HashKey</th>
																	<th>LateSubmRemark</th>
																	<th>KeyGenerationTime</th>
																	<th>IsHashKeyLateSubmitted</th>
					</tr>												
					</thead>
															
														
															 <tbody>

																<c:forEach var="allAssignment" items="${studentdetails}"
																	varStatus="status">
																	<tr>
																	<td>${allAssignment.id}</td>
																	<td>${allAssignment.username}</td>
																	<td>${allAssignment.studentName}</td>
																	<td>${allAssignment.submissionStatus}</td>
																	<td>${allAssignment.submissionDate}</td>
																	<td>${allAssignment.studentFilePath}
																		<c:if test="${not empty allAssignment.studentFilePath}">
																			<a href="#renameFilePath${allAssignment.id}" data-toggle="modal"  
	                                            							data-placement="right" title="Edit File Path" class="fas fa-edit fa-lg text-primary"></a>
																		</c:if>
																	</td>
																	<td>${allAssignment.isAcceptDisclaimer}</td>
																	<td>${allAssignment.acceptDisclaimerDate}</td>
																	<td>${allAssignment.hashKey}</td>
																	<td>${allAssignment.lateSubmRemark}</td>
																	<td>${allAssignment.keyGenerationTime}</td>
																	<td>${allAssignment.isHashKeyLateSubmitted}</td>
																	
																			
																	</tr>
																</c:forEach>
															</tbody> 
														</table>
</div>		
							</div>
														<c:forEach var="allAssignment" items="${studentdetails}"
																	varStatus="status">
										<div class="modal fade "
											id="renameFilePath${allAssignment.id}" tabindex="-1" role="dialog"
											aria-labelledby="renameFilePathTitle" aria-hidden="true">
											<div
												class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
												role="document">
												<div class="modal-content">
													<div class="modal-header">
														<h5 class="modal-title" id="renameFilePathTitle">Rename File Path</h5>
														<button type="button" class="close" data-dismiss="modal"
															aria-label="Close">
															<span aria-hidden="true">&times;</span>
														</button>
													</div>
													<div class="modal-body">
									                    <!--FORM ITEMS STARTS HERE-->
									                    <form:form action="${pageContext.request.contextPath}/updateFilePath?assignmentId=${allAssignment.assignmentId}&studentAssignmentId=${allAssignment.id}" method="post" name="updateFilePath" id="updateFilePath"
																				modelAttribute="studentAssn">
										                    <div class="col-lg-10 col-md-10 col-sm-12 mt-4">
										                        <div class="uploadFileText">
																	<label>Enter New File Path</label>
										                            <input id="studentFilePath" class="form-control" name="studentFilePath" type="text" value="${allAssignment.studentFilePath}" />
										                        </div>
										                    </div>
										
										                    <div class="col-lg-10 col-md-10 col-sm-12 mt-3">
																<button type="submit" id="submit" class="btn btn-primary">Submit</button>
																<button id="cancel" type="submit" data-dismiss="modal" class="btn btn-danger" formaction="getStudentDetails" formnovalidate="formnovalidate" value="Cancel">Cancel</button>
										                    </div>
									                    </form:form>
													</div>
														
									</div>
								</div>
			</div>
			</c:forEach>
							
			
			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />
		
		
		
		
		
		
		