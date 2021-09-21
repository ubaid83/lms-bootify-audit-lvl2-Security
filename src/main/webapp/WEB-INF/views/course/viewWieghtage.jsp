<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<html lang="en">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<jsp:include page="../common/css.jsp" />


<body class="nav-md footer_fixed">


	<!-- <div class="loader"></div> -->

	<div class="container body">
		<div class="main_container">
		 <jsp:include page="../common/leftSidebar.jsp">
				<jsp:param name="courseId" value="${courseId}" />
			</jsp:include> 
			
			<jsp:include page="../common/topHeader.jsp">
			<jsp:param value="gradeMenu" name="activeMenu"/>
			</jsp:include>


			<!-- page content: START -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<div class="dashboard_container_spacing">

						<div class="breadcrumb">
						
						<c:out value="${Program_Name}" />
						
						<sec:authorize access="hasRole('ROLE_STUDENT')">
						
						<i
						class="fa fa-angle-right"></i>
											
											<c:out value="${AcadSession}" />
											
											</sec:authorize>
						<br><br>
							<a href="${pageContext.request.contextPath}/homepage">Dashboard</a>
							<i class="fa fa-angle-right"></i> Wieghtage Details
							${course.courseName }
						</div>
						<jsp:include page="../common/alert.jsp" />

						<!-- Input Form Panel -->
						<div class="row">

							<div class="col-md-12 col-xs-12 col-sm-12">
								<div class="x_panel">


									<div class="x_content">
										<form>
											<div class="row">
												<div class="col-md-4 col-sm-6 col-xs-12">
													<div class="form-group">

														<label for="courseId">Course</label> <select id="courseId"
															name="courseId" class="form-control">
															
																<option value="">Select Course</option>
															
															<c:forEach var="course" items="${coursesForViewWieghtage}"
																varStatus="status">
															
																	<option value="${course.id}">${course.courseName}</option>
															
															</c:forEach>
														</select>
													</div>
												</div>
												</div>

										</form>
									</div>
								</div>
							</div>
						</div>
						
						
						   <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
                                   <div class="ui-105-content">
		<ul class="nav nav-tabs nav-justified">
		
			<li class="link-two" id="link-two"><a href="#register-block" data-toggle="tab"><i class="fa fa-sign-in"></i>Wieghtage Assigned</a></li>
		</ul>
                       <div class="tab-content"  >
			<div class="tab-pane active fade in" id="login-block">
				<!-- Login Block Form -->
				<div class="login-block-form">
                        <form:form cssClass="form" role="form" action=""
						method="post" modelAttribute="wieghtageData" >
							 <div class="table-responsive">
                                    <table id="inboxTable" class="table table-striped table-hover"
                                          style="font-size: 12px">
                                          <thead>
                                                <tr>
                                                      <th>Sr. No.</th>
                                                      <th>Wieghtage Type </th>
                                                      <th>Wieghtage Assigned</th>
                                                      <th>CreatedBy</th>
                                                      <th>Created Date</th>
                                                   
                                                </tr>
                                          </thead>
                                          <tbody>
                                                <c:forEach var="listOfAssignedWieghtage" items="${listOfAssignedWieghtage}"
                                                      varStatus="status">
                                                      <tr>
                                                            <td><c:out value="${status.count}" /></td>
                                                            <td><c:out value="${listOfAssignedWieghtage.wieghtagetype} " /></td>
                                                            <td><c:out value="${listOfAssignedWieghtage.wieghtageassigned} " /></td>
                                                            <td><c:out value="${listOfAssignedWieghtage.createdBy} " /></td>
															<td><c:out value="${listOfAssignedWieghtage.createdDate} " /></td>
															
                                                    </tr>
                                                                        
                                                </c:forEach>
                                               
                                          </tbody>
                                    </table>
                                    
                                    
                              </div>
					</form:form>
                                    </div>
                                </div>
                                    <!-- Results Panel -->
      
                                    </div>
                                </div>
                            </div>
                        </div></div>

						<!-- Results Panel -->
					
					
					</div>



				</div>

			</div>

			<!-- /page content: END -->


			<jsp:include page="../common/footer.jsp" />

		</div>
	</div>







</body>

<script>

$(document).ready(function() {
 

   $("#courseId").on('change',function() {
					var selectedValue = $(this).val();
						window.location = '${pageContext.request.contextPath}/viewWieghtageDetails?courseId='
								+ encodeURIComponent(selectedValue);
						return false;
					});
					});
   
</script>
</html>
