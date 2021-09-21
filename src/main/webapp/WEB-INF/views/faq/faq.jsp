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
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newTopHeader.jsp" />
     
     <div class="container mt-5">
                <div class="row">
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
          <!-- page content: START -->
          
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> FAQ</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5 class="text-center pb-2 border-bottom">Frequently Asked Questions</h5>
			
									</div>



									<div class="x_content">
										<div class="container">


											<div class="panel-group" id="accordion1">

												<c:choose>
													<c:when test="${admissionsFAQs.size() > 0}">

														<div class="panel panel-default">
															<div class="panel-heading">
																<h4 class="panel-title" data-toggle="collapse" data-parent="#accordion1" class="accordion-toggle"
																		 data-target="#collapse1">
																	Admissions
																</h4>

															</div>
															<div id="collapse1" class="panel-collapse collapse">

																<div class="panel-body">

																	<div class="panel-body">


																		<div class="panel-group" id="accordion11">
																			<c:forEach items="${admissionsFAQs}" var="admission"
																				varStatus="status">


																				<div class="panel">
																					<a class="text-dark" data-toggle="collapse"
																						data-parent="#accordion11"
																						style="font-size: 17px;"
																						href="#collapse1${status.index}"><strong>Q${status.index+1}.${admission.question}</strong>
																					</a>
																					<div id="collapse1${status.index}"
																						class="panel-collapse collapse">
																						<div class="panel-body"><p>${admission.answer}</p></div>
																					</div>
																				</div>
																			</c:forEach>

																		</div>


																	</div>
																</div>

															</div>
														</div>
													</c:when>
												</c:choose>
												<c:choose>
													<c:when test="${academicsFAQs.size() > 0}">


														<div class="panel panel-default">
															<div class="panel-heading">
																<h4 class="panel-title">
																	<a data-toggle="collapse" data-parent="#accordion1"
																		style="font-size: 20px;" href="#collapse2">Academics</a>
																</h4>
															</div>
															<div id="collapse2" class="panel-collapse collapse">
																<div class="panel-body">
																	<div class="panel-body">


																		<div class="panel-group" id="accordion12">
																			<c:forEach items="${academicsFAQs}" var="academic"
																				varStatus="status">
																				<div class="panel">
																					<a class="text-center" data-toggle="collapse"
																						data-parent="#accordion12"
																						style="font-size: 17px;"
																						href="#collapse2${status.index}"><strong>Q${status.index+1}.${academic.question}</strong>
																					</a>
																					<div id="collapse2${status.index}"
																						class="panel-collapse collapse">
																						<div class="panel-body"><p>${academic.answer}</p></div>
																					</div>
																				</div>
																			</c:forEach>
																		</div>


																	</div>
																</div>
															</div>
														</div>
													</c:when>
												</c:choose>
												<c:choose>
													<c:when test="${examsFAQs.size() > 0}">


														<div class="panel panel-default">
															<div class="panel-heading">
																<h4 class="panel-title">
																	<a class="text-dark" data-toggle="collapse" data-parent="#accordion1"
																		style="font-size: 20px;" href="#collapse3">Exams</a>
																</h4>
															</div>
															<div id="collapse3" class="panel-collapse collapse">
																<div class="panel-body">
																	<div class="panel-body">

																		<div class="panel-group" id="accordion13">
																			<c:forEach items="${examsFAQs}" var="exams"
																				varStatus="status">

																				<div class="panel">
																					<a class="text-dark" data-toggle="collapse"
																						data-parent="#accordion13"
																						style="font-size: 17px;"
																						href="#collapse3${status.index}"><strong>Q${status.index+1}.${exams.question}</strong>
																					</a>
																					<div id="collapse3${status.index}"
																						class="panel-collapse collapse">
																						<div class="panel-body"><p>${exams.answer}</p></div>
																					</div>
																				</div>
																			</c:forEach>
																		</div>


																	</div>
																</div>
															</div>
														</div>
													</c:when>
												</c:choose>
												<c:choose>
													<c:when test="${othersFAQs.size() > 0}">



														<div class="panel panel-default">
															<div class="panel-heading">
																<h4 class="panel-title">
																	<a class="text-dark" data-toggle="collapse" data-parent="#accordion1"
																		style="font-size: 20px;" href="#collapse4">Others</a>
																</h4>
															</div>
															<div id="collapse4" class="panel-collapse collapse">
																<div class="panel-body">
																	<div class="panel-body">


																		<div class="panel-group" id="accordion14">
																			<c:forEach items="${othersFAQs}" var="others"
																				varStatus="status">
																				<div class="panel">
																					<a class="text-dark" data-toggle="collapse"
																						data-parent="#accordion14"
																						style="font-size: 17px;"
																						href="#collapse4${status.index}"><strong>Q${status.index+1}.${others.question}</strong>
																					</a>
																					<div id="collapse4${status.index}"
																						class="panel-collapse collapse">
																						<div class="panel-body"><p>${others.answer}</p></div>
																					</div>
																				</div>
																			</c:forEach>
																		</div>


																	</div>
																</div>
															</div>
														</div>
													</c:when>
												</c:choose>
												<c:choose>
													<c:when test="${supportFAQs.size() > 0}">


														<div class="panel panel-default">
															<div class="panel-heading">
																<h4 class="panel-title">
																	<a class="text-dark" data-toggle="collapse" data-parent="#accordion1"
																		style="font-size: 20px;" class="accordion-toggle"
																		href="#collapse5">Student Support</a>
																</h4>
															</div>
															<div id="collapse5" class="panel-collapse collapse">
																<div class="panel-body">
																	<div class="panel-body">

																		<div class="panel-group" id="accordion15">
																			<c:forEach items="${supportFAQs}" var="support"
																				varStatus="status">

																				<div class="panel">
																					<a class="text-dark" data-toggle="collapse"
																						data-parent="#accordion15"
																						style="font-size: 17px;"
																						href="#collapse5${status.index}"><strong>Q${status.index+1}.${support.question}</strong>
																					</a>
																					<div id="collapse5${status.index}"
																						class="panel-collapse collapse">
																						<div class="panel-body"><p>${support.answer}</p></div>
																					</div>
																				</div>
																			</c:forEach>
																		</div>


																	</div>
																</div>
															</div>
														</div>
													</c:when>
												</c:choose>












											</div>

										</div>
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

