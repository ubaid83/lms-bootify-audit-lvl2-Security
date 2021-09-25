
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



							<div class="card bg-white border">
								<div class="card-body">
									<h5 class="text-center border-bottom pb-2">Change Password</h5>


									<div class="x_panel">
										<form:form action="changePasswordBySupportAdmin" id=""
											method="post" modelAttribute="user">

											<%-- <form:input path="username" type="hidden" />
											<form:input path="id" type="hidden" /> --%>
											<jsp:include page="../common/alert.jsp" />

											<div class="row">
												<div class="col-md-4 col-12">
													<div class="form-group">
														<label><i class="fa fa-key"></i> <span
															style="color: red">* Enter SAP Id</span></label>
														<form:input type="text" path="username"
															cssClass="form-control" placeholder="Enter SAPID"
															id="username" required="required" />


													</div>
												</div>



											</div>

											<div class="col-12 text-left">
												<button id="submit" name="submit" class="btn btn-danger">Change
													Password</button>

												<button id="cancel" class="btn btn-danger"
													formaction="homepage" formnovalidate="formnovalidate">BACK</button>
											</div>
						
										</form:form>
									</div>
									


								</div>
							</div>
							<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Check User List</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="searchUserBySupportAdmin" method="post"
											modelAttribute="user">
											<fieldset>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="username" for="username">Search User </form:label>
														<form:input id="username" path="username" class="form-control"
														placeholder="User SapId" required="required"/>
													</div>
												</div>



												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-primary">Search</button>
														<!-- <input id="reset" type="reset" class="btn btn-danger"> -->
														<button id="cancel" name="cancel" class="btn btn-danger"
															formaction="homepage"
															formnovalidate="formnovalidate">Cancel</button>
															
															<c:choose>
																<c:when test="${not empty userList}"> 
																	<div class="" >																  	 
																     <a href="changeTemporaryPasswordBySupportAdmin?username=${user.username}" class="btn btn-large btn-primary">
																     Temporary Password</a>	
																     <a href="deleteTemporaryPasswordBySupportAdmin?username=${user.username}" class="btn btn-large btn-primary">
																     Reset</a>	
																     <c:url  value="/changeTemporaryPasswordBySupportAdmin" var="unlockUser">
																	     <c:param name="username" value="${user.username}"/>
																	     <c:param name="isUserBlocked" value="${userList.isUserBlocked}"/>
																     </c:url>	
																     <c:if test="${not empty userList.isUserBlocked && userList.isUserBlocked eq 10 }">
																     <a href="${unlockUser}" class="btn btn-large btn-primary">Unlock User</a>	
																     </c:if>									
																     </div> 																     
																</c:when>
														  		<c:otherwise> 
														  		</c:otherwise>
														</c:choose>
													</div>
												</div>
											</fieldset>
										</form:form>
									</div>
								</div>
								
								<div class="x_panel">

									<div class="x_title">
										<h2>User List</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<div class="table-responsive">
													<table id="showUsers" class="table table-hover ">
														<thead>
															<tr>
																
																<th>Username</th>
																<th>First Name</th>
																<th>Last Name</th>
																<th>Enabled</th>
																<th>Email</th>
																<th>Mobile Number</th>
																<th>Campus Id</th>
																<th>Campus Name</th>
																<th>DeReg Status</th>
																<th>DeReg Reason</th>
																
															</tr>
														</thead>
														<tbody>
															<%-- <c:forEach var="userList" items="${userList}"
																varStatus="status"> --%>
																<tr>
																	
																	<td>${userList.username}</td>
																	<td>${userList.firstname}</td>
																	<td>${userList.lastname}</td>
																	<%-- <c:if test="${userList.enabled eq true}">
																	<td>False</td>
																	</c:if>
																	<c:if test="${userList.enabled eq false}">
																	<td>True</td>
																	</c:if> --%>
																	<td>${userList.enableS}</td>
																	<td>${userList.email}</td>
																	<td>${userList.mobile}</td>
																	<td>${userList.campusId}</td>
																	<td>${userList.campusName}</td>
																	<td>${userList.deRegStatus}</td>
																	<td>${userList.deRegReason}</td>

																</tr>

																<%-- </c:forEach> --%>
														</tbody>
													</table>
												</div>
									</div>
									<div class="alert alert-dismissible" role="alert">${note}</div>
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
	$(document).ready(
			function() {
				var cars = [ "bgcolor1", "bgcolor2", "bgcolor3", "bgcolor4",
						"bgcolor5", "bgcolor6", "bgcolor7", "bgcolor8",
						"bgcolor9" ];
				var count = 0;
				$('[id^=courseDetail]').each(function() {
					if (count == cars.length - 1) {
						count = 0;
					}
					$(this).addClass(cars[count]);
					count++;
				})

				$('body').addClass("dashboard_left");
			});
</script>



