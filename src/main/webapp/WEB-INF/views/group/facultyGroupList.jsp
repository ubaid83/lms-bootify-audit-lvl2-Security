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
                                <li class="breadcrumb-item active" aria-current="page"> Group List </li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5>My Groups</h5>
										
									</div>

									<div class="x_content">
										<form:form action="searchFacultyGroups" method="post"
											modelAttribute="groups">


											<form:input path="courseId" type="hidden" />
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12 column">
													<div class="form-group">
														<form:label class="textStrong" path="courseId" for="courseId">Course</form:label>
														<form:select id="courseIdForForum" path="courseId"
															class="form-control">
															<form:option value="">Select Course</form:option>
															<c:forEach var="course" items="${allCourses}"
																varStatus="status">
																<form:option value="${course.id}">${course.courseName}</form:option>
															</c:forEach>
														</form:select>
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
										<h5>Groups | ${fn:length(groupList)} Records Found</h5>
										
									</div>
									<div class="x_itemCount" style="display: none;">
										<div class="image_not_found">
											<i class="fa fa-newspaper-o"></i>
											<p>
												<label class=""></label>${fn:length(allForums)} Forums
											</p>
										</div>
									</div>
									<div class="x_content">
										<form:form action="searchFacultyGroups" method="post"
											modelAttribute="groups">
											<form:input path="courseId" type="hidden" />
											<div class="table-responsive">
												<table class="table  table-hover">
													<thead>
														<tr>
															<th>Sr. No.</th>
															<th>Session Month <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Session Year <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
															<th>Course</th>
															<th>Group Name <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>
															<th>No. of students <i class="fa fa-sort" aria-hidden="true" style="cursor: pointer"></i></th>



															<th>Actions</th>
														</tr>
													</thead>
													<tfoot>
														<tr>
															<th></th>
															<th>Session Month</th>
															<th>Session Year</th>
															<th>Course</th>
															<th>Group Name</th>
															<th>No. of students</th>

														</tr>
													</tfoot>
													<tbody>

														<c:forEach var="groups" items="${groupList}"
															varStatus="status">
															<tr>
																<td><c:out value="${status.count}" /></td>
																<td><c:out value="${groups.acadMonth}" /></td>
																<td><c:out value="${groups.acadYear}" /></td>
																<td><c:out value="${groups.courseName}" /></td>
																<td><c:out value="${groups.groupName}" /></td>
																<td><c:out value="${groups.noOfStudents}" /></td>




																<td><c:url value="viewGroup" var="viewUrl">
																		<c:param name="id" value="${groups.id}" />
																	</c:url> <a href="${viewUrl}" title="Group Details"><i
																		class="fa fa-info-circle fa-lg"></i></a>&nbsp; <c:url
																		value="viewGroupStudents" var="viewStudentsurl">
																		<c:param name="id" value="${groups.id}" />
																	</c:url> <a href="${viewStudentsurl}" title="Group Members"><i
																		class="fa fa-users"></i></a>&nbsp; <c:url
																		value="deleteGroup" var="deleteurl">
																		<c:param name="id" value="${groups.id}" />
																		<c:param name="courseId" value="${groups.courseId}" />
																	</c:url> <c:if test="${group.createdBy == username}">
																		<a href="${deleteurl}" title="Delete"
																			onclick="return confirm('Are you sure you want to delete this record?')"><i
																			class="fas fa-trash"></i></a>

																	</c:if></td>
															</tr>
														</c:forEach>

													</tbody>
												</table>
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
				$("#courseIdForForum")
						.on(
								'change',
								function() {

									//alert("Onchange Function called!");
									var selectedValue = $(this).val();
									window.location = '${pageContext.request.contextPath}/searchFacultyGroups?courseId='
											+ encodeURIComponent(selectedValue);
									return false;
								});
			</script>