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
                                <li class="breadcrumb-item active" aria-current="page"> Search Assignments/Tests</li>
                            </ol>
                        </nav>
                        
                                    <jsp:include page="../common/alert.jsp" />
                                    
                                    
                                    <!-- Input Form Panel -->
                                    <div class="card bg-white border">
                                    <div class="card-body">
                                          <div class="col-xs-12 col-sm-12">
                                                <div class="x_panel">

                                                      <div class="x_title">
                                                            <h5>Search Assignments/Tests</h5>
                                                            
                                                      </div>

                                                      <div class="x_content">
                                                            <form:form action="searchAssignmentTest" method="post"
                                                                  modelAttribute="assignment">



                                                                  <div class="row">
                                                                        <div class="col-sm-4 col-md-4 col-xs-12 column">
                                                                              <div class="form-group">
                                                                                    <form:label path="acadMonth" for="acadMonth">Academic Month </form:label>
                                                                                    <form:select id="acadMonth" path="acadMonth"
                                                                                          class="form-control">
                                                                                          <form:option value="">Select Academic Month</form:option>


                                                                                          <form:option value="Jan">Jan</form:option>
                                                                                          <form:option value="Feb">Feb</form:option>
                                                                                          <form:option value="Mar">Mar</form:option>
                                                                                          <form:option value="Apr">Apr</form:option>
                                                                                          <form:option value="May">May</form:option>
                                                                                          <form:option value="June">June</form:option>
                                                                                          <form:option value="July">July</form:option>
                                                                                          <form:option value="Aug">Aug</form:option>
                                                                                          <form:option value="Sept">Sept</form:option>
                                                                                          <form:option value="Oct">Oct</form:option>
                                                                                          <form:option value="Nov">Nov</form:option>

                                                                                          <form:option value="Dec">Dec</form:option>
                                                                                          <%-- <c:forEach var="course" items="${acadMonths}"
                                                                                                varStatus="status">
                                                                                                <form:option value="${course}">${course}</form:option>
                                                                                          </c:forEach> --%>
                                                                                    </form:select>
                                                                              </div>
                                                                        </div>
                                                                        <div class="col-sm-4 col-md-4 col-xs-12 column">
                                                                              <div class="form-group">
                                                                                    <form:label path="acadYear" for="acadYear">Academic Year </form:label>
                                                                                    <form:select id="acadYear" path="acadYear"
                                                                                          class="form-control" required="required">
                                                                                          <form:option value="">Select Academic Year</form:option>

                                                                                          <form:option value="2017">2017</form:option>
                                                                                          <form:option value="2018">2018</form:option>
                                                                                          <form:option value="2019">2019</form:option>
                                                                                          <form:option value="2020">2020</form:option>
                                                                                          <form:option value="2021">2021</form:option>
                                                                                          <%-- <c:forEach var="course" items="${acadYears}"
                                                                                                varStatus="status">
                                                                                                <form:option value="${course}">${course}</form:option>
                                                                                          </c:forEach> --%>
                                                                                    </form:select>
                                                                              </div>
                                                                        </div>




                                                                        <div class="col-sm-4 col-md-4 col-xs-12 column">
                                                                              <div class="form-group">
                                                                                    <form:label path="" for="">Select Assignments/Test/Learning Resource <span
                                                                                                style="color: red">*</span>
                                                                                    </form:label>
                                                                                    <form:select id="dropdwonId" path="" name="dropdwonId"
                                                                                          required="required" class="form-control">
                                                                                          <form:option value="">Select Assignments/Test/Learning Resource</form:option>

                                                                                          <c:forEach var="list" items="${list1}" varStatus="status">
                                                                                                <form:option value="${list}">${list}</form:option>
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
                                          <c:when test="${testList.size() > 0}">
                                    
                                                <div class="card bg-white border">
                                                <div class="card-body">                                           
                                                      <div class="col-xs-12 col-sm-12">
                                                            <div class="x_panel">
                                                                  <div class="x_title">
                                                                        <h5>
                                                                              Tests<font size="2px"> | ${testList.size()} Records
                                                                                    Found &nbsp; </font>
                                                                        </h5>
                                                                        
                                                                  </div>
                                                                  <div class="x_content">
                                                                        <div class="table-responsive">
                                                                              <table class="table table-hover" style="font-size: 12px">
                                                                                    <thead>
                                                                                          <tr>
                                                                                                <th>Sr. No.</th>
                                                                                                <th>Name</th>
                                                                                                <th>Course</th>
                                                                                                <th>Academic Month</th>
                                                                                                <th>Academic Year</th>
                                                                                                <th>Start Date</th>
                                                                                                <th>Due Date</th>

                                                                                                <th>End Date</th>


                                                                                                <th>Actions</th>
                                                                                          </tr>
                                                                                    </thead>
                                                                                    <tbody>

                                                                                          <c:forEach var="test" items="${testList}"
                                                                                                varStatus="status">
                                                                                                <tr>
                                                                                                      <td><c:out value="${status.count}" /></td>
                                                                                                      <td><c:out value="${test.testName}" /></td>
                                                                                                      <td><c:out value="${test.course.courseName}" /></td>
                                                                                                      <td><c:out value="${test.acadMonth}" /></td>
                                                                                                      <td><c:out value="${test.acadYear}" /></td>
                                                                                                      <td><c:out
                                                                                                                  value="${fn:replace(test.startDate,'T', ' ')}"></c:out></td>
                                                                                                      <td><c:out
                                                                                                                  value="${fn:replace(test.dueDate, 
                                'T', ' ')}"></c:out></td>
                                                                                                      <td><c:out
                                                                                                                  value="${fn:replace(test.endDate, 
                                'T', ' ')}"></c:out></td>

                                                                                                      <td><c:url value="viewThisTest" var="detailsUrl">
                                                                                                                  <c:param name="testId" value="${test.id}" />
                                                                                                            </c:url> <a href="${detailsUrl}" title="Details"><i
                                                                                                                  class="fa fa-info-circle fa-lg"></i></a>&nbsp;</td>
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
                                          <c:when test="${assignmentList.size() > 0}">
                                                <div class="card bg-white border">
                                                <div class="card-body">                                           
                                                      <div class="col-xs-12 col-sm-12">
                                                            <div class="x_panel">
                                                                  <div class="x_title">
                                                                        <h5>
                                                                              Assignments<font size="2px"> |
                                                                                    ${assignmentList.size()} Records Found &nbsp; </font>
                                                                        </h5>
                                                                        
                                                                  </div>
                                                                  <div class="x_content">
                                                                        <div class="table-responsive">
                                                                              <table class="table table-hover">
                                                                                    <thead>
                                                                                          <tr>
                                                                                                <th>Sr. No.</th>
                                                                                                <th>Name</th>
                                                                                                <th>Course</th>
                                                                                                <th>Academic Month</th>
                                                                                                <th>Academic Year</th>
                                                                                                <th>Start Date</th>
                                                                                                <th>Due Date</th>

                                                                                                <th>End Date</th>


                                                                                                <th>Actions</th>
                                                                                          </tr>
                                                                                    </thead>
                                                                                    <tbody>

                                                                                          <c:forEach var="assignment" items="${assignmentList}"
                                                                                                varStatus="status">
                                                                                                <tr>
                                                                                                      <td><c:out value="${status.count}" /></td>
                                                                                                      <td><c:out value="${assignment.assignmentName}" /></td>
                                                                                                      <td><c:out value="${assignment.course.courseName}" /></td>
                                                                                                      <td><c:out value="${assignment.acadMonth}" /></td>
                                                                                                      <td><c:out value="${assignment.acadYear}" /></td>
                                                                                                      <td><c:out
                                                                                                                  value="${fn:replace(assignment.startDate,'T', ' ')}"></c:out></td>
                                                                                                      <td><c:out
                                                                                                                  value="${fn:replace(assignment.endDate, 
                                'T', ' ')}"></c:out></td>
                                                                                                      <td><c:out
                                                                                                                  value="${fn:replace(assignment.dueDate, 
                                'T', ' ')}"></c:out></td>
                                                                                                      <sec:authorize access="hasRole('ROLE_FACULTY')">
                                                                                                            <td><c:url value="viewThisAssignment"
                                                                                                                        var="detailsUrl">
                                                                                                                        <c:param name="assignmentId"
                                                                                                                              value="${assignment.id}" />
                                                                                                                  </c:url> <a href="${detailsUrl}" title="Details"><i
                                                                                                                        class="fa fa-info-circle fa-lg"></i></a>&nbsp;</td>
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
                                          <c:when test="${allContent.size() > 0}">
                                                <div class="card bg-white border">
                                                <div class="card-body">                                           
                                                      <div class="col-xs-12 col-sm-12">
                                                            <div class="x_panel">
                                                                  <div class="x_title">
                                                                        <h5>
                                                                              Learning Resource<font size="2px"> |
                                                                                    ${allContent.size()} Records Found &nbsp; </font>
                                                                        </h5>
                                                                        
                                                                  </div>
                                                                  <div class="x_content">
                                                                        <div class="table-responsive">

                                                                              <table class="table table-striped table-hover"
                                                                                    style="font-size: 12px" id="contentTree">
                                                                                    <thead>
                                                                                          <tr>
                                                                                                <!-- <th>Sr. No.</th>
                                                                                                <th>Content Type</th> -->
                                                                                                <th>Content Name</th>
                                                                                                <th>Description</th>
                                                                                                <th>Visible From</th>
                                                                                                <th>Visible Till</th>
                                                                                                <th>View Count</th>
                                                                                                <th>Actions</th>
                                                                                          </tr>
                                                                                    </thead>
                                                                                    <tbody>

                                                                                          <c:forEach var="content" items="${allContent}"
                                                                                                varStatus="status">
                                                                                                <tr data-tt-id="${content.id}"
                                                                                                      data-tt-parent-id="${content.parentContentId}">

                                                                                                      <td><c:if
                                                                                                                  test="${content.contentType == 'Folder' }">
                                                                                                                  <i class="fa lms-folder-o fa-lg"
                                                                                                                        style="background: #E6CB47; margin-right: 5px"></i>

                                                                                                                  <c:url value="/getContentUnderAPathForFaculty"
                                                                                                                        var="navigateInsideFolder">
                                                                                                                        <c:param name="courseId" value="${content.courseId}" />
                                                                                                                        <c:param name="acadMonth"
                                                                                                                              value="${content.acadMonth}" />
                                                                                                                        <c:param name="acadYear" value="${content.acadYear}" />
                                                                                                                        <c:param name="folderPath"
                                                                                                                              value="${content.filePath}" />
                                                                                                                        <c:param name="parentContentId"
                                                                                                                              value="${content.id}" />
                                                                                                                  </c:url>
                                                                                                                  <a href="${navigateInsideFolder}"><c:out
                                                                                                                              value="${content.contentName}" /> <c:if
                                                                                                                              test="${content.accessType == 'Public' || content.accessType == 'Shared'}">
                                                                                                                              <i class="fa fa-globe fa-lg"></i>
                                                                                                                        </c:if> <c:if
                                                                                                                              test="${content.accessType != 'Public' && content.accessType != 'Shared'}">
                                                                                                                              <i class="fa fa-lock fa-lg"></i>
                                                                                                                        </c:if> </a>


                                                                                                                  <c:url value="/addContentForm" var="addFolder">
                                                                                                                        <c:param name="courseId" value="${content.courseId}" />
                                                                                                                        <c:param name="acadMonth"
                                                                                                                              value="${content.acadMonth}" />
                                                                                                                        <c:param name="acadYear" value="${content.acadYear}" />
                                                                                                                        <c:param name="folderPath"
                                                                                                                              value="${content.filePath}" />
                                                                                                                        <c:param name="contentType" value="Folder" />
                                                                                                                        <c:param name="parentContentId"
                                                                                                                              value="${content.id}" />
                                                                                                                        <c:param name="accessType"
                                                                                                                              value="${content.accessType}" />
                                                                                                                  </c:url>

                                                                                                                  <c:url value="/addContentForm" var="addFile">
                                                                                                                        <c:param name="courseId" value="${content.courseId}" />
                                                                                                                        <c:param name="acadMonth"
                                                                                                                              value="${content.acadMonth}" />
                                                                                                                        <c:param name="acadYear" value="${content.acadYear}" />
                                                                                                                        <c:param name="folderPath"
                                                                                                                              value="${content.filePath}" />
                                                                                                                        <c:param name="contentType" value="File" />
                                                                                                                        <c:param name="parentContentId"
                                                                                                                              value="${content.id}" />
                                                                                                                        <c:param name="accessType"
                                                                                                                              value="${content.accessType}" />
                                                                                                                  </c:url>

                                                                                                                  <c:url value="/addContentForm" var="addLink">
                                                                                                                        <c:param name="courseId" value="${content.courseId}" />
                                                                                                                        <c:param name="acadMonth"
                                                                                                                              value="${content.acadMonth}" />
                                                                                                                        <c:param name="acadYear" value="${content.acadYear}" />
                                                                                                                        <c:param name="folderPath"
                                                                                                                              value="${content.filePath}" />
                                                                                                                        <c:param name="contentType" value="Link" />
                                                                                                                        <c:param name="parentContentId"
                                                                                                                              value="${content.id}" />
                                                                                                                        <c:param name="accessType"
                                                                                                                              value="${content.accessType}" />
                                                                                                                  </c:url>


                                                                                                                  <!-- Arrow icon for Menu -->
                                                                                                                  <div class="btn-group">
                                                                                                                        <button type="button"
                                                                                                                              class="btn btn-xs dropdown-toggle"
                                                                                                                              data-toggle="dropdown"
                                                                                                                              style="margin-left: 5px; padding: 0px 3px; line-height: 1">
                                                                                                                              <span class="caret" style="line-height: 1"></span>
                                                                                                                        </button>
                                                                                                                        <ul class="dropdown-menu" role="menu">
                                                                                                                              <li><a href="${addFolder}">Add Subfolder</a></li>
                                                                                                                              <li><a href="${addFile}">Add File</a></li>
                                                                                                                              <li><a href="${addLink}">Add Link</a></li>
                                                                                                                        </ul>
                                                                                                                  </div>
                                                                                                            </c:if> <c:if test="${content.contentType == 'File' }">
                                                                                                                  <i class="fa ${content.fontAwesomeClass} fa-lg"
                                                                                                                        style="margin-right: 5px"></i>
                                                                                                                  <a href="downloadFile?filePath=${content.filePath}">
                                                                                                                        <c:out value="${content.contentName}" /> <i
                                                                                                                        class="fa fa-download" style="margin-left: 5px"></i>
                                                                                                                  </a>
                                                                                                            </c:if> <c:if test="${content.contentType == 'Link' }">
                                                                                                                  <i class="fa fa-link fa-lg" style="margin-right: 5px"></i>
                                                                                                                  <a href="${content.linkUrl}" target="_blank"> <c:out
                                                                                                                              value="${content.contentName}" />
                                                                                                                  </a>
                                                                                                            </c:if></td>

                                                                                                      <td><c:out value="${content.contentDescription}" /></td>
                                                                                                      <td><c:out value="${content.startDate}" /></td>
                                                                                                      <td><c:out value="${content.endDate}" /></td>
                                                                                                      <td><c:out value="${content.count}"></c:out></td>
                                                                                                      <td><c:url value="/addContentForm" var="editurl">
                                                                                                                  <c:param name="id" value="${content.id}" />
                                                                                                                  <c:param name="courseId" value="${content.courseId}" />
                                                                                                                  <c:param name="contentType"
                                                                                                                        value="${content.contentType}" />
                                                                                                            </c:url> <c:url value="deleteContent" var="deleteurl">
                                                                                                                  <c:param name="courseId" value="${content.courseId}" />
                                                                                                                  <c:param name="id" value="${content.id}" />
                                                                                                                  <c:param name="contentType"
                                                                                                                        value="${content.contentType}" />
                                                                                                            </c:url> <c:url value="viewContent" var="detailsUrl">
                                                                                                                  <c:param name="id" value="${content.id}" />
                                                                                                                  <c:param name="contentType"
                                                                                                                        value="${content.contentType}" />
                                                                                                            </c:url> <c:if test="${content.accessType eq 'Public' }">
                                                                                                                  <a href="${detailsUrl}" title="Share"><i
                                                                                                                        class="fa fa-share-alt fa-lg"></i></a>&nbsp;
                                                                                                            </c:if> <a href="${editurl}" title="Edit"><i
                                                                                                                  class="fa fa-pencil-square-o fa-lg"></i></a>&nbsp; <a
                                                                                                            href="${deleteurl}" title="Delete"
                                                                                                            <c:if test="${content.contentType eq 'Folder' }">
                                                                                                                              onclick="return confirm('Are you sure you want to delete this Folder and all of its Content ?')"
                                                                                                                        </c:if>
                                                                                                            <c:if test="${content.contentType eq 'File' }">
                                                                                                                              onclick="return confirm('Are you sure you want to delete this File ?')"
                                                                                                                        </c:if>
                                                                                                            <c:if test="${content.contentType eq 'Folder' }">
                                                                                                                              onclick="return confirm('Are you sure you want to delete this Link ?')"
                                                                                                                        </c:if>>
                                                                                                                  <i class="fa fa-trash-o fa-lg"></i>
                                                                                                      </a></td>
                                                                                                </tr>
                                                                                          </c:forEach>

                                                                                    </tbody>
                                                                              </table>
                                                                              <c:if test="${size == 0}">
                                                                                    No Content under this folder
                                                                                    </c:if>
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

