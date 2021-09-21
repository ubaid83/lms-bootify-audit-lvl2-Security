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
								Test Question Pool</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>${testPool.testPoolName}</h5>


									</div>

									<div class="x_content">
										<c:if test="${not empty testPool.testQuestionPools}">

											<div class="panel-group" id="testQuestionAccordion"
												role="tablist" aria-multiselectable="true">
												<c:forEach items="${testPool.testQuestionPools}"
													var="testQuestionVar" varStatus="status">
													<div class="panel panel-default">
														<div class="panel-heading" role="tab"
															id="heading-${status.index}">

															<%-- <a data-toggle="collapse"
																	data-parent="#testQuestionAccordion"
																	href="#collapse-${status.index}" aria-expanded="false"
																	aria-controls="collapse-${status.index}"><strong>Q${status.index+1}.
																		${testQuestionVar.description} </strong></a> (Marks :
																${testQuestionVar.marks})
																${testQuestionVar.testType}/${testQuestionVar.questionType} --%>
															<div class="p-3 border cursor-pointer" data-toggle="collapse"
																data-parent="#testQuestionAccordion"
																data-target="#collapse-${status.index}"
																aria-expanded="false"
																aria-controls="collapse-${status.index}">
																<strong>Q${status.index+1}. <%-- ${testQuestionVar.description} --%>
																</strong>(Marks : ${testQuestionVar.marks})
																${testQuestionVar.testType}
																/${testQuestionVar.questionType }
															</div>

														</div>
														<div id="collapse-${status.index}"
															class="panel-collapse collapse" role="tabpanel"
															aria-labelledby="heading-${status.index}">
															<div class="panel-body">
																<div class="imageUrl">	
																	<button id="genLink" class="btn btn-modalSub mb-2 mt-2" name="genLink" 
																		 data-toggle="modal" data-target="#genLinkModal">
																	Generate Image Url</button>
																</div>
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
																		
																		<div class="form-group mt-3">
																		
																			<form:label
																				path="testQuestionPools[${status.index}]."
																				for="description">Description</form:label>
																			<%-- <form:input
																					path="testQuestionPools[${status.index}].description"
																					type="textarea" required="required"
																					placeholder="Description"
																					class="form-control ckeditorClass"
																					value="${testQuestionVar.description}" /> --%>

																			<form:textarea class="form-group testDesc ckeditorClass"
																				path="testQuestionPools[${status.index}].description"
																				name="description" id="editor${status.index}"
																				rows="10" cols="80" />
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
																				type="number" required="required" step=".00001"
																				placeholder="Marks" class="form-control"
																				value="${testQuestionVar.marks}" />
																		</div>
																	</div>
																	<div class="col-sm-6">
																		<div class="form-group">
																			<form:label
																				path="testQuestionPools[${status.index}].testType"
																				for="testType">Question Type :  ${testQuestionVar.testType}/${testQuestionVar.questionType}</form:label>
																			<!-- <select
																					id="testType" name="testType" class="form-control">

																					<option value="">Select Type</option>

																					<option value="Objective">Objective</option>
																					<option value="Subjective">Subjective</option>


																				</select> -->
																		</div>
																	</div>
																	<c:if test="${testQuestionVar.testType eq 'Objective'}">

																		<c:choose>
																			<c:when
																				test="${testQuestionVar.questionType eq 'Numeric'}">
																				<div class="col-sm-6">
																					<div class="form-group">
																						<form:label
																							path="testQuestionPools[${status.index}].answerRangeFrom"
																							for="answerRangeFrom">Answer-Range From</form:label>
																						<form:input id="answerRangeFrom${status.index}"
																							path="testQuestionPools[${status.index}].answerRangeFrom"
																							type="number" step=".00001"
																							placeholder="Answer-Range From"
																							class="form-control"
																							value="${testQuestionVar.answerRangeFrom}" />
																					</div>
																				</div>

																				<div class="col-sm-6">
																					<div class="form-group">
																						<form:label
																							path="testQuestionPools[${status.index}].answerRangeTo"
																							for="answerRangeTo">Answer-Range To</form:label>
																						<form:input id="answerRangeTo${status.index}"
																							path="testQuestionPools[${status.index}].answerRangeTo"
																							type="number" placeholder="Answer-Range To"
																							class="form-control" step=".00001"
																							value="${testQuestionVar.answerRangeTo}" />
																					</div>
																				</div>

																				<div class="col-sm-6">
																					<div class="form-group">
																						<form:label
																							path="testQuestionPools[${status.index}].correctOption"
																							for="correctOption">Correct Option</form:label>
																						<form:input id="correctOption${status.index}"
																							path="testQuestionPools[${status.index}].correctOption"
																							type="number" placeholder="Correct-Option"
																							class="form-control" step=".00001"
																							value="${testQuestionVar.correctOption}" />
																					</div>
																				</div>
																			</c:when>
																			<c:otherwise>
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
																						<form:select id="optionShuffle${status.index}"
																							name="optionShuffle" class="form-control"
																							path="testQuestionPools[${status.index}].optionShuffle">

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
																									class="form-control mt-3"
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
																			</c:otherwise>
																		</c:choose>
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
																					value="configureImageTestQuestionPoolForm" />
																			</c:url>



																			<button id="submit" class="btn btn-large btn-primary"
																				formaction="updateTestImageQuestionPool">Update
																				Question</button>
																			<button id="delete" class="btn btn-large btn-primary"
																				formaction="${deleteTestQuestionPool}">Delete
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
										<c:if test="${not empty testPool}">


											<form:form action="addTestQuestionPool" method="post"
												modelAttribute="testQuestionPools"
												id="configureImageTestQuestionPoolForm">
												<div class="col-sm-12">
													<form:input type="hidden" path="testPoolId"
														value="${testPool.id}" />
													<div class="form-group">
														<form:label path="description" for="description">Description</form:label>
														<%-- <form:input path="description" type="textarea"
																required="required" placeholder="Description"
																class="form-control ckeditorClass" /> --%>

														<form:textarea class="form-group testDesc ckeditorClass"
															path="description" name="description" id="description${status.index}"
															rows="10" cols="80" />

													</div>
												</div>
												<div class="col-sm-6">
													<div class="form-group">
														<form:label path="marks" for="marks">Marks <span
																style="color: red">*</span>
														</form:label>
														<form:input id="marks" path="marks" type="number"
															step=".00001" required="required" placeholder="Marks"
															class="form-control" />
													</div>
												</div>
												<div class="col-sm-6">
													<div class="form-group">
														<label for="testType">Question For <span
															style="color: red">*</span></label> <select id="testType"
															name="testType" class="form-control testType"
															required="required">

															<option value="">Select Question For</option>

															<option value="Subjective">Subjective</option>
															<option value="Objective">Objective</option>


														</select>
													</div>
												</div>


												<div class="col-sm-6 hiddenFieldObjective">
													<div class="form-group">
														<label for="questionType">Question Type For
															Objective <span style="color: red">*</span>
														</label> <select id="questionType" name="questionType"
															class="form-control questnTypeOption">

															<option value="">Select Question Type</option>

															<option value="MCQ">MCQ</option>
															<option value="Numeric">Numeric</option>


														</select>
													</div>
												</div>



												<div class="hiddenFieldNumeric">
													<div class="col-sm-6">
														<div class="form-group">
															<form:label path="answerRangeFrom" for="answerRangeFrom">Answer-Range From <span
																	style="color: red">*</span>
															</form:label>
															<form:input id="answerRangeFrom" path="answerRangeFrom"
																type="number" step=".00001"
																placeholder="Answer-Range From" class="form-control" />
														</div>
													</div>

													<div class="col-sm-6">
														<div class="form-group">
															<form:label path="answerRangeTo" for="answerRangeTo">Answer-Range To <span
																	style="color: red">*</span>
															</form:label>
															<form:input id="answerRangeTo" path="answerRangeTo"
																type="number" step=".00001"
																placeholder="Answer-Range To" class="form-control" />
														</div>
													</div>

													<div class="col-sm-6">
														<div class="form-group">
															<form:label path="correctAnswerNum" for="correctOption">Correct Answer <span
																	style="color: red">*</span>
															</form:label>
															<form:input id="correctOption" path="correctAnswerNum"
																type="number" step=".00001" placeholder="Correct-Answer"
																class="form-control" />
														</div>
													</div>
												</div>


												<div class="hiddenFieldMCQ">
													<div class="col-sm-6">
														<div class="form-group ">
															<label for="type">Selection Type</label>
															<div class="clearfix"></div>
															<label class="radio-inline"> <form:radiobutton
																	path="type" value="SINGLESELECT"
																	onclick="singleSelectClicked1('configureImageTestQuestionPoolForm')" />
																Single Select
															</label> <label class="radio-inline"> <form:radiobutton
																	path="type" value="MULTISELECT"
																	onclick="multipleSelectClicked1('configureImageTestQuestionPoolForm')" />
																Multiple Select
															</label>
														</div>
													</div>


													<div class="col-sm-6">
														<div class="form-group">
															<label for="optionShuffle">Option Shuffle
																Required? <span style="color: red">*</span>
															</label> <select id="optionShuffle" name="optionShuffle"
																class="form-control">

																<option value="">Select</option>

																<option value="Y">Y</option>
																<option value="N">N</option>


															</select>
														</div>
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
															formaction="addTestQuestionPool">Add Question</button>
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


					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

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

				<!-- SIDEBAR START -->
				<sec:authorize access="hasRole('ROLE_FACULTY')">
					<jsp:include page="../common/newSidebar.jsp" />
				</sec:authorize>
				<!-- SIDEBAR END -->
				<sec:authorize access="hasRole('ROLE_FACULTY')">
					<jsp:include page="../common/footer.jsp" />
				</sec:authorize>
				<sec:authorize access="hasRole('ROLE_ADMIN')">
					<jsp:include page="../common/newAdminFooter.jsp" />
				</sec:authorize>
				<script type="text/javascript"
					src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script>
				<script>
					$(document).ready(function() {

						$(".hiddenFieldObjective").hide();
						$(".hiddenFieldNumeric").hide();
						$(".hiddenFieldMCQ").hide();
					});

					$("select.testType").change(function() {

						if ($(this).val() == "Objective") {

							$(".hiddenFieldObjective").show();
							$(".hiddenFieldMCQ").hide();
							$(".hiddenFieldNumeric").hide();
							$("#questionType").prop('required', true);

						} else if ($(this).val() == "Subjective") {
							$(".hiddenFieldObjective").hide();
							$("#questionType").prop('required', false);
							$(".hiddenFieldMCQ").hide();
							$(".hiddenFieldNumeric").hide();
						} else {
							$(".hiddenFieldObjective").hide();
							$(".hiddenFieldMCQ").hide();
							$(".hiddenFieldNumeric").hide();
							$("#questionType").prop('required', true);
						}
					});

					$("select.questnTypeOption").change(function() {
						if ($(this).val() == 'MCQ') {
							$(".hiddenFieldMCQ").show();
							$(".hiddenFieldNumeric").hide();
							$("#answerRangeFrom").prop('required', false);
							$("#answerRangeTo").prop('required', false);
							$("#correctOption").prop('required', false);
							$("#optionShuffle").prop('required', true);
						} else if ($(this).val() == 'Numeric') {
							$(".hiddenFieldMCQ").hide();
							$(".hiddenFieldNumeric").show();
							$("#answerRangeFrom").prop('required', true);
							$("#answerRangeTo").prop('required', true);
							$("#correctOption").prop('required', true);
							$("#optionShuffle").prop('required', false);
						} else {
							$(".hiddenFieldMCQ").hide();
							$(".hiddenFieldNumeric").hide();
							$("#answerRangeFrom").prop('required', false);
							$("#answerRangeTo").prop('required', false);
							$("#correctOption").prop('required', false);
							$("#optionShuffle").prop('required', false);
						}
					});
					
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