
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<!doctype html>
<html lang="en">

<jsp:include page="../common/headerCss.jsp" />

<body class="nav-md ">


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/NewHeader1.jsp" />





			<!-- page content -->
			<div class="right_col" role="main">

				<div class="dashboard_contain">

					<div class="dashboard_height" id="main">

						<div class="dashboard_contain_specing">
							<div class="breadcrumb">
								<a href="${pageContext.request.contextPath}/viewCourse?id=${courseId}">My Courses</a> <i
									class="fa fa-angle-right"></i> <a href="my-courses.html">Business
									Law</a> <i class="fa fa-angle-right"></i> Feedback
							</div>

							<div class="row">
								<div class="col-xs-12 col-sm-12">
									<div class="x_panel">
										<div class="x_title">
											<h2>${feedbackName}</h2>
											<div class="clearfix"></div>
										</div>
										<div class="x_content">

											<c:forEach items="${feedback.feedbackQuestions}"
												var="feedbackQuestion" varStatus="status">
												<form:form action="addStudentFeedbackResponse" method="post"
													modelAttribute="feedback"
													id="studentFeedbackForm-${status.index}">
													<div class="table_overflow feedback_provide_contain"
														id="feedBackTable-${status.index}">
														<table class="table">
															<thead>
																<tr>
																	<td>${feedbackQuestion.description}</td>
																</tr>
															</thead>
															<tr>
																<td><form:input type="hidden"
																		path="feedbackQuestions[${status.index}].studentFeedbackResponse.studentFeedbackId"
																		value="${feedback.studentFeedback.id}" /> <form:input
																		type="hidden"
																		path="feedbackQuestions[${status.index}].studentFeedbackResponse.feedbackQuestionId"
																		value="${feedbackQuestion.id}" />

																	<div class="feedback_provide_contain">
																		<p>
																			<c:choose>
																				<c:when
																					test="${feedbackQuestion.type eq 'SINGLESELECT'}"> Select One
																				</c:when>
																				<c:otherwise>Select Multiple</c:otherwise>
																			</c:choose>
																		</p>
																		<div>
																			<c:if test="${not empty feedbackQuestion.option1}">

																				<c:choose>
																					<c:when test="${fn:contains(feedbackQuestion.studentFeedbackResponse.answer, '1')}">
																						<label class="active1" id="label${status.count}1"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:when>
																					<c:otherwise>
																						<label class="" id="label${status.count}1"><i
																							class="check_ellipse fa fa-check "> </i>
																					</c:otherwise>

																				</c:choose>

																				<c:choose>
																					<c:when
																						test="${feedbackQuestion.type eq 'SINGLESELECT'}">

																						<form:radiobutton class=""
																							path="feedbackQuestions[${status.index}].answer"
																							value="1" id="check${status.count}1"
																							onclick="checkfield('check${status.count}1','label${status.count}1',true)" />
																					</c:when>
																					<c:otherwise>

																						<form:checkbox
																							path="feedbackQuestions[${status.index}].answers"
																							value="1" id="check${status.count}1"
																							onclick="checkfield('check${status.count}1','label${status.count}1',false)" />
																					</c:otherwise>
																				</c:choose>
											

																				
																				${feedbackQuestion.option1}
																				</label>

																			</c:if>

																			<c:if test="${not empty feedbackQuestion.option2}">
																				<c:choose>
																					<c:when test="${fn:contains(feedbackQuestion.studentFeedbackResponse.answer, '2')}">
																						<label class="active1" id="label${status.count}2"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:when>
																					<c:otherwise>
																						<label class="" id="label${status.count}2"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:otherwise>

																				</c:choose>

																				<c:choose>
																					<c:when
																						test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																						<form:radiobutton
																							path="feedbackQuestions[${status.index}].answer"
																							value="2" id="check${status.count}2"
																							class="checkClass"
																							onclick="checkfield('check${status.count}2','label${status.count}2',true)" />
																					</c:when>
																					<c:otherwise>
																						<form:checkbox
																							path="feedbackQuestions[${status.index}].answers"
																							value="2" id="check${status.count}2"
																							onclick="checkfield('check${status.count}2','label${status.count}2',false)" />
																					</c:otherwise>
																				</c:choose>
																				${feedbackQuestion.option2}
																				</label>

																			</c:if>

																			<c:if test="${not empty feedbackQuestion.option3}">
																				<c:choose>
																					<c:when test="${fn:contains(feedbackQuestion.studentFeedbackResponse.answer, '3')}">
																						<label class="active1" id="label${status.count}3"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:when>
																					<c:otherwise>
																						<label class=" " id="label${status.count}3">
																							<i class="check_ellipse fa fa-check"> </i>
																					</c:otherwise>

																				</c:choose>

																				<c:choose>
																					<c:when
																						test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																						<form:radiobutton
																							path="feedbackQuestions[${status.index}].answer"
																							value="3" id="check${status.count}3"
																							onclick="checkfield('check${status.count}3','label${status.count}3',true)" />
																					</c:when>
																					<c:otherwise>
																						<form:checkbox
																							path="feedbackQuestions[${status.index}].answers"
																							value="3" id="check${status.count}3"
																							onclick="checkfield('check${status.count}3','label${status.count}3',false)" />
																					</c:otherwise>
																				</c:choose>
																				${feedbackQuestion.option3}
																				</label>

																			</c:if>

																			<c:if test="${not empty feedbackQuestion.option4}">
																				<c:choose>
																					<c:when test="${fn:contains(feedbackQuestion.studentFeedbackResponse.answer, '4')}">
																						<label class="active1" id="label${status.count}4"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:when>
																					<c:otherwise>
																						<label class=" " id="label${status.count}4"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:otherwise>

																				</c:choose>

																				<c:choose>
																					<c:when
																						test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																						<form:radiobutton
																							path="feedbackQuestions[${status.index}].answer"
																							value="4" id="check${status.count}4"
																							onclick="checkfield('check${status.count}4','label${status.count}4',true)" />
																					</c:when>
																					<c:otherwise>
																						<form:checkbox
																							path="feedbackQuestions[${status.index}].answers"
																							value="4" id="check${status.count}4"
																							onclick="checkfield('check${status.count}4','label${status.count}4',false)" />
																					</c:otherwise>
																				</c:choose>
																				${feedbackQuestion.option4}
																				</label>

																			</c:if>
																			
																			<c:if test="${not empty feedbackQuestion.option5}">
																				<c:choose>
																					<c:when test="${fn:contains(feedbackQuestion.studentFeedbackResponse.answer, '5')}">
																						<label class="active1" id="label${status.count}5"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:when>
																					<c:otherwise>
																						<label class=" " id="label${status.count}5"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:otherwise>

																				</c:choose>

																				<c:choose>
																					<c:when
																						test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																						<form:radiobutton
																							path="feedbackQuestions[${status.index}].answer"
																							value="5" id="check${status.count}5"
																							onclick="checkfield('check${status.count}5','label${status.count}5',true)" />
																					</c:when>
																					<c:otherwise>
																						<form:checkbox
																							path="feedbackQuestions[${status.index}].answers"
																							value="5" id="check${status.count}5"
																							onclick="checkfield('check${status.count}5','label${status.count}5',false)" />
																					</c:otherwise>
																				</c:choose>
																				${feedbackQuestion.option5}
																				</label>

																			</c:if>

																		<c:if test="${not empty feedbackQuestion.option6}">
																				<c:choose>
																					<c:when test="${fn:contains(feedbackQuestion.studentFeedbackResponse.answer, '6')}">
																						<label class="active1" id="label${status.count}6"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:when>
																					<c:otherwise>
																						<label class=" " id="label${status.count}6"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:otherwise>

																				</c:choose>

																				<c:choose>
																					<c:when
																						test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																						<form:radiobutton
																							path="feedbackQuestions[${status.index}].answer"
																							value="6" id="check${status.count}6"
																							onclick="checkfield('check${status.count}6','label${status.count}6',true)" />
																					</c:when>
																					<c:otherwise>
																						<form:checkbox
																							path="feedbackQuestions[${status.index}].answers"
																							value="6" id="check${status.count}6"
																							onclick="checkfield('check${status.count}6','label${status.count}6',false)" />
																					</c:otherwise>
																				</c:choose>
																				${feedbackQuestion.option6}
																				</label>

																			</c:if>

																			<c:if test="${not empty feedbackQuestion.option7}">
																				<c:choose>
																					<c:when test="${fn:contains(feedbackQuestion.studentFeedbackResponse.answer, '7')}">
																						<label class="active1" id="label${status.count}7"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:when>
																					<c:otherwise>
																						<label class=" " id="label${status.count}7"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:otherwise>

																				</c:choose>

																				<c:choose>
																					<c:when
																						test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																						<form:radiobutton
																							path="feedbackQuestions[${status.index}].answer"
																							value="7" id="check${status.count}7"
																							onclick="checkfield('check${status.count}7','label${status.count}7',true)" />
																					</c:when>
																					<c:otherwise>
																						<form:checkbox
																							path="feedbackQuestions[${status.index}].answers"
																							value="7" id="check${status.count}7"
																							onclick="checkfield('check${status.count}7','label${status.count}7',false)" />
																					</c:otherwise>
																				</c:choose>
																				${feedbackQuestion.option7}
																				</label>

																			</c:if>

																		<c:if test="${not empty feedbackQuestion.option8}">
																				<c:choose>
																					<c:when test="${fn:contains(feedbackQuestion.studentFeedbackResponse.answer, '8')}">
																						<label class="active1" id="label${status.count}8"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:when>
																					<c:otherwise>
																						<label class=" " id="label${status.count}8"><i
																							class="check_ellipse fa fa-check"> </i>
																					</c:otherwise>

																				</c:choose>

																				<c:choose>
																					<c:when
																						test="${feedbackQuestion.type eq 'SINGLESELECT'}">
																						<form:radiobutton
																							path="feedbackQuestions[${status.index}].answer"
																							value="8" id="check${status.count}8"
																							onclick="checkfield('check${status.count}8','label${status.count}8',true)" />
																					</c:when>
																					<c:otherwise>
																						<form:checkbox
																							path="feedbackQuestions[${status.index}].answers"
																							value="8" id="check${status.count}8"
																							onclick="checkfield('check${status.count}8','label${status.count}8',false)" />
																					</c:otherwise>
																				</c:choose>
																				${feedbackQuestion.option8}
																				</label>

																			</c:if>

																		</div>
																	</div></td>
															</tr>

															<tr>
																<td>
																	<div class="text-center">
																		<c:if test="${not status.first}">
																			<a class="btn btn-danger prev-step"
																				onclick="showModal('feedBackTable-${status.index }','feedBackTable-${status.index - 1}','nav-${status.index - 1}')">Previous</a>
																		</c:if>

																		<c:choose>
																			<c:when test="${status.last}">
																			
																					<a class="btn btn-primary next-step"
																						onclick="submitForm('studentFeedbackForm-${status.index}','nav-${status.index}',true)"
																						>Save
																						and Complete</a>
																				
																			</c:when>
																			<c:otherwise>
																				
																					<a class="btn btn-primary next-step"
																						onclick="submitForm('studentFeedbackForm-${status.index}','nav-${status.index}');showModal('feedBackTable-${status.index }','feedBackTable-${status.index + 1}','nav-${status.index + 1}')">Save
																						and Next</a>
																				
																				<a class="btn btn-danger next-step	"
																					onclick="showModal('feedBackTable-${status.index }','feedBackTable-${status.index + 1}','nav-${status.index + 1}')">Skip</a>
																			</c:otherwise>
																		</c:choose>


																	</div>
																</td>
															</tr>

														</table>
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

				<!-- <div class="dashboard_height rightpanel" id="mySidenav1">
						<div class="right-arrow">
							<img src="images/dash-right.gif" alt="" onclick="openNav2()">
						</div>
						<div class="rightpanel_content">
							<h3>
								To Do <a href="#">View All</a>
							</h3>
							<ul>
								<li class="x_panel">
									<h4>
										<a href="#">Assignments</a> <a href="#" class="close-link"><i
											class="fa fa-close"></i></a>
									</h4>
									<p>Business Communication &amp; Etiquettes</p>
									<p>
										<span>19 points</span> - June 19, 2017 at 11:30am
									</p>
								</li>
								<li class="x_panel">
									<h4>
										<a href="#">Introduction Sessions</a> <a href="#"
											class="close-link"><i class="fa fa-close"></i></a>
									</h4>
									<p>Intro Oceans</p>
									<p>
										<span>10 points</span> - June 21, 2017 at 9:30am
									</p>
								</li>
								<li class="x_panel">
									<h4>
										<a href="#">Practice Quize</a> <a href="#" class="close-link"><i
											class="fa fa-close"></i></a>
									</h4>
									<p>Financial Accounts &amp; Analysis and Business Law</p>
									<p>
										<span>14 points</span> - June 21, 2017 at 1:30pm
									</p>
								</li>
							</ul>
							<h3>
								Announcements <a href="#">View All</a>
							</h3>
							<ul>
								<li class="x_panel">
									<h4>
										<a href="#">June 19, 2017</a> <a href="#" class="close-link"><i
											class="fa fa-close"></i></a>
									</h4>
									<p>Campus Recruitement Interviews by TCS, Infosys &amp;
										LTI.</p>
								</li>
								<li class="x_panel">
									<h4>
										<a href="#">June 23, 2017</a> <a href="#" class="close-link"><i
											class="fa fa-close"></i></a>
									</h4>
									<p>Welcome to Algebra 1A!</p>
									<p>Have you ever heard someone say, I'll never use algebra
										in real life.</p>
								</li>
								<li class="x_panel">
									<h4>
										<a href="#">June 27, 2017</a> <a href="#" class="close-link"><i
											class="fa fa-close"></i></a>
									</h4>
									<p>Internal Assignment applicable for June, 2017 Exam cycle
										is live!</p>
								</li>
							</ul>
						</div>
					</div> -->

				<%-- <jsp:include page="../homepage/toDoDashboard.jsp" /> --%>

			</div>

		</div>
		<!-- /page content -->









		<jsp:include page="../common/DashboardFooter.jsp" />

	</div>


</body>
<script>
	$("document").ready(
			function() {

				window.checkfield = function checkfield(chkId, lblId,
						isRadioOrChk) {
				var checked = $('#'+chkId).is(':checked');
				console.log('checked is'+ checked);
					console.log(isRadioOrChk);
					if (isRadioOrChk && checked) {
						$('#' + lblId).addClass('active1').siblings(".active1")
								.removeClass("active1");
					}
					if(!checked) {
						$('#' + lblId).removeClass('active1');
					}
					else {
						$('#' + lblId).addClass('active1');
					}

				}

			});
</script>

</html>