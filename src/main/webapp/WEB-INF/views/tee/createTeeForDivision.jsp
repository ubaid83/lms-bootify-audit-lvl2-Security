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
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
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
					<li class="breadcrumb-item" aria-current="page"><c:out
							value="${Program_Name}" /></li>
					<sec:authorize access="hasRole('ROLE_STUDENT')">
						<c:out value="${AcadSession}" />
					</sec:authorize>
					<%
						if (isEdit) {
					%>
					<li class="breadcrumb-item active" aria-current="page">Update
						Division Wise Term End Exam Evaluation</li>
					<%
						} else {
					%>
					<li class="breadcrumb-item active" aria-current="page">Add
						Division Wise Term End Exam Evaluation</li>
					<%
						}
					%>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">
						<%
							if ("true".equals((String) request.getAttribute("edit"))) {
						%>
						Edit Division Wise Term End Exam Evaluation
						<%
							} else {
						%>
						Add Division Wise Term End Exam Evaluation
						<%
							}
						%>
					</h5>

					<form:form action="addTeeForDivision" method="post"
						modelAttribute="teeBean">

						<%
							if (isEdit) {
						%>
						<form:input type="hidden" path="id" value="${teeBean.id}" />
						<%-- <form:input type="hidden" path="moduleId"
							value="${icaBean.moduleId}" />
						<form:input type="hidden" path="acadYear"
							value="${icaBean.acadYear}" />
						<form:input type="hidden" path="acadSession"
							value="${icaBean.acadSession}" />
						<form:input type="hidden" path="programId"
							value="${icaBean.programId}" /> --%>
						<%
							}
						%>
						<div class="row">


							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="teeName" for="teeName">TEE Name <span
											style="color: red">*</span>
									</form:label>
									<form:input path="teeName" type="text" class="form-control"
										required="required" />
								</div>
							</div>

							<%
								if (isEdit) {
							%>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<div class="col-md-4 col-sm-6 col-xs-12 column">
										<div class="form-group">
											<form:label path="acadYear" for="acadYear">Academic Year</form:label>
											<form:select id="acadYear" path="acadYear"
												class="form-control subjectParam" disabled="disabled">
												<option value="${teeBean.acadYear}" selected="selected">${teeBean.acadYear}</option>
											</form:select>
										</div>
									</div>
								</div>
							</div>

							<%
								} else {
							%>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="acadYear" for="acadYear">Academic Year</form:label>
									<form:select id="acadYear" path="acadYear"
										class="form-control subjectParam">
										<option value="" selected disabled hidden>Select
											Academic Year</option>
									<%-- 	<form:options items="${acadYears}" /> --%>
									<form:options items="${acadYearCodeList}" />
									</form:select>
								</div>
							</div>
							<%
								}
							%>
						</div>
						<%-- <c:if test="${edit eq true and showDates eq true}">
							<p>
								<Strong>Note : Once allocated to students feedback name
									cannot be changed!</Strong>
							</p>
							<div class="row">
								<div class="col-md-4 col-sm-12">
									<div class="form-group">
										<form:label path="startDate" for="startDate">Start Date <span
												style="color: red">*</span>
										</form:label>
										<form:input path="endDate" type="datetime-local"
                                                                                          class="form-control" value="${assignment.endDate}"
                                                                                          required="required" />

										<div class='input-group date' id='datetimepicker2'>
											<form:input id="startDate" path="startDate" type="text"
												placeholder="Start Date" class="form-control"
												required="required" readonly="readonly" />
											<span class="input-group-addon"><span
												class="glyphicon glyphicon-calendar"></span> </span>
										</div>

									</div>
								</div>



								<div class="col-md-4 col-sm-12">
									<div class="form-group">
										<form:label path="endDate" for="endDate">End Date <span
												style="color: red">*</span>
										</form:label>
										<form:input path="endDate" type="datetime-local"
                                                                                          class="form-control" value="${assignment.endDate}"
                                                                                          required="required" />

										<div class='input-group date' id='datetimepicker2'>
											<form:input id="endDate" path="endDate" type="text"
												placeholder="End Date" class="form-control"
												required="required" readonly="readonly" />
											<span class="input-group-addon"><span
												class="glyphicon glyphicon-calendar"></span> </span>
										</div>

									</div>
								</div>






							</div>
						</c:if> --%>

						<div class="row">
							<c:if test="${campusList.size()>0 }">

								<%
									if (isEdit) {
								%>
								<div class="col-md-4 col-sm-6 col-xs-12 column">
									<div class="form-group">
										<form:label path="campusId" for="campusId">Program Campus </form:label>


										<form:select id="campus" path="campusId"
											class="form-control subjectParam"
											disabled="disabled">



											<c:if test="${ica.campusId eq null}">
												<option value="" selected disabled hidden>Select
													Campus</option>
											</c:if>

											<c:forEach var="listValue" items="${campusList}">
												<c:if test="${teeBean.campusId eq listValue.campusId}">
													<option value="${listValue.campusId}" selected>${listValue.campusName}</option>
												</c:if>
												<%-- <c:if test="${icaBean.campusId ne listValue.campusId}">
													<option value="${listValue.campusId}">${listValue.campusName}</option>
												</c:if> --%>
											</c:forEach>
										</form:select>
									</div>
								</div>
								<%
									} else {
								%>
								<div class="col-md-4 col-sm-6 col-xs-12 column">
									<div class="form-group">
										<form:label path="campusId" for="campusId">Program Campus </form:label>


										<form:select id="campus" path="campusId"
											class="form-control subjectParam">



											<c:if test="${teeBean.campusId eq null}">
												<option value="" selected disabled hidden>Select
													Campus</option>
											</c:if>

											<c:forEach var="listValue" items="${campusList}">
												<c:if test="${teeBean.campusId eq listValue.campusId}">
													<option value="${listValue.campusId}" selected>${listValue.campusName}</option>
												</c:if>
												<c:if test="${teeBean.campusId ne listValue.campusId}">
													<option value="${listValue.campusId}">${listValue.campusName}</option>
												</c:if>
											</c:forEach>
										</form:select>
									</div>
								</div>
								<%
									}
								%>

							</c:if>



							<%-- 			<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="moduleId" for="moduleId">Session</form:label>
									<form:select id="moduleId" path="moduleId"
										class="form-control subjectParam">
										<form:option value="">Select Session</form:option>
										 <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach>
									</form:select>
								</div>
							</div> --%>


							<%
								if (isEdit) {
							%>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="acadSession" for="acadSession">Acad Session</form:label>
									<form:select id="acadSession" path="acadSession"
										class="form-control subjectParam" disabled="disabled">
										<form:option value="${teeBean.acadSession}">${teeBean.acadSession}</form:option>
										<%--  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach> --%>
									</form:select>
								</div>
							</div>
							<%
								} else {
							%>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="acadSession" for="acadSession">Acad Session</form:label>
									<form:select id="acadSession" path="acadSession"
										class="form-control subjectParam">
										<form:option value="">Select Session</form:option>
										<%--  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach> --%>
									</form:select>
								</div>
							</div>
							<%
								}
							%>
							`
							<%
								if (isEdit) {
							%>
							<div class="col-md-6 col-sm-8 col-xs-12 column">
								<div class="form-group">
									<form:label path="moduleId" for="moduleId">Subject</form:label>
									<form:select id="moduleId" path="moduleId"
										class="form-control subjectParam">
										<form:option value="${teeBean.moduleId}">${moduleName}</form:option>
										<%--  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach> --%>
									</form:select>
								</div>
							</div>
							<%
								} else {
							%>
							<div class="col-md-6 col-sm-8 col-xs-12 column">
								<div class="form-group">
									<form:label path="moduleId" for="moduleId">Subject</form:label>
									<form:select id="moduleId" path="moduleId"
										class="form-control subjectParam">
										<form:option value="">Select Subject</form:option>
										<%--  <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach> --%>
									</form:select>
								</div>
							</div>
							<%
								}
							%>
							<%
								if (isEdit) {
							%>
							<div class="col-sm-6">
								<div class="form-group">

									<label path="" for="groupId">Select Program <span
										style="color: red">*</span></label>
									<form:select id="programIdForProgramWise" path="programId"
										required="required" class="form-control"
										style="overflow: auto;" disabled="disabled">

										<c:forEach var="p" items="${programList}">
											<option value="${p.id}" selected="selected">${p.programName}</option>
										</c:forEach>

									</form:select>
								</div>
							</div>
							<%
								} else {
							%>
							<div class="col-sm-6">
								<div class="form-group">

									<label path="" for="groupId">Select Program <span
										style="color: red">*</span></label>
									<form:select id="programIdForProgramWise" path="programId"
										required="required" class="form-control"
										style="overflow: auto;">



									</form:select>
								</div>
							</div>
							<%
								}
							%>
							<%-- <%
								if (isEdit) {
							%>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<form:label path="assignedFaculty" for="assignedFaculty">Assign Faculty
								<span style="color: red">*</span></form:label>
								<form:select id="assignedFaculty" path="assignedFaculty"
									class="form-control">
									<c:forEach var="uc" items="${facultyList}">
										<c:if test="${icaBean.assignedFaculty eq uc.username}">
											<option value="${uc.username}" selected>${uc.facultyName}-(${uc.username})</option>
										</c:if>
										<c:if test="${icaBean.assignedFaculty ne uc.username}">
											<option value="${uc.username}">${uc.facultyName}-(${uc.username})</option>
										</c:if>
									</c:forEach>
								</form:select>
							</div>

							<%
								} else {
							%>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<form:label path="assignedFaculty" for="assignedFaculty">Assign Faculty</form:label>
								<form:select id="assignedFaculty" path="assignedFaculty"
									class="form-control">
									<form:option value="">Select Faculty</form:option>
									 <c:forEach var="course" items="${allCourses}"
                                                            varStatus="status">
                                                            <form:option value="${course.id}">${course.courseName}</form:option>
                                                      </c:forEach>
								</form:select>
							</div>
							<%
								}
							%> --%>

							<div class="col-md-4 col-sm-6 col-xs-12 column">

								<div class="form-group">
									<form:label path="internalMarks" for="internalMarks">Internal Marks
									</form:label>
									<c:choose>
										<c:when test="${isGradingStart eq 't'}">
											<form:input id="internalMarks" min="1" path="internalMarks"
												type="number" placeholder="Internal Marks"
												class="form-control weightClass" 
												disabled="true" />
										</c:when>
										<c:otherwise>
											<form:input id="internalMarks" min="1" path="internalMarks"
												type="number" placeholder="Internal Marks"
												class="form-control weightClass"  />
										</c:otherwise>
									</c:choose>
								</div>

							</div>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="internalPassMarks" for="internalPassMarks">Internal Passing Marks
									</form:label>
									<c:choose>
										<c:when test="${isGradingStart eq 't'}">
											<form:input id="internalPassMarks" min="1"
												path="internalPassMarks" type="number"
												placeholder="Internal Passing Marks"
												class="form-control weightClass" 
												disabled="true" />
										</c:when>
										<c:otherwise>
											<form:input id="internalPassMarks" min="1"
												path="internalPassMarks" type="number"
												placeholder="Internal Passing Marks"
												class="form-control weightClass"  />
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-md-4 col-sm-6 col-xs-12 column">

								<div class="form-group">
									<form:label path="externalMarks" for="externalMarks">External  Marks <span
											style="color: red">*</span></form:label>
									
									<c:choose>
										<c:when test="${isGradingStart eq 't'}">
											<form:input id="externalMarks" min="1" path="externalMarks"
												type="number" placeholder="External Marks"
												class="form-control weightClass" disabled="true" required="required"/>
										</c:when>
										<c:otherwise>
											<form:input id="externalMarks" min="1" path="externalMarks"
												type="number" placeholder="External Marks"
												class="form-control weightClass" required="required" />
										</c:otherwise>
									</c:choose>




								</div>

							</div>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<div class="form-group">
									<form:label path="externalPassMarks" for="externalPassMarks">
									External Passing Marks<span
											style="color: red">*</span>
									</form:label>

									<c:choose>
										<c:when test="${isGradingStart eq 't'}">
											<form:input id="externalPassMarks" min="1"
												path="externalPassMarks" type="number"
												placeholder="External Passing Marks"
												class="form-control weightClass" disabled="true" required="required"/>
										</c:when>
										<c:otherwise>
											<form:input id="externalPassMarks" min="1"
												path="externalPassMarks" type="number"
												placeholder="External Passing Marks"
												class="form-control weightClass" required="required"/>
										</c:otherwise>
									</c:choose>



								</div>
							</div>

							<div class="col-md-4 col-sm-6 col-xs-12 column">

								<div class="form-group">
									<form:label path="totalMarks" for="totalMarks">Total Marks 
									</form:label>

									<c:choose>
										<c:when test="${isGradingStart eq 't'}">
											<form:input id="totalMarks" min="1" path="totalMarks"
										type="number" placeholder="Total Marks"
										class="form-control weightClass" disabled="true"/>
										</c:when>
										<c:otherwise>
											<form:input id="totalMarks" min="1" path="totalMarks"
										type="number" placeholder="Total Marks"
										class="form-control weightClass" />
										</c:otherwise>
									</c:choose>

									
								</div>
							</div>



						</div>
						<%-- <%if(isEdit){ %>
						
						<div class="row">
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<label class="small"><strong>Start Date</strong> <span
									class="text-danger f-13">*</span></label>
								<div class='input-group date' id='datetimepicker1'>
									<form:input id="startDate" path="startDate" type="text"
										placeholder="Start Date" class="form-control"
										required="required"/>
									<span class="input-group-addon"><span
										class="glyphicon glyphicon-calendar"></span> </span>
								</div>
							</div>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<label class="small"><strong>End Date</strong> <span
									class="text-danger f-13">*</span></label>
								<div class='input-group date' id='datetimepicker2'>
									<form:input id="endDate" path="endDate" type="text"
										placeholder="End Date" class="form-control"
										required="required"  />
									<span class="input-group-addon"><span
										class="glyphicon glyphicon-calendar"></span> </span>
								</div>
							</div>
						</div>
						
						<%}else{ %> --%>
						<%
							if (isEdit) {
						%>
						<input type="hidden" value="${teeBean.startDate}" id="startDateDB" />
						<input type="hidden" value="${teeBean.endDate}" id="endDateDB" />
						<%
							} else {
						%>
						<input type="hidden" value="" id="startDateDB" />
						<input type="hidden" value="" id="endDateDB" />
						<%
							}
						%>
						<div class="row">
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<label class="small"><strong>Start Date</strong> <span
									class="text-danger f-13">*</span></label>
								<div class="input-group">
									<form:input id="startDateIca" path="startDate" type="text"
										placeholder="Start Date" class="form-control"
										required="required" readonly="true" />
									<div class="input-group-append">
										<button class="btn btn-outline-secondary initiateSDatePicker"
											type="button">
											<i class="fas fa-calendar"></i>
										</button>
									</div>
								</div>
							</div>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<label class="small"><strong>End Date</strong> <span
									class="text-danger f-13">*</span></label>

								<div class="input-group">
									<form:input id="endDateIca" path="endDate" type="text"
										placeholder="End Date" class="form-control"
										required="required" readonly="true" />
									<div class="input-group-append">
										<button class="btn btn-outline-secondary initiateEDatePicker"
											type="button">
											<i class="fas fa-calendar"></i>
										</button>
									</div>
								</div>
							</div>
							<div class="col-md-4 col-sm-6 col-xs-12 column">
								<form:label class="textStrong" path="autoAssignMarks"
									for="autoAssignMarks">Auto Assign Marks From Portal Assignment? <span
										style="color: red">*</span>
								</form:label>
								<br>
								<form:radiobutton name="autoAssignMarks" id="YesAE" value="Y"
									path="autoAssignMarks" required="required"/>
								Yes<br>
								<form:radiobutton name="autoAssignMarks" id="NoAE" value="N"
									path="autoAssignMarks" />
								No
							</div>
						</div>
						<%-- <%} %> --%>

						<c:if test="${edit eq 'true'}">
							<div class="row">
								<div class="col-lg-6 col-md-6 col-sm-12 mt-3 mb-3">
									<label class="textStrong">Do You Want To Scale
										(Up/Down) Marks For External??</label><br>
									<c:choose>
									<c:when test="${isGradingStart eq 't'}">
									<form:label path="scaledReq">${teeBean.scaledReq}</form:label>
									</c:when>
									<c:otherwise>
									<div>
									<c:if test="${teeBean.scaledReq eq null}">
										<div class="pretty p-switch p-fill p-toggle">
											<form:checkbox id="scaledReq" value="N"
												path="scaledReq" />
											<div class="state p-success p-on">
												<label>Yes</label>
											</div>
											<div class="state p-danger p-off">
												<label>No</label>
											</div>
										</div>
										</c:if>
									<c:if test="${teeBean.scaledReq ne null}">
										<div class="pretty p-switch p-fill p-toggle">
											<form:checkbox id="scaledReq" value="${teeBean.scaledReq}"
												path="scaledReq" />
											<div class="state p-success p-on">
												<label>Yes</label>
											</div>
											<div class="state p-danger p-off">
												<label>No</label>
											</div>
										</div>
										</c:if>
									</div>
									</c:otherwise>
									</c:choose>

								</div>
								<c:if test="${teeBean.scaledMarks ne null}">
									<div class="form-check pl-0 mt-2">
										<label class="textStrong">Scaled Marks</label>
										<c:choose>
									<c:when test="${isGradingStart eq 't'}">
										<form:input id="scaledMarks" path="scaledMarks" type="number"
											placeholder="Scaled Marks" class="form-control"
											step=".0001" disabled="true"/>
									</c:when>
									<c:otherwise>
									<form:input id="scaledMarks" path="scaledMarks" type="number"
											placeholder="Marks per question" class="form-control"
											step=".0001" />
									</c:otherwise>
									</c:choose>
									</div>
								</c:if>
								<c:if test="${teeBean.scaledMarks eq null}">
									<div class="form-check pl-0 mt-2 d-none">
										<label class="textStrong">Scaled Marks</label>
										<form:input id="scaledMarks" path="scaledMarks" type="number"
											placeholder="Marks per question" class="form-control"
											step=".0001" />
									</div>
								</c:if>

							</div>

						</c:if>
						<c:if test="${edit ne 'true'}">
							<div class="row">
								<div class="col-lg-6 col-md-6 col-sm-12 mt-3 mb-3">
									<label class="textStrong">Do You Want To Scale
										(Up/Down) Marks For External??</label>
									<div>
										<div class="pretty p-switch p-fill p-toggle">
											<form:checkbox id="scaledReq" value="N" path="scaledReq" />
											<div class="state p-success p-on">
												<label>Yes</label>
											</div>
											<div class="state p-danger p-off">
												<label>No</label>
											</div>
										</div>
									</div>

								</div>

								<div class="form-check pl-0 mt-2 d-none">
									<label class="textStrong">Scaled Marks</label>
									<form:input id="scaledMarks" path="scaledMarks" type="number"
										placeholder="Marks per question" class="form-control"
										step=".0001" />
								</div>


							</div>
						</c:if>

						<div class="col-12 mt-3 position-relative">
							<div class="form-group">
								<form:label path="teeDesc" for="teeDesc">TEE Description <span
										style="color: red"></span>
								</form:label>
								<form:textarea rows="5" path="teeDesc" type="text"
									class="form-control w-100" />
							</div>
						</div>

						<div class="col-sm-12 column">
							<div class="form-group">
								<%
									if (isEdit) {
								%>
								<button id="submit" name="submit"
									class="btn btn-large btn-primary" formaction="updateTee">Update
									TEE</button>
								<%
									} else {
								%>

								<button id="submit" name="submit"
									class="btn btn-large btn-primary">Add TEE</button>
								<%
									}
								%>
								<input id="reset" type="reset" class="btn btn-danger">
								<button id="cancel" name="cancel" class="btn btn-danger"
									formnovalidate="formnovalidate" formaction="homepage">Cancel</button>
							</div>
						</div>



					</form:form>
				</div>
			</div>
			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />
		<script>
			$(document).ready(function() {
				$('#moduleId').select2();
				if ($('#scaledReq').val() == "N") {
					$('#scaledReq').attr('checked', false);
				}

			});
		</script>
		<script>
			$(window)
					.bind(
							"pageshow",
							function() {
								var TommorowDate = new Date();
								//start Date picker
								$('.initiateSDatePicker').daterangepicker({
									autoUpdateInput : false,
									minDate : TommorowDate,
									locale : {
										cancelLabel : 'Clear'
									},
									"singleDatePicker" : true,
									"showDropdowns" : true,
									"timePicker" : true,
									"showCustomRangeLabel" : false,
									"alwaysShowCalendars" : true,
									"opens" : "center"
								});

								$('.initiateSDatePicker')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													//validDateTimepicks();
													var fromDate = $(
															'#startDateIca')
															.val();
													var toDate = $(
															'#endDateIca')
															.val();
													var sDate = new Date(
															fromDate);
													var eDate = new Date(toDate);
													console.log("s date-->"
															+ sDate
															+ "e Date-->"
															+ eDate);
													if (sDate > eDate) {
														alert("endate cannot be smaller than startDate");
														$('#startDateIca')
																.val(
																		$(
																				'#startDateDB')
																				.val());
														$('#endDateIca').val(
																$('#endDateDB')
																		.val());
													} else {
														$(this)
																.parent()
																.parent()
																.find('input')
																.val(
																		picker.startDate
																				.format('YYYY-MM-DD HH:mm:ss'));
													}
												});

								$('.initiateSDatePicker').on(
										'cancel.daterangepicker',
										function(ev, picker) {
											$(this).parent().parent().find(
													'input').val(
													$('#startDateDB').val());

										});

								//end Date picker

								$('.initiateEDatePicker').daterangepicker({
									autoUpdateInput : false,
									minDate : TommorowDate,
									locale : {
										cancelLabel : 'Clear'
									},
									"singleDatePicker" : true,
									"showDropdowns" : true,
									"timePicker" : true,
									"showCustomRangeLabel" : false,
									"alwaysShowCalendars" : true,
									"opens" : "center"
								});

								$('.initiateEDatePicker')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													//validDateTimepicks();
													var fromDate = $(
															'#startDateIca')
															.val();
													var toDate = $(
															'#endDateIca')
															.val();
													var eDate = new Date(toDate);
													var sDate = new Date(
															fromDate);
													console
															.log('validate called'
																	+ sDate
																	+ ','
																	+ eDate);
													if (sDate > eDate) {
														alert("endate cannot be smaller than startDate");
														$('#startDateIca')
																.val(
																		$(
																				'#startDateDB')
																				.val());
														$('#endDateIca').val(
																$('#endDateDB')
																		.val());
													} else {
														$(this)
																.parent()
																.parent()
																.find('input')
																.val(
																		picker.startDate
																				.format('YYYY-MM-DD HH:mm:ss'));
													}
												});

								$('.initiateEDatePicker').on(
										'cancel.daterangepicker',
										function(ev, picker) {
											$(this).parent().parent().find(
													'input').val(
													$('#endDateDB').val());

										});

								/* 	function validDateTimepicks() {
										if (($('#startDateIca').val() != '' && $('#startDateIca').val() != null)
												&& ($('#endDateIca').val() != '' && $('#endDateIca').val() != null)) {
											var fromDate = $('#startDateIca').val();
											var toDate = $('#endDateIca').val();
											var eDate = new Date(fromDate);
											var sDate = new Date(toDate);
											console.log('validate called'+ sDate+','+eDate);
											if (sDate < eDate) {
												alert("endate cannot be smaller than startDate");
												$('#startDateIca').val($('#startDateDB').val());
												$('#endDateIca').val($('#endDateDB').val());
											}
										}
									} */

							});
		</script>
		<script>
			var acadSessionGen;
			$(".subjectParam")
					.on(
							'change',
							function() {

								console.log("subject param entered");
								var acadYear = $('#acadYear').val();
								var campusId = $('#campus').val();
								var programId = $('#programId').val();
								var courseid = $('#moduleId').val();
								var acadSession = $('#acadSession').val();

								if (campusId == null) {
									console
											.log("undefined campusId"
													+ campusId);
									//campusId='null';
								}

								if (acadYear && !acadSession) {

									$
											.ajax({
												type : 'GET',
												url : '${pageContext.request.contextPath}/getSessionByParam?'
														+ 'acadYear='
														+ acadYear
														+ '&campusId='
														+ campusId,

												success : function(data) {
													var json = JSON.parse(data);
													console.log('json--->'
															+ json);
													var optionsAsString = "";

													$('#acadSession').find(
															'option').remove();
													optionsAsString += "<option value=\"\" selected disabled> Select Session</option>";
													console.log(json);
													for (var i = 0; i < json.length; i++) {
														var idjson = json[i];
														console.log(idjson);

														for ( var key in idjson) {
															console
																	.log(key
																			+ ""
																			+ idjson[key]);

															console
																	.log('else entered00');
															optionsAsString += "<option value='" +key + "'>"
																	+ idjson[key]
																	+ "</option>";

														}
													}

													$('#acadSession').append(
															optionsAsString);

												}
											});
								}
								
								var isSessionChange=false;
                                if(acadSession){
                                                if(acadSession!=acadSessionGen){
                                                                isSessionChange=true;
                                                }
                                }
                                
								if (acadYear && acadSession && isSessionChange==true) {
									acadSessionGen = acadSession;
									$
											.ajax({
												type : 'GET',
												url : '${pageContext.request.contextPath}/getModuleByParam?'
														+ 'acadYear='
														+ acadYear
														+ '&campusId='
														+ campusId
														+ '&acadSession='
														+ acadSession,
												success : function(data) {
													var json = JSON.parse(data);
													console.log('json--->'
															+ json);
													var optionsAsString = "";

													$('#moduleId').find(
															'option').remove();
													optionsAsString += "<option value=\"\" selected disabled> Select Subject</option>";
													console.log(json);
													for (var i = 0; i < json.length; i++) {
														var idjson = json[i];
														console.log(idjson);

														for ( var key in idjson) {
															console
																	.log(key
																			+ ""
																			+ idjson[key]);
															if (moduleId == idjson[key]) {
																optionsAsString += "<option value='" +key + "' selected>"
																		+ idjson[key]
																		+ "</option>";

															} else {
																console
																		.log('else entered00');
																optionsAsString += "<option value='" +key + "'>"
																		+ idjson[key]
																		+ "</option>";
															}
														}
													}

													$('#moduleId').append(
															optionsAsString);

												}
											});
								} else {
									console.log("no acadYear");
								}
								console.log('course id got' + courseid);

								if (courseid && acadYear) {

									if (acadSession) {
										$
												.ajax(
														{
															url : "${pageContext.request.contextPath}/GetProgramsFromAcadSessionYearModule?acadSession="
																	+ acadSession
																	+ '&moduleId='
																	+ courseid
																	+ '&year='
																	+ acadYear
														})
												.done(
														function(data) {
															console.log("data:"
																	+ data);
															programObj = JSON
																	.parse(data);
															console
																	.log("Size:"
																			+ programObj.length);
															$(
																	"#programIdForProgramWise option")
																	.remove();
															for (var i = 0; i < programObj.length; i++) {
																var a = JSON
																		.stringify(programObj[i]);
																console
																		.log("a is "
																				+ a);
																$(
																		"#programIdForProgramWise")
																		.append(
																				$(
																						'<option>',
																						{
																							value : programObj[i].value,
																							text : programObj[i].text
																						}));

															}

														});

									}

								} else {
									console.log("no course");
								}

							});
		</script>
		<!-- 	<script>
		$("#internalMarks, #externalMarks")
				.on(
						'focusout',
						function() {
						console.log('entered!!!!!!');
							$("#totalMarks")
									.on(
											'focusout',
											function() {
												
												console.log('entered123!!!!!!');
												var totalMarks = $("#totalMarks").val();
												var value2 = parseInt($(
														"#internalMarks").val()) > 0 ? parseInt($(
														"#externalMarks").val())
														: 0;
												var value1 = parseInt($(
														"#internalMarks").val()) > 0 ? parseInt($(
														"#externalMarks").val())
														: 0
												var sumOfValues = value1
														+ value2;
												if (sumOfValues < totalMarks || sumOfValues > totalMarks) {
													alert('total Marks has to match with sum of internal and external marks');
													return false;
												}
											});
						});
	</script> -->

		<script>
			$(function() {

				$('#Yes').on('click', function() {
					$('#scaledMarks').show();

					$("#scaledMarks").prop('required', true);
				});

				$('#No').on('click', function() {
					$('#scaledMarks').hide();

					$("#scaledMarks").prop('required', false);
				});
			});
		</script>

		<script>
			$('.weightClass')
					.change(
							function() {

								var internalMarks = $('#internalMarks').val();
								var internalPassMarks = $('#internalPassMarks')
										.val();
								var externalMarks = $('#externalMarks').val();
								var externalPassMarks = $('#externalPassMarks')
										.val();
								var totalMarks = $('#totalMarks').val();

								if (parseInt(internalPassMarks) > parseInt(internalMarks)) {
									$('#internalPassMarks').val('');
									alert('internal passing score cannot be greater than internal marks');

									return;
								} else if (externalMarks != ''
										&& externalMarks != null
										&& externalPassMarks != ''
										&& externalPassMarks != '') {
									if (parseInt(externalPassMarks) > parseInt(externalMarks)) {
										$('#externalPassMarks').val('');
										alert('external pass score cannot be greater that external marks');
										return;
									} else if (totalMarks != ''
											&& totalMarks != null) {

										var totalM = parseFloat(totalMarks);
										var internalM = parseFloat(internalMarks);
										var externalM = parseFloat(externalMarks);

										var total = internalM + externalM;
										if (parseInt(total) != parseInt(totalMarks)) {
											$('#totalMarks').val(total);
											alert('total marks should match with sum of internal & external marks');
											return;
										}
									}
								}
							});
		</script>