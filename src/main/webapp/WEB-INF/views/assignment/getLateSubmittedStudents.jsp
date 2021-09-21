<jsp:include page="../common/topHeaderLibrian.jsp" />
<jsp:include page="../common/css.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<body class="nav-md footer_fixed dashboard_left">

	<!-- Example row of columns -->
	<div class="loader"></div>
	<div class="container body">
		<div class="main_container">


			<!--  -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<!-- <div class="dash-main"> -->
					<div class="dashboard_height" id="main">


						<div class="dashboard_contain_specing dash-main">

<jsp:include page="../common/alert.jsp" />

							<div class="row">
							<div class="col-xs-12 col-sm-12">
							
							
					
								

								
								
								<div class="x_panel">
<form:form>
<button id="Back" name="cancel" class="btn btn-danger"formaction="getAssignmentStatus">Back</button>
</form:form>

									<div class="x_title">
										<h2>Student List</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<div class="table-responsive">
											<form:form action="updateApprovalStatusBySupportAdmin?assignmentId=${assignmentId}" method="post" modelAttribute="assign"> <!-- Peter 15/07/2021 added checkbox -->
													<table id="showUsers" class="table table-hover ">
														<thead>
															<tr>
																<th>Username</th>
																<th>Student Name</th>
																<th>Roll No</th>
																<th>Submission Date</th>
																<th><input name="select_all" value="1"
																	id="checkAll" type="checkbox" /></th>
															</tr>
														</thead>
														<tbody>
															<c:forEach var="assignmentList" items="${assignmentList}"
																varStatus="status">
																<tr>
																	<td>${assignmentList.username}</td>
																	<td>${assignmentList.studentName}</td>
																	<td>${assignmentList.rollNo}</td>
																	<td>${assignmentList.submissionDate}</td>
																	<td>
																		<form:checkbox path="students" value="${assignmentList.username}" />
																	</td>
																</tr>
															</c:forEach>
															<tr>
																<td></td>
																<td></td>
																<td></td>
																<td></td>
																<td>
																	<input class="btn btn-large btn-primary" type="submit" value="Approve"/>
																</td>
															</tr>
														</tbody>
													</table>
											</form:form>
												</div>
									</div>
								
								</div>
								
							
							
						</div>
						</div>
					</div>
				</div>


			</div>
		</div>
	</div>
	</div>
</body>

<jsp:include page="../common/footerLibrarian.jsp" />


<script>
	$("#checkAll").click(function(){
	    $('input:checkbox').not(this).prop('checked', this.checked);
	})		
</script>




