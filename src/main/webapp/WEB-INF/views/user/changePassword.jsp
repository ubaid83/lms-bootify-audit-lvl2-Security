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

<sec:authorize access="hasAnyRole('ROLE_EXAM', 'ROLE_IT')">
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/topHeaderLibrian.jsp" />

			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Profile
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Change Password</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
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
												formaction="homepage" formnovalidate="formnovalidate">BACK</button>
										</form:form>
									</div>
								</div>
							</div>
						</div>


					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footerLibrarian.jsp" />

		</div>
	</div>





</body>
</html>

</sec:authorize>

<sec:authorize access="hasAnyRole('ROLE_STUDENT', 'ROLE_FACULTY', 'ROLE_PARENT','ROLE_STAFF')">
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
	<sec:authorize access="hasRole('ROLE_PARENT')">
	<jsp:include page="../common/newLeftNavbar.jsp"/>
		<jsp:include page="../common/newLeftSidebarParent.jsp" />
	</sec:authorize>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newTopHeaderFaculty.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newTopHeader.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_PARENT')">
	<jsp:include page="../common/newTopHeader.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STAFF')">
		<div class="container-fluid m-0 p-0 dashboardWraper">
			<header class="container-fluid sticky-top">
				<nav class="navbar navbar-expand-lg navbar-light p-0">
					<!--                     <i class="fas fa-bars fa-2x mr-3 leftnav-btn" data-toggle="modal" data-target="#leftnav"></i> -->
					<a class="navbar-brand" href="homepage"> <c:choose>
							<c:when test="${instiFlag eq 'nm'}">
								<img src="<c:url value="/resources/images/logo.png" />"
									class="logo" title="NMIMS logo" alt="NMIMS logo" />
							</c:when>
							<c:otherwise>
								<img src="<c:url value="/resources/images/svkmlogo.png" />"
									class="logo" title="SVKM logo" alt="SVKM logo" />
							</c:otherwise>
						</c:choose>

					</a>
					<button class="adminNavbarToggler" type="button"
						data-toggle="collapse" data-target="#adminNavbarCollapse">
						<i class="fas fa-bars"></i>
					</button>

					<div class="collapse navbar-collapse" id="adminNavbarCollapse">
						<ul class="navbar-nav ml-auto">

							<li id="program" class="nav-item active" data-toggle="tooltip"
								data-placement="bottom" title="homepage"><a
								class="nav-link" href="homepage"><i class="fas fa-home"></i>
									<span>Home</span></a></li>

						</ul>
					</div>
				</nav>
			</header>
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
                                <li class="breadcrumb-item active" aria-current="page">Profile</li>
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

						
								
							

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->
			<sec:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_STUDENT', 'ROLE_PARENT')">
	         <jsp:include page="../common/newSidebar.jsp" />
	         </sec:authorize>
	        <!-- SIDEBAR END -->
	<sec:authorize access="hasAnyRole('ROLE_FACULTY','ROLE_STUDENT', 'ROLE_PARENT')">
		<jsp:include page="../common/footer.jsp" />
	</sec:authorize>
	
	<sec:authorize access="hasRole('ROLE_STAFF')">
		<jsp:include page="../common/newAdminFooter.jsp" />
	</sec:authorize>
	
	</sec:authorize>