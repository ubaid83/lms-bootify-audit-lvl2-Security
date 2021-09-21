<%@page import="com.spts.lms.web.utils.Utils"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@page import="java.util.*"%>



<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<jsp:include page="../common/newLeftSidebar.jsp" />
	<jsp:include page="../common/rightSidebar.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeader.jsp" />

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

					<!-- page content: START -->

					<nav aria-label="breadcrumb">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a
								href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>

							<li class="breadcrumb-item active" aria-current="page">
								Overview</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<!-- <div class="x_title">
										<h2 class="" style="color: red;">STUDENT RESOURCE BOOK
											(SRB)</h2>
										<ul class="nav navbar-right panel_toolbox">
											<li><a class="collapse-link"><i
													class="fa fa-chevron-up"></i></a></li>
										</ul>
										<div class="clearfix"></div>
									</div> -->

									<div class="x_content">

										<div class="row">

											<div class="col-sm-12 column">
												<h2 class="" style="color: red;">STUDENT RESOURCE BOOK
													(SRB)</h2>
												<div class="clearfix"></div>
												<p>
													Please read the Student Resource Book carefully as it
													contains details of the Academic, Evaluation and
													Administrative Rules and Regulations of the University. All
													students are expected to know these rules and policies as
													mentioned in the SRB.
													<!-- <a class="forumLinks"
														href="sendSrbFile?filePath=/data/srbFolder/SRB.pdf" target="_blank"
														onclick="return preview(this.href); "><b><i
															class="fa fa-download fa-lg"></i>Download SRB</b></a> -->

													<!--<a class="forumLinks" href="sendSrbFile?id=1"
														target="_blank" onclick="return preview(this.href); "><b><i
															class="fa fa-download fa-lg"></i>Download SRB</b></a>-->

                                                        <c:choose>
                                                            <c:when test="${downloadAvailable eq 'yes'}">
                                                            <a class="forumLinks" href="sendSrbFile?id=1"
                                                             onclick="return preview(this.href); ">
                                                            <b><i class="fa fa-download fa-lg"></i> Download SRB</b></a>
                                                            </c:when>

                                                            <c:when test="${downloadAvailable eq 'no'}">
                                                            <a class="forumLinks" href="#">
                                                            <b><i class="fa fa-download fa-lg"></i> SRB Not Available</b></a>
                                                            </c:when>
														</c:choose>
                                            
												</p>
											</div>
										</div>


										<%-- 	<div class="col-sm-12 column">
											<h2 class="" style="color: red;">HELP AND SUPPORT AT
												NGASCE IS NOW FASTER</h2>
											<div class="clearfix"></div>
											<ul>
												<li><p>An efficient and responsive help desk
														reduces the distance between students and the University</p></li>
												<li><p>It is the point of contact for students to
														get any information / service from the University</p></li>
												<li><p>Timely resolution of students queries is the
														top priority at NGASCE</p></li>
												<li><p>Our single-window help desk helps you
														clarify all issues pertaining to any department:
														Admissions, Academics, Examinations, Books, Icards, Fee
														Receipts, etc.</p></li>
											</ul>
										</div>


										<div class="col-sm-12 column">
											<h2 class="" style="color: red;">HIGHLIGHTS</h2>
											<div class="clearfix"></div>
											<ul>
												<li><p>Department-wise classification of queries
														for better response time</p></li>
												<li><p>Time bound resolution of queries within 2
														working days</p></li>
												<li><p>Well Defined Three Level Escalation process
														to manage Complains and Student Grievances</p></li>
												<li><p>Multiple means to raise a query - Phone,
														Post My Query, Chat and by Visiting our Regional Office</p></li>
												<li><p>Multi skilled Counsellors with extensive
														knowledge across departments so you can get relevant and
														specific answers</p></li>
												<li><p>Comprehensive List of Frequently Asked
														Queries (FAQs) to ensure that we have the answers for most
														of the questions a student might have</p></li>
											</ul>
										</div>

										<div class="row">
											<div class="col-sm-12 column">
												<h2 class="" style="color: red;">ESCALATION MATRIX FOR
													STUDENTS:</h2>
												<div class="clearfix"></div>


												<div class="panel-group panel-overview">
													<div class="panel panel-default">
														<div class="panel-heading">
															<h4 class="panel-title"></h4>
															<h5 data-toggle="collapse" href="#collapse1">Level
																1:City Wise</h5>

														</div>
														<div id="collapse1" class="panel-collapse collapse">
															<div class="panel-body">
																<div class="form-group">

																	 <label for="city">City</label> <select
                                                                        class="form-control" id="citywise">
                                                                        <option value="">Select City</option>
                                                                        <c:forEach var="nameOfCity" items="${cityList}">
                                                                              <option value="${nameOfCity}">${nameOfCity}</option>
                                                                        </c:forEach>
                                                                  </select>
																	<label for="city">City</label> <select
																		class="form-control" id="citywise">
																		<option value="">Select City</option>
																		<c:forEach var="nameOfCity"
																			items="${overviewCitiesList}">
																			<option value="${nameOfCity}">${nameOfCity}</option>
																		</c:forEach>
																	</select>

																</div>

															</div>
														</div>
													</div>
												</div>



												<div class="panel-group panel-overview">
													<div class="panel panel-default">
														<div class="panel-heading">
															<h4 class="panel-title"></h4>
															<h5 data-toggle="collapse" href="#collapse2">Level
																2:Department Wise</h5>

														</div>
														<div id="collapse2" class="panel-collapse collapse">
															<div class="panel-body">
																<div class="form-group">
																	<label for="dept">Department</label> <select
																		class="form-control" id="dept1">
																		<option value="">Select Department</option>
																		<c:forEach var="dept" items="${deptList}">
																			<option value="${dept}">${dept}</option>
																		</c:forEach>
																	</select>

																</div>

															</div>
														</div>
													</div>
												</div>

												<div class="panel-group panel-overview">
													<div class="panel panel-default">
														<div class="panel-heading">
															<h4 class="panel-title"></h4>
															<h5 data-toggle="collapse" href="#collapse4">Level
																3: City and Department</h5>

														</div>
														<div id="collapse4" class="panel-collapse collapse">
															<div class="panel-body">
																<div class="form-group">
																	<label for="city">City</label> <select
																		class="form-control" id="city">
																		<option value="">Select City</option>
																		<c:forEach var="nameOfCity"
																			items="${overviewCitiesList}">
																			<option value="${nameOfCity}">${nameOfCity}</option>
																		</c:forEach>
																	</select>
																</div>
																<div class="form-group" id="deptDiv"
																	style="display: none;">
																	<label for="dept">Department</label> <select
																		class="form-control" id="dept">
																		<option value="">Select Department</option>
																		<c:forEach var="dept" items="${deptList}">
																			<option value="${dept}">${dept}</option>
																		</c:forEach>
																	</select>

																</div>
															</div>
														</div>
													</div>
												</div>



											</div>
										</div>

										<div class="table-responsive" id="table-responsiveId">
											<label for="contactTable">Contact Support</label>
											<table id="contactTable"
												class="table table-bordered table-hover escalationthree">
												<tbody>
													<tr id="contactRow" style="display: none;">
                                                <td id="cityName">Person To Contact</td>
                                                <td id="personInCharge">Email Id</td>
                                                <td id="mailerLink"><a href=""></a></td>
                                                <td id="number">Contact Number</td>
                                          </tr>  
												</tbody>
											</table>
										</div> --%>

									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Results Panel -->




					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />

				<script>
					$(document)
							.ready(
									function() {
										$("#deptDiv").css('display', 'none');
										$("#city")
												.on(
														'change',
														function() {
															var x = $(this)
																	.val();
															console
																	.log('select option val'
																			+ x);
															if (x != '') {
																$("#deptDiv")
																		.css(
																				'display',
																				'block');
															} else {
																$("#deptDiv")
																		.css(
																				'display',
																				'none');
															}
														});

										$("#citywise")
												.on(
														'change',
														function() {

															var citySelectedOption = $(
																	'#citywise')
																	.find(
																			":selected")
																	.text();
															var url = '${pageContext.request.contextPath}/getContactDetails?cityValue='
																	+ citySelectedOption
																	+ "&dept=";
															console.log('url :'
																	+ url);

															$
																	.ajax({
																		type : 'GET',
																		url : url,
																		success : function(
																				data) {
																			var overview = JSON
																					.parse(data);

																			for (var i = 0; i < overview.length; i++) {
																				console
																						.log(overview[i]["persontocontact"]);
																				console
																						.log(overview[i]["emailid"]);
																				console
																						.log(overview[i]["number"]);

																				var tr = "<tr>";
																				var td1 = "<td>"
																						+ overview[i]["persontocontact"]
																						+ "</td>";
																				var td2 = "<td>"
																						+ overview[i]["emailid"]
																						+ "</td>";
																				var td3 = "<td>"
																						+ overview[i]["number"]
																						+ "</td></tr>";
																				$(
																						"#contactTable")
																						.append(
																								tr
																										+ td1
																										+ td2
																										+ td3);
																			}
																		},
																		error : function(
																				data) {
																			console
																					.log("error");
																		}
																	});
														});
										$("#dept1")
												.on(
														'change',
														function() {
															console
																	.log("called");
															var deptSelectedOption = $(
																	'#dept1')
																	.find(
																			":selected")
																	.text();
															var url = '${pageContext.request.contextPath}/getContactDetails?dept='
																	+ deptSelectedOption
																	+ "&cityValue=";
															console.log('url :'
																	+ url);

															$
																	.ajax({
																		type : 'GET',
																		url : url,
																		success : function(
																				data) {
																			var overview = JSON
																					.parse(data);

																			for (var i = 0; i < overview.length; i++) {
																				console
																						.log(overview[i]["persontocontact"]);
																				console
																						.log(overview[i]["emailid"]);
																				console
																						.log(overview[i]["number"]);

																				var tr = "<tr>";
																				var td1 = "<td>"
																						+ overview[i]["persontocontact"]
																						+ "</td>";
																				var td2 = "<td>"
																						+ overview[i]["emailid"]
																						+ "</td>";
																				var td3 = "<td>"
																						+ overview[i]["number"]
																						+ "</td></tr>";
																				$(
																						"#contactTable")
																						.append(
																								tr
																										+ td1
																										+ td2
																										+ td3);
																			}
																		},
																		error : function(
																				data) {
																			console
																					.log("error");
																		}
																	});
														});

										$("#city").on(
												'change',
												function() {
													var cityOption = $(this)
															.val();
													if (cityOption != '') {
														$("#deptDiv").css(
																'display',
																'block');
													} else {
														$("#deptDiv").css(
																'display',
																'none');
													}

												});

										$("#dept")
												.on(
														'change',
														function() {
															var deptOption = $(
																	this).val();
															var cityOption = $(
																	'#city')
																	.find(
																			":selected")
																	.text();
															var url = '${pageContext.request.contextPath}/getContactDetails?dept='
																	+ deptOption
																	+ "&cityValue="
																	+ cityOption;
															$
																	.ajax({
																		type : 'GET',
																		url : url,
																		success : function(
																				data) {
																			var overview = JSON
																					.parse(data);
																			var tbl = $(
																					"<table><tbody>")
																					.attr(
																							"id",
																							"contactTable");
																			tbl
																					.attr(
																							"class",
																							"table table-bordered table-hover escalationthree");
																			$(
																					"#table-responsiveId")
																					.append(
																							tbl);
																			var tbodyEnd = "</tbody>";
																			for (var i = 0; i < overview.length; i++) {
																				console
																						.log(overview[i]["persontocontact"]);
																				console
																						.log(overview[i]["emailid"]);
																				console
																						.log(overview[i]["number"]);

																				var tr = "<tr>";
																				var td1 = "<td>"
																						+ overview[i]["persontocontact"]
																						+ "</td>";
																				var td2 = "<td>"
																						+ overview[i]["emailid"]
																						+ "</td>";
																				var td3 = "<td>"
																						+ overview[i]["number"]
																						+ "</td></tr></tbody></table>";
																				$(
																						"#table-responsiveId")
																						.append(
																								tr
																										+ td1
																										+ td2
																										+ td3);
																			}
																			$(
																					"#table-responsiveId")
																					.append(
																							tbodyEnd);
																		},
																		error : function(
																				data) {
																			console
																					.log("error");
																		}
																	});

														});

									});
				</script>