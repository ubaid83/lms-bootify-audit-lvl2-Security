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
                                <li class="breadcrumb-item active" aria-current="page"> Create Group</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5 class="text-center pb-2 border-bottom">Create Random groups For ${groups.course.courseName }</h5>
										
									</div>

									<div class="x_content">
										<form:form action="createRandomGroups" id="createGroup"
											method="post" modelAttribute="groups"
											enctype="multipart/form-data">

											<form:input path="courseId" type="hidden" />


											<div class="row"></div>

											<div class="row">
												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<form:label path="" for="">Total No. of Students : </form:label>
														<c:out value="${totalStudents}"></c:out>
													</div>

												</div>
											</div>
											<hr>
											<p>
												<b>Note:</b>
											<ul>
												<li>Extra students will be added in last group.</li>
												<li>Random characters will be appended in Group name</li>
												<li>Example : ${groups.course.courseName } Group 1
													nB</li>


											</ul>
											<hr>


											<div class="row">

												<div class="col-sm-6 col-md-4 col-xs-12 column">
													<div class="form-group">
														<p>
															<form:label path="noOfStudents" for="noOfStudents">Number of students for each group <span style="color: red">*</span> : </form:label>
															<form:input path="noOfStudents" type="number"
																max="${totalStudents}" class="form-control"
																required="required" />
													</div>
												</div>
											</div>


											<hr>

												<form:label path="group_details" for="editor">Group Description</form:label>
											<%-- 	<form:textarea class="form-group" path="group_details"
													id="editor" style="margin-top: 30px;margin-bottom:10px" /> --%>
													<form:textarea class="form-group testDesc" path="group_details" name="editor1" id="editor1" rows="10" cols="80" />






											<hr>
											<div class="row">

												<div class="col-12">
													<div class="form-group">



														<button id="submit" class="btn btn-large btn-success"
															formaction="createRandomGroups">Create Groups</button>

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
						</div>

						<!-- Results Panel -->
							<c:choose>
							<c:when test="${groupList.size() > 0}">
						
								<div class="card bg-white border">
								<div class="card-body">								
											<div class="x_title">
												<h5 class="text-center pb-2 border-bottom">Groups | ${fn:length(groupList)} Records Found</h5>
												
											</div>
											<div class="x_itemCount" style="display: none;">
												<div class="image_not_found">
													<i class="fa fa-newspaper-o"></i>
													<p>
														<label class=""></label>${fn:length(groupList)} Groups
													</p>
												</div>
											</div>
											<div class="x_content">
												<form:form action="searchFacultyGroups" method="post"
													modelAttribute="groups">
													<form:input path="courseId" type="hidden" />
													<div class="table-responsive testAssignTable">
														<table class="table table-striped  table-hover">
															<thead>
																<tr>
																	<th>Sr. No.</th>
																	<th>Session Month</th>
																	<th>Session Year</th>
																	<th>Course</th>
																	<th>Group Name</th>
																	<th>No. of students</th>



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
																			</c:url> <c:if test="${group.createdBy == username}">
																				<a href="${deleteurl}" title="Delete"
																					onclick="return confirm('Are you sure you want to delete this record?')"><i
																					class="fa fa-trash-o fa-lg"></i></a>

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
	<jsp:include page="../common/footer.jsp"/>
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script>
	