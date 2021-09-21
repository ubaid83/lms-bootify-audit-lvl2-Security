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
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Add Offline Test</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>
											<%
												if ("true".equals((String) request.getAttribute("edit"))) {
											%>
											Edit Offline Test
											<%
												} else {
											%>
											Add Offline Test
											<%
												}
											%>
										</h5>
										
									</div>

									<div class="x_content">
										<form:form action="addOfflineTest" method="post"
											modelAttribute="offlineTest">
											<form:input type="hidden" path="courseId" value="${offlineTest.courseId}"/>

											<div class="row">
												<div class="col-sm-4 column">
													<%
														if ("true".equals((String) request.getAttribute("edit"))) {
													%>
													<form:input type="hidden" path="id" />
													
													<%
														}
													%>
													<div class="form-group">
														<form:label path="testName" for="testName">Test Name</form:label>
														<form:input id="testName" path="testName" type="text"
															placeholder="Add Offline Test Name" class="form-control"
															required="required" />
													</div>
												</div>
											</div>
											<div class="row">

												<div class="col-sm-12 column">
													<div class="form-group">
														<%
															if ("true".equals((String) request.getAttribute("edit"))) {
														%>
														<button id="submit" class="btn btn-large btn-primary"
															formaction="updateOfflineTest">Update</button>
														<%
															} else {
														%>
														<button id="submit" class="btn btn-large btn-primary"
															formaction="addOfflineTest">Add</button>
														<%
															}
														%>
														<button id="cancel" class="btn btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
													</div>
												</div>

											</div>


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
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>















