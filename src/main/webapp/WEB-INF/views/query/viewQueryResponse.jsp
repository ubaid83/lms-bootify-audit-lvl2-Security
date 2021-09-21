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
                    <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
                        
          <!-- page content: START -->
          
                        <nav aria-label="breadcrumb">
                            <ol class="breadcrumb">
                                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
								<c:out value="${AcadSession}" />
							</sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Response To Query</li>
                            </ol>
                        </nav>
                        
						<jsp:include page="../common/alert.jsp" />
						
						
						<!-- Input Form Panel -->
						<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
                                   <div class="ui-105-content">
		<ul class="nav nav-tabs nav-justified">
			<li class="active link-one"><a class="btn btn-large btn-danger" href="#login-block"
				data-toggle="tab"><i class="fa fa-inbox" aria-hidden="true"></i>View Query Response</a></li>
		</ul>
                       <div class="tab-content"  >
			<div class="tab-pane active fade in" id="login-block">
				<!-- Login Block Form -->
				<div class="login-block-form">
                                        <form:form cssClass="form" role="form" action=""
						method="post" modelAttribute="queryResponse" >
						
							 <div class="table-responsive">
                                    <table id="grievanceTable" class="table  table-hover"
                                          style="font-size: 12px">
                                          <thead>
                                                <tr>
                                                      <th>Sr. No.</th>
													  
                                                      <th> Query Description</th>
													  <th>Query Response</th>
													  <th>Query Responded By </th>
                                                      <th> Time </th>
                                                     
                                                </tr>
                                          </thead>
 
                                          <tbody>
 
                                                <c:forEach var="listOfMyQueries" items="${listOfMyQueries}"
                                                      varStatus="status">
                                                      <tr>
                                                            <td><c:out value="${status.count}" /></td>
                                                            <td><c:out value="${listOfMyQueries.queryDesc} " /></td>
                                                            <td><c:out value="${listOfMyQueries.queryResponse}"/></td>
															 <td><c:out value="${listOfMyQueries.queryRespondedBy}"/></td>
															  <td><c:out value="${listOfMyQueries.queryRespondedTime}"/></td>
															
                                                            
                                                      </tr>      
                                                      
                                                                             
                                                </c:forEach>
                                                
                                          </tbody>
 
                                    </table>
                                    
                              </div>
						

					</form:form>

                                    </div>

                                </div>
                  


                                    </div>
                                    <div class="row">
												<div class="col-sm-4 column">
													<div class="form-group">

														<a href="homepage"><button id="cancel"
																class="btn btn-large btn-danger"
																style="margin-left: 150%">Back</button></a>
													</div>
												</div>

											</div>

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
