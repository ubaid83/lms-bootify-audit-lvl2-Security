<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
      uri="http://www.springframework.org/security/tags"%>
<html lang="en">
<script
      src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<jsp:include page="../common/css.jsp" />



<body class="nav-md footer_fixed">


      <!-- <div class="loader"></div> -->

      <div class="container body">
            <div class="main_container">

                  <%-- <jsp:include page="../common/leftSidebar.jsp">
                        <jsp:param name="courseId" value="${courseId}" />
                  </jsp:include> --%>
                  <jsp:include page="../common/leftSidebar.jsp" />
                  <jsp:include page="../common/topHeader.jsp" />



                  <!-- page content: START -->
                  <div class="right_col" role="main">

                        <div class="dashboard_container">

                              <div class="dashboard_container_spacing">

                                    <div class="breadcrumb">
                                          <c:out value="${Program_Name}" />

                                          <sec:authorize access="hasRole('ROLE_STUDENT')">

                                                <i class="fa fa-angle-right"></i>

                                                <c:out value="${AcadSession}" />

                                          </sec:authorize>

                                          <br> <br> <a
                                                href="${pageContext.request.contextPath}/homepage">Dashboard</a>
                                          <i class="fa fa-angle-right"></i> Download Report

                                    </div>
                                    <jsp:include page="../common/alert.jsp" />

                                    <!-- Input Form Panel -->


                                    <!-- Results Panel -->

                                    <div class="row">
                                          <div class="col-xs-12 col-sm-12">
                                                <div class="x_panel">
                                                      <div class="x_title">
                                                            <h2>Download Reports</h2>

                                                            <ul class="nav navbar-right panel_toolbox">
                                                                  <li><a href="#"><span>View All</span></a></li>
                                                                  <li><a class="collapse-link"><i
                                                                              class="fa fa-chevron-up"></i></a></li>
                                                                  <li><a class="close-link"><i class="fa fa-close"></i></a></li>
                                                            </ul>
                                                            <div class="clearfix"></div>
                                                      </div>
                                                      <div class="x_content">
                                                            <div class="table-responsive">
                                                                  <table id="downloadReport" class="table  table-hover"
                                                                        style="font-size: 12px">

                                                                        <thead>
                                                                              <tr>

                                                                                    <th class="text-center border-grey">Report Name</th>
                                                                                    <th class="text-center  border-grey">Action</th>

                                                                              </tr>

                                                                        </thead>
                                                                        <tbody>

                                                                              <c:forEach items="${downloadReportLinkList}" var="report">
                                                                                    <tr>


                                                                                          <td class="text-center border-grey">
                                                                                                <div class="text-nowrap">${report.reportName}</div>
                                                                                          </td>
                                                                                          <c:url value="${report.reportLink}" var="downloadReport">

                                                                                          </c:url>
                                                                                          <td class="text-center border-grey"><a
                                                                                                href="${downloadReport}" title="Downlaod">Download</a></td>


                                                                                    </tr>
                                                                              </c:forEach>


                                                                        </tbody>
                                                                  </table>
                                                            </div>
                                                      </div>



                                                </div>

                                                <div class="x_panel">
                                                      <div class="panel-group" id="testQuestionAccordion"
                                                            role="tablist" aria-multiselectable="true">

                                                            <div class="panel panel-default">
                                                                  <div class="panel-heading" role="tab" id="heading-1">
                                                                        <h4 class="panel-title">
                                                                              <a data-toggle="collapse"
                                                                                    data-parent="#testQuestionAccordion" href="#collapse-1"
                                                                                    aria-expanded="false" aria-controls="collapse-1"><strong>Feedback
                                                                                          Report Range Wise</strong></a>
                                                                        </h4>
                                                                  </div>
                                                                  <div id="collapse-1" class="panel-collapse collapse"
                                                                        role="tabpanel" aria-labelledby="heading-1">
                                                                        <div class="panel-body">
                                                                              <div class="row">
                                                                                    <form action="downloadFeedbackReportRangeWise"
                                                                                          method="get">

                                                                                          <div class="col-sm-6">
                                                                                                <div class="form-group">
                                                                                                      <label path="" for="courseQuestionCount">No of
                                                                                                            Course Questions <span style="color: red">*</span></label> <input id="courseQuestCount"
                                                                                                            path="courseQuestionCount" type="number"
                                                                                                            required="required" min="1"
                                                                                                            placeholder="Course Question Count"
                                                                                                            class="form-control" value="" />
                                                                                                </div>
                                                                                          </div>
                                                                                          <div class="col-sm-6">
                                                                                                <div class="form-group">

                                                                                                      <label path="" for="groupId">Select Term1 Acad
                                                                                                            Session <span style="color: red">*</span></label> <select multiple="multiple"
                                                                                                            id="term1AcadSession" path="" required="required"
                                                                                                            class="form-control" style="overflow: auto;">

                                                                                                            <%-- <form:options items="${acadSessionList}" /> --%>
                                                                                                            <c:forEach var="listValue" items="${acadSessionList}">
                                                                                                                  <option value="${listValue}">${listValue}</option>
                                                                                                            </c:forEach>

                                                                                                      </select>
                                                                                                </div>
                                                                                          </div>

                                                                                          <div class="col-sm-6">
                                                                                                <div class="form-group">

                                                                                                      <label path="" for="groupId">Select Term2 Acad
                                                                                                            Session <span style="color: red">*</span></label> <select multiple="multiple"
                                                                                                            id="term2AcadSession" path="" required="required"
                                                                                                            class="form-control" style="overflow: auto;">
                                                                                                            <%-- <form:option value="Semester II">Semester II</form:option>
                                                                                                                                          <form:option value="Semester IV">Semester IV</form:option> --%>
                                                                                                            <c:forEach var="listValue" items="${acadSessionList}">
                                                                                                                  <option value="${listValue}">${listValue}</option>
                                                                                                            </c:forEach>

                                                                                                            <%-- <options items="${acadSessionList}" /> --%>


                                                                                                      </select>
                                                                                                </div>
                                                                                          </div>

                                                                                          <a class="btn btn-large btn-primary"
                                                                                                onclick="submitForm()">Generate Report</a>

                                                                                          <!-- <button id="submit"
                                                                                                                              class="btn btn-large btn-primary"
                                                                                                                              formaction="downloadFeedbackReportRangeWise">Generate Report</button> -->

                                                                                    </form>
                                                                              </div>
                                                                        </div>
                                                                  </div>


                                                            </div>
                                                      </div>


                                                
                                                
                                                
                                                
                                                
                                                
                                                
                                                
                                                
                                                
                                                
                                                
                                                
                                                
                                                
                                                      <div class="panel-group" id=""
                                                            role="tablist" aria-multiselectable="true">

                                                            <div class="panel panel-default">
                                                                  <div class="panel-heading" role="tab" id="heading-2">
                                                                        <h4 class="panel-title">
                                                                              <a data-toggle="collapse"
                                                                                    data-parent="#testQuestionAccordion" href="#collapse-2"
                                                                                    aria-expanded="false" aria-controls="collapse-2"><strong>Top 5 Faculty Feedbacks Average</strong></a>
                                                                        </h4>
                                                                  </div>
                                                                  <div id="collapse-2" class="panel-collapse collapse"
                                                                        role="tabpanel" aria-labelledby="heading-2">
                                                                        <div class="panel-body">
                                                                              <div class="row">
                                                                                    <form action="downloadTop5FacultyFeedback"
                                                                                          method="get">

                                                                                          
                                                                                          <div class="col-sm-6">
                                                                                                <div class="form-group">

                                                                                                      <label path="" for="groupId">Select Term1 Acad
                                                                                                            Session <span style="color: red">*</span></label> <select multiple="multiple"
                                                                                                            id="acadSessionTopFive" path="" required="required"
                                                                                                            class="form-control" style="overflow: auto;">

                                                                                                            <%-- <form:options items="${acadSessionList}" /> --%>
                                                                                                            <c:forEach var="listValue" items="${acadSessionList}">
                                                                                                                  <option value="${listValue}">${listValue}</option>
                                                                                                            </c:forEach>

                                                                                                      </select>
                                                                                                </div>
                                                                                          </div>

                                                                                          

                                                                                          <a class="btn btn-large btn-primary"
                                                                                                onclick="submitFormtop5Feedback()"> Generate Report</a>

                                                                                          <!-- <button id="submit"
                                                                                                                              class="btn btn-large btn-primary"
                                                                                                                              formaction="downloadFeedbackReportRangeWise">Generate Report</button> -->

                                                                                    </form>
                                                                              </div>
                                                                        </div>
                                                                  </div>


                                                            </div>
                                                      </div>
                                                

                                                </div>

                                                <jsp:include page="../common/paginate.jsp">
                                                      <jsp:param name="baseUrl" value="searchList" />
                                                </jsp:include>
                                          </div>



                                    </div>

                              </div>

                              <!-- /page content: END -->


                              <jsp:include page="../common/footer.jsp" />

                        </div>
                  </div>
</body>

<script>
      function submitForm() {

            var term1 = $('#term1AcadSession').val();//document.getElementById("term2AcadSession");
            var term2 = $('#term2AcadSession').val();
            var courseQuestCount = $('#courseQuestCount').val();

            if (term1 != null && term2 != null && courseQuestCount != null
                        && courseQuestCount > 0) {

                  window.location.href = '${pageContext.request.contextPath}/downloadFeedbackReportRangeWise?'
                              + 'term1AcadSession='
                              + term1
                              + '&term2AcadSession='
                              + term2 + '&courseQuestnCount=' + courseQuestCount;
            } else {
                  alert("please fill all the fields");
            }

      }
      
      function submitFormtop5Feedback(){
            var acadSession = $('#acadSessionTopFive').val();
            
            if (acadSession != null ) {
                  
                  window.location.href = '${pageContext.request.contextPath}/downloadTop5FacultyFeedback?'
                        + 'acadSession='
                        + acadSession;
                  
            }else {
                  alert("please fill all the fields");
            }
            
            
      }
</script>

</html>
