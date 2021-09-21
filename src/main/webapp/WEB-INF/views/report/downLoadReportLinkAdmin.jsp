<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex adminPage dataTableBottom" id="adminPage">
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
					<li class="breadcrumb-item active" aria-current="page">
						Download report</li>
				</ol>
			</nav>
			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->


			<!-- Results Panel -->

			<div class="card bg-white border">
				<div class="card-body">

					<h5 class="text-center pb-2 border-bottom">Download Reports</h5>


					<sec:authorize access="hasRole('ROLE_ADMIN')">

						<div class="panel-group" id="feedbackReportAccordion"
							role="tablist" aria-multiselectable="true">

							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-1">
									<h4 class="panel-title cursor-pointer" data-toggle="collapse"
										data-parent="#feedbackReportAccordion"
										data-target="#collapse-1" aria-expanded="false"
										aria-controls="collapse-1">Program Feedbacks Report</h4>
								</div>
								<div id="collapse-1" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-1">
									<div class="panel-body">

										<form action="downloadTop5FacultyFeedback" method="get">
											<div class="row">
												
												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label class="control-label" for="feedbackTypeProgram">Feedback Type
															<!-- <span style="color: red">*</span> -->
														</label> <select id="feedbackTypeProgram"
															class="form-control facultyparameter" >
															<option value="">Select Feedback Type</option>
															<option value="mid-term">Mid-Term</option>
															<option value="end-term">End-Term</option>
														</select>
													</div>
												</div>
												
												<div class="col-sm-12">
													<div class="form-group">
														<label path="" for="courseQuestionCount">No of
															Course Questions <span style="color: red">*</span>
														</label> <input id="courseQuestCountForProgramwise"
															path="courseQuestionCount" type="number"
															required="required" min="1"
															placeholder="Course Question Count" class="form-control"
															value="" />
													</div>
												</div>


												<div class="col-sm-12">
													<div class="form-group">

														<label path="" for="groupId">Select Acad Session <span
															style="color: red">*</span>
														</label> <select multiple="multiple"
															onchange="myFunctionProgramWise(this, event)"
															id="acadSessionProgramWise" path="" required="required"
															class="form-control" style="overflow: auto;">
															<option value="" selected disabled hidden>Select</option>

															<c:forEach var="listValue" items="${acadSessionList}">
																<option value="${listValue}">${listValue}</option>
															</c:forEach>

														</select>

														<ul id="JSProgramResult">
														</ul>
													</div>
												</div>

												<div class="col-sm-12">
													<div class="form-group">

														<label path="" for="groupId">Select Program <span
															style="color: red">*</span></label> <select multiple="multiple"
															id="programIdForProgramWise" path="" required="required"
															class="form-control" style="overflow: auto;">

														</select>
													</div>
												</div>



												<div class="col-sm-12">
													<a class="btn btn-large btn-primary text-white"
														onclick="submitFormProgramWiseReport()"> Generate
														Report</a>
												</div>


											</div>
											<hr />
										</form>

									</div>
								</div>


							</div>


							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-campus">
									<h4 class="panel-title cursor-pointer" data-toggle="collapse"
										data-parent="#feedbackReportAccordion"
										data-target="#collapse-campus" aria-expanded="false"
										aria-controls="collapse-campus">Feedbacks Report For
										Campus</h4>
								</div>
								<div id="collapse-campus" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-campus">
									<div class="panel-body">

										<form action="downloadTop5FacultyFeedback" method="get">
											<div class="row">
												
												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label class="control-label" for="feedbackTypeQWiseC">Feedback Type
															<!-- <span style="color: red">*</span> -->
														</label> <select id="feedbackTypeQWiseC"
															class="form-control facultyparameter" >
															<option value="">Select Feedback Type</option>
															<option value="mid-term">Mid-Term</option>
															<option value="end-term">End-Term</option>
														</select>
													</div>
												</div>
												
												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label path="" for="courseQuestionCount">No of
															Course Questions <span style="color: red">*</span>
														</label> <input id="courseQuestCountForQuestionwiseAndCampus"
															path="courseQuestionCount" type="number"
															required="required" min="0"
															placeholder="Course Question Count" class="form-control"
															value="" />
													</div>
												</div>


												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label class="control-label" for="course">acadYear
															<span style="color: red">*</span>
														</label> <select id="acadYearForAcadSessionForCampus"
															class="form-control facultyparameter" required="required">
															<option value="">Select Acad Year</option>

															<c:forEach var="listValue" items="${yearList}">
																<option value="${listValue}">${listValue}</option>
															</c:forEach>
														</select>
													</div>
												</div>


												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="groupId">Select Acad Session <span
															style="color: red">*</span>
														</label> <select onchange="myFunctionCampusResult(this, event)"
															id="acadSessionQuestionWiseAndCampus" path=""
															required="required" class="form-control"
															style="overflow: auto;">
															<option value="" selected disabled hidden>Select</option>

															<c:forEach var="listValue" items="${acadSessionList}">
																<option value="${listValue}">${listValue}</option>
															</c:forEach>

														</select>

														<ul id="JSResultCampus">
														</ul>
													</div>
												</div>

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="groupId">Select Program <span
															style="color: red">*</span></label> <select multiple="multiple"
															id="programIdCampus" path="" required="required"
															class="form-control" style="overflow: auto;">

															<%-- <form:options items="${acadSessionList}" /> --%>
															<%-- <c:forEach var="listValue" items="${acadSessionList}">
																			<option value="${listValue}">${listValue}</option>
																		</c:forEach> --%>

														</select>
													</div>
												</div>

												<div class="col-sm-12">
													<div class="form-group">
														<label class="control-label" for="course">Campus <!-- <span
															style="color: red">*</span> -->
														</label> <select id="campus" class="form-control facultyparameter"
															required="required">
															<option value="" selected disabled hidden>Select
																Campus</option>

															<c:forEach var="listValue" items="${campusList}">
																<option value="${listValue.campusId}">${listValue.campusName}</option>
															</c:forEach>
														</select>
													</div>
												</div>






												<div class="col-sm-12">
													<a class="btn btn-large btn-primary text-white"
														onclick="submitFormCampusWiseReport()"> Generate
														Report</a>
												</div>
											</div>
											<hr />
										</form>

									</div>
								</div>


							</div>


							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-2">
									<h4 class="panel-title cursor-pointer" data-toggle="collapse"
										data-parent="#feedbackReportAccordion"
										data-target="#collapse-2" aria-expanded="false"
										aria-controls="collapse-2">QuestionWise Feedbacks Report
									</h4>
								</div>
								<div id="collapse-2" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-2">
									<div class="panel-body">

										<form action="downloadTop5FacultyFeedback" method="get">
											<div class="row">
												
												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label class="control-label" for="feedbackTypeQWise">Feedback Type
															<!-- <span style="color: red">*</span> -->
														</label> <select id="feedbackTypeQWise"
															class="form-control facultyparameter" >
															<option value="">Select Feedback Type</option>
															<option value="mid-term">Mid-Term</option>
															<option value="end-term">End-Term</option>
														</select>
													</div>
												</div>
												
												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label path="" for="courseQuestionCount">No of
															Course Questions <span style="color: red">*</span>
														</label> <input id="courseQuestCountForQuestionwise"
															path="courseQuestionCount" type="number"
															required="required" min="0"
															placeholder="Course Question Count" class="form-control"
															value="" />
													</div>
												</div>

												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label class="control-label" for="course">acadYear
															<span style="color: red">*</span>
														</label> <select id="acadYearQWise"
															class="form-control facultyparameter" required="required">
															<option value="">Select Acad Year</option>

															<c:forEach var="listValue" items="${yearList}">
																<option value="${listValue}">${listValue}</option>
															</c:forEach>
														</select>
													</div>
												</div>

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="groupId">Select Acad Session <span
															style="color: red">*</span>
														</label> <select onchange="myFunction(this, event)"
															id="acadSessionQuestionWise" path="" required="required"
															class="form-control" style="overflow: auto;">
															<option value="" selected disabled hidden>Select</option>

															<c:forEach var="listValue" items="${acadSessionList}">
																<option value="${listValue}">${listValue}</option>
															</c:forEach>

														</select>

														<ul id="JSResult">
														</ul>
													</div>
												</div>

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="groupId">Select Program <span
															style="color: red">*</span></label> <select multiple="multiple"
															id="programId" path="" required="required"
															class="form-control" style="overflow: auto;">

															<%-- <form:options items="${acadSessionList}" /> --%>
															<%-- <c:forEach var="listValue" items="${acadSessionList}">
																			<option value="${listValue}">${listValue}</option>
																		</c:forEach> --%>

														</select>
													</div>
												</div>



												<div class="col-sm-12">
													<a class="btn btn-large btn-primary text-white"
														onclick="submitFormQuestionWiseReport()"> Generate
														Report</a>
												</div>

											</div>
										</form>
										<hr />
									</div>
								</div>


							</div>


							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-3">
									<h4 class="panel-title" data-toggle="collapse"
										data-parent="#feedbackReportAccordion"
										data-target="#collapse-3" aria-expanded="false"
										aria-controls="collapse-3">Feedback Report Range Wise</h4>
								</div>
								<div id="collapse-3" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-3">
									<div class="panel-body">

										<form action="downloadFeedbackReportRangeWise" method="get">
											<div class="row">
												
												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label class="control-label" for="feedbackTypeRangeWise">Feedback Type
															<!-- <span style="color: red">*</span> -->
														</label> <select id="feedbackTypeRangeWise"
															class="form-control facultyparameter" >
															<option value="">Select Feedback Type</option>
															<option value="mid-term">Mid-Term</option>
															<option value="end-term">End-Term</option>
														</select>
													</div>
												</div>
												
												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label path="" for="courseQuestionCount">No of
															Course Questions <span style="color: red">*</span>
														</label> <input id="courseQuestCount" path="courseQuestionCount"
															type="number" required="required" min="1"
															placeholder="Course Question Count" class="form-control"
															value="" />
													</div>
												</div>
												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="groupId">Select Term1 Acad
															Session <span style="color: red">*</span>
														</label> <select multiple="multiple" id="term1AcadSession" path=""
															required="required" class="form-control"
															style="overflow: auto;">

															<%-- <form:options items="${acadSessionList}" /> --%>
															<c:forEach var="listValue" items="${acadSessionList}">
																<option value="${listValue}">${listValue}</option>
															</c:forEach>

														</select>
													</div>
												</div>

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="groupId">Select Term2 Acad
															Session <span style="color: red">*</span>
														</label> <select multiple="multiple" id="term2AcadSession" path=""
															required="required" class="form-control"
															style="overflow: auto;">
															<%-- <form:option value="Semester II">Semester II</form:option>
																							<form:option value="Semester IV">Semester IV</form:option> --%>
															<c:forEach var="listValue" items="${acadSessionList}">
																<option value="${listValue}">${listValue}</option>
															</c:forEach>

															<%-- <options items="${acadSessionList}" /> --%>


														</select>
													</div>
												</div>


												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label class="control-label" for="course">acadYear
															<span style="color: red">*</span>
														</label> <select id="acadYearRWise"
															class="form-control facultyparameter" required="required">
															<option value="">Select Acad Year</option>
															<option value="2016-2017">2016-17</option>
															<option value="2017-2018">2017-18</option>
															<option value="2018-2019">2018-19</option>
															<option value="2019-2020">2019-20</option>
															<option value="2020-2021">2020-21</option>
															<option value="2021-2022">2021-22</option>
															<option value="2022-2023">2022-23</option>

														</select>
													</div>
												</div>

												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label class="control-label" for="course">Campus </label>
														<select id="campusR" class="form-control facultyparameter"
															required="required">
															<option value="" selected disabled hidden>Select
																Campus</option>

															<c:forEach var="listValue" items="${campusList}">
																<option value="${listValue.campusId}">${listValue.campusName}</option>
															</c:forEach>
														</select>
													</div>
												</div>

												<div class="col-sm-12">

													<a class="btn btn-large btn-primary" onclick="submitForm()">Generate
														Report</a>
												</div>


											</div>
										</form>
										<hr />

									</div>
								</div>


							</div>

							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-4">
									<h4 class="panel-title" data-toggle="collapse"
										data-parent="#feedbackReportAccordion"
										data-target="#collapse-4" aria-expanded="false"
										aria-controls="collapse-4">Top Faculties Average-Wise</h4>
								</div>
								<div id="collapse-4" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-4">
									<div class="panel-body">

										<form action="downloadTop5FacultyFeedback" method="get">
											<div class="row">


												<div class="col-md-6 col-12">
													<div class="form-group">

														<label path="" for="groupId">Select Term1 Acad
															Session <span style="color: red">*</span>
														</label> <select multiple="multiple" id="acadSessionTopFive"
															path="" required="required" class="form-control"
															style="overflow: auto;">

															<%-- <form:options items="${acadSessionList}" /> --%>
															<c:forEach var="listValue" items="${acadSessionList}">
																<option value="${listValue}">${listValue}</option>
															</c:forEach>

														</select>
													</div>
												</div>

												<div class="col-md-6 col-12">
													<div class="form-group">
														<label class="control-label" for="course">Campus <span
															style="color: red"></span>
														</label> <select id="campusIdTop5"
															class="form-control facultyparameter" required="required">
															<option value="" selected disabled hidden>Select
																Campus</option>

															<c:forEach var="listValue" items="${campusList}">
																<option value="${listValue.campusId}">${listValue.campusName}</option>
															</c:forEach>
														</select>
													</div>
												</div>

												<div class="col-md-6 col-12">
													<div class="form-group">
														<label class="control-label" for="course">acadYear
															<span style="color: red">*</span>
														</label> <select id="acadYearQWiseTop5"
															class="form-control facultyparameter" required="required">
															<option value="" selected disabled hidden>Select
																Acad Year</option>

															<c:forEach var="listValue" items="${yearList}">
																<option value="${listValue}">${listValue}</option>
															</c:forEach>
														</select>
													</div>
												</div>


												<div class="col-12">
													<a class="btn btn-large btn-primary"
														onclick="submitFormtop5Feedback()"> Generate Report</a>
												</div>

											</div>
										</form>
										<hr />

									</div>
								</div>


							</div>

							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-5">
									<h4 class="panel-title" data-toggle="collapse"
										data-parent="#feedbackReportAccordion"
										data-target="#collapse-5" aria-expanded="false"
										aria-controls="collapse-5">Feedbacks Report as per
										Program vs Area</h4>
								</div>
								<div id="collapse-5" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-5">
									<div class="panel-body">

										<form action="downloadDepartmentWiseFeedback" method="get">
											<div class="row">
												<div class="col-12 form-group">

													<label path="" for="groupId">Select Term1 Acad
														Session <span style="color: red">*</span>
													</label> <select multiple="multiple" id="acadSessionDepart" path=""
														required="required" class="form-control"
														style="overflow: auto;">

														<%-- <form:options items="${acadSessionList}" /> --%>
														<c:forEach var="listValue" items="${acadSessionList}">
															<option value="${listValue}">${listValue}</option>
														</c:forEach>

													</select>
												</div>

												<div class="col-12">
													<a class="btn btn-large btn-primary"
														onclick="submitFormDepartFeedbackReport()"> Generate
														Report</a>
												</div>
											</div>

										</form>
										<hr />
									</div>
								</div>


							</div>

							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-6">
									<h4 class="panel-title" data-toggle="collapse"
										data-parent="#feedbackReportAccordion"
										data-target="#collapse-6" aria-expanded="false"
										aria-controls="collapse-6">Feedback Report Department
										Faculty Term Wise</h4>
								</div>
								<div id="collapse-6" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-6">
									<div class="panel-body">

										<form action="downloadDepartmentFacultyTermWiseFeedback"
											method="get">
											<div class="row">

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="groupId">Select Term1 Acad
															Session <span style="color: red">*</span>
														</label> <select multiple="multiple"
															id="term1AcadSessionDeptFaculty" path=""
															required="required" class="form-control"
															style="overflow: auto;">

															<%-- <form:options items="${acadSessionList}" /> --%>
															<c:forEach var="listValue" items="${acadSessionList}">
																<option value="${listValue}">${listValue}</option>
															</c:forEach>

														</select>
													</div>
												</div>

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="groupId">Select Term2 Acad
															Session <span style="color: red">*</span>
														</label> <select multiple="multiple"
															id="term2AcadSessionDeptFaculty" path=""
															required="required" class="form-control"
															style="overflow: auto;">
															<%-- <form:option value="Semester II">Semester II</form:option>
																							<form:option value="Semester IV">Semester IV</form:option> --%>
															<c:forEach var="listValue" items="${acadSessionList}">
																<option value="${listValue}">${listValue}</option>
															</c:forEach>

															<%-- <options items="${acadSessionList}" /> --%>


														</select>
													</div>
												</div>

												<div class="col-sm-12">

													<a class="btn btn-large btn-primary"
														onclick="submitFormDepartFacultyFeedbackReport()">Generate
														Report</a>

												</div>
											</div>
										</form>
										<hr />

									</div>
								</div>


							</div>



							<!-- ------------------Utility Report------------------ -->

							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-7">
									<h4 class="panel-title" data-toggle="collapse"
										data-parent="#feedbackReportAccordion"
										data-target="#collapse-7" aria-expanded="false"
										aria-controls="collapse-7">Student Portal Utility Report
										- Summary</h4>
								</div>
								<div id="collapse-7" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-7">
									<div class="panel-body">

										<form action="featureWiseUtilisationReport" method="get">
											<div class="row">

												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label class="control-label" for="campus1">Campus</label>
														<select id="campus1" class="form-control facultyparameter">
															<option value="" selected disabled hidden>Select
																Campus</option>
															<c:forEach var="listValue" items="${campusList}">
																<option value="${listValue.campusId}">${listValue.campusName}</option>
															</c:forEach>
														</select>
													</div>
												</div>


												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="startDate">From Date <span
															style="color: red">*</span></label>

														<div class='input-group date' id='datetimepicker1'>
															<input id="startDate" type="text" placeholder="From Date"
																class="form-control" required="required" readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="endDate">To Date <span
															style="color: red">*</span></label>

														<div class='input-group date' id='datetimepicker2'>
															<input id="endDate" type="text" placeholder="To Date"
																class="form-control" required="required" readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>


												<div class="col-12">
													<a class="btn btn-large btn-primary"
														onclick="submitFeatureWiseUtilisationReport()">
														Generate Report</a>
												</div>

											</div>
										</form>
										<hr />

									</div>
								</div>


							</div>


							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-8">
									<h4 class="panel-title" data-toggle="collapse"
										data-parent="#feedbackReportAccordion"
										data-target="#collapse-8" aria-expanded="false"
										aria-controls="collapse-8">Student Portal Utility Report
										- Detailed</h4>
								</div>
								<div id="collapse-8" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-8">
									<div class="panel-body">

										<form action="featureWiseDetailedUtilisationReport"
											method="get">
											<div class="row">

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="startDate">From Date <span
															style="color: red">*</span></label>

														<div class='input-group date' id='datetimepicker3'>
															<input id="startDate1" type="text"
																placeholder="From Date" class="form-control"
																required="required" readonly="true" /> <span
																class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="endDate">To Date <span
															style="color: red">*</span></label>

														<div class='input-group date' id='datetimepicker4'>
															<input id="endDate1" type="text" placeholder="To Date"
																class="form-control" required="required" readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>


												<div class="col-md-6 col-sm-12">
													<a class="btn btn-large btn-primary"
														onclick="submitFeatureWiseDetailedUtilisationReport()">
														Generate Report</a>
												</div>

												<!-- <button id="submit"
                                                                                                                              class="btn btn-large btn-primary"
                                                                                                                              formaction="downloadFeedbackReportRangeWise">Generate Report</button> -->
											</div>
										</form>
									</div>
								</div>


							</div>

							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-9">
									<h4 class="panel-title" data-toggle="collapse"
										data-parent="#feedbackReportAccordion"
										data-target="#collapse-9" aria-expanded="false"
										aria-controls="collapse-9">Mark Attendance Report Faculty
										Wise</h4>
								</div>
								<div id="collapse-9" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-9">
									<div class="panel-body">

										<form action="downloadAttendanceReport" method="get">
											<div class="row">

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="ofDate">Select Date <span
															style="color: red">*</span></label>

														<div class='input-group date' id='datetimepicker5'>
															<input id="ofDate" type="text" placeholder="Select Date"
																class="form-control" required="required" readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6 col-sm-12">
													<a class="btn btn-large btn-primary"
														onclick="submitMarkAttendanceReport()"> Generate
														Report</a>
												</div>

												<!-- <button id="submit"
                                                                                                                              class="btn btn-large btn-primary"
                                                                                                                              formaction="downloadFeedbackReportRangeWise">Generate Report</button> -->
											</div>
										</form>
									</div>
								</div>
							</div>
							<!-- Faculty Feedback Letter For KPMSoL -->


						<c:if test="${app eq 'KPMSoL'}">
							
							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-10">
									<h4 class="panel-title">

							<%-- <c:if test="${app eq 'KPMSoL'}">
								<div class="panel panel-default">
									<div class="panel-heading" role="tab" id="heading-10">
										<h4 class="panel-title">

										<a data-toggle="collapse"
											data-parent="#feedbackReportAccordion" href="#collapse-10"
											aria-expanded="false" aria-controls="collapse-7"><strong>
												Faculty Feedback Letter</strong></a>
									</h4>  --%>
										<h4 class="panel-title" data-toggle="collapse"
											data-parent="#feedbackReportAccordion"
											data-target="#collapse-10" aria-expanded="false"
											aria-controls="collapse-10">Faculty Feedback Letter</h4>
									</div>
									<div id="collapse-10" class="panel-collapse collapse"
										role="tabpanel" aria-labelledby="heading-10">
										<div class="panel-body">

											<form action="downloadFacultyFeedbackLetterForAllProgramsAlt"
												method="post" enctype="multipart/form-data">

												<div class="row">
													<div class="col-md-6 col-sm-12">
														<div class="form-group">
															<label path="" for="username">Faculty Id </label> <input
																id="facultyId" type="text" name="username"
																placeholder="Faculty SAPID" class="form-control"
																value="" />
														</div>
													</div>

													<div class="col-md-6 col-sm-12">
														<div class="form-group">
															<label path="" for="username">Term <span
																style="color: red">*</span>
															</label> <input id="term" type="text" required="required"
																placeholder="Enter a Term" class="form-control" value=""
																name="term" />
														</div>
													</div>

													<div class="col-md-6 col-sm-12">
														<div class="form-group">
															<label path="" for="username">Dean <span
																style="color: red">*</span>
															</label> <input id="dean" type="text" required="required"
																placeholder="Enter a College Dean Name"
																class="form-control" value="" name="dean" />
														</div>
													</div>

													<div class="col-md-6 col-sm-12">
														<div class="form-group">
															<label class="control-label" for="course">Campus
															</label> <select id="campusIdFeedbackLetter"
																class="form-control facultyparameter" name="campusId">
																<option value="" selected disabled hidden>Select
																	Campus</option>

																<c:forEach var="listValue" items="${campusList}">
																	<option value="${listValue.campusId}">${listValue.campusName}</option>
																</c:forEach>
															</select>
														</div>
													</div>

													<div class="col-md-6 col-sm-12">
														<div class="form-group">
															<label class="control-label" for="course">acadYear
																<span style="color: red">*</span>
															</label> <select id="acadYearFeedbackLetter"
																class="form-control facultyparameter"
																required="required" name="acadYear">
																<option value="" selected disabled hidden>Select
																	Acad Year</option>

																<c:forEach var="listValue" items="${yearList}">
																	<option value="${listValue}">${listValue}</option>
																</c:forEach>
															</select>
														</div>
													</div>

													<!--<div class="col-md-6 col-sm-12" id="studnetSign">
													<div class="form-group">
														<label>Upload Dean's Signature <span
															style="color: red">*</span>Note: Image width should be
															160px and height should be 60px.
														</label> <input id="file2" name="file2" type="file"
															class="form-control files" required="required" />

													</div>
												</div>-->

													<div class="col-md-6 col-sm-12">
														<div class="form-group">

															<label path="" for="groupId">Select Acad Session
																For Term <span style="color: red">*</span>
															</label> <select multiple="multiple" id="acadSessionForTerm"
																path="" required="required" class="form-control"
																style="overflow: auto;" name="acadSessionList">
																<%-- <form:option value="Semester II">Semester II</form:option>
																							<form:option value="Semester IV">Semester IV</form:option> --%>
																<c:forEach var="listValue" items="${acadSessionList}">
																	<option value="${listValue}">${listValue}</option>
																</c:forEach>

																<%-- <options items="${acadSessionList}" /> --%>


															</select>
														</div>
													</div>

												</div>

												<a class="btn btn-large btn-primary"
													onclick="submitForFacultyLetter()"> Generate Report</a>
												<!--<input type="submit" value="Generate Report" />

											 	<button id="submit"
																					class="btn btn-large btn-primary"
																					formaction="downloadFacultyFeedbackLetterForAllProgramsAlt">Generate Report</button> 
 -->
											</form>
										</div>
									</div>
								</div>



							</c:if>

							<!-- Added New LMS Utility Format Form -->



							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-71">
									<h4 class="panel-title" data-toggle="collapse"
										data-parent="#feedbackReportAccordion"
										data-target="#collapse-71" aria-expanded="false"
										aria-controls="collapse-71">Student-Portal Utilization
										Report(Consolidated)</h4>
								</div>
								<div id="collapse-71" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-71">
									<div class="panel-body">

										<form action="featureWiseUtilisationReport" method="get">
											<div class="row">

												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<label class="control-label" for="campus1">Campus</label>
														<select id="campus11" class="form-control facultyparameter">
															<option value="" selected disabled hidden>Select
																Campus</option>
															<c:forEach var="listValue" items="${campusList}">
																<option value="${listValue.campusId}">${listValue.campusName}</option>
															</c:forEach>
														</select>
													</div>
												</div>


												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="startDate">From Date <span
															style="color: red">*</span></label>

														<div class='input-group date' id='datetimepicker1'>
															<input id="startDate11" type="text" placeholder="From Date"
																class="form-control" required="required" readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>

												<div class="col-md-6 col-sm-12">
													<div class="form-group">

														<label path="" for="endDate">To Date <span
															style="color: red">*</span></label>

														<div class='input-group date' id='datetimepicker2'>
															<input id="endDate11" type="text" placeholder="To Date"
																class="form-control" required="required" readonly="true" />
															<span class="input-group-addon"><span
																class="glyphicon glyphicon-calendar"></span> </span>
														</div>

													</div>
												</div>


												<div class="col-12">
													<a class="btn btn-large btn-primary"
														onclick="submitFeatureWiseConsoliUtilisationReport()">
														Generate Report</a>
												</div>

											</div>
										</form>
										<hr />

									</div>
								</div>


							</div>
							
							<!-- Assignment Report -->
							
							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-72">
									<h4 class="panel-title" data-toggle="collapse"
										data-parent="#feedbackReportAccordion"
										data-target="#collapse-72" aria-expanded="false"
										aria-controls="collapse-72">Assignment Report Question
										Wise</h4>
								</div>
								<div id="collapse-72" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-72">
									<div class="panel-body">

										<form action="downloadAttendanceReport" method="get">
											<div class="row">

												<div class="col-md-6 col-sm-12">
													<div class="form-group">
														<div class="row">
															<div class="col-md-6 col-sm-12">
															<label>Select Course <span class="text-danger">*</span></label>
															</div>
														</div>
														<div class="row">
															<div class="col-md-6 col-sm-12">
															<select id="idForCourse" path="courseId" onchange="myFunctionAssignmentWise(this, event)"
																class="form-control" required="required" style="width: 400px;">
																<option value="" selected>--Select Course--</option>
																<c:forEach var='cList' items='${allCourses}'>
																	
																	<option value="${cList.id}">${cList.courseName} - (${cList.acadYear})</option>
																	
																</c:forEach>
															</select>
															
															<ul id="JSAssignmentResult" style="display: none;">
															</ul>
															</div>
														</div>
													</div>
												</div>
												
												<div class="col-md-6 col-sm-12">
													<div class="form-group">
															<label>Select Assignment <span class="text-danger">*</span></label>
															<select id="idForAssignment"
																class="form-control" required="required">
																<option value="" selected>--Select Assignment--</option>
															</select>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-6 col-sm-12">
													<a class="btn btn-large btn-primary"
														onclick="submitAssignmentsReport()"> Generate
														Report</a>
												</div>

												<!-- <button id="submit"
                                                                                                                              class="btn btn-large btn-primary"
                                                                                                                              formaction="downloadFeedbackReportRangeWise">Generate Report</button> -->
											</div>
										</form>
									</div>
								</div>
							</div>
							
							<!-- End -->

						</div>
					</sec:authorize>

					<sec:authorize access="hasRole('ROLE_FACULTY')">
						<div class="x_panel">

							<div class="panel panel-default">
								<div class="panel-heading" role="tab" id="heading-2">
									<h4 class="panel-title">
										<a data-toggle="collapse"
											data-parent="#feedbackReportAccordion" href="#collapse-2"
											aria-expanded="false" aria-controls="collapse-2"><strong>QuestionWise
												Feedbacks Report</strong></a>
									</h4>
								</div>
								<div id="collapse-2" class="panel-collapse collapse"
									role="tabpanel" aria-labelledby="heading-2">
									<div class="panel-body">
										<div class="row">
											<form action="downloadTop5FacultyFeedback" method="get">

												<div class="col-sm-6">
													<div class="form-group">
														<label path="" for="courseQuestionCount">No of
															Course Questions</label> <input
															id="courseQuestCountForQuestionwise"
															path="courseQuestionCount" type="number"
															required="required" min="0"
															placeholder="Course Question Count" class="form-control"
															value="" />
													</div>
												</div>

												<div class="col-sm-6">
													<div class="form-group">
														<label class="control-label" for="course">acadYear</label>
														<select id="acadYearQWise"
															class="form-control facultyparameter" required="required">
															<option value="">Select Acad Year</option>

															<c:forEach var="listValue" items="${yearList}">
																<option value="${listValue}">${listValue}</option>
															</c:forEach>
														</select>
													</div>
												</div>

												<%-- <div class="col-sm-6">
																	<div class="form-group">

																		<label path="" for="groupId">Select Acad
																			Session</label> <select onchange="myFunction(this, event)"
																			id="acadSessionQuestionWise" path=""
																			required="required" class="form-control"
																			style="overflow: auto;">
																			<option value="" selected disabled hidden>Select</option>

																			<c:forEach var="listValue" items="${acadSessionList}">
																				<option value="${listValue}">${listValue}</option>
																			</c:forEach>

																		</select>

																		<ul id="JSResult">
																		</ul>
																	</div>
																</div> --%>

												<%-- <div class="col-sm-6">
																	<div class="form-group">

																		<label path="" for="groupId">Select Program</label> <select
																			multiple="multiple" id="programId" path=""
																			required="required" class="form-control"
																			style="overflow: auto;">

																			<form:options items="${acadSessionList}" />
																			<c:forEach var="listValue" items="${acadSessionList}">
																			<option value="${listValue}">${listValue}</option>
																		</c:forEach>

																		</select>
																	</div>
																</div> --%>




												<a class="btn btn-large btn-primary"
													onclick="submitFormQuestionWiseReportForFacultyFeedback()">
													Generate Report</a>

												<!-- 	<button id="submit"
																					class="btn btn-large btn-primary"
																					formaction="downloadFeedbackReportRangeWise">Generate Report</button> -->

											</form>
										</div>
									</div>
								</div>


							</div>
						</div>
					</sec:authorize>



					<%-- <jsp:include page="../common/paginate.jsp">
									<jsp:param name="baseUrl" value="searchList" />
								</jsp:include> --%>
				</div>



			</div>

		</div>

		<!-- /page content: END -->


		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->
		<jsp:include page="../common/newAdminFooter.jsp" />















		<%-- 			<script type="text/javascript"
				src="<c:url value="/resources/js/customDateTimePicker.js" />"></script> --%>


		<script>
			$(document).ready(function() {
				$('#JSResult').hide();
				$('#JSProgramResult').hide();

				//var programObj = eval('('+'${programList}'+')'); 

				/* $("#programIdForProgramWise option").remove();
				  for(var i=0 ;i<programObj.length;i++){
				   var a = JSON.stringify(programObj[i]);
				  
				   $("#programIdForProgramWise").append($('<option>', {value:programObj[i].value, text: programObj[i].text}));
				
				  }  */

			});

			function submitForm() {

				var term1 = $('#term1AcadSession').val();//document.getElementById("term2AcadSession");
				var term2 = $('#term2AcadSession').val();
				var courseQuestCount = $('#courseQuestCount').val();
				var acadYear = $('#acadYearRWise').val();
				var campusId = $('#campusR').val();
				var feedbackType = $('#feedbackTypeRangeWise').val();

				if (term1 != null && term2 != null && courseQuestCount != null
						&& acadYear != null && courseQuestCount > 0) {

					window.location.href = '${pageContext.request.contextPath}/downloadFeedbackReportRangeWiseNew?'
							+ 'term1AcadSession='
							+ term1
							+ '&term2AcadSession='
							+ term2
							+ '&courseQuestnCount='
							+ courseQuestCount
							+ '&acadYear=' + acadYear 
							+ '&campusId=' + campusId
							+ '&feedbackType=' + feedbackType;
				} else {
					alert("please fill all the fields");
				}

			}

			/* function submitForm() {

				var term1 = $('#term1AcadSession').val();//document.getElementById("term2AcadSession");
				var term2 = $('#term2AcadSession').val();
				var courseQuestCount = $('#courseQuestCount').val();

				if (term1 != null && term2 != null && courseQuestCount != null
						&& courseQuestCount > 0) {

					window.location.href = '${pageContext.request.contextPath}/downloadFeedbackReportRangeWise?'
							+ 'term1AcadSession='
							+ term1
							+ '&term2AcadSession='
							+ term2 + '&courseQuestnCount=' + courseQuestCount;
				} else {
					alert("please fill all the fields");
				}

			} */

			/* function submitFormtop5Feedback() {
				var acadSession = $('#acadSessionTopFive').val();

				if (acadSession != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadTop5FacultyFeedback?'
							+ 'acadSession=' + acadSession;

				} else {
					alert("please fill all the fields");
				}

			} */

			function submitFormtop5Feedback() {
				var acadSession = $('#acadSessionTopFive').val();

				var acadYear = $('#acadYearQWiseTop5').val();

				var campusId = $('#campusIdTop5').val();

				if (acadSession != null && acadYear != null) {

					if (campusId != null) {
						window.location.href = '${pageContext.request.contextPath}/downloadTop5FacultyFeedbackNew?'
								+ 'acadSession='
								+ acadSession
								+ '&acadYear='
								+ acadYear + '&campusId=' + campusId;
					} else {
						window.location.href = '${pageContext.request.contextPath}/downloadTop5FacultyFeedbackNew?'
								+ 'acadSession='
								+ acadSession
								+ '&acadYear='
								+ acadYear;
					}

				} else {
					alert("please fill all the fields");
				}

			}

			function submitFormDepartFeedbackReport() {

				var acadSession = $('#acadSessionDepart').val();

				if (acadSession != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadDepartmentWiseFeedback?'
							+ 'acadSession=' + acadSession;

				} else {
					alert("please fill all the fields");
				}

			}

			function submitFormDepartFacultyFeedbackReport() {
				var acadSessionTerm1 = $('#term1AcadSessionDeptFaculty').val();
				var acadSessionTerm2 = $('#term2AcadSessionDeptFaculty').val();

				if (acadSessionTerm1 != null && acadSessionTerm2 != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadDepartmentFacultyTermWiseFeedback?'
							+ 'term1AcadSession='
							+ acadSessionTerm1
							+ '&term2AcadSession=' + acadSessionTerm2;

				} else {
					alert("please fill all the fields");
				}

			}

			function myFunctionCampusResult(element, event) {

				var result = document.getElementById('JSResultCampus');
				while (result.firstChild)
					result.removeChild(result.firstChild);
				var values = [];
				for (var i = 0; i < element.options.length; i++) {
					if (element.options[i].selected) {
						var li = document.createElement('li');
						li.appendChild(document
								.createTextNode(element.options[i].value));
						result.appendChild(li);
					}
				}

				var acadList = document.getElementById('JSResultCampus')
						.getElementsByTagName("LI");

				var programObj = [];
				console.log(acadList.length);
				var acadSession = [];

				for (var i = 0; i < acadList.length; i++) {
					acadSession.push(acadList[i].innerHTML);
				}
				console.log("acadSessionList" + acadSession);

				$
						.ajax(
								{
									url : "${pageContext.request.contextPath}/GetProgramsFromAcadSession?acadSession="
											+ acadSession
								}).done(function(data) {
							console.log("data:" + data);
							programObj = JSON.parse(data);
							console.log("Size:" + programObj.length);
							$("#programIdCampus option").remove();
							for (var i = 0; i < programObj.length; i++) {
								var a = JSON.stringify(programObj[i]);
								console.log("a is " + a);
								$("#programIdCampus").append($('<option>', {
									value : programObj[i].value,
									text : programObj[i].text
								}));

							}

						});

			}

			function myFunction(element, event) {

				var result = document.getElementById('JSResult');
				while (result.firstChild)
					result.removeChild(result.firstChild);
				var values = [];
				for (var i = 0; i < element.options.length; i++) {
					if (element.options[i].selected) {
						var li = document.createElement('li');
						li.appendChild(document
								.createTextNode(element.options[i].value));
						result.appendChild(li);
					}
				}

				var acadList = document.getElementById('JSResult')
						.getElementsByTagName("LI");

				var programObj = [];
				console.log(acadList.length);
				var acadSession = [];

				for (var i = 0; i < acadList.length; i++) {
					acadSession.push(acadList[i].innerHTML);
				}
				console.log("acadSessionList" + acadSession);

				$
						.ajax(
								{
									url : "${pageContext.request.contextPath}/GetProgramsFromAcadSession?acadSession="
											+ acadSession
								}).done(function(data) {
							console.log("data:" + data);
							programObj = JSON.parse(data);
							console.log("Size:" + programObj.length);
							$("#programId option").remove();
							for (var i = 0; i < programObj.length; i++) {
								var a = JSON.stringify(programObj[i]);
								console.log("a is " + a);
								$("#programId").append($('<option>', {
									value : programObj[i].value,
									text : programObj[i].text
								}));

							}

						});

			}

			function myFunctionProgramWise(element, event) {
				var result = document.getElementById('JSProgramResult');
				while (result.firstChild)
					result.removeChild(result.firstChild);
				var values = [];
				for (var i = 0; i < element.options.length; i++) {
					if (element.options[i].selected) {
						var li = document.createElement('li');
						li.appendChild(document
								.createTextNode(element.options[i].value));
						result.appendChild(li);
					}
				}

				var acadList = document.getElementById('JSProgramResult')
						.getElementsByTagName("LI");

				var programObj = [];
				console.log(acadList.length);
				var acadSession = [];

				for (var i = 0; i < acadList.length; i++) {
					acadSession.push(acadList[i].innerHTML);
				}
				console.log("acadSessionList" + acadSession);

				$
						.ajax(
								{
									url : "${pageContext.request.contextPath}/GetProgramsFromAcadSession?acadSession="
											+ acadSession
								})
						.done(
								function(data) {
									console.log("data:" + data);
									programObj = JSON.parse(data);
									console.log("Size:" + programObj.length);
									$("#programIdForProgramWise option")
											.remove();
									for (var i = 0; i < programObj.length; i++) {
										var a = JSON.stringify(programObj[i]);
										console.log("a is " + a);
										$("#programIdForProgramWise")
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

			function submitFormQuestionWiseReport() {

				var courseCountQuestion = $('#courseQuestCountForQuestionwise')
						.val();
				var acadSession = $('#acadSessionQuestionWise').val();
				var programId = $('#programId').val();
				var acadYear = $('#acadYearQWise').val();
				var feedbackType = $('#feedbackTypeQWise').val();

				if (courseCountQuestion != null && acadSession != null
						&& programId != null && acadYear != null && feedbackType != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackReportQuestionWise?'
							+ 'acadSession='
							+ acadSession
							+ '&courseQuestnCount='
							+ courseCountQuestion
							+ '&programId='
							+ programId
							+ '&acadYear='
							+ acadYear
							+ '&feedbackType='
							+ feedbackType;
				} else {
					alert("please fill all the fields");
				}

			}
			function submitFormQuestionWiseReportForFacultyFeedback() {
				//alert('function callled')

				var courseCountQuestion = $('#courseQuestCountForQuestionwise')
						.val();
				//alert('courseCountQuestion --- ', courseCountQuestion)
				//var acadSession = $('#acadSessionQuestionWise').val();
				//var programId = $('#programId').val();
				var acadYear = $('#acadYearQWise').val();

				//alert('courseCountQuestion -- ' + courseCountQuestion + 'acadYear - '
				//		+ acadYear)

				if (courseCountQuestion != null && acadYear != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackReportQuestionWiseForAllPrograms?'
							+ '&courseQuestnCount='
							+ courseCountQuestion
							+ '&acadYear=' + acadYear;
				} else {
					alert("please fill all the fields");
				}

			}

			function submitFormQuestionWiseReportForFaculty() {

				var courseCountQuestion = $('#courseQuestCountForQuestionwise')
						.val();
				var acadSession = $('#acadSessionQuestionWise').val();
				var programId = $('#programId').val();
				var acadYear = $('acadYearQWise').val();

				if (courseCountQuestion != null && acadSession != null
						&& programId != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackReportQuestionWise?'
							+ 'acadSession='
							+ acadSession
							+ '&courseQuestnCount='
							+ courseCountQuestion
							+ '&programId='
							+ programId
							+ '&acadYear='
							+ acadYear;
				} else {
					alert("please fill all the fields");
				}

			}

			function submitFormProgramWiseReport() {

				var courseCountQuestion = $('#courseQuestCountForProgramwise')
						.val();

				var programId = $('#programIdForProgramWise').val();
				var acadSession = $('#acadSessionProgramWise').val();
				var feedbackType = $('#feedbackTypeProgram').val();

				if (courseCountQuestion != null && acadSession != null
						&& programId != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadFeedbackReportProgramWise?'
							+ 'courseQuestnCount='
							+ courseCountQuestion
							+ '&acadSession='
							+ acadSession
							+ '&programId='
							+ programId
							+ '&feedbackType='
							+ feedbackType;

				} else {
					alert("please fill all the fields");
				}

			}

			function submitFormCampusWiseReport() {

				var courseCountQuestion = $(
						'#courseQuestCountForQuestionwiseAndCampus').val();
				var acadSession = $('#acadSessionQuestionWiseAndCampus').val();
				var programId = $('#programIdCampus').val();
				var acadYear = $('#acadYearForAcadSessionForCampus').val();
				var campusId = $('#campus').val();
				var feedbackType = $('#feedbackTypeQWiseC').val();

				console.log("entreies---->");
				console.log("count--->" + courseCountQuestion + "session--->"
						+ acadSession + "programId" + programId + "year"
						+ acadYear + "campus" + campusId);
				if (courseCountQuestion != null && acadSession != null
						&& programId != null && acadYear != null) {

					if (campusId != null || campusId != '') {
						console.log("campsId is not empty");
						window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackReportQWise?'
								+ 'acadSession='
								+ acadSession
								+ '&courseQuestnCount='
								+ courseCountQuestion
								+ '&programId='
								+ programId
								+ '&acadYear='
								+ acadYear + '&campusId=' + campusId
								+ '&feedbackType=' + feedbackType;
					} else {
						console.log("campsId is  empty");
						window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackReportQWise?'
								+ 'acadSession='
								+ acadSession
								+ '&courseQuestnCount='
								+ courseCountQuestion
								+ '&programId='
								+ programId
								+ '&acadYear='
								+ acadYear
								+ '&feedbackType=' + feedbackType;
					}

				} else {
					alert("please fill all the fields");
				}

			}

			function submitFeatureWiseUtilisationReport() {
				var fromDate = $('#startDate').val();
				var endDate = $('#endDate').val();
				var campus = $('#campus1').val();

				if (fromDate != null && endDate != null) {

					window.location.href = '${pageContext.request.contextPath}/featureWiseSummaryUtilityReport?'
							+ 'fromDate='
							+ fromDate
							+ '&toDate='
							+ endDate
							+ '&campus=' + campus;

				} else {
					alert("please fill all the fields");
				}
			}

			function submitFeatureWiseConsoliUtilisationReport() {
				var fromDate = $('#startDate11').val();
				var endDate = $('#endDate11').val();
				var campus = $('#campus11').val();

				if (fromDate != null && endDate != null) {

					window.location.href = '${pageContext.request.contextPath}/lmsUsageReportCombinedWithAllCampus?'
							+ 'fromDate='
							+ fromDate
							+ '&toDate='
							+ endDate
							+ '&campus=' + campus;

				} else {
					alert("please fill all the fields");
				}
			}

			function submitFeatureWiseDetailedUtilisationReport() {
				var fromDate = $('#startDate1').val();
				var endDate = $('#endDate1').val();

				if (fromDate != null && endDate != null) {

					window.location.href = '${pageContext.request.contextPath}/featureWiseDetailedUtilityReport?'
							+ 'fromDate=' + fromDate + '&toDate=' + endDate;

				} else {
					alert("please fill all the fields");
				}
			}

			function submitMarkAttendanceReport() {
				var ofDate = $('#ofDate').val();

				if (ofDate != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadAttendanceReport?'
							+ 'ofDate=' + ofDate;

				} else {
					alert("please fill all the fields");
				}
			}
			
			function submitAssignmentsReport() {
				var courseId = $('#idForCourse').val();
				var assignmentId = $('#idForAssignment').val();

				if (courseId != null && assignmentId != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadAssignmentReportQuestionWise?'
							+ 'courseId=' + courseId
							+ '&assignmentId=' + assignmentId;

				} else {
					alert("please fill all the fields");
				}
			}

			function submitFormByAcadMonthYearAndSessionFeedbackReport() {

				var courseCountQuestion = $('#courseQuestCountForFaculty')
						.val();

				var acadYear = $('#acadYear').val();
				var acadMonth = $('#acadMonth').val();
				var acadSessionValue = $('#acadSessionId').val();
				console.log('acadYear--->' + acadYear);
				console.log('acadMonth--->' + acadMonth);
				console.log('acadSessionId--->' + acadSessionValue);
				console.log('courseCountQuestion--->' + courseCountQuestion);
				console.log('acadSession value()' + $('#acadSessionId').val())

				if (courseCountQuestion != null && acadSessionValue != null
						&& acadYear != null && acadMonth != null) {

					window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackReportQuestionWise?'
							+ 'courseQuestnCount='
							+ courseCountQuestion
							+ '&acadSession='
							+ acadSessionValue

							+ '&acadYear='
							+ acadYear
							+ '&acadMonth='
							+ acadMonth;

				} else {
					alert("please fill all the field!!!!!!");
				}

			}

			function submitForFacultyLetter() {

				var username = $('#facultyId').val();

				var acadYear = $('#acadYearFeedbackLetter').val();
				var campusId = $('#campusIdFeedbackLetter').val();
				var acadSessionList = $('#acadSessionForTerm').val();
				var term = $('#term').val();
				var dean = $('#dean').val();

				console.log('username-->' + username + 'acadYear--->'
						+ acadYear + 'campusId--->' + campusId);

				if (acadYear != null && acadSessionList != null && term != null
						&& term != '' && dean != null && dean != '') {

					if (campusId != null) {

						if (username != null && username != '') {
							window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackLetterForAllPrograms?'
									+ 'acadYear='
									+ acadYear
									+ '&acadSessionList='
									+ acadSessionList
									+ '&term='
									+ term
									+ '&dean='
									+ dean
									+ '&campusId='
									+ campusId
									+ '&username='
									+ username;
						} else {
							window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackLetterForAllPrograms?'
									+ 'acadYear='
									+ acadYear
									+ '&acadSessionList='
									+ acadSessionList
									+ '&term='
									+ term
									+ '&dean='
									+ dean
									+ '&campusId=' + campusId;
						}
					} else {
						if (username != null && username != '') {
							window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackLetterForAllPrograms?'
									+ 'acadYear='
									+ acadYear
									+ '&acadSessionList='
									+ acadSessionList
									+ '&term='
									+ term
									+ '&dean='
									+ dean
									+ '&username=' + username;
						} else {
							window.location.href = '${pageContext.request.contextPath}/downloadFacultyFeedbackLetterForAllPrograms?'
									+ 'acadYear='
									+ acadYear
									+ '&acadSessionList='
									+ acadSessionList
									+ '&term=' + term + '&dean=' + dean;

						}
					}

				} else {
					alert("please note: acadYear,session,term & dean are the required fields");
				}

			}
			
			function myFunctionAssignmentWise(element, event) {
				var result = document.getElementById('JSAssignmentResult');
				while (result.firstChild)
					result.removeChild(result.firstChild);
				var values = [];
				for (var i = 0; i < element.options.length; i++) {
					if (element.options[i].selected) {
						var li = document.createElement('li');
						li.appendChild(document
								.createTextNode(element.options[i].value));
						result.appendChild(li);
					}
				}

				var courseList = document.getElementById('JSAssignmentResult')
						.getElementsByTagName("LI");

				var assignmentObj = [];
				console.log(courseList.length);
				var courses = [];

				for (var i = 0; i < courseList.length; i++) {
					courses.push(courseList[i].innerHTML);
				}
				console.log("coursesList" + courses);

				$
						.ajax(
								{
									url : "${pageContext.request.contextPath}/GetAssignmentFromCourse?courses="
											+ courses
								})
						.done(
								function(data) {
									console.log("data:" + data);
									assignmentObj = JSON.parse(data);
									console.log("Size:" + assignmentObj.length);
									$("#idForAssignment option")
											.remove();
									for (var i = 0; i < assignmentObj.length; i++) {
										var a = JSON.stringify(assignmentObj[i]);
										console.log("a is " + a);
										$("#idForAssignment")
												.append(
														$(
																'<option>',
																{
																	value : assignmentObj[i].value,
																	text : assignmentObj[i].text
																}));

									}

								});

			}
		</script>
		<script>
			$(window)
					.bind(
							"pageshow",
							function() {
								$("#startDate1").val('');
								$('#endDate1').val('');
								$('#ofDate').val('');

								$('#startDate1').daterangepicker({
									autoUpdateInput : false,
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

								$('#startDate1')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													$(this)
															.val(
																	picker.startDate
																			.format('YYYY-MM-DD HH:mm:ss'));
												});

								$('#startDate1').on('cancel.daterangepicker',
										function(ev, picker) {
											$(this).val('');
										});

								$('#endDate1').daterangepicker({
									autoUpdateInput : false,
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

								$('#endDate1')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													$(this)
															.val(
																	picker.startDate
																			.format('YYYY-MM-DD HH:mm:ss'));
												});

								$('#endDate1').on('cancel.daterangepicker',
										function(ev, picker) {
											$(this).val('');
										});

								$('#ofDate').daterangepicker({
									autoUpdateInput : false,
									locale : {
										cancelLabel : 'Clear'
									},
									"maxDate" : new Date(),
									"singleDatePicker" : true,
									"showDropdowns" : true,
									"timePicker" : true,
									"showCustomRangeLabel" : false,
									"alwaysShowCalendars" : true,
									"opens" : "center"
								});

								$('#ofDate')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													$(this)
															.val(
																	picker.startDate
																			.format('YYYY-MM-DD HH:mm:ss'));
												});

								$('#ofDate').on('cancel.daterangepicker',
										function(ev, picker) {
											$(this).val('');
										});

							});
		</script>



		<script>
			$(window)
					.bind(
							"pageshow",
							function() {
								$("#startDate").val('');
								$('#endDate').val('');

								$('#startDate').daterangepicker({
									autoUpdateInput : false,
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

								$('#startDate')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													$(this)
															.val(
																	picker.startDate
																			.format('YYYY-MM-DD HH:mm:ss'));
												});

								$('#startDate').on('cancel.daterangepicker',
										function(ev, picker) {
											$(this).val('');
										});

								$('#endDate').daterangepicker({
									autoUpdateInput : false,
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

								$('#endDate')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													$(this)
															.val(
																	picker.startDate
																			.format('YYYY-MM-DD HH:mm:ss'));
												});

								$('#endDate').on('cancel.daterangepicker',
										function(ev, picker) {
											$(this).val('');
										});

							});
		</script>


		<!-- <script>
	$("#datetimepicker1").on("dp.change", function(e) {

		validDateTimepicks();
	}).datetimepicker({
		// minDate:new Date(),
		useCurrent : false,
		format : 'YYYY-MM-DD HH:mm:ss'
	});

	$("#datetimepicker2").on("dp.change", function(e) {

		validDateTimepicks();
	}).datetimepicker({
		// minDate:new Date(),
		useCurrent : false,
		format : 'YYYY-MM-DD HH:mm:ss'
	});

	function validDateTimepicks() {
		if (($('#startDate').val() != '' && $('#startDate').val() != null)
				&& ($('#endDate').val() != '' && $('#endDate').val() != null)) {
			var fromDate = $('#startDate').val();
			var toDate = $('#endDate').val();
			var eDate = new Date(fromDate);
			var sDate = new Date(toDate);
			if (sDate < eDate) {
				alert("endate cannot be smaller than startDate");
				$('#startDate').val("");
				$('#endDate').val("");
			}
		}
	}

	$("#datetimepicker3").on("dp.change", function(e) {

		validDateTimepicksNew();
	}).datetimepicker({
		// minDate:new Date(),
		useCurrent : false,
		format : 'YYYY-MM-DD HH:mm:ss'
	});

	$("#datetimepicker4").on("dp.change", function(e) {

		validDateTimepicksNew();
	}).datetimepicker({
		// minDate:new Date(),
		useCurrent : false,
		format : 'YYYY-MM-DD HH:mm:ss'
	});

	function validDateTimepicksNew() {
		if (($('#startDate1').val() != '' && $('#startDate1').val() != null)
				&& ($('#endDate1').val() != '' && $('#endDate1').val() != null)) {
			var fromDate = $('#startDate1').val();
			var toDate = $('#endDate1').val();
			var eDate = new Date(fromDate);
			var sDate = new Date(toDate);
			if (sDate < eDate) {
				alert("endate cannot be smaller than startDate");
				$('#startDate1').val("");
				$('#endDate1').val("");
			}
		}
	}
</script> -->

		<!-- For Consolidated -->
		
		<script>
			$(window)
					.bind(
							"pageshow",
							function() {
								$("#startDate11").val('');
								$('#endDate11').val('');
								$('#ofDate').val('');

								$('#startDate11').daterangepicker({
									autoUpdateInput : false,
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

								$('#startDate11')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													$(this)
															.val(
																	picker.startDate
																			.format('YYYY-MM-DD HH:mm:ss'));
												});

								$('#startDate11').on('cancel.daterangepicker',
										function(ev, picker) {
											$(this).val('');
										});

								$('#endDate11').daterangepicker({
									autoUpdateInput : false,
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

								$('#endDate11')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													$(this)
															.val(
																	picker.startDate
																			.format('YYYY-MM-DD HH:mm:ss'));
												});

								$('#endDate11').on('cancel.daterangepicker',
										function(ev, picker) {
											$(this).val('');
										});

								$('#ofDate').daterangepicker({
									autoUpdateInput : false,
									locale : {
										cancelLabel : 'Clear'
									},
									"maxDate" : new Date(),
									"singleDatePicker" : true,
									"showDropdowns" : true,
									"timePicker" : true,
									"showCustomRangeLabel" : false,
									"alwaysShowCalendars" : true,
									"opens" : "center"
								});

								$('#ofDate')
										.on(
												'apply.daterangepicker',
												function(ev, picker) {
													$(this)
															.val(
																	picker.startDate
																			.format('YYYY-MM-DD HH:mm:ss'));
												});

								$('#ofDate').on('cancel.daterangepicker',
										function(ev, picker) {
											$(this).val('');
										});

							});
		</script>
		<script type="text/javascript">
			$(document).ready(function() {
	
				// Initialize select2
				$("#idForCourse").select2();
			});
		</script>


