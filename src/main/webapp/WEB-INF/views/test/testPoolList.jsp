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
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
          <!-- page content: START -->
          
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Test Pool List</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">



									<div class="x_content">
										<form:form action="viewThisTest" method="post"
											modelAttribute="test">



											<c:choose>
												<c:when test="${testPoolsList.size() > 0}">
													<div class="row">
														<div class="col-xs-12 col-sm-12">

															<div class="x_title">
																<h5>
																	Test Pools<font size="2px"> |
																		${testPoolsList.size()} Records Found &nbsp; </font>
																</h5>
																
															</div>
															<div class="x_content">
																<div class="table-responsive">
																	<table class="table table-hover"
																		style="font-size: 12px">
																		<thead>
																			<tr>

																				<th>Test Pool Name</th>

																				<th>Course</th>
																				<sec:authorize
																							access="hasAnyRole('ROLE_FACULTY')">
																				<th>Acad Month</th>
																				<th>Acad Year</th>
																				<th>Acad Session</th>
																				</sec:authorize>
																				<th>Action</th>

																			</tr>
																		</thead>

																		<tfoot>
																			<tr>
																				<th>Test Pool Name</th>

																				<th>Course</th>
																				<sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																				<th>Acad Month</th>
																				<th>Acad Year</th>
																				<th>Acad Session</th>
																				</sec:authorize>
																				<th></th>
																				<!-- <th>Start Time</th>
															<th>End Time</th>

															<th>Absence Reason</th> -->

																			</tr>
																		</tfoot>
																		<tbody>

																			<c:forEach var="testPool" items="${testPoolsList}"
																				varStatus="status">
																				<c:if test="${isTestIdPresent eq true}">
																					<c:url value="viewTestQuestionsByTestPool"
																						var="viewTestQuestionPool">
																						<c:param name="testId">${testId}</c:param>
																						<c:param name="testPoolId">${testPool.id}</c:param>
																					</c:url>
																				</c:if>
																				<tr>
																					<td><c:out value="${testPool.testPoolName}" /></td>

																					<td><c:out value="${testPool.courseName}" /></td>
																					<sec:authorize
																							access="hasAnyRole('ROLE_FACULTY')">
																					<td><c:out value="${testPool.acadMonth}" /></td>
																					<td><c:out value="${testPool.acadYear}" /></td>
																					<td><c:out value="${testPool.acadSession}" /></td>
																					</sec:authorize>
																					<td><c:if test="${isTestIdPresent eq true}">
																							<a href="${viewTestQuestionPool}"
																								title="Configure Questions By Test Pool"><i
																								class="fa fa-question-circle" aria-hidden="true"
																								style="font-size: medium;"></i></a>
																						</c:if> 
																							<sec:authorize
																							access="hasAnyRole('ROLE_FACULTY')">
																						<c:url value="addTestPoolForm" var="editurl">
																							<c:param name="id" value="${testPool.id}" />
																						</c:url>
																						</sec:authorize>
																						<sec:authorize
																							access="hasAnyRole('ROLE_ADMIN')">
																							<c:url value="addTestPoolFormByAdmin" var="editurl">
																							<c:param name="id" value="${testPool.id}" />
																						</c:url>
																						</sec:authorize>
																						 <c:url value="deleteTestPool" var="deleteurl">
																							<c:param name="id" value="${testPool.id}" />
																							
																						</c:url> 
																						<c:url value="uploadTestQuestionPoolForm" var="uploadTestQForTestPool">
																							<c:param name="id" value="${testPool.id}" />
																							
																						</c:url>
																						
																						<c:url value="viewTestQuestionsByTestPool" var="viewTestQuestionsByPool">
																							<c:param name="testPoolId" value="${testPool.id}" />
																							
																						</c:url>
																						
																						<c:url value="downloadTestQuestionPoolByTestPoolId" var="downloadTestQuestionPool">
																							<c:param name="testPoolId" value="${testPool.id}" />
																							
																						</c:url>
																						
																						<c:url value="exportTestPoolForm"
																							var="exportTestPoolUrl">
																							<c:param name="id" value="${testPool.id}" />
																						</c:url>
																						
																						<sec:authorize
																							access="hasAnyRole('ROLE_FACULTY','ROLE_ADMIN')">
																							<a href="${editurl}" title="Edit"><i
																								class="fas fa-pen-square"></i></a>
																							<a href="${deleteurl}" title="Delete"
																								onclick="return confirm('Are you sure you want to delete this record?')"><i
																								class="fas fa-trash"></i></a>
																							<a href="${uploadTestQForTestPool}" title="Upload Test Questions"
																								><i
																								class="fa  fa-upload"></i></a>
																							<a href="${viewTestQuestionsByPool}" title="View & Update Test Questions"
																								><i
																								class="fa fa-info-circle fa-lg"></i></a>
																							
																							<a href="${downloadTestQuestionPool}" title="Download Test Questions"
																								><i
																								class="fa  fa-download"></i></a>&nbsp;
																								
																							<sec:authorize
																							access="hasAnyRole('ROLE_FACULTY')">
																							<a href="${exportTestPoolUrl}" title="Export Test Pool"><i
																								class="fas fa-file-excel"></i></a>
																								</sec:authorize>
																						</sec:authorize></td>
																				</tr>
																			</c:forEach>


																		</tbody>
																	</table>
																</div>
															</div>

														</div>
													</div>
												</c:when>
											</c:choose>



										</form:form>
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
	         	<sec:authorize access="hasRole('ROLE_FACULTY')">
					<jsp:include page="../common/newSidebar.jsp" />
				</sec:authorize>
	        <!-- SIDEBAR END -->
	<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="../common/footer.jsp"/>
			</sec:authorize>
			<sec:authorize access="hasRole('ROLE_ADMIN')">
			<jsp:include page="../common/newAdminFooter.jsp"/>
			</sec:authorize>

	













