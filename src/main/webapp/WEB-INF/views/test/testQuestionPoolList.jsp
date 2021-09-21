<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
		<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
		<jsp:include page="../common/rightSidebarFaculty.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_ADMIN')">

		<jsp:include page="../common/newAdminLeftNavBar.jsp" />
		<jsp:include page="../common/rightSidebarAdmin.jsp" />
	</sec:authorize>

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
			<jsp:include page="../common/newAdminTopHeader.jsp" />
		</sec:authorize>


		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

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
							<li class="breadcrumb-item active" aria-current="page">Add
								Subjective Test Question</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<c:if test="${testId ne null}">
						<div class="card bg-white border">
							<div class="card-body">
								<div class="col-md-12 col-xs-12 col-sm-12">
									<div class="x_panel">

										<div class="x_title">
											<h5>Test Pools</h5>

										</div>


										<div class="x_content">
											<form>
												<div class="row">
													<div class="col-md-4 col-sm-6 col-xs-12">
														<div class="form-group">

															<label for="courseId">Test Pools</label> <select
																id="testPoolId" name="testPoolId" class="form-control">
																<c:if test="${testPoolId eq  null }">
																	<option value="">Select Test Pool</option>
																</c:if>
																<c:forEach var="pool" items="${testPoolsList}"
																	varStatus="status">
																	<c:if test="${testPoolId eq pool.id }">
																		<option value="${pool.id}" selected>${pool.testPoolName}</option>
																	</c:if>
																	<c:if test="${testPoolId ne pool.id }">
																		<option value="${pool.id}">${pool.testPoolName}</option>
																	</c:if>
																</c:forEach>
															</select>
														</div>
													</div>

												</div>

											</form>
										</div>
									</div>
								</div>
							</div>
						</div>
					</c:if>

					<!-- Results Panel -->

					<c:if test="${testId eq null}">

						<div class="card bg-white border">
							<div class="card-body">
								<div class="panel-group" id="testQuestionAccordion"
									role="tablist" aria-multiselectable="true">
									<c:forEach items="${testPool.testQuestionPools}"
										var="testQuestionVar" varStatus="status">


										<div class="panel panel-default">
											<div class="panel-heading" role="tab"
												id="heading-${status.index}">
												<div class="p-3 border" data-toggle="collapse"
													data-parent="#testQuestionAccordion"
													data-target="#collapse-${status.index}"
													aria-expanded="false"
													aria-controls="collapse-${status.index}">
													<strong>Q${status.index+1}. <%-- ${testQuestionVar.description} --%>
													</strong>(Marks : ${testQuestionVar.marks})
													${testQuestionVar.testType} /${testQuestionVar.questionType }
												</div>
											</div>
											<div id="collapse-${status.index}"
												class="panel-collapse collapse border pl-3 pr-3"
												role="tabpanel" aria-labelledby="heading-${status.index}">
												<div class="panel-body">
													<div class="row">
														<form:form action="configureImageTestQuestionPool"
															method="post" modelAttribute="testPool"
															id="testQuestionPoolForm-${status.index}">


															<div class="col-sm-12">
																<form:input type="hidden"
																	path="testQuestionPools[${status.index}].id"
																	value="${testQuestionVar.id}" />
																<form:input type="hidden"
																	path="testQuestionPools[${status.index}].testType"
																	value="${testQuestionVar.testType}" />
																<form:input type="hidden"
																	path="testQuestionPools[${status.index}].questionType"
																	value="${testQuestionVar.questionType}" />
																<div class="form-group">
																	<form:label path="testQuestionPools[${status.index}]."
																		for="description">Description</form:label>
																	<%-- <form:input
																					path="testQuestionPools[${status.index}].description"
																					type="textarea" required="required"
																					placeholder="Description"
																					class="form-control ckeditorClass"
																					value="${testQuestionVar.description}" /> --%>

																	<form:textarea class="form-group ckeditorClass"
																		path="testQuestionPools[${status.index}].description"
																		name="description" id="editor${status.index}"
																		rows="10" cols="80" value="${testQuestionVar.marks}" />
																</div>
															</div>
															<div class="col-sm-6">
																<div class="form-group">
																	<form:label
																		path="testQuestionPools[${status.index}].marks"
																		for="marks">Marks <span
																			style="color: red">*</span>
																	</form:label>
																	<form:input id="marks"
																		path="testQuestionPools[${status.index}].marks"
																		type="number" required="required" placeholder="Marks"
																		class="form-control" value="${testQuestionVar.marks}" />
																</div>
															</div>
															<div class="col-sm-6">
																<div class="form-group">
																	<form:label
																		path="testQuestionPools[${status.index}].testType"
																		for="testType">Question Type :  ${testQuestionVar.testType} /${testQuestionVar.questionType }</form:label>
																	<!-- <select
																					id="testType" name="testType" class="form-control">

																					<option value="">Select Type</option>

																					<option value="Objective">Objective</option>
																					<option value="Subjective">Subjective</option>


																				</select> -->
																</div>
															</div>
															<c:if test="${testQuestionVar.testType eq 'Objective'}">


																<c:if
																	test="${testQuestionVar.questionType eq 'MCQ' || testQuestionVar.questionType eq 'Image'}">

																	<div class="col-sm-6">
																		<div class="form-group">
																			<label for="type">Type</label>
																			<div class="clearfix"></div>
																			<label class="radio-inline"> <form:radiobutton
																					path="testQuestionPools[${status.index}].type"
																					value="SINGLESELECT"
																					onclick="singleSelectClicked1('testQuestionPoolForm-${status.index}')" />
																				Single Select
																			</label> <label class="radio-inline"> </label>
																			<form:radiobutton
																				path="testQuestionPools[${status.index}].type"
																				value="MULTISELECT"
																				onclick="multipleSelectClicked1('testQuestionPoolForm-${status.index}')" />
																			Multiple Select
																		</div>
																	</div>


																	<div class="col-sm-6 ">
																		<div class="form-group">
																			<label for="optionShuffle">Option Shuffle
																				Required?</label>
																			<form:select id="optionShuffle" name="optionShuffle"
																				class="form-control"
																				path="testQuestionPools[${status.index}].optionShuffle">

																				<option value="">Select</option>
																				<c:if test="${testQuestionVar.optionShuffle eq 'Y'}">
																					<option value="Y" selected>Y</option>
																					<option value="N">N</option>
																				</c:if>

																				<c:if test="${testQuestionVar.optionShuffle eq 'N'}">
																					<option value="Y">Y</option>
																					<option value="N" selected>N</option>
																				</c:if>

																				<c:if
																					test="${testQuestionVar.optionShuffle eq null}">
																					<option value="Y" selected>Y</option>
																					<option value="N">N</option>
																				</c:if>
																			</form:select>
																		</div>
																	</div>
																	<div class="testOptions">
																		<div class="col-sm-12">
																			<div class="form-group">
																				<form:label
																					path="testQuestionPools[${status.index}].option1"
																					for="option1">Option 1</form:label>
																				<div class="input-group">
																					<span class="input-group-addon"> <c:choose>
																							<c:when
																								test="${testQuestionVar.type eq 'SINGLESELECT'}">
																								<form:radiobutton
																									path="testQuestionPools[${status.index}].correctOption"
																									value="1" />
																							</c:when>
																							<c:otherwise>
																								<form:checkbox
																									path="testQuestionPools[${status.index}].correctOptions"
																									value="1" />
																							</c:otherwise>
																						</c:choose>
																					</span>
																					<form:input id="option1"
																						path="testQuestionPools[${status.index}].option1"
																						type="textarea" placeholder="Option 1"
																						class="form-control"
																						value="${testQuestionVar.option1}" />
																				</div>
																			</div>
																		</div>
																		<div class="col-sm-12">
																			<div class="form-group">
																				<form:label
																					path="testQuestionPools[${status.index}].option2"
																					for="option2">Option 2</form:label>
																				<div class="input-group">
																					<span class="input-group-addon"> <c:choose>
																							<c:when
																								test="${testQuestionVar.type eq 'SINGLESELECT'}">
																								<form:radiobutton
																									path="testQuestionPools[${status.index}].correctOption"
																									value="2" />
																							</c:when>
																							<c:otherwise>
																								<form:checkbox
																									path="testQuestionPools[${status.index}].correctOptions"
																									value="2" />
																							</c:otherwise>
																						</c:choose>
																					</span>
																					<form:input id="option2"
																						path="testQuestionPools[${status.index}].option2"
																						type="textarea" placeholder="Option 2"
																						class="form-control"
																						value="${testQuestionVar.option2}" />
																				</div>
																			</div>
																		</div>
																		<div class="col-sm-12">
																			<div class="form-group">
																				<form:label
																					path="testQuestionPools[${status.index}].option3"
																					for="option3">Option 3</form:label>
																				<div class="input-group">
																					<span class="input-group-addon"> <c:choose>
																							<c:when
																								test="${testQuestionVar.type eq 'SINGLESELECT'}">
																								<form:radiobutton
																									path="testQuestionPools[${status.index}].correctOption"
																									value="3" />
																							</c:when>
																							<c:otherwise>
																								<form:checkbox
																									path="testQuestionPools[${status.index}].correctOptions"
																									value="3" />
																							</c:otherwise>
																						</c:choose>
																					</span>
																					<form:input id="option3"
																						path="testQuestionPools[${status.index}].option3"
																						type="textarea" placeholder="Option 3"
																						class="form-control"
																						value="${testQuestionVar.option3}" />
																				</div>
																			</div>
																		</div>
																		<div class="col-sm-12">
																			<div class="form-group">
																				<form:label
																					path="testQuestionPools[${status.index}].option4"
																					for="option4">Option 4</form:label>
																				<div class="input-group">
																					<span class="input-group-addon"> <c:choose>
																							<c:when
																								test="${testQuestionVar.type eq 'SINGLESELECT'}">
																								<form:radiobutton
																									path="testQuestionPools[${status.index}].correctOption"
																									value="4" />
																							</c:when>
																							<c:otherwise>
																								<form:checkbox
																									path="testQuestionPools[${status.index}].correctOptions"
																									value="4" />
																							</c:otherwise>
																						</c:choose>
																					</span>
																					<form:input id="option4"
																						path="testQuestionPools[${status.index}].option4"
																						type="textarea" placeholder="Option 4"
																						class="form-control"
																						value="${testQuestionVar.option4}" />
																				</div>
																			</div>
																		</div>
																		<div class="col-sm-12">
																			<div class="form-group">
																				<form:label
																					path="testQuestionPools[${status.index}].option5"
																					for="option5">Option 5</form:label>
																				<div class="input-group">
																					<span class="input-group-addon"> <c:choose>
																							<c:when
																								test="${testQuestionVar.type eq 'SINGLESELECT'}">
																								<form:radiobutton
																									path="testQuestionPools[${status.index}].correctOption"
																									value="5" />
																							</c:when>
																							<c:otherwise>
																								<form:checkbox
																									path="testQuestionPools[${status.index}].correctOptions"
																									value="5" />
																							</c:otherwise>
																						</c:choose>
																					</span>
																					<form:input id="option5"
																						path="testQuestionPools[${status.index}].option5"
																						type="textarea" placeholder="Option 5"
																						class="form-control"
																						value="${testQuestionVar.option5}" />
																				</div>
																			</div>
																		</div>
																		<div class="col-sm-12">
																			<div class="form-group">
																				<form:label
																					path="testQuestionPools[${status.index}].option6"
																					for="option6">Option 6</form:label>
																				<div class="input-group">
																					<span class="input-group-addon"> <c:choose>
																							<c:when
																								test="${testQuestionVar.type eq 'SINGLESELECT'}">
																								<form:radiobutton
																									path="testQuestionPools[${status.index}].correctOption"
																									value="6" />
																							</c:when>
																							<c:otherwise>
																								<form:checkbox
																									path="testQuestionPools[${status.index}].correctOptions"
																									value="6" />
																							</c:otherwise>
																						</c:choose>
																					</span>
																					<form:input id="option6"
																						path="testQuestionPools[${status.index}].option6"
																						type="textarea" placeholder="Option 6"
																						class="form-control"
																						value="${testQuestionVar.option6}" />
																				</div>
																			</div>
																		</div>
																		<div class="col-sm-12">
																			<div class="form-group">
																				<form:label
																					path="testQuestionPools[${status.index}].option7"
																					for="option7">Option 7</form:label>
																				<div class="input-group">
																					<span class="input-group-addon"> <c:choose>
																							<c:when
																								test="${testQuestionVar.type eq 'SINGLESELECT'}">
																								<form:radiobutton
																									path="testQuestionPools[${status.index}].correctOption"
																									value="7" />
																							</c:when>
																							<c:otherwise>
																								<form:checkbox
																									path="testQuestionPools[${status.index}].correctOptions"
																									value="7" />
																							</c:otherwise>
																						</c:choose>
																					</span>
																					<form:input id="option7"
																						path="testQuestionPools[${status.index}].option7"
																						type="textarea" placeholder="Option 7"
																						class="form-control"
																						value="${testQuestionVar.option7}" />
																				</div>
																			</div>
																		</div>
																		<div class="col-sm-12">
																			<div class="form-group">
																				<form:label
																					path="testQuestionPools[${status.index}].option8"
																					for="option8">Option 8</form:label>
																				<div class="input-group">
																					<span class="input-group-addon"> <c:choose>
																							<c:when
																								test="${testQuestionVar.type eq 'SINGLESELECT'}">
																								<form:radiobutton
																									path="testQuestionPools[${status.index}].correctOption"
																									value="8" />
																							</c:when>
																							<c:otherwise>
																								<form:checkbox
																									path="testQuestionPools[${status.index}].correctOptions"
																									value="8" />
																							</c:otherwise>
																						</c:choose>
																					</span>
																					<form:input id="option8"
																						path="testQuestionPools[${status.index}].option8"
																						type="textarea" placeholder="Option 8"
																						class="form-control"
																						value="${testQuestionVar.option8}" />
																				</div>
																			</div>
																		</div>
																	</div>
																</c:if>
																<c:if
																	test="${testQuestionVar.questionType eq 'Numeric'}">
																	<div class="col-sm-6">
																		<div class="form-group">
																			<form:label
																				path="testQuestionPools[${status.index}].answerRangeFrom"
																				for="answerRangeFrom">Answer-Range From</form:label>
																			<form:input id="answerRangeFrom"
																				path="testQuestionPools[${status.index}].answerRangeFrom"
																				type="text" placeholder="Answer-Range From"
																				class="form-control"
																				value="${testQuestionVar.answerRangeFrom}" />
																		</div>
																	</div>

																	<div class="col-sm-6">
																		<div class="form-group">
																			<form:label
																				path="testQuestionPools[${status.index}].answerRangeTo"
																				for="answerRangeTo">Answer-Range To</form:label>
																			<form:input id="answerRangeTo"
																				path="testQuestionPools[${status.index}].answerRangeTo"
																				type="text" placeholder="Answer-Range To"
																				class="form-control"
																				value="${testQuestionVar.answerRangeTo}" />
																		</div>
																	</div>

																	<div class="col-sm-6">
																		<div class="form-group">
																			<form:label
																				path="testQuestionPools[${status.index}].correctOption"
																				for="correctOption">Correct Option</form:label>
																			<form:input id="correctOption"
																				path="testQuestionPools[${status.index}].correctOption"
																				type="text" placeholder="Correct-Answer"
																				class="form-control"
																				value="${testQuestionVar.correctOption}" />
																		</div>
																	</div>

																</c:if>
															</c:if>


															<div class="col-sm-12 column">
																<div class="form-group">

																	<c:url value="/deleteTestQuestionPool"
																		var="deleteTestQuestionPool">
																		<c:param name="testQuestionPoolId"
																			value="${testQuestionVar.id}" />
																		<c:param name="testPoolId"
																			value="${testQuestionVar.testPoolId}" />
																		<c:param name="mapping"
																			value="viewTestQuestionsByTestPool" />
																	</c:url>

																	<button id="submit" class="btn btn-large btn-success"
																		formaction="updateTestQuestionPool">Update
																		Question</button>
																	<button id="delete" class="btn btn-large btn-danger"
																		formaction="${deleteTestQuestionPool}">Delete
																		Question</button>

																</div>
															</div>
														</form:form>
													</div>
												</div>
											</div>
										</div>
									</c:forEach>
								</div>

							</div>
						</div>

					</c:if>

					<c:if test="${showQuestionByPool && testId ne null}">
						<div class="card bg-white border">
							<div class="card-body">
								<div class="col-xs-12 col-sm-12">
									<div class="x_title">
										<h5 class="text-center border-bottom pb-2">Test Questions
											In A Pool</h5>
										<c:if test="${showProceed}">
											<c:if test="${disabled ne true}">
												<div class="col-12">
													<div class="form-group">
														<sec:authorize access="hasRole('ROLE_FACULTY')">
														<a href="viewTestDetails?testId=${testId}"><i
															class="btn btn-large btn-dark">Proceed to allocate
																students</i></a>
														</sec:authorize>
														<sec:authorize access="hasRole('ROLE_ADMIN')">
														<a href="viewTestDetailsByAdmin?testId=${testId}"><i
															class="btn btn-large btn-dark">Proceed to allocate
																students</i></a>
														</sec:authorize>
													</div>
												</div>
											</c:if>
										</c:if>
										<c:if test="${showStudents}">
											<c:if test="${disabled ne true}">
												<div class="col-12">
													<div class="form-group">
														<sec:authorize access="hasRole('ROLE_FACULTY')">
														<a href="viewTestDetails?testId=${testId}"><i
															class="btn btn-large btn-dark">Proceed to view allocated 
																students</i></a>
														</sec:authorize>
														<sec:authorize access="hasRole('ROLE_ADMIN')">
														<a href="viewTestDetailsByAdmin?testId=${testId}"><i
															class="btn btn-large btn-dark">Proceed to view allocated
																students</i></a>
														</sec:authorize>
													</div>
												</div>
											</c:if>
										</c:if>

									</div>

									<div class="col-12">
										<c:if test="${not empty testPool.testQuestionPools}">
											<form:form action="saveTestQuestionsByTestPool"
												id="saveTestQuestions" method="post"
												modelAttribute="testPool">
												<form:input path="testId" type="hidden" id="testId1" />
												<form:input path="id" type="hidden" />

												<div class="panel-group" id="testQuestionAccordion"
													role="tablist" aria-multiselectable="true">
													<input name="select_all" value="1" id="example-select-all-questions" type="checkbox" /> Select All
													<c:forEach items="${testPool.testQuestionPools}"
														var="testQuestionVar" varStatus="status">

														<div class="panel panel-default">

															<div class="panel-heading" role="tab"
																id="heading-${status.index}">

																<div class="row">
																	<div class="col-lg-1 col-md-1 col-sm-2 col-3"
																		style="line-height: 58px;">
																		<c:if test="${testId ne null}">
																			<c:if test="${empty testQuestionVar.testQuestionId }">
																				<form:checkbox path="testQuestionsPoolIds"
																					value="${testQuestionVar.id}"
																					class="testQuestionPoolIds"
																					id='checkBoxAllocate${status.index}' />
																			</c:if>
																		</c:if>
																		<c:if
																			test="${not empty testQuestionVar.testQuestionId }">
																			<i class="fa fa-check-circle"
																				style="font-size: 25px; color: green;"></i>
																		</c:if>
																	</div>
																	<%-- <a data-toggle="collapse"
																	data-parent="#testQuestionAccordion"
																	href="#collapse-${status.index}" aria-expanded="false"
																	aria-controls="collapse-${status.index}"><strong>Q${status.index+1}.
																		${testQuestionVar.description} </strong></a> (Marks :
																${testQuestionVar.marks}) ${testQuestionVar.testType}
																/${testQuestionVar.questionType } --%>
																	<div
																		class="p-3 border cursor-pointer col-lg-11 col-md-11 col-sm-10 col-9"
																		data-toggle="collapse"
																		data-parent="#testQuestionAccordion"
																		data-target="#collapse-${status.index}"
																		aria-expanded="false"
																		aria-controls="collapse-${status.index}">
																		Q${status.index+1}.
																		<%-- ${testQuestionVar.description} --%>
																		(Marks : ${testQuestionVar.marks})
																		${testQuestionVar.testType}
																		/${testQuestionVar.questionType }
																	</div>

																</div>
															</div>



															<div id="collapse-${status.index}"
																class="panel-collapse collapse border pl-3 pr-3"
																role="tabpanel"
																aria-labelledby="heading-${status.index}">
																<div class="panel-body">
																	<div class="row">

																		<div class="col-sm-12">
																			<form:input type="hidden"
																				path="testQuestionPools[${status.index}].id"
																				value="${testQuestionVar.id}" />
																			<form:input type="hidden"
																				path="testQuestionPools[${status.index}].questionType"
																				value="${testQuestionVar.questionType}" />
																			<form:input type="hidden"
																				path="testQuestionPools[${status.index}].optionShuffle"
																				value="${testQuestionVar.optionShuffle}" />
																			<div class="form-group">
																				<form:label
																					path="testQuestionPools[${status.index}]."
																					for="description"></form:label>
																				<%-- <form:input
																					path="testQuestionPools[${status.index}].description"
																					type="textarea" required="required"
																					placeholder="Description"
																					class="form-control ckeditorClass"
																					value="${testQuestionVar.description}" /> --%>

																				<form:textarea class="form-group ckeditorClass"
																					path="testQuestionPools[${status.index}].description"
																					name="description" id="editor${status.index}"
																					rows="10" cols="80" />
																			</div>
																		</div>
																		<div class="col-sm-4">
																			<div class="form-group">
																				<form:label
																					path="testQuestionPools[${status.index}].marks"
																					for="marks">Marks : ${testQuestionVar.marks}</form:label>

																			</div>
																		</div>
																		<div class="col-sm-4">
																			<div class="form-group">
																				<label for="questionType">Test Type :
																					${testQuestionVar.testType}</label>
																			</div>
																		</div>
																		<div class="col-sm-4">
																			<div class="form-group">
																				<label for="questionType">Test-Question Type
																					: ${testQuestionVar.questionType}</label>
																			</div>
																		</div>

																		<c:if
																			test="${testQuestionVar.questionType ne 'Numeric'}">
																			<div class="col-sm-4">
																				<div class="form-group">
																					<form:label
																						path="testQuestionPools[${status.index}].optionShuffle"
																						for="option1">Option Shuffle Required? : ${testQuestionVar.optionShuffle}</form:label>
																				</div>
																			</div>
																		</c:if>
																		<div class="col-sm-6 ">
																			<div class="form-group">
																				<label for="type">Question Type :
																					${testQuestionVar.type}</label>

																			</div>
																		</div>

																		<c:if
																			test="${testQuestionVar.testType eq 'Objective'}">


																			<div class="testOptions ">
																				<div class="col-sm-12">
																					<div class="form-group">
																						<form:label
																							path="testQuestionPools[${status.index}].option1"
																							for="option1">Option 1 : ${testQuestionVar.option1}</form:label>

																					</div>
																				</div>
																				<div class="col-sm-12">
																					<div class="form-group">
																						<form:label
																							path="testQuestionPools[${status.index}].option2"
																							for="option2">Option 2 : ${testQuestionVar.option2}</form:label>

																					</div>
																				</div>
																				<div class="col-sm-12">
																					<div class="form-group">
																						<form:label
																							path="testQuestionPools[${status.index}].option3"
																							for="option3">Option 3 : ${testQuestionVar.option3}</form:label>

																					</div>
																				</div>
																				<div class="col-sm-12">
																					<div class="form-group">
																						<form:label
																							path="testQuestionPools[${status.index}].option4"
																							for="option4">Option 4 : ${testQuestionVar.option4}</form:label>

																					</div>
																				</div>
																				<div class="col-sm-12">
																					<div class="form-group">
																						<form:label
																							path="testQuestionPools[${status.index}].option5"
																							for="option5">Option 5 : ${testQuestionVar.option5}</form:label>

																					</div>
																				</div>
																				<div class="col-sm-12">
																					<div class="form-group">
																						<form:label
																							path="testQuestionPools[${status.index}].option6"
																							for="option6">Option 6 : ${testQuestionVar.option6}</form:label>

																					</div>
																				</div>
																				<div class="col-sm-12">
																					<div class="form-group">
																						<form:label
																							path="testQuestionPools[${status.index}].option7"
																							for="option7">Option 7 : ${testQuestionVar.option7}</form:label>

																					</div>
																				</div>
																				<div class="col-sm-12">
																					<div class="form-group">
																						<form:label
																							path="testQuestionPools[${status.index}].option8"
																							for="option8">Option 8 : ${testQuestionVar.option8}</form:label>

																					</div>
																				</div>

																				<div class="col-sm-12">
																					<div class="form-group">
																						<form:label
																							path="testQuestionPools[${status.index}].correctOption"
																							for="option8">Correct Option : ${testQuestionVar.correctOption}</form:label>

																					</div>
																				</div>

																				<c:if
																					test="${testQuestionVar.questionType eq 'Numeric'}">
																					<div class="col-sm-12">
																						<div class="form-group">
																							<form:label
																								path="testQuestionPools[${status.index}].answerRangeFrom"
																								for="answerRangeFrom">Answer-Range From : ${testQuestionVar.answerRangeFrom}</form:label>

																						</div>
																					</div>
																					<div class="col-sm-12">
																						<div class="form-group">
																							<form:label
																								path="testQuestionPools[${status.index}].answerRangeTo"
																								for="answerRangeTo">Correct Option : ${testQuestionVar.answerRangeTo}</form:label>

																						</div>
																					</div>
																				</c:if>
																			</div>
																		</c:if>


																	</div>
																</div>
															</div>
														</div>
													</c:forEach>
													<c:if test="${testId ne null}">
														<div class="text-center mt-5">
															<div class="form-group">
																<sec:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_ADMIN')">
																	<button class="btn btn-large btn-success"
																		onclick="return clicked();" id="frmTest"
																		formaction="saveTestQuestionsByTestPool">Allocate
																		Questions</button>

																	<c:if test="${randomQuestion eq 'Y'}">
																		<button class="btn btn-large btn-success"
																			onclick="return clicked();"
																			formaction="saveAllTestQuestionsByTestPool">Allocate
																			All Questions</button>
																	</c:if>
																	<button id="cancel" class="btn btn-danger"
																		formaction="homepage" formnovalidate="formnovalidate">Cancel</button>

																</sec:authorize>
															</div>
														</div>
													</c:if>
												</div>
											</form:form>
										</c:if>



									</div>
								</div>
							</div>
						</div>
					</c:if>
					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<sec:authorize access="hasRole('ROLE_FACULTY')">
					<jsp:include page="../common/newSidebar.jsp" />
				</sec:authorize>
				<!-- SIDEBAR END -->
				<%-- <jsp:include page="../common/footer.jsp" /> --%>
				
				<footer class="text-white m-0">
	<div class="container-fluid">
		<div class="container pt-3 footer-nav">
			<div class="row">
				<div class="col-lg-4 col-md-4 col-sm-12">
					<h6>${userBean.firstname} ${userBean.lastname}</h6>
					<hr />
					<ul class="list-unstyled">
						<li>My Account</li>
						<li>Personal Details</li>
						<li>Dashboard</li>
						<li>Logout</li>
					</ul>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-12">
					<h6>Quick Links</h6>
					<hr />
					<ul class="list-unstyled">
						<li>My Tasks</li>
						<li>Forum</li>
						<li>Announcements</li>
						<li>Teachers</li>
					</ul>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-12">
					<h6>Some links</h6>
					<hr />
					<ul class="list-unstyled">
						<li>Terms &amp; Conditions</li>
						<li>Privacy Policy</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="container-fluid footer-nav2 pb-3">
		<hr />
		<div class="container text-center">© Copyright 2019 | NMIMS</div>
	</div>

</footer>

<script src="<c:url value="/resources/js/jquery.min.js" />"></script>
<!-- popper -->
<script src="<c:url value="/resources/js/popper.min.js" />"
	type="text/javascript"></script>

<script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.richtext.min.js" />"></script>
<script src="<c:url value="/resources/js/vendor/bootstrap-editable.js" />"></script>
<script src="<c:url value="/resources/js/moment.min.js" />"></script> 
 <script src="<c:url value="/resources/js/daterangepicker.min.js" />"></script> 
<script src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>
<script
	src="<c:url value="/resources/js/dataTables.bootstrap4.min.js" />"></script>

 <!-- Timer circles Style -->
  <script type="text/javascript" src="<c:url value="/resources/js/timecircles.js" />"></script>
  <script type="text/javascript" src="<c:url value="/resources/js/custom-timecircles.js" />" ></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/Chart.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/timecircles.js" />"></script>
<script>
	var myContextPath = "${pageContext.request.contextPath}"
</script>

<script type="text/javascript"
	src="<c:url value="/resources/js/style.js" />"></script>
	
	
	<!-- <script type="text/javascript"
	src="//cdn.ckeditor.com/4.8.0/standard-all/ckeditor.js"></script>
<script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script>

<script type="text/javascript">
	CKEDITOR
			.replace(
					'editor1',
					{
						extraPlugins : 'mathjax',
						mathJaxLib : 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML'
					});
</script> -->

<!--  DashBoard Js -->

<script>
	$(function() {

		$('#selectSem')
				.on(
						'change',
						function() {

							var selected = $('#selectSem').val();
							console.log(selected);

							if (dashboardPie) {
								dashboardPie.destroy();
							}
							if (dashboardBar) {
								dashboardBar.destroy();
							}

							var dataArr = [];
							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/getTestStatsBySem?acadSess='
												+ selected,
										success : function(data) {

											var parsedObj = JSON.parse(data);
											if (parsedObj
													.hasOwnProperty("passed")) {
												dataArr
														.push(Number(parsedObj.passed));
												dataArr
														.push(Number(parsedObj.pending));
												dataArr
														.push(Number(parsedObj.failed));
												console.log(dataArr);

												dashboardPie = new Chart(
														document
																.getElementById("testPieChart"),
														{
															type : 'pie',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Failed" ],
																datasets : [ {
																	label : "Test",
																	backgroundColor : [
																			"#39b54a",
																			"#feb42f",
																			"#d53439" ],
																	data : dataArr
																} ]
															},
															options : {
																legend : {
																	display : false
																},
																title : {
																	display : true,
																	text : 'Test Pie Chart'
																}
															}
														});
											} else {
												dashboardPie = new Chart(
														document
																.getElementById("testPieChart"),
														{
															type : 'pie',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Failed",
																		"No Data" ],
																datasets : [ {
																	label : "Test",
																	backgroundColor : [
																			"#39b54a",
																			"#feb42f",
																			"#d53439",
																			"#ddd" ],
																	data : [ 0,
																			0,
																			0,
																			1 ]
																} ]
															},
															options : {
																legend : {
																	display : false
																},
																title : {
																	display : true,
																	text : 'Test Pie Chart'
																}
															}
														});
											}
										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}
									});

							var dataArr1 = [];
							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/getAssignmentStatsBySem?acadSess='
												+ selected,
										success : function(data) {

											var parsedObj = JSON.parse(data);

											if (parsedObj
													.hasOwnProperty("completed")) {
												dataArr1
														.push(Number(parsedObj.completed));
												dataArr1
														.push(Number(parsedObj.pending));
												dataArr1
														.push(Number(parsedObj.lateSubmitted));
												dataArr1
														.push(Number(parsedObj.rejected));
												console.log(dataArr1);

												dashboardBar = new Chart(
														document
																.getElementById("assignBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Late Submitted",
																		"Rejected" ],
																datasets : [ {
																	label : "Assignments",
																	backgroundColor : [
																			"#39b54a",
																			"#feb42f",
																			"#8E5EA2",
																			"#d53439" ],
																	data : dataArr1
																} ]
															},
															options : {
																legend : {
																	display : false
																},
																title : {
																	display : true,
																	text : 'Assignments Bar Chart'
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 20
																		}
																	} ]
																}
															}
														});
											} else {
												dashboardBar = new Chart(
														document
																.getElementById("assignBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Late Submitted",
																		"Rejected" ],
																datasets : [ {
																	label : "Assignments",
																	backgroundColor : [
																			"#39b54a",
																			"#feb42f",
																			"#8E5EA2",
																			"#d53439" ],
																	data : [ 0,
																			0,
																			0,
																			0 ]
																} ]
															},
															options : {
																legend : {
																	display : false
																},
																title : {
																	display : true,
																	text : 'Assignments Bar Chart  (No Data)'
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 1
																		}
																	} ]
																}
															}
														});
											}
										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}
									});

						});

	});
</script>
<!--  END DashBoard Js -->

<!--  myCourse Js -->
<script>
	$(function() {

		$('#semSelect')
				.on(
						'change',
						function() {

							var selected = $('#semSelect').val();
							console.log(selected);

							<c:forEach var='sem' items='${ sessionWiseCourseList }'>
							if (selected == '<c:out value="${sem.key}"/>') {

								document.getElementById("courseListSemWise").innerHTML = ""
								<c:forEach var='group' items='${ sessionWiseCourseList[sem.key] }'>
										+ '<div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5"> '
										+ '<div class="courseAsset d-flex align-items-start flex-column"> '
										+ '<h6 class="text-uppercase mb-auto">${ group.courseName }</h6>'
										+ '<span class="courseNav"> <a href="${pageContext.request.contextPath}/viewAssignmentFinal?courseId=${group.id}">'
										+ '		<p class="caBg">View Assignment</p>'
										+ '</a> <a href="#">'
										+ '		<p class="ctBg">View Test</p>'
										+ '</a>' + '</span>' + '</div>'
										+ '</div>'
								</c:forEach>;
							}
							</c:forEach>

						});

	});
</script>
<!-- END myCourse Js -->

<!--  viewAssignmentFinal Js -->
<script>
	$(function() {

		$('#assignSem')
				.on(
						'change',
						function() {

							var selected = $('#assignSem').val();
							console.log(selected);

							<c:forEach var='sem' items='${ sessionWiseCourseList }'>
							if (selected == '<c:out value="${sem.key}"/>') {

								var optionsAsString = "";

								$('#assignCourse').find('option').remove();

								optionsAsString += "<option value='' disabled selected>--SELECT COURSE--</option>";
								<c:forEach var='group' items='${ sessionWiseCourseList[sem.key] }'>
								optionsAsString += "<option value='${ group.id }'>${ group.courseName }</option>";
								</c:forEach>

								console
										.log("optionsAsString"
												+ optionsAsString);

								$('#assignCourse').append(optionsAsString);
							}
							</c:forEach>

							if (AssignmentBar) {
								AssignmentBar.destroy();
							}

							AssignmentBar = new Chart(
									document
											.getElementById("assignmentBarChart"),
									{
										type : 'bar',
										data : {
											labels : [ "Completed", "Pending",
													"Late Submitted",
													"Rejected" ],
											datasets : [ {
												label : "Total",
												backgroundColor : [ "#2ea745",
														"#d69400", "#8e5ea2",
														"#d53439" ],
												data : [ 0, 0, 0, 0 ]
											} ]
										},
										options : {
											responsive : true,
											maintainAspectRatio : false,
											legend : {
												display : false
											},
											scales : {
												yAxes : [ {
													ticks : {
														min : 0,
														stepSize : 1
													}
												} ],
												xAxes : [ {
													ticks : {
														fontSize : 14
													}
												} ]
											},

											title : {
												display : true,
												text : 'Overall Assignment Data (No Data)'
											}
										}
									});

						});

		$('#assignCourse')
				.on(
						'change',
						function() {

							var acadSession = $('#assignSem').val();
							var courseId = $('#assignCourse').val();

							if (AssignmentBar) {
								AssignmentBar.destroy();
							}

							$.ajax({
								type : 'POST',
								url : myContextPath
										+ '/viewAssignmentFinalAjax?courseId='
										+ courseId,
								success : function(data) {

									var parsedObj = JSON.parse(data);
									console.log(parsedObj);

								},
								error : function(result) {
									var parsedObj = JSON.parse(result);
									console.log('error' + parsedObj);
								}
							});

							var dataArr1 = [];
							$
									.ajax({
										type : 'POST',
										url : myContextPath
												+ '/getAssignmentStatsBySem?acadSess='
												+ acadSession + '&courseId='
												+ courseId,
										success : function(data) {

											var parsedObj = JSON.parse(data);

											if (parsedObj
													.hasOwnProperty("completed")) {
												dataArr1
														.push(Number(parsedObj.completed));
												dataArr1
														.push(Number(parsedObj.pending));
												dataArr1
														.push(Number(parsedObj.lateSubmitted));
												dataArr1
														.push(Number(parsedObj.rejected));
												console.log(dataArr1);

												if (AssignmentBar) {
													AssignmentBar.destroy();
												}
												AssignmentBar = new Chart(
														document
																.getElementById("assignmentBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Late Submitted",
																		"Rejected" ],
																datasets : [ {
																	label : "Total",
																	backgroundColor : [
																			"#2ea745",
																			"#d69400",
																			"#8e5ea2",
																			"#d53439" ],
																	data : dataArr1
																} ]
															},
															options : {
																responsive : true,
																maintainAspectRatio : false,
																legend : {
																	display : false
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 1
																		}
																	} ],
																	xAxes : [ {
																		ticks : {
																			fontSize : 14
																		}
																	} ]
																},

																title : {
																	display : true,
																	text : 'Overall Assignment Data'
																}
															}
														});
											} else {
												AssignmentBar = new Chart(
														document
																.getElementById("assignmentBarChart"),
														{
															type : 'bar',
															data : {
																labels : [
																		"Completed",
																		"Pending",
																		"Late Submitted",
																		"Rejected" ],
																datasets : [ {
																	label : "Total",
																	backgroundColor : [
																			"#2ea745",
																			"#d69400",
																			"#8e5ea2",
																			"#d53439" ],
																	data : [ 0,
																			0,
																			0,
																			0 ]
																} ]
															},
															options : {
																responsive : true,
																maintainAspectRatio : false,
																legend : {
																	display : false
																},
																scales : {
																	yAxes : [ {
																		ticks : {
																			min : 0,
																			stepSize : 1
																		}
																	} ],
																	xAxes : [ {
																		ticks : {
																			fontSize : 14
																		}
																	} ]
																},

																title : {
																	display : true,
																	text : 'Overall Assignment Data (No Data)'
																}
															}
														});
											}
										},
										error : function(result) {
											var parsedObj = JSON.parse(result);
											console.log('error' + parsedObj);
										}
									});

						});

		$('#viewAssignmentTable').DataTable();

	});
</script>
<!-- DataTable JS -->
<script>
	var table = $('.table')
			.DataTable(
					{
						"dom" : '<"top"i>rt<"bottom"flp><"clear">',
						"lengthMenu" : [ [ 10, 25, 50, -1 ],
								[ 10, 25, 50, "All" ] ],
						/*
						 * buttons: [ 'selectAll', 'selectNone' ],
						 * language: { buttons: { selectAll: "Select
						 * all items", selectNone: "Select none" } },
						 */
						initComplete : function() {
							this
									.api()
									.columns()
									.every(
											function() {
												var column = this;
												var headerText = $(
														column.header()).text();
												if (headerText == "Sr. No."
														|| headerText == "Select To Allocate")
													return;
												var select = $(
														'<select class="form-control"><option value="">All</option></select>')
														.appendTo(
																$(
																		column
																				.footer())
																		.empty())
														.on(
																'change',
																function() {
																	var val = $.fn.dataTable.util
																			.escapeRegex($(
																					this)
																					.val());

																	column
																			.search(
																					val ? '^'
																							+ val
																							+ '$'
																							: '',
																					true,
																					false)
																			.draw();
																});

												column
														.data()
														.unique()
														.sort()
														.each(
																function(d, j) {
																	select
																			.append('<option value="'
																		+ d
																		+ '">'
																					+ d
																					+ '</option>')
																});
											});
						}
					});

	$('#example-select-all').on('click', function() {
		// Check/uncheck all checkboxes in the table
		var rows = table.rows({
			'search' : 'applied'
		}).nodes();
		$('input[type="checkbox"]', rows).prop('checked', this.checked);
	});
</script>
<!-- END viewAssignmentFinal Js -->

<script>
$(document).ready(function(){
	var randQReq = $('#randQReq').val();
	
	if (randQReq == 'Y') {
		$('#randQReq').prop('checked', true);
	} else {
		$('#randQReq').prop('checked', false);
	}
	
});

</script>


<script>
	/*SET RAND QUESTION AND PASSWORD FOR TEST*/

	$('#randQReq').click(function() {
		var randQReq = $('#randQReq').val();
		$('#inputMaxQ').parent().toggleClass('d-none');
		$('#sameMarks').toggleClass('d-none');
		//IF Y AND N

		if (randQReq == 'Y') {
			$('#randQReq').val('N');
		} else {
			$('#randQReq').val('Y');
		}

	});
	


	$('#setTestPwd').click(function() {
		var setTestPwd = $('#setTestPwd').val();
		$('#testPwdVal').parent().toggleClass('d-none');

		if (setTestPwd == 'Y') {
			$('#setTestPwd').val('N');
		} else {
			$('#setTestPwd').val('Y');
		}

	});
	
	$('#smqChk').click(function() {
		var smqChk = $('#smqChk').val();
		$('#marksPerQueIn').parent().toggleClass('d-none');

		if (smqChk == 'Y') {
			$('#smqChk').val('N');
		} else {
			$('#smqChk').val('Y');
		}

	});

	/*CALLING RICH TEXT EDITOR*/
	/* $('.testDesc').richText(); */

	/*CALLING DATE PICKER*/
	$(function() {

		$('#testDateRangeBtn').daterangepicker({

			"showDropdowns" : true,
			"timePicker" : true,
			"showCustomRangeLabel" : false,
			"alwaysShowCalendars" : true,
			"opens" : "center",
		    "minDate": new Date()

		/* autoUpdateInput: false,
		locale: {
		    cancelLabel: 'Clear'
		} */
		},

		function(start, end, label) {
			var sDate = start.format('YYYY-MM-DD HH:mm:ss');
			var eDate = end.format('YYYY-MM-DD HH:mm:ss');
			$('#startDate').val(sDate + '-' + eDate);
			$('#testStartDate').val(sDate);
			$('#testEndDate').val(eDate);

		});

		/*  $('#startDate').on('apply.daterangepicker', function(ev, picker) {
		     $(this).val(picker.startDate.format('YYYY/MM/DD HH:mm:ss') + ' - ' + picker.endDate.format('YYYY/MM/DD HH:mm:ss'));
		 });
		 
		 $('#testDateRangeBtn').on('apply.daterangepicker', function(ev, picker) {
		     
		$('#startDate').val(picker.startDate.format('YYYY/MM/DD HH:mm:ss') + ' - ' + picker.endDate.format('YYYY/MM/DD HH:mm:ss'));
		
		var x = $('#startDate').val(picker.startDate.format('YYYY/MM/DD HH:mm:ss') + ' - ' + picker.endDate.format('YYYY/MM/DD HH:mm:ss'));
		
		console.log($('#startDate').val(picker.startDate.format('YYYY/MM/DD HH:mm:ss') + ' - ' + picker.endDate.format('YYYY/MM/DD HH:mm:ss')));
		     
		$('#startDate').on('apply.daterangepicker', function(start, end) {
		console.log("A new date selection was made: " + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
		}); 
		 
		 }); */

		$('#startDate').on('cancel.daterangepicker', function(ev, picker) {
			$(this).val('');
		});
		$('#testDateRangeBtn').on('cancel.daterangepicker',
				function(ev, picker) {
					$('#startDate').val('');
				});

	});
</script>

<script>
	$(function() {

		$('#semDashboardFaculty')
				.on(
						'change',
						function() {

							var selected = $('#semDashboardFaculty').val();
							var vals = selected.split('-')[0];

							console.log(vals);

							<c:forEach var='sem' items='${ sessionWiseCourseListMap }'>
							if (selected == '<c:out value="${sem.key}"/>') {

								document
										.getElementById("courseListSemWisefaculty").innerHTML = ""
								<c:forEach var='group' items='${ sessionWiseCourseListMap[sem.key] }'>
										+ '<div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5"> '
										+ '<div class="courseAsset d-flex align-items-start flex-column"> '
										+ '<h6 class="text-uppercase mb-auto">${ group.course.courseName }</h6>'
										+ '<span class="courseNav"> <a href="${pageContext.request.contextPath}/viewCourse?id=${group.course.id}">'
										+ '         <p class="caBg">View Course</p>'
										+ '</a>' + '</span>' + '</div>'
										+ '</div>'
								</c:forEach>;
							}
							</c:forEach>

						});

	});
	
	
	
	$(function() {

		$('#semSelectFaculty')
				.on(
						'change',
						function() {

							var selected = $('#semSelectFaculty').val();
							var vals = selected.split('-')[0];

							console.log(vals);

							<c:forEach var='sem' items='${ sessionWiseCourseListMap }'>
							if (selected == '<c:out value="${sem.key}"/>') {

								document.getElementById("courseListSemWisefaculty").innerHTML= ""
                                    <c:forEach var='group' items='${ sessionWiseCourseListMap[sem.key] }'>
                                    + '<div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5"> '
                                    + '<div class="courseAsset d-flex align-items-start flex-column"> '
                                    + '<h6 class="text-uppercase mb-auto">${ group.course.courseName }</h6>'
                                    + '<span class="courseNav"> <a href="${pageContext.request.contextPath}/createTestForm?courseId=${group.course.id}&acadSession=${group.course.acadSession}&acadYear=${group.course.acadYear}">'
                                    + '         <p class="caBg">Create Test</p>'
                                    + '</a> <a href="${pageContext.request.contextPath}/testList?courseId=${group.course.id}">'
                                    + '         <p class="ctBg">View Test</p>'
                                    + '</a>'
                                    + '</span>'
                                    + '</div>'
                                    + '</div>'

								</c:forEach>;
							}
							</c:forEach>

						});

	});
	
	
	
	
	
$(function() {
		
		$('#semSelectFacultyAssignment').on('change', function() {
			
			var selected = $('#semSelectFacultyAssignment').val();
			var vals = selected.split('-')[0];
			

			
			<c:forEach var='sem' items='${ sessionWiseCourseListMap }'>
				if(selected == '<c:out value="${sem.key}"/>'){
					
					document.getElementById("courseListSemWisefacultyAssignment").innerHTML= ""
						<c:forEach var='group' items='${ sessionWiseCourseListMap[sem.key] }'>
						+ '<div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5"> '
						+ '<div class="courseAsset d-flex align-items-start flex-column"> '
						+ '<h6 class="text-uppercase mb-auto">${ group.course.courseName }</h6>'
						+ '<span class="courseNav"> <a href="${pageContext.request.contextPath}/createAssignmentFromGroup?courseId=${group.course.id}">'
						+ '		<p class="caBg">Create Group Assignment</p>'
						+ '</a> <a href="${pageContext.request.contextPath}/createAssignmentFromMenu?courseId=${group.course.id}&acadSession=${group.course.acadSession}&acadYear=${group.course.acadYear}">'
						+ '		<p class="ctBg">Create Student Assignement</p>'
						+ '</a>'
						+ '</span>'
						+ '</div>'
						+ '</div>'
						</c:forEach>;
				}
			</c:forEach>

		});

	});
	
	
	
</script>


<script>
	//Timer Logic

	$("#DateCountdown").TimeCircles();
	//For Mobile
	$("#CountDownTimer").TimeCircles({
		count_past_zero : false,
		time : {
			Hours : {
				show : true,
				color : "#d53439"
			},
			Minutes : {
				show : true,
				color : "#2e8a00"
			},
			Seconds : {
				show : true,
				color : "#071e38"
			}

		}
	}).addListener(countdownComplete);

	var i = 0;
	var j = $('#durationCompletedByStudent').val();

	console.log('j value' + j)
	function countdownComplete(unit, value, total) {

		//console.log("function called 123123---"+i);
		//console.log('value--->' + value);

		/* if( value >49 && value <51 ){
			console.log('value is between 51 and 49');
		} */

		if (value == 0 && total != 0) {

			j++;

			saveCompletedDuration(j);
		}

		if (total == 0) {

			submitForm('studentTestForm-0', 'nav-0', true);
		}

		if (total == 0) {
			console.log("total " + total);
			/*
			 * $("#DateCountdown").TimeCircles().stop();
			 * $(this).fadeOut('slow').replaceWith( "<h2 style='margin-top:5%;'>Time
			 * Over!</h2>");
			 */

			alert("Times up2!");
			window.location.href = 'testList';
			$('#test_quiz_pop2').fadeOut('slow');
		}
	}

	$("#CountDownTimerMobile, #CountDownTimer").TimeCircles({
		circle_bg_color : "#d2d2d2"
	});

	//For larger devices

	$("#PageOpenTimer").TimeCircles();

	var updateTime = function() {
		var date = $("#date").val();
		var time = $("#time").val();
		var datetime = date + ' ' + time + ':00';
		$("#DateCountdown").data('date', datetime).TimeCircles().start();
	}
	$("#date").change(updateTime).keyup(updateTime);
	$("#time").change(updateTime).keyup(updateTime);
	//Stopwatch Timer end
</script>

<!-- End ViewFinal Test -->


<!-- Start newUserAnnouncementList -->
<script>
$(function() {

            $('#stProgram')
                        .on('change',
                                    function() {
                                    
                                    var selected = $('#stProgram').val();
                                    console.log(selected);
                                    var str = "";
                                    <c:forEach var="announcement"
                                          items="${announcementTypeMap['PROGRAM']}" varStatus="status">
                                          if (selected == 'ALL') {
                                                str += ' <div class="announcementItem" data-toggle="modal" '
                                                      
                                                      +' data-target="#modalAnnounceProgram${status.count}"> '
                                                      +' <h6 class="card-title">${announcement.subject}<sup '
                                                      +' class="announcementDate text-danger font-italic"><small><span '
                                                      +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                                      +' class="toDate">${announcement.endDate}</span></small></sup> '
                                                      +' </h6> '
                                                      
                                                      +' <p class="border-bottom"></p> '
                                                      +' </div> '
                                                      +' <div id="modalAnnouncement position-fixed"> '
                                                      +' <div class="modal fade fnt-13" id="modalAnnounceProgram${status.count}" '
                                                      +' tabindex="-1" role="dialog" '
                                                      +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                                      +' <div '
                                                      +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                                      +' role="document"> '
                                                      +' <div class="modal-content"> '
                                                      +' <div class="modal-header"> '
                                                      +' <h6 class="modal-title">${announcement.subject}</h6> '
                                                      +' <button type="button" class="close" data-dismiss="modal" '
                                                      +' aria-label="Close"> '
                                                      +' <span aria-hidden="true">&times;</span> '
                                                      +' </button> '
                                                      +' </div> '
                                                      +' <div class="modal-body"> '
                                                      +' <div class="d-flex font-weight-bold"> '
                                                      +' <p class="mr-auto"> '
                                                      +' Start Date: <span>${announcement.startDate}</span> '
                                                      +' </p> '
                                                      +' <p> '
                                                      +' End Date: <span>${announcement.endDate}</span> '
                                                      +' </p> '
                                                      +' </div><c:if test="${announcement.filePath ne null}"> '
                                                      +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                                      +' </c:if> '

                                                      +' <p class="announcementDetail"> ' ;
                                                      
                                                str   += `${announcement.description}`;
                                                
                                                str += ' </p> ' 
                                                      +' </div> '
                                                      +' <div class="modal-footer"> '
                                                      +' <button type="button" class="btn btn-modalClose" '
                                                      +' data-dismiss="modal">Close</button> '
                                                      +' </div> '
                                                      +' </div> '
                                                      +' </div> '
                                                      +' </div> '
                                                      +' </div> ';
                                          }else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
                                          str += ' <div class="announcementItem" data-toggle="modal" '
                                          
                                                +' data-target="#modalAnnounceProgram${status.count}"> '
                                                +' <h6 class="card-title">${announcement.subject}<sup '
                                                +' class="announcementDate text-danger font-italic"><small><span '
                                                +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                                +' class="toDate">${announcement.endDate}</span></small></sup> '
                                                +' </h6> '
                                                
                                                +' <p class="border-bottom"></p> '
                                                +' </div> '
                                                +' <div id="modalAnnouncement position-fixed"> '
                                                +' <div class="modal fade fnt-13" id="modalAnnounceProgram${status.count}" '
                                                +' tabindex="-1" role="dialog" '
                                                +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                                +' <div '
                                                +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                                +' role="document"> '
                                                +' <div class="modal-content"> '
                                                +' <div class="modal-header"> '
                                                +' <h6 class="modal-title">${announcement.subject}</h6> '
                                                +' <button type="button" class="close" data-dismiss="modal" '
                                                +' aria-label="Close"> '
                                                +' <span aria-hidden="true">&times;</span> '
                                                +' </button> '
                                                +' </div> '
                                                +' <div class="modal-body"> '
                                                +' <div class="d-flex font-weight-bold"> '
                                                +' <p class="mr-auto"> '
                                                +' Start Date: <span>${announcement.startDate}</span> '
                                                +' </p> '
                                                +' <p> '
                                                +' End Date: <span>${announcement.endDate}</span> '
                                                +' </p> '
                                                +' </div><c:if test="${announcement.filePath ne null}"> '
                                                +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                                +' </c:if> '

                                                +' <p class="announcementDetail"> ' ;
                                                
                                          str   += `${announcement.description}`;
                                          
                                          str += ' </p> ' 
                                                +' </div> '
                                                +' <div class="modal-footer"> '
                                                +' <button type="button" class="btn btn-modalClose" '
                                                +' data-dismiss="modal">Close</button> '
                                                +' </div> '
                                                +' </div> '
                                                +' </div> '
                                                +' </div> '
                                                +' </div> ';
                                          }
                                    </c:forEach>
                                    document.getElementById("programAnn").innerHTML = "" + str;
                  });
            
            $('#stCourse')
            .on('change',
                        function() {
                        
                        var selected = $('#stCourse').val();
                        console.log(selected);
                        var str = "";
                        <c:forEach var="announcement"
                              items="${announcementTypeMap['COURSE']}" varStatus="status">
                              if (selected == 'ALL') {
                                    str += ' <div class="announcementItem" data-toggle="modal" '
                                          
                                          +' data-target="#modalAnnounceCourse${status.count}"> '
                                          +' <h6 class="card-title">${announcement.subject}<sup '
                                          +' class="announcementDate text-danger font-italic"><small><span '
                                          +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                          +' class="toDate">${announcement.endDate}</span></small></sup> '
                                          +' </h6> '
                                          
                                          +' <p class="border-bottom"></p> '
                                          +' </div> '
                                          +' <div id="modalAnnouncement position-fixed"> '
                                          +' <div class="modal fade fnt-13" id="modalAnnounceCourse${status.count}" '
                                          +' tabindex="-1" role="dialog" '
                                          +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                          +' <div '
                                          +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                          +' role="document"> '
                                          +' <div class="modal-content"> '
                                          +' <div class="modal-header"> '
                                          +' <h6 class="modal-title">${announcement.subject}</h6> '
                                          +' <button type="button" class="close" data-dismiss="modal" '
                                          +' aria-label="Close"> '
                                          +' <span aria-hidden="true">&times;</span> '
                                          +' </button> '
                                          +' </div> '
                                          +' <div class="modal-body"> '
                                          +' <div class="d-flex font-weight-bold"> '
                                          +' <p class="mr-auto"> '
                                          +' Start Date: <span>${announcement.startDate}</span> '
                                          +' </p> '
                                          +' <p> '
                                          +' End Date: <span>${announcement.endDate}</span> '
                                          +' </p> '
                                          +' </div><c:if test="${announcement.filePath ne null}"> '
                                          +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                          +' </c:if> '

                                          +' <p class="announcementDetail"> ' ;
                                          
                                    str   += `${announcement.description}`;
                                    
                                    str += ' </p> ' 
                                          +' </div> '
                                          +' <div class="modal-footer"> '
                                          +' <button type="button" class="btn btn-modalClose" '
                                          +' data-dismiss="modal">Close</button> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> ';
                              }else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
                              str += ' <div class="announcementItem" data-toggle="modal" '
                              
                                    +' data-target="#modalAnnounceCourse${status.count}"> '
                                    +' <h6 class="card-title">${announcement.subject}<sup '
                                    +' class="announcementDate text-danger font-italic"><small><span '
                                    +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                    +' class="toDate">${announcement.endDate}</span></small></sup> '
                                    +' </h6> '
                                    
                                    +' <p class="border-bottom"></p> '
                                    +' </div> '
                                    +' <div id="modalAnnouncement position-fixed"> '
                                    +' <div class="modal fade fnt-13" id="modalAnnounceCourse${status.count}" '
                                    +' tabindex="-1" role="dialog" '
                                    +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                    +' <div '
                                    +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                    +' role="document"> '
                                    +' <div class="modal-content"> '
                                    +' <div class="modal-header"> '
                                    +' <h6 class="modal-title">${announcement.subject}</h6> '
                                    +' <button type="button" class="close" data-dismiss="modal" '
                                    +' aria-label="Close"> '
                                    +' <span aria-hidden="true">&times;</span> '
                                    +' </button> '
                                    +' </div> '
                                    +' <div class="modal-body"> '
                                    +' <div class="d-flex font-weight-bold"> '
                                    +' <p class="mr-auto"> '
                                    +' Start Date: <span>${announcement.startDate}</span> '
                                    +' </p> '
                                    +' <p> '
                                    +' End Date: <span>${announcement.endDate}</span> '
                                    +' </p> '
                                    +' </div><c:if test="${announcement.filePath ne null}"> '
                                    +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                    +' </c:if> '

                                    +' <p class="announcementDetail"> ' ;
                                    
                              str   += `${announcement.description}`;
                              
                              str += ' </p> ' 
                                    +' </div> '
                                    +' <div class="modal-footer"> '
                                    +' <button type="button" class="btn btn-modalClose" '
                                    +' data-dismiss="modal">Close</button> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> ';
                              }
                        </c:forEach>
                        document.getElementById("courseAnn").innerHTML = "" + str;
            });
            
            $('#stInstitute')
            .on('change',
                        function() {
                        
                        var selected = $('#stInstitute').val();
                        console.log(selected);
                        var str = "";
                        <c:forEach var="announcement"
                              items="${announcementTypeMap['INSTITUTE']}" varStatus="status">
                              if (selected == 'ALL') {
                                    str += ' <div class="announcementItem" data-toggle="modal" '
                                          
                                          +' data-target="#modalAnnounceInstitute${status.count}"> '
                                          +' <h6 class="card-title">${announcement.subject}<sup '
                                          +' class="announcementDate text-danger font-italic"><small><span '
                                          +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                          +' class="toDate">${announcement.endDate}</span></small></sup> '
                                          +' </h6> '
                                          
                                          +' <p class="border-bottom"></p> '
                                          +' </div> '
                                          +' <div id="modalAnnouncement position-fixed"> '
                                          +' <div class="modal fade fnt-13" id="modalAnnounceInstitute${status.count}" '
                                          +' tabindex="-1" role="dialog" '
                                          +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                          +' <div '
                                          +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                          +' role="document"> '
                                          +' <div class="modal-content"> '
                                          +' <div class="modal-header"> '
                                          +' <h6 class="modal-title">${announcement.subject}</h6> '
                                          +' <button type="button" class="close" data-dismiss="modal" '
                                          +' aria-label="Close"> '
                                          +' <span aria-hidden="true">&times;</span> '
                                          +' </button> '
                                          +' </div> '
                                          +' <div class="modal-body"> '
                                          +' <div class="d-flex font-weight-bold"> '
                                          +' <p class="mr-auto"> '
                                          +' Start Date: <span>${announcement.startDate}</span> '
                                          +' </p> '
                                          +' <p> '
                                          +' End Date: <span>${announcement.endDate}</span> '
                                          +' </p> '
                                          +' </div><c:if test="${announcement.filePath ne null}"> '
                                          +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                          +' </c:if> '

                                          +' <p class="announcementDetail"> ' ;
                                          
                                    str   += `${announcement.description}`;
                                    
                                    str += ' </p> ' 
                                          +' </div> '
                                          +' <div class="modal-footer"> '
                                          +' <button type="button" class="btn btn-modalClose" '
                                          +' data-dismiss="modal">Close</button> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> ';
                              }else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
                              str += ' <div class="announcementItem" data-toggle="modal" '
                              
                                    +' data-target="#modalAnnounceInstitute${status.count}"> '
                                    +' <h6 class="card-title">${announcement.subject}<sup '
                                    +' class="announcementDate text-danger font-italic"><small><span '
                                    +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                    +' class="toDate">${announcement.endDate}</span></small></sup> '
                                    +' </h6> '
                                    
                                    +' <p class="border-bottom"></p> '
                                    +' </div> '
                                    +' <div id="modalAnnouncement position-fixed"> '
                                    +' <div class="modal fade fnt-13" id="modalAnnounceInstitute${status.count}" '
                                    +' tabindex="-1" role="dialog" '
                                    +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                    +' <div '
                                    +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                    +' role="document"> '
                                    +' <div class="modal-content"> '
                                    +' <div class="modal-header"> '
                                    +' <h6 class="modal-title">${announcement.subject}</h6> '
                                    +' <button type="button" class="close" data-dismiss="modal" '
                                    +' aria-label="Close"> '
                                    +' <span aria-hidden="true">&times;</span> '
                                    +' </button> '
                                    +' </div> '
                                    +' <div class="modal-body"> '
                                    +' <div class="d-flex font-weight-bold"> '
                                    +' <p class="mr-auto"> '
                                    +' Start Date: <span>${announcement.startDate}</span> '
                                    +' </p> '
                                    +' <p> '
                                    +' End Date: <span>${announcement.endDate}</span> '
                                    +' </p> '
                                    +' </div><c:if test="${announcement.filePath ne null}"> '
                                    +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                    +' </c:if> '

                                    +' <p class="announcementDetail"> ' ;
                                    
                              str   += `${announcement.description}`;
                              
                              str += ' </p> ' 
                                    +' </div> '
                                    +' <div class="modal-footer"> '
                                    +' <button type="button" class="btn btn-modalClose" '
                                    +' data-dismiss="modal">Close</button> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> ';
                              }
                        </c:forEach>
                        document.getElementById("instituteAnn").innerHTML = "" + str;
      });
            
            $('#stLibrary')
            .on('change',
                        function() {
                        
                        var selected = $('#stLibrary').val();
                        console.log(selected);
                        var str = "";
                        <c:forEach var="announcement"
                              items="${announcementTypeMap['LIBRARY']}" varStatus="status">
                              if (selected == 'ALL') {
                                    str += ' <div class="announcementItem" data-toggle="modal" '
                                          
                                          +' data-target="#modalAnnounceLibrary${status.count}"> '
                                          +' <h6 class="card-title">${announcement.subject}<sup '
                                          +' class="announcementDate text-danger font-italic"><small><span '
                                          +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                          +' class="toDate">${announcement.endDate}</span></small></sup> '
                                          +' </h6> '
                                          
                                          +' <p class="border-bottom"></p> '
                                          +' </div> '
                                          +' <div id="modalAnnouncement position-fixed"> '
                                          +' <div class="modal fade fnt-13" id="modalAnnounceLibrary${status.count}" '
                                          +' tabindex="-1" role="dialog" '
                                          +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                          +' <div '
                                          +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                          +' role="document"> '
                                          +' <div class="modal-content"> '
                                          +' <div class="modal-header"> '
                                          +' <h6 class="modal-title">${announcement.subject}</h6> '
                                          +' <button type="button" class="close" data-dismiss="modal" '
                                          +' aria-label="Close"> '
                                          +' <span aria-hidden="true">&times;</span> '
                                          +' </button> '
                                          +' </div> '
                                          +' <div class="modal-body"> '
                                          +' <div class="d-flex font-weight-bold"> '
                                          +' <p class="mr-auto"> '
                                          +' Start Date: <span>${announcement.startDate}</span> '
                                          +' </p> '
                                          +' <p> '
                                          +' End Date: <span>${announcement.endDate}</span> '
                                          +' </p> '
                                          +' </div><c:if test="${announcement.filePath ne null}"> '
                                          +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                          +' </c:if> '

                                          +' <p class="announcementDetail"> ' ;
                                          
                                    str   += `${announcement.description}`;
                                    
                                    str += ' </p> ' 
                                          +' </div> '
                                          +' <div class="modal-footer"> '
                                          +' <button type="button" class="btn btn-modalClose" '
                                          +' data-dismiss="modal">Close</button> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> ';
                              }else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
                              str += ' <div class="announcementItem" data-toggle="modal" '
                              
                                    +' data-target="#modalAnnounceLibrary${status.count}"> '
                                    +' <h6 class="card-title">${announcement.subject}<sup '
                                    +' class="announcementDate text-danger font-italic"><small><span '
                                    +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                    +' class="toDate">${announcement.endDate}</span></small></sup> '
                                    +' </h6> '
                                    
                                    +' <p class="border-bottom"></p> '
                                    +' </div> '
                                    +' <div id="modalAnnouncement position-fixed"> '
                                    +' <div class="modal fade fnt-13" id="modalAnnounceLibrary${status.count}" '
                                    +' tabindex="-1" role="dialog" '
                                    +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                    +' <div '
                                    +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                    +' role="document"> '
                                    +' <div class="modal-content"> '
                                    +' <div class="modal-header"> '
                                    +' <h6 class="modal-title">${announcement.subject}</h6> '
                                    +' <button type="button" class="close" data-dismiss="modal" '
                                    +' aria-label="Close"> '
                                    +' <span aria-hidden="true">&times;</span> '
                                    +' </button> '
                                    +' </div> '
                                    +' <div class="modal-body"> '
                                    +' <div class="d-flex font-weight-bold"> '
                                    +' <p class="mr-auto"> '
                                    +' Start Date: <span>${announcement.startDate}</span> '
                                    +' </p> '
                                    +' <p> '
                                    +' End Date: <span>${announcement.endDate}</span> '
                                    +' </p> '
                                    +' </div><c:if test="${announcement.filePath ne null}"> '
                                    +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                    +' </c:if> '

                                    +' <p class="announcementDetail"> ' ;
                                    
                              str   += `${announcement.description}`;
                              
                              str += ' </p> ' 
                                    +' </div> '
                                    +' <div class="modal-footer"> '
                                    +' <button type="button" class="btn btn-modalClose" '
                                    +' data-dismiss="modal">Close</button> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> ';
                              }
                        </c:forEach>
                        document.getElementById("libraryAnn").innerHTML = "" + str;
      });
            
            $('#stCounselor')
            .on('change',
                        function() {
                        
                        var selected = $('#stCounselor').val();
                        console.log(selected);
                        var str = "";
                        <c:forEach var="announcement"
                              items="${announcementTypeMap['COUNSELOR']}" varStatus="status">
                              if (selected == 'ALL') {
                                    str += ' <div class="announcementItem" data-toggle="modal" '
                                          
                                          +' data-target="#modalAnnounceCounselor${status.count}"> '
                                          +' <h6 class="card-title">${announcement.subject}<sup '
                                          +' class="announcementDate text-danger font-italic"><small><span '
                                          +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                          +' class="toDate">${announcement.endDate}</span></small></sup> '
                                          +' </h6> '
                                          
                                          +' <p class="border-bottom"></p> '
                                          +' </div> '
                                          +' <div id="modalAnnouncement position-fixed"> '
                                          +' <div class="modal fade fnt-13" id="modalAnnounceCounselor${status.count}" '
                                          +' tabindex="-1" role="dialog" '
                                          +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                          +' <div '
                                          +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                          +' role="document"> '
                                          +' <div class="modal-content"> '
                                          +' <div class="modal-header"> '
                                          +' <h6 class="modal-title">${announcement.subject}</h6> '
                                          +' <button type="button" class="close" data-dismiss="modal" '
                                          +' aria-label="Close"> '
                                          +' <span aria-hidden="true">&times;</span> '
                                          +' </button> '
                                          +' </div> '
                                          +' <div class="modal-body"> '
                                          +' <div class="d-flex font-weight-bold"> '
                                          +' <p class="mr-auto"> '
                                          +' Start Date: <span>${announcement.startDate}</span> '
                                          +' </p> '
                                          +' <p> '
                                          +' End Date: <span>${announcement.endDate}</span> '
                                          +' </p> '
                                          +' </div><c:if test="${announcement.filePath ne null}"> '
                                          +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                          +' </c:if> '

                                          +' <p class="announcementDetail"> ' ;
                                          
                                    str   += `${announcement.description}`;
                                    
                                    str += ' </p> ' 
                                          +' </div> '
                                          +' <div class="modal-footer"> '
                                          +' <button type="button" class="btn btn-modalClose" '
                                          +' data-dismiss="modal">Close</button> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> '
                                          +' </div> ';
                              }else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
                              str += ' <div class="announcementItem" data-toggle="modal" '
                              
                                    +' data-target="#modalAnnounceCounselor${status.count}"> '
                                    +' <h6 class="card-title">${announcement.subject}<sup '
                                    +' class="announcementDate text-danger font-italic"><small><span '
                                    +' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
                                    +' class="toDate">${announcement.endDate}</span></small></sup> '
                                    +' </h6> '
                                    
                                    +' <p class="border-bottom"></p> '
                                    +' </div> '
                                    +' <div id="modalAnnouncement position-fixed"> '
                                    +' <div class="modal fade fnt-13" id="modalAnnounceCounselor${status.count}" '
                                    +' tabindex="-1" role="dialog" '
                                    +' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
                                    +' <div '
                                    +' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
                                    +' role="document"> '
                                    +' <div class="modal-content"> '
                                    +' <div class="modal-header"> '
                                    +' <h6 class="modal-title">${announcement.subject}</h6> '
                                    +' <button type="button" class="close" data-dismiss="modal" '
                                    +' aria-label="Close"> '
                                    +' <span aria-hidden="true">&times;</span> '
                                    +' </button> '
                                    +' </div> '
                                    +' <div class="modal-body"> '
                                    +' <div class="d-flex font-weight-bold"> '
                                    +' <p class="mr-auto"> '
                                    +' Start Date: <span>${announcement.startDate}</span> '
                                    +' </p> '
                                    +' <p> '
                                    +' End Date: <span>${announcement.endDate}</span> '
                                    +' </p> '
                                    +' </div><c:if test="${announcement.filePath ne null}"> '
                                    +' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
                                    +' </c:if> '

                                    +' <p class="announcementDetail"> ' ;
                                    
                              str   += `${announcement.description}`;
                              
                              str += ' </p> ' 
                                    +' </div> '
                                    +' <div class="modal-footer"> '
                                    +' <button type="button" class="btn btn-modalClose" '
                                    +' data-dismiss="modal">Close</button> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> '
                                    +' </div> ';
                              }
                        </c:forEach>
                        document.getElementById("counselorAnn").innerHTML = "" + str;
      });
                                    
      });
</script>
<!-- End newUserAnnouncementList -->


				<!-- <script type="text/javascript"
					src="//cdn.ckeditor.com/4.8.0/standard-all/ckeditor.js"></script>
				<script type="text/javascript"
					src="//cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script> -->
					<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/MathJax.js?config=TeX-AMS_HTML"></script>

<script src="<c:url value="/resources/ckeditor/ckeditor.js" />"></script>  

				<script>
					$('#frmTest')
							.click(
									function() {
										//alert('function called');
										var i = 0;
										$(".testQuestionPoolIds")
												.each(
														function() {

															var checkBoxId = $(
																	this).attr(
																	'id');

															var checked = document
																	.querySelectorAll('input[type="checkbox"]:checked');
															//$("input[@id=" + checkBoxId + "]:checked").length;

															if (checked.length != 0) {
																i++;
															}
														});

										if (i == 0) {
											alert('please select question');
											return false;
										} else {
											//alert('checked');
										}

									});
				</script>

				<script>
					$(document).ready(function() {

						$(".hiddenField").hide();
					});

					$("select").change(function() {

						if ($(this).val() == "Objective") {

							$(".hiddenField").show();
						} else if ($(this).val() == "Subjective") {
							$(".hiddenField").hide();
						} else {
							$(".hiddenField").show();
						}
					})
				</script>


				<script>
					$(".ckeditorClass")
							.each(
									function() {

										CKEDITOR
												.replace(
														$(this).attr('id'),
														{
															extraPlugins : 'mathjax',
															mathJaxLib : 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/MathJax.js?config=TeX-AMS_HTML'
														});
									});
				</script>


				<script>
					$("#testPoolId")
							.on(
									'change',
									function() {

										var selectedValue = $(this).val();
										var testId = $('testId').val();
										window.location = '${pageContext.request.contextPath}/viewTestQuestionsByTestPool?testId='+ ${testId}+'&testPoolId='+ encodeURIComponent(selectedValue);

										return false;
									});
					
					
					$('#example-select-all-questions').on('click', function() {
						// Check/uncheck all checkboxes in the table
						
						$('.testQuestionPoolIds').prop('checked', this.checked);
					});
				</script>
</body>
</html>
				
				