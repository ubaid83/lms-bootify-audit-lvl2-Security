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
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page">Response To Query </li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
	
										<h5 class="text-center border-bottom pb-2">Response To Query</h5>
		
										<form:form action="createQueryForm" method="post"
											modelAttribute="query">
											<form:hidden path="id" value="${queryId}" />
											


											<fieldset>
											<div class="row">
													<div class="col-sm-12">
														

														<div class="form-group">
															
															<form:label path="queryDesc" for="queryDesc">Query Description</form:label>
															<form:textarea class="form-control" path="queryDesc"
																id="queryDesc" rows="10" />

														</div>
														
													</div>

														<div class="col-sm-12">
															<div class="form-group">
																
																<form:label path="queryResponse" for="queryResponse">Response</form:label>
																<form:textarea class="form-control" path="queryResponse"
																	id="queryResponse" rows="10" placeholder="ENTER REPLY" />

																<button type="submit" class="btn btn-primary mt-3"
																	formaction="saveQueryReply">Submit</button>
															</div>
														</div>

													</div>
											</fieldset>
										</form:form>
						</div>
						</div>

						<!-- Results Panel -->

			<!-- /page content: END -->
                   
                    </div>
                                                
                                                <!-- SIDEBAR START -->

                        <!-- SIDEBAR END -->
                <jsp:include page="../common/newAdminFooter.jsp"/>




                        
       
                   