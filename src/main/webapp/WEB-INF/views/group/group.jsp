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
<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>

<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

	<jsp:include page="../common/newTopHeaderFaculty.jsp" />
     
     <div class="container mt-5">
                <div class="row">
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
          <!-- page content: START -->
          
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> View Each Group </li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h5 class="text-center pb-2 border-bottom">Group Details</h5>
									</div>
									<div class="x_content">

										<form:form action="createGroupForm" id="editGroup"
											method="post" modelAttribute="groups"
											enctype="multipart/form-data">

											<form:input path="id" type="hidden" />
											<form:input path="courseId" type="hidden" />
											<form:input path="acadMonth" type="hidden" />
											<form:input path="acadYear" type="hidden" />


											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="groupName" for="groupName"><strong>Group Title:</strong></form:label>
														${groups.groupName}
													</div>
												</div>
											</div>
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label path="noOfStudents" for="noOfStudents"><strong>No. of Students:</strong></form:label>
														${groups.noOfStudents}
													</div>
												</div>
											</div>

											<%-- <div class="row">
                                    <div class="col-md-4 col-sm-6 col-xs-12 column">
                                          <div class="form-group">
                                                <form:label path="sendEmailAlert" for="sendEmailAlert">Send Email Alert for New Group:</form:label>
                                                ${groups.sendEmailAlert}
                                          </div>
                                    </div>
                                    <div class="col-md-4 col-sm-6 col-xs-12 column">
                                          <div class="form-group">
                                                <form:label path="sendSmsAlert" for="sendSmsAlert">Send SMS Alert for New Group:</form:label>
                                                ${groups.sendSmsAlert}
                                          </div>
                                </div>
                              </div> --%>

											<div class="row">
												<div class="col-sm-12 column">
													<form:label path="group_details" for="group_details">
														<a data-toggle="collapse" href="#group_details"
															aria-expanded="false" aria-controls="group_details" class="text-dark">
															<strong>Group Details: (Expand/Collapse) </strong></a>
													</form:label>
													<div id="group_details" class="collapse"
														style="border-style: solid; border-width: 1px; padding: 10px; margin-top: 10px">${groups.group_details}</div>
												</div>
											</div>
											<br>
											<div class="row">
												<div class="col-sm-8 column">
													<div class="form-group">
														<sec:authorize access="hasRole('ROLE_FACULTY')">
															<button id="submit" class="btn btn-large btn-primary"
																formaction="createGroupForm">Edit Group</button>
														</sec:authorize>

														<button id="selectall" class="btn btn-large btn-danger"
															formaction="saveStudentGroupAllocationSelectAll"
															formnovalidate="formnovalidate">Allocate All The
															Students</button>
														<button id="cancel" class="btn btn-large btn-danger"
															formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
														<button id="cancel" class="btn btn-danger" type="button"
															formaction="homepage" formnovalidate="formnovalidate"
															onclick="history.go(-1);">Back</button>
														<%-- <button
                                                      class="btn btn-large btn-primary" formaction="${addStudents}">Add Students</button> --%>
													</div>
												</div>
											</div>


										</form:form>
									</div>
								</div>
							</div>
						</div>
						</div>

						<!-- Results Panel -->

						
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">

								<div class="x_panel">
									<div class="x_title">
										<h5 class="text-center pb-2 border-bottom">Select Students to Add in Group | Students added in
											this Group : ${noOfStudentAllocated} Students</h5>
										
									</div>
									<div class="x_content">

										<form:form action="saveStudentGroupAllocation"
											id="saveStudentGroupAllocation" method="post"
											modelAttribute="groups">


											<form:input path="id" type="hidden" />
											<form:input path="courseId" type="hidden" />
											<form:input path="acadMonth" type="hidden" />
											<form:input path="acadYear" type="hidden" />
											<form:input path="facultyId" type="hidden" />
											<c:if test="${allCampuses.size() > 0}">

												<div class="row">
													<div class="col-md-4 col-sm-6 col-xs-12 column">
														<div class="form-group">
															<form:label path="campusId" for="campusId"><strong>Select Campus</strong></form:label>

															<form:select id="campusId" path="campusId" type="text"
																placeholder="campus" class="form-control">
																<form:option value="">Select Campus</form:option>
																<c:forEach var="campus" items="${allCampuses}"
																	varStatus="status">
																	<form:option value="${campus.campusId}">${campus.campusName}</form:option>
																</c:forEach>
															</form:select>
														</div>
													</div>
												</div>
											</c:if>

											<div class="table-responsive">
												<table class="table table-striped table-hover" id="example"
													style="font-size: 12px">
													<thead>
														<tr>
															<th>Sr. No.</th>
															<th><input name="select_all" value="1"
																id="example-select-all" type="checkbox" /></th>

															<th>Student Name</th>
															<th>SAPID</th>
															<th>Roll No.</th>
															<th>Campus</th>
															<th>Program</th>
														</tr>
													</thead>
													<tfoot>
														<tr>
															<th></th>
															<th></th>

															<th>Student Name</th>
															<th>SAPID</th>
															<th>Roll No.</th>
															<th>Campus</th>
															<th>Program</th>

														</tr>
													</tfoot>
													<tbody>

														<c:forEach var="student" items="${students}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>
																<td><c:if test="${empty student.id }">
																		<form:checkbox id="chkbox${status.count}"
																			path="students" value="${student.username}" />
																	</c:if> <c:if test="${not empty student.id }">
																	<c:if test="${student.groupId eq groups.id}">
                                                      Group Allocated</c:if>
                                                      				<c:if test="${student.groupId ne groups.id}">
                                                      				Allocated To Other Group For Course.
                                                      				</c:if>
                                                </c:if></td>

																<td><c:out
																		value="${student.firstname} ${student.lastname}" /></td>
																<td><c:out value="${student.username}" /></td>
																<td><c:out value="${student.rollNo}" /></td>
																<td><c:out value="${student.campusName}" /></td>
																<td><c:out value="${student.programName}" /></td>
															</tr>
														</c:forEach>
													</tbody>
												</table>
											</div>

											<div class="col-md-4 col-sm-6 col-xs-12 column">
												<div class="form-group">
													<sec:authorize access="hasRole('ROLE_FACULTY')">
														<button id="submit" class="btn btn-large btn-primary"
															onclick="return clicked();"
															formaction="saveStudentGroupAllocation">Add
															Students</button>


													</sec:authorize>
													<button id="cancel" class="btn btn-large btn-danger"
														formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
												</div>
											</div>


										</form:form>

									</div>
								</div>
							</div>
								</div>
								</div>
							

					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

			<!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->
	         <jsp:include page="../common/newSidebar.jsp" />
	        <!-- SIDEBAR END -->
	<jsp:include page="../common/footer.jsp"/>
			<script>
				function clicked() {
					var name = document
							.querySelectorAll('input[type="checkbox"]:checked').length
							+ " Students selected";

					return confirm(name);
				}
				$("#campusId")
						.on(
								'change',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									var id = ${id};
									window.location = '${pageContext.request.contextPath}/viewGroup?id='
											+ id + '&campusId=' + selectedValue;
									+encodeURIComponent(selectedValue);
									return false;
								});
				
				
				
				//var email = document.getElementById('name').value;
			</script>