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
<style>
table {
	font-family: arial, sans-serif;
	border-collapse: collapse;
	width: 100%;
	border: 1px solid #dddddd;
	
}

td, th {
	border: 1px solid #dddddd;
	text-align: left;
	padding: 8px;
}

/*th {
	background-color: #696969;
	color: #ffffff;
}*/
/*  tr:nth-child(even) {
	background-color: #dddddd;
}  */
</style>


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
										<h2>Hostel Service</h2>


										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div>

									<div class="x_content">
										<form:form action="createHostelService" method="post"
											name="myForm" modelAttribute="hostelForm">

											<form:input path="id" type="hidden" />

											<%-- <div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="payment" for="payment">Mode of Service :</form:label>
														<c:if test="${hostelForm.payment eq 'Y' }">
														Paid
														</c:if>
														<c:if test="${hostelForm.payment eq 'N' }">
														Free
														</c:if>

													</div>
												</div>
											</div> --%>
											<div class="row">
												<div class="table-responsive" style="display: inline-block;">
													<table>




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





														</tbody>


													</table>
												</div>
											</div>
											<div class="row">
												<div class="col-md-2 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="location" for="location">Campus Location :</form:label>
														</div>
												</div>
												<div class="col-md-2 col-sm-6 col-xs-12 column">
												<div class="form-group">
														<form:select class="form-control" id="showStatusDropDown"
															path="location" placeholder="Campus Location"
															required="required">
															<form:option value="" disabled="true" selected="true">Select </form:option>
															<form:option value="Mumbai">Mumbai</form:option>
															<form:option value="Hyderabad">Hyderabad</form:option>
															<form:option value="NaviMumbai">Navi Mumbai</form:option>
															<form:option value="Banglore">Banglore</form:option>
														</form:select>
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-2 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="year" for="year">Year :</form:label>
														</div>
														</div>
														<div class="col-md-2 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:input path="year" class="form-control"
															value="${hostelForm.year}" type="number"
															required="required" />
													</div>
												</div>
											</div>
											<%-- <div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column"
													style="width: fit-content">
													<div class="form-group">
														<form:label path="totalSeats" for="totalSeats">Total count</form:label>
														<form:input path="totalSeats" class="form-control"
															value="${hostelForm.totalSeats}" type="number"
															required="required" />
													</div>
												</div>
											</div> --%>

											<div class="row hostelTable" id="Mumbai"
												style="display: none">
												<table>
													<tr>
														<th>Hostel Name</th>
														<th>Total Seats</th>
														<th>Hostel Fees</th>
														<th>R.S.D.</th>
														<th>Total Hostel Fees</th>

													</tr>

													<c:forEach items="${mumbaiHostelList}" var="mumbai"
														varStatus="status1">
														<tr>
															<td><c:out value="${mumbai}"></c:out></td>
															<td><a href="#" class="editable form-control"
																id="totalSeats${status1.count}" data-type="text"
																data-url="saveHostelDetails?dbCol=totalSeats&location=Mumbai"
																data-pk="${mumbai}" data-title="Enter Seats">${hostelForm.totalSeats}</a></td>
															<td><a href="#" class="editable form-control"
																id="hostelFees${status1.count}" data-type="text"
																data-pk="${mumbai}"
																data-url="saveHostelDetails?dbCol=hostelFees&location=Mumbai"
																data-title="Enter Fees">${hostelForm.hostelFees}</a></td>
															<td><a href="#" class="editable form-control"
																id="refundDeposit${status1.count}" data-type="text"
																data-pk="${mumbai}"
																data-url="saveHostelDetails?dbCol=refundDeposit&location=Mumbai"
																data-title="Enter deposit">${hostelForm.refundDeposit}</a></td>

															<td><a href="#" class="editable form-control"
																id="totalFees${status1.count}" data-type="text"
																data-pk="${mumbai}"
																data-url="saveHostelDetails?dbCol=totalFees&location=Mumbai"
																data-title="Enter Total Fees">${hostelForm.totalFees}</a></td>



														</tr>

													</c:forEach>
												</table>
											</div>
											<div class="row hostelTable" id="Hyderabad"
												style="display: none">
												<table>
													<tr>
														<th>Hostel Name</th>
														<th>Total Seats</th>
														<th>Hostel Fees</th>
														<th>Refundable Security Deposit</th>
														<th>Total Hostel Fees</th>

													</tr>

													<c:forEach items="${hydHostelList}" var="hyd"
														varStatus="status1">
														<tr>
															<td><c:out value="${hyd}"></c:out></td>
															<td><a href="#" class="editable form-control"
																id="totalSeats${status1.count}" data-type="text"
																data-url="saveHostelDetails?dbCol=totalSeats&location=Hyderabad"
																data-pk="${hyd}" data-title="Enter Seats">${hostelForm.totalSeats}</a></td>
															<td><a href="#" class="editable form-control"
																id="hostelFees${status1.count}" data-type="text"
																data-url="saveHostelDetails?dbCol=hostelFees&location=Hyderabad"
																data-pk="${hyd}" data-title="Enter Fees">${hostelForm.hostelFees}</a></td>
															<td><a href="#" class="editable form-control"
																id="refundDeposit${status1.count}" data-type="text"
																data-url="saveHostelDetails?dbCol=refundDeposit&location=Hyderabad"
																data-pk="${hyd}" data-title="Enter deposit">${hostelForm.refundDeposit}</a></td>

															<td><a href="#" class="editable form-control"
																id="totalFees${status1.count}" data-type="text"
																data-url="saveHostelDetails?dbCol=totalFees&location=Hyderabad"
																data-pk="${hyd}" data-title="Enter Total Fees">${hostelForm.totalFees}</a></td>



														</tr>

													</c:forEach>
												</table>
											</div>
											<div class="row hostelTable" id="NaviMumbai"
												style="display: none">
												<table>
													<tr>
														<th>Hostel Name</th>
														<th>Total Seats</th>
														<th>Hostel Fees</th>
														<th>Refundable Security Deposit</th>
														<th>Total Hostel Fees</th>

													</tr>

													<c:forEach items="${nmHostelList}" var="nm"
														varStatus="status1">
														<tr>
															<td><c:out value="${nm}"></c:out></td>
															<td><a href="#" class="editable form-control"
																id="totalSeats${status1.count}" data-type="text"
																data-url="saveHostelDetails?dbCol=totalSeats&location=NaviMumbai"
																data-pk="${nm}" data-title="Enter Seats">${hostelForm.totalSeats}</a></td>
															<td><a href="#" class="editable form-control"
																id="hostelFees${status1.count}" data-type="text"
																data-url="saveHostelDetails?dbCol=hostelFees&location=NaviMumbai"
																data-pk="${nm}" data-title="Enter Fees">${hostelForm.hostelFees}</a></td>
															<td><a href="#" class="editable form-control"
																id="refundDeposit${status1.count}" data-type="text"
																data-url="saveHostelDetails?dbCol=refundDeposit&location=NaviMumbai"
																data-pk="${nm}" data-title="Enter Deposit">${hostelForm.refundDeposit}</a></td>

															<td><a href="#" class="editable form-control"
																id="totalFees${status1.count}" data-type="text"
																data-url="saveHostelDetails?dbCol=totalFees&location=NaviMumbai"
																data-pk="${nm}" data-title="Enter Total Fees">${hostelForm.totalFees}</a></td>



														</tr>

													</c:forEach>
												</table>
											</div>
											<div class="row hostelTable" id="Banglore"
												style="display: none">
												<table>
													<tr>
														<th>Hostel Name</th>
														<th>Total Seats</th>
														<th>Hostel Fees</th>
														<th>Refundable Security Deposit</th>
														<th>Total Hostel Fees</th>

													</tr>

													<c:forEach items="${bngHostelList}" var="bng"
														varStatus="status1">
														<tr>
															<td><c:out value="${bng}"></c:out></td>
															<td><a href="#" class="editable form-control"
																id="totalSeats${status1.count}" data-type="text"
																data-url="saveHostelDetails?dbCol=totalSeats&location=Banglore"
																data-pk="${bng}" data-title="Enter Seats">${hostelForm.totalSeats}</a></td>
															<td><a href="#" class="editable form-control"
																id="hostelFees${status1.count}" data-type="text"
																data-url="saveHostelDetails?dbCol=hostelFees&location=Banglore"
																data-pk="${bng}" data-title="Enter Fees">${hostelForm.hostelFees}</a></td>
															<td><a href="#" class="editable form-control"
																id="refundDeposit${status1.count}" data-type="text"
																data-url="saveHostelDetails?dbCol=refundDeposit&location=Banglore"
																data-pk="${bng}" data-title="Enter Deposit">${hostelForm.refundDeposit}</a></td>

															<td><a href="#" class="editable form-control"
																id="totalFees${status1.count}" data-type="text"
																data-url="saveHostelDetails?dbCol=totalFees&location=Banglore"
																data-pk="${bng}" data-title="Enter Total Fees">${hostelForm.totalFees}</a></td>



														</tr>

													</c:forEach>
												</table>
											</div>
											<%-- <div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column"
													style="width: fit-content">
													<div class="form-group">
														<form:label path="hostelFees" for="hostelFees">Hostel Fees</form:label>
														<form:input path="hostelFees" class="form-control notes"
															id="hostelFees" value="${hostelForm.hostelFees}"
															type="number" required="required" />
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column"
													style="width: fit-content">
													<div class="form-group">
														<form:label path="refundDeposit" for="refundDeposit">Refundable Security Deposite</form:label>
														<form:input path="refundDeposit"
															class="form-control notes" id="refundDeposit"
															value="${hostelForm.refundDeposit}" type="number"
															required="required" />
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column"
													style="width: fit-content">
													<div class="form-group">
														<form:label path="totalFees" for="totalFees">Total Fees</form:label>
														<form:input path="totalFees" class="form-control"
															id="totalFees" value="${hostelForm.totalFees}"
															type="number" required="required" />
													</div>
												</div>


											</div> --%>







											<div>
												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" class="btn btn-large btn-primary"
															formaction="createHostelService">Create Service</button>



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




			<script type="text/javascript">
				$(".notes").change(function() {

					var id = $(this).attr("id");
					var val1 = $(this).val();

					var val2 = 0;
					//alert(val1);
					if (id == ('hostelFees')) {
						val2 = $('#refundDeposit').val();
					}
					if (id == ('refundDeposit')) {
						val2 = $('#hostelFees').val();
					}

					var total = Number(val1) + Number(val2);
					$('#totalFees').val(total);

				});
			</script>

			<script type="text/javascript">
				$("#showStatusDropDown").on('change', function() {
					var location = $(this).val();
					alert(location)
					if (location) {
						$('.hostelTable').hide();
						var d = '#' + location;
						$(d).show();

					} else {
						alert('Error!');
					}
				});
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
