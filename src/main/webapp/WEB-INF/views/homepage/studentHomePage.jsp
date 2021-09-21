<jsp:include page="../common/header.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<meta name="viewport" content="width=device-width, initial-scale=.5, maximum-scale=12.0, minimum-scale=.25, user-scalable=yes"/>
<section class="container-fluid" style="background-color:#DCDCDC;">
      <!-- Example row of columns -->
      <div class="row">
            <div class="demo-wrapper"  style="margin-top: 4%;">
                  <div class="dashboard clearfix" >
                  <h2>${course.courseName }</h2>
                  <%-- <ul class="tiles">
                              <div class="clearfix col-sm-12 padding-small">
                                  
                                    <li class="tile tile-1 tile-1-slider">
                                          <h2>ANNOUNCEMENTS</h2>
                                          <div id="carousel-example-generic" class="carousel slide"
                                                data-ride="carousel">
                                                <a href="viewUserAnnouncements" >
                                                <!-- Wrapper for slides -->
                                                <div class="carousel-inner" role="listbox">
                                                      <c:forEach var="announcement" items="${announcements}" varStatus="status">
                                                    
                                                     <div class="item <c:if test="${status.count == 1 }">active</c:if>">
                                            <div class="carousel-caption">                              
                                              <p>${announcement.announcementType}</p>
                                              <h3>${announcement.subject}</h3>
                                            </div>
                                          </div>
                                  
                                    
                                              </c:forEach>
                                                </div>
                                                <!-- Controls -->
                                                <a class="left carousel-control"
                                                      href="#carousel-example-generic" role="button"
                                                      data-slide="prev"> <span class="fa fa-angle-left"
                                                      aria-hidden="true"></span> <span class="sr-only">Previous</span>
                                                </a> <a class="right carousel-control"
                                                      href="#carousel-example-generic" role="button"
                                                      data-slide="next"> <span class="fa fa-angle-right"
                                                      aria-hidden="true"></span> <span class="sr-only">Next</span>
                                                </a>
                                                </a>
                                          </div>
                                    </li>
                                  
                              </div>
                        </ul> --%>
                        <ul class="tiles">
                        <c:forEach items="${courses }" var="course" varStatus="status">
                              <div class="clearfix col-sm-4 padding-small" >
                            
                             
                                    <li class="tile theme-4  " style="float:left;background-color:#63bef5;color:#a88282;width:70%;">
                                  
                              <!--        <div>
                                                <p>
                                                      <i class="fa fa-file-text-o"></i> COURSES
                                                </p>
                                          </div> -->
                                          <div class="assignmentsList" >
                                                <c:if test="${not empty courses}">
                                                      <table class="table">
                                                          
                                                                  <tr>
                                                                  <%--       <td><p>${status.count}</p></td> --%>
                                                                        <td><p style="font-size:20px">
                                                                                    <a href="<c:url value="/viewCourse?id=${course.id}" />" >${course.courseName}</a>
                                                                              </p></td>
                                                                            
                                                                  </tr>
                                                          
                                                      </table>
                                                </c:if>
                                          </div>
                                         
                                    </li>
                                  
                              </div>
                              </c:forEach>
                   </ul>
                 
                              <%-- <div class="clearfix col-sm-4 padding-small">
                                    <li class="tile theme-9 slideTextRight doubleHeight">
                                                <div class="assignmentsList">
                                                      <c:if test="${not empty tests}">
                                                            <table class="table">
                                                                  <c:forEach items="${tests}" var="test" varStatus="status">
                                                                        <c:url value="startStudentTest" var="testUrl">
                                                                              <c:param name="id">${test.id}</c:param>
                                                                        </c:url>
                                                                        <tr>
                                                                              <td><p><a href="${testUrl }">${status.count}</a></p></td>
                                                                              <td><p>
                                                                                    <a href="${testUrl }">${test.testName}</a>
                                                                              </p></td>
                                                                              <td><p>
                                                                                    <a href="#"><c:out value="${fn:substring(test.dueDate,
                                0,10)}" ></c:out></a>
                                                                              </p></td>
                                                                        </tr>
                                                                  </c:forEach>
                                                            </table>
                                                      </c:if>
                                                      <p class="viewAllBtn"><a class="btn btn-default btn-sm" role="button" href="testList">VIEW ALL</a></p>
                                                </div>
                                                <div>
                                                      <p>TESTS</p>
                                                </div>
                                    </li>
                              </div> --%>
                  <%--        <div class="clearfix col-sm-4 padding-small">
                                    <li class="tile theme-1 slideTextUp doubleHeight">
                                                <div>
                                                      <p>
                                                            <i class="fa fa-file-text-o"></i> ASSIGNMENTS
                                                      </p>
                                                </div>
                                                <div class="assignmentsList">
                                                      <c:if test="${not empty assignments}">
                                                            <table class="table">
                                                                  <c:forEach items="${assignments }" var="assignment"
                                                                        varStatus="status">
                                                                      
                                                                        <c:url value="submitAssignmentForm" var="submitAssignmentUrl">
                                                                              <c:param name="id">${assignment.id}</c:param>
                                                                        </c:url>
                                                                      
                                                                        <tr>
                                                                              <td><p>${status.count}</p></td>
                                                                              <td><p><a href="${submitAssignmentUrl}">${assignment.assignmentName}</a></p></td>
                                                                        </tr>
                                                                  </c:forEach>
                                                            </table>
                                                      </c:if>
                                                      <p class="viewAllBtn"><a class="btn btn-default btn-sm" role="button" href="assignmentList">VIEW ALL</a></p>
                                                </div>
                                    </li>
                              </div> --%>
                        <%--
                        <ul class="tiles">
                              <div class="clearfix col-sm-4 padding-small">
                                    <a href="studentContentList">
                                    <li class="tile theme-6 rotate3d rotate3dY doubleHeight">
                          <div class="faces">
                            <div class="front"><span class="icon-instagram"><i class="fa-custom fa-pencil-square-o"></i></span></div>
                            <div class="back"><p>CONTENT</p></div>
                          </div>
                        </li>
                        </a>
                              </div>
                              <div class="clearfix col-sm-4 padding-small">
                                    <a href="viewLibrary">
                                    <li class="tile theme-11 rotate3d rotate3dX doubleHeight">
                                                <div class="faces">
                                    <div class="front"><span><i class="fa-custom fa-university"></i></span></div>
                                    <div class="back"><p>DIGITAL LIBRARY</p></div>
                                  </div>
                                    </li>
                                    </a>
                              </div>
                              <div class="clearfix col-sm-4 padding-small">
                                    <li class="tile theme-12 rotate3d rotate3dX doubleHeight">
                                                <div class="faces">
                                    <div class="front"><span><i class="fa-custom fa-envelope"></i>Feedbacks</span></div>
                                    <div class="back">
                                          <div class="assignmentsList">
                                                                  <c:if test="${not empty feedbacks}">
                                                                        <table class="table">
                                                                              <c:forEach items="${feedbacks }" var="feedback" varStatus="status">
                                                                                    <tr>
                                                                                          <td><p>${status.count}</p></td>
                                                                                          <td><p>
                                                                                                      <a href="<c:url value="/startStudentFeedback?id=${feedback.id}" />">${feedback.feedbackName}</a>
                                                                                                </p></td>
                                                                                    </tr>
                                                                              </c:forEach>
                                                                        </table>
                                                                  </c:if>
                                                            </div>
                                    </div>
                                  </div>
                                    </li>
                              </div>
                        </ul> --%>
                  </div>
                  <!--end dashboard-->
                                          <div class="dashboard clearfix" >
                               <span id="ctl00_rightContainer_lblPanel2" class="panel-head" style="margin-left:2%;"><lang key="G2">To Do </lang> </span>
                                <span id="ctl00_rightContainer_lblPanel2" class="panel-head" style="margin-left:48%;"><lang key="G2">Announcements </lang> </span>
        <div class="green-box-area">
            <div class="col-sm-6"><marquee id="ctl00_rightContainer_panel2" align="justify" direction="up" onmouseout="this.start()" height="85px" onmouseover="this.stop()" scrollamount="1" scrolldelay="60"><p align="justify"><a target="_blank" href="http://fileserver.mkcl.org/PostHSCDiploma2017/OasisModules_Files/Files/133.pdf?did=1619"><lang key="D1619">circular revised eligibility as per Court Order Dt.10/08/2016</lang> <img src="./marquee_files/new.gif" border="none"></a></p></marquee></div>
       
        <div  class="col-sm-6"><marquee id="ctl00_rightContainer_panel2" align="justify" direction="up" onmouseout="this.start()" height="85px" onmouseover="this.stop()" scrollamount="1" scrolldelay="60"><p align="justify"><a target="_blank" href="http://fileserver.mkcl.org/PostHSCDiploma2017/OasisModules_Files/Files/133.pdf?did=1619"><lang key="D1619">circular revised eligibility as per Court Order Dt.10/08/2016</lang> <img src="./marquee_files/new.gif" border="none"></a></p></marquee></div>
       
        </div>
        </div>
            </div>
      </div>
      <c:if test="${not empty feedback}">
            <div class="modal" id="feedbackModal" tabindex="-1" role="dialog" data-keyboard="false" data-backdrop="static">
            <div class="modal-dialog">
                  <c:forEach items="${feedback.feedbackQuestions}" var="feedbackQuestion"
                        varStatus="status">
                        <form:form action="addStudentFeedbackResponse" method="post"
                              modelAttribute="feedback" id="studentFeedbackForm-${status.index}">
                              <div class="modal-content" id="modal-${status.index}">
                                    <div class="modal-header">
                                          <h4>
                                                Feedback for ${feedback.studentFeedback.courseName} by <cite>${feedback.studentFeedback.firstname} ${feedback.studentFeedback.lastname}</cite>
                                          </h4>
                                          <h3>
                                                <span class="label label-warning" id="qid">${status.index + 1}</span>
                                                ${feedbackQuestion.description}
                                                <br /> <small><c:choose>
                                                            <c:when test="${feedbackQuestion.type eq 'SINGLESELECT'}"> Select One
                                                            </c:when>
                                                            <c:otherwise>Select Multiple</c:otherwise>
                                                      </c:choose></small>
                                          </h3>
                                    </div>
                                    <div class="modal-body">
                                          <div class="quiz" data-toggle="buttons">
                                                <form:input type="hidden"
                                                      path="feedbackQuestions[${status.index}].studentFeedbackResponse.studentFeedbackId"
                                                      value="${feedback.studentFeedback.id}" />
                                                <form:input type="hidden"
                                                      path="feedbackQuestions[${status.index}].studentFeedbackResponse.feedbackQuestionId"
                                                      value="${feedbackQuestion.id}" />
                                                <c:if test="${not empty feedbackQuestion.option1}">
                                                      <label
                                                            class="element-animation element1 btn btn-lg btn-primary btn-block"><span
                                                            class="btn-label"><i
                                                                  class="glyphicon glyphicon-chevron-right"></i></span> <c:choose>
                                                                  <c:when test="${feedbackQuestion.type eq 'SINGLESELECT'}">
                                                                        <form:radiobutton
                                                                              path="feedbackQuestions[${status.index}].answer" value="1" />
                                                                  </c:when>
                                                                  <c:otherwise>
                                                                        <form:checkbox path="feedbackQuestions[${status.index}].answers"
                                                                              value="1" />
                                                                  </c:otherwise>
                                                            </c:choose>${feedbackQuestion.option1}</label>
                                                </c:if>
                                                <c:if test="${not empty feedbackQuestion.option2}">
                                                      <label
                                                            class="element-animation element2 btn btn-lg btn-primary btn-block"><span
                                                            class="btn-label"><i
                                                                  class="glyphicon glyphicon-chevron-right"></i></span> <c:choose>
                                                                  <c:when test="${feedbackQuestion.type eq 'SINGLESELECT'}">
                                                                        <form:radiobutton
                                                                              path="feedbackQuestions[${status.index}].answer" value="2" />
                                                                  </c:when>
                                                                  <c:otherwise>
                                                                        <form:checkbox path="feedbackQuestions[${status.index}].answers"
                                                                              value="2" />
                                                                  </c:otherwise>
                                                            </c:choose>${feedbackQuestion.option2}</label>
                                                </c:if>
                                                <c:if test="${not empty feedbackQuestion.option3}">
                                                      <label
                                                            class="element-animation element3 btn btn-lg btn-primary btn-block"><span
                                                            class="btn-label"><i
                                                                  class="glyphicon glyphicon-chevron-right"></i></span> <c:choose>
                                                                  <c:when test="${feedbackQuestion.type eq 'SINGLESELECT'}">
                                                                        <form:radiobutton
                                                                              path="feedbackQuestions[${status.index}].answer" value="3" />
                                                                  </c:when>
                                                                  <c:otherwise>
                                                                        <form:checkbox path="feedbackQuestions[${status.index}].answers"
                                                                              value="3" />
                                                                  </c:otherwise>
                                                            </c:choose>${feedbackQuestion.option3}</label>
                                                </c:if>
                                                <c:if test="${not empty feedbackQuestion.option4}">
                                                      <label
                                                            class="element-animation element4 btn btn-lg btn-primary btn-block"><span
                                                            class="btn-label"><i
                                                                  class="glyphicon glyphicon-chevron-right"></i></span> <c:choose>
                                                                  <c:when test="${feedbackQuestion.type eq 'SINGLESELECT'}">
                                                                        <form:radiobutton
                                                                              path="feedbackQuestions[${status.index}].answer" value="4" />
                                                                  </c:when>
                                                                  <c:otherwise>
                                                                        <form:checkbox path="feedbackQuestions[${status.index}].answers"
                                                                              value="4" />
                                                                  </c:otherwise>
                                                            </c:choose>${feedbackQuestion.option4}</label>
                                                </c:if>
                                                <c:if test="${not empty feedbackQuestion.option5}">
                                                      <label
                                                            class="element-animation element5 btn btn-lg btn-primary btn-block"><span
                                                            class="btn-label"><i
                                                                  class="glyphicon glyphicon-chevron-right"></i></span> <c:choose>
                                                                  <c:when test="${feedbackQuestion.type eq 'SINGLESELECT'}">
                                                                        <form:radiobutton
                                                                              path="feedbackQuestions[${status.index}].answer" value="5" />
                                                                  </c:when>
                                                                  <c:otherwise>
                                                                        <form:checkbox path="feedbackQuestions[${status.index}].answers"
                                                                              value="5" />
                                                                  </c:otherwise>
                                                            </c:choose>${feedbackQuestion.option5}</label>
                                                </c:if>
                                                <c:if test="${not empty feedbackQuestion.option6}">
                                                      <label
                                                            class="element-animation element6 btn btn-lg btn-primary btn-block"><span
                                                            class="btn-label"><i
                                                                  class="glyphicon glyphicon-chevron-right"></i></span> <c:choose>
                                                                  <c:when test="${feedbackQuestion.type eq 'SINGLESELECT'}">
                                                                        <form:radiobutton
                                                                              path="feedbackQuestions[${status.index}].answer" value="6" />
                                                                  </c:when>
                                                                  <c:otherwise>
                                                                        <form:checkbox path="feedbackQuestions[${status.index}].answers"
                                                                              value="6" />
                                                                  </c:otherwise>
                                                            </c:choose>${feedbackQuestion.option6}</label>
                                                </c:if>
                                                <c:if test="${not empty feedbackQuestion.option7}">
                                                      <label
                                                            class="element-animation element7 btn btn-lg btn-primary btn-block"><span
                                                            class="btn-label"><i
                                                                  class="glyphicon glyphicon-chevron-right"></i></span> <c:choose>
                                                                  <c:when test="${feedbackQuestion.type eq 'SINGLESELECT'}">
                                                                        <form:radiobutton
                                                                              path="feedbackQuestions[${status.index}].answer" value="7" />
                                                                  </c:when>
                                                                  <c:otherwise>
                                                                        <form:checkbox path="feedbackQuestions[${status.index}].answers"
                                                                              value="7" />
                                                                  </c:otherwise>
                                                            </c:choose>${feedbackQuestion.option7}</label>
                                                </c:if>
                                                <c:if test="${not empty feedbackQuestion.option8}">
                                                      <label
                                                            class="element-animation element8 btn btn-lg btn-primary btn-block"><span
                                                            class="btn-label"><i
                                                                  class="glyphicon glyphicon-chevron-right"></i></span> <c:choose>
                                                                  <c:when test="${feedbackQuestion.type eq 'SINGLESELECT'}">
                                                                        <form:radiobutton
                                                                              path="feedbackQuestions[${status.index}].answer" value="8" />
                                                                  </c:when>
                                                                  <c:otherwise>
                                                                        <form:checkbox path="feedbackQuestions[${status.index}].answers"
                                                                              value="8" />
                                                                  </c:otherwise>
                                                            </c:choose>${feedbackQuestion.option8}</label>
                                                </c:if>
                                          </div>
                                    </div>
                                   <div class="modal-footer">
                                          <c:if test="${not status.first}">
                                                <a class="btn btn-large btn-primary"
                                                      onclick="showModal('modal-${status.index }','modal-${status.index - 1}','nav-${status.index - 1}')">Previous</a>
                                          </c:if>
                                          <c:choose>
                                                <c:when test="${status.last}">
                                                            <a class="btn btn-large btn-primary"
                                                                  onclick="submitForm('studentFeedbackForm-${status.index}','nav-${status.index}',true)">Save
                                                                  and Complete</a>
                                                </c:when>
                                                <c:otherwise>
                                                            <a class="btn btn-large btn-primary"
                                                                  onclick="submitForm('studentFeedbackForm-${status.index}','nav-${status.index}');showModal('modal-${status.index }','modal-${status.index + 1}','nav-${status.index + 1}')">Save
                                                                  and Next</a>
                                                      <a class="btn btn-large btn-primary"
                                                            onclick="showModal('modal-${status.index }','modal-${status.index + 1}','nav-${status.index + 1}')">Skip</a>
                                                </c:otherwise>
                                          </c:choose>
                                    </div>
                              </div>
                        </form:form>
                  </c:forEach>
            </div>
            </div>
      </c:if>
</section>
<jsp:include page="../common/footer.jsp" />