<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
	
	
	
	
	<jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex adminPage" id="adminPage">
<jsp:include page="../common/newAdminLeftNavBar.jsp"/>
<jsp:include page="../common/rightSidebarAdmin.jsp" />


<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

                <jsp:include page="../common/newAdminTopHeader.jsp" />
     
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
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
										<form:label path="username" for="username"><strong>User Name :</strong></form:label>
										${user.username }
									</div>
								</div>

								<div class="col-md-4 col-sm-6 mt-3">
									<div class="form-group">
										<form:label path="firstname" for="firstname"><strong>First Name :</strong></form:label>
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
										<form:label path="email" for="email"><strong>Email Id :</strong></form:label>
										${user.email}
									</div>
								</div>
								
								<div class="col-md-4 col-sm-6 mt-3">
									<div class="form-group">
										<form:label path="mobile" for="mobile"><strong>Mobile :</strong></form:label>
										${user.mobile}
									</div>
								</div>
								
								<div class="col-md-4 col-sm-6 mt-3">
									<div class="form-group">
										<form:label path="" for=""><strong>Program Name :</strong></form:label>
										${programName}
									</div>
								</div>

							</div>

							<div class="form-row ml-2">

								<sec:authorize access="hasRole('ROLE_STUDENT')">
								<div class="col-md-4 col-sm-6 mt-3">
									<div class="form-group">
										<form:label path="fatherName" for="fatherName"><strong>Father Name :</strong></form:label>
										${user.fatherName}
									</div>
								</div>

								<div class="col-md-4 col-sm-6 mt-3">
									<div class="form-group">
										<form:label path="motherName" for="motherName"><strong>Mother Name :</strong></form:label>
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

                        <!-- SIDEBAR END -->
                <jsp:include page="../common/newAdminFooter.jsp"/>
	
	
	
	
	
					


