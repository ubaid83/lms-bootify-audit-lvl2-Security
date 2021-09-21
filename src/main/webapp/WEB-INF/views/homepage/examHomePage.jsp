<jsp:include page="../common/topHeaderLibrian.jsp" />
<jsp:include page="../common/css.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<body class="nav-md footer_fixed dashboard_left">

	<!-- Example row of columns -->
	<div class="loader"></div>
	<div class="container body">
		<div class="main_container">


			<!--  -->
			<div class="right_col" role="main">

				<div class="dashboard_container">

					<!-- <div class="dash-main"> -->
					<div class="dashboard_height" id="main">


						<div class="dashboard_contain_specing dash-main">

							<div class="alert alert-success alert-dismissible" role="alert">
								<button type="button" class="close" data-dismiss="alert"
									aria-label="Close">
									<span aria-hidden="true">&times;</span>
								</button>
								<h4>
									<strong>Welcome to Exam Login of
										<p>${libraryName }</p>
									</strong>

								</h4>
							</div>

							<div class="container-fluid dashboardcon">


								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor2">
										<div>
											<c:url value="searchAnnouncementForLibrarian"
												var="viewAnnouncementUrl">
												<c:param name="announcementType" value="INSTITUTE" />
												<c:param name="announcementSubType" value="EXAM" />

											</c:url>
										</div>
										<span><a href="${viewAnnouncementUrl}" />Announcement</a></span>
									</div>
								</div>

								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor4">
										<div>
											<c:url value="viewLibraryAnnouncements" var="viewLibraryUrl">
												<c:param name="announcementType" value="LIBRARY" />
											</c:url>
										</div>
										<span><a href="${viewLibraryUrl}" />Library</a></span>
									</div>
								</div>

								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor4">
										<div></div>
										<span><a href="https://ezproxy.svkm.ac.in/login"
											target="_blank" />E-database Link</a></span>
									</div>
								</div>

								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor7">
										<div>
											<c:url value="visitExamApp" var="visitExamAppUrl">
											</c:url>
										</div>
										<span><a href="${visitExamAppUrl}" target="_blank"/>Practical Exam</a></span>
									</div>
								</div>
								
								
								<c:if test="${userBean.username eq '32200093_EXAM'}">
                                                      <div class="col-md-4 course_widget_height">
                                                      <div class="dashboardcon_title bgcolor8">
                                                            <div>
                                                                  <c:url value="uploadTimeLimitSession"
                                                                        var="uploadTimeLimitSession">
                                                                        <c:param name="" value="" />
                                                                  </c:url>
                                                            </div>
                                                            <span><a href="${uploadTimeLimitSession}">Upload
                                                                        Time Limit For Session</a></span>
                                                      </div>
                                                </div>
                                                </c:if>

								
								<c:if test="${userBean.username eq '32200093_EXAM' || (userBean.username eq '32200465_EXAM')}">
								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor10">
										<div>			
										</div>
										<span><a href="icaListBySupportAdmin">Change ICA
												Date </a></span>
									</div>
								</div> 
								</c:if>
								<c:if test="${(userBean.username eq '32200465_EXAM')}">
								<div class="col-md-4 course_widget_height">
									<div class="dashboardcon_title bgcolor11">
										<div>			
										</div>
										<span><a href="teeListBySupportAdmin">Change TEE Date </a></span>
									</div>
								</div> 
								</c:if>

						<c:if test="${userBean.username eq '32200093_EXAM'}">
                                                      <div class="col-md-4 course_widget_height">
                                                      <div class="dashboardcon_title bgcolor8">
                                                            <div>
                                                                  <c:url value="downloadReportLinkExam"
                                                                        var="downloadReportLinkExam">
                                                                        <c:param name="" value="" />
                                                                  </c:url>
                                                            </div>
                                                            <span><a href="${downloadReportLinkExam}">Download Report For Exam</a></span>
                                                      </div>
                                                </div>
                                 </c:if>
								<!--end dashboard-->




							</div>
						</div>
					</div>
				</div>


			</div>
		</div>
	</div>
</body>

<jsp:include page="../common/footerLibrarian.jsp" />

<script>
	$(document).ready(
			function() {
				var cars = [ "bgcolor1", "bgcolor2", "bgcolor3", "bgcolor4",
						"bgcolor5", "bgcolor6", "bgcolor7", "bgcolor8",
						"bgcolor9" ];
				var count = 0;
				$('[id^=courseDetail]').each(function() {
					if (count == cars.length - 1) {
						count = 0;
					}
					$(this).addClass(cars[count]);
					count++;
				})

				$('body').addClass("dashboard_left");
			});
</script>
