<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="com.spts.lms.web.utils.*"%>
<%@page import="com.spts.lms.web.utils.*"%>
<%@page import="java.util.List"%>
<%@page import="com.spts.lms.beans.test.Test"%>


<script>
	var a = '<c:out value="${test.studentTest.id}" />';
	console.log('a' + a);
</script>

<!doctype html>
<html lang="en">

<jsp:include page="../common/headerCss.jsp" />

<body class="nav-md ">



	<div class="container body">
		<div class="main_container">

			
			<%-- <jsp:include page="../common/leftSidebar.jsp" />
			<jsp:include page="../common/NewHeader1.jsp" /> --%>

	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
		<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
		<jsp:include page="../common/rightSidebarFaculty.jsp"/>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftNavbar.jsp" />
		<jsp:include page="../common/newLeftSidebar.jsp" />
		<jsp:include page="../common/rightSidebar.jsp" />
	</sec:authorize>
		<sec:authorize access="hasRole('ROLE_ADMIN')">
		<jsp:include page="../common/newAdminLeftNavBar.jsp"/>
	<jsp:include page="../common/rightSidebarAdmin.jsp" />
	</sec:authorize>

			<!-- page content -->
			<div class="right_col" role="main">

				<div class="dashboard_contain">

					<div class="dashboard_height" id="main">

						<div class="right-arrow">
							<img class="toggle_to_do"
								src="<c:url value="/resources/images/dash-right.gif" />" alt=""
								onclick="openNav2()">
						</div>

						<div class="dashboard_contain_specing">
							<div class="breadcrumb">
							
							 <c:out value="${Program_Name}" />
						
						<sec:authorize access="hasRole('ROLE_STUDENT')">
						
						<i
						class="fa fa-angle-right"></i>
											
											<c:out value="${AcadSession}" />
											
											</sec:authorize>
											
						<br><br>
							
								<a href="${pageContext.request.contextPath}/viewCourse?id=${courseId}">My Courses</a> <i
									class="fa fa-angle-right"></i> <a href="my-courses.html">Business
									Law</a> <i class="fa fa-angle-right"></i> Test/Quiz
							</div>

							<div class="row">
								<div class="col-xs-12 col-sm-12">
									<div class="x_panel">
										<div class="x_title">
											<h2>Test/Quiz</h2>
											<div class="clearfix"></div>
										</div>
										<div class="x_content">
											<div class="table_overflow">
												<table class="table">
													<thead>
														<tr>
															<th>Name</th>
															<th>Term</th>
															<th>Start Date</th>
															<th>End Date</th>
															<th>Status</th>
														</tr>
													</thead>
													<tbody>

														<tr>
															<th><a data-toggle="collapse"
																href="#test_quiz_acoording1"><i
																	class="pluse_ellipse fa fa-plus"></i> Concepts of
																	Electricity</a></th>
															<td>Jul-2017</td>
															<td>10-Sep-17</td>
															<td>20-Sep-17</td>
															<td><i class="check_ellipse fa fa-check"></i> Not
																Submitted</td>
														</tr>
														<tr id="test_quiz_acoording1"
															class="panel-collapse collapse">
															<td colspan="5">
																<div class="feedback_des_detail">
																	<p class="font_weight_bold">Test/quiz Details</p>
																	<ul class="bullet1">
																		<li>This Test is on Concepts of Electricity.</li>
																		<li>All questions are mandatory.</li>
																		<li>Test carries 20% weightage of ICE marks.</li>
																		<li>Marks will be considered only if test is
																			attended before end date</li>
																	</ul>
																	<div class="text-center">
																		<a data-fancybox data-src="#test_quiz_pop1"
																			href="javascript:;" class="btn2">Attend Test</a>
																	</div>
																</div>
															</td>
														</tr>
														<tr>
															<th><a data-toggle="collapse"
																href="#test_quiz_acoording2"><i
																	class="pluse_ellipse fa fa-plus"></i> Theory of
																	Kirchhoff Law</a></th>
															<td>Jul-2017</td>
															<td>9-Sep-17</td>
															<td>19-Sep-17</td>
															<td><i class="check_ellipse fa fa-check bg-green"></i>
																Not Submitted</td>
														</tr>
														<tr id="test_quiz_acoording2"
															class="panel-collapse collapse">
															<td colspan="5">
																<div class="feedback_des_detail">
																	<p class="font_weight_bold">Description Details</p>
																	<ul class="bullet1">
																		<li>This feedback is for administrative
																			facilities of College.</li>
																		<li>Please rate all services as per your
																			experience.</li>
																		<li>Your feedback is important to us to improve
																			services.</li>
																		<li>Anonymity of Feedback will be maintained</li>
																	</ul>
																	<div class="text-center">

																		<!--changes by hiren button -->
																		<a data-fancybox
																			data-options='{"smallBtn":false, "buttons":false}'
																			data-src="#test_quiz_pop2" href="javascript:;"
																			class="btn2 btntestquiz ">Provide Feedback</a>
																	</div>
																</div>
															</td>
														</tr>



													</tbody>
												</table>


												<div id="test_quiz_pop1" class="fancy_pop_box1">
													<h5>Are you ready to take Test?</h5>
													<hr>
													<div class="text-center">
														<a href="#" class="btn1" onclick="closeFancybox()">Cancel</a>
														<a data-fancybox data-src="#test_quiz_close1"
															href="javascript:;" class="btn2"
															onclick="closeFancybox()">Yes</a>
													</div>
												</div>

												<div id="test_quiz_close1" class="fancy_pop_box1">
													<h5>
														Test has started. <span>Best of Luck!</span>
													</h5>
												</div>

												<div id="test_quiz_pop2" class="fancy_pop_box2">
													<h2>Concepts of Electricity Test/Quiz</h2>
													<div class="container-fluid">
														<div class="row">
															<section>
																<div class="wizard">
																	<div class="col-xs-12 col-sm-2">
																		<div class="wizard-inner">

																			<ul class="nav nav-tabs" role="tablist">
																				<li role="presentation" class="active"><a
																					href="#tab1" data-toggle="tab"
																					aria-controls="step1" role="tab">Q.1</a></li>
																				<li role="presentation" class="disabled"><a
																					href="#tab2" data-toggle="tab"
																					aria-controls="step2" role="tab">Q.2</a></li>
																				<li role="presentation" class="disabled"><a
																					href="#tab3" data-toggle="tab"
																					aria-controls="step3" role="tab">Q.3</a></li>
																				<li role="presentation" class="disabled"><a
																					href="#tab4" data-toggle="tab" aria-controls="done"
																					role="tab" style="display: none;">Q.3</a></li>
																			</ul>
																		</div>
																	</div>
																	<div class="col-xs-12 col-sm-8">
																		<form role="form">
																			<div class="tab-content">

																				<c:forEach items="${test.testQuestions}"
																					var="testQuestion" varStatus="status">
																					
																					<form:form action="addStudentQuestionResponse"
																					method="post" modelAttribute="test"
																					id="studentTestForm-${status.index}">
																					
																					<div class="tab-pane active" role="tabpanel"
																						id="tab1">
																						<div class="test_popbox_contain">
																							<h4>${status.index + 1} - ${testQuestion.description}</h4>
																							<div class="feedback_provide_contain">
<!-- 																								<p>Select One Answer from the followings..</p>
 -->
																								<div>
																									<input type="radio" id="radio01" name="radio" />
																									<label for="radio01"><i
																										class="check_ellipse fa fa-check"> </i> Worst</label>
																								</div>

																								<div>
																									<input type="radio" id="radio02" name="radio" />
																									<label for="radio02"><i
																										class="check_ellipse fa fa-check"> </i> Better</label>
																								</div>

																								<div>
																									<input type="radio" id="radio03" name="radio" />
																									<label for="radio03"><i
																										class="check_ellipse fa fa-check"> </i> Good</label>
																								</div>

																								<div>
																									<input type="radio" id="radio04" name="radio" />
																									<label for="radio04"><i
																										class="check_ellipse fa fa-check"> </i> Nice</label>
																								</div>
																							</div>
																							<hr>
																							<div class="text-center">
																								<a class="btn1 next-step">Skip</a> <a
																									class="btn2 next-step">Save &amp; Next</a>
																							</div>

																						</div>
																					</div>
																					
																					</form:form>
																				</c:forEach>
																				
																					
																					<!-- <div class="tab-pane" role="tabpanel" id="tab2">
																						<div class="test_popbox_contain">
																							<h4>2 - Which two facilities are working to
																								expectations?</h4>
																							<div class="feedback_provide_contain">
																								<p>Select Two Answers from the followings..</p>

																								<div>
																									<input type="checkbox" id="facilities01"
																										name="facilities" /> <label
																										for="facilities01"><i
																										class="check_ellipse fa fa-check"> </i>
																										Canteen</label>
																								</div>

																								<div>
																									<input type="checkbox" id="facilities02"
																										name="facilities" /> <label
																										for="facilities02"><i
																										class="check_ellipse fa fa-check"> </i> Better</label>
																								</div>

																								<div>
																									<input type="checkbox" id="facilities03"
																										name="facilities" /> <label
																										for="facilities03"><i
																										class="check_ellipse fa fa-check"> </i> Good</label>
																								</div>

																								<div>
																									<input type="checkbox" id="facilities04"
																										name="facilities" /> <label
																										for="facilities04"><i
																										class="check_ellipse fa fa-check"> </i> Nice</label>
																								</div>
																							</div>
																							<hr>
																							<div class="text-center">
																								<a class="btn1 prev-step">Previous</a> <a
																									class="btn2 next-step">Skip</a> <a
																									class="btn2 next-step">Save &amp; Next</a>
																							</div>

																						</div>
																					</div>
																					<div class="tab-pane" role="tabpane1" id="tab3">
																						<div class="test_popbox_contain">
																							<h4>3 - Which two facilities are working to
																								expectations?</h4>
																							<div class="feedback_provide_contain">
																								<p>Select Two Answers from the followings..</p>

																								<div>
																									<input type="checkbox" id="facilities05"
																										name="facilities" /> <label
																										for="facilities05"><i
																										class="check_ellipse fa fa-check"> </i>
																										Canteen</label>
																								</div>

																								<div>
																									<input type="checkbox" id="facilities06"
																										name="facilities" /> <label
																										for="facilities06"><i
																										class="check_ellipse fa fa-check"> </i> Better</label>
																								</div>

																								<div>
																									<input type="checkbox" id="facilities07"
																										name="facilities" /> <label
																										for="facilities07"><i
																										class="check_ellipse fa fa-check"> </i> Good</label>
																								</div>

																								<div>
																									<input type="checkbox" id="facilities08"
																										name="facilities" /> <label
																										for="facilities08"><i
																										class="check_ellipse fa fa-check"> </i> Nice</label>
																								</div>
																							</div>
																							<hr>
																							<div class="text-center">
																								<a class="btn1 prev-step">Previous</a> <a
																									class="btn2 next-step">Save &amp; Next</a>
																							</div>

																						</div>
																					</div> -->
																					<div class="tab-pane" role="tabpane1" id="tab4">
																						<div class="test_popbox_contain">
																							<div class="test_submit_text">
																								<p>
																									You have reached to the end of Test.<br>Please
																									click submit button to submit your answers. <br>
																									<span>If you wish to review answers,</span><br>
																									<span>you can click on any of the
																										questions.</span>
																								</p>
																							</div>

																							<hr>
																							<div class="text-center">
																								<!--changes by hiren button -->
																								<a data-fancybox
																									data-options='{"smallBtn":false, "buttons":false}'
																									data-src="#test_quiz_concepts3"
																									href="javascript:;" onclick="closeFancybox()"
																									class="btn2">Submit</a>
																							</div>

																						</div>
																					</div>

																					<div class="clearfix"></div>
																			</div>
																		</form>
																	</div>
																	<div class="col-xs-12 col-sm-2 text-center">
																		<div id="DateCountdown"
																			data-date="2017-08-03 20:00:00
"
																			style="width: 160px; height: 360px; padding: 0px; box-sizing: border-box;">
																			<div class="timer_right_img">
																				<img
																					src="<c:url value="/resources/images/watch.png" />" />
																			</div>
																		</div>
																	</div>
																</div>
															</section>
														</div>
													</div>
													<div class="clear"></div>
												</div>

												<div id="test_quiz_concepts3" class="fancy_pop_box2">
													<h2>Concepts of Electricity Test/Quiz</h2>

													<div class="test_submit_Contain">
														<div class="alert alert-success">
															<strong>Congratulations!</strong> You have uploaded file
															successfully.
														</div>
														<div class="group">
															<h6>
																<span>Total Score:</span> 20 points
															</h6>
															<h6>
																<span>Status:</span> passed
															</h6>
															<h6>
																<span>Reattempt:</span> Allowed once
															</h6>
														</div>
														<hr>
														<div class="text-center">
															<a href="test-quiz-detail.html" class="btn1">Close</a> <a
																href="test-quiz-detail.html" class="btn2">Reattempt</a>
														</div>
													</div>

												</div>

											</div>
										</div>
									</div>
								</div>
							</div>

						</div>

					</div>

					<div class="dashboard_height rightpanel" id="mySidenav1">
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
					</div>

				</div>

			</div>
			<!-- /page content -->


			<jsp:include page="../common/DashboardFooter.jsp" />

		</div>
	</div>
	<script>
		$('[data-fancybox]').fancybox({
			clickSlide : false
		});
		function closeFancybox() {
			$.fancybox.close();
		}
	</script>
</body>
</html>
