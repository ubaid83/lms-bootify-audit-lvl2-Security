<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />
<style>
table {
	font-family: arial, sans-serif;
	border-collapse: collapse;
	width: 100%;
}

td, th {
	border: 1px solid #dddddd;
	text-align: left;
	padding: 8px;
}
</style>
<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>

			<jsp:include page="../common/topHeader.jsp" />
			<jsp:include page="../common/alert.jsp" />


			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">

							<c:out value="${Program_Name}" />

							<sec:authorize access="hasRole('ROLE_STUDENT')">

								<i class="fa fa-angle-right"></i>

								<c:out value="${AcadSession}" />

							</sec:authorize>
							<br> <br> <a href="index.html">Dashboard</a> <i
								class="fa fa-angle-right"></i> Assign Weight
						</div>

						<c:choose>
							<c:when test="${addedWieghtageList.size() > 0}">
								<div class="row">
									<div class="col-xs-12 col-sm-12">
										<div class="x_panel">
											<div class="x_title">
												<h2>
													Assigned Weight for ${courseName}<font size="2px"> |
														${addedWieghtageList.size()} Records Found &nbsp; </font>
												</h2>

												<ul class="nav navbar-right panel_toolbox">
													<li><a class="collapse-link"><i
															class="fa fa-chevron-up"></i></a></li>
													<li><a class="close-link"><i class="fa fa-close"></i></a></li>
												</ul>
												<div class="clearfix"></div>
											</div>
											<div class="x_content">
												<div class="table-responsive">
													<table id="inboxTable"
														class="table table-striped table-hover"
														style="font-size: 12px">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Weight Type</th>
																<th>Weight Assigned</th>
																<th>CreatedBy</th>
																<th>Created Date</th>

															</tr>
														</thead>
														<tbody>
															<c:forEach var="listOfAssignedWieghtage"
																items="${addedWieghtageList}" varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:out
																			value="${listOfAssignedWieghtage.wieghtagetype} " /></td>
																	<td><c:out
																			value="${listOfAssignedWieghtage.wieghtageassigned} " /></td>
																	<td><c:out
																			value="${listOfAssignedWieghtage.createdBy} " /></td>
																	<td><c:out
																			value="${listOfAssignedWieghtage.createdDate} " /></td>

																</tr>

															</c:forEach>

														</tbody>
													</table>


												</div>
											</div>
										</div>
									</div>
								</div>
							</c:when>
						</c:choose>
						<!-- Input Form Panel -->
						<div class="row">
						<c:choose>
							<c:when test="${weightageDataList.size() > 0}">
						
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h2>Assign Weight for ${courseName}</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="internalWieghtage"
											id="addInternalWieghtageForm" method="post"
											modelAttribute="wieghtageData">

											<div class="table-responsive">
												<table class="table  table-hover">
													<thead>
														<tr>
															<th>Sr. No.</th>


															<th>Weight Type</th>
															<th>Add Weight <span style="color: red">*</span></th>
															<th>Action</th>
														</tr>
													</thead>
													
													<tbody>
														<c:forEach var="weight" items="${weightageDataList}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>

																<td><c:out value="${weight.wieghtagetype}" /></td>

<%-- 
																<td><form:input path="wieghtageassigned"
																		class="form-control"
																		id="saveScore${weight.wieghtagetype}"
																		value="${weight.wieghtageassigned}" type="number"
																		min="0" required="required" /></td> --%>



																<td><form:input class="form-control"
																		path="wieghtageassigned"
																		id="saveScore${weight.wieghtagetype}" type="number"
																		min="0" value="${weight.wieghtageassigned }"
																		required="required" /></td>
																<td><a href="#" id="like${weight.wieghtagetype}"
																	class="likeClass"><i
																		class="fa fa-check-square fa-lg"></i></a></td>


															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>

										</form:form>
									</div>
								</div>
							</div>
							</c:when>
							</c:choose>
							
							<c:choose>
							<c:when test="${weightageDataList.size() > 0}">
							<!-- <div class="x_title">
								<h1>OR</h1>

								<div class="clearfix"></div>
							</div> -->

							<div class="row">
								<div class="col-xs-12 col-sm-12">
									<div class="x_panel">

										<div class="x_title">
											<h2>Upload Weight File for ${courseName}</h2>
											<ul class="nav navbar-right panel_toolbox">
												<li><a class="collapse-link"><i
														class="fa fa-chevron-up"></i></a></li>
											</ul>
											<div class="clearfix"></div>
										</div>

										<div class="x_content">
											<form:form action="uploadWeightageData"
												id="addInternalWieghtageForm" method="post"
												modelAttribute="wieghtageData" enctype="multipart/form-data">



												<div class="col-sm-4 column ">
													<div class="form-group">


														<label class="control-label" for="courses">Excel
															Format: </label>
														<p>wieghtagetype | wieghtageassigned</p>
														<p>
															<b>Note:</b>
														<ul>
															<li><b>wieghtageassigned </b>Should be a valid
																Integer value</li>

														</ul>
														<br>
														<p>
															<a
																href="resources/templates/weighate_Upload_Template.xlsx"><font
																color="red">Download a sample template</font></a>
														</p>



													</div>
												</div>
												<div class="col-sm-4 column">
													<div class="form-group">
														<label for="file">Upload Weight File(Excel):</label> <input
															id="file" name="file" type="file" class="form-control" />
													</div>
												</div>
												<div class="row">

													<div class="col-sm-12 column">
														<div class="form-group">

															<button id="submit" class="btn btn-large btn-primary"
																formaction="uploadWeightageData?courseId=${courseId}">Upload</button>

															<button id="cancel" class="btn btn-danger"
																formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
														</div>
													</div>

												</div>




											</form:form>
										</div>
									</div>
								</div>
							</div>
							</c:when>
							</c:choose>
						</div>


					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />
			<script>
			

				$(".likeClass")
						.click(
								function() {
									$('#likeClass').click(function() {
										change(1);
									});
									console
											.log("called ........................................................000000.");

									var likeId = $(this).attr("id");

									var wieghtagetype = likeId.substr(4);
									console.log(wieghtagetype);

									var str1 = "saveScore".concat(wieghtagetype);
									console.log("str1" + str1);
									var wieghtageassigned = $('#' + str1).val();
									console.log("wieghtageassigned is " + wieghtageassigned);

									

									var courseid = ${courseId};

								//alert(courseid);
									//alert('Course Id is '+courseid);

									//alert(score);
									//alert(remarks);

									if (wieghtageassigned > 0) {
										$
												.ajax({
													type : 'GET',
													url : '${pageContext.request.contextPath}/saveInternalWieghtage?'
															+ 'wieghtagetype='
															+ wieghtagetype
															+ '&wieghtageassigned='
															+ wieghtageassigned
															+ '&courseId='
															+ courseid,

													success : function(data) {
														console
																.log("sucess messsgae e like "
																		+ likeId)
														alert("Marks Saved!");
													}

												});
									} else {
										alert("Please enter marks greater than 0!");
									}

								});
			</script>
		</div>
	</div>





</body>

</html>