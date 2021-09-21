<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec"
      uri="http://www.springframework.org/security/tags"%>
      
      <jsp:include page="../common/newDashboardHeader.jsp" /> 

<div class="d-flex adminPage dataTableBottom" id="adminPage">
<jsp:include page="../common/newAdminLeftNavBar.jsp"/>
<jsp:include page="../common/rightSidebarAdmin.jsp" />


<!-- DASHBOARD BODY STARTS HERE -->

<div class="container-fluid m-0 p-0 dashboardWraper">

      <jsp:include page="../common/newAdminTopHeader.jsp" />
     
            <!-- SEMESTER CONTENT -->    
                    <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">
                        
                     <!-- page content: START -->

                                    <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                        <sec:authorize access="hasRole('ROLE_STUDENT')">
                            <c:out value="${AcadSession}" />
                        </sec:authorize>
                        <li class="breadcrumb-item active" aria-current="page"> View All Faculties</li>
                    </ol>
                </nav>
                                    <jsp:include page="../common/alert.jsp" />

                                    <!-- Input Form Panel -->
                                    <div class="card bg-white border">
                                          <div class="card-body">
                                          
                                                            <h5 class="text-center border-bottom pb-2">Faculty List</h5>

                                                            <form:form action="" method="post"
                                                                  modelAttribute="facultyDetails">

                                                                  <div class="table-responsive testAssignTable">
                                                                        <table class="table table-striped table-hover">
                                                                              <thead>
                                                                                    <tr>
                                                                                          <th>Sr. No.</th>
                                                                                          <th>Faculty Username</th>
                                                                                          <th>Action</th>

                                                                                    </tr>
                                                                              </thead>
                                                                              <tbody>

                                                                                    <c:forEach var="faculty" items="${facultyList}"
                                                                                          varStatus="status">
                                                                                          <form:input path="id" value="${faculty.id}" type="hidden" />
                                                                                          <tr>
                                                                                                <td><c:out value="${status.count}" /></td>
                                                                                                <td><c:out value="${faculty.username}"></c:out></td>
                                                                                                <td><c:url value="addFacultyDetailsForm"
                                                                                                            var="viewUrl">
                                                                                                            <c:param name="id" value="${faculty.id}" />
                                                                                                      </c:url> <a href="${viewUrl}" title="View Faculty Details"><i
                                                                                                            class="fa fa-info-circle fa-lg"></i></a></td>



                                                                                          </tr>
                                                                                    </c:forEach>

                                                                              </tbody>
                                                                        </table>
                                                                  </div>
                                                            </form:form>
                                          </div>
                                    </div>
                  <!-- /page content: END -->
                   
                    </div>
                  
                  <!-- SIDEBAR START -->

              <!-- SIDEBAR END -->
      <jsp:include page="../common/newAdminFooter.jsp"/>
      
      

                  
