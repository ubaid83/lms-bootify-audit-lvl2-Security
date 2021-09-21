<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<jsp:include page="../common/css.jsp" />

<body class="nav-md footer_fixed">
    
    <div class="loader"></div>
    
    
    <div class="container body">
        <div class="main_container">
            
        <jsp:include page="../common/leftSidebar.jsp">
			<jsp:param name="courseId" value="${courseId}" />
		</jsp:include>    
		<jsp:include page="../common/topHeader.jsp" />
        <%-- <jsp:include page="../common/alert.jsp" /> --%>
            
            
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
							Response To Grievance
						</div>
                        <jsp:include page="../common/alert.jsp" />
						<!-- Input Form Panel -->
                        <div class="row">
                            <div class="col-xs-12 col-sm-12">
                                <div class="x_panel">
								
                                    <div class="x_title">
                                        <h2>Response To Grievance</h2>
                                        <ul class="nav navbar-right panel_toolbox">
                                            <li><a class="collapse-link"><i class="fa fa-chevron-up"></i></a></li>
                                        </ul>
                                        <div class="clearfix"></div>
                                    </div>
									
                                    <div class="x_content">
                                        <form:form action="grievanceForm" method="post"
                        modelAttribute="grievance">
                        <form:hidden path="id" value="${grievanceId}" />
                        <fieldset>
 
 
                             <div class="col-sm-12 column">
                                    <div class="form-group">
                                          <!--<form:label path="description" for="description">Example textarea</form:label>-->
                                          <form:label path="grievanceCase" for="grievanceCase">Grievance Case</form:label>
                                          <form:textarea class="form-control" path="grievanceCase"
                                                id="description" rows="2" readonly="true"/>
 
                                    </div>
                                    </div>
                                    <div class="col-sm-12 column">
                                    <form:label path="grievanceStatus" for="grievanceStatus">Select Status <span style="color: red">*</span></form:label>
                                    <form:select id="grievanceStatus" path="grievanceStatus"
                                          class="form-control" required="required">
                                          <form:option value="Resolved">Resolved</form:option>
                                           <form:option value="Please contact admission department"> Please contact admission department</form:option>
                                            <form:option value="Please contact examination department"> Please contact examination department</form:option>
                                          <form:option value="Will be fixed later">Will be fixed later</form:option>
                                    </form:select>
                                    </div>
                                    <div class="col-sm-12 column">
                                          <div class="form-group">
                                                <!--<form:label path="description" for="description">Example textarea</form:label>-->
                                                <form:label path="grievanceReason" for="grievanceReason">Reason</form:label>
                                                <form:textarea class="form-control" path="grievanceReason"
                                                      id="grievanceReason" rows="2" placeholder="ENTER REASON" />
                                          </div>
                                    </div>
                                   
                              <div class="col-sm-12 column">
                                    <div class="form-group">
                                          <!--<form:label path="description" for="description">Example textarea</form:label>-->
                                          <form:label path="description" for="description">Grievance Description</form:label>
                                          <form:textarea  class="form-control" path="description"
                                                id="description" rows="5"  readonly="true" />
 
                                    </div>
                                   </div>
                                   
 
                                    <div class="col-sm-12 column">
                                          <div class="form-group">
                                                <!--<form:label path="description" for="description">Example textarea</form:label>-->
                                                <form:label path="grievanceResponse" for="grievanceResponse">Response</form:label>
                                                <form:textarea class="form-control" path="grievanceResponse"
                                                      id="grievanceResponse" rows="10" placeholder="ENTER RESPONSE" />
 
                                                <button type="submit" class="btn btn-primary"
                                                      formaction="saveGrievanceResponse">Submit</button>
                                          </div>
                                    </div>
 
                          
 
                        </fieldset>
                  </form:form>
                                    </div>
                                </div>
                            </div>
                        </div>
                        
						<!-- Results Panel -->
                       
                        
                    </div>
                    
                    
                        
                </div>
                
            </div>
            <!-- /page content: END -->
            
            
            <jsp:include page="../common/footer.jsp" />
            
        </div>
    </div>


    
      <jsp:include page="../common/paginate.jsp">
            <jsp:param name="baseUrl" value="evaluateByStudent" />
      </jsp:include>
    
</body>
</html>
