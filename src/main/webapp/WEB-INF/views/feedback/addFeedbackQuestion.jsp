<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex adminPage" id="adminPage">
<jsp:include page="../common/newAdminLeftNavBar.jsp"/>
<jsp:include page="../common/rightSidebarAdmin.jsp" />


<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newAdminTopHeader.jsp" />
     
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                       
                        <!-- page content: START -->
					<nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                        <li class="breadcrumb-item" aria-current="page">
                            <c:out value="${Program_Name}" />
                        </li>
                        <sec:authorize access="hasRole('ROLE_STUDENT')">
                            <c:out value="${AcadSession}" />
                        </sec:authorize>
                        <li class="breadcrumb-item active" aria-current="page"> Add Feedback Questions</li>
                    </ol>
                </nav>

						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="card bg-white border">
							<div class="card-body">


										<h5 class="text-center border-bottom pb-2">Configure Questions</h5>
										<c:if test="${showProceed eq true}">
										<div class="col-12 p-0 mb-3 text-right">
											<a href="addStudentFeedbackForm"><i
												class="btn btn-large btn-primary">Proceed
													to allocate students</i></a>
													</div>
										</c:if>

										<c:if test="${not empty feedback.feedbackQuestions}">

													<div class="panel-group" id="feedbackQuestionAccordion"
														role="tablist" aria-multiselectable="true">
														<c:forEach items="${feedback.feedbackQuestions}"
															var="feedbackQuestionVar" varStatus="status">
															<div class="panel panel-default">
																<div class="panel-heading cursor-pointer" role="tab"
																	id="heading-${status.index}" data-toggle="collapse"
																			data-parent="#feedbackQuestionAccordion"
																			href="#collapse-${status.index}"
																			aria-expanded="false"
																			aria-controls="collapse-${status.index}">
																	<h4 class="panel-title">
																		Q${status.index+1}.
																				${feedbackQuestionVar.description}
																	</h4>
																</div>
																<div id="collapse-${status.index}"
																	class="panel-collapse collapse" role="tabpanel"
																	aria-labelledby="heading-${status.index}">
																	<div class="panel-body">
																		
																			<form:form action="updateFeedbackQuestion"
																				method="post" modelAttribute="feedback"
																				id="feedbackQuestionForm-${status.index}">
																				<div class="row">
																				<div class="col-sm-12">
																					<form:input type="hidden"
																						path="feedbackQuestions[${status.index}].id"
																						value="${feedbackQuestionVar.id}" />
																					<div class="form-group">
																						<form:label
																							path="feedbackQuestions[${status.index}].description"
																							for="description">Description <span style="color: red">*</span></form:label>
																						<form:input id="description"
																							path="feedbackQuestions[${status.index}].description"
																							type="textarea" required="required"
																							placeholder="Description" class="form-control"
																							value="${feedbackQuestionVar.description}" />
																					</div>
																				</div>
																				<div class="col-sm-12">
																					<div class="form-group">
																						<label for="type">Question Type</label>
																						<div class="clearfix"></div>
																						<label class="radio-inline"> <form:radiobutton
																								path="feedbackQuestions[${status.index}].type"
																								value="SINGLESELECT"
																								onclick="singleSelectClicked('feedbackQuestionForm-${status.index}')" />
																							Single Select
																						</label>
																						<%-- <label class="radio-inline"> </label>
																						<form:radiobutton
																							path="feedbackQuestions[${status.index}].type"
																							value="MULTISELECT"
																							onclick="multipleSelectClicked('feedbackQuestionForm-${status.index}')" />
																						Multiple Select --%>
																					</div>
																				</div>
																				
																				<div class="col-sm-12 column">
																					<div class="form-group">

																						<button id="submit"
																							class="btn btn-large btn-success"
																							formaction="updateFeedbackQuestion?feedbackId=${feedback.id}">Update
																							Question</button>
																					</div>
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

						<!-- Results Panel -->
						<div class="card bg-white border">
							<div class="card-body">

										<h5 class="border-bottom pb-2">Add New Questions</h5>

										<c:if test="${not empty feedback}">


											
												<form:form action="addFeedbackQuestion" method="post"
													modelAttribute="feedbackQuestion"
													id="addFeedbackQuestionForm">
													<div class="row">
													<div class="col-sm-12">
														<form:input type="hidden" path="feedbackId"
															value="${feedback.id}" />
														<div class="form-group">
															<form:label path="description" for="description">Description<span style="color: red">*</span></form:label>
															<form:input id="description" path="description"
																type="textarea" required="required"
																placeholder="Description" class="form-control" />
														</div>
													</div>
													<div class="col-md-6 col-sm-12">
														<div class="form-group">
															<label for="type">Question Type</label>
															<div class="clearfix"></div>
															<label class="radio-inline"> <form:radiobutton
																	path="type" value="SINGLESELECT" /> Single Select
															</label>
															
														</div>
													</div>
													
													<div class="ccol-md-6 col-sm-12">
														<div class="form-group">

															<button id="submit" class="btn btn-large btn-success"
																formaction="addFeedbackQuestion">Add Question</button>
															<button id="cancel" class="btn btn-danger"
																formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
															<button id="cancel" class="btn btn-danger" type="button"
																formaction="homepage" formnovalidate="formnovalidate"
																onclick="history.go(-1);">Back</button>
														</div>
													</div>
													</div>
												</form:form>

											



										</c:if>
							</div>
						</div>

			<!-- /page content: END -->
                     
                   
                    </div>
			
			<!-- SIDEBAR START -->

	        <!-- SIDEBAR END -->
	<jsp:include page="../common/newAdminFooter.jsp"/>
	


			
