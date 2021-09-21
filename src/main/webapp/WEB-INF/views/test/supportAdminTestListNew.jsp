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

<%@page import="com.spts.lms.web.utils.*"%>
<%@page import="com.spts.lms.web.utils.*"%>
<%@page import="java.util.List"%>
<%@page import="java.util.*"%>
<%@page import="java.text.*"%>
<%@page import="com.spts.lms.beans.test.Test"%>
<%@page import="com.spts.lms.beans.test.StudentTest"%>
<%@page import="com.spts.lms.services.test.StudentTestService"%>


<%
	List<Test> testList = (List<Test>) request.getAttribute("page");
	 List<StudentTest> studentTestList = (List<StudentTest>)request.getAttribute("sTestList");
	Map<Long,StudentTest> mapOfTestIdAndStudentTest = new HashMap<>();
	for(StudentTest st : studentTestList) {
		
		mapOfTestIdAndStudentTest.put(st.getTestId(),st);
	} 
	String status="";
	int countOfTest = 0;
%>

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
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Test/Quiz</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h2>Test/Quiz</h2>
										<div class="clearfix"></div>
									</div>
									<div class="x_content">
										<div class="table_responsive">
											<table class="table table-hover">
												<thead>
													<tr>
														<th>Name</th>
														<th>Term</th>
														<th>Start Date</th>
														<th>End Date</th>
														<th>Test Type</th>
														<th>Status</th>
													</tr>
												</thead>
												<tbody>

													<%
														for (Test test : testList) {
																																																																																																																																																																																																																					countOfTest++;	
																																																																																																																																																																																																																																																																																																																																																																																																											/* 
																																																																																																																																																																																																																																																																																																																																																																																																									    String startDate = Utils.formatDate("yyyy-MM-dd hh:mm:ss",
																																																																																																																																																																																																																																																																																																																																																																																																											"dd-MMM-yy", test.getStartDate().replace('T', ' '));
																																																																																																																																																																																																																																																																																																																																																																																																										  	String endDate = Utils.formatDate("yyyy-MM-dd hh:mm:ss",
																																																																																																																																																																																																																																																																																																																																																																																																										  	"dd-MMM-yy", test.getEndDate().replace('T', ' ')); */
													%>

													<tr>
														<%-- <td><a data-toggle="collapse"
															><!-- <i
																class="pluse_ellipse fa fa-plus"></i>  --><%=test.getTestName()%>
														</a></td> --%>
														<td><c:out value="<%=test.getTestName()%>"/></td>
														<td><c:out value="<%=test.getAcadMonth()%>" /></td>
														<%-- <td><%=startDate%></td>
														<td><%=endDate%></td> --%>
														<td><c:out value="<%=test.getStartDate()%>" /></td>
														<td><c:out value="<%=test.getEndDate()%>" /></td>
														<td><%=test.getTestType()%></td>
														<%
																	if ("Y".equals(test.getTestCompleted())) {
																%>
														<td>Submitted</td>
														<% }else{ %>
														<td>Not Submitted</td>
														<%} %>
												
													</tr>
													
														<%} %>
												</tbody>
											</table>



										</div>




									<c:url value="/checkUserCourse" var="goBack">
													<c:param name="username" value="${username}"></c:param>
													</c:url>
													<a href="${goBack}" class="btn btn-success" >Back</a>
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

	













