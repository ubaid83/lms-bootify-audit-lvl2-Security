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
<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newTopHeaderFaculty.jsp" />
     
     <div class="container mt-5">
                <div class="row">
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
          <!-- page content: START -->
          
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                            <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/viewTestDetailsToEvaluate?testId=${TestId}">Back</a></li>	
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Evaluate Test</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Evaluate Test</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<c:if test="${not empty test.testQuestions}">

											<div class="panel-group" id="testQuestionAccordion"
												role="tablist" aria-multiselectable="true">
												<c:forEach items="${test.testQuestions}"
													var="testQuestionVar" varStatus="status">
													<div class="panel panel-default">
														<div class="panel-heading" role="tab"
															id="heading-${status.index}">
															<h4 class="panel-title">
																<a data-toggle="collapse"
																	data-parent="#testQuestionAccordion"
																	href="#collapse-${status.index}" aria-expanded="false"
																	aria-controls="collapse-${status.index}"><strong>Q${status.index+1}.
																		${testQuestionVar.description}</strong></a> (Marks :
																${testQuestionVar.marks})
																<c:if test="${not empty testQuestionVar.studentMarks}">
																Evaluated Marks : ${testQuestionVar.studentMarks}
																</c:if>
															</h4>
														</div>
														<div id="collapse-${status.index}"
															class="panel-collapse collapse" role="tabpanel"
															aria-labelledby="heading-${status.index}">
															<div class="panel-body">
																<div class="row">
																	<form:form  method="post"
																		modelAttribute="test"
																		id="testQuestionForm-${status.index}">
																		<div class="col-sm-12">
																			<form:input type="hidden"
																				path="testQuestions[${status.index}].id"
																				value="${testQuestionVar.id}" />
																			<div class="form-group">
																				<form:label
																					path="testQuestions[${status.index}].description"
																					for="description">Description</form:label>
																				<%-- <form:input id="description"
																					path="testQuestions[${status.index}].description"
																					type="textarea" required="required"
																					placeholder="Description" class="form-control"
																					value="${testQuestionVar.description}" /> --%>
																				<form:textarea class="form-group testDesc ckeditorClass"
																					readonly="${disabled}" 
																					path="testQuestions[${status.index}].description"
																					name="description" id="editor${status.index}"
																					rows="10" cols="80" disabled="true"/>
																			</div>
																		</div>
																		<div class="col-sm-6">
																			<div class="form-group">
																				<form:label
																					path="testQuestions[${status.index}].marks"
																					for="marks">Marks <span style="color: red">*</span></form:label>
																				<%-- <form:input id="marks"
																					path="testQuestions[${status.index}].marks"
																					type="number" required="required"
																					placeholder="Marks" class="form-control"
																					value="${testQuestionVar.marks}" /> --%>
																						<c:choose>
																			 <c:when test="${not empty testQuestionVar.studentMarks}">
																					<form:input id="marks"
																					path="testQuestions[${status.index}].marks"
																					type="number" required="required"
																					placeholder="Marks" class="form-control"
																					value="${testQuestionVar.studentMarks}" />
																			</c:when>
																			<c:otherwise>
																				<form:input id="marks"
																					path="testQuestions[${status.index}].marks"
																					type="number" required="required"
																					placeholder="Marks" class="form-control"
																					value="0" />
																			</c:otherwise>
																			
																			</c:choose>
																					
																			</div>
																		</div>
																		
																		<div class="col-sm-12">
																			 <form:input type="hidden"
																				path="testQuestions[${status.index}].id"
																				value="${testQuestionVar.id}" /> 
																				
																				
																			<div class="form-group">
																				<form:label
																					path="testQuestions[${status.index}].studentQuestionResponse.answer"
																					for="answer">Answer <span style="color: red">*</span></form:label>
																				<%-- <form:input id="answer"
																					path="testQuestions[${status.index}].studentQuestionResponse.answer"
																					type="textarea" required="required"
																					placeholder="Answer" class="form-control"
																					value="${testQuestionVar.studentQuestionResponse.answer}"/> --%>
																					
																					<form:textarea  cols="40" rows="7"  id="answer"
																					path="testQuestions[${status.index}].studentQuestionResponse.answer"
																					type="textarea" required="required"
																					placeholder="Answer" class="form-control"
																					value="${testQuestionVar.studentQuestionResponse.answer}" 
																					disabled="true"/>

																			</div>
																		</div>
																	
																		<div class="col-sm-12 column">
																			<div class="form-group">

																				
																					
																					<button id="submit" class="btn btn-large btn-primary"
														formaction="addStudentMarks?testQuestionId=${testQuestionVar.id}&studusername=${studusername}">Add
														Marks</button>
																			</div>
																		</div>
																	</form:form>
																</div>
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

						
							

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>

	

<!-- <script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script> -->


<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.4/MathJax.js?config=TeX-AMS_HTML"></script>

<script src="<c:url value="/resources/ckeditor/ckeditor.js" />"></script>  

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









