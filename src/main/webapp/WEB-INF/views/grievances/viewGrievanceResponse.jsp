<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec"
      uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />
<body class="nav-md footer_fixed">
    <div class="loader"></div>
    <div class="container body">
        <div class="main_container">
        <jsp:include page="../common/leftSidebar.jsp" />
            <jsp:include page="../common/topHeader.jsp" >
            	<jsp:param value="Grievance" name="activeMenu"/>
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
                                          <i class="fa fa-angle-right"></i> 
                                          Response To Grievances
                                    </div>
                                    <jsp:include page="../common/alert.jsp" />
                                    <!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
                                   <div class="ui-105-content">
		<ul class="nav nav-tabs nav-justified">
			<li class="active link-one"><a href="#login-block"
				data-toggle="tab"><i class="fa fa-inbox" aria-hidden="true"></i>View Grievance Response</a></li>
		</ul>
                       <div class="tab-content"  >
			<div class="tab-pane active fade in" id="login-block">
				<!-- Login Block Form -->
				<div class="login-block-form">
                                        <form:form cssClass="form" role="form" action=""
						method="post" modelAttribute="grievance" >
						
							 <div class="table-responsive">
                                    <table id="grievanceTable" class="table  table-hover"
                                          style="font-size: 12px">
                                          <thead>
                                                <tr>
                                                      <th>Sr. No.</th>
                                                      <th> Grievance Case</th>
                                                      <th>Grievance Status</th>
                                                      <th>Grievance Reason</th>
                                                       <th>Grievance Response</th>
                                                     
                                                </tr>
                                          </thead>
 
                                          <tbody>
 
                                                <c:forEach var="grievance" items="${listofAllGrievances}"
                                                      varStatus="status">
                                                      <tr>
                                                            <td><c:out value="${status.count}" /></td>
                                                            <td><c:out value="${grievance.grievanceCase} " /></td>
                                                            <td><c:out value="${grievance.grievanceStatus}"/></td>
                                                            <td><%-- <button type="button" class="btn btn-danger"
														style="width: 70%" data-toggle="collapse" data-target="#demo">Click to View Reason</button>
														<div id="demo" class="collapse" style="width:30%">
                                                      ${grievance.grievanceReason}
                                                </div> --%>
                                                <c:out value="${grievance.grievanceReason}"/>
                                                
                                                </td>
                                                            <td><%-- <button type="button" class="btn btn-danger"
														style="width: 70%" data-toggle="collapse" data-target="#demo1">Click To View Response</button>
														 <div id="demo1" class="collapse" style="width:30%">
                                                 ${grievance.grievanceResponse}
                                                </div>  --%>
                                                <c:out value="${grievance.grievanceResponse}"/>
                                                  </td>
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

                        </div></div></div>

                       

                    

                   

                    

                        

                </div>

               

            </div>

            <!-- /page content: END -->

           

            

            <jsp:include page="../common/footer.jsp" />

           

        </div>

    </div>

 

 

   

    

    

</body>

</html>