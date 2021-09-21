
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
									<h5 class="text-center border-bottom pb-2">Hostel Student Details</h5>
								</div>
							</div>
							
							<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Check Hostel Student Detail List</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="searchHostelStudentDetailBySupportAdmin" method="post"
											modelAttribute="hostelUser">
											<fieldset>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="appId" for="appId">Search Hostel Student </form:label>
														<form:input id="appId" path="appId" class="form-control"
														placeholder="Hostel Student SAP-Id" required="required"/>
													</div>
												</div>
												<div class="col-sm-12 column">
													<div class="form-group">													
														<button id="submit" name="submit"
														class="btn btn-large btn-primary">Search</button>
														
														<button id="cancel" name="cancel" class="btn btn-danger"
														formaction="homepage" formnovalidate="formnovalidate">Cancel</button>													
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
																
																<th>id</th>
																<th>username</th>
																<th>location</th>
																<th>hostelName</th>
																<th>name</th>													
																<th>studentImage</th>
																<th>studentSign</th>
																<th>parentSign</th>
																<th>proofPath</th>
																<th>Verification Status</th>
																<th>Verification Mail Status</th>
																<th>Allotment status </th>
																<th>Allotment Mail status</th>
																<th>payment</th>													
																<th>Submission Status</th>														
																<th>active</th>
															</tr>
														</thead>
														<tbody>
															<c:forEach var="userList" items="${userList}"
																varStatus="status">
																<tr>
																	
																	<td>${userList.id}</td>
																	<td id="appId">${userList.appId}</td>
																	<td>${userList.location}</td>
																	<td>${userList.hostelName}</td>
																	<td>${userList.firstname} ${userList.lastname}</td>
																	
																	<%-- <td>${userList.studentImage}</td> --%>
																	<c:if test="${userList.studentImage ne null}">
																		<td><c:out value="Y"></c:out></td>
																	</c:if>
																	<c:if test="${userList.studentImage eq null}">
																		<td>N</td>
																	</c:if>
																	
																	<%-- <td>${userList.studentSign}</td> --%>
																	<c:if test="${userList.studentSign ne null}">
																		<td>Y</td>
																	</c:if>
																	<c:if test="${userList.studentSign eq null}">
																		<td>N</td>
																	</c:if>
																																		
																	<%-- <td>${userList.parentSign}</td> --%>
																	<c:if test="${userList.parentSign ne null}">
																		<td>Y</td>
																	</c:if>
																	<c:if test="${userList.parentSign eq null}">
																		<td>N</td>
																	</c:if>
																	
																	<%-- <td>${userList.proofPath}</td> --%>
																	<c:if test="${userList.proofPath ne null}">
																		<td>Y</td>
																	</c:if>
																	<c:if test="${userList.proofPath eq null}">
																		<td>N</td>
																	</c:if>
																	
																	<td>${userList.flag1}</td>
																	
																	<%-- <td>${userList.flag2}</td> --%>
																	<c:if test="${userList.flag2 eq 1}">
																		<td>Y</td>
																	</c:if>
																	<c:if test="${userList.flag2 eq 0}">
																		<td>N</td>
																	</c:if>
																	
																	<td>${userList.flag3}</td>
																	
																	<%-- <td>${userList.flag4}</td> --%>
																	<c:if test="${userList.flag4 eq 1}">
																		<td>Y</td>
																	</c:if>
																	<c:if test="${userList.flag4 eq 0}">
																		<td>N</td>
																	</c:if>
																	
																	<td>${userList.payment}</td>
																																		
																	<td>
																	
																	<c:if test="${userList.isSubmitted eq 'Y'}">
																	<!-- <input type="button" id="updateIsSubmitted" class="btn btn-primary"
																	value="Update Status Y" />  -->
																	<a href = "#" id="updateIsSubmitted"><i class="fa fa-pencil fa-lg" aria-hidden="true" id = "hideUpdateIcon"  title="Update Status N"></i></a>	<!-- id="updateIsSubmitted" -->										
																	<%-- <c:out value="N"></c:out> --%>																																	
																	</c:if>
																	
																	<div id="isSubmitted-data"></div>
																	
																	<c:if test="${userList.isSubmitted eq 'N'}">
																	
																	<c:out value="N"></c:out> 
																	<!-- <input type="text" readonly="readonly" id="isSubmitted" value="Y" /> --> 
																	</c:if>
																	
																	</td>
																	
																	<td>${userList.active}</td>
																	
																	<%-- <c:if test="${userList.enabled eq true}">
																	<td>False</td>
																	</c:if>
																	<c:if test="${userList.enabled eq false}">
																	<td>True</td>
																	</c:if>  --%>
																	
																	
																	<%-- <td> 
																	<a href="updateIcaDateBySupportAdminForm?icaId=${ica.id}"
																		title="Update Date">
																		<i class="fas fa-edit fa-lg text-primary"></i></a>													
																	</td> --%>
																
																</tr>

															</c:forEach>
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
$("#updateIsSubmitted")
.click(
		function() {
			console.log('updateIsSubmitted');
			var appId = $("#appId");
			$.ajax({
						
				
						type : "POST",
						url : "${pageContext.request.contextPath}/updateHostelStudentDetailBySupportAdmin",
						data : appId,
						success : function(data) {
							if(data="success")
								{
								alert('Status Updated Succesfully');
								//document.location.reload();
								$("#hideUpdateIcon").hide();
								 $("a").removeAttr("href");
								$("#isSubmitted-data").append("N");
								console.log($("updateIsSubmitted").val("N"));
								}
							
							
							
						},
						error : function() {
							console.log('Error updateIsSubmitted');
							alert('Error here');
						}
					});
		}); 
</script>



