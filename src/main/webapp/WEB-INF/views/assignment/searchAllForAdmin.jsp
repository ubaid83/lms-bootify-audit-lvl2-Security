<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex adminPage dataTableBottom" id="adminPage">
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
	<jsp:include page="../common/rightSidebarAdmin.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newAdminTopHeader.jsp" />

		<!-- SEMESTER CONTENT -->
		<div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">


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
						All Assignments/Tests</li>
				</ol>
			</nav>
			<jsp:include page="../common/alert.jsp" />
			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">

					<h5 class="text-center border-bottom pb-2">Search All
						Assignments/Tests</h5>


					<form:form action="searchAssignmentTest" method="post"
						modelAttribute="assignment">
						<fieldset>


							<div class="row">
								<div class="col-md-4 col-sm-12">
									<div class="form-group">
										<form:label path="acadMonth" for="acadMonth">Academic Month <span
												style="color: red">*</span>
										</form:label>
										<form:select id="acadMonth" path="acadMonth"
											class="form-control" required="required">
											<form:option value="">Select Academic Month</form:option>
											<c:forEach var="course" items="${acadMonths}"
												varStatus="status">
												<form:option value="${course}">${course}</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>
								<div class="col-md-4 col-sm-12">
									<div class="form-group">
										<form:label path="acadYear" for="acadYear">Academic Year <span
												style="color: red">*</span>
										</form:label>
										<form:select id="acadYear" path="acadYear"
											class="form-control" required="required">
											<form:option value="">Select Academic Year</form:option>
											<c:forEach var="course" items="${acadYears}"
												varStatus="status">
												<form:option value="${course}">${course}</form:option>
											</c:forEach>
										</form:select>
									</div>
								</div>




								<div class="col-md-4 col-sm-12">
									<div class="form-group">
										<form:label path="" for="">Select Assignments/Test</form:label>
										<form:select id="dropdwonId" path="" name="dropdwonId"
											class="form-control">
											<form:option value="">Select Assignments/Test</form:option>

											<c:forEach var="list" items="${list1}" varStatus="status">
												<c:if test="${dropdownId eq list}">
													<form:option value="${list}" selected="selected">${list}</form:option>
												</c:if>
												<c:if test="${dropdownId ne list}">
													<form:option value="${list}">${list}</form:option>
												</c:if>
											</c:forEach>

										</form:select>
									</div>
								</div>

								<div class="col-sm-12">
									<div class="form-group">
										<button id="submit" name="submit"
											class="btn btn-large btn-primary">Search</button>
										<!-- <input id="reset" type="reset" value="reset"
                                                                                                class="btn btn-danger"> -->


									</div>
								</div>
							</div>
						</fieldset>
					</form:form>
				</div>
			</div>

			<!-- Results Panel -->
			<c:choose>
				<c:when test="${allContent.size() > 0}">
					<div class="card bg-white border">
						<div class="card-body">

							<h5 class="text-center border-bottom pb-2">
								Learning Resource<small> | ${allContent.size()} Records
									Found </small>
							</h5>
							<div class="table-responsive testAssignTable">


								<table class="table table-striped table-hover" id="contentTree">
									<thead>
										<tr>
											<th>Content Name <i class="fa fa-sort"
												aria-hidden="true" style="cursor: pointer"></i></th>
											<th>Description</th>
										</tr>
									</thead>
									<tbody>

										<c:forEach var="content" items="${allContent}"
											varStatus="status">
											<tr data-tt-id="${content.id}"
												data-tt-parent-id="${content.parentContentId}">

												<td><c:if test="${content.contentType == 'Folder' }">
														<i class="fas fa-folder fa-lg text-warning"></i>

														<c:url value="/getContentUnderAPathForStudent"
															var="navigateInsideFolder">
															<c:param name="courseId" value="${content.courseId}" />
															<c:param name="acadMonth" value="${content.acadMonth}" />
															<c:param name="acadYear" value="${content.acadYear}" />
															<c:param name="folderPath" value="${content.filePath}" />
															<c:param name="parentContentId" value="${content.id}" />
														</c:url>
														<a href="${navigateInsideFolder}" class="clickedFolder"
															id="folder${content.id}"><c:out
																value="${content.contentName}" /></a>
													</c:if> <c:if test="${content.contentType == 'File' }">
														<i class="fa ${content.fontAwesomeClass} fa-lg"
															style="margin-right: 5px"></i>
														<a href="downloadFile?filePath=${content.filePath}"
															class="clickedFile" id="file${content.id}"> <c:out
																value="${content.contentName}" /> <i
															class="fa fa-download" style="margin-left: 5px"></i>
														</a>
													</c:if> <c:if test="${content.contentType == 'Link' }">
														<i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
														<a href="${content.linkUrl}" target="_blank"
															class="clickedLink" id="link${content.id}"> <c:out
																value="${content.contentName}" />
														</a>
													</c:if></td>

												<td><c:out value="${content.contentDescription}" /></td>
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
				</c:when>
			</c:choose>

			<c:choose>
				<c:when test="${assignmentList.size() > 0}">
					<div class="card bg-white border">
						<div class="card-body">
							<div class="x_panel">
								<div class="x_title">
									<h5 class="text-center border-bottom pb-2">
										Assignment<small> | ${assignmentList.size()} Records
											Found </small>
									</h5>

								</div>
								<div class="x_content">
									<div class="table-responsive testAssignTable">
										<table class="table table-striped table-hover"
											style="font-size: 12px">

											<thead>



												<tr>
													<th>Sr. No.</th>

													<th>Faculty ID</th>
													<th>Assignment Name <i class="fa fa-sort"
														aria-hidden="true" style="cursor: pointer"></i></th>
													<th>Assignment File</th>
													<th>Preview</th>


													<!-- <th>Evaluation Status</th>
                                                                                                <th>Submission Status</th> -->


													<th>Max Score <i class="fa fa-sort" aria-hidden="true"
														style="cursor: pointer"></i></th>
													<th>Action <i class="fa fa-sort" aria-hidden="true"
														style="cursor: pointer"></i></th>


												</tr>
											</thead>
											<tbody>




												<c:forEach var="assignment" items="${assignmentList}"
													varStatus="status">
													<tr>
														<td><c:out value="${status.count}" /></td>

														<td><c:out value="${assignment.createdBy}" /></td>
														<td><c:out value="${assignment.assignmentName}" /></td>
														<td><a href="downloadFile?id=${assignment.id}">Download</a>
														</td>
														<td><a
															href="sendFileForAssignment?id=${assignment.id}"
															target="_blank">Preview</a></td>


														<%-- <td><c:out value="${assignment.evaluationStatus}" /></td>
                                                                                                      <td><c:out value="${assignment.submissionStatus}" /></td> --%>
														<td><c:out value="${assignment.maxScore}" /></td>


														<td><c:url value="viewThisAssignment"
																var="detailsUrl">
																<c:param name="assignmentId" value="${assignment.id}" />
															</c:url> <a href="${detailsUrl}" title="Details"><i
																class="fa fa-info-circle fa-lg"></i></a>&nbsp;</td>






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

			<c:choose>
				<c:when test="${testList.size() > 0}">
					<div class="card bg-white border">
						<div class="card-body">
							<div class="x_panel">
								<div class="x_title">
									<h5 class="text-center border-bottom pb-2">
										Tests
										<snall> | ${testList.size()} Records Found </small>
									</h5>
								</div>
								<div class="x_content">
									<div class="table-responsive testAssignTable">
										<table class="table table-striped table-hover">

											<thead>



												<tr>
													<th>Sr. No.</th>

													<th>Faculty ID <i class="fa fa-sort"
														aria-hidden="true" style="cursor: pointer"></i></th>
													<th>Test Name <i class="fa fa-sort" aria-hidden="true"
														style="cursor: pointer"></i></th>
													<th>Course Name</th>
													<th>Start Date <i class="fa fa-sort"
														aria-hidden="true" style="cursor: pointer"></i></th>
													<th>End Date <i class="fa fa-sort" aria-hidden="true"
														style="cursor: pointer"></i></th>
													<th>Maximum Attempts <i class="fa fa-sort"
														aria-hidden="true" style="cursor: pointer"></i></th>
													<th>Maximum Score <i class="fa fa-sort"
														aria-hidden="true" style="cursor: pointer"></i></th>
													<th>Duartion <i class="fa fa-sort" aria-hidden="true"
														style="cursor: pointer"></i></th>
													<th>Pass score <i class="fa fa-sort"
														aria-hidden="true" style="cursor: pointer"></i></th>

													<th>Action</th>




												</tr>
											</thead>
											<tbody>




												<c:forEach var="test" items="${testList}" varStatus="status">
													<tr>
														<td><c:out value="${status.count}" /></td>

														<td><c:out value="${test.createdBy}" /></td>
														<td><c:out value="${test.testName}" /></td>
														<td><c:out value="${test.course.courseName}" /></td>
														<td><c:out value="${test.startDate}" /></td>
														<td><c:out value="${test.endDate}" /></td>
														<td><c:out value="${test.maxAttempt}" /></td>
														<td><c:out value="${test.maxScore}" /></td>
														<td><c:out value="${test.duration}" /></td>
														<td><c:out value="${test.passScore}" /></td>


														<td><c:url value="viewThisTest" var="detailsUrl">
																<c:param name="testId" value="${test.id}" />
															</c:url> <a href="${detailsUrl}" title="Details"><i
																class="fa fa-info-circle fa-lg"></i></a>&nbsp;</td>






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

			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->
		<jsp:include page="../common/newAdminFooter.jsp" />