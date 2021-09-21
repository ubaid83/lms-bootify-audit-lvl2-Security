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
                                <li class="breadcrumb-item active" aria-current="page"> Configure Questions</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
							

						<!-- Results Panel -->

						
								<div class="card bg-white border">
								<div class="card-body">								
									<div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
                                    <div class="x_title">
                                        <h5>Tests Record | ${page.rowCount } Records Found</h5>
                                       
                                    </div>
                                    <div class="x_content">
										<div class="table-responsive">
											<table class="table table-hover ">
											 
												<thead>
									<tr>
										<th>Sr. No.</th>
										<th>Name</th>
										<th>Course</th>
										<th>Academic Month</th>
										<th>Academic Year</th>
										<!-- <th>Start Date</th>
										<th>Due Date</th>
										<th>End Date</th> -->
										<th>Actions</th>
									</tr>
								</thead>
								<tbody>

									<c:forEach var="test" items="${page.pageItems}"
										varStatus="status">
										<tr>
											<td><c:out value="${status.count}" /></td>
											<td><c:out value="${test.testName}" /></td>
											<td><c:out value="${test.courseName}" /></td>
											<td><c:out value="${test.acadMonth}" /></td>
											<td><c:out value="${test.acadYear}" /></td>
										
											<td><c:url value="addTestQuestionForm" var="addTestQuestionUrl">
													<c:param name="id" value="${test.id}" />
												</c:url>  <a
												href="${addTestQuestionUrl}" title="Configure Questions">Configure Questions</a></td>
										</tr>
										
											  </c:forEach></tbody>
											</table>
										</div>
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
	$("#deleteurl").on("click", deleteTest); //when #deleteurl link is clicked, deleteTest() will be called

	function deleteTest() {

		swal(
				{
					title : "Are you sure?",
					text : "You will not be able to recover the test later !",
					type : "warning"
				},
				function(isConfirm) {
					if (isConfirm) {
						var data = {};
						data["id"] = $("#id").html();
						$
								.ajax({
									type : "GET",
									contentType : "application/json",
									url : "${home}/deleteTest",
									data : JSON.stringify(data),
									dataType : "json",
									success : function() {
										swal(
												"Deleted!",
												"The test has been deleted Successfully !!",
												"success");
									},
									error : function() {
										swal(
												"Error",
												"Test Could not be deleted ! :)",
												"error");
									}

								});
					}
				});
	}
</script>