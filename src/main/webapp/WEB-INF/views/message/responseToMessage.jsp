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
		<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
		<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
		<jsp:include page="../common/rightSidebarFaculty.jsp"/>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftNavbar.jsp" />
		<jsp:include page="../common/newLeftSidebar.jsp" />
		<jsp:include page="../common/rightSidebar.jsp" />
	</sec:authorize>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newTopHeaderFaculty.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newTopHeader.jsp" />
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
                                <li class="breadcrumb-item active" aria-current="page"> Response To Message</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Response To Message</h5>
										
									</div>

									<div class="x_content">
										<form:form action="createMessageForm" method="post"
											modelAttribute="message">
											<form:hidden path="id" value="${messageId}" />
											<form:hidden path="courseId" value="${messageCourse}" />


											<fieldset>
												<div class="row">


													<div class="col-sm-12 column">
														<div class="form-group">
															<!--<form:label path="description" for="description">Example textarea</form:label>-->
															<form:label path="subject" for="subject">Message Subject</form:label>
															<form:textarea class="form-control" path="subject"
																id="description" rows="2" readonly="true"/>

														</div>

														<div class="form-group">
															<!--<form:label path="description" for="description">Example textarea</form:label>-->
															<form:label path="description" for="description">Message Description</form:label>
															<form:textarea class="form-control" path="description"
																id="description" rows="2" readonly="true" />

														</div>

														
															<div class="form-group">
																<!--<form:label path="description" for="description">Example textarea</form:label>-->
																<form:label path="messageReply" for="messageReply">Reply</form:label>
																<form:textarea class="form-control" path="messageReply"
																	id="messageReply" rows="10" placeholder="ENTER REPLY" />

																<button type="submit" class="btn btn-primary mt-3"
																	formaction="saveMessageReply">Submit</button>
															
														</div>

													</div>
											</fieldset>
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

	













