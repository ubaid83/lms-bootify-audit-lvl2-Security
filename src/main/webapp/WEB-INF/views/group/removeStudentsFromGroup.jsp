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
	<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
	<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
	<jsp:include page="../common/rightSidebarFaculty.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeaderFaculty.jsp" />

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
							<li class="breadcrumb-item" aria-current="page"><c:out
									value="${Program_Name}" /></li>
							<sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
							<li class="breadcrumb-item active" aria-current="page">Search
								Groups To Remove Stduents</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Search Groups To Remove Students</h5>
										
									</div>

									<div class="x_content">
										<form:form action="removeStudentsFromGroup" method="post"
											modelAttribute="groups">
											<fieldset>




												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label class="textStrong" path="courseId" for="courseId">Course</form:label>
														<form:select id="courseId" path="courseId"
															class="form-control" required="required">
															<form:option value="">Select Course</form:option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
													</div>
												</div>

												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label class="textStrong" path="id" for="id">Groups</form:label>
														<form:select id="grpid" path="id" class="form-control">
															<form:option value="">Select Group</form:option>

															<c:forEach var="preGroup" items="${preGroup}"
																varStatus="status">
																<form:option value="${preGroup.id}">${preGroup.groupName}</form:option>
															</c:forEach>

														</form:select>
													</div>
												</div>



												<div class="col-sm-12 column">
													<div class="form-group">
														<button id="submit" name="submit"
															class="btn btn-large btn-primary">Search</button>
														<input id="reset" type="reset" class="btn btn-danger">
														<button id="cancel" name="cancel" class="btn btn-danger"
															formnovalidate="formnovalidate" formaction="homepage">Cancel</button>
													</div>
												</div>
											</fieldset>
										</form:form>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Results Panel -->

					<c:choose>
						<c:when test="${students.size() > 0}">
							<form:form action="removeStudentsFromGroup" method="post"
								modelAttribute="groups">
								<form:input path="id" type="hidden" />
								<form:input path="courseId" type="hidden" />
								<div class="card bg-white border">
									<div class="card-body">

										<div class="col-xs-12 col-sm-12">
											<div class="x_panel">
												<div class="text-center border-bottom pb-2">
													<h5>Students | ${fn:length(students)} Records Found</h5>
														<p>Note : On removing any student, assignment allocated to
														him/her will get deallocated.</p>


												</div>
												<div class="x_content">
													<div class="table-responsive testAssignTable">
														<table class="table table-striped table-hover" id="POITable">
															<thead>
																<tr>
																	<th>Sr. No.</th>

																	<th>Username</th>
																	<th>Roll No.</th>
																	<th>Student Name</th>
																	<th>Action</th>
																</tr>
															</thead>
															<tfoot>
																<tr>
																	<th></th>
																	<th></th>

																</tr>
															</tfoot>
															<tbody>

																<c:forEach var="student" items="${students}"
																	varStatus="status">
																	<tr id="tr${student.id}">
																		<td><c:out value="${status.count}" /></td>
																		<td><c:out value="${student.username}" /></td>
																		<td><c:out value="${student.rollNo}" /></td>

																		<td><c:out
																				value="${student.firstname} ${student.lastname}" /></td>
																		<td><a href="#" id="like${student.id}"
																			class="btn btn-dark text-white likeClass"> Remove
																				Student</a></td>
																	</tr>
																</c:forEach>
															</tbody>
														</table>
													</div>

												</div>
											</div>
										</div>

									</div>
								</div>
							</form:form>
						</c:when>
					</c:choose>


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

										$('#reset')
												.on(
														'click',
														function() {
															$('#grpid').empty();
															var optionsAsString = "";

															optionsAsString = "<option value=''>"
																	+ "Select Assignment"
																	+ "</option>";
															$('#grpid')
																	.append(
																			optionsAsString);

															$("#courseId")
																	.each(
																			function() {
																				this.selectedIndex = 0
																			});

														});

										$("#courseId")
												.on(
														'change',
														function() {
															var courseid = $(
																	this).val();
															if (courseid) {
																$
																		.ajax({
																			type : 'GET',
																			url : '${pageContext.request.contextPath}/getGroupsByCourseForReAllocation?'
																					+ 'courseId='
																					+ courseid,
																			success : function(
																					data) {

																				var json = JSON
																						.parse(data);
																				var optionsAsString = "";

																				$(
																						'#grpid')
																						.find(
																								'option')
																						.remove();
																				console
																						.log(json);
																				for (var i = 0; i < json.length; i++) {
																					var idjson = json[i];
																					console
																							.log(idjson);

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

																				$(
																						'#grpid')
																						.append(
																								optionsAsString);

																			}
																		});
															}/*  else {
																alert('Error no course');
															} */
														});

										

									});
				</script>

				<script type="text/javascript">
					$(".likeClass")
							.click(
									function() {
										console
												.log("called ........................................................000000.");

										var likeId = $(this).attr("id");

										var id = likeId.substr(4, 5);
										console.log(id);
										$
												.ajax({
													type : 'GET',
													url : '${pageContext.request.contextPath}/removeStudents?'
															+ 'id=' + id,
													success : function(data) {
														var str1 = "tr";
														var str2 = str1
																.concat(id);
														console.log("str2 ",
																str2);

														$('#' + str2).remove();
														alert('Student Removed!');

														//$("likes").update('<h5>Votes:<c:out value="${forumReply.vote}" /></h5>');

													}

												});

									});

					/* function deleteRow(row) {
						var i = row.parentNode.parentNode.rowIndex;
						document.getElementById('POITable').deleteRow(i);
					} */
				</script>