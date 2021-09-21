<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>

			<jsp:include page="../common/topHeader.jsp" />
			


			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a> <i class="fa fa-angle-right"></i>
							Student Feedback
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>${feedbackName}</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>


									<div class="x_content">
										<div class="modal-dialog">
											<c:forEach items="${feedback.feedbackQuestions}"
												var="feedbackQuestion" varStatus="status">
												<form:form action="addStudentFeedbackResponse" method="post"
													modelAttribute="feedback"
													id="studentFeedbackForm-${status.index}">
													<div class="modal-content" id="modal-${status.index}">

														<div class="modal-header">
															<h3>
																<span class="label label-warning" id="qid">${status.index + 1}</span>
																${feedbackQuestion.description} <br /> <small><c:choose>
																		<c:when
																			test="${feedbackQuestion.type eq 'SINGLESELECT'}"> Select One
										</c:when>
																		<c:otherwise>Select Multiple</c:otherwise>
																	</c:choose></small>
															</h3>
														</div>

														<c:set var="ans" value="${feedbackQuestion.answer}" />


														<div class="modal-body">
															<div class="quiz" data-toggle="buttons">
																<form:input type="hidden"
																	path="feedbackQuestions[${status.index}].studentFeedbackResponse.studentFeedbackId"
																	value="${feedback.studentFeedback.id}" />
																<form:input type="hidden"
																	path="feedbackQuestions[${status.index}].studentFeedbackResponse.feedbackQuestionId"
																	value="${feedbackQuestion.id}" />

																<c:if test="${not empty feedbackQuestion.option1}">


																	<c:choose>
																		<c:when test="${fn:contains(ans, '1')}">
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block active"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:when>
																		<c:otherwise>
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:otherwise>

																	</c:choose>

																	<c:choose>
																		<c:when
																			test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																			<form:radiobutton
																				path="feedbackQuestions[${status.index}].answer"
																				value="1" />
																		</c:when>
																		<c:otherwise>
																			<form:checkbox
																				path="feedbackQuestions[${status.index}].answers"
																				value="1" />
																		</c:otherwise>
																	</c:choose>${feedbackQuestion.option1}</label>
																</c:if>
																<c:if test="${not empty feedbackQuestion.option2}">
																	<c:choose>
																		<c:when test="${fn:contains(ans, '2')}">
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block active"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:when>
																		<c:otherwise>
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:otherwise>

																	</c:choose>
																	<c:choose>
																		<c:when
																			test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																			<form:radiobutton
																				path="feedbackQuestions[${status.index}].answer"
																				value="2" />
																		</c:when>
																		<c:otherwise>
																			<form:checkbox
																				path="feedbackQuestions[${status.index}].answers"
																				value="2" />
																		</c:otherwise>
																	</c:choose>${feedbackQuestion.option2}</label>
																</c:if>




																<c:if test="${not empty feedbackQuestion.option3}">
																	<c:choose>
																		<c:when test="${fn:contains(ans, '3')}">
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block active"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:when>
																		<c:otherwise>
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:otherwise>

																	</c:choose>

																	<c:choose>
																		<c:when
																			test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																			<form:radiobutton
																				path="feedbackQuestions[${status.index}].answer"
																				value="3" />
																		</c:when>
																		<c:otherwise>
																			<form:checkbox
																				path="feedbackQuestions[${status.index}].answers"
																				value="3" />
																		</c:otherwise>
																	</c:choose>${feedbackQuestion.option3}</label>
																</c:if>
																<c:if test="${not empty feedbackQuestion.option4}">
																	<c:choose>
																		<c:when test="${fn:contains(ans, '4')}">
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block active"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:when>
																		<c:otherwise>
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:otherwise>

																	</c:choose>

																	<c:choose>
																		<c:when
																			test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																			<form:radiobutton
																				path="feedbackQuestions[${status.index}].answer"
																				value="4" />
																		</c:when>
																		<c:otherwise>
																			<form:checkbox
																				path="feedbackQuestions[${status.index}].answers"
																				value="4" />
																		</c:otherwise>
																	</c:choose>${feedbackQuestion.option4}</label>
																</c:if>
																<c:if test="${not empty feedbackQuestion.option5}">
																	<c:choose>
																		<c:when test="${fn:contains(ans, '5')}">
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block active"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:when>
																		<c:otherwise>
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:otherwise>

																	</c:choose>
																	<c:choose>
																		<c:when
																			test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																			<form:radiobutton
																				path="feedbackQuestions[${status.index}].answer"
																				value="5" />
																		</c:when>
																		<c:otherwise>
																			<form:checkbox
																				path="feedbackQuestions[${status.index}].answers"
																				value="5" />
																		</c:otherwise>
																	</c:choose>${feedbackQuestion.option5}</label>
																</c:if>
																<c:if test="${not empty feedbackQuestion.option6}">
																	<c:choose>
																		<c:when test="${fn:contains(ans, '6')}">
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block active"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:when>
																		<c:otherwise>
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:otherwise>

																	</c:choose>

																	<c:choose>
																		<c:when
																			test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																			<form:radiobutton
																				path="feedbackQuestions[${status.index}].answer"
																				value="6" />
																		</c:when>
																		<c:otherwise>
																			<form:checkbox
																				path="feedbackQuestions[${status.index}].answers"
																				value="6" />
																		</c:otherwise>
																	</c:choose>${feedbackQuestion.option6}</label>
																</c:if>
																<c:if test="${not empty feedbackQuestion.option7}">
																	<c:choose>
																		<c:when test="${fn:contains(ans, '7')}">
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block active"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:when>
																		<c:otherwise>
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:otherwise>

																	</c:choose>
																	<c:choose>
																		<c:when
																			test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																			<form:radiobutton
																				path="feedbackQuestions[${status.index}].answer"
																				value="7" />
																		</c:when>
																		<c:otherwise>
																			<form:checkbox
																				path="feedbackQuestions[${status.index}].answers"
																				value="7" />
																		</c:otherwise>
																	</c:choose>${feedbackQuestion.option7}</label>
																</c:if>
																<c:if test="${not empty feedbackQuestion.option8}">
																	<c:choose>
																		<c:when test="${fn:contains(ans, '8')}">
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block active"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:when>
																		<c:otherwise>
																			<label
																				class="element-animation element1 btn btn-lg btn-primary btn-block"><span
																				class="btn-label"> <i
																					class="glyphicon glyphicon-chevron-right"></i>
																			</span>
																		</c:otherwise>

																	</c:choose>
																	<c:choose>
																		<c:when
																			test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																			<form:radiobutton
																				path="feedbackQuestions[${status.index}].answer"
																				value="8" />
																		</c:when>
																		<c:otherwise>
																			<form:checkbox
																				path="feedbackQuestions[${status.index}].answers"
																				value="8" />
																		</c:otherwise>
																	</c:choose>${feedbackQuestion.option8}</label>
																</c:if>
															</div>
														</div>
														<div class="modal-footer">
															<c:if test="${not status.first}">
																<a class="btn btn-large btn-primary"
																	onclick="showModal('modal-${status.index }','modal-${status.index - 1}','nav-${status.index - 1}')">Previous</a>
															</c:if>

															<c:choose>
																<c:when test="${status.last}">
																	<c:if test="${not feedback.studentFeedback.completed}">
																		<a class="btn btn-large btn-primary"
																			onclick="submitForm('studentFeedbackForm-${status.index}','nav-${status.index}',true)">Save
																			and Complete</a>
																	</c:if>
																</c:when>
																<c:otherwise>
																	<c:if test="${not feedback.studentFeedback.completed}">
																		<a class="btn btn-large btn-primary"
																			onclick="submitForm('studentFeedbackForm-${status.index}','nav-${status.index}');showModal('modal-${status.index }','modal-${status.index + 1}','nav-${status.index + 1}')">Save
																			and Next</a>
																	</c:if>
																	<a class="btn btn-large btn-primary"
																		onclick="showModal('modal-${status.index }','modal-${status.index + 1}','nav-${status.index + 1}')">Skip</a>
																</c:otherwise>
															</c:choose>
														</div>
													</div>
												</form:form>
											</c:forEach>
										</div>
									</div>
								</div>
							</div>



						</div>



					</div>

				</div>
				<!-- /page content: END -->


				<jsp:include page="../common/footer.jsp" />

			</div>
		</div>
</body>
</html>
