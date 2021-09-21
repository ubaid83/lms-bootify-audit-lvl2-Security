<jsp:include page="../common/header.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
 <jsp:include page="../common/sidebar.jsp" />  
<section class="container-fluid">


  
        
                           
       
	
	<!-- Example row of columns -->
        <div class="row">
        	<div style="padding:0px 25px 25px 300px;">
				
                <div class="dashboard clearfix">
                	<h2 class="boldTitle">${course.courseName }</h2>
                    <ul class="tiles">
					<div class="clearfix col-sm-8 padding-small">
					
						<c:url value="viewUserAnnouncements" var="announcementListUrl">
							<c:param name="courseId">${course.id}</c:param>
						</c:url>
						
						<c:url value="addAnnouncementForm" var="addAnnouncementUrl">
							<c:param name="courseId">${course.id}</c:param>
						</c:url>
									
						
						<li class="tile tile-1 tile-1-slider">
							<a class="btn btn-default btn-sm pull-right" role="button" href="${addAnnouncementUrl}">ADD ANNOUNCEMENT</a>
							<h2 style="color: #fff;">ANNOUNCEMENTS</h2>
							<div id="carousel-example-generic" class="carousel slide"
								data-ride="carousel">
								<a href="${addAnnouncementUrl}" >
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
					</ul>
					
					<div class="clearfix col-sm-4 padding-small">
						<c:url value="gradeCenter" var="gradeCenterUrl">
							<c:param name="courseId">${course.id}</c:param>
						</c:url>
						<a href="${gradeCenterUrl}">
						<li class="tile theme-5 rotate3d rotate3dX">
								<div class="faces">
                                    <div class="front"><span><i class="fa-custom fa-check-square"></i></span></div>
                                    <div class="back"><p>Grade Center</p></div>
                                  </div>
						</li>
						</a>
					</div>
					
					
				</ul>
				<ul class="tiles">
					<div class="clearfix col-sm-4 padding-small">
						<li class="tile theme-9 slideTextRight doubleHeight">
								<div class="assignmentsList">
									<c:if test="${not empty tests}">
										<table class="table">
											<c:forEach items="${tests}" var="test" varStatus="status">
												<c:url value="viewTestDetails" var="testUrl">
													<c:param name="testId">${test.id}</c:param>
												</c:url>
												<tr>
													<td><p><a href="${testUrl }">${status.count}</a></p></td>
													<td><p>
														<a href="${testUrl }">${test.testName}</a>
													</p></td>
													<td><p>
														<a href="#"><c:out value="${fn:substring(test.endDate, 
                                0,10)}" ></c:out></a>
													</p></td>
												</tr>
											</c:forEach>
										</table>
									</c:if>
									<c:url value="testList" var="viewAllTestUrl">
										<c:param name="courseId">${courseId}</c:param>
									</c:url>
									<c:url value="addTestForm" var="addTestUrl">
										<c:param name="courseId">${courseId}</c:param>
									</c:url>
									<p class="viewAllBtn">
									<a class="btn btn-default btn-sm" role="button" href="${addTestUrl}">ADD TEST</a>
									<a class="btn btn-default btn-sm" role="button" href="${viewAllTestUrl }">VIEW ALL</a>
									</p>
								</div>
								<div>
									<p>TESTS</p>
								</div>
						</li>
					</div>
					<div class="clearfix col-sm-4 padding-small">
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
												
												<c:url value="viewAssignment" var="viewAssignmentUrl">
													<c:param name="id">${assignment.id}</c:param>
												</c:url>
												
												
												<tr>
													<td><p>${status.count}</p></td>
													<td><p><a href="${viewAssignmentUrl}">${assignment.assignmentName}</a></p></td>
												</tr>
											</c:forEach>
										</table>
									</c:if>
									<c:url value="searchFacultyAssignment" var="assignmentListUrl">
										<c:param name="courseId">${course.id}</c:param>
									</c:url>
									<c:url value="createAssignmentForm" var="addAssignmentUrl">
										<c:param name="courseId">${course.id}</c:param>
									</c:url>
									<p class="viewAllBtn">
									<a class="btn btn-default btn-sm" role="button" href="${addAssignmentUrl}">ADD ASSIGNMENT</a>
									<a class="btn btn-default btn-sm" role="button" href="${assignmentListUrl}">VIEW ALL</a>
									</p>
								</div>
						</li>
					</div>
					<div class="clearfix col-sm-4 padding-small">
						<c:url value="getContentUnderAPathForFaculty" var="contentListUrl">
							<c:param name="courseId">${course.id}</c:param>
						</c:url>
						<a href="${contentListUrl}">
						<li class="tile theme-12 rotate3d rotate3dX doubleHeight">
								<div class="faces">
                                    <div class="front"><span><i class="fa-custom fa-folder-o"></i></span></div>
                                    <div class="back"><p>Content</p></div>
                                  </div>
						</li>
						</a>
					</div>
				</ul>
                </div><!--end dashboard-->
              
            </div>
        </div>
</section>
<jsp:include page="../common/footer.jsp" />