<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
    <%@page import="java.util.*"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    
    <div id="leftNav">
        <div class="modal left" id="leftnav" tabindex="-1" role="dialog" aria-labelledby="leftSidebarNavigation" aria-hidden="true">
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
                        
                        

                        <button type="button" id="sidebarClose" class="close text-dark float-right" aria-label="Close">
                            <span aria-hidden="true"><i class="fas fa-times-circle"></i></span>
                        </button>
                    </div>
                    <div class="modal-body text-left">
                        <div class="col-12">
                            <div class="row">
                                <div class="col-3 pl-2 pb-3">
                                    <div class="userLeftNav-ico rounded-circle">
                                       <img src="<c:url value="/resources/images/img-user.png" />" class="logo" alt="User Image" />
                                       
                                    </div>
                                </div>
                                <div class="col-9">
                                    <p class="p-0 m-0">ID: <span>${userBean.username}</span></p>
                                    <p class="p-0 m-0"><small>${userBean.firstname}
						${userBean.lastname}</small></p>
                                </div>
                            </div>
                        </div>
                        <ul class="list-unstyled p-0 m-0" id="accordion2">
                            <a href="${pageContext.request.contextPath}/homepage">
                                <li class="sideActive"><img src="<c:url value="/resources/images/icons/dashboard-icon.png"/>"/> Dashboard</li>
                            </a>
                            
                            	<c:if test="${loggeedInApp eq 'MBC' || loggeedInApp eq 'NMC' || loggeedInApp eq 'AAVP-AIDED' || loggeedInApp eq 'AAVP-UNAIDED'}">
						
					<c:choose>
						<c:when  test="${downloadAvailable eq 'yes'}">
						
						<span>
                            <a href="${pageContext.request.contextPath}/downloadExamResult" target="_blank">
                                <li><span class="mr-auto"><i class="fas fa-download"></i> Evaluation Report</span></li>
							</a> 
							
                         </span>
                         
						 </c:when>
						</c:choose> 
							
						</c:if>
                            
                            
                         	  <c:if test="${tcsLink eq true}">
                            <a href="https://g21.tcsion.com/LX/INDEXES/AppLaunchSAML?app_id=9517&org_id=${orgId}&serviceid=20&lsamltype=${appName}" target="_blank">
                                <li><img src="<c:url value="/resources/images/icons/desktop.png" />"/> Examinations</li>
                            </a>
                            </c:if>
                            
							<c:if test="${appNameForTee eq 'KPMSoL'}">
							<span>
                            <a href="${pageContext.request.contextPath}/viewSelectiveEvents">
                                <li class="collapsed cursor-pointer d-flex" data-toggle="collapse" data-target="#collapseTest" aria-expanded="false" aria-controls="collapseTest">
                                    <span class="mr-auto">
                                    	<svg xmlns="http://www.w3.org/2000/svg" height="20px" viewBox="-39 0 511 512" width="20px"><path d="m396.257812 504.195312h-357.621093c-16.753907 0-30.332031-13.578124-30.332031-30.332031v-435.726562c0-16.75 13.578124-30.332031 30.332031-30.332031h357.621093c16.75 0 30.332032 13.578124 30.332032 30.332031v435.726562c0 16.753907-13.582032 30.332031-30.332032 30.332031zm0 0" fill="#eff2fa"/><path d="m160.539062 109.59375c0 28.398438-23.023437 51.421875-51.425781 51.421875-28.398437 0-51.421875-23.023437-51.421875-51.421875 0-28.402344 23.023438-51.425781 51.421875-51.425781 28.402344 0 51.425781 23.023437 51.425781 51.425781zm0 0" fill="#c7cfe2"/><path d="m160.539062 256.117188c0 28.402343-23.023437 51.425781-51.425781 51.425781-28.398437 0-51.421875-23.023438-51.421875-51.425781 0-28.398438 23.023438-51.421876 51.421875-51.421876 28.402344 0 51.425781 23.023438 51.425781 51.421876zm0 0" fill="#87dbff"/><path d="m160.539062 402.644531c0 28.402344-23.023437 51.425781-51.425781 51.425781-28.398437 0-51.421875-23.023437-51.421875-51.425781 0-28.398437 23.023438-51.421875 51.421875-51.421875 28.402344 0 51.425781 23.023438 51.425781 51.421875zm0 0" fill="#c7cfe2"/><path d="m395.027344 0h-306.867188c-4.308594 0-7.800781 3.492188-7.800781 7.804688 0 4.308593 3.492187 7.804687 7.800781 7.804687h306.867188c13.101562 0 23.757812 10.65625 23.757812 23.757813v372.292968c0 4.308594 3.492188 7.804688 7.804688 7.804688 4.308594 0 7.804687-3.496094 7.804687-7.804688v-372.292968c-.003906-21.707032-17.660156-39.367188-39.367187-39.367188zm0 0"/><path d="m426.589844 436.632812c-4.3125 0-7.804688 3.492188-7.804688 7.804688v28.199219c0 13.097656-10.65625 23.757812-23.757812 23.757812h-355.164063c-13.097656 0-23.757812-10.660156-23.757812-23.757812v-433.269531c0-13.101563 10.660156-23.757813 23.757812-23.757813h14.742188c4.308593 0 7.804687-3.496094 7.804687-7.804687 0-4.3125-3.496094-7.804688-7.804687-7.804688h-14.742188c-21.703125 0-39.363281 17.660156-39.363281 39.367188v433.269531c0 21.703125 17.660156 39.363281 39.363281 39.363281h355.164063c21.707031 0 39.363281-17.660156 39.363281-39.363281v-28.199219c0-4.3125-3.492187-7.804688-7.800781-7.804688zm0 0"/><path d="m109.113281 50.363281c-32.65625 0-59.226562 26.570313-59.226562 59.226563 0 32.660156 26.570312 59.230468 59.226562 59.230468 32.660157 0 59.230469-26.570312 59.230469-59.230468 0-32.65625-26.570312-59.226563-59.230469-59.226563zm0 102.847657c-24.050781 0-43.621093-19.566407-43.621093-43.617188 0-24.054688 19.570312-43.621094 43.621093-43.621094s43.621094 19.566406 43.621094 43.621094c0 24.050781-19.570313 43.617188-43.621094 43.617188zm0 0"/><path d="m204.726562 80.527344h170.109376c4.308593 0 7.804687-3.496094 7.804687-7.804688 0-4.3125-3.496094-7.804687-7.804687-7.804687h-170.109376c-4.308593 0-7.800781 3.492187-7.800781 7.804687 0 4.308594 3.492188 7.804688 7.800781 7.804688zm0 0"/><path d="m204.726562 117.394531h123.132813c4.3125 0 7.804687-3.492187 7.804687-7.804687 0-4.308594-3.492187-7.804688-7.804687-7.804688h-123.132813c-4.308593 0-7.800781 3.496094-7.800781 7.804688 0 4.3125 3.492188 7.804687 7.800781 7.804687zm0 0"/><path d="m204.726562 154.265625h148.488282c4.308594 0 7.800781-3.496094 7.800781-7.804687 0-4.3125-3.492187-7.804688-7.800781-7.804688h-148.488282c-4.308593 0-7.800781 3.492188-7.800781 7.804688 0 4.308593 3.492188 7.804687 7.800781 7.804687zm0 0"/><path d="m204.726562 227.054688h170.109376c4.308593 0 7.804687-3.496094 7.804687-7.804688 0-4.3125-3.496094-7.804688-7.804687-7.804688h-170.109376c-4.308593 0-7.800781 3.492188-7.800781 7.804688 0 4.308594 3.492188 7.804688 7.800781 7.804688zm0 0"/><path d="m204.726562 263.921875h123.132813c4.3125 0 7.804687-3.492187 7.804687-7.804687 0-4.308594-3.492187-7.804688-7.804687-7.804688h-123.132813c-4.308593 0-7.800781 3.496094-7.800781 7.804688 0 4.3125 3.492188 7.804687 7.800781 7.804687zm0 0"/><path d="m204.726562 300.792969h148.488282c4.308594 0 7.800781-3.496094 7.800781-7.804688 0-4.308593-3.492187-7.804687-7.800781-7.804687h-148.488282c-4.308593 0-7.800781 3.496094-7.800781 7.804687 0 4.308594 3.492188 7.804688 7.800781 7.804688zm0 0"/><path d="m109.113281 343.417969c-32.65625 0-59.226562 26.570312-59.226562 59.226562 0 32.660157 26.570312 59.230469 59.226562 59.230469 32.660157 0 59.230469-26.570312 59.230469-59.230469 0-32.65625-26.570312-59.226562-59.230469-59.226562zm0 102.847656c-24.050781 0-43.621093-19.566406-43.621093-43.621094 0-24.050781 19.570312-43.617187 43.621093-43.617187s43.621094 19.566406 43.621094 43.617187c0 24.054688-19.570313 43.621094-43.621094 43.621094zm0 0"/><path d="m204.726562 373.582031h170.109376c4.308593 0 7.804687-3.496093 7.804687-7.804687s-3.496094-7.804688-7.804687-7.804688h-170.109376c-4.308593 0-7.800781 3.496094-7.800781 7.804688s3.492188 7.804687 7.800781 7.804687zm0 0"/><path d="m204.726562 410.449219h123.132813c4.3125 0 7.804687-3.492188 7.804687-7.804688 0-4.308593-3.492187-7.800781-7.804687-7.800781h-123.132813c-4.308593 0-7.800781 3.492188-7.800781 7.800781 0 4.3125 3.492188 7.804688 7.800781 7.804688zm0 0"/><path d="m204.726562 447.320312h148.488282c4.308594 0 7.800781-3.496093 7.800781-7.804687 0-4.3125-3.492187-7.804687-7.800781-7.804687h-148.488282c-4.308593 0-7.800781 3.492187-7.800781 7.804687 0 4.308594 3.492188 7.804687 7.800781 7.804687zm0 0"/><path d="m98.363281 244.082031c-3.046875-3.046875-7.992187-3.046875-11.039062 0s-3.046875 7.988281 0 11.035157l19.25 19.25c1.527343 1.523437 3.523437 2.285156 5.519531 2.285156s3.996094-.761719 5.519531-2.285156l40.265625-40.265626c.039063-.039062.074219-.078124.113282-.113281l26.734374-26.734375c3.046876-3.046875 3.046876-7.988281 0-11.035156-3.046874-3.046875-7.992187-3.046875-11.039062 0l-20.414062 20.417969c-11.144532-12.503907-27.070313-19.746094-44.160157-19.746094-32.65625 0-59.226562 26.570313-59.226562 59.230469 0 32.65625 26.570312 59.226562 59.226562 59.226562 32.660157 0 59.230469-26.570312 59.230469-59.226562 0-4.3125-3.496094-7.804688-7.804688-7.804688-4.3125 0-7.804687 3.492188-7.804687 7.804688 0 24.050781-19.570313 43.617187-43.621094 43.617187s-43.621093-19.566406-43.621093-43.617187c0-24.054688 19.570312-43.621094 43.621093-43.621094 12.882813 0 24.855469 5.597656 33.085938 15.207031l-30.105469 30.109375zm0 0"/></svg>
                                    	Specialization/Electives
                                    </span>
                                </li>
							</a>
                            </span>
                            </c:if>
                            <span>
                            <a href="${pageContext.request.contextPath}/viewTestFinal">
                                <li class="collapsed cursor-pointer d-flex" data-toggle="collapse" data-target="#collapseTest" aria-expanded="false" aria-controls="collapseTest">
                                    <span class="mr-auto"><img src="<c:url value="/resources/images/icons/test-icon.png" />"/> Test/Quiz</span>
							</a>
                            </span>
                            
                            <span>
                            <a href="${pageContext.request.contextPath}/viewAssignmentFinal">
                                <li class="collapsed cursor-pointer d-flex" data-toggle="collapse" data-target="#collapseTask" aria-expanded="false" aria-controls="collapseTask">
                                    <span class="mr-auto"><img src="<c:url value="/resources/images/icons/assignment-icon.png" />"/> Assignments/Tasks</span>
                                </li>
                            </a>
                            </span>
                            <span>
                            <a href="${pageContext.request.contextPath}/myCourseForm">
                                <li class="collapsed cursor-pointer d-flex" data-toggle="collapse" data-target="#collapseCourse" aria-expanded="false" aria-controls="collapseCourse">
                                    <span class="mr-auto"><img src="<c:url value="/resources/images/icons/courses-icon.png" />"/> My Courses</span>
                                </li>
                            </a>
                            </span>
                            <a href="${pageContext.request.contextPath}/viewUserAnnouncementsSearchNew">
                                <li><img src="<c:url value="/resources/images/icons/announcement-icon.png" />"/> Announcements</li>
                            </a>
                            
                            <a href="${pageContext.request.contextPath}/showInternalMarks">
                                <li class=""> <i class="fas fa-marker"></i> ICA</li>
                            </a>
                            
                            <%-- <span>
                            <a href="#">
                                <li class="collapsed cursor-pointer d-flex" data-toggle="collapse" data-target="#collapseGroup" aria-expanded="false" aria-controls="collapseGroup">
                                    <span class="mr-auto"><img src="<c:url value="/resources/images/icons/group-icon.png" />"/> Group</span>
                                </li>
                            </a>
                            </span> --%>

                            <a href="${pageContext.request.contextPath}/viewDailyAttendanceByStudent">
                                <li><img src="<c:url value="/resources/images/icons/attendance-icon.png" />"/> Attendance</li>
                            </a>
                            <a href="${pageContext.request.contextPath}/reportFormForStudents">
                                <li><img src="<c:url value="/resources/images/icons/report-icon.png" />"/> My Report</li>
                            </a>
                            <a href="${pageContext.request.contextPath}/gradeCenterFormForStudent">
                                <li><img src="<c:url value="/resources/images/icons/gradecenter-icon.png" />"/> Grade Center</li>
                            </a>
                            <a href="${pageContext.request.contextPath}/contentFormForStudents">
                                <li><img src="<c:url value="/resources/images/icons/learning-resources-icon.png" />"/> Learning Resources</li>
                            </a>
							<a href="${pageContext.request.contextPath}/viewLibraryAnnouncements">
                                <li><img src="<c:url value="/resources/images/icons/library-icon.png" />"/> Library</li>
                            </a>


							<a href="http://ezproxy.svkm.ac.in:2048/login">
                                <li><i class="fas fa-globe fa-lg"></i> e-Library</li>
                            </a>
                            
                            <a href="${pageContext.request.contextPath}/knowMyFaculty?courseId=">
                                <li><img src="<c:url value="/resources/images/icons/teacher-icon.png" />"/> Teachers&#39; Profile</li>
                            </a>
							
							<span>
                                <li class="collapsed cursor-pointer d-flex" data-toggle="collapse" data-target="#collapseMessage" aria-expanded="false" aria-controls="collapseSettings">
                                    <span class="mr-auto"><img src="<c:url value="/resources/images/icons/msg-icon.png" />"/> Message</span>
                                    <i class="fas fa-caret-right ml-3 pt-1"></i>
                                </li>

                                <ul id="collapseMessage" class="collapse list-unstyled" data-parent="#accordion2" aria-labelledby="message">
                                    <a href="${pageContext.request.contextPath}/viewMyMessage">
                                        <li class="pl-5">View Message</li>
                                    </a>
                                    
									<a href="${pageContext.request.contextPath}/createMessageForm">
	                                       <li class="pl-5">Create Message</li>
	                                   </a>
										
                                </ul>
                            </span>
							
							<span>
                                <li class="collapsed cursor-pointer d-flex" data-toggle="collapse" data-target="#collapseSupport" aria-expanded="false" aria-controls="collapseSettings">
                                    <span class="mr-auto"> <i class="far fa-life-ring fa-lg"></i> Support</span>
                                    <i class="fas fa-caret-right ml-3 pt-1"></i>
                                </li>

                                <ul id="collapseSupport" class="collapse list-unstyled" data-parent="#accordion2" aria-labelledby="support">
                                    <a href="${pageContext.request.contextPath}/overview?courseId=">
                                        <li class="pl-5">Support</li>
                                    </a>
                                    
									<a href="${pageContext.request.contextPath}/createQueryForm">
	                                       <li class="pl-5">Raise A Ticket</li>
	                                   </a>
									   <a href="${pageContext.request.contextPath}/viewQueryResponse">
	                                       <li class="pl-5">Track Your Ticket Status</li>
	                                   </a>
									   <a href="${pageContext.request.contextPath}/viewFAQ">
	                                       <li class="pl-5">FAQ</li>
	                                   </a>
										
                                </ul>
                            </span>
							
							
							
                            <span>
                                <li class="collapsed cursor-pointer d-flex" data-toggle="collapse" data-target="#collapseSetting" aria-expanded="false" aria-controls="collapseSettings">
                                    <span class="mr-auto"><img src="<c:url value="/resources/images/icons/profile-icon.png" />"/> Setting</span>
                                    <span class="badge badge-pill bg-secondary pt-1 text-white">&#33;</span>
                                    <i class="fas fa-caret-right ml-3 pt-1"></i>
                                </li>
	
                                <ul id="collapseSetting" class="collapse list-unstyled" data-parent="#accordion2" aria-labelledby="settings">
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
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </div>