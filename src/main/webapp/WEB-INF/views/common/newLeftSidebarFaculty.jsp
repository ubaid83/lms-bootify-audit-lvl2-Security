<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
      pageEncoding="ISO-8859-1"%>


<%@page import="java.util.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
      uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<div id="leftNav">
      <div class="modal left" id="leftnav" tabindex="-1" role="dialog"
            aria-labelledby="leftSidebarNavigation" aria-hidden="true">
            <div class="modal-dialog" role="document">
                  <div class="modal-content scrollcust">
                        <div class="modal-header d-block border-bottom">
                              <h5 class="modal-title float-left" id="leftSidebarNavigation">
                              
                                  <c:if test="${instiFlag eq 'nm'}">
                        <img src="<c:url value="/resources/images/logo.png"/>" style="width: 130px;" />
                        </c:if>
                        <c:if test="${instiFlag eq 'sv'}">
                        <img src="<c:url value="/resources/images/svkmlogo.png"/>" style="width: 130px;" />
                        </c:if>
                              </h5>

                              <button type="button" id="sidebarClose"
                                    class="close text-dark float-right" aria-label="Close">
                                    <span aria-hidden="true"><i class="fas fa-times-circle"></i></span>
                              </button>
                        </div>
                        <div class="modal-body text-left">
                              <div class="col-12">
                                    <div class="row">
                                          <div class="col-3 pl-2 pb-3">
                                                <div class="userLeftNav-ico rounded-circle">
                                                      <img src="<c:url value="/resources/images/img-user.png" />"
                                                            alt="Name of the user" title="Name of the user" />
                                                </div>
                                          </div>
                                          <div class="col-9">
                                                <p class="p-0 m-0">
                                                      ID: <span>${userBean.username}</span>
                                                </p>
                                                <p class="p-0 m-0">
                                                      <small>${userBean.firstname} ${userBean.lastname}</small>
                                                </p>
                                          </div>
                                    </div>
                              </div>
                              <ul class="list-unstyled p-0 m-0" id="accordion2">
                                    <a href="${pageContext.request.contextPath}/homepage">
                                          <li class="sideActive"><img
                                                src="<c:url value="/resources/images/icons/dashboard-icon.png"/> " />
                                                Dashboard</li>
                                    </a>
                                    <span>
                                          <li class="collapsed cursor-pointer d-flex"
                                          data-toggle="collapse" data-target="#collapseTest"
                                          aria-expanded="false" aria-controls="collapseTest"><span
                                                class="mr-auto"><img
                                                      src="<c:url value="/resources/images/icons/test-icon.png" />" />
                                                      Test / Quiz</span> <i class="fas fa-caret-right ml-3 pt-1"></i></li>

                                          <ul id="collapseTest" class="collapse list-unstyled"
                                                data-parent="#accordion2" aria-labelledby="test">
                                                <c:if test="${courseId ne null ||  not empty courseId}">
                                                <a
                                                      href="${pageContext.request.contextPath}/createTestForm?courseId=${courseId}">
                                                      <li class="pl-5">Create Test</li>
                                                </a>

                                                


                                                      <a
                                                            href="${pageContext.request.contextPath}/addTestPoolForm?courseId=${courseId}">
                                                            <li class="pl-5">Create Test Pool</li>
                                                      </a>

                                                      <a
                                                            href="${pageContext.request.contextPath}/viewTestPools?courseId=${courseId}">
                                                            <li class="pl-5">Test Pools For Course</li>
                                                      </a>

                                                      <a
                                                            href="${pageContext.request.contextPath}/uploadTestQuestionForm?courseId=${courseId}&type=Subjective">
                                                            <li class="pl-5">Subjective From Question Bank</li>
                                                      </a>
                                                      <a
                                                            href="${pageContext.request.contextPath}/uploadTestQuestionForm?courseId=${courseId}&type=Objective">
                                                            <li class="pl-5">Objective From Question Bank</li>
                                                      </a>


                                                </c:if>

                                                <a
                                                      href="${pageContext.request.contextPath}/testList?courseId=${courseId}">
                                                      <li class="pl-5">View Test</li>
                                                </a>



                                                <a
                                                      href="${pageContext.request.contextPath}/searchTest?courseId=${courseId}">
                                                      <li class="pl-5">Search Test</li>
                                                </a>


                                          </ul>
                                    </span>

                                    <!-- Assignment Start-->
                                    <sec:authorize access="hasRole('ROLE_FACULTY')">
                                          <span>
                                                <li class="collapsed cursor-pointer d-flex"
                                                data-toggle="collapse" data-target="#collapseAssignment"
                                                aria-expanded="false" aria-controls="collapseAssignment"><span
                                                      class="mr-auto"> <img
                                                            src="<c:url value="/resources/images/icons/assignment-icon.png" />" />
                                                            Assignment
                                                </span> <i class="fas fa-caret-right ml-3 pt-1"></i></li>

                                                <ul id="collapseAssignment" class="collapse list-unstyled"
                                                      data-parent="#accordion2" aria-labelledby="test">

                                                      <c:if
                                                            test="${courseRecord.id ne '' or not empty courseRecord.id}">

                                                            <c:if test="${ not empty courseId || courseId ne null}">
                                                                  <a
                                                                        href="${pageContext.request.contextPath}/createAssignmentFromGroup?courseId=${courseId}">
                                                                        <li class="pl-5">Create For Group</li>
                                                                  </a>
                                                            
                                                            <a
                                                                  href="${pageContext.request.contextPath}/createAssignmentFromMenu?courseId=${courseId}"><li
                                                                  class="pl-5">Create For Student</li></a>
                                                                  </c:if>
                                                                  
                                                            <a
                                                                  href="${pageContext.request.contextPath}/searchFacultyAssignment?courseId=${courseId}"><li
                                                                  class="pl-5">View</li></a>
                                                                  <c:if test="${ not empty courseId || courseId ne null}">
                                                            <a
                                                                  href="${pageContext.request.contextPath}/searchAssignmentToEvaluate?courseId=${courseId}"><li
                                                                  class="pl-5">Evaluate with Advance Search</li></a>
                                                            
                                                                  <a
                                                                        href="${pageContext.request.contextPath}/createGroupAssignmentsForm?courseId=${courseId}">
                                                                        <li class="pl-5">Create Multiple Assignments for Groups</li>
                                                                  </a>
                                                            
                                                            <a
                                                                  href="${pageContext.request.contextPath}/evaluateByStudentForm?courseId=${courseId}"><li
                                                                  class="pl-5">Evaluate Student</li></a>
                                                            <a
                                                                  href="${pageContext.request.contextPath}/evaluateByStudentGroupForm?courseId=${courseId}"><li
                                                                  class="pl-5">Evaluate Group</li></a>
                                                            <a
                                                                  href="${pageContext.request.contextPath}/lateSubmissionApprovalForm?courseId=${courseId}"><li
                                                                  class="pl-5">Late Submitted Assignments</li></a>
                                                                  </c:if>

                                                      </c:if>
                                                      <c:if
                                                            test="${courseRecord.id eq '' and  empty courseRecord.id}">




                                                            <a
                                                                  href="${pageContext.request.contextPath}/searchFacultyAssignment">
                                                                  <li class="pl-5">View</li>
                                                            </a>
                                                            <a
                                                                  href="${pageContext.request.contextPath}/searchAssignmentToEvaluate">
                                                                  <li class="pl-5">Evaluate</li>
                                                            </a>
                                                            <a
                                                                  href="${pageContext.request.contextPath}/evaluateByStudentForm">
                                                                  <li class="pl-5">Evaluate For Student</li>
                                                            </a>

                                                      </c:if>


                                                </ul>
                                          </span>
                                          
                                          <span>
								<li class="collapsed cursor-pointer d-flex"
								data-toggle="collapse"
								data-target="#collapseAssignmentForModule" aria-expanded="false"
								aria-controls="collapseAssignmentForModule"><span class="mr-auto">
										<img
										src="<c:url value="/resources/images/icons/assignment-icon.png" />" />
										Assignment For Module
								</span> <i class="fas fa-caret-right ml-3 pt-1"></i></li>

								<ul id="collapseAssignmentForModule" class="collapse list-unstyled"
									data-parent="#accordion2" aria-labelledby="test">

									
											<a
												href="${pageContext.request.contextPath}/createAssignmentModuleForm"><li
												class="pl-5">Create For Module</li></a>
									

										<a
											href="${pageContext.request.contextPath}/searchFacultyAssignmentForModule"><li
											class="pl-5">View For Module</li></a>
										<a
												href="${pageContext.request.contextPath}/evaluateByStudentFormForModule"><li
												class="pl-5">Evaluate Student</li></a>	
											
										


								</ul>
							</span>

                                          <!-- Groups Start -->
                                          <c:if test="${ not empty courseId || courseId ne null}">
                                          <span>
                                                <li class="collapsed cursor-pointer d-flex"
                                                data-toggle="collapse" data-target="#collapseGroup"
                                                aria-expanded="false" aria-controls="collapseGroup"><span
                                                      class="mr-auto"> <img
                                                            src="<c:url value="/resources/images/icons/group-icon.png" />" />
                                                            Group
                                                </span> <i class="fas fa-caret-right ml-3 pt-1"></i></li>

                                                <ul id="collapseGroup" class="collapse list-unstyled"
                                                      data-parent="#accordion2" aria-labelledby="test">




                                                      <a
                                                            href="${pageContext.request.contextPath}/createGroupForm?courseId=${courseId}"><li
                                                            class="pl-5">Create Group</li></a>
                                                      <a
                                                            href="${pageContext.request.contextPath}/searchFacultyGroups?courseId=${courseId}"><li
                                                            class="pl-5">View Group</li></a>
                                                      <a
                                                            href="${pageContext.request.contextPath}/removeStudentsFromGroupForm?courseId=${courseId}"><li
                                                            class="pl-5">Remove Students From Group</li></a>
                                                </ul>
                                          </span>
                                          </c:if>

                                          <!-- Groups End -->
                                          <!-- Class Paricipation Start -->

<%--                                      <a
                                                href="${pageContext.request.contextPath}/classParticipation?courseId=${courseId}">
                                                <li><img
                                                      src="<c:url value="/resources/images/icons/class-icon.png" />" />
                                                      Class Participation</li>
                                          </a> --%>

                                          <!-- ClassParticipation End -->


                                          <!-- Announcement Start -->

                                          <span>
                                                <li class="collapsed cursor-pointer d-flex"
                                                data-toggle="collapse" data-target="#collapseAnnouncement"
                                                aria-expanded="false" aria-controls="collapseAnnouncement"><span
                                                      class="mr-auto"> <img
                                                            src="<c:url value="/resources/images/icons/announcement-icon.png" />" />
                                                            Announcement
                                                </span> <i class="fas fa-caret-right ml-3 pt-1"></i></li>

                                                <ul id="collapseAnnouncement" class="collapse list-unstyled"
                                                      data-parent="#accordion2" aria-labelledby="test">




                                                      <a
                                                            href="${pageContext.request.contextPath}/viewUserAnnouncementsSearchNew"><li
                                                            class="pl-5">View Announcements</li></a>
                                                      <c:if test="${not empty courseId}">
                                                            <a
                                                                  href="${pageContext.request.contextPath}/addAnnouncementForm?courseId=${courseId}"><li
                                                                  class="pl-5">Add Announcements</li></a>
                                                            <%-- <a
                                                                  href="${pageContext.request.contextPath}/viewUserAnnouncements"><li
                                                                  class="pl-5">View Announcements</li></a> --%>
                                                      </c:if>






                                                </ul>
                                          </span>

                                          <!-- Announcement End -->

                                          <!-- Discussion Start -->
                                          <span>
                                                <li class="collapsed cursor-pointer d-flex"
                                                data-toggle="collapse" data-target="#collapseDiscussion"
                                                aria-expanded="false" aria-controls="collapseDiscussion"><span
                                                      class="mr-auto"> <img
                                                            src="<c:url value="/resources/images/icons/forum-icon.png" />" />
                                                            Forum
                                                </span> <i class="fas fa-caret-right ml-3 pt-1"></i></li>

                                                <ul id="collapseDiscussion" class="collapse list-unstyled"
                                                      data-parent="#accordion2" aria-labelledby="test">




                                                      <a
                                                            href="${pageContext.request.contextPath}/createForumForm?courseId=${courseId}"><li
                                                            class="pl-5">Create Forum</li></a>
                                                      <a
                                                            href="${pageContext.request.contextPath}/viewForum?courseId=${courseId}"><li
                                                            class="pl-5">View Forum</li></a>







                                                </ul>
                                          </span>
                                          <!-- Discussion End -->

                                          <!-- My Course Students Start -->
                                          
                                          <c:if test="${ not empty courseId || courseId ne null}">

                                          <a
                                                href="${pageContext.request.contextPath}/showMyCourseStudents?courseId=${courseId}">
                                                <li><img
                                                      src="<c:url value="/resources/images/icons/courses-icon.png" />" />
                                                      My Course Students</li>
                                          </a>
                                          </c:if>

                                          <!-- My Cours Students End -->


                                          <!-- Grade Center Start -->

                                          <a
                                                href="${pageContext.request.contextPath}/gradeCenter?courseId=${courseId}">
                                                <li><img
                                                      src="<c:url value="/resources/images/icons/gradecenter-icon.png" />" />
                                                      Grade Center</li>
                                          </a>
                                          <c:if test="${courseId ne null ||  not empty courseId}">
                                          <a
                                                href="${pageContext.request.contextPath}/classParticipation?courseId=${courseId}">
                                                <li><img
                                                      src="<c:url value="/resources/images/icons/class-icon.png" />" />
                                                      Class Participation</li>
                                          </a>
                                          </c:if>

                                          <!-- Grade Center End -->
                                          <!-- Mark Attendance -->
                                          <a
                                                href="${pageContext.request.contextPath}/markAttendanceForm">
                                                <li><img
                                                      src="<c:url value="/resources/images/icons/attendance-icon.png" />" />
                                                      Mark Attendance</li>
                                          </a>


                                          <!-- Report Start -->
                                          <span>
                                                <li class="collapsed cursor-pointer d-flex"
                                                data-toggle="collapse" data-target="#collapseReport"
                                                aria-expanded="false" aria-controls="collapseReport"><span
                                                      class="mr-auto"> <img
                                                            src="<c:url value="/resources/images/icons/report-icon.png" />" />
                                                            Report
                                                </span> <i class="fas fa-caret-right ml-3 pt-1"></i></li>

                                                <ul id="collapseReport" class="collapse list-unstyled"
                                                      data-parent="#accordion2" aria-labelledby="test">

                                                      <a
                                                            href="${pageContext.request.contextPath}/createReportsForm?courseId=${courseId}"><li
                                                            class="pl-5">View Report</li></a>
                                                      <a href="${pageContext.request.contextPath}/downloadReportMyCourseStudentForm"><li
                                                            class="pl-5">Download Feedback Report</li></a>
                                                </ul>
                                          </span>


                                          <!-- Report End -->
                                          
                               <span>
								<li class="collapsed cursor-pointer d-flex"
								data-toggle="collapse" data-target="#collapseLearningResource"
								aria-expanded="false" aria-controls="collapseLearningResource"><span
									class="mr-auto"> <img
										src="<c:url value="/resources/images/icons/learning-resources-icon.png" />" />
										Learning Resource
								</span> <i class="fas fa-caret-right ml-3 pt-1"></i></li>

							<ul id="collapseLearningResource" class="collapse list-unstyled"
									data-parent="#accordion2" aria-labelledby="test">

									<%-- <a
										href="${pageContext.request.contextPath}/getContentUnderAPathForFaculty?courseId=${courseId}"><li
										class="pl-5">View Content</li></a> --%>
										
									 <a
										href="${pageContext.request.contextPath}/getContentUnderAPathForFacultyForModule"><li
										class="pl-5">View Content</li></a>
										
									<a href="http://ezproxy.svkm.ac.in:2048/login"><li
										class="pl-5">Library Link</li></a> 
								</ul>
							</span>
                                          <!-- Learning Resource End -->
                                          
                                          <!-- Search -->
                                          <span>
                                                <li class="collapsed cursor-pointer d-flex"
                                                data-toggle="collapse" data-target="#collapseSearch"
                                                aria-expanded="false" aria-controls="collapseSearch"><span
                                                      class="mr-auto">
                                                      <i class="fas fa-search fa-lg"></i> Search</span>
                                                      <i class="fas fa-caret-right ml-3 pt-1"></i></li>

                                                <ul id="collapseSearch" class="collapse list-unstyled"
                                                      data-parent="#accordion2" aria-labelledby="Search">
                                                      <a href="${pageContext.request.contextPath}/searchAssignmentTestForm">
                                                            <li class="pl-5">Search All Assignments</li>
                                                      </a>
                                                      
													<a href="${pageContext.request.contextPath}/addSearchForm">
				                                        <li class="pl-5">Generic Search</li>
				                                    </a>
															
                                                      
                                                </ul>
                                          </span>
                                          
                                          
                                                                               
                                          <!-- Change Program -->
                                          <a href="https://dev-portal.svkm.ac.in/usermgmt">
                                           <li>
                                           <i class="fas fa-exchange-alt fa-lg"></i> Change Program
                                           </li>
                                          </a>

 


                                          <!-- Profile Start -->
                                          <span>
                                                <li class="collapsed cursor-pointer d-flex"
                                                data-toggle="collapse" data-target="#collapseSetting"
                                                aria-expanded="false" aria-controls="collapseSettings"><span
                                                      class="mr-auto"><img
                                                            src="<c:url value="/resources/images/icons/profile-icon.png" />" />
                                                            Setting</span> <span
                                                      class="badge badge-pill bg-secondary pt-1 text-white">&#33;</span>
                                                      <i class="fas fa-caret-right ml-3 pt-1"></i></li>

                                                <ul id="collapseSetting" class="collapse list-unstyled"
                                                      data-parent="#accordion2" aria-labelledby="settings">
                                                      <a href="${pageContext.request.contextPath}/profileDetails">
                                                            <li class="pl-5">My Profile</li>
                                                      </a>
                                                      
                                                      <a href="${pageContext.request.contextPath}/changePasswordForm">
                                                            <li class="pl-5">Change Password</li>
                                                      </a>
                                                      
													<a href="${pageContext.request.contextPath}/loggedout">
				                                        <li class="pl-5">Logout</li>
				                                    </a>
															
                                                      
                                                </ul>
                                          </span>

                                          <!-- Profile End -->

                                    </sec:authorize>


                              </ul>
                        </div>
                  </div>
            </div>
      </div>
</div>
