<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">
	<jsp:include page="../common/newAdminLeftNavBar.jsp" />
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
					<li class="breadcrumb-item active" aria-current="page">Publish
						ICA</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">Publish ICA</h5>



					<div class="table-responsive mt-3 mb-3 testAssignTable">
						<table class="table table-striped table-hover">
							<thead>
								<tr>
									<th scope="col">Sl. No.</th>
									<th scope="col">ICA Name</th>

									<th scope="col">Module Name</th>
									<th scope="col">Assign Faculty</th>
									<th scope="col">Acad Year</th>
									<th scope="col">Acad Session</th>
									<th scope="col">Current Date</th>

									<th scope="col">Actions</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="ica" items="${submittedIcaList}"
									varStatus="status">
									<tr>
										<td><c:out value="${status.count}" /></td>
										<td><c:out value="${ica.icaName}" /></td>
										<%-- <td><c:out value="${course.abbr}" /></td> --%>
										<c:if test="${ica.courseName ne null}">
											<td><c:out value="${ica.moduleName}(${ica.courseName})" /></td>
										</c:if>
										<c:if test="${ica.courseName eq null}">
											<td><c:out value="${ica.moduleName}" /></td>
										</c:if>
										<td><c:out value="${ica.facultyName}" /></td>

										<td><c:out value="${ica.acadYear}" /></td>




										<td><c:out value="${ica.acadSession}" /></td>

										<td><c:out value="${currentDate}" /></td>

										<%-- <c:choose>
										<c:when test="${ica.isPublished eq 'Y'}">
											<td
																					
												<a href="#" id="publish${ica.id}" class="showClass" style="float: right; display: none;"
																					onclick='document.getElementById(this.id).removeAttribute("href");'>
																					Publish </a></td>
										</c:when>
										<c:otherwise>
										
										</c:otherwise>
										</c:choose>
										 --%>

										<td><c:choose>
												<c:when test="${ica.isPublished eq 'Y'}">
												Ica Published
												</c:when>
												<c:otherwise>
													<a href="#" id="publish${ica.id}" class="showClass"
														 onclick="publishIca('${ica.id}')">
														Publish </a>
													<p id="published${ica.id}" class="showClass"
														style="float: center; display: none;">Ica Published</p>

												</c:otherwise>
											</c:choose></td>



									</tr>
								</c:forEach>
							</tbody>
							<tfoot>
								<tr>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
									<th>--</th>
								</tr>
							</tfoot>
						</table>
					</div>
					<div class="col-12 mt-3">
						<form>
							<button id="publishId" class="btn btn-large btn-primary mt-2"
								formaction="publishAllIca">Publish All</button>
							<!-- 	<input type="button" id="publishId"
										value="Publish All" class="btn btn-dark mt-2" /> <a id="dlink"
										style="display: none;"></a> -->
						</form>
					</div>
				</div>
			</div>



			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->

		<jsp:include page="../common/newAdminFooter.jsp" />


<script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
		<script>
		
		function publishIca(icaId){
            console.log('publish Ica called');
                  
            //evvar testId = 'publish';
            
            var id = icaId;
			
			swal({
                title: "Publish ICA?",
                 
                icon: "warning",
                buttons: true,
                
                dangerMode: true,
            })
            .then((willDelete) => {
              if (willDelete) {
            	
            	  $
                  .ajax({
                        type : 'GET',
                        url : '${pageContext.request.contextPath}/publishOneIca?'
                                    + 'id=' + id,
                        success : function(data) {

                              $(this).find('span').addClass(
                                          "icon-success");
                              var str1 = "published";
                              var str2 = str1.concat(id);
                              $('#' + str2).css({
                                    'display' : 'block'
                              });
                              //$('#' + str2).show();
                              var str3 = "publish";
                              var str4 = str3.concat(id);

                              $('#' + str4).hide();

                        }

                  });
            	  
                swal("ICA Published Successfully", {
                  icon: "success",
                });
                
              } else {
                //swal("Your imaginary file is safe!");
              }
            });
            
            /*swal(
                        {
                              title : 'Are you sure you want to publish? once published marks will be visible to students',
                              // text: "It will permanently deleted !",
                              //type: 'warning',
                              //icon : 'success',
                              showCancelButton : true,
                              confirmButtonColor : '#3085d6',
                              cancelButtonColor : '#d33',
                        // confirmButtonText: 'Yes, delete it!'
                        }).then(function() {
                              $
                              .ajax({
                                    type : 'GET',
                                    url : '${pageContext.request.contextPath}/publishOneIca?'
                                                + 'id=' + id,
                                    success : function(data) {

                                          $(this).find('span').addClass(
                                                      "icon-success");
                                          var str1 = "published";
                                          var str2 = str1.concat(id);
                                          $('#' + str2).css({
                                                'display' : 'block'
                                          });
                                          //$('#' + str2).show();
                                          var str3 = "publish";
                                          var str4 = str3.concat(id);

                                          $('#' + str4).hide();

                                    }

                              });

            });*/
            
            
      }
		
			/* $(".showClass")
					.click(
							function() {
								console
										.log("called ........................................................000000.");
								//$(this).css('color', 'black');
								var testId = $(this).attr("id");

								var id = testId.substr(7, testId.length);
								console.log(id);
								$
										.ajax({
											type : 'GET',
											url : '${pageContext.request.contextPath}/publishOneIca?'
													+ 'id=' + id,
											success : function(data) {

												$(this).find('span').addClass(
														"icon-success");
												var str1 = "published";
												var str2 = str1.concat(id);
												$('#' + str2).css({
													'display' : 'block'
												});
												//$('#' + str2).show();
												var str3 = "publish";
												var str4 = str3.concat(id);

												$('#' + str4).hide();

											}

										});

							}); */
		</script>