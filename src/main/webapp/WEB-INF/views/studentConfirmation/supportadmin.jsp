<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">

	<!-- DASHBOARD BODY STARTS HERE -->

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
							data-placement="bottom" title="My Program"><a
							class="nav-link" href="homepage"><i class="fas fa-home"></i>
								<span>Home</span></a></li>

					</ul>
				</div>
			</nav>
		</header>

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">

			<!-- page content: START -->
			<nav aria-label="breadcrumb">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a
						href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>

					
					<li class="breadcrumb-item active" aria-current="page">Student Master</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
				<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-left pb-2 border-bottom">Program Created By Admin</h5>
					<div class="table-responsive mt-3 mb-3 testAssignTable">
						<table class="table table-striped table-hover">
							<thead>
								<tr>
									<th scope="col">Sr. No.</th>
									<th scope="col">EndDate</th>
									<th scope="col">ProgramName</th>
									<th scope="col">Campus</th>
									<th scope="col">Semester</th>
									<th scope="col">createdBy</th>
									<th scope="col">Email Alert</th>
								<!-- 	<th scope="col">Created By</th>
									<th scope="col">Email Alert</th> -->
								</tr>
							</thead>
							<tbody>
							
								<c:forEach var="tee" items="${activeprogramList}" varStatus="status">
									<tr>
										<td><c:out value="${status.count}" /></td>
										<td><c:out value="${tee.endDate}" /></td>
										<td><c:out value="${tee.programName}" /></td>
										<td><c:out value="${tee.campusId}" /></td>
										<td><c:out value="${tee.acadSession}" /></td>
										<td><c:out value="${tee.createdBy}" /></td>
										<td><c:out value="${tee.sendEmailAlert}" /></td>
									<%-- 	<td><c:out value="${tee.facultyName}" /></td>
										<td><c:out value="${tee.facultyName}" /></td> --%>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
				</div>
			
			
			
			
			
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-left pb-2 border-bottom">Students List</h5>



					<div class="table-responsive mt-3 mb-3 testAssignTable">
						<table class="table table-striped table-hover">
							<thead>
								<tr>
									<th scope="col">Sl. No.</th>
									<th scope="col">SapId</th>
									<th scope="col">FirstName</th>
									<th scope="col">FatherName</th>
									<th scope="col">Mother Name</th>
									<th scope="col">Program Name</th>
									<th scope="col">Semester</th>
									<!-- <th scope="col">UserImage</th> -->
									<th scope="col">Action</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="list" items="${studentList}" varStatus="status">
									<tr>
										<td><c:out value="${status.count}" /></td>
										<td><c:out value="${list.username}" /></td>
										<td>
										<c:choose>
									    <c:when test="${list.firstname ne null}">
									        <c:out value="${list.firstname}" />
									    </c:when>    
									    <c:otherwise>
									        Agree 
									        <br />
									    </c:otherwise>
									</c:choose>
										</td>
										<td>
										<c:choose>
									    <c:when test="${list.fathername ne null}">
									        <c:out value="${list.fathername}" />
									    </c:when>    
									    <c:otherwise>
									        Agree 
									        <br />
									    </c:otherwise>
									</c:choose>
									</td>
										<td>
										<c:choose>
									    <c:when test="${list.mothername ne null}">
									        <c:out value="${list.mothername}" />
									    </c:when>    
									    <c:otherwise>
									        Agree 
									        <br />
									    </c:otherwise>
									</c:choose>
									</td>
									<%-- <td><img
							src="${pageContext.request.contextPath}/savedImages/${list.username}.jpg"
							onerror="this.src='<c:url value="/resources/images/img-user.png" />'"
							class="ms-student-img" title="${userBean.firstname}" alt="Name of the user" /></td> --%>
									<td><c:out value="${list.programName}" /></td>
									<td><c:out value="${list.acadSession}" /></td>
										<td><a href="studentMasterReuploadPhoto?username=${list.username}"
															title="Re-upload Photo"><i class="fas fa-camera"></i></a>  | | 
															
															
											<a href="studentMasterNamechange?username=${list.username}"
															title="Apply For Name Chnage"><i class="fas fa-user-edit"></i></a>				
															</td>

									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>



			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />
