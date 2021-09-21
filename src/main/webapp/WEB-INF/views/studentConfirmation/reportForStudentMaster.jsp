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

    <div class="d-flex adminPage tabPage dataTableBottom" id="adminPage">
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
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Master Validation Report </li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Generate Student Master Validation Report Program-wise</h5>
										
									</div>

									<div class="x_content">
										<form:form action="" method="post" modelAttribute="studentDetailConfirmation"
											enctype="multipart/form-data">
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="programId" for="programId">Program</form:label>
														<form:select id="programIdForForum" path="programId"
															class="form-control">
															<form:option value="">Select Program</form:option>
															<c:forEach var="prog" items="${allPrograms}"
																varStatus="status">
																<form:option value="${prog.id}" title="${prog.programName}">${prog.programName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column pt-4">
													<button id="submit" class="btn btn-info"
													formaction="generateReportForPhotoSubmittedProgramWise">Generate Report</button>
												</div>
											</div>
										</form:form>
									</div>
								</div>
							</div>
						</div>
						</div>

							

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->
                   
                  
                        </div>

                <!-- SIDEBAR START -->

                <!-- SIDEBAR END -->
        <jsp:include page="../common/newAdminFooter.jsp"/>
        
       


	


