<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" />
<div class="d-flex" id="announcementPage">
<sec:authorize access="hasAnyRole('ROLE_STUDENT','ROLE_PARENT')">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftSidebar.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_PARENT')">
		<%-- <jsp:include page="../common/newLeftSidebarParent.jsp" /> --%>
	</sec:authorize>
	
	<sec:authorize access="hasRole('ROLE_FACULTY')">
	<jsp:include page="../common/newLeftNavBarFaculty.jsp"/>
<jsp:include page="../common/newLeftSidebarFaculty.jsp"/>
<jsp:include page="../common/rightSidebarFaculty.jsp"/>
	</sec:authorize>
	<sec:authorize access="hasAnyRole('ROLE_STUDENT','ROLE_PARENT')">
	<jsp:include page="../common/rightSidebar.jsp" />
	</sec:authorize>

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper assignmentW">
		<sec:authorize access="hasAnyRole('ROLE_STUDENT','ROLE_PARENT')">
		<jsp:include page="../common/newTopHeader.jsp" />
		</sec:authorize>
		
		<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		</sec:authorize>

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
					<jsp:include page="../common/alert.jsp" />
					<div class="bg-white mb-5">
						<nav class="" aria-label="breadcrumb">
							<ol class="breadcrumb d-flex">
								<li class="breadcrumb-item"><a
									href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
								<li class="breadcrumb-item active" aria-current="page">Announcement
								</li>
							</ol>

						</nav>

						<div class="row">
							<div class="col-lg-6 col-md-12 col-sm-12 mt-4" id="courseRelated">
								<div class="card bg-light mb-3">
									<div class="card-header text-center">Course Related</div>
									<div class="card-body" id="courseAnn">
										<c:forEach var="announcement"
											items="${announcementTypeMap['COURSE']}" varStatus="status">
											<div class="announcementItem" data-toggle="modal"
												data-target="#modalAnnounceCourse${status.count}">
												<h6 class="card-title">${announcement.subject}<sup
														class="announcementDate text-danger font-italic"><small><span
															class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span
															class="toDate">${announcement.endDate}</span></small></sup>
												</h6>
												
												<p class="border-bottom"></p>
											</div>

											<div id="modalAnnouncement position-fixed">
												<div class="modal fade fnt-13" id="modalAnnounceCourse${status.count}"
													tabindex="-1" role="dialog"
													aria-labelledby="submitAssignmentTitle" aria-hidden="true">
													<div
														class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
														role="document">
														<div class="modal-content">
															<div class="modal-header">
																<h6 class="modal-title">${announcement.subject}</h6>
																<button type="button" class="close" data-dismiss="modal"
																	aria-label="Close">
																	<span aria-hidden="true">&times;</span>
																</button>
															</div>
															<div class="modal-body">
																<div class="d-flex font-weight-bold">
																	<p class="mr-auto">
																		Start Date: <span>${announcement.startDate}</span>
																	</p>
																	<p>
																		End Date: <span>${announcement.endDate}</span>
																	</p>
																</div><c:if test="${announcement.filePath ne null}">
																<p class="font-weight-bold">Attachment: <a style="color: red!important;"  href="sendAnnouncementFile?id=${announcement.id}"><i class="fas fa-download"></i> Download</a></p>
																</c:if>

																<p class="announcementDetail">${announcement.description}</p>
															</div>
															<div class="modal-footer">
															<c:url value="deleteFacultyAnnouncement" var="deleteAnn">
																<c:param name="id" value="${announcement.id}"/>  
															</c:url> 
															<c:if test="${announcement.createdBy eq userBean.username}">
																<a href="${deleteAnn}" onclick="return confirm('Are you sure you want to delete this Announcement?')" class="btn btn-warning text-light text-decoration-none"
																>Delete</a>
															</c:if>
															
																<button type="button" class="btn btn-modalClose"
																	data-dismiss="modal">Close</button>
															</div>
														</div>
													</div>
												</div>
											</div>

										</c:forEach> 
										<c:if test="${empty announcementTypeMap['COURSE']}">
											<h6 align="center">No data</h6>
										</c:if>
									</div>
									<div class="card-footer text-center">
										<div class="d-flex">
											<!-- <div class="mr-auto">
                                            <div class="input-group flex-nowrap input-group-sm">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text cust-select-span" id="addon-wrapping">Show</span>
                                                </div>
                                                
                                            <select class="cust-select">
                                            <option>5</option>
                                            <option>10</option>
                                            <option>15</option>
                                            <option>20</option>
                                            <option>25</option>
                                            <option>30</option>
                                            <option>All</option>
                                            </select>
                                            </div>                    
                                    </div> -->
											<div>
												<label class="sr-only">Announcement Sub Type</label> <select
													type="text" class="form-control" id="stCourse">
													<option selected>ALL</option>
													<c:forEach var="subtyp" items="${courseSubType}"
														varStatus="sub">
														<c:if test="${subtyp ne null}">
														<option value="${subtyp}">${subtyp}</option>
														</c:if>
													</c:forEach>
												</select>
											</div>

										</div>
									</div>
								</div>
							</div>
							<div class="col-lg-6 col-md-12 col-sm-12 mt-4"
								id="instituteRelated">
								<div class="card bg-light mb-3">
									<div class="card-header text-center">Institute Related</div>
									<div class="card-body" id="instituteAnn">
										<c:forEach var="announcement"
											items="${announcementTypeMap['INSTITUTE']}"
											varStatus="status">
											<div class="announcementItem" data-toggle="modal"
												data-target="#modalAnnounceInstitute${status.count}">
												<h6 class="card-title">${announcement.subject}<sup
														class="announcementDate text-danger font-italic"><small><span
															class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span
															class="toDate">${announcement.endDate}</span></small></sup>
												</h6>
												
												<p class="border-bottom"></p>
											</div>
											
											<div id="modalAnnouncement position-fixed">
												<div class="modal fade fnt-13" id="modalAnnounceInstitute${status.count}"
													tabindex="-1" role="dialog"
													aria-labelledby="submitAssignmentTitle" aria-hidden="true">
													<div
														class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
														role="document">
														<div class="modal-content">
															<div class="modal-header">
																<h6 class="modal-title">${announcement.subject}</h6>
																<button type="button" class="close" data-dismiss="modal"
																	aria-label="Close">
																	<span aria-hidden="true">&times;</span>
																</button>
															</div>
															<div class="modal-body">
																<div class="d-flex font-weight-bold">
																	<p class="mr-auto">
																		Start Date: <span>${announcement.startDate}</span>
																	</p>
																	<p>
																		End Date: <span>${announcement.endDate}</span>
																	</p>
																</div><c:if test="${announcement.filePath ne null}">
																<p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}" style="color: red!important;"><i class="fas fa-download"></i> Download</a></p>
																</c:if>

																<p class="announcementDetail">${announcement.description}</p>
															</div>
															<div class="modal-footer">
															
															<c:url value="deleteFacultyAnnouncement" var="deleteAnn">
																<c:param name="id" value="${announcement.id}"/>  
															</c:url> 
															<c:if test="${announcement.createdBy eq userBean.username}">
																<a href="${deleteAnn}" onclick="return confirm('Are you sure you want to delete this Announcement?')" class="btn btn-warning text-light text-decoration-none"
																>Delete</a>
															</c:if>
															
																<button type="button" class="btn btn-modalClose"
																	data-dismiss="modal">Close</button>
															</div>
														</div>
													</div>
												</div>
											</div>

										</c:forEach>
										<c:if test="${empty announcementTypeMap['INSTITUTE']}">
											<h6 align="center">No data</h6>
										</c:if>

									</div>
									<div class="card-footer text-center">
										<div class="d-flex">
											<!-- <div class="mr-auto">
                                            <div class="input-group flex-nowrap input-group-sm">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text cust-select-span" id="addon-wrapping">Show</span>
                                                </div>
                                                
                                            <select class="cust-select">
                                            <option>5</option>
                                            <option>10</option>
                                            <option>15</option>
                                            <option>20</option>
                                            <option>25</option>
                                            <option>30</option>
                                            <option>All</option>
                                            </select>
                                            </div>                    
                                    </div> -->
											<div>
												<label class="sr-only">Announcement Sub Type</label> <select
													type="text" class="form-control" id="stInstitute">
													<option selected>ALL</option>
													<c:forEach var="subtyp" items="${instituteSubType}"
														varStatus="sub">
														<c:if test="${subtyp ne null}">
														<option value="${subtyp}">${subtyp}</option>
														</c:if>
													</c:forEach>
												</select>
											</div>

										</div>
									</div>
								</div>
							</div>
							<div class="col-lg-6 col-md-12 col-sm-12 mt-4"
								id="libraryRelated">
								<div class="card bg-light mb-3">
									<div class="card-header text-center">Library Related</div>
									<div class="card-body" id="libraryAnn">
										<c:forEach var="announcement"
											items="${announcementTypeMap['LIBRARY']}" varStatus="status">
											<div class="announcementItem" data-toggle="modal"
												data-target="#modalAnnounceLibrary${status.count}">
												<h6 class="card-title">${announcement.subject}<sup
														class="announcementDate text-danger font-italic"><small><span
															class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span
															class="toDate">${announcement.endDate}</span></small></sup>
												</h6>
												
												<p class="border-bottom"></p>
											</div>
											
											<div id="modalAnnouncement position-fixed">
												<div class="modal fade fnt-13" id="modalAnnounceLibrary${status.count}"
													tabindex="-1" role="dialog"
													aria-labelledby="submitAssignmentTitle" aria-hidden="true">
													<div
														class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
														role="document">
														<div class="modal-content">
															<div class="modal-header">
																<h6 class="modal-title">${announcement.subject}</h6>
																<button type="button" class="close" data-dismiss="modal"
																	aria-label="Close">
																	<span aria-hidden="true">&times;</span>
																</button>
															</div>
															<div class="modal-body">
																<div class="d-flex font-weight-bold">
																	<p class="mr-auto">
																		Start Date: <span>${announcement.startDate}</span>
																	</p>
																	<p>
																		End Date: <span>${announcement.endDate}</span>
																	</p>
																</div><c:if test="${announcement.filePath ne null}">
																<p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}" style="color: red!important;"><i class="fas fa-download"></i> Download</a></p>
																</c:if>

																<p class="announcementDetail">${announcement.description}</p>
															</div>
															<div class="modal-footer">
																<button type="button" class="btn btn-modalClose"
																	data-dismiss="modal">Close</button>
															</div>
														</div>
													</div>
												</div>
											</div>

										</c:forEach>
										<c:if test="${empty announcementTypeMap['LIBRARY']}">
											<h6 align="center">No data</h6>
										</c:if>

									</div>
									<div class="card-footer text-center">
										<div class="d-flex">
											<!--  <div class="mr-auto">
                                            <div class="input-group flex-nowrap input-group-sm">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text cust-select-span" id="addon-wrapping">Show</span>
                                                </div>
                                                
                                            <select class="cust-select">
                                            <option>5</option>
                                            <option>10</option>
                                            <option>15</option>
                                            <option>20</option>
                                            <option>25</option>
                                            <option>30</option>
                                            <option>All</option>
                                            </select>
                                            </div>                    
                                    </div> -->
											<div>
												<label class="sr-only">Announcement Sub Type</label> <select
													type="text" class="form-control" id="stLibrary">
													<option selected>ALL</option>
													<c:forEach var="subtyp" items="${librarySubType}"
														varStatus="sub">
														<c:if test="${subtyp ne null}">
														<option value="${subtyp}">${subtyp}</option>
														</c:if>
													</c:forEach>
												</select>
											</div>

										</div>
									</div>
								</div>
							</div>
							<div class="col-lg-6 col-md-12 col-sm-12 mt-4"
								id="counselorRelated">
								<div class="card bg-light mb-3">
									<div class="card-header text-center">Counselor Related</div>
									<div class="card-body" id="counselorAnn">
										<c:forEach var="announcement"
											items="${announcementTypeMap['COUNSELOR']}"
											varStatus="status">
											<div class="announcementItem" data-toggle="modal"
												data-target="#modalAnnounceCounselor${status.count}">
												<h6 class="card-title">${announcement.subject}<sup
														class="announcementDate text-danger font-italic"><small><span
															class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span
															class="toDate">${announcement.endDate}</span></small></sup>
												</h6>
												
												<p class="border-bottom"></p>
											</div>
											
											<div id="modalAnnouncement position-fixed">
												<div class="modal fade fnt-13" id="modalAnnounceCounselor${status.count}"
													tabindex="-1" role="dialog"
													aria-labelledby="submitAssignmentTitle" aria-hidden="true">
													<div
														class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
														role="document">
														<div class="modal-content">
															<div class="modal-header">
																<h6 class="modal-title">${announcement.subject}</h6>
																<button type="button" class="close" data-dismiss="modal"
																	aria-label="Close">
																	<span aria-hidden="true">&times;</span>
																</button>
															</div>
															<div class="modal-body">
																<div class="d-flex font-weight-bold">
																	<p class="mr-auto">
																		Start Date: <span>${announcement.startDate}</span>
																	</p>
																	<p>
																		End Date: <span>${announcement.endDate}</span>
																	</p>
																</div><c:if test="${announcement.filePath ne null}">
																<p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}" style="color: red!important;"> <i class="fas fa-download"></i> Download</a></p>
																</c:if>

																<p class="announcementDetail">${announcement.description}</p>
															</div>
															<div class="modal-footer">
																<button type="button" class="btn btn-modalClose"
																	data-dismiss="modal">Close</button>
															</div>
														</div>
													</div>
												</div>
											</div>

										</c:forEach>
										<c:if test="${empty announcementTypeMap['COUNSELOR']}">
											<h6 align="center">No data</h6>
										</c:if>

									</div>
									<div class="card-footer text-center">
										<div class="d-flex">
											<!-- <div class="mr-auto">
                                            <div class="input-group flex-nowrap input-group-sm">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text cust-select-span" id="addon-wrapping">Show</span>
                                                </div>
                                                
                                            <select class="cust-select">
                                            <option>5</option>
                                            <option>10</option>
                                            <option>15</option>
                                            <option>20</option>
                                            <option>25</option>
                                            <option>30</option>
                                            <option>All</option>
                                            </select>
                                            </div>                    
                                    </div> -->
											<div>
												<label class="sr-only">Announcement Sub Type</label> <select
													type="text" class="form-control" id="stCounselor">
													<option selected>ALL</option>
													<c:forEach var="subtyp" items="${counselorSubType}"
														varStatus="sub">
														<c:if test="${subtyp ne null}">
														<option value="${subtyp}">${subtyp}</option>
														</c:if>
													</c:forEach>
												</select>
											</div>

										</div>
									</div>
								</div>
							</div>

							<div class="col-lg-6 col-md-12 col-sm-12 mt-4"
								id="programRelated">
								<div class="card bg-light mb-3">
									<div class="card-header text-center">Program Related</div>
									<div class="card-body" id="programAnn">
										<c:forEach var="announcement"
											items="${announcementTypeMap['PROGRAM']}" varStatus="status">
											<div class="announcementItem" data-toggle="modal"
												data-target="#modalAnnounceProgram${status.count}">
												<h6 class="card-title">${announcement.subject}<sup
														class="announcementDate text-danger font-italic"><small><span
															class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span
															class="toDate">${announcement.endDate}</span></small></sup>
												</h6>
												
												<p class="border-bottom"></p>
											</div>
											<div id="modalAnnouncement position-fixed">
												<div class="modal fade fnt-13" id="modalAnnounceProgram${status.count}"
													tabindex="-1" role="dialog"
													aria-labelledby="submitAssignmentTitle" aria-hidden="true">
													<div
														class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
														role="document">
														<div class="modal-content">
															<div class="modal-header">
																<h6 class="modal-title">${announcement.subject}</h6>
																<button type="button" class="close" data-dismiss="modal"
																	aria-label="Close">
																	<span aria-hidden="true">&times;</span>
																</button>
															</div>
															<div class="modal-body">
																<div class="d-flex font-weight-bold">
																	<p class="mr-auto">
																		Start Date: <span>${announcement.startDate}</span>
																	</p>
																	<p>
																		End Date: <span>${announcement.endDate}</span>
																	</p>
																</div><c:if test="${announcement.filePath ne null}">
																<p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}" style="color: red!important;"><i class="fas fa-download"></i> Download</a></p>
																</c:if>

																<p class="announcementDetail">${announcement.description}</p>
															</div>
															<div class="modal-footer">
																<button type="button" class="btn btn-modalClose"
																	data-dismiss="modal">Close</button>
															</div>
														</div>
													</div>
												</div>
											</div>

										</c:forEach>
										<c:if test="${empty announcementTypeMap['PROGRAM']}">
											<h6 align="center">No data</h6>
										</c:if>

									</div>
									<div class="card-footer text-center">
										<div class="d-flex">
											<!-- <div class="mr-auto">
                                            <div class="input-group flex-nowrap input-group-sm">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text cust-select-span" id="addon-wrapping">Show</span>
                                                </div>
                                                
                                            <select class="cust-select">
                                            <option>5</option>
                                            <option>10</option>
                                            <option>15</option>
                                            <option>20</option>
                                            <option>25</option>
                                            <option>30</option>
                                            <option>All</option>
                                            </select>
                                            </div>                    
                                    </div> -->
											<div>
												<label class="sr-only">Announcement Sub Type</label> <select
													type="text" class="form-control" id="stProgram">
													<option selected>ALL</option>
													<c:forEach var="subtyp" items="${programSubType}"
														varStatus="sub">
														<c:if test="${subtyp ne null}">
														<option value="${subtyp}">${subtyp}</option>
														</c:if>
													</c:forEach>
												</select>
											</div>

										</div>
									</div>
								</div>
							</div>
  
  
  							<div class="col-lg-6 col-md-12 col-sm-12 mt-4"
								id="timetableRelated">
								<div class="card bg-light mb-3">
									<div class="card-header text-center">TimeTable Related</div>
									<div class="card-body" id="timetableAnn">
										<c:forEach var="announcement"
											items="${announcementTypeMap['TIMETABLE']}" varStatus="status">
											<div class="announcementItem" data-toggle="modal"
												data-target="#modalAnnounceProgram${status.count}">
												<h6 class="card-title">${announcement.subject}<sup
														class="announcementDate text-danger font-italic"><small><span
															class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span
															class="toDate">${announcement.endDate}</span></small></sup>
												</h6>
												
												<p class="border-bottom"></p>
											</div>
											<div id="modalAnnouncement position-fixed">
												<div class="modal fade fnt-13" id="modalAnnounceProgram${status.count}"
													tabindex="-1" role="dialog"
													aria-labelledby="submitAssignmentTitle" aria-hidden="true">
													<div
														class="modal-dialog modal-dialog-centered modal-dialog-scrollable"
														role="document">
														<div class="modal-content">
															<div class="modal-header">
																<h6 class="modal-title">${announcement.subject}</h6>
																<button type="button" class="close" data-dismiss="modal"
																	aria-label="Close">
																	<span aria-hidden="true">&times;</span>
																</button>
															</div>
															<div class="modal-body">
																<div class="d-flex font-weight-bold">
																	<p class="mr-auto">
																		Start Date: <span>${announcement.startDate}</span>
																	</p>
																	<p>
																		End Date: <span>${announcement.endDate}</span>
																	</p>
																</div><c:if test="${announcement.filePath ne null}">
																<p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}" target="_blank" style="color: red!important;"><i class="fas fa-download"></i> Download</a></p>
																</c:if>

																<p class="announcementDetail">${announcement.description}</p>
															</div>
															<div class="modal-footer">
																<button type="button" class="btn btn-modalClose"
																	data-dismiss="modal">Close</button>
															</div>
														</div>
													</div>
												</div>
											</div>

										</c:forEach>
										<c:if test="${empty announcementTypeMap['TIMETABLE']}">
											<h6 align="center">No data</h6>
										</c:if>

									</div>
									<div class="card-footer text-center">
										<div class="d-flex">
											<!-- <div class="mr-auto">
                                            <div class="input-group flex-nowrap input-group-sm">
                                                <div class="input-group-prepend">
                                                    <span class="input-group-text cust-select-span" id="addon-wrapping">Show</span>
                                                </div>
                                                
                                            <select class="cust-select">
                                            <option>5</option>
                                            <option>10</option>
                                            <option>15</option>
                                            <option>20</option>
                                            <option>25</option>
                                            <option>30</option>
                                            <option>All</option>
                                            </select>
                                            </div>                    
                                    </div> -->
											<div>
												<label class="sr-only">Announcement Sub Type</label> <select
													type="text" class="form-control" id="stProgram">
													<option selected>ALL</option>
													<c:forEach var="subtyp" items="${timetableSubType}"
														varStatus="sub">
														<c:if test="${subtyp ne null}">
														<option value="${subtyp}">${subtyp}</option>
														</c:if>
													</c:forEach>
												</select>
											</div>

										</div>
									</div>
								</div>
							</div>

						</div>


					</div>




				</div>



				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />