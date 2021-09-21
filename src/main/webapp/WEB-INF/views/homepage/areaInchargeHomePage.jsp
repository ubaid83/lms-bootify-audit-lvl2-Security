<jsp:include page="../common/header.jsp" />
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<section class="container-fluid">
      <!-- Example row of columns -->
              
      <div class="row">
  
            <div  class="demo-wrapper"   >
                           
                <div class="dashboard clearfix" style="padding:0px 0px 0px 0px;" >
                 
                    <ul class="tiles">
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
				</ul>
				<ul class="tiles">
					<div class="clearfix col-sm-4 padding-small">
						<li class="tile theme-4 slideTextUp doubleHeight">
							<div>
								<p>
									<i class="fa fa-file-text-o"></i> COURSES
								</p>
							</div>
							<div class="assignmentsList">
								<c:if test="${not empty courses}">
									<table class="table">
										<c:forEach items="${courses }" var="course" varStatus="status">
											<tr>
												<td><p>${status.count}</p></td>
												<td><p>
														<a href="<c:url value="/viewCourse?id=${course.id}" />">${course.courseName}</a>
													</p></td>
											</tr>
										</c:forEach>
									</table>
								</c:if>
							</div>
						</li>
					</div>
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
									<p class="viewAllBtn"><a class="btn btn-default btn-sm" role="button" href="testList">VIEW ALL</a></p>
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
									<p class="viewAllBtn"><a class="btn btn-default btn-sm" role="button" href="searchFacultyAssignment">VIEW ALL</a></p>
								</div>
						</li>
					</div>
				</ul>
				<ul class="tiles">
					<div class="clearfix col-sm-6 padding-small">
						<a href="getContentUnderAPath">
						<li class="tile theme-6 rotate3d rotate3dY doubleHeight">
                          <div class="faces">
                            <div class="front"><span class="icon-instagram"><i class="fa-custom fa-folder-o"></i></span></div>
                            <div class="back"><p>CONTENT</p></div>
                          </div>
                        </li>
                        </a>
					</div>
					<div class="clearfix col-sm-6 padding-small">
						<a href="viewLibrary">
						<li class="tile theme-11 rotate3d rotate3dX doubleHeight">
								<div class="faces">
                                    <div class="front"><span><i class="fa-custom fa-university"></i></span></div>
                                    <div class="back"><p>DIGITAL LIBRARY</p></div>
                                  </div>
						</li>
						</a>
					</div>
				</ul>
			</div>
			<!--end dashboard-->

		</div>
	</div>
</section>
<jsp:include page="../common/footer.jsp" />