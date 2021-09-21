<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>



<jsp:include page="../common/newDashboardHeader.jsp" />

<div class="d-flex" id="facultyAssignmentPage">

    <!-- <jsp:include page="../common/newAdminLeftNavBar.jsp" />-->
    <jsp:include page="../common/rightSidebarAdmin.jsp" />


    <!-- DASHBOARD BODY STARTS HERE -->

    <div class="container-fluid m-0 p-0 dashboardWraper">

        <jsp:include page="../common/newAdminTopHeader.jsp" />

        <!-- SEMESTER CONTENT -->
        <div class="col-lg-12 col-md-12 col-sm-12 dashboardBody p-5">

            <!-- page content: START -->
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
                    <li class="breadcrumb-item" aria-current="page">
                        <c:out value="${Program_Name}" />
                    </li>
                    <sec:authorize access="hasRole('ROLE_STUDENT')">
                        <c:out value="${AcadSession}" />
                    </sec:authorize>
                    <li class="breadcrumb-item active" aria-current="page">Publish
                        Term End Exam Evaluation</li>
                </ol>
            </nav>

            <jsp:include page="../common/alert.jsp" />

            <!-- Input Form Panel -->
            <div class="card bg-white border">
                <div class="card-body">
                    <h5 class="text-center pb-2 border-bottom">Publish Term End Exam Evaluation</h5>



                    <div class="table-responsive mt-3 mb-3 testAssignTable">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th scope="col">Sl. No.</th>
                                    <th scope="col">TEE Name</th>
                                    <th scope="col">Program Name</th>
                                    <th scope="col">Module Name</th>
                                    <th scope="col">Assign Faculty</th>
                                    <th scope="col">Acad Year</th>
                                    <th scope="col">Acad Session</th>
                                    <th scope="col">Publish Date</th>
                                    <th scope="col">Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="tee" items="${submittedTeeList}" varStatus="status">
                                    <tr>
                                        <td>
                                            <c:out value="${status.count}" />
                                        </td>
                                        <td>
                                            <c:out value="${tee.teeName}" />
                                        </td>
                                        <td>
                                            <c:out value="${tee.programName}" />
                                        </td>
                                        <c:if test="${tee.courseName ne null}">
                                            <td>
                                                <c:out value="${tee.moduleName}(${tee.courseName})" />
                                            </td>
                                        </c:if>
                                        <c:if test="${tee.courseName eq null}">
                                            <td>
                                                <c:out value="${tee.moduleName}" />
                                            </td>
                                        </c:if>
                                        <td>
                                            <c:out value="${tee.facultyName}" />
                                        </td>
                                        <td>
                                            <c:out value="${tee.acadYear}" />
                                        </td>
                                        <td>
                                            <c:out value="${tee.acadSession}" />
                                        </td>
                                        <td>
                                            <c:if test="${tee.isPublished eq 'Y'}"><c:out value="${tee.publishedDate}" /></c:if>
                                            <c:if test="${tee.isPublished ne 'Y'}"><c:out value="${currentDate}" /></c:if>
                                        </td>

                                        <td>
                                            <c:choose>
                                                <c:when test="${tee.isPublished eq 'Y'}">
                                                    TEE Published
                                                </c:when>
                                                <c:otherwise>
                                                    <a href="#" id="publish${tee.id}" class="showClass" onclick="publishTEE('${tee.id}')">
                                                        Publish </a>
                                                    <p id="published${tee.id}" class="showClass" style="float: center; display: none;">TEE Published</p>

                                                </c:otherwise>
                                            </c:choose>
                                        </td>



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
                            <button id="publishId" class="btn btn-large btn-primary mt-2" formaction="publishAllTee">Publish All</button>
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
            function publishTEE(teeId) {
                var id = teeId;
                console.log("id--->" + id);
                swal({
                        title: "Publish TEE?",

                        icon: "warning",
                        buttons: true,

                        dangerMode: true,
                    })
                    .then((willDelete) => {
                        if (willDelete) {

                            $
                                .ajax({
                                    type: 'GET',
                                    url: '${pageContext.request.contextPath}/publishOneTee?' +
                                        'id=' + id,
                                    success: function(data) {

                                        $(this).find('span').addClass(
                                            "icon-success");
                                        var str1 = "published";
                                        var str2 = str1.concat(id);
                                        $('#' + str2).css({
                                            'display': 'block'
                                        });
                                        //$('#' + str2).show();
                                        var str3 = "publish";
                                        var str4 = str3.concat(id);

                                        $('#' + str4).hide();

                                    }

                                });

                            swal("TEE Published Successfully", {
                                icon: "success",
                            });

                        } else {
                            //swal("Your imaginary file is safe!");
                        }
                    });
            }

        </script>
