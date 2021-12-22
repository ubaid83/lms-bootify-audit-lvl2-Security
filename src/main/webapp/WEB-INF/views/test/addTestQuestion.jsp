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
								Objective Test Question</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5 class="text-center border-bottom pb-2">Add Test Question</h5>
										<sec:authorize access="hasRole('ROLE_FACULTY')">
										<c:if test="${showProceed}">
											<c:if test="${disabled ne true}">
												<div class="col-12 text-left">
													<div class="form-group">
														<a href="viewTestDetails?testId=${testId}"><i
															class="btn btn-large btn-primary">Proceed to allocate
																students</i></a>
													</div>
												</div>
											</c:if>
										</c:if>
										<c:if test="${showStudents}">
											<c:if test="${disabled ne true}">
												<div class="col-12 text-left">
													<div class="form-group">
														<a href="viewTestDetails?testId=${testId}"><i
															class="btn btn-large btn-primary">Proceed to view
																allocated students</i></a>
													</div>
												</div>
											</c:if>
										</c:if>
										</sec:authorize>
										<sec:authorize access="hasRole('ROLE_ADMIN')">
											<c:if test="${showProceed}">
											<c:if test="${disabled ne true}">
												<div class="col-12 text-left">
													<div class="form-group">
														<a href="viewTestDetailsByAdmin?testId=${testId}"><i
															class="btn btn-large btn-primary">Proceed to allocate
																students</i></a>
													</div>
												</div>
											</c:if>
										</c:if>
										<c:if test="${showStudents}">
											<c:if test="${disabled ne true}">
												<div class="col-12 text-left">
													<div class="form-group">
														<a href="viewTestDetailsByAdmin?testId=${testId}"><i
															class="btn btn-large btn-primary">Proceed to view
																allocated students</i></a>
													</div>
												</div>
											</c:if>
										</c:if>
										</sec:authorize>
										
									</div>

									<div class="x_content">
										<c:if test="${not empty test.testQuestions}">

											<div class="panel-group" id="testQuestionAccordion"
												role="tablist" aria-multiselectable="true">
												<c:forEach items="${test.testQuestions}"
													var="testQuestionVar" varStatus="status">




													<div class="panel panel-default" id="#testQuestionAccordion">
														<div class="panel-heading" role="tab"
															id="heading-${status.index}">
															<h4 class="panel-title" data-toggle="collapse"
																	data-target="#collapse-${status.index}" aria-expanded="false"
																	aria-controls="collapse-${status.index}">
																Q${status.index+1}
																		 (Marks :
																${testQuestionVar.marks})
															</h4>
														</div>
														<div id="collapse-${status.index}"
															class="panel-collapse collapse" role="tabpanel"
															aria-labelledby="heading-${status.index}" data-parent="#testQuestionAccordion">
															<div class="panel-body">
																<div class="imageUrl">
																<button id="genLink" class="btn btn-modalSub mb-2 mt-2" name="genLink" 
																		 data-toggle="modal" data-target="#genLinkModal">
																	Generate Image Url</button>
																</div>
																<form:form action="updateTestQuestion" method="post"
																	modelAttribute="test"
																	id="testQuestionForm-${status.index}">
																	<div class="col-sm-12">
																		<form:input type="hidden"
																			path="testQuestions[${status.index}].id"
																			value="${testQuestionVar.id}" />
																		<form:input type="hidden"
																			path="testQuestions[${status.index}].questionType"
																			value="${testQuestionVar.questionType}" />
																		<div class="form-group">
																			
																			<form:label
																				path="testQuestions[${status.index}].description"
																				for="description">Description</form:label>
																			<%-- <form:input id="description"
																					path="testQuestions[${status.index}].description"
																					type="textarea" required="required"
																					placeholder="Description" class="form-control"
																					value="${testQuestionVar.description}" /> --%>

																			<form:textarea class="form-group ckeditorClass"
																				readonly="${disabled}"
																				path="testQuestions[${status.index}].description"
																				name="description" id="editor${status.index}"
																				rows="10" cols="80" value="${testQuestionVar.description}"/>
																		</div>
																	</div>
																	<div class="col-sm-6">
																		<div class="form-group">
																			<form:label
																				path="testQuestions[${status.index}].marks"
																				for="marks">Marks <span
																					style="color: red">*</span>
																			</form:label>
																			<%-- <form:input id="marks" readonly="${disabled}"
																					path="testQuestions[${status.index}].marks"
																					type="number" required="required"
																					placeholder="Marks" class="form-control"
																					value="${testQuestionVar.marks}" /> --%>

																			<c:if test="${randomQuestionStatus eq 'N' && sameMarksQue eq 'N'}">
																				<form:input id="marks" readonly="${disabled}"
																					path="testQuestions[${status.index}].marks"
																					type="number" required="required"
																					placeholder="Marks" class="form-control"
																					value="${testQuestionVar.marks}" step=".00001" />
																			</c:if>

																			<c:if
																				test="${randomQuestionStatus eq 'Y' && (empty sameMarksQue || sameMarksQue eq 'N')}">
																				<form:select id="marks" name="marks"
																					path="testQuestions[${status.index}].marks"
																					class="form-control">
																					<c:forEach var="testConfig"
																						items="${testConfigList}">

																						<c:if
																							test="${ testConfig.marks eq testQuestionVar.marks }">
																							<option value="${testConfig.marks}" selected>${testConfig.marks}</option>
																						</c:if>
																						<c:if
																							test="${ testConfig.marks ne testQuestionVar.marks }">
																							<option value="${testConfig.marks}">${testConfig.marks}</option>
																						</c:if>


																					</c:forEach>
																				</form:select>
																			</c:if>

																			<c:if
																				test="${ sameMarksQue eq 'Y' }">
																				<form:input id="marks" readonly="true"
																					path="testQuestions[${status.index}].marks"
																					type="number" required="required"
																					placeholder="Marks" class="form-control"
																					value="${testQuestionVar.marks}" />
																			</c:if>
																		</div>
																	</div>

																	<c:if
																		test="${testQuestionVar.questionType eq 'MCQ' || testQuestionVar.questionType eq null || testQuestionVar.questionType eq 'Image'}">
																		<div class="col-sm-6">
																			<div class="form-group">
																				<label for="type">Selection Type</label>
																				<div class="clearfix"></div>
																				<label class="radio-inline"> <form:radiobutton
																						path="testQuestions[${status.index}].type"
																						value="SINGLESELECT"
																						onclick="singleSelectClicked('testQuestionForm-${status.index}')" />
																					Single Select
																				</label> <label class="radio-inline"> </label>
																				<form:radiobutton
																					path="testQuestions[${status.index}].type"
																					value="MULTISELECT"
																					onclick="multipleSelectClicked('testQuestionForm-${status.index}')" />
																				Multiple Select
																			</div>
																		</div>
																		<div class="col-sm-6 ">
																			<div class="form-group">
																				<label for="optionShuffle">Option Shuffle
																					Required?</label>
																				<form:select id="optionShuffle" name="optionShuffle"
																					class="form-control"
																					path="testQuestions[${status.index}].optionShuffle">

																					<c:choose>
																						<c:when
																							test="${testQuestionVar.optionShuffle eq 'Y'}">


																							<option value="Y" selected>Y</option>
																							<option value="N">N</option>
																						</c:when>
																						<c:when
																							test="${testQuestionVar.optionShuffle eq 'N'}">
																							<option value="Y">Y</option>
																							<option value="N" selected>N</option>
																						</c:when>
																						<c:otherwise>
																							<option value="">Select</option>
																							<option value="Y">Y</option>
																							<option value="N">N</option>
																						</c:otherwise>
																					</c:choose>
																				</form:select>
																			</div>
																		</div>
																		<div class="testOptions">
																			<div class="col-sm-12">
																				<div class="form-group">
																					<form:label
																						path="testQuestions[${status.index}].option1"
																						for="option1">Option 1</form:label>
																					<div class="input-group">
																						<span class="input-group-addon"> <c:choose>
																								<c:when
																									test="${testQuestionVar.type eq 'SINGLESELECT'}">
																									<form:radiobutton
																										path="testQuestions[${status.index}].correctOption"
																										value="1" />
																								</c:when>
																								<c:otherwise>
																									<form:checkbox
																										path="testQuestions[${status.index}].correctOptions"
																										value="1" />
																								</c:otherwise>
																							</c:choose>
																						</span>
																						<form:input id="option1" readonly="${disabled}"
																							path="testQuestions[${status.index}].option1"
																							type="textarea" placeholder="Option 1"
																							class="form-control"
																							value="${testQuestionVar.option1}" />
																					</div>
																				</div>
																			</div>
																			<div class="col-sm-12">
																				<div class="form-group">
																					<form:label
																						path="testQuestions[${status.index}].option2"
																						for="option2">Option 2</form:label>
																					<div class="input-group">
																						<span class="input-group-addon"> <c:choose>
																								<c:when
																									test="${testQuestionVar.type eq 'SINGLESELECT'}">
																									<form:radiobutton
																										path="testQuestions[${status.index}].correctOption"
																										value="2" />
																								</c:when>
																								<c:otherwise>
																									<form:checkbox
																										path="testQuestions[${status.index}].correctOptions"
																										value="2" />
																								</c:otherwise>
																							</c:choose>
																						</span>
																						<form:input id="option2" readonly="${disabled}"
																							path="testQuestions[${status.index}].option2"
																							type="textarea" placeholder="Option 2"
																							class="form-control"
																							value="${testQuestionVar.option2}" />
																					</div>
																				</div>
																			</div>
																			<div class="col-sm-12">
																				<div class="form-group">
																					<form:label
																						path="testQuestions[${status.index}].option3"
																						for="option3">Option 3</form:label>
																					<div class="input-group">
																						<span class="input-group-addon"> <c:choose>
																								<c:when
																									test="${testQuestionVar.type eq 'SINGLESELECT'}">
																									<form:radiobutton
																										path="testQuestions[${status.index}].correctOption"
																										value="3" />
																								</c:when>
																								<c:otherwise>
																									<form:checkbox
																										path="testQuestions[${status.index}].correctOptions"
																										value="3" />
																								</c:otherwise>
																							</c:choose>
																						</span>
																						<form:input id="option3" readonly="${disabled}"
																							path="testQuestions[${status.index}].option3"
																							type="textarea" placeholder="Option 3"
																							class="form-control"
																							value="${testQuestionVar.option3}" />
																					</div>
																				</div>
																			</div>
																			<div class="col-sm-12">
																				<div class="form-group">
																					<form:label
																						path="testQuestions[${status.index}].option4"
																						for="option4">Option 4</form:label>
																					<div class="input-group">
																						<span class="input-group-addon"> <c:choose>
																								<c:when
																									test="${testQuestionVar.type eq 'SINGLESELECT'}">
																									<form:radiobutton
																										path="testQuestions[${status.index}].correctOption"
																										value="4" />
																								</c:when>
																								<c:otherwise>
																									<form:checkbox
																										path="testQuestions[${status.index}].correctOptions"
																										value="4" />
																								</c:otherwise>
																							</c:choose>
																						</span>
																						<form:input id="option4" readonly="${disabled}"
																							path="testQuestions[${status.index}].option4"
																							type="textarea" placeholder="Option 4"
																							class="form-control"
																							value="${testQuestionVar.option4}" />
																					</div>
																				</div>
																			</div>
																			<div class="col-sm-12">
																				<div class="form-group">
																					<form:label
																						path="testQuestions[${status.index}].option5"
																						for="option5">Option 5</form:label>
																					<div class="input-group">
																						<span class="input-group-addon"> <c:choose>
																								<c:when
																									test="${testQuestionVar.type eq 'SINGLESELECT'}">
																									<form:radiobutton
																										path="testQuestions[${status.index}].correctOption"
																										value="5" />
																								</c:when>
																								<c:otherwise>
																									<form:checkbox
																										path="testQuestions[${status.index}].correctOptions"
																										value="5" />
																								</c:otherwise>
																							</c:choose>
																						</span>
																						<form:input id="option5" readonly="${disabled}"
																							path="testQuestions[${status.index}].option5"
																							type="textarea" placeholder="Option 5"
																							class="form-control"
																							value="${testQuestionVar.option5}" />
																					</div>
																				</div>
																			</div>
																			<div class="col-sm-12">
																				<div class="form-group">
																					<form:label
																						path="testQuestions[${status.index}].option6"
																						for="option6">Option 6</form:label>
																					<div class="input-group">
																						<span class="input-group-addon"> <c:choose>
																								<c:when
																									test="${testQuestionVar.type eq 'SINGLESELECT'}">
																									<form:radiobutton
																										path="testQuestions[${status.index}].correctOption"
																										value="6" />
																								</c:when>
																								<c:otherwise>
																									<form:checkbox
																										path="testQuestions[${status.index}].correctOptions"
																										value="6" />
																								</c:otherwise>
																							</c:choose>
																						</span>
																						<form:input id="option6" readonly="${disabled}"
																							path="testQuestions[${status.index}].option6"
																							type="textarea" placeholder="Option 6"
																							class="form-control"
																							value="${testQuestionVar.option6}" />
																					</div>
																				</div>
																			</div>
																			<div class="col-sm-12">
																				<div class="form-group">
																					<form:label
																						path="testQuestions[${status.index}].option7"
																						for="option7">Option 7</form:label>
																					<div class="input-group">
																						<span class="input-group-addon"> <c:choose>
																								<c:when
																									test="${testQuestionVar.type eq 'SINGLESELECT'}">
																									<form:radiobutton
																										path="testQuestions[${status.index}].correctOption"
																										value="7" />
																								</c:when>
																								<c:otherwise>
																									<form:checkbox
																										path="testQuestions[${status.index}].correctOptions"
																										value="7" />
																								</c:otherwise>
																							</c:choose>
																						</span>
																						<form:input id="option7" readonly="${disabled}"
																							path="testQuestions[${status.index}].option7"
																							type="textarea" placeholder="Option 7"
																							class="form-control"
																							value="${testQuestionVar.option7}" />
																					</div>
																				</div>
																			</div>
																			<div class="col-sm-12">
																				<div class="form-group">
																					<form:label
																						path="testQuestions[${status.index}].option8"
																						for="option8">Option 8</form:label>
																					<div class="input-group">
																						<span class="input-group-addon"> <c:choose>
																								<c:when
																									test="${testQuestionVar.type eq 'SINGLESELECT'}">
																									<form:radiobutton
																										path="testQuestions[${status.index}].correctOption"
																										value="8" />
																								</c:when>
																								<c:otherwise>
																									<form:checkbox
																										path="testQuestions[${status.index}].correctOptions"
																										value="8" />
																								</c:otherwise>
																							</c:choose>
																						</span>
																						<form:input id="option8" readonly="${disabled}"
																							path="testQuestions[${status.index}].option8"
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
																					path="testQuestions[${status.index}].answerRangeFrom"
																					for="answerRangeFrom">Answer-Range From</form:label>
																				<form:input id="answerRangeFrom"
																					path="testQuestions[${status.index}].answerRangeFrom"
																					type="text" placeholder="Answer-Range From"
																					class="form-control"
																					value="${testQuestionVar.answerRangeFrom}" />
																			</div>
																		</div>

																		<div class="col-sm-6">
																			<div class="form-group">
																				<form:label
																					path="testQuestions[${status.index}].answerRangeTo"
																					for="answerRangeTo">Answer-Range To</form:label>
																				<form:input id="answerRangeTo"
																					path="testQuestions[${status.index}].answerRangeTo"
																					type="text" placeholder="Answer-Range To"
																					class="form-control"
																					value="${testQuestionVar.answerRangeTo}" />
																			</div>
																		</div>

																		<div class="col-sm-6">
																			<div class="form-group">
																				<form:label
																					path="testQuestions[${status.index}].correctOption"
																					for="correctOption">Correct Option</form:label>
																				<form:input id="correctOption"
																					path="testQuestions[${status.index}].correctOption"
																					type="text" placeholder="Correct-Option"
																					class="form-control"
																					value="${testQuestionVar.correctOption}" />
																			</div>
																		</div>
																	</c:if>
																	<div class="col-sm-12 column">
																		<div class="form-group">

																			<button id="submit" class="btn btn-large btn-primary"
																				formaction="updateTestQuestion">Update
																				Question</button>
																		</div>
																	</div>
																</form:form>

															</div>
														</div>
													</div>
												</c:forEach>
											</div>
										</c:if>


									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Results Panel -->


					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h5>Add New Questions (*Choose at least one correct
											option)</h5>
										
									</div>
									<div class="imageUrl">
									<button id="genLink" class="btn btn-modalSub mb-2 mt-2" name="genLink" 
											 data-toggle="modal" data-target="#genLinkModal">
										Generate Image Url</button>
									</div>
									<div class="x_content">
										<c:if test="${not empty test}">


											<form:form action="addTestQuestion" method="post"
												modelAttribute="testQuestion" id="addTestQuestionForm">
												<div class="col-sm-12">
													<form:input type="hidden" path="testId" value="${test.id}" />
													<div class="form-group">
														<form:label path="description" for="description">Description</form:label>
														<%-- <form:input id="description" path="description"
																type="textarea" required="required"
																placeholder="Description" class="form-control" /> --%>
														<form:textarea class="form-group ckeditorClass"
															path="description" name="description" id="description"
															rows="10" cols="80" />
													</div>
												</div>
												<div class="col-sm-6">
													<div class="form-group">
														<form:label path="marks" for="marks">Marks <span
																style="color: red">*</span>
														</form:label>
														<%-- <form:input id="marks" path="marks" type="number"
																	required="required" placeholder="Marks"
																	class="form-control" /> --%>

														<c:if test="${randomQuestionStatus eq 'N' && (empty sameMarksQue || sameMarksQue eq 'N')}">
															<form:input id="marks" path="marks" type="number"
																required="required" placeholder="Marks"
																class="form-control" step=".00001" />
														</c:if>

														<c:if
															test="${randomQuestionStatus eq 'Y' && (empty sameMarksQue || sameMarksQue eq 'N')}">
															<form:select id="marks" path="marks" class="form-control">
																<c:forEach var="testConfig" items="${testConfigList}"
																	varStatus="status">

																	<option value="${testConfig.marks}">${testConfig.marks}</option>

																</c:forEach>
															</form:select>
														</c:if>

														<c:if
															test="${sameMarksQue eq 'Y'}">
															<form:input id="marks" path="marks" type="number"
																readonly="true" required="required" placeholder="Marks"
																class="form-control" value="${ marksPerQue }" />
														</c:if>
													</div>
												</div>

												<div class="col-sm-6">
													<div class="form-group">
														<label for="questionType">Question Type</label> <select
															id="questionType" name="questionType"
															class="form-control questionTypeSelection">

															<option value="">Question-Type</option>

															<option value="MCQ">MCQ</option>
															<option value="Numeric">Numeric</option>
															<option value="Image">MCQ+Image</option>


														</select>
													</div>
												</div>

												<div class="hiddenFieldNumeric">
													<div class="col-sm-6">
														<div class="form-group">
															<form:label path="answerRangeFrom" for="answerRangeFrom">Answer-Range From</form:label>
															<form:input id="answerRangeFrom" path="answerRangeFrom"
																type="text" placeholder="Answer-Range From"
																class="form-control" />
														</div>
													</div>

													<div class="col-sm-6">
														<div class="form-group">
															<form:label path="answerRangeTo" for="answerRangeTo">Answer-Range To</form:label>
															<form:input id="answerRangeTo" path="answerRangeTo"
																type="text" placeholder="Answer-Range To"
																class="form-control" />
														</div>
													</div>

													<div class="col-sm-6">
														<div class="form-group">
															<form:label path="correctAnswerNum" for="correctOption">Correct Answer</form:label>
															<form:input id="correctOption" path="correctAnswerNum"
																type="text" placeholder="Correct-Answer"
																class="form-control" />
														</div>
													</div>
												</div>

												<div class="col-sm-6 hiddenFieldMCQ">
													<div class="form-group">
														<label for="type">Selection Type</label>
														<div class="clearfix"></div>
														<label class="radio-inline"> <form:radiobutton
																path="type" value="SINGLESELECT"
																onclick="singleSelectClicked('addTestQuestionForm')" />
															Single Select
														</label> <label class="radio-inline"> <form:radiobutton
																path="type" value="MULTISELECT"
																onclick="multipleSelectClicked('addTestQuestionForm')" />
															Multiple Select
														</label>
													</div>
													<div class="form-group">
														<label for="optionShuffle">Option Shuffle
															Required?</label>
														<form:select id="optionShuffle" name="optionShuffle"
															class="form-control" path="optionShuffle">

															<option value="">Select</option>

															<option value="Y">Y</option>
															<option value="N">N</option>


														</form:select>
													</div>
												</div>

												<div class="testOptions hiddenFieldMCQ">
													<div class="col-sm-12">
														<div class="form-group">
															<form:label path="option1" for="option1">Option 1</form:label>
															<div class="input-group">
																<span class="input-group-addon"> <input
																	type="radio" name="correctOption" value="1">
																</span>
																<form:input id="option1" path="option1" type="textarea"
																	placeholder="Option 1" class="form-control" />
															</div>
														</div>
													</div>
													<div class="col-sm-12">
														<div class="form-group">
															<form:label path="option2" for="option2">Option 2</form:label>
															<div class="input-group">
																<span class="input-group-addon"> <input
																	type="radio" name="correctOption" value="2">
																</span>
																<form:input id="option2" path="option2" type="textarea"
																	placeholder="Option 2" class="form-control" />
															</div>
														</div>
													</div>
													<div class="col-sm-12">
														<div class="form-group">
															<form:label path="option3" for="option3">Option 3</form:label>
															<div class="input-group">
																<span class="input-group-addon"> <input
																	type="radio" name="correctOption" value="3">
																</span>
																<form:input id="option3" path="option3" type="textarea"
																	placeholder="Option 3" class="form-control" />
															</div>
														</div>
													</div>
													<div class="col-sm-12">
														<div class="form-group">
															<form:label path="option4" for="option4">Option 4</form:label>
															<div class="input-group">
																<span class="input-group-addon"> <input
																	type="radio" name="correctOption" value="4">
																</span>
																<form:input id="option4" path="option4" type="textarea"
																	placeholder="Option 4" class="form-control" />
															</div>
														</div>
													</div>
													<div class="col-sm-12">
														<div class="form-group">
															<form:label path="option5" for="option5">Option 5</form:label>
															<div class="input-group">
																<span class="input-group-addon"> <input
																	type="radio" name="correctOption" value="5">
																</span>
																<form:input id="option5" path="option5" type="textarea"
																	placeholder="Option 5" class="form-control" />
															</div>
														</div>
													</div>
													<div class="col-sm-12">
														<div class="form-group">
															<form:label path="option6" for="option6">Option 6</form:label>
															<div class="input-group">
																<span class="input-group-addon"> <input
																	type="radio" name="correctOption" value="6">
																</span>
																<form:input id="option6" path="option6" type="textarea"
																	placeholder="Option 6" class="form-control" />
															</div>
														</div>
													</div>
													<div class="col-sm-12">
														<div class="form-group">
															<form:label path="option7" for="option7">Option 7</form:label>
															<div class="input-group">
																<span class="input-group-addon"> <input
																	type="radio" name="correctOption" value="7">
																</span>
																<form:input id="option7" path="option7" type="textarea"
																	placeholder="Option 7" class="form-control" />
															</div>
														</div>
													</div>
													<div class="col-sm-12">
														<div class="form-group">
															<form:label path="option8" for="option8">Option 8</form:label>
															<div class="input-group">
																<span class="input-group-addon"> <input
																	type="radio" name="correctOption" value="8">
																</span>
																<form:input id="option8" path="option8" type="textarea"
																	placeholder="Option 8" class="form-control" />
															</div>
														</div>
													</div>
												</div>
												<div class="col-sm-12 column">
													<div class="form-group">

														<button id="submit" class="btn btn-large btn-primary"
															formaction="addTestQuestion">Add Question</button>
														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>
											</form:form>


										</c:if>


									</div>
								</div>
							</div>
						</div>
					</div>
<div id="modalGenLinkSub">
<div class="modal fade fnt-13"
	id="genLinkModal" tabindex="-1" role="dialog"
	aria-labelledby="genLinkTitle" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
		role="document">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="genLinkTitle">Generate Link</h5>
				<button type="button" class="close" data-dismiss="modal"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
			<form:form action="uploadImageFileAndGenerateLink" id="uploadImageFileGenLink"
					method="post" enctype="multipart/form-data" >
			<div class="modal-body">
					<div class="row">
						<div class="col-12">
							<h6 for="uploadImageFile">Upload Image file</h6>
							<input type="file" name="file" class="form-control-file uploadImageFile mb-1" id="uploadImageFile" accept="image/jpeg,image/png" data-max-size="1000000">
							<p><Strong>Note:</Strong> Image file size should be less than 1 MB</p>
							<span id="result"></span>
						</div>
						
					</div>
					<div class="row">
						<div class="col-12 mt-3" id="subDisplay"></div>
					</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-modalClose"
					data-dismiss="modal">Close</button>
				<button id="submitGenLink" class="btn btn-modalSub" name="subGenLink" 
						formaction="uploadImageFileAndGenerateLink">Submit</button>
			</div>
			</form:form>
		</div>
	</div>
</div>
</div>

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
<%-- 				<jsp:include page="../common/footer.jsp" /> --%>
				<!-- <script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script> -->


<!-- FOOTER -->
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
<%--   </c:if> --%>






<!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script> -->
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
  <script type="text/javascript" src="<c:url value="/resources/js/custom-timecircles.js" />" ></script> --%>
<script type="text/javascript"
	src="<c:url value="/resources/js/Chart.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/timecircles.js" />"></script>
	<script src="<c:url value="/resources/ckeditor/ckeditor.js" />"></script>  
<script>
	var myContextPath = "${pageContext.request.contextPath}"
</script>

<script type="text/javascript"
	src="<c:url value="/resources/js/style.js" />"></script>
	
	


<!-- ==================================================================== -->

 
 
 <script>
	$(".ckeditorClass")
			.each(
					function() {
						console.log("id--->" + ($(this).attr('id')));

						CKEDITOR
								.replace(
										$(this).attr('id')
									);

					});
</script>


<script>
	$(document).ready(function() {

		$(".hiddenFieldMCQ").hide();
		$(".hiddenFieldNumeric").hide();
	});

	$("select.questionTypeSelection").change(function() {

		if ($(this).val() == "MCQ" || $(this).val() == 'Image') {
			$(".hiddenFieldNumeric").hide();
			$(".hiddenFieldMCQ").show();
		} else if ($(this).val() == "Numeric") {
			$(".hiddenFieldMCQ").hide();
			$(".hiddenFieldNumeric").show();
		} else {
			$(".hiddenFieldMCQ").hide();
			$(".hiddenFieldNumeric").hide();
		}
	})
	
	
	
	$("#submitGenLink").click(function (event) {
 var fileInput = $('.uploadImageFile');
 var maxSize = fileInput.data('max-size');
 console.log(maxSize)
 if(fileInput.get(0).files.length){
	  var fileSize = fileInput.get(0).files[0].size;
	  console.log(fileSize)
		if(fileSize>maxSize){
			alert('file size is more then 1 MB');
			event.preventDefault();
		}else{
			//stop submit the form, we will post it manually.
			event.preventDefault();
			// Get form
			var form = $('#uploadImageFileGenLink')[0];

			// Create an FormData object
			var data = new FormData(form);
			
			$.ajax({
				type: "POST",
				enctype: 'multipart/form-data',
				url: "${pageContext.request.contextPath}/api/uploadImageFileAndGenerateLink",
				data: data,
				processData: false,
				contentType: false,
				cache: false,
				timeout: 600000,
				success: function (data) {

					//$("#result").text(""+"${pageContext.request.contextPath}"+data);
					$('#result').html('<p><Strong>Image Url:</Strong> '+data+'</p>')
					console.log("SUCCESS : ", data);
				},
				error: function (e) {
					$("#result").text(e.responseText);
					console.log("ERROR : ", e);
				}
			});
		}
		console.log("file")
	}else{
		alert('choose file, please');
		return false;
	}
});
</script>	
	
	

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



	/*CALLING DATE PICKER*/
	$(function() {

		$('#testDateRangeBtn').daterangepicker({

			"showDropdowns" : true,
			"timePicker" : true,
			"showCustomRangeLabel" : false,
			"alwaysShowCalendars" : true,
			"opens" : "center",
		    "minDate": new Date()
		},

		function(start, end, label) {
			var sDate = start.format('YYYY-MM-DD HH:mm:ss');
			var eDate = end.format('YYYY-MM-DD HH:mm:ss');
			$('#startDate').val(sDate + '-' + eDate);
			$('#testStartDate').val(sDate);
			$('#testEndDate').val(eDate);

		});



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

</script>




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
                    
            window.multipleSelectClicked = function multipleSelectClicked(formId) {
                $('#'+formId+" .testOptions").find('input[type="radio"]').each(function(index, element) {
                      var elem = $(element);
                      elem.replaceWith(
                      $("<input>",{
                          type:'checkbox',
                          name: elem.attr('name'),
                          value: elem.val()
                      }));
                });
          }
            
      });
</script>
<!-- End newUserAnnouncementList -->










</body>

</html>




