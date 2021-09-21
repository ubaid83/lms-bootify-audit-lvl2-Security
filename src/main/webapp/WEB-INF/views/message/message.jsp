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
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
		<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
		<jsp:include page="../common/rightSidebarFaculty.jsp"/>
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftNavbar.jsp" />
		<jsp:include page="../common/newLeftSidebar.jsp" />
		<jsp:include page="../common/rightSidebar.jsp" />
	</sec:authorize>

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_STUDENT')">
			<jsp:include page="../common/newTopHeader.jsp" />
		</sec:authorize>

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
							<li class="breadcrumb-item active" aria-current="page">My
								Message</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Select To Send Message</h5>
										
									</div>

									<div class="x_content">
										<form:form action="saveStudentMessageAllocation"
											id="saveStudentMessageAllocation" method="post"
											modelAttribute="message">
											<fieldset>


												<form:input path="id" type="hidden" />
												<form:input path="courseId" type="hidden" />
												<form:input path="acadMonth" type="hidden" />
												<form:input path="acadYear" type="hidden" />
												<form:input path="facultyId" type="hidden" />
												<form:input path="subject" type="hidden" />
												<form:input path="description" type="hidden" />

												<div class="table-responsive">
													<table class="table-striped table-hover w-100 text-center table-bordered"
														id="messageTable" style="font-size: 12px">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Select (<a onclick="checkAll()">All</a> | <a
																	onclick="uncheckAll()">None</a>)
																</th>
																<th>Student Name</th>
																<th>SAP ID</th>
																<th>Roll No.</th>
															</tr>
														</thead>

														<tbody>

															<c:forEach var="student" items="${students}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:if test="${empty student.id }">
																			<form:checkbox path="students"
																				value="${student.username}" class="messageCheckbox" />
																		</c:if> <c:if test="${not empty student.id }">
						            	Message Allocated
						            </c:if></td>

																	<td><c:out
																			value="${student.firstname} ${student.lastname}" /></td>
																	<td><c:out value="${student.username} " /></td>
																	<td><c:out value="${student.rollNo} " /></td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>

												<div class="col-sm-4 column">
													<div class="form-group">

														<button id="submit" class="btn btn-large btn-primary"
															formaction="saveStudentMessageAllocation">Add
															Students</button>
														<button id="cancel" class="btn btn-large btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
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

					$("#courseId")
							.on(
									'change',
									function() {
										var selectedValue = $(this).val();
										var messageId = $("#messageId").val();
										console.log('messageId' + messageId);
										var url = '${pageContext.request.contextPath}/getStudentByCourse?courseId='
												+ selectedValue
												+ "&messageId="
												+ messageId;
										console.log('URL :' + url);
										window.location = url;
										return false;
									});
				</script>
				<script>
					
					//var table = $('#messageTable').DataTable();
					$('#messageTable').DataTable({
					    "ordering": false,
					    initComplete: function() {
					        $(this.api().table().container()).find('input[type="search"]').parent().wrap('<form>').parent().attr('autocomplete','off').css('overflow','hidden').css('margin','auto');
					    }    
					}); 
					
				    window.checkAll = function checkAll(){
				    	$('input:checkbox[name=students]').prop('checked', true);
				    	return false;
				    }


				    window.uncheckAll = function uncheckAll(){
				    	$('input:checkbox[name=students]').prop('checked', false);
				    	return false;
				    }
				    
					$('#saveStudentMessageAllocation')
							.click(
									function(event) { //on click
										var checked = this.checked;
										table
												.column(0)
												.nodes()
												.to$()
												.each(
														function(index) {
															if (checked) {
																$(this)
																		.find(
																				'.messageCheckbox')
																		.prop(
																				'checked',
																				'checked');
															} else {
																$(this)
																		.find(
																				'.messageCheckbox')
																		.removeProp(
																				'checked');
															}
														});
										table.draw();
									});
				</script>