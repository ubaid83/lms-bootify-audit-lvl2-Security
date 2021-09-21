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
<%
	boolean isEdit = "true".equals((String) request
			.getAttribute("edit"));
%>
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
                                <li class="breadcrumb-item active" aria-current="page"> Share Folder with Students </li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>Share Folder with Students</h5>
										
									</div>

									<div class="x_content">
										<form:form id="createAssignment"
											method="post" modelAttribute="content"
											enctype="multipart/form-data">
											<fieldset>
												<form:input type="hidden" path="id" />
												<form:input path="courseId" type="hidden" />
												<form:hidden path="contentType" />
												<form:hidden path="folderPath" />
												<form:hidden path="parentContentId" />
												<form:input path="acadMonth" type="hidden" />
												<form:input path="acadYear" type="hidden" />
												<form:input path="moduleId" type="hidden" />
												<form:hidden path="accessType" />

												<%-- <c:url value="/getContentUnderAPathForFaculty"
													var="rootFolder">
													<c:param name="courseId" value="${content.courseId}" />
													<c:param name="acadMonth" value="${content.acadMonth}" />
													<c:param name="acadYear" value="${content.acadYear}" />
												</c:url> --%>
												
												<c:url value="/getContentUnderAPathForFacultyForModule"
													var="rootFolder">
													<%-- <c:param name="courseId" value="${content.courseId}" />
													<c:param name="acadMonth" value="${content.acadMonth}" />
													<c:param name="acadYear" value="${content.acadYear}" /> --%>
												</c:url>

												<c:url value="/getContentUnderAPath"
													var="rootFolderForAdmin">
													<c:param name="courseId" value="${content.courseId}" />
													<c:param name="acadMonth" value="${content.acadMonth}" />
													<c:param name="acadYear" value="${content.acadYear}" />
												</c:url>
												
												

												<div class="row"></div>

												<div class="row">
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="acadMonth" for="acadMonth"><strong>Academic Month:</strong></form:label>
															${content.acadMonth }
														</div>
													</div>
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="acadYear" for="acadYear"><strong>Academic Year:</strong></form:label>
															${content.acadYear}
														</div>
													</div>
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="contentName" for="contentName"><strong>Folder Name: </strong><i
																	class="fa fa-folder-o"></i>
															</form:label>
															${content.contentName}
														</div>
													</div>
												</div>

												<div class="row">

													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="contentDescription"
																for="contentDescription"><strong>Content Description:</strong></form:label>
															${content.contentDescription}

														</div>
													</div>

													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="accessType" for="accessType"><strong>Access Type:</strong></form:label>
															${content.accessType }
														</div>
													</div>

												</div>

												<div class="row">
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="startDate" for="startDate"><strong>Display From:</strong></form:label>
															${content.startDate }
														</div>
													</div>
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="endDate" for="endDate"><strong>Display Until:</strong></form:label>
															${content.endDate }
														</div>
													</div>
												</div>


												<div class="row">
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="sendEmailAlert" for="sendEmailAlert"><strong>Send Email Alert for New Content:</strong></form:label>
															${content.sendEmailAlert }
														</div>
													</div>
													<div class="col-sm-4 column">
														<div class="form-group">
															<form:label path="sendSmsAlert" for="sendSmsAlert"><strong>Send SMS Alert for New Content:</strong></form:label>
															${content.sendSmsAlert }
														</div>
													</div>
													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label path="sendEmailAlertToParents"
																for="sendEmailAlertToParents"><strong>Send Email Alert to Parents:</strong></form:label>
															${content.sendEmailAlertToParents}
														</div>
													</div>

													<div class="col-sm-6 col-md-4 col-xs-12 column">
														<div class="form-group">
															<form:label path="sendSmsAlertToParents"
																for="sendSmsAlertToParents"><strong>Send SMS Alert to Parents:</strong></form:label>
															${content.sendSmsAlertToParents}
														</div>
													</div>

												</div>


												<div class="row">

													<div class="col-sm-8 column">

														<c:url value="/addContentForm" var="editurl">
															<c:param name="id" value="${content.id}" />
															<c:param name="courseId" value="${content.courseId}" />
															<c:param name="moduleId" value="${content.moduleId}" />
															<c:param name="contentType"
																value="${content.contentType}" />
														</c:url>
														<div class="form-group">

															<a id="submit" class="btn btn-xs btn-primary"
																href="${editurl}">Edit Folder</a>

															<!-- <button id="selectall" class="btn mt-2 btn-primary"
																formaction="saveStudentContentAllocationForAllStudents">Allocate
																To All Students</button> -->
																<button id="selectall" class="btn mt-2 btn-primary"
																formaction="saveStudentContentAllocationForAllStudentsForModule">Allocate
																To All Students</button>
																
																
																
															<sec:authorize access="hasAnyRole('ROLE_ADMIN')">
																<button id="submit" class="btn mt-2 btn-primary "
																	formaction="${rootFolderForAdmin}">Back to
																	Root Folder</button>
															</sec:authorize>
															<sec:authorize access="hasAnyRole('ROLE_FACULTY')">
																<button id="submit" class="btn mt-2 btn-primary "
																	formaction="${rootFolder}">Back to Root Folder</button>
															</sec:authorize>
															<button id="cancel" class="btn mt-2 btn-danger"
																formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
														</div>
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

						
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h5>Select Students to share Folder With | Folder Shared
											With : ${noOfStudentAllocated} Students</h5>
										
									</div>
									<div class="x_content">
										<form:form action="saveStudentContentAllocationForModule"
											id="saveStudentContentAllocationForModule" method="post"
											modelAttribute="content">
											<fieldset>

												<form:input path="id" type="hidden" />
												<form:input path="courseId" type="hidden" />
												<form:input path="acadMonth" type="hidden" />
												<form:input path="acadYear" type="hidden" />
												<form:input path="facultyId" type="hidden" />
												<form:input path="contentType" type="hidden" />
												<form:input path="moduleId" type="hidden" />
												<%-- <c:if test="${allCampuses.size() > 0}">
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
												</c:if>  --%>
												<div class="table-responsive">
													<table class="table table-hover">
														<thead>
															<tr>
																<th>Sr. No.</th>
																<th>Select (<a onclick="checkAll()">All</a> | <a
																	onclick="uncheckAll()">None</a>)
																</th>
																<th>SapID <i class="fa fa-sort" aria-hidden="true"
																	style="cursor: pointer"></i></th>
																<th>Roll No. <i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>
																<!-- <th>Campus</th> -->
																<th>Student Name <i class="fa fa-sort"
																	aria-hidden="true" style="cursor: pointer"></i></th>
															</tr>
														</thead>
														<tfoot>
															<tr>
																<th></th>
																<th></th>
																<th>SapID</th>
																<th>Roll No.</th>
																<!-- <th>Campus</th> -->
																<th>Student Name</th>
															</tr>
														</tfoot>
														<tbody>

															<c:forEach var="student" items="${students}"
																varStatus="status">
																<tr>
																	<td><c:out value="${status.count}" /></td>
																	<td><c:if test="${empty student.id }">
																			<form:checkbox path="students"
																				value="${student.username}_${student.courseId}" />
																		</c:if> <c:if test="${not empty student.id }">
													            	Folder Shared
													            </c:if></td>
																	<td><c:out value="${student.username}" /></td>
																	<td><c:out value="${student.rollNo}" /></td>
																	<%-- <td><c:out value="${student.campusName}" /></td> --%>
																	<td><c:out
																			value="${student.firstname} ${student.lastname}" /></td>
																</tr>
															</c:forEach>
														</tbody>
													</table>
												</div>

												<div class="col-12">
													<div class="form-group">

													<!-- 	<button id="submit" class="btn btn-primary mt-2"
															formaction="saveStudentContentAllocation"
															onclick="return clicked();">Share Folder with
															Selected Students</button> -->
															
														 	<button id="submit" class="btn btn-primary mt-2"
															formaction="saveStudentContentAllocationForModule"
															onclick="return clicked();">Share Folder with
															Selected Students</button>
															
														<button id="cancel" class="btn btn-danger mt-2"
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
									window.location = '${pageContext.request.contextPath}/viewContent?id='
											+ id
											+ '&contentType=Folder&campusId='
											+ selectedValue;
									+encodeURIComponent(selectedValue);
									return false;
								});
				//var email = document.getElementById('name').value;
			</script>