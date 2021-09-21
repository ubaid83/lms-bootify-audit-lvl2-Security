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
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page">Change Password</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Change Password</h5>
										
									</div>

									<div class="x_content">
										<form:form action="changePassword" id="" method="post"
											modelAttribute="user">

											<form:input path="username" type="hidden" />
											<form:input path="id" type="hidden" />

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">

														<form:password path="oldPassword" cssClass="form-control"
															placeholder="Current Password" id="oldPassword"
															required="required" />
														<label><i class="fa fa-key"></i> <span style="color: red">*</span></label>

													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:password path="newPassword" cssClass="form-control"
															placeholder="Enter New Password" id="password"
															required="required" />
														<label><i class="fa fa-lock"></i> <span style="color: red">*</span></label>
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:password path="reenterPassword"
															name="reenterPassword"
															placeholder="Re Enter New Password" id="reenterPassword"
															class="form-control" required="required" />
														<label><i class="fa fa-lock"></i> <span style="color: red">*</span></label>
													</div>
												</div>

											</div>



											<br>
											<button id="submit" class="btn btn-danger"
												formaction="changePassword" formnovalidate="formnovalidate">Change
												Password Details</button>

											<button id="cancel" class="btn btn-danger"
												formaction="homepage" formnovalidate="formnovalidate">Back</button>
										</form:form>
									</div>
								</div>
							</div>
						</div>
						</div>

						<!-- Results Panel -->
			<!-- /page content: END -->
                     
                   
                    </div>
                                                
                                                <!-- SIDEBAR START -->

                        <!-- SIDEBAR END -->
                <jsp:include page="../common/newAdminFooter.jsp"/>


                        
