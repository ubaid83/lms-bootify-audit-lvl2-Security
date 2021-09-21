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
<%
      boolean isEdit = "true".equals((String) request
                  .getAttribute("edit"));
%>

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
                                          <li class="breadcrumb-item"><a
                                                href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                                          <li class="breadcrumb-item" aria-current="page"><c:out
                                                      value="${Program_Name}" /></li>
                                          <sec:authorize access="hasRole('ROLE_STUDENT')">
                                                <c:out value="${AcadSession}" />
                                          </sec:authorize>
                                          <li class="breadcrumb-item active" aria-current="page">Add
                                                Question Paper Files</li>
                                    </ol>
                              </nav>

                              <jsp:include page="../common/alert.jsp" />


                              <!-- Input Form Panel -->
                              <div class="card bg-white border">
                                    <div class="card-body">
                                          <div class="col-xs-12 col-sm-12">
                                                <div class="x_panel">

                                                      <div class="x_title">
                                                            <h5>Add Question Paper Files</h5>

                                                      </div>

                                                      <div class="x_content">
                                                            <form:form action="addQFolderFile" method="post"
                                                                  modelAttribute="content" enctype="multipart/form-data">
                                                                  <fieldset>
                                                                        <%-- <form:input path="courseId" type="hidden" /> --%>
                                                                        <form:hidden path="contentType" />
                                                                        <form:hidden path="folderPath" />
                                                                        <form:hidden path="parentContentId" />
                                                                        <%
                                                                              if (isEdit) {
                                                                        %>
                                                                        <form:input type="hidden" path="id" />
                                                                        <form:input type="hidden" path="filePath" />
                                                                        <%
                                                                              }
                                                                        %>


                                                                        <div class="row">
                                                                              <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                                    <div class="form-group">
                                                                                          <form:label path="acadMonth" for="acadMonth">Academic Month <span
                                                                                                      style="color: red">*</span>
                                                                                          </form:label>
                                                                                          <form:select id="acadMonth" path="acadMonth"
                                                                                                class="form-control" required="required">
                                                                                                <form:option value="">Select Academic Month</form:option>
                                                                                                <form:options items="${acadMonths}" />
                                                                                          </form:select>
                                                                                    </div>
                                                                              </div>
                                                                              <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                                    <div class="form-group">
                                                                                          <form:label path="acadYear" for="acadYear">Academic Year <span
                                                                                                      style="color: red">*</span>
                                                                                          </form:label>
                                                                                          <form:select id="acadYear" path="acadYear"
                                                                                                class="form-control" required="required">
                                                                                                <form:option value="">Select Academic Year</form:option>
                                                                                                <form:options items="${acadYears}" />
                                                                                          </form:select>
                                                                                    </div>
                                                                              </div>
                                                                              <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                                    <div class="form-group">
                                                                                          <form:label path="courseId" for="courseId">Course</form:label>
                                                                                          <form:select id="courseId" path="courseId"
                                                                                                class="form-control">
                                                                                                <form:option value="">Select Course</form:option>
                                                                                                <c:forEach var="course" items="${allCourses}"
                                                                                                      varStatus="status">
                                                                                                      <form:option value="${course.id}">${course.courseName}</form:option>
                                                                                                </c:forEach>
                                                                                          </form:select>
                                                                                    </div>
                                                                              </div>
                                                                        </div>
                                                                        <c:forEach begin='1' end='5' varStatus="loop">
                                                                              <div class="row">
                                                                                    <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                                          <div class="form-group">
                                                                                                <form:label path="contentFileName" for="contentFileName">File Name ${loop.index}</form:label>
                                                                                                <form:input path="contentFileName" type="text"
                                                                                                      name="contentFileName[]" class="form-control" />

                                                                                          </div>
                                                                                    </div>
                                                                                    <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                                          <div class="form-group" id="check">


                                                                                                <label for="file"> <%
     if (isEdit) {
%> Select if you wish to override earlier file <%
     } else {
%> Question Paper File ${loop.index}<%
     }
%>
                                                                                                </label> <input id="filePaths${loop.index}"
                                                                                                      name="filePaths${loop.index}" type="file"
                                                                                                      class="form-control" />



                                                                                          </div>
                                                                                          <div id="fileSize${loop.index}"></div>
                                                                                    </div>



                                                                              </div>
                                                                        </c:forEach>


                                                                        <div class="row">

                                                                              <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                                    <div class="form-group">
                                                                                          <form:label path="contentDescription"
                                                                                                for="contentDescription">File Description</form:label>
                                                                                          <form:textarea path="contentDescription"
                                                                                                class="form-control" />

                                                                                    </div>
                                                                              </div>
                                                                        </div>
                                                                        <div class="row">
                                                                        
                                                                        
                                                                  <%
                                                                        if(isEdit) {
                                                                  %>

                                                                  <%-- <div
                                                                        class="col-lg-8 col-md-8 col-sm-12 mt-3 position-relative">
                                                                        <label>Select Date Range <span class="text-danger">*</span></label>
                                                                        <div class="input-group">
                                                                              <label class="f-13 text-danger req">*</label> <label
                                                                                    class="sr-only">Select Date Range</label>
                                                                              <form:input type="hidden" id="testStartDate"
                                                                                    path="startDate" />
                                                                              <form:input type="hidden" id="testEndDate" path="endDate" />
                                                                              <input id="startDate" type="text"
                                                                                    placeholder="Start Date - End Date" class="form-control"
                                                                                    required="required"
                                                                                    value="${content.startDate} - ${content.endDate}"
                                                                                    readonly />
                                                                              <div class="input-group-append">
                                                                                    <button class="btn btn-outline-secondary" type="button"
                                                                                          id="testDateRangeBtn">
                                                                                          <i class="fas fa-calendar"></i>
                                                                                    </button>
                                                                              </div>
                                                                        </div>
                                                                  </div> --%>
                                                                  <div class="col-md-4 col-sm-12 mt-3">
														<div class="form-group">
															<form:label path="startDate" for="startDate">Display From <span style="color: red">*</span></form:label>


															<div class='input-group date' id='datetimepicker1'>
																<form:input id="startDate" path="startDate" value="${content.startDate}" type="text"
																	placeholder="Start Date" class="form-control"
																	required="required" readonly="true"/>
																<span class="input-group-addon"><span
																	class="glyphicon glyphicon-calendar"></span> </span>
															</div>

														</div>
													</div>
													<div class="col-md-4 col-sm-12 mt-3">
														<div class="form-group">
															<form:label path="endDate" for="endDate">Display Until <span style="color: red">*</span></form:label>

															<div class='input-group date' id='datetimepicker2'>
																<form:input id="endDate" path="endDate" value="${content.endDate}" type="text"
																	placeholder="End Date" class="form-control"
																	required="required" readonly="true"/>
																<span class="input-group-addon"><span
																	class="glyphicon glyphicon-calendar"></span> </span>
															</div>

														</div>
													</div>

                                                                  <%
                                                                        }else{
                                                                  %>
                                                                  <%-- <div
                                                                        class="col-lg-8 col-md-8 col-sm-12 mt-3 position-relative">
                                                                        <label>Select Date Range <span class="text-danger">*</span></label>
                                                                        <div class="input-group">
                                                                              <label class="f-13 text-danger req">*</label> <label
                                                                                    class="sr-only">Select Date Range</label>
                                                                              <form:input type="hidden" id="testStartDate"
                                                                                    path="startDate" />
                                                                              <form:input type="hidden" id="testEndDate" path="endDate" />
                                                                              <input id="startDate" type="text"
                                                                                    placeholder="Start Date - End Date" class="form-control"
                                                                                    required="required" />
                                                                              <div class="input-group-append">
                                                                                    <button class="btn btn-outline-secondary" type="button"
                                                                                          id="testDateRangeBtn">
                                                                                          <i class="fas fa-calendar"></i>
                                                                                    </button>
                                                                              </div>
                                                                        </div>
                                                                  </div> --%>
                                                                  
                                                                  <div class="col-md-4 col-sm-12 mt-3">
														<div class="form-group">
															<form:label path="startDate" for="startDate">Display From <span style="color: red">*</span></form:label>


															<div class='input-group date' id='datetimepicker1'>
																<form:input id="startDate" path="startDate" type="text"
																	placeholder="Start Date" class="form-control"
																	required="required" readonly="true"/>
																<span class="input-group-addon"><span
																	class="glyphicon glyphicon-calendar"></span> </span>
															</div>

														</div>
													</div>
													<div class="col-md-4 col-sm-12 mt-3">
														<div class="form-group">
															<form:label path="endDate" for="endDate">Display Until <span style="color: red">*</span></form:label>

															<div class='input-group date' id='datetimepicker2'>
																<form:input id="endDate" path="endDate" type="text"
																	placeholder="End Date" class="form-control"
																	required="required" readonly="true"/>
																<span class="input-group-addon"><span
																	class="glyphicon glyphicon-calendar"></span> </span>
															</div>

														</div>
													</div>
                                                                  <%
                                                                        }
                                                                  %>

                                                                              <%-- <div
                                                                                    class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                                                                    <div class="input-group">
                                                                                          <label class="f-13 text-danger req">*</label> <label
                                                                                                class="sr-only">Select Date Range</label>
                                                                                          <form:input type="hidden" id="testStartDate"
                                                                                                path="startDate" />
                                                                                          <form:input type="hidden" id="testEndDate" path="endDate" />
                                                                                          <input id="startDate" type="text"
                                                                                                placeholder="Start Date-End Date" class="form-control"
                                                                                                required="required" />
                                                                                          <div class="input-group-append">
                                                                                                <button class="btn btn-outline-secondary" type="button"
                                                                                                      id="testDateRangeBtn">
                                                                                                      <i class="fas fa-calendar"></i>
                                                                                                </button>
                                                                                          </div>
                                                                                    </div>
                                                                              </div> --%>
                                                                              <%-- <div class="col-sm-4 column">
                                                                                    <div class="form-group">
                                                                                          <form:label path="startDate" for="startDate">Display From <span style="color: red">*</span></form:label>


                                                                                          <div class='input-group date' id='datetimepicker1'>
                                                                                                <form:input id="startDate" path="startDate" type="text"
                                                                                                      placeholder="Start Date" class="form-control"
                                                                                                      required="required" readonly="true"/>
                                                                                                <span class="input-group-addon"><span
                                                                                                      class="glyphicon glyphicon-calendar"></span> </span>
                                                                                          </div>

                                                                                    </div>
                                                                              </div>
                                                                              <div class="col-sm-4 column">
                                                                                    <div class="form-group">
                                                                                          <form:label path="endDate" for="endDate">Display Until <span style="color: red">*</span></form:label>

                                                                                          <div class='input-group date' id='datetimepicker2'>
                                                                                                <form:input id="endDate" path="endDate" type="text"
                                                                                                      placeholder="End Date" class="form-control"
                                                                                                      required="required" readonly="true"/>
                                                                                                <span class="input-group-addon"><span
                                                                                                      class="glyphicon glyphicon-calendar"></span> </span>
                                                                                          </div>

                                                                                    </div>
                                                                              </div> --%>
                                                                              <div class="col-md-4 col-sm-12 mt-3 column">
                                                                                    <div class="form-group">
                                                                                          <form:label path="accessType" for="accessType">Access Type <span
                                                                                                      style="color: red">*</span>
                                                                                          </form:label>
                                                                                          <c:if test="${ not empty content.accessType }">
                                                                                                <form:hidden path="accessType" />
                                                                                          : ${content.accessType}
                                                                                    </c:if>
                                                                                          <c:if test="${ empty content.accessType }">
                                                                                                <form:select id="accessType" path="accessType"
                                                                                                      disabled="true" class="form-control"
                                                                                                      required="required">
                                                                                                      <%-- <form:option value="">Select Access Type</form:option> --%>
                                                                                                      <form:option value="Public">Public</form:option>
                                                                                                      <%-- <form:option value="Private">Private</form:option> --%>
                                                                                                </form:select>
                                                                                          </c:if>
                                                                                    </div>
                                                                              </div>
                                                                        </div>



                                                                        <div class="form-row">
                                                                              <table
                                                                                    class="table table-bordered table-striped mt-5 font-weight-bold">
                                                                                    <tbody>

                                                                                          <tr>
                                                                                                <td>Send Email Alert for New Content?</td>
                                                                                                <td>
                                                                                                
                                                                                                <%if(isEdit) { %>
                                                                                                      <div class="pretty p-switch p-fill p-toggle">
                                                                                                            <form:checkbox value="${content.sendEmailAlert}" id="sendEmailAlert"
                                                                                                                  path="sendEmailAlert" />
                                                                                                            <div class="state p-success p-on">
                                                                                                                  <label>Yes</label>
                                                                                                            </div>
                                                                                                            <div class="state p-danger p-off">
                                                                                                                  <label>No</label>
                                                                                                            </div>

                                                                                                      </div>
                                                                                                      <% } else { %>
                                                                                                      <div class="pretty p-switch p-fill p-toggle">
                                                                                                            <form:checkbox value="N" id="sendEmailAlert"
                                                                                                                  path="sendEmailAlert" />
                                                                                                            <div class="state p-success p-on">
                                                                                                                  <label>Yes</label>
                                                                                                            </div>
                                                                                                            <div class="state p-danger p-off">
                                                                                                                  <label>No</label>
                                                                                                            </div>

                                                                                                      </div>
                                                                                                      <% } %>
                                                                                                </td>
                                                                                          </tr>
                                                                                          <tr>
                                                                                                <td>Send SMS Alert for New Content?</td>
                                                                                                <td>
                                                                                                <% if(isEdit){ %>
                                                                                                      <div class="pretty p-switch p-fill p-toggle">
                                                                                                            <form:checkbox value="${content.sendSmsAlert}" id="sendSmsAlert"
                                                                                                                  path="sendSmsAlert" />
                                                                                                            <div class="state p-success p-on">
                                                                                                                  <label>Yes</label>
                                                                                                            </div>
                                                                                                            <div class="state p-danger p-off">
                                                                                                                  <label>No</label>
                                                                                                            </div>

                                                                                                      </div>
                                                                                                      <% } else { %>
                                                                                                      <div class="pretty p-switch p-fill p-toggle">
                                                                                                            <form:checkbox value="N" id="sendSmsAlert"
                                                                                                                  path="sendSmsAlert" />
                                                                                                            <div class="state p-success p-on">
                                                                                                                  <label>Yes</label>
                                                                                                            </div>
                                                                                                            <div class="state p-danger p-off">
                                                                                                                  <label>No</label>
                                                                                                            </div>

                                                                                                      </div>
                                                                                                      <% } %>
                                                                                                </td>
                                                                                          </tr>



                                                                                    </tbody>
                                                                              </table>
                                                                        </div>
                                                                        <%-- <div class="row">
                                                                              <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                                    <div class="slider round">
                                                                                          <form:label path="sendEmailAlert" for="sendEmailAlert">Send Email Alert for New Assignment?</form:label>
                                                                                          <br>
                                                                                          <form:checkbox path="sendEmailAlert" class="form-control"
                                                                                                value="Y" data-toggle="toggle" data-on="Yes"
                                                                                                data-off="No" data-onstyle="success"
                                                                                                data-offstyle="danger" data-style="ios" data-size="mini" />
                                                                                    </div>
                                                                              </div>
                                                                              <div class="col-md-4 col-sm-6 col-xs-12 column">
                                                                                    <div class="slider round">
                                                                                          <form:label path="sendSmsAlert" for="sendSmsAlert">Send SMS Alert for New Assignment?</form:label>
                                                                                          <br>
                                                                                          <form:checkbox path="sendSmsAlert" class="form-control"
                                                                                                value="Y" data-toggle="toggle" data-on="Yes"
                                                                                                data-off="No" data-onstyle="success"
                                                                                                data-offstyle="danger" data-style="ios" data-size="mini" />
                                                                                    </div>
                                                                              </div>

                                                                        </div> --%>







                                                                        <div class="row">

                                                                              <div class="col-sm-8 column">
                                                                                    <div class="form-group">

                                                                                          <%
                                                                                                if (isEdit) {
                                                                                          %>
                                                                                          <button id="submit" class="btn btn-large btn-primary"
                                                                                                formaction="updateFile">Update File</button>
                                                                                          <%
                                                                                                } else {
                                                                                          %>
                                                                                          <button id="submit" class="btn btn-large btn-primary"
                                                                                                formaction="addQFolderFile">Upload Files</button>
                                                                                          <%
                                                                                                }
                                                                                          %>
                                                                                          <button id="cancel" class="btn btn-danger"
                                                                                                formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
                                                                                    </div>
                                                                              </div>
                                                                        </div>
                                                                  </fieldset>
                                                            </form:form>
                                                      </div>
                                                </div>
                                          </div>
                                    </div>
                              </div>
                     
                   
                    </div>
			
			<!-- SIDEBAR START -->

	        <!-- SIDEBAR END -->
	<jsp:include page="../common/newAdminFooter.jsp"/>














                              
    
                        <script>
                              var times = 5;
                              for (var i = 1; i <= times; i++) {

                                    //var str = "filePaths";
                                    var id = "filePaths".concat(i);

                                    //alert("ID 1 "+id);

                                    $('#' + id).bind(
                                                'change',
                                                function() {
                                                      alert('This file size is: '
                                                                  + this.files[0].size / 1024 / 1024
                                                                  + "MB");
                                                      var fileSize = this.files[0].size / 1024
                                                                  / 1024 + "MB";
                                                      var id1 = $(this).attr("id");
                                                      //alert(id1);
                                                      var j = id1.charAt(9);
                                                      //alert(j);
                                                      var id2 = "fileSize".concat(j);
                                                      //alert("Id 2 "+id2);
                                                      $('#' + id2)
                                                                  .html("File Size:" + (fileSize));
                                                });
                              }
                        </script>
                        <script type="text/javascript"
                              src="<c:url value="/resources/js/customDateTimePicker.js" />"></script>
                        <script>
                              //$(document).ready(function() {
                              $("#datetimepicker1").on("dp.change", function(e) {

                                    validDateTimepicks();
                              }).datetimepicker({
                                    // minDate:new Date(),
                                    useCurrent : false,
                                    format : 'YYYY-MM-DD HH:mm:ss'
                              });

                              $("#datetimepicker2").on("dp.change", function(e) {

                                    validDateTimepicks();
                              }).datetimepicker({
                                    // minDate:new Date(),
                                    useCurrent : false,
                                    format : 'YYYY-MM-DD HH:mm:ss'
                              });

                              function validDateTimepicks() {
                                    if (($('#startDate').val() != '' && $('#startDate')
                                                .val() != null)
                                                && ($('#endDate').val() != '' && $('#endDate')
                                                            .val() != null)) {
                                          var fromDate = $('#startDate').val();
                                          var toDate = $('#endDate').val();
                                          var eDate = new Date(fromDate);
                                          var sDate = new Date(toDate);
                                          if (sDate < eDate) {
                                                alert("endate cannot be smaller than startDate");
                                                $('#startDate').val("");
                                                $('#endDate').val("");
                                          }
                                    }
                              }
                              //});
                        </script>
