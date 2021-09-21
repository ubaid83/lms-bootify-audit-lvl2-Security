<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />
<link
	href="https://gitcdn.github.io/bootstrap-toggle/2.2.2/css/bootstrap-toggle.min.css"
	rel="stylesheet">
<script
	src="https://gitcdn.github.io/bootstrap-toggle/2.2.2/js/bootstrap-toggle.min.js"></script>
<!-- <style>
#myInput1 {
	border-box: box-sizing;
	background-image: url('searchicon.png');
	background-position: 14px 12px;
	background-repeat: no-repeat;
	font-size: 16px;
	padding: 14px 20px 12px 45px;
	border: none;
	border-bottom: 1px solid #ddd;
}
#myInput2 {
	border-box: box-sizing;
	background-image: url('searchicon.png');
	background-position: 14px 12px;
	background-repeat: no-repeat;
	font-size: 16px;
	padding: 14px 20px 12px 45px;
	border: none;
	border-bottom: 1px solid #ddd;
}
#myInput3 {
	border-box: box-sizing;
	background-image: url('searchicon.png');
	background-position: 14px 12px;
	background-repeat: no-repeat;
	font-size: 16px;
	padding: 14px 20px 12px 45px;
	border: none;
	border-bottom: 1px solid #ddd;
}

#myInput1:focus {
	outline: 3px solid #ddd;
}
#myInput2:focus {
	outline: 3px solid #ddd;
}
#myInput3:focus {
	outline: 3px solid #ddd;
}

.dropdown1 {
	position: relative;
	display: inline-block;
}

.dropdown-content {
	/* display: none; */
	/* position: absolute; */
	background-color: #f6f6f6;
	min-width: 230px;
	overflow: auto;
	border: 1px solid #ddd;
	z-index: 1;
}

.dropdown-content a {
	color: black;
	padding: 12px 16px;
	text-decoration: none;
	display: block;
}

.dropdown1 a:hover {
	background-color: #ddd;
}
.dropdown2 {
	position: relative;
	display: inline-block;
}



.dropdown2 a:hover {
	background-color: #ddd;
}

.dropdown3 {
	position: relative;
	display: inline-block;
}





.dropdown3 a:hover {
	background-color: #ddd;
}


.show {
	display: block;
}
</style> -->


<%
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
%>
<body class="nav-md footer_fixed">

	<div class="loader"></div>


	<div class="container body">
		<div class="main_container">

			<jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include>
			<jsp:include page="../common/topHeader.jsp">
				<jsp:param value="Forum" name="activeMenu" />
			</jsp:include>


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

							<br> <br> <a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> ${studentServiceBean.name }
						</div>
						<jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
						<div class="row">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<!-- <div class="x_title">
										<h2>Create Forum</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div> -->

									<div class="x_title">
										<h2>${studentServiceBean.name }</h2>


										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="updateRCService" method="post"
											modelAttribute="studentServiceBean"
											enctype="multipart/form-data">

											<form:input path="id" type="hidden" />

											 <div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="payment" for="payment">Mode of Service :</form:label>
														<c:if test="${studentServiceBean.payment eq 'Y' }">
														Paid
														</c:if><c:if test="${studentServiceBean.payment eq 'N' }">
														Free
														</c:if>
														
													</div>
												</div>
											</div> 
											<div class="row">
												<div class="table-responsive" style="display: inline-block;">
													<table class="table  table-hover">




														<thead>
															<tr>
																<th>Level No.</th>
																<th>Users</th>

															</tr>
														</thead>
														<tbody>
															<tr>
																<td><c:out value="1"></c:out></td>
																<td><form:input path="level1" type="text"
																		readonly="true" value="${username}"
																		class="form-control" required="required" /></td>
																<%-- <td><c:out value="${wf.status}"></c:out></td> --%>


															</tr>
															<tr>
																<td><c:out value="2"></c:out></td>
																<td><form:input path="level2" type="text"
																		class="form-control" /></td>
																<%-- <td><c:out value="${wf.status}"></c:out></td> --%>


															</tr>
															<tr>
																<td><c:out value="3"></c:out></td>
																<td><form:input path="level3" type="text"
																		class="form-control" /></td>
																<td><c:out value="${wf.status}"></c:out></td>


															</tr>
															<c:if test="${studentServiceBean.payment eq 'Y' }">


																<tr style="display: none">
																	<td><c:out value="4"></c:out></td>
																	<td><form:input path="level4" type="text"
																			value="${level2 }" class="form-control" /></td>
																	<%-- <td><c:out value="${wf.status}"></c:out></td> --%>


																</tr>



															</c:if>
															<c:if test="${studentServiceBean.payment ne 'Y' }">

																<%-- <tr>
																	<td><c:out value="3"></c:out></td>
																	<td><form:input path="level4" type="text"
																			class="form-control" /></td>
																	<td><c:out value="${wf.status}"></c:out></td>


																</tr> --%>
																<%-- <tr>
																	<td><c:out value="4"></c:out></td>
																	<td><form:input path="level4" type="text"
																			class="form-control" /></td>
																	<td><c:out value="${wf.status}"></c:out></td>


																</tr> --%>



															</c:if>


														</tbody>


													</table>
												</div>
											</div>
											<div class="row">
												<div class="col-md-2 col-sm-2 col-xs-12 column">
													<div class="slider round">
														<form:label path="makeAvail" for="makeAvail">Make Available to Students?</form:label>
														<br>
														<form:checkbox path="makeAvail" class="form-control"
															value="Y" data-toggle="toggle" data-on="Yes"
															data-off="No" data-onstyle="success"
															data-offstyle="danger" data-style="ios" data-size="mini" />
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column"
													style="width: fit-content">
													<div class="form-group">
														<form:label path="duration" for="duration">Max no of hours before which the student application should get reviewed.</form:label>
														<%-- <form:input path="duration" class="form-control"
															value="${studentServiceBean.duration}" type="number"
															required="required" /> --%>
													</div>
												</div>


											</div>
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column"
													style="width: fit-content">
													<div class="form-group">
														<%-- <form:label path="duration" for="duration">Max no of hours before which the student application should get reviewed.</form:label> --%>
														<form:input path="duration" class="form-control"
															value="${studentServiceBean.duration}" type="number"
															required="required" />
													</div>
												</div>


											</div>

											<div>
												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" class="btn btn-large btn-primary"
															formaction="updateRCService">Update Service</button>
														<input id="reset" type="reset" class="btn btn-danger">
													</div>
												</div>
											</div>


										</form:form>
									</div>
								</div>
							</div>
						</div>

						<!-- Results Panel -->


					</div>



				</div>

			</div>
			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />

			<!-- <script>
				$("#checkUsers")
						.on(
								'input',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									alert(selectedValue)
									if (selectedValue) {
										$
												.ajax({
													type : 'GET',
													url : '${pageContext.request.contextPath}/getRelatedUsers?'
															+ 'selectedValue='
															+ selectedValue,
													success : function(data) {
														var json = JSON
																.parse(data);
														var optionsAsString = "";

														$('#assid').find(
																'option')
																.remove();
														console.log(json);
														for (var i = 0; i < json.length; i++) {
															var idjson = json[i];
															console.log(idjson);

															for ( var key in idjson) {
																console
																		.log(key
																				+ ""
																				+ idjson[key]);
																optionsAsString += "<option value='" +key + "'>"
																		+ idjson[key]
																		+ "</option>";
															}
														}
														console
																.log("optionsAsString"
																		+ optionsAsString);

														$('#assid')
																.append(
																		optionsAsString);

													}
												});
									} else {
										alert('Error no course');
									}

								});
			</script> -->


			<script>
				function selectFunction() {
					var id = $(this).val();

				}

				function filterFunction1() {
					var input, filter, ul, li, a, i;
					input = document.getElementById("myInput1");
					filter = input.value.toUpperCase();
					div = document.getElementById("myDropdown1");
					a = div.getElementsByTagName("a");
					for (i = 0; i < a.length; i++) {
						txtValue = a[i].textContent || a[i].innerText;
						if (txtValue.toUpperCase().indexOf(filter) > -1) {
							a[i].style.display = "";
						} else {
							a[i].style.display = "none";
						}
					}
				}
				function filterFunction2() {
					var input, filter, ul, li, a, i;
					input = document.getElementById("myInput2");
					filter = input.value.toUpperCase();
					div = document.getElementById("myDropdown2");
					a = div.getElementsByTagName("a");
					for (i = 0; i < a.length; i++) {
						txtValue = a[i].textContent || a[i].innerText;
						if (txtValue.toUpperCase().indexOf(filter) > -1) {
							a[i].style.display = "";
						} else {
							a[i].style.display = "none";
						}
					}
				}
				function filterFunction3() {
					var input, filter, ul, li, a, i;
					input = document.getElementById("myInput3");
					filter = input.value.toUpperCase();
					div = document.getElementById("myDropdown3");
					a = div.getElementsByTagName("a");
					for (i = 0; i < a.length; i++) {
						txtValue = a[i].textContent || a[i].innerText;
						if (txtValue.toUpperCase().indexOf(filter) > -1) {
							a[i].style.display = "";
						} else {
							a[i].style.display = "none";
						}
					}
				}
			</script>


		</div>
	</div>





</body>

<script type="text/javascript">
	CKEDITOR
			.replace(
					'editor1',
					{
						extraPlugins : 'mathjax',
						mathJaxLib : 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML'
					});
</script>


</html>
