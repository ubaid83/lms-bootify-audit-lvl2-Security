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

<div class="d-flex adminPage" id="adminPage">
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
                                <li class="breadcrumb-item" aria-current="page"><c:out value="${Program_Name}" /></li>
                                <sec:authorize access="hasRole('ROLE_STUDENT')">
                                                <c:out value="${AcadSession}" />
                                          </sec:authorize>
                                <li class="breadcrumb-item active" aria-current="page"> Generic Search </li>
                            </ol>
                        </nav>
                        
                                    <jsp:include page="../common/alert.jsp" />
                                    
                                    
                                    <!-- Input Form Panel -->
                                    <div class="card bg-white border">
                                    <div class="card-body">
                                          <div class="col-xs-12 col-sm-12">
                                                <div class="x_panel">

                                                      <div class="x_title">
                                                            <h5>Generic Search</h5>
                                                            
                                                      </div>

                                                      <div class="x_content">
                                                            <form:form action="searchItem" method="get" name="myform"
                                                                  novalidate="required" modelAttribute="search">



                                                                  <div class="row">
                                                                        <div class="col-sm-4 col-md-4 col-xs-12 column">
                                                                              <div class="form-group">
                                                                                    <form:label path="" for="">Select Assignments/Test/Resource <span style="color: red">*</span></form:label>
                                                                                    <form:select id="searchType" path="searchType"
                                                                                          name="searchType" required="required"
                                                                                          class="form-control">
                                                                                          <form:option value="">Select Assignments/Test/Resource</form:option>

                                                                                          <c:forEach var="list" items="${typeList}"
                                                                                                varStatus="status">
                                                                                                <form:option value="${list}">${list}</form:option>
                                                                                          </c:forEach>

                                                                                    </form:select>
                                                                              </div>
                                                                        </div>

                                                                        <div class="col-sm-4 col-md-4 col-xs-12 column"
                                                                              id="statusDiv">
                                                                              <div class="form-group">
                                                                                    <form:label path="" for="">Select Status <span style="color: red">*</span></form:label>
                                                                                    <form:select id="status" path="status" name="status"
                                                                                          required="required" class="form-control">
                                                                                          <form:option value="">Select Status</form:option>

                                                                                          <c:forEach var="st" items="${statusList}"
                                                                                                varStatus="status">
                                                                                                <form:option value="${st}">${st}</form:option>
                                                                                          </c:forEach>

                                                                                    </form:select>
                                                                              </div>
                                                                        </div>

                                                                        <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                              <div class="form-group">
                                                                                    <form:label path="courseId" for="courseId">Course <span style="color: red">*</span></form:label>
                                                                                    <form:select id="idForCourse" path="courseId"
                                                                                          required="required" class="form-control">
                                                                                          <form:option value="">Select Course</form:option>
                                                                                          <c:forEach var="course" items="${allCourses}"
                                                                                                varStatus="status">
                                                                                                <form:option value="${course.id}">${course.courseName}</form:option>
                                                                                          </c:forEach>
                                                                                    </form:select>
                                                                              </div>
                                                                        </div>

                                                                        <div class="col-sm-12 column">
                                                                              <div class="form-group">
                                                                                    <button id="submitId" name="submit"
                                                                                          class="btn btn-large btn-primary">Search</button>
                                                                                    <input id="reset" type="reset" class="btn btn-danger">


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
                                          <c:when test="${allTests.size() > 0}">
                                    
                                                <div class="card bg-white border">
                                                <div class="card-body">                                           
                                                      <div class="col-xs-12 col-sm-12">
                                                            <div class="x_panel">
                                                                  <div class="x_title">
                                                                        <h5>
                                                                              Tests<font size="2px"> | ${allTests.size()} Records
                                                                                    Found &nbsp; </font>
                                                                        </h5>
                                                                  
                                                                  </div>
                                                                  <div class="x_content">
                                                                        <div class="table-responsive">
                                                                              <table class="table table-hover" style="font-size: 12px">
                                                                                    <thead>
                                                                                          <tr>
                                                                                                <th>Sr. No.</th>
                                                                                                <th>Name <i class="fa fa-sort" aria-hidden="true"
                                                                                                      style="cursor: pointer"></i></th>
                                                                                                <th>Student Username <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>

                                                                                                <th>Attempts <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <th>Start Time <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <th>End Time <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <th>Test Completed <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <th>Score <i class="fa fa-sort" aria-hidden="true"
                                                                                                      style="cursor: pointer"></i></th>
                                                                                          </tr>
                                                                                    </thead>
                                                                                    <tbody>

                                                                                          <c:forEach var="test" items="${allTests}"
                                                                                                varStatus="status">
                                                                                                <tr>
                                                                                                      <td><c:out value="${status.count}" /></td>
                                                                                                      <td><c:out value="${test.testName}" /></td>
                                                                                                      <td><c:out value="${test.username}" /></td>
                                                                                                      <td><c:out value="${test.attempt}" /></td>

                                                                                                      <td><c:out
                                                                                                                  value="${fn:replace(test.testStartTime,'T', ' ')}"></c:out></td>
                                                                                                      <td><c:out
                                                                                                                  value="${fn:replace(test.testEndTime, 
                                'T', ' ')}"></c:out></td>

                                                                                                      <td><c:if test="${test.testCompleted eq 'Y' }">
                                                      Yes
                                                                                        </c:if> <c:if
                                                                                                                  test="${test.testCompleted ne 'Y' }">
                                                No
                                                
                                                                                                            
                                                                                                            
                                                                                                            </c:if></td>
                                                                                                      <td><c:out value="${test.score}" /></td>
                                                                                                </tr>
                                                                                          </c:forEach>


                                                                                    </tbody>
                                                                              </table>
                                                                        </div>
                                                                  </div>
                                                            </div>
                                                      </div>
                                                </div>
                                                </div>
                                          </c:when>
                                          </c:choose>
                                          
                                                                              <!-- Results Panel -->
                                                <c:choose>
                                          <c:when test="${allContent.size() > 0}">
                                    
                                                <div class="card bg-white border">
                                                <div class="card-body">                                           
                                                      <div class="col-xs-12 col-sm-12">
                                                            <div class="x_panel">
                                                                  <div class="x_title">
                                                                        <h5>
                                                                              Resources<font size="2px"> | ${allContent.size()}
                                                                                    Records Found &nbsp; </font>
                                                                        </h5>
                                                                        
                                                                  </div>
                                                                  <div class="x_content">
                                                                        <div class="table-responsive">
                                                                              <table class="table table-hover" style="font-size: 12px">
                                                                                    <thead>
                                                                                          <tr>
                                                                                                <th>Sr. No.</th>
                                                                                                <th>Name <i class="fa fa-sort" aria-hidden="true"
                                                                                                      style="cursor: pointer"></i></th>
                                                                                                <th>Description</th>

                                                                                                <th>Created By</th>
                                                                                                <th>Content Type <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <th>View Count <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <th>Access Type <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <th colspan="2">Action</th>


                                                                                          </tr>
                                                                                    </thead>
                                                                                    <tbody>


                                                                                          <c:forEach var="content" items="${allContent}"
                                                                                                varStatus="status">
                                                                                                <tr data-tt-id="${content.id}"
                                                                                                      data-tt-parent-id="${content.parentContentId}">
                                                                                                      <td><c:out value="${status.count}" /></td>

                                                                                                      <td><c:out value="${content.contentName}" /></td>

                                                                                                      <td><c:out value="${content.contentDescription}" /></td>
                                                                                                      <td><c:out value="${content.createdBy}" /></td>
                                                                                                      <td><c:out value="${content.contentType}" /></td>
                                                                                                      <td><c:out value="${content.count}" /></td>
                                                                                                      <td><c:out value="${content.accessType}" /></td>


                                                                                                      <td><sec:authorize
                                                                                                                  access="hasAnyRole('ROLE_STUDENT')">
                                                                                                                  <c:if test="${content.contentType == 'Folder' }">
                                                                                                                        <i class="fa lms-folder-o fa-lg"
                                                                                                                              style="background: #E6CB47; margin-right: 5px"></i>

                                                                                                                        <c:url value="/getContentUnderAPathForStudent"
                                                                                                                              var="navigateInsideFolder">
                                                                                                                              <c:param name="courseId"
                                                                                                                                    value="${content.courseId}" />
                                                                                                                              <c:param name="acadMonth"
                                                                                                                                    value="${content.acadMonth}" />
                                                                                                                              <c:param name="acadYear"
                                                                                                                                    value="${content.acadYear}" />
                                                                                                                              <c:param name="folderPath"
                                                                                                                                    value="${content.filePath}" />
                                                                                                                              <c:param name="parentContentId"
                                                                                                                                    value="${content.id}" />
                                                                                                                        </c:url>
                                                                                                                        <a href="${navigateInsideFolder}"
                                                                                                                              class="clickedFolder" id="folder${content.id}"><c:out
                                                                                                                                    value="${content.contentName}" /></a>
                                                                                                                  </c:if>

                                                                                                            </sec:authorize> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
                                                                                                                  <c:if test="${content.contentType == 'Folder' }">
                                                                                                                        <i class="fa lms-folder-o fa-lg"
                                                                                                                              style="background: #E6CB47; margin-right: 5px"></i>

                                                                                                                        <c:url value="/getContentUnderAPathForFaculty"
                                                                                                                              var="navigateInsideFolder">
                                                                                                                              <c:param name="courseId"
                                                                                                                                    value="${content.courseId}" />
                                                                                                                              <c:param name="acadMonth"
                                                                                                                                    value="${content.acadMonth}" />
                                                                                                                              <c:param name="acadYear"
                                                                                                                                    value="${content.acadYear}" />
                                                                                                                              <c:param name="folderPath"
                                                                                                                                    value="${content.filePath}" />
                                                                                                                              <c:param name="parentContentId"
                                                                                                                                    value="${content.id}" />
                                                                                                                        </c:url>
                                                                                                                        <a href="${navigateInsideFolder}"><c:out
                                                                                                                                    value="${content.contentName}" /></a>
                                                                                                                  </c:if>

                                                                                                            </sec:authorize> <sec:authorize access="hasAnyRole('ROLE_ADMIN')">
                                                                                                                  <c:if test="${content.contentType == 'Folder' }">
                                                                                                                        <i class="fa lms-folder-o fa-lg"
                                                                                                                              style="background: #E6CB47; margin-right: 5px"></i>

                                                                                                                        <c:url value="/getContentUnderAPath"
                                                                                                                              var="navigateInsideFolder">
                                                                                                                              <c:param name="courseId"
                                                                                                                                    value="${content.courseId}" />
                                                                                                                              <c:param name="acadMonth"
                                                                                                                                    value="${content.acadMonth}" />
                                                                                                                              <c:param name="acadYear"
                                                                                                                                    value="${content.acadYear}" />
                                                                                                                              <c:param name="folderPath"
                                                                                                                                    value="${content.filePath}" />
                                                                                                                              <c:param name="parentContentId"
                                                                                                                                    value="${content.id}" />
                                                                                                                        </c:url>
                                                                                                                        <a href="${navigateInsideFolder}"><c:out
                                                                                                                                    value="${content.contentName}" /></a>
                                                                                                                  </c:if>

                                                                                                            </sec:authorize> <c:if test="${content.contentType == 'File' }">



                                                                                                                  <a href="downloadFile?filePath=${content.filePath}"
                                                                                                                        class="clickedFile" id="file${content.id}"> <i
                                                                                                                        class="fa fa-cloud-download"></i> Download
                                                                                                                  </a>

                                                                                                                  <%-- <a href="downloadFile?filePath=${content.filePath}"><i
                                                                                                                        class="fa fa-cloud-download"></i> Download</a> --%>


                                                                                                            </c:if> <c:if test="${content.contentType == 'Link' }">
                                                                                                                  <i class="fa fa-link fa-lg" style="margin-right: 5px"></i>

                                                                                                                  <a href="${content.linkUrl}" target="_blank"
                                                                                                                        class="clickedLink" id="link${content.id}"> <c:out
                                                                                                                              value="${content.contentName}" />
                                                                                                                  </a>

                                                                                                                  <%-- <a href="${content.linkUrl}" target="_blank"> <c:out
                                                                                                                              value="${content.contentName}" />
                                                                                                                  </a> --%>

                                                                                                            </c:if></td>



                                                                                                      <td><sec:authorize
                                                                                                                  access="hasAnyRole('ROLE_STUDENT')">
                                                                                                                  <c:if test="${content.contentType == 'Folder' }">
                                                                                                                        <!-- <i class="fa lms-folder-o fa-lg"
                                                                        style="background: #E6CB47; margin-right: 5px"></i> -->

                                                                                                                        <c:url value="/getContentUnderAPathForStudent"
                                                                                                                              var="navigateInsideFolder">
                                                                                                                              <c:param name="courseId"
                                                                                                                                    value="${content.courseId}" />
                                                                                                                              <c:param name="acadMonth"
                                                                                                                                    value="${content.acadMonth}" />
                                                                                                                              <c:param name="acadYear"
                                                                                                                                    value="${content.acadYear}" />
                                                                                                                              <c:param name="folderPath"
                                                                                                                                    value="${content.filePath}" />
                                                                                                                              <c:param name="parentContentId"
                                                                                                                                    value="${content.id}" />
                                                                                                                        </c:url>
                                                                                                                        <a href="${navigateInsideFolder}"
                                                                                                                              class="clickedFolder" id="folder${content.id}">View</a>
                                                                                                                  </c:if>

                                                                                                            </sec:authorize> <sec:authorize access="hasAnyRole('ROLE_FACULTY')">
                                                                                                                  <c:if test="${content.contentType == 'Folder' }">
                                                                                                                        <!-- <i class="fa lms-folder-o fa-lg"
                                                                        style="background: #E6CB47; margin-right: 5px"></i> -->

                                                                                                                        <c:url value="/getContentUnderAPathForFaculty"
                                                                                                                              var="navigateInsideFolder">
                                                                                                                              <c:param name="courseId"
                                                                                                                                    value="${content.courseId}" />
                                                                                                                              <c:param name="acadMonth"
                                                                                                                                    value="${content.acadMonth}" />
                                                                                                                              <c:param name="acadYear"
                                                                                                                                    value="${content.acadYear}" />
                                                                                                                              <c:param name="folderPath"
                                                                                                                                    value="${content.filePath}" />
                                                                                                                              <c:param name="parentContentId"
                                                                                                                                    value="${content.id}" />
                                                                                                                        </c:url>
                                                                                                                        <a href="${navigateInsideFolder}">View</a>
                                                                                                                  </c:if>

                                                                                                            </sec:authorize> <sec:authorize access="hasAnyRole('ROLE_ADMIN')">
                                                                                                                  <c:if test="${content.contentType == 'Folder' }">
                                                                                                                        <!-- <i class="fa lms-folder-o fa-lg"
                                                                        style="background: #E6CB47; margin-right: 5px"></i> -->

                                                                                                                        <c:url value="/getContentUnderAPath"
                                                                                                                              var="navigateInsideFolder">
                                                                                                                              <c:param name="courseId"
                                                                                                                                    value="${content.courseId}" />
                                                                                                                              <c:param name="acadMonth"
                                                                                                                                    value="${content.acadMonth}" />
                                                                                                                              <c:param name="acadYear"
                                                                                                                                    value="${content.acadYear}" />
                                                                                                                              <c:param name="folderPath"
                                                                                                                                    value="${content.filePath}" />
                                                                                                                              <c:param name="parentContentId"
                                                                                                                                    value="${content.id}" />
                                                                                                                        </c:url>
                                                                                                                        <a href="${navigateInsideFolder}">View</a>
                                                                                                                  </c:if>

                                                                                                            </sec:authorize>
                                                                                                </tr>
                                                                                          </c:forEach>



                                                                                    </tbody>
                                                                              </table>
                                                                        </div>
                                                                  </div>
                                                            </div>
                                                      </div>
                                                </div>
                                                </div>
                                          
                                          </c:when>
                                          </c:choose>
                                          
                                                                              <!-- Results Panel -->
                                          <c:choose>
                                          <c:when test="${allAssignments.size() > 0}">
                                    
                                                <div class="card bg-white border">
                                                <div class="card-body">                                           
                                                      <div class="col-xs-12 col-sm-12">
                                                            <div class="x_panel">
                                                                  <div class="x_title">
                                                                        <h5>
                                                                              Assignments<font size="2px"> |
                                                                                    ${allAssignments.size()} Records Found &nbsp; </font>
                                                                        </h5>
                                                                        
                                                                  </div>
                                                                  <div class="x_content">
                                                                        <div class="table-responsive">
                                                                              <table class="table table-hover" style="font-size: 12px">
                                                                                    <thead>
                                                                                          <tr>
                                                                                                <th>Sr. No.</th>
                                                                                                <th>Name <i class="fa fa-sort" aria-hidden="true"
                                                                                                      style="cursor: pointer"></i></th>
                                                                                                <th>Student Username <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>

                                                                                                <th>Start Date <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <th>End Date <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <th>Submission Date <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <th>Assignment File</th>
                                                                                                <th>Submitted File</th>
                                                                                                <th>Attempts <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <!-- <th>Submission Status</th> -->
                                                                                                <th>Evaluation Status <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <th>Score <i class="fa fa-sort" aria-hidden="true"
                                                                                                      style="cursor: pointer"></i></th>
                                                                                                <th>Remarks <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>



                                                                                          </tr>
                                                                                    </thead>
                                                                                    <tbody>

                                                                                          <c:forEach var="assignment" items="${allAssignments}"
                                                                                                varStatus="status">
                                                                                                <tr>
                                                                                                      <td><c:out value="${status.count}" /></td>
                                                                                                      <td><c:out value="${assignment.assignmentName}" /></td>
                                                                                                      <td><c:out value="${assignment.username}" /></td>


                                                                                                      <td><c:out
                                                                                                                  value="${fn:replace(assignment.startDate,'T', ' ')}"></c:out></td>
                                                                                                      <td><c:out
                                                                                                                  value="${fn:replace(assignment.endDate, 
                                'T', ' ')}"></c:out></td>
                                                                                                      <td><c:out
                                                                                                                  value="${fn:replace(assignment.submissionDate, 
                                'T', ' ')}"></c:out></td>

                                                                                                      <td><c:if
                                                                                                                  test="${assignment.showFileDownload eq 'true'}">
                                                                                                                  <a href="downloadFile?id=${assignment.assignmentId}">Download</a>
                                                                                                            </c:if> <c:if
                                                                                                                  test="${assignment.showFileDownload eq 'false'}">No File</c:if>
                                                                                                      </td>
                                                                                                      <td><c:if
                                                                                                                  test="${assignment.showStudentFileDownload eq 'true'}">
                                                                                                                  <a href="downloadFile?saId=${assignment.id }">Download
                                                                                                                        Answer File</a>
                                                                                                            </c:if> <c:if
                                                                                                                  test="${assignment.showStudentFileDownload eq 'false'}">No File</c:if></td>
                                                                                                      <td><c:out value="${assignment.attempts}" /></td>
                                                                                                      <%-- <td><c:out value="${assignment.submissionStatus}" /></td> --%>
                                                                                                      <td><c:out value="${assignment.evaluationStatus}" /></td>
                                                                                                      <td><c:out value="${assignment.score}" /></td>
                                                                                                      <td><c:out value="${assignment.remarks}" /></td>

                                                                                                </tr>
                                                                                          </c:forEach>


                                                                                    </tbody>
                                                                              </table>
                                                                        </div>
                                                                  </div>
                                                            </div>
                                                      </div>
                                                </div>
                                                </div>
                                          </c:when>
                                          </c:choose>
                                          
                                          
                                          
                                          
                                                                              <!-- Results Panel -->

                                          <c:choose>
                                          <c:when test="${allFeedbacks.size() > 0}">
                                                <div class="card bg-white border">
                                                <div class="card-body">                                           
                                                      <div class="col-xs-12 col-sm-12">
                                                            <div class="x_panel">
                                                                  <div class="x_title">
                                                                        <h5>
                                                                              Student Feedbacks<font size="2px"> |
                                                                                    ${allFeedbacks.size()} Records Found &nbsp; </font>
                                                                        </h5>
                                                                  
                                                                  </div>
                                                                  <div class="x_content">
                                                                        <div class="table-responsive">
                                                                              <table class="table table-hover" style="font-size: 12px">
                                                                                    <thead>
                                                                                          <tr>
                                                                                                <th>Sr. No.</th>
                                                                                                <th>Name <i class="fa fa-sort" aria-hidden="true"
                                                                                                      style="cursor: pointer"></i></th>


                                                                                                <th>Start Date <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <th>End Date <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>

                                                                                                <th>Student Username <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>
                                                                                                <th>Faculty <i class="fa fa-sort"
                                                                                                      aria-hidden="true" style="cursor: pointer"></i></th>





                                                                                          </tr>
                                                                                    </thead>
                                                                                    <tbody>

                                                                                          <c:forEach var="feedback" items="${allFeedbacks}"
                                                                                                varStatus="status">
                                                                                                <tr>
                                                                                                      <td><c:out value="${status.count}" /></td>
                                                                                                      <td><c:out value="${feedback.feedbackName}" /></td>


                                                                                                      <td><c:out
                                                                                                                  value="${fn:replace(feedback.startDate,'T', ' ')}"></c:out></td>
                                                                                                      <td><c:out
                                                                                                                  value="${fn:replace(feedback.endDate, 
                                'T', ' ')}"></c:out></td>


                                                                                                      <td><c:out value="${feedback.username}" /></td>
                                                                                                      <td><c:out value="${feedback.facultyId}" /></td>


                                                                                                </tr>
                                                                                          </c:forEach>


                                                                                    </tbody>
                                                                              </table>
                                                                        </div>
                                                                  </div>
                                                            </div>
                                                      </div>
                                                </div>
                                                </div>
                                          </c:when>
                                          </c:choose>

                              <%-- <jsp:include page="../common/paginate.jsp">
                                          <jsp:param name="baseUrl" value="searchFacultyGroups" />
                                    </jsp:include> --%>

                  <!-- /page content: END -->
                   
                    </div>
			
			<!-- SIDEBAR START -->

	        <!-- SIDEBAR END -->
	<jsp:include page="../common/newAdminFooter.jsp"/>


                   
                 
                  <script>
                        $(document).ready(function() {

                              $("#searchType").on('change', function() {
                                    var selectedValue = $(this).val();
                                    var str = "Resource";
                                    //alert("Selected value is "+selectedValue);
                                    if (selectedValue == str) {
                                          $('#statusDiv').hide();
                                    } else {
                                          //alert("ELSE");
                                          $('#statusDiv').show();
                                    }

                                    //alert("Onchange Function called!");

                              });
                              $('#searchType').trigger('change');

                        });
                  </script>
