<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex adminPage" id="facultyAssignmentPage">
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	<jsp:include page="../common/rightSidebarAdmin.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newAdminTopHeader.jsp" />

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12  my-5">
			<!-- <h2 class="text-center pb-2 border-bottom mb-5">WELCOME TO ADMIN
				DASHBOARD!</h2> -->

			<jsp:include page="../common/alert.jsp" />
	






			<div class="card rounded-0 my-5">
				<h5 class="my-3 pl-3 text-dark">Registered Student</h5>
				<div class="card-body">
					<div class="table-responsive">

						<table class="table table-striped table-hover"
							style="font-size: 12px" id="contentTree">
							<thead>
								<tr>
								<th>Sr No.</th>
									<th>Username</th>
									<th>Photo</th>
								</tr>
							</thead>
							<tbody>
							
							<c:forEach var="userList" items="${StudentMastreStudentList}" varStatus="status">
							<tr>
								<td>${status.count}</td>
								<td>${userList.username}</td>
								<td><img
									src="data:image/png;base64, ${userList.userImage}"
									height="100px" alt="" /></td>
							</tr>
							</c:forEach>
							</tbody>
						</table>
						<c:if test="${size == 0}">
					No Content under this folder
				</c:if>
					</div>
				</div>
			</div>


		</div>
	</div>





</div>



<jsp:include page="../common/newAdminFooter.jsp" />


