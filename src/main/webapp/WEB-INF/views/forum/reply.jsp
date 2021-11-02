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

<div class="d-flex dataTableBottom" id="facultyAssignmentPage">
	<sec:authorize access="hasRole('ROLE_FACULTY')">
		<jsp:include page="../common/newLeftNavBarFaculty.jsp" />
		<jsp:include page="../common/newLeftSidebarFaculty.jsp" />
		<jsp:include page="../common/rightSidebarFaculty.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftNavbar.jsp" />
		<jsp:include page="../common/newLeftSidebar.jsp" />
		<jsp:include page="../common/rightSidebar.jsp" />
	</sec:authorize>

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<sec:authorize access="hasRole('ROLE_FACULTY')">
			<jsp:include page="../common/newTopHeaderFaculty.jsp" />
		</sec:authorize>
		<sec:authorize access="hasRole('ROLE_STUDENT')">
			<jsp:include page="../common/newTopHeader.jsp" />
		</sec:authorize>


		<%
		String courseRecord = request.getParameter("courseRecord");
		%>
		<%
		boolean isEdit = "true".equals((String) request.getAttribute("edit"));
		%>

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

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
							<li class="breadcrumb-item active" aria-current="page">View
								Forum</li>
						</ol>
					</nav>

					<jsp:include page="../common/alert.jsp" />


					<!-- Input Form Panel -->
					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">

									<div class="x_title">
										<h5 style="font-size: 24px; font-family: sans-serif;">${forum.topic}</h5>

									</div>

									<div class="x_content">
										<form action="replyToQuestion" method="post"
											id="replyToQuestion">


											<input value="${courseId}" type="hidden" /> <input
												value="${id}" type="hidden" />

											<div class="form-group">
												<label for="question"></label> <b style="font-size: 20px;">${forum.question}</b>
											</div>


											<%-- <div class="well"
												style="color: white; padding: 8px; background-color: dimgrey">

												<b style="font-size: 15px;"> <i class="fa fa-user"
													aria-hidden="true"></i> ${forum.firstname}
													${forum.lastname} | ${forum.dateCreated}
												</b>

											</div> --%>
											<div class="well"
												style="color: white; padding: 8px; background-color: dimgrey">

												<b style="font-size: 15px;"> <i class="fa fa-user"
													aria-hidden="true"></i> ${forum.firstname}
													${forum.lastname} | ${forum.dateCreated} <c:if
														test="${forum.createdBy eq username }">
                                                                              | 
                                                                              <c:url
															value="updateForumForm" var="viewUrl">
															<c:param name="id" value="${forum.id}" />
														</c:url>
														<c:url value="deleteForum" var="deleteUrl">
															<c:param name="id" value="${forum.id}" />
														</c:url>

														<a href="${viewUrl}" title="Edit"><i
															style="color: white;" class="fa fa-info-circle fa-lg"></i></a>
														<a href="${deleteUrl}" title="Delete"
															onclick="return confirm('Are you sure you want to delete this discussion?')"><i
															style="color: white;" class="fas fa-trash"></i></a>
													</c:if>
												</b>

											</div>



										</form>
									</div>
								</div>
							</div>
						</div>
					</div>

					<!-- Results Panel -->


					<div class="card bg-white border">
						<div class="card-body">
							<div class="col-xs-12 col-sm-12">
								<div class="x_panel">
									<div class="x_title">
										<h5>Answers | ${fn:length(allReplies)} Records found</h5>

									</div>
									<div class="x_itemCount" style="display: none;">
										<div class="image_not_found">
											<i class="fa fa-newspaper-o"></i>
											<p>
												<label class=""></label>${fn:length(allReplies)} Answers
											</p>
										</div>
									</div>
									<div class="x_content">
										<form:form action="replyToQuestion" method="post"
											modelAttribute="forumReply" enctype="multipart/form-data">


											<form:input path="courseId" type="hidden" />
											<form:input path="id" type="hidden" />

											<div class="col-sm-12">

												<div class="form-group">

													<form:label path="reply" for="editor">Enter Your Reply<span
															style="color: red">*</span>
													</form:label>
													<%-- <form:textarea class="form-group" path="reply" id="editor"
														style="margin-top: 30px;margin-bottom:10px; white-space: pre-wrap;" /> --%>
													<form:textarea class="form-group" path="reply"
														name="editor1" id="editor1" rows="10" cols="80"
														required="required" />



												</div>
											</div>

											<div class="col-12">
												<button id="submit" class="btn btn-large btn-primary"
													formaction="replyToQuestion?questionId=${forum.id}">Reply</button>
												<!-- 													<button id="reset" type="reset" class="btn btn-danger">Reset</button>
 -->
											</div>

											<!-- <hr style="height: 1px; border: none; color: #333; background-color: #333;"> -->
											<!-- <div id="llc_comments" class="well"
												style="outline: 0; box-sizing: border-box; display: block; background-color: white;"> -->

											<c:forEach var="forumReply" items="${allReplies}"
												varStatus="status">
												<div id="comments" class="comments-area well mt-3">
													<article class="comment-body">
														<header>
															<h6 class="p-2">${forumReply.firstname}
																${forumReply.lastname}| ${forumReply.repliedDate} | <b
																	id="likes${forumReply.id}"> <c:if
																		test="${forumReply.showLike eq true}">

																		<a href="#" id="like${forumReply.id}"
																			class="likeClass"
																			onclick='document.getElementById(this.id).removeAttribute("href");'>
																			<i style="color: black;" class="fa fa-thumbs-up"></i>

																			<!-- <span class="glyphicon glyphicon-thumbs-up"></span> -->
																		</a>

																	</c:if><input type="hidden" id="liked${forumReply.id}"
																	value="${forumReply.likes}"> Likes :

																	${forumReply.likes}
																</b> | <b id="dislikes${forumReply.id}"> <c:if
																		test="${forumReply.showDislike eq true}">

																		<a href="#" id="dislike${forumReply.id}"
																			class="dislikeClass"
																			onclick='document.getElementById(this.id).removeAttribute("href");'>
																			<!-- <span class="glyphicon glyphicon-thumbs-down"></span> -->
																			<i style="color: black;" class="fa fa-thumbs-down"></i>

																		</a>
																	</c:if> <input type="hidden" id="disliked${forumReply.id}"
																	value="${forumReply.disLikes}"> Dislikes :

																	${forumReply.disLikes}
																</b> | <a href="#" id="reply${forumReply.id}"
																	class="replyClass">Reply </a><b style="float: right">#<c:out
																		value="${status.count}" /></b>
															</h6>
															<p>${forumReply.reply}</p>
														</header>

													</article>
													<div class="row" id="replyDiv${forumReply.id}"
														style="display: none">
														<div class="form-group">

															<form:label path="counterReply" for="editor">Enter Your Reply</form:label>
															<%-- <form:textarea class="form-group" path="reply" id="editor"
														style="margin-top: 30px;margin-bottom:10px; white-space: pre-wrap;" /> --%>
															<form:textarea class="form-group" path="counterReply"
																name="editor1" id="editor1" rows="10" cols="80" />



														</div>



														<div class="col-md-4 col-sm-6 col-xs-12 column">
															<button id="submit" class="btn btn-large btn-primary"
																formaction="replyToReply?replyId=${forumReply.id}&questionId=${forum.id}">Reply</button>
															<button id="reset" type="reset" class="btn btn-danger">Reset</button>
														</div>
													</div>
													<c:if test="${forumReply.counterReplyList.size() > 0 }">
														<hr>
														<c:forEach var="fr" items="${forumReply.counterReplyList}"
															varStatus="status1">

															<c:if test="${status1.count%2 == 0 }">
																<div id="comments" class=" counter comments-area well"
																	style="background-color: white; box-shadow: 0 0 11px 2px #dfdfdf; width: 90%; margin-left: auto; margin-right: 0;">
																	<article class="comment-body">
																		<header>
																			<h5 style="font-weight: 600;">${fr.firstname}
																				${fr.lastname} | ${fr.repliedDate}<b
																					style="float: right">#<c:out
																						value="${status1.count}" /></b>
																			</h5>
																			<p>${fr.answer}</p>
																		</header>

																	</article>


																</div>
															</c:if>
															<c:if test="${status1.count%2 != 0 }">
																<div id="comments"
																	class=" counter comments-area well right"
																	style="background-color: #cce6ff; box-shadow: 0 0 11px 2px #dfdfdf; width: 90%; margin-left: auto; margin-right: 0;">
																	<article class="comment-body">
																		<header>
																			<h5 style="font-weight: 600;">${fr.firstname}
																				${fr.lastname} | ${fr.repliedDate} <b
																					style="float: right">#<c:out
																						value="${status1.count}" /></b>
																			</h5>
																			<p>${fr.answer}</p>
																		</header>

																	</article>


																</div>
															</c:if>







														</c:forEach>
													</c:if>
												</div>
											</c:forEach>

											<!-- </div> -->
										</form:form>
									</div>
								</div>
							</div>
						</div>
					</div>


					<%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

					<!-- /page content: END -->

				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />



				<script type="text/javascript">
					$(".replyClass")
							.click(
									function() {
										$('#replyClass').click(function() {
											change(1);
										});
										console
												.log("called ........................................................000000.");
										$(this).css('color', 'blue');
										var likeId = $(this).attr("id");

										var replyId = likeId.substr(5, 6);
										console.log(replyId);
										var str = '#' + 'replyDiv' + replyId;
										//alert(str)
										$(str).removeAttr("style");

									});
				</script>

				<script type="text/javascript">
					$(".likeClass")
							.click(
									function() {
										$('#likeClass').click(function() {
											change(1);
										});
										console
												.log("called ........................................................000000.");
										$(this).css('color', 'blue');
										var likeId = $(this).attr("id");

										var replyId = likeId.substr(4, 5);
										console.log(replyId);
										$
												.ajax({
													type : 'GET',
													url : '${pageContext.request.contextPath}/updateLike?'
															+ 'replyId='
															+ replyId,
													success : function(data) {
														console
																.log("sucess messsgae e like "
																		+ likeId)
														$(this)
																.find('span')
																.addClass(
																		"icon-success");

														var likesId = $(this)
																.attr("id");

														var replyId = likeId
																.substr(4, 5);
														console.log(replyId);
														var str1 = "likes";
														var likeReply = str1
																.concat(replyId);
														console
																.log("like reply"
																		+ likeReply);

														var str2 = "liked";
														var likedReply = str2
																.concat(replyId);
														console
																.log("liked reply"
																		+ likedReply);

														var likeCount = $(
																'#'
																		+ likedReply)
																.val();
														var likeCountIncrement = 1 + parseInt(likeCount);
														console.log("likeCount"
																+ likeCount);
														$('#' + likeReply)
																.html(
																		"Likes:"
																				+ (likeCountIncrement));

														/* $('#' + likeReply)
																.html(
																		(likeCountIncrement));
														 */
														/* document
																.getElementById(likedReply).update = "_$taLikes:_$tag____________________________________________$tag"; */

													}

												});

									});
				</script>

				<script type="text/javascript">
					$(".dislikeClass")
							.click(
									function() {
										$('#dislikeClass').click(function() {
											change(1);
										});
										console
												.log("called ........................................................000000.");
										$(this).css('color', 'blue');
										var dislikeId = $(this).attr("id");

										var replyId = dislikeId.substr(7, 8);
										console.log(replyId);
										$
												.ajax({
													type : 'GET',
													url : '${pageContext.request.contextPath}/updateDisLike?'
															+ 'replyId='
															+ replyId,
													success : function(data) {

														$(this)
																.find('span')
																.addClass(
																		"icon-success");

														var replyId = dislikeId
																.substr(7, 8);
														var str1 = "dislikes";
														var dislikeReply = str1
																.concat(replyId);
														console
																.log("dislikes reply"
																		+ dislikeReply);

														var str2 = "disliked";
														var dislikedReply = str2
																.concat(replyId);
														console
																.log("disliked reply"
																		+ dislikedReply);

														var dislikeCount = $(
																'#'
																		+ dislikedReply)
																.val();
														var dislikeCountIncrement = 1 + parseInt(dislikeCount);
														console
																.log("dislikeCount"
																		+ dislikeCount);

														/* $('#' + dislikeReply)
																.html((dislikeCountIncrement));
														 */
														$('#' + dislikeReply)
																.html(
																		"Dislikes:"
																				+ (dislikeCountIncrement));
														$('#dislikeClass')
																.attr(
																		"disabled",
																		true);

													}

												});

									});
				</script>
				<script>
					$(document).ready(function() {
						$('#wrapper').on('click', '.link', function() {
							$(this).next().toggleClass('hide');
							return false;
						});
					});
				</script>


				<script type="text/javascript">
					CKEDITOR
							.replace(
									'editor1',
									{
										extraPlugins : 'mathjax',
										mathJaxLib : 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML'
									});
				</script>