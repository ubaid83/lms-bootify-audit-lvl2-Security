<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<sec:authorize access="hasAnyRole('ROLE_STUDENT', 'ROLE_FACULTY')">
<jsp:include page="../common/newDashboardHeader.jsp" />
<div class="d-flex" id="prilfeDetailsPage">
    
    <sec:authorize access="hasRole('ROLE_FACULTY')">
    <jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>
    </sec:authorize>

<sec:authorize access="hasRole('ROLE_STUDENT')">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />
	
		<jsp:include page="../common/newLeftSidebar.jsp" />
	</sec:authorize>
	
	<sec:authorize access="hasRole('ROLE_PARENT')">
	<jsp:include page="../common/newLeftNavbar.jsp"/>
		<jsp:include page="../common/newLeftSidebarParent.jsp" />
	</sec:authorize>
	


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper assignmentW">
	 <sec:authorize access="hasRole('ROLE_FACULTY')">
	  <jsp:include page="../common/newTopHeaderFaculty.jsp" />
	 </sec:authorize>
	 
	 <sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newTopHeader.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_PARENT')">
	<jsp:include page="../common/newTopHeader.jsp" />
	</sec:authorize>

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto mb-3">
					<jsp:include page="../common/alert.jsp" />
					<div class="bg-white pb-5">
						<nav class="" aria-label="breadcrumb">
							<ol class="breadcrumb d-flex">
								<li class="breadcrumb-item"><a
									href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
								<li class="breadcrumb-item active" aria-current="page">Profile
								</li>
							</ol>

						</nav>

						<form:form action="profileDetails" id="" method="post"
							modelAttribute="user">

							<form:input path="username" type="hidden" />
							<form:input path="id" type="hidden" />

							<div class="form-row ml-2">
								<div class="col-md-4 col-sm-6 mt-3">
									<div class="form-group">
										<form:label path="username" for="username"><strong>User Name:</strong></form:label>
										${user.username }
									</div>
								</div>

								<div class="col-md-4 col-sm-6 mt-3">
									<div class="form-group">
										<form:label path="firstname" for="firstname"><strong>First Name:</strong></form:label>
										${user.firstname }
									</div>
								</div>

								<div class="col-md-4 col-sm-6 mt-3">
									<div class="form-group">
										<form:label path="lastname" for="lastname"><strong>Last Name:</strong></form:label>
										${user.lastname }
									</div>
								</div>

							</div>

							<div class="form-row ml-2">
							

								<div class="col-md-4 col-sm-6 mt-3">
									<div class="form-group">
										<form:label path="email" for="email"><strong>Email Id:</strong></form:label>
										${user.email}
									</div>
								</div>
								
								<div class="col-md-4 col-sm-6 mt-3">
									<div class="form-group">
										<form:label path="mobile" for="mobile"><strong>Mobile:</strong></form:label>
										${user.mobile}
									</div>
								</div>
								
								<div class="col-12">
									<div class="form-group">
										<form:label path="" for=""><strong>Program Name:</strong></form:label>
										${programName}
									</div>
								</div>

							</div>
							<hr/>

							<div class="form-row ml-2">

								<sec:authorize access="hasRole('ROLE_STUDENT')">
								<div class="col-md-4 col-sm-6 mt-3">
									<div class="form-group">
										<form:label path="fatherName" for="fatherName"><strong>Father Name:</strong></form:label>
										${user.fatherName}
									</div>
								</div>

								<div class="col-md-4 col-sm-6 mt-3">
									<div class="form-group">
										<form:label path="motherName" for="motherName"><strong>Mother Name:</strong></form:label>
										${user.motherName}
									</div>
								</div>
								</sec:authorize>

								

							</div>

							<br>
							<sec:authorize access="hasRole('ROLE_STUDENT')">
								<button id="submit" class="btn btn-large btn-primary ml-2"
									formaction="updateProfileForm">Update Profile</button>
							</sec:authorize>
							<button id="cancel" class="btn btn-danger ml-2" formaction="homepage"
								formnovalidate="formnovalidate">Back</button>
						</form:form>
					</div>




				</div>
				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
				</sec:authorize>
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
										<h2>Profile Details</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="profileDetails" id="" method="post"
											modelAttribute="user">

											<form:input path="username" type="hidden" />
											<form:input path="id" type="hidden" />

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="username" for="username">User Name :</form:label>
														${user.username }
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="firstname" for="firstname">First Name :</form:label>
														${user.firstname }
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="lastname" for="lastname">Last Name:</form:label>
														${user.lastname }
													</div>
												</div>

											</div>

											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="fatherName" for="fatherName">Father Name :</form:label>
														${user.fatherName}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="motherName" for="motherName">Mother Name :</form:label>
														${user.motherName}
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="email" for="email">Email Id :</form:label>
														${user.email}

													</div>
												</div>

											</div>


											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="mobile" for="mobile">Mobile :</form:label>
														${user.mobile}
													</div>
												</div>
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="" for="">Program Name :</form:label>
														${programName}
													</div>
												</div>


											</div>

											<br>
											<!-- <button id="submit" class="btn btn-danger"
													formaction="saveProfileDetails" formnovalidate="formnovalidate">Save Details</button> -->
											<sec:authorize access="hasRole('ROLE_STUDENT')">
												<button id="submit" class="btn btn-large btn-primary"
													formaction="updateProfileForm">Update Profile</button>
											</sec:authorize>
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