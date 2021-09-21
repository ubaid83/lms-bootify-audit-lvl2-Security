<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex adminPage" id="adminPage">
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	<jsp:include page="../common/rightSidebarAdmin.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newAdminTopHeader.jsp" />

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">

			<!-- Input Form Panel -->

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /></li>
							<li class="breadcrumb-item active" aria-current="page"><sec:authorize
									access="hasRole('ROLE_STUDENT')">
									<c:out value="${AcadSession}" />
								</sec:authorize> Assignment</li>
						</ol>
					</nav>
					<jsp:include page="../common/alert.jsp" />
					
					<!-- Input Form Panel -->
					<c:if test="${assignmentId ne null}">
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

					<c:if test="${assignmentId eq null}">

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
															<%-- <c:if test="${testQuestionVar.testType eq 'Objective'}">


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
															</c:if> --%>


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

					<c:if test="${showQuestionByPool && assignmentId ne null}">
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
														<a href="viewAssignment?id=${assignmentId}"><i
															class="btn btn-large btn-dark">Proceed to allocate
																students</i></a>
														</sec:authorize>
														<sec:authorize access="hasRole('ROLE_ADMIN')">
														<a href="viewAssignment?id=${assignmentId}"><i
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
														<a href="viewAssignment?id=${assignmentId}"><i
															class="btn btn-large btn-dark">Proceed to view allocated 
																students</i></a>
														</sec:authorize>
														<sec:authorize access="hasRole('ROLE_ADMIN')">
														<a href="viewAssignment?id=${assignmentId}"><i
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
											<form:form action="saveAssignmentQuestionsByTestPool"
												id="saveTestQuestions" method="post"
												modelAttribute="testPool">
												<form:input path="assignmentId" type="hidden" id="assignmentId1" />
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
																		<c:if test="${assignmentId ne null}">
																			<c:if test="${empty testQuestionVar.assignmentQuestionId }">
																				<form:checkbox path="testQuestionsPoolIds"
																					value="${testQuestionVar.id}"
																					class="testQuestionPoolIds"
																					id='checkBoxAllocate${status.index}' />
																			</c:if>
																		</c:if>
																		<c:if
																			test="${not empty testQuestionVar.assignmentQuestionId }">
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
																			<%-- <form:input type="hidden"
																				path="testQuestionPools[${status.index}].optionShuffle"
																				value="${testQuestionVar.optionShuffle}" /> --%>
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

																		<%-- <c:if
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
																		</div> --%>

																		<%-- <c:if
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
																		</c:if> --%>


																	</div>
																</div>
															</div>
														</div>
													</c:forEach>
													<c:if test="${assignmentId ne null}">
														<div class="text-center mt-5">
															<div class="form-group">
																<sec:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_ADMIN')">
																	<button class="btn btn-large btn-success"
																		onclick="return clicked();" id="frmTest"
																		formaction="saveAssignmentQuestionsByTestPool">Allocate
																		Questions</button>

																	<c:if test="${randomQuestion eq 'Y'}">
																		<button class="btn btn-large btn-success"
																			onclick="return clicked();"
																			formaction="saveAllAssignmentQuestionsByTestPool">Allocate
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
                    
                    
					<!-- /page content -->



				

			<!-- /page content: END -->




		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->
		<jsp:include page="../common/newAdminFooter.jsp" />





		<script type="text/javascript"
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
		</script>
		
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
										var assignmentId = $('assignmentId').val();
										window.location = '${pageContext.request.contextPath}/viewAssignmentQuestionsByTestPool?assignmentId='+ ${assignmentId}+'&testPoolId='+ encodeURIComponent(selectedValue);

										return false;
									});
					
					
					$('#example-select-all-questions').on('click', function() {
						// Check/uncheck all checkboxes in the table
						
						$('.testQuestionPoolIds').prop('checked', this.checked);
					});
				</script>
		