<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


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
                        <li class="breadcrumb-item active" aria-current="page"> Search Course Enrollments</li>
                    </ol>
                </nav>

                                    <jsp:include page="../common/alert.jsp" />
                                    
                                    <!-- Input Form Panel -->
                                    <div class="card bg-white border">
                                          <div class="card-body">


                                                            <h5 class="text-center border-bottom pb-2">Search Course Enrollments</h5>

                                                            <form:form action="searchUser" method="post"
                                                                  modelAttribute="user">
                                                                  
                                                                  <div class="row">
                                                                  <div class="col-md-4 col-sm-12">
                                                                        <div class="form-group">
                                                                              <form:label path="programId" for="program">Program</form:label>
                                                                              <form:select id="program" path="programId"
                                                                                    placeholder="program Id" class="form-control">
                                                                                    <form:option value="">Select Program</form:option>
                                                                                    <form:options items="${programs}" itemLabel="programName"
                                                                                          itemValue="id" />
                                                                              </form:select>
                                                                        </div>
                                                                  </div>

                                                                  <div class="col-md-4 col-sm-12">
                                                                        <div class="form-group">
                                                                        <form:label path="programId" for="program"> Username</form:label>
                                                                              <form:input id="username" path="username" type="text"
                                                                                    placeholder="User Name (Student/Faculty)"
                                                                                    class="form-control" />
                                                                        </div>
                                                                  </div>

                                                                  <div class="col-12">
                                                                        <div class="form-group">
                                                                              <button id="submit" name="submit"
                                                                                    class="btn btn-large btn-primary mt-2">Search</button>
                                                                              <input id="reset" type="reset" class="btn btn-danger mt-2">
                                                                              <button id="cancel" name="cancel" class="btn btn-danger mt-2" formaction="homepage"
                                                                                    formnovalidate="formnovalidate">Cancel</button>
                                                                        </div>
                                                                  </div>
                                                                  </div>
                                                            </form:form>
                                    </div>
                              </div>
                              
                              
                              <c:choose>
                                    <c:when test="${page.rowCount > 0}">
                                          <!-- Results Panel -->
                                          <div class="card bg-white border">
                                                <div class="card-body">
                                                            <div class="x_title">
                                                                  <h5 class="text-center border-bottom pb-2">
                                                                        Enrollment List <small> |
                                                                              ${page.rowCount} Records Found &nbsp; </small>
                                                                  </h5>

                                                                  <div class="table-responsive testAssignTable">
                                                                        <table class="table table-striped table-hover">
                                                                              <thead>
                                                                                    <tr>
                                                                                          <th>Sr. No.</th>

                                                                                          <th>Programs</th>
                                                                                          <th>User Name</th>
                                                                                          <th>Role</th>
                                                                                          <th>Actions</th>
                                                                                    </tr>
                                                                              </thead>
                                                                              <tbody>

                                                                                    <c:forEach var="users" items="${page.pageItems}"
                                                                                          varStatus="status">
                                                                                          <tr>
                                                                                                <td><c:out value="${status.count}" /></td>

                                                                                                <td><c:out value="${users.programId}" /></td>
                                                                                                <td><c:out value="${users.username}" /></td>
                                                                                                <td><c:out value="${users.role}" /></td>

                                                                                                <c:url value="deleteUserFromAll" var="deleteurl">
                                                                                                      <c:param name="username" value="${users.username}" />
                                                                                                </c:url>
                                                                                                <td>

                                                                                                <a href="${deleteurl}" title="Delete"
                                                                                                      onclick="return confirm('Are you sure you want to delete this record?')"><i
                                                                                                      class="fas fa-trash-alt fa-lg"></i></a>


                                                                                                </td>
                                                                                          </tr>
                                                                                    </c:forEach>


                                                                              </tbody>
                                                                        </table>
                                                                  </div>
                                                </div>
                                          </div>
                                          </div>
                                    </c:when>
                              </c:choose>

            <!-- /page content: END -->
                     
                   
                    </div>
                  
                  <!-- SIDEBAR START -->

              <!-- SIDEBAR END -->
      <jsp:include page="../common/newAdminFooter.jsp"/>

