
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page import="java.util.*"%>
<%@ page import="java.util.Random"%>

<%@page import="com.spts.lms.beans.test.Test"%>

<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">


			<jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/topHeader.jsp" />




			<!-- page content: START -->

			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">
						<input type="hidden" id="duration" value="${durationsLeft}" /> <input
							type="hidden" id="durationCompletedByStudent"
							value="${durationCompletedByStudent}" /> <input type="hidden"
							id="testCompleted" value="${action}" /> <a
							href="${pageContext.request.contextPath}/viewCourse?id=${courseId}">My
							Courses</a> <i class="fa fa-angle-right"></i> Test Quiz
					</div>
					<jsp:include page="../common/alert.jsp" />
					<!-- Input Form Panel -->


					<!-- Results Panel -->
					<div class="row">
						<div class="col-xs-12 col-sm-12">
							<div class="x_panel">
								<div class="x_title">
									<h2>
										Test Quiz
										<c:out value="${test.studentTest.completed}" />
										<input type="hidden" id="dateTimeVal" value="${dateTime}" />
										----
										<c:out value="${test.studentTest.attempt}" />
										---
										<c:out value="${disableCancel}" />
									</h2>

									<a data-fancybox data-src="#test_quiz_pop2" id="modal_link"
										href="javascript:;" class="btn btn-primary btntestquiz"
										style="display: none;"
										data-options='{"smallBtn":false, "buttons":false, "clickSlide": false, "touch": false, "keyboard": false}'></a>

									<%-- <c:if test="${action eq 'add' }">
										<a data-fancybox data-src="#test_quiz_pop1" id="modal_link"
											href="javascript:;" class="btn btn-primary btntestquiz"
											style="display: none;"
											data-options='{"smallBtn":false, "buttons":false, "clickSlide": false, "touch": false, "keyboard": false}'></a>
									</c:if>
									<c:if test="${ action eq 'view' }">
										<a data-fancybox data-src="#test_quiz_pop10" id="modal_link1"
											href="javascript:;" class="btn btn-primary btntestquiz"
											style="display: none;"
											data-options='{"smallBtn":false, "buttons":false, "clickSlide": false, "touch": false, "keyboard": false}'></a>
									</c:if> --%>



									<div class="clearfix"></div>
								</div>
								<div class="x_content">
									<div id="test_quiz_pop1" class="fancy_pop_box1">
										<h5>Are you ready to take Test?</h5>
										<hr>

										<div class="text-center">
											<a data-fancybox data-src="#test_quiz_pop2"
												href="javascript;;" class="btn btn-primary btntestquiz"
												onclick="deleteResponse(${test.id});closeFancybox()">Yes</a>

											<a href="testList" class="btn btn-primary"
												onclick="closeFancybox()">Cancel</a>

										</div>
									</div>
									<div id="test_quiz_pop10" class="fancy_pop_box1">
										<h5>Are you ready to view test response?</h5>
										<hr>

										<div class="text-center">
											<a data-fancybox data-src="#test_quiz_pop2"
												href="javascript;;" class="btn btn-primary btntestquiz"
												onclick="closeFancybox()">Yes</a> <a href="testList"
												class="btn btn-primary" onclick="closeFancybox()">Cancel</a>

										</div>
									</div>
									<div id="test_quiz_pop2" class="fancy_pop_box2">
										<h2>${test.testName}</h2>
										<div class="container-fluid">
											<div class="row">
												<section>
													<div class="wizard">
														<div class="col-xs-12 col-sm-2">
															<div class="wizard-inner">




																<ul class="nav nav-tabs" role="tablist">
																	<c:forEach items="${test.testQuestions}"
																		var="testQuestion" varStatus="status">


																		<c:choose>
																			<c:when test="${status.first}">
																				<li class="active tabPanelClass" role="presentation"
																					id="tap-panelTest${status.index}"><a
																					href="#modal-${status.index}"
																					onclick="showModalOnClick('modal-${status.index}','tap-panelTest${status.index}')"
																					data-toggle="tab" role="tab"> <fmt:formatNumber
																							value="${status.index + 1}" pattern="00" />.
																				</a></li>

																			</c:when>
																			<c:otherwise>

																				<li class="disabled tabPanelClass"
																					role="presentation"
																					id="tap-panelTest${status.index}"><a
																					href="#modal${status.index}"
																					onclick="showModalOnClick('modal-${status.index}','tap-panelTest${status.index}')"
																					data-toggle="tab" role="tab"> <fmt:formatNumber
																							value="${status.index + 1}" pattern="00" />.
																				</a></li>
																			</c:otherwise>
																		</c:choose>



																	</c:forEach>
																</ul>
															</div>
														</div>
														<div class="col-xs-12 col-sm-8">
															<div class="tab-content">
																<c:forEach items="${test.testQuestions}"
																	var="testQuestion" varStatus="status">
																	<form:form action="addStudentQuestionResponse"
																		method="post" modelAttribute="test"
																		id="studentTestForm-${status.index}">


																		<div class="tab-pane active"
																			id="modal-${status.index}">
																			<div class="test_popbox_contain"
																				id="tab-${status.index}">
																				<h4>${status.index + 1}-
																					${testQuestion.description} ( Marks:
																					${testQuestion.marks} )</h4>
																				<div class="feedback_provide_contain">
																					<c:if test="${testQuestion.type eq 'SINGLESELECT'}">
																						<!-- <p>Select One Answer from the followings..</p> -->
																					</c:if>
																					<c:if test="${testQuestion.type eq 'MULTISELECT'}">
																						<p>Select Multiple</p>
																					</c:if>


																					<c:if
																						test="${testQuestion.studentQuestionResponse.answer ne null && 'Objective' eq testDb.testType && (testDb.maxAttempt) eq test.studentTest.attempt}">
																						<p>
																							Correct Answer:
																							<c:forEach begin="0"
																								end="${fn:length(testQuestion.correctOptions) - 1}"
																								var="index">
																								<c:if
																									test="${(fn:length(testQuestion.correctOptions) - 1) ne index }">
																									<c:out
																										value="${testQuestion.correctOptions[index]} ," />
																								</c:if>
																								<c:if
																									test="${(fn:length(testQuestion.correctOptions) - 1) eq index }">
																									<c:out
																										value="${testQuestion.correctOptions[index]} " />
																								</c:if>

																							</c:forEach>
																						</p>
																					</c:if>
																					<%-- <c:if test="${test.studentTest.completed}">
																							<c:choose>
																								<c:when
																									test="${not empty testQuestion.studentQuestionResponse.marks}">
																									<i class="fa fa-check-square-o fa-2x"
																										style="color: green;"></i>
																								</c:when>
																								<c:otherwise>
																									<i class="fa fa-times-circle-o fa-2x"
																										style="color: red;"></i>
																								</c:otherwise>
																							</c:choose>

																						</c:if> --%>
																					<form:input type="hidden"
																						path="testQuestions[${status.index}].studentQuestionResponse.studentTestId"
																						value="${test.studentTest.id}"
																						id="StudentTestIdValue" />
																					<form:input type="hidden"
																						path="testQuestions[${status.index}].studentQuestionResponse.questionId"
																						value="${testQuestion.id}" />


																					<!-- Added Logic By Akshay to check optionShuffle whether is required -->

																					<c:choose>

																						<c:when
																							test="${testQuestion.questionType eq 'Numeric'}">

																							<label>Enter Your Answer Here:</label>
																							<form:textarea rows="1" cols="20"
																								class="form-group scrollDesc w-100"
																								path="testQuestions[${status.index}].answer"
																								id="checkField${status.count}1"
																								onkeypress="return isNumberKey(event)"
																								style="margin-top: 30px;margin-bottom:10px resize: none;" />
																						</c:when>
																						<c:when
																							test="${testQuestion.questionType eq 'Descriptive'}">

																							<label>Enter Your Answer Here:</label>
																							<form:textarea rows="15" cols="40"
																								class="form-group scrollDescription w-100"
																								path="testQuestions[${status.index}].answer"
																								id="checkField${status.count}1"
																								style="margin-top: 30px;margin-bottom:10px resize: none;" />
																						</c:when>

																						<c:when
																							test="${testQuestion.questionType eq 'Image' && testQuestion.type eq null }">
																							
																							<label>Enter Your Answer Here:</label>
																							<form:textarea rows="15" cols="40"
																								class="form-group scrollDescription w-100"
																								path="testQuestions[${status.index}].answer"
																								id="checkField${status.count}1"
																								style="margin-top: 30px;margin-bottom:10px resize: none;" />
																						</c:when>


																						<c:otherwise>
																							<c:choose>
																								<c:when
																									test="${'Y' eq testQuestion.optionShuffle}">

																									<%
																										Random rand = new Random();
																																Integer n = rand.nextInt(3) + 1;
																																if (n == 1) {
																									%>
																									<c:if test="${not empty testQuestion.option1}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '1')}">
																												<label class="active1"
																													id="label${status.count}1"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}1"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="1" id="checkField${status.count}1"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}1','label${status.count}1',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="1" id="checkField${status.count}1"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}1','label${status.count}1',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option1}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option2}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '2')}">
																												<label class="active1"
																													id="label${status.count}2"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}2"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="2" id="checkField${status.count}2"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}2','label${status.count}2',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="2" id="checkField${status.count}2"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}2','label${status.count}2',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option2}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option3}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '3')}">
																												<label class="active1"
																													id="label${status.count}3"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}3"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="3" id="checkField${status.count}3"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}3','label${status.count}3',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="3" id="checkField${status.count}3"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}3','label${status.count}3',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option3}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option4}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '4')}">
																												<label class="active1"
																													id="label${status.count}4"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}4"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="4" id="checkField${status.count}4"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}4','label${status.count}4',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="4" id="checkField${status.count}4"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}4','label${status.count}4',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option4}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option5}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '5')}">
																												<label class="active1"
																													id="label${status.count}5"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}5"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="5" id="checkField${status.count}5"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}5','label${status.count}5',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="5" id="checkField${status.count}5"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}5','label${status.count}5',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option5}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option6}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '6')}">
																												<label class="active1"
																													id="label${status.count}6"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}6"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="6" id="checkField${status.count}6"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}6','label${status.count}6',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="6" id="checkField${status.count}6"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}6','label${status.count}6',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option6}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option7}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '7')}">
																												<label class="active1"
																													id="label${status.count}7"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}7"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="7" id="checkField${status.count}7"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}7','label${status.count}7',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="7" id="checkField${status.count}7"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}7','label${status.count}7',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option7}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option8}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '8')}">
																												<label class="active1"
																													id="label${status.count}8"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}8"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="8" id="checkField${status.count}8"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}8','label${status.count}8',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="8" id="checkField${status.count}8"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}8','label${status.count}8',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option8}</label>
																									</c:if>

																									<%
																										}
																																if (n == 2) {
																									%>

																									<c:if test="${not empty testQuestion.option2}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '2')}">
																												<label class="active1"
																													id="label${status.count}2"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}2"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="2" id="checkField${status.count}2"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}2','label${status.count}2',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="2" id="checkField${status.count}2"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}2','label${status.count}2',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option2}</label>
																									</c:if>


																									<c:if test="${not empty testQuestion.option4}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '4')}">
																												<label class="active1"
																													id="label${status.count}4"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}4"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="4" id="checkField${status.count}4"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}4','label${status.count}4',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="4" id="checkField${status.count}4"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}4','label${status.count}4',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option4}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option5}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '5')}">
																												<label class="active1"
																													id="label${status.count}5"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}5"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="5" id="checkField${status.count}5"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}5','label${status.count}5',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="5" id="checkField${status.count}5"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}5','label${status.count}5',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option5}</label>
																									</c:if>
																									<c:if test="${not empty testQuestion.option1}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '1')}">
																												<label class="active1"
																													id="label${status.count}1"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}1"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="1" id="checkField${status.count}1"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}1','label${status.count}1',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="1" id="checkField${status.count}1"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}1','label${status.count}1',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option1}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option7}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '7')}">
																												<label class="active1"
																													id="label${status.count}7"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}7"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="7" id="checkField${status.count}7"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}7','label${status.count}7',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="7" id="checkField${status.count}7"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}7','label${status.count}7',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option7}</label>
																									</c:if>


																									<c:if test="${not empty testQuestion.option3}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '3')}">
																												<label class="active1"
																													id="label${status.count}3"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}3"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="3" id="checkField${status.count}3"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}3','label${status.count}3',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="3" id="checkField${status.count}3"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}3','label${status.count}3',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option3}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option8}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '8')}">
																												<label class="active1"
																													id="label${status.count}8"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}8"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="8" id="checkField${status.count}8"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}8','label${status.count}8',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="8" id="checkField${status.count}8"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}8','label${status.count}8',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option8}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option6}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '6')}">
																												<label class="active1"
																													id="label${status.count}6"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}6"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="6" id="checkField${status.count}6"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}6','label${status.count}6',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="6" id="checkField${status.count}6"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}6','label${status.count}6',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option6}</label>
																									</c:if>






																									<%
																										}
																																if (n == 3) {
																									%>

																									<c:if test="${not empty testQuestion.option4}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '4')}">
																												<label class="active1"
																													id="label${status.count}4"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}4"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="4" id="checkField${status.count}4"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}4','label${status.count}4',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="4" id="checkField${status.count}4"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}4','label${status.count}4',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option4}</label>
																									</c:if>




																									<c:if test="${not empty testQuestion.option6}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '6')}">
																												<label class="active1"
																													id="label${status.count}6"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}6"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="6" id="checkField${status.count}6"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}6','label${status.count}6',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="6" id="checkField${status.count}6"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}6','label${status.count}6',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option6}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option5}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '5')}">
																												<label class="active1"
																													id="label${status.count}5"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}5"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="5" id="checkField${status.count}5"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}5','label${status.count}5',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="5" id="checkField${status.count}5"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}5','label${status.count}5',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option5}</label>
																									</c:if>
																									<c:if test="${not empty testQuestion.option1}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '1')}">
																												<label class="active1"
																													id="label${status.count}1"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}1"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="1" id="checkField${status.count}1"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}1','label${status.count}1',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="1" id="checkField${status.count}1"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}1','label${status.count}1',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option1}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option3}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '3')}">
																												<label class="active1"
																													id="label${status.count}3"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}3"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="3" id="checkField${status.count}3"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}3','label${status.count}3',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="3" id="checkField${status.count}3"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}3','label${status.count}3',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option3}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option7}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '7')}">
																												<label class="active1"
																													id="label${status.count}7"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}7"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="7" id="checkField${status.count}7"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}7','label${status.count}7',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="7" id="checkField${status.count}7"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}7','label${status.count}7',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option7}</label>
																									</c:if>




																									<c:if test="${not empty testQuestion.option8}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '8')}">
																												<label class="active1"
																													id="label${status.count}8"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}8"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="8" id="checkField${status.count}8"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}8','label${status.count}8',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="8" id="checkField${status.count}8"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}8','label${status.count}8',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option8}</label>
																									</c:if>
																									<c:if test="${not empty testQuestion.option2}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '2')}">
																												<label class="active1"
																													id="label${status.count}2"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}2"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="2" id="checkField${status.count}2"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}2','label${status.count}2',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="2" id="checkField${status.count}2"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}2','label${status.count}2',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option2}</label>
																									</c:if>



																									<%
																										}
																									%>
																								</c:when>
																								<c:otherwise>
																									<c:if test="${not empty testQuestion.option1}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '1')}">
																												<label class="active1"
																													id="label${status.count}1"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}1"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="1" id="checkField${status.count}1"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}1','label${status.count}1',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="1" id="checkField${status.count}1"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}1','label${status.count}1',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option1}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option2}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '2')}">
																												<label class="active1"
																													id="label${status.count}2"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}2"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="2" id="checkField${status.count}2"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}2','label${status.count}2',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="2" id="checkField${status.count}2"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}2','label${status.count}2',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option2}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option3}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '3')}">
																												<label class="active1"
																													id="label${status.count}3"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}3"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="3" id="checkField${status.count}3"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}3','label${status.count}3',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="3" id="checkField${status.count}3"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}3','label${status.count}3',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option3}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option4}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '4')}">
																												<label class="active1"
																													id="label${status.count}4"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}4"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="4" id="checkField${status.count}4"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}4','label${status.count}4',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="4" id="checkField${status.count}4"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}4','label${status.count}4',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option4}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option5}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '5')}">
																												<label class="active1"
																													id="label${status.count}5"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}5"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="5" id="checkField${status.count}5"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}5','label${status.count}5',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="5" id="checkField${status.count}5"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}5','label${status.count}5',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option5}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option6}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '6')}">
																												<label class="active1"
																													id="label${status.count}6"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}6"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="6" id="checkField${status.count}6"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}6','label${status.count}6',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="6" id="checkField${status.count}6"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}6','label${status.count}6',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option6}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option7}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '7')}">
																												<label class="active1"
																													id="label${status.count}7"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}7"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="7" id="checkField${status.count}7"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}7','label${status.count}7',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="7" id="checkField${status.count}7"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}7','label${status.count}7',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option7}</label>
																									</c:if>

																									<c:if test="${not empty testQuestion.option8}">
																										<c:choose>
																											<c:when
																												test="${fn:contains(testQuestion.studentQuestionResponse.answer, '8')}">
																												<label class="active1"
																													id="label${status.count}8"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:when>
																											<c:otherwise>
																												<label class="" id="label${status.count}8"><i
																													class="check_ellipse fa fa-check"></i>
																											</c:otherwise>
																										</c:choose>
																										<c:choose>
																											<c:when
																												test="${testQuestion.type eq 'SINGLESELECT'}">
																												<form:radiobutton
																													path="testQuestions[${status.index}].answer"
																													value="8" id="checkField${status.count}8"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}8','label${status.count}8',true)" />
																											</c:when>
																											<c:otherwise>
																												<form:checkbox
																													path="testQuestions[${status.index}].answers"
																													value="8" id="checkField${status.count}8"
																													onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');checkFields('checkField${status.count}8','label${status.count}8',false)" />
																											</c:otherwise>
																										</c:choose>${testQuestion.option8}</label>
																									</c:if>
																								</c:otherwise>
																							</c:choose>
																						</c:otherwise>
																					</c:choose>

																					<!-- End -->

																					<c:if test="${'Subjective' eq testDb.testType}">


																						<form:textarea rows="15" cols="40"
																							class="form-group scrollDescription"
																							path="testQuestions[${status.index}].answers"
																							id="checkField${status.count}1"
																							style="margin-top: 30px;margin-bottom:10px resize: none;" />
																					</c:if>

																				</div>
																				<hr>



																				<div class="text-center">
																					<c:if test="${not status.first}">
																						<a class="btn btn-primary prev-step"
																							onclick="showModal('modal-${status.index }','modal-${status.index - 1}','nav-${status.index - 1}')">Previous</a>
																					</c:if>

																					<c:choose>
																						<c:when test="${status.last}">
																							<%-- <c:if test="${not test.studentTest.completed}">
																									<a class="btn btn-large btn-primary"
																										onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');showModal('modal-${status.index }','modal-added','nav-${status.index + 1}')"
																										
																										
																										>Save
																										And Complete</a>
																								</c:if> --%>
																							<c:if test="${action eq 'add'}">
																								<a class="btn btn-large btn-primary"
																									onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');showModal('modal-${status.index }','modal-added','nav-${status.index + 1}')">Save
																									And Complete</a>
																							</c:if>

																							<c:if test="${action eq 'view'}">
																								<a
																									href="${pageContext.request.contextPath}/testList"
																									class="btn btn-primary">Exit</a>
																							</c:if>


																							<%-- <c:if test="${not test.studentTest.completed}">
																									<a class="btn btn-large btn-primary"
																										onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}',true);showModalToSubmit('modal-${status.index }')">Save
																										And Complete</a>
																								</c:if> --%>

																						</c:when>
																						<c:otherwise>
																							<c:if test="${action eq 'add'}">
																								<a class="btn btn-primary next-step"
																									onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');showModal('modal-${status.index }','modal-${status.index + 1}','nav-${status.index + 1}')">Save
																									and Next</a>
																							</c:if>
																							<c:if test="${action eq 'view'}">
																								<a
																									href="${pageContext.request.contextPath}/testList"
																									class="btn btn-primary">Exit</a>
																							</c:if>

																							<a class="btn btn-primary next-step"
																								onclick="submitForm('studentTestForm-${status.index}','nav-${status.index}');showModal('modal-${status.index }','modal-${status.index + 1}','nav-${status.index + 1}')">Skip</a>
																						</c:otherwise>
																					</c:choose>
																				</div>

																			</div>
																		</div>

																	</form:form>
																</c:forEach>
																<div class="tab-pane" id="modal-added">
																	<div class="test_popbox_contain">
																		<div class="test_submit_text">
																			<p>
																				You have reached to the end of Test.<br>Please
																				click submit button to submit your answers. <br>
																				<span>If you wish to review answers,</span><br>
																				<span>you can click on any of the questions.</span>
																				<span>Total Questions: </span>
																			<p id="totalQuestions"></p>
																			<span>Total Question Answered: </span>
																			<p id="totalQuestionAnswered"></p>
																			</p>
																		</div>

																		<hr>
																		<div class="text-center">
																			<a data-fancybox
																				data-options='{"smallBtn":false, "buttons":false}'
																				onclick="saveStudentsTest('${test.studentTest.id}')"
																				data-src="#test_quiz_concepts3"
																				onclick="closeFancybox();" class="btn btn-primary"
																				id="finalSubmit1">Submit</a>
																		</div>

																	</div>
																</div>
																<c:if test="${test.showResultsToStudents eq 'Y'}">
																	<div id="test_quiz_concepts3" class="fancy_pop_box2"
																		onload="getStudentData('${test.studentTest.id}')">
																		<h2>${test.testName}</h2>

																		<div class="test_submit_Contain">
																			<div class="alert alert-success">
																				<strong>Congratulations!</strong> You have completed
																				test successfully.
																			</div>
																			<div class="group">
																				<h6>
																					<span>Total Score:</span>
																					<p id="totalScoreParagraph"></p>
																				</h6>
																				<h6>
																					<span>Status:</span>
																					<p id="passParagraph"></p>
																				</h6>
																				<h6>
																					<span>Reattempt:</span> Allowed ${test.maxAttempt}
																					times
																				</h6>
																			</div>
																			<hr>
																			<div class="text-center">
																				<a
																					href="${pageContext.request.contextPath}/testList"
																					class="btn btn-primary">Close</a>
																			</div>
																		</div>

																	</div>
																</c:if>
																<c:if test="${test.showResultsToStudents eq 'N'}">
																	<div id="test_quiz_concepts3" class="fancy_pop_box2"
																		onload="getStudentData('${test.studentTest.id}')">
																		<h2>${test.testName}</h2>

																		<div class="test_submit_Contain">
																			<div class="alert alert-success">
																				<strong>Congratulations!</strong> You have completed
																				test successfully.
																			</div>
																			<div class="group">
																				<h6>
																					<span>Total Score Will be displayed Soon..</span>

																				</h6>
																				<h6>
																					<span>Reattempt:</span> Allowed ${test.maxAttempt}
																					times
																				</h6>
																			</div>
																			<hr>
																			<div class="text-center">
																				<a
																					href="${pageContext.request.contextPath}/testList"
																					class="btn btn-primary">Close</a>
																			</div>
																		</div>
																	</div>
																</c:if>
															</div>

														</div>

														<div class="col-xs-12 col-sm-2 text-center">
															<div id="DateCountdown" data-date=""
																style="width: 160px; height: 360px; padding: 0px; box-sizing: border-box;">
																<div class="timer_right_img">
																	<img src="<c:url value="/resources/images/watch.png"/>" />
																</div>
															</div>
														</div>


														<div class="clearfix"></div>
													</div>
												</section>
											</div>

										</div>
									</div>

								</div>
							</div>
							<div class="clear"></div>
						</div>
					</div>
				</div>
			</div>
		</div>

	</div>



	</div>


	<!-- /page content: END -->


	<jsp:include page="../common/testFooter.jsp" />







</body>

<script type="text/javascript"
      src="//cdn.ckeditor.com/4.8.0/standard-all/ckeditor.js"></script>
<script type="text/javascript"
      src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script>

<script type="text/javascript">
       
       function isNumberKey(evt)
       {
          var charCode = (evt.which) ? evt.which : evt.keyCode;
          if (charCode != 46 && charCode > 31 && ((charCode < 48) || (charCode > 57))){
        	  
 				if(charCode==45 && !charCode.shiftKey){
        		  
        		  	return true;
        	 	 }
          return false;
          }
             

          return true;
       }
      
</script>

<script type="text/javascript">
	$(document).ready(function() {
		$("#modal_link").fancybox().trigger('click');
		$("#modal_link1").fancybox().trigger('click');
		$('#modal-added').hide();
		
		 
		      
		 
		 
	});
	
	$('[data-fancybox]').fancybox({
		// Clicked on the background (backdrop) element
		clickSlide : false,
		"smallBtn" : false,
		"buttons" : false,
		"touch" : false,
		keyboard : false

	});
	
	window.deleteResponse = function deleteResponse(testId) {
	
		 $.post('deleteStudentTestResponse?testId='+testId+'', null).then(
				function(response) {
					
				}).done(function(response) {
			if (response)
				location.reload();
		}).fail(function() {
		}).always(function() {
		});
	};

	
	function closeFancybox() {
		console.log('fancybox closed')
		$.fancybox.close();
	};

	$(".tab-pane").each(function() {
		var id = $(this).attr("id");
		console.log("id -->" + id);
		if (id == 'modal-0') {
			$('#' + id).show();
		} else {
			$('#' + id).hide();

		}
	});

	$('#modal-0').show();
	$(':checked').parent().addClass('active');
	$('.nav-tabs-vert li a').click(
			function(event) {
				$(".modal-content").hide();
				$(this).parent().addClass('active').siblings(".active")
						.removeClass("active");
				$($(this).data('href')).show();
			});
	window.showModal = function showModal(currentModal, modalToView,
			navToSelect) {
		$('#' + currentModal).hide();
		$('#' + modalToView).show();
		$('#' + navToSelect).addClass('active').siblings(".active")
				.removeClass("active");
	};

	window.showModalToSubmit = function showModalToSubmit(currentModal) {
		$('#' + currentModal).hide();
		$('#modal-added').show();

	}

	window.getStudentData = function getStudentData(studentTestId) {
		console.log('getStudentData called successfully...');
		$.ajax({
			type : 'POST',
			url : '${pageContext.request.contextPath}/getStudentTestData?'
					+ 'studentTestId=' + studentTestId,
			success : function(data) {
				
				console.log("data is ------------" + data);
			},
			error : function(result) {

			}
		});
	}

	window.submitForm = function submitForm(formId, navId, isComplete) {
		$('#' + navId).addClass("submitted");
		$.post('addStudentQuestionResponse', $('#' + formId).serialize()).then(
				function(response) {
					
					var studentTestId =  ${test.studentTest.id} ;
					console.log(studentTestId);
					getTestSummery(studentTestId);
					
					if (isComplete) {
						console.log('test completed');
						return $.post('completeStudentTestForMixAjax', {
							studentTestId : response.studentTestId
						});
					}
				}).done(function(response) {
			if (response)
				location.reload();
		}).fail(function() {
		}).always(function() {
		});
	};
	
	
	window.getTestSummery = function getTestSummery(studTestId) {

		$.ajax({
			type : 'POST',
			url : '${pageContext.request.contextPath}/getTestSummeryAjax?'
					+ 'studentTestId=' + studTestId,
			success : function(data) {

				var parsedObj = JSON.parse(data);
				console.log("totalQuestions is---" + parsedObj.totalQuestions);
				console.log("totalQuestionAttempted is---" + parsedObj.totalQuestionAttempted);
				$("#totalQuestions").text(parsedObj.totalQuestions);
				$("#totalQuestionAnswered").text(parsedObj.totalQuestionAttempted);
			},
			error : function(result) {
				console.log('error');
			}
		});
	};
	

	window.saveStudentsTest = function saveStudentsTest(studTestId) {

		$.ajax({
			type : 'POST',
			url : '${pageContext.request.contextPath}/completeStudentTestForMixAjax?'
					+ 'studentTestId=' + studTestId,
			success : function(data) {

				var parsedObj = JSON.parse(data);
				console.log("score is---" + parsedObj.score);
				console.log("status is---" + parsedObj.score);
				$("#totalScoreParagraph").text(parsedObj.score);
				$("#passParagraph").text(parsedObj.status);
			},
			error : function(result) {
				console.log('error');
			}
		});
	};

	window.checkFields = function checkFields(chkId, lblId, isRadioOrChk) {
		var checked = $('#' + chkId).is(':checked');
		console.log('checked is' + checked);
		console.log(isRadioOrChk);
		if (isRadioOrChk && checked) {
			$('#' + lblId).addClass('active1').siblings(".active1")
					.removeClass("active1");
		}
		if (!checked) {
			$('#' + lblId).removeClass('active1');
		} else {
			$('#' + lblId).addClass('active1');
		}

	}

	window.showModalOnClick = function showModalOnClick(modalToShow,tabPaneId) {

		$(".tab-pane").each(function() {
			var id = $(this).attr("id");
			if (id == modalToShow) {
				console.log('funcion showmodalclick called');
				$('#' + tabPaneId).addClass('active').siblings(".tabPanelClass").removeClass("active");
				
				$('#' + modalToShow).show();
			} else {
				$('#' + id).hide();
			}
		});
	};
	
	window.saveCompletedDuration = function saveCompletedDuration(durationCompleted) {
		console.log('update duration function called');
		console.log('duration is==>'+ durationCompleted);
			$.ajax({
				type : 'POST',
				url : '${pageContext.request.contextPath}/updateStudentsAttemptedDuration?'
						+ 'durationInMinute=' + durationCompleted+'&studentTestId='+$("#StudentTestIdValue").val(),
				success : function(data) {

					console.log('SUCCESS!');
				},
				error : function(result) {
					console.log('error');
				}
			});
		};
</script>



</html>
