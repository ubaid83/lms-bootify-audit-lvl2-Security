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
                                <li class="breadcrumb-item active" aria-current="page"> Faculty Allocation</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>Faculty Allocation</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                        <form:form action="saveFacultyTestAllocation" method="post" modelAttribute="testObj">
				<fieldset>
					
					<form:input path="courseId" type="hidden" />
					<form:input path="id" type="hidden" /> 
		
				
						<div class="table-responsive">
						<table class="table table-hover" style="font-size:12px">
							<thead>
							<tr>
								<th>Sr. No.</th>
								<th>Tests</th>
								<th>Created By</th>
								<th>Assign To New Faculty</th>
							</tr>
							</thead>
							<tbody>
							
							<c:forEach var="tst" items="${list1}" varStatus="status">
						        <tr>
						            <td><c:out value="${status.count}" /></td>
									<td><c:out value="${tst.testName}"/> </td>
									<td><c:out value=""/> ${tst.createdBy} </td>
									<td>
									<form:label path="facultyId" for="facultyId">Set Faculty</form:label>
								<form:select id="facultyId" path="facultyId" class="form-control">
									<form:option value="">Select Faculty</form:option>
									<c:forEach var="faculty" items="${allFaculty}" varStatus="status">
										<form:option value="${faculty.username}">${faculty.username}</form:option>
									</c:forEach>
								</form:select></td>
						        </tr>   
						    </c:forEach> 
							</tbody>
					</table>
					</div>
					
					<div class="col-sm-4 column">
						<div class="form-group">
							<button id="cancel" class="btn btn-large btn-danger"
								formaction="saveFacultyTestAllocation" formnovalidate="formnovalidate">Save</button>
						
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
	<jsp:include page="../common/footer.jsp"/>

	













