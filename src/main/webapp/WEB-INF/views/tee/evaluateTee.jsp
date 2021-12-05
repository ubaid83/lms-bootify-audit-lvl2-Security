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

<head>
    <style>
        .osTable {
            overflow-x: scroll;
        }

        table.scroll {
            width: 100%;
            /* Optional */
            /* border-collapse: collapse; */
            border-spacing: 0;
            /*border: 2px solid black;*/
        }

        table.scroll tbody,
        table.scroll thead {
            display: block;
        }

        thead tr th {
            height: 30px;
            line-height: 30px;
            /*text-align: left;*/
        }

        table.scroll tbody {
            height: 500px;
            overflow-y: auto;
            overflow-x: hidden;
        }

        tbody {
            border-top: 2px solid black;
        }

        tbody td,
        thead th {
            width: 50%;
            /* Optional */
            border-right: 1px solid black;
        }

        tbody td:last-child,
        thead th:last-child {
            border-right: none;
        }

        /* LOADER CSS */
        .newLoaderWrap {
            height: 100vh;
            position: fixed;
            background: rgba(0, 0, 0, 0.40);
            ;
            top: 0;
            left: 0;
            width: 100%;
            z-index: 20000;
            display: none;
        }

        .newLoader {
            position: fixed;
            z-index: 20001;
            border: 8px solid #000000;
            border-top: 8px solid #ffffff;
            height: 120px;
            width: 120px;
            border-radius: 50%;
            top: calc(50% - 60px);
            left: calc(50% - 60px);
            -webkit-animation: spin 2s linear infinite;
            /* Safari */
            animation: spin 2s linear infinite;
        }

        /* Safari */
        @ -webkit-keyframes spin {
            0% {
                -webkit-transform: rotate(0deg);
            }

            100% {
                -webkit-transform:



                    rotate (360 deg);




            }
        }

        @ keyframes spin {
            0% {
                transform: rotate(0deg);
            }

            100% {
                transform:



                    rotate (360 deg);




            }
        }

    </style>
</head>
<div class="d-flex dataTableBottom" id="facultyAssignmentPage">

    <sec:authorize access="hasRole('ROLE_FACULTY')">
        <jsp:include page="../common/newLeftNavBarFaculty.jsp" />
        <jsp:include page="../common/newLeftSidebarFaculty.jsp" />
        <jsp:include page="../common/rightSidebarFaculty.jsp" />
    </sec:authorize>
    <sec:authorize access="hasRole('ROLE_ADMIN')">
        <jsp:include page="../common/newAdminLeftNavBar.jsp" />
        <jsp:include page="../common/rightSidebarAdmin.jsp" />
    </sec:authorize>

    <!-- DASHBOARD BODY STARTS HERE -->

    <div class="container-fluid m-0 p-0 dashboardWraper">
        <sec:authorize access="hasRole('ROLE_FACULTY')">
            <jsp:include page="../common/newTopHeaderFaculty.jsp" />
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <jsp:include page="../common/newAdminTopHeader.jsp" />
        </sec:authorize>

        <div class="container mt-5">
            <div class="row">
                <!-- LOADER HTML -->
                <div class="newLoaderWrap position-fixed">
                    <div class="newLoader"></div>
                </div>

                <!-- SEMESTER CONTENT -->
                <div class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">

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
                            <li class="breadcrumb-item active" aria-current="page">Evaluate
                                Term End Exam</li>
                        </ol>
                    </nav>

                    <jsp:include page="../common/alert.jsp" />


                    <!-- Input Form Panel -->
                    <div class="card bg-white border">
                        <div class="card-body">
                            <h5 class="text-center border-bottom pb-2 text-uppercase">
                                External Mark Assignment For <span>${moduleName}</span>
                            </h5>

                            <div class="row">

                                <div class="col-md-4 col-sm-6 mt-3">
                                    <strong>Acad year:</strong> <span>${tee.acadYear}</span>
                                </div>
                                <div class="col-md-4 col-sm-6 mt-3">
                                    <strong>Semester:</strong> <span>${acadSession}</span>
                                </div>
                                <div class="col-md-4 col-sm-6 mt-3">
                                    <strong>Program:</strong> <span>${programName}</span>
                                </div>
                                <div class="col-md-4 col-sm-6 mt-3">
                                    <strong>Module:</strong> <span>${moduleName}</span>
                                </div>

                                <%-- <div class="col-md-4 col-sm-6 mt-3">
                                    <strong>Start Date:</strong> <span>${tee.startDate}</span>
                                </div>
                                <div class="col-md-4 col-sm-6 mt-3">
                                    <strong>End Date:</strong> <span>${tee.endDate}</span>
                                </div> --%>
                                <div class="col-md-4 col-sm-6 mt-3">
                                    <strong>Total External Marks:</strong> <span>${tee.externalMarks}</span>
                                </div>
                                <div class="col-md-4 col-sm-6 mt-3">
                                    <strong>External Passing Marks:</strong> <span>${tee.externalPassMarks}</span>
                                </div>

                                <c:if test="${teeQuery eq 'true'}">
                                    <input type="hidden" id="icaQ" value='Y'>
                                </c:if>
                                <c:if test="${teeQuery ne 'true'}">
                                    <input type="hidden" id="icaQ" value='N'>
                                    <div class="col-md-4 col-sm-6 mt-3">
                                        <strong>How do you want to evaluate?</strong>
                                        <form>
                                            <input type="radio" name="opt" value="manual" id="manualRad" checked="checked"> Manual<br> <input type="radio" name="opt" value="excel" id="excelRad">
                                            Via Excel<br>

                                        </form>
                                    </div>
                                </c:if>
                            </div>

                        </div>
                    </div>


                    <div class="card bg-white border" id="manual">

                        <div class="card-body">
                            <form id="icaGrade" action="${pageContext.request.contextPath}/submitTee" method="post">

                                <div class="col-12 testAssignTable osTable">
                                    <table id="evaulateMarks" class="scroll mt-3 table-bordered text-center">

                                        <thead>
                                            <tr>
                                                <th>Sr. No</th>
                                                <th>Roll No.</th>
                                                <th>Name</th>
                                                <th>SAP ID</th>
                                                <th>Mark Absent?</th>


                                                <th>Total (Out of ${tee.externalMarks})</th>
                                                <c:if test="${tee.scaledReq eq 'Y'}">
                                                    <th><i class="fas fa-long-arrow-alt-down text-danger"></i>
                                                        Scale Marks (Out of ${tee.scaledMarks})
                                                </c:if>
                                                <c:if test="${teeQuery eq 'true'}">
                                                    <th>Query Raised</th>
                                                </c:if>
                                                <th>Remarks</th>
                                                <c:if test="${teeQuery eq 'true'}">
                                                    <th>Approve?</th>
                                                </c:if>
                                                <!-- <th rowspan="2">Action</th> -->
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <input type="hidden" id="externalM" value="${tee.externalMarks}">
                                            <input type="hidden" id="teeIdValue" name="teeIdValue" value="${tee.id}">
                                            <c:choose>
                                                <c:when test="${teeQuery eq 'true'}">
                                                    <c:choose>

                                                        <c:when test="${tee.scaledReq eq 'Y'}">
                                                            <input type="hidden" id="isScal" value="Y" />
                                                            <input type="hidden" id="scaledM" value="${tee.scaledMarks}" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="hidden" id="isScal" value="N" />
                                                        </c:otherwise>
                                                    </c:choose>

                                                    <c:forEach var="stud" varStatus="statusStud" items="${studentsListForIca}">


                                                        <c:if test="${'Y' eq stud.isQueryRaise}">
                                                            <input type="hidden" id="studUnameTee" name="${stud.username}-Tee" />
                                                            <tr class="tr${statusStud.count}">

                                                                <td>${statusStud.index+1}</td>
                                                                <td>${stud.rollNo}</td>
                                                                <td>${stud.studentName}</td>
                                                                <td>${stud.username}</td>
                                                                <!-- Query raise is absent -->
                                                                <%-- <td style="min-width: 60px;"><input type="hidden" name="${stud.username}isAbsent" value="${stud.isAbsent}" id="isAbsentY" />
                                                                    ${stud.isAbsent} </td> --%>
																<td style="min-width: 60px;" class="isAbsent">
		                                                            <c:if test="${stud.isAbsent eq 'Y'}">
		                                                                <input type="radio" name="${stud.username}isAbsent" value="Y" id="isAbsentY" checked="checked" onclick="disableTr('tr${statusStud.count}');" class="absentStatus">
		                                                                Yes<br />
		                                                                <input type="radio" name="${stud.username}isAbsent" value="N" id="isAbsentN" style="margin-right: 4px;" onclick="enableTr('tr${statusStud.count}');" class="absentStatus">No
		                                                            </c:if>
		                                                            <c:if test="${stud.isAbsent ne 'Y'}">
		                                                                <input type="radio" name="${stud.username}isAbsent" value="Y" id="isAbsentY" onclick="disableTr('tr${statusStud.count}');" class="absentStatus">
		                                                                Yes<br />
		                                                                <input type="radio" name="${stud.username}isAbsent" value="N" checked="checked" id="isAbsentN" style="margin-right: 4px;" onclick="enableTr('tr${statusStud.count}');" class="absentStatus">No
		                                                            </c:if>
		                                                        </td>
																<!-- Query raise is absent -->
                                                                <td>
                                                                    <c:set var="keyTotal" value="${stud.username}totalM" />
                                                                    <c:set var="keyRemark" value="${stud.username}remark" />
                                                                    <c:set var="keyQuery" value="${stud.username}query" />
                                                                    <input class="text-center totalMarks marks" type="text" placeholder="Total Marks" id="total${stud.username}" name="total${stud.username}" value="${stud.teeTotalMarks}"></td>
                                                                <c:if test="${tee.scaledReq eq 'Y'}">
                                                                    <c:set var="keyScale" value="${stud.username}scaleM" />
                                                                    <td><input class="text-center marks" type="text" placeholder="Scaled Marks" readonly="readonly" id="scale${stud.username}" name="scale${stud.username}" value="${stud.teeScaledMarks}"></td>
                                                                </c:if>
                                                                <c:if test="${teeQuery eq 'true'}">
                                                                    <td><textarea disabled="disabled" name="query${stud.username}">${stud.query}
														</textarea></td>
                                                                </c:if>
                                                                <td><textarea class="remarkTextArea" placeholder="Enter remark" name="remark${stud.username}">${stud.remarks}</textarea></td>
                                                                <td style="min-width: 60px;" class="isApproved">
                                                                    <c:if test="${stud.isQueryApproved eq 'Y'}">
                                                                        <input type="radio" name="${stud.username}isApproved" value="Y" id="isApprovedY" checked="checked" onclick="approvedQuery('tr${statusStud.count}');" class="loadApprovalStatus">Yes<br />
                                                                        <input type="radio" name="${stud.username}isApproved" value="N" id="isApprovedN" style="margin-right: 4px;" onclick="rejectQuery('tr${statusStud.count}');" class="loadApprovalStatus">No
                                                                    </c:if>
                                                                    <c:if test="${empty stud.isQueryApproved}">
                                                                        <input type="radio" name="${stud.username}isApproved" value="Y" id="isApprovedY" checked="checked" onclick="approvedQuery('tr${statusStud.count}');" class="loadApprovalStatus">
                                                                        Yes<br />
                                                                        <input type="radio" name="${stud.username}isApproved" value="N" id="isApprovedN" style="margin-right: 4px;" onclick="rejectQuery('tr${statusStud.count}');" class="loadApprovalStatus">No
                                                                    </c:if>
                                                                    <c:if test="${stud.isQueryApproved eq 'N'}">
                                                                        <input type="radio" name="${stud.username}isApproved" value="Y" id="isApprovedY" onclick="approvedQuery('tr${statusStud.count}');" class="loadApprovalStatus">
                                                                        Yes<br />
                                                                        <input type="radio" name="${stud.username}isApproved" value="N" checked="checked" id="isApprovedN" style="margin-right: 4px;" onclick="rejectQuery('tr${statusStud.count}');" class="loadApprovalStatus">No
                                                                    </c:if>
                                                                </td>

                                                            </tr>
                                                        </c:if>
                                                    </c:forEach>
                                                </c:when>
                                                <c:otherwise>


                                                    <c:choose>

                                                        <c:when test="${tee.scaledReq eq 'Y'}">
                                                            <input type="hidden" id="isScal" value="Y" />
                                                            <input type="hidden" id="scaledM" value="${tee.scaledMarks}" />
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="hidden" id="isScal" value="N" />
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <c:forEach var="stud" varStatus="statusStud" items="${studentsListForIca}">
                                                        <input type="hidden" id="studUnameTee" name="${stud.username}-Tee" />
                                                        <c:if test="${stud.isAbsent eq 'Y'}">
                                                            <tr class="readOnlyClass">
                                                        </c:if>
                                                        <c:if test="${stud.isAbsent ne 'Y'}">
                                                            <tr class="tr${statusStud.count}">
                                                        </c:if>


                                                        <td>${statusStud.index+1}</td>
                                                        <td>${stud.rollNo}</td>
                                                        <td>${stud.studentName}</td>
                                                        <td>${stud.username}</td>
                                                        <td style="min-width: 60px;">
                                                            <c:if test="${stud.isAbsent eq 'Y'}">
                                                                <input type="radio" name="${stud.username}isAbsent" value="Y" id="isAbsentY" checked="checked" onclick="disableTr('readOnlyClass');" class="absentStatus">
                                                                Yes<br />
                                                                <input type="radio" name="${stud.username}isAbsent" value="N" id="isAbsentN" style="margin-right: 4px;" onclick="enableTr('readOnlyClass');" class="absentStatus">No
                                                            </c:if>
                                                            <c:if test="${stud.isAbsent ne 'Y'}">
                                                                <input type="radio" name="${stud.username}isAbsent" value="Y" id="isAbsentY" onclick="disableTr('tr${statusStud.count}');" class="absentStatus">
                                                                Yes<br />
                                                                <input type="radio" name="${stud.username}isAbsent" value="N" checked="checked" id="isAbsentN" style="margin-right: 4px;" onclick="enableTr('tr${statusStud.count}');" class="absentStatus">No
                                                            </c:if>
                                                        </td>

                                                        <td>
                                                            <c:set var="keyTotal" value="${stud.username}totalM" />
                                                            <c:set var="keyRemark" value="${stud.username}remark" />
                                                            <input class="text-center totalMarks marks" type="text" placeholder="Total Marks" id="total${stud.username}" name="total${stud.username}" value="${stud.teeTotalMarks}"></td>
                                                        <c:if test="${tee.scaledReq eq 'Y'}">
                                                            <c:set var="keyScale" value="${stud.username}scaleM" />
                                                            <td><input class="text-center marks" type="text" placeholder="Scaled Marks" readonly="readonly" id="scale${stud.username}" name="scale${stud.username}" value="${stud.teeScaledMarks}"></td>
                                                        </c:if>
                                                        <td><textarea class="remarkTextArea" placeholder="Enter remark" name="remark${stud.username}">${stud.remarks}</textarea></td>

                                                        </tr>
                                                    </c:forEach>

                                                </c:otherwise>
                                            </c:choose>
                                        </tbody>
                                    </table>
                                </div>


                                

                                <div class="col-12 mt-3">
                                    <!-- 	<button class="btn btn-secondary mt-2">Save Changes As
										Draft</button>
									<button class="btn btn-dark mt-2">Submit</button> -->
                                    <c:if test="${teeQuery ne 'true'}">
                                        <input type="button" id="saveas" class="btn btn-secondary mt-2" value="Save as Draft" />
                                    </c:if>
                                    <input type="button" id="genId" value="Submit" class="btn btn-dark mt-2" /> <a id="dlink" style="display: none;"></a>

                                </div>
                            </form>


                        </div>
                    </div>

                    <div class="card bg-white border" id="excel">
                        <div class="card-body">
                            <h5 class="text-center pb-2 border-bottom">Upload Student
                                TEE Marks Via Excel</h5>
                            <form:form id="uploadIcaFromExcel" action="uploadStudentMarksExcel" method="post" enctype="multipart/form-data">
                                <div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">
                                    <input id="file" name="file" type="file" class="form-control" required="required" />
                                </div>
                                <div class="col-lg-4 col-md-4 col-sm-12 mt-3 position-relative">

                                    <button id="submit" class="btn btn-secondary mt-2" formaction="uploadStudentMarksExcelTee?saveAs=draft&teeId=${tee.id}" onclick="return confirm('Are you sure you want to save?')">Save
                                        As Draft</button>
                                    <button id="submit" class="btn btn-dark mt-2" formaction="uploadStudentMarksExcelTee?saveAs=submit&teeId=${tee.id}" onclick="return confirm('Are you sure? once submitted you cannot edit marks')">Final
                                        Submit</button>


                                </div>
                                <div class="col-12 mt-3 position-relative text-left">
                                    <h6>Excel Format:</h6>
                                    <a class="text-danger" href="downloadTeeMarksUploadTemplate?teeId=${tee.id}">Download
                                        Template</a>
                                </div>
                            </form:form>
                        </div>
                    </div>

                    <!-- Results Panel -->




                    <%-- 	<jsp:include page="../common/paginate.jsp">
							<jsp:param name="baseUrl" value="searchFacultyGroups" />
						</jsp:include> --%>

                    <!-- /page content: END -->

                </div>
                <sec:authorize access="hasRole('ROLE_FACULTY')">
                    <!-- SIDEBAR START -->
                    <jsp:include page="../common/newSidebar.jsp" />
                    <!-- SIDEBAR END -->
                    <jsp:include page="../common/footer.jsp" />
                </sec:authorize>
                <sec:authorize access="hasRole('ROLE_ADMIN')">
                    <jsp:include page="../common/newAdminFooter.jsp" />
                </sec:authorize>



                <!-- <script type="text/javascript"
	src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script> -->

                <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>

                <script>
                    // Change the selector if needed
                    var $table = $('table.scroll'),
                        $bodyCells = $table.find(
                            'tbody tr:first').children(),
                        colWidth;

                    // Adjust the width of thead cells when window resizes
                    $(window).resize(function() {
                        // Get the tbody columns width array
                        colWidth = $bodyCells.map(function() {
                            return $(this).width();
                        }).get();

                        // Set the width of thead columns
                        $table.find('thead tr').children().each(function(i, v) {
                            $(v).width(colWidth[i]);
                        });
                    }).resize(); // Trigger resize handler

                </script>

                <script>
                    $(".totalMarks")
                        .change(
                            function() {

                                //alert("On change called")

                                var idTotalM = $(this).attr("id");
                                console.log('idTotalM' + idTotalM)
                                var splitId = idTotalM.split('total');

                                var total = $(this).val();
                                var scaledTotal = 0;

                                var oldValue = $('#total' + splitId[1])
                                    .val();

                                var externalM = $('#externalM').val();

                                if (!isNumber($(this).val(), externalM)) {

                                    $(this).val(oldValue);

                                    alert('mark entered is not in range');
                                    return;
                                }
                                //alert("idOfCriteria ------ "+idOfCriteria)



                                if ($('#isScal').val() == 'Y') {

                                    var scaledMark = $('#scaledM')
                                        .val();
                                    multiplyValue = multiply(total,
                                        scaledMark);
                                    scaledTotal = multiplyValue /
                                        $('#externalM').val();
                                    console.log('scaledMark-->' +
                                        scaledMark);
                                    console.log('multiplyValue-->' +
                                        multiplyValue);
                                    console.log('scaledTotal-->' +
                                        scaledTotal);
                                    $('#scale' + splitId[1]).val(
                                        scaledTotal);
                                }

                                //var multiplyValue =  total*

                            });

                </script>
                <script>
                    function isNumber(n, maxMarks) {
                        if (!isNaN(parseFloat(n)) && isFinite(n)) {
                            var value = parseFloat(n);
                            if (value >= 0 && value <= maxMarks) {
                                return true;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    }

                    function multiply(x, y) {
                        var one = parseFloat(x);
                        var two = parseFloat(y);
                        return one * two;
                    }

                    function performtotalCalc(sapId) {
                        var total = 0;

                        $('[id^=' + sapId + ']').each(function() {
                            if ($(this).val() != '') {

                                total = total + parseFloat($(this).val());
                            }

                        });

                        return parseFloat(total);
                    }

                </script>

                <script>
                    $("#genId")
                        .click(
                            function() {

                                /* 		var cf = confirm("Warning! Ensure grading of all students has been done?");
                                		if (cf == true) {
                                			$("#grade").submit();
                                		} else {

                                		} */
                                var errCount = 0;
                                $('.totalMarks').each(function() {
                                    if ($(this).val() == '') {
                                        errCount++;
                                    }
                                });

                                if (errCount > 0) {
                                    swal({
                                        title: 'cannot submit, grading of all student has not been done.',
                                        // text: "It will permanently deleted !",
                                        type: 'warning',
                                        //icon : 'success',
                                        showCancelButton: true,
                                        confirmButtonColor: '#3085d6',
                                        cancelButtonColor: '#d33',
                                        // confirmButtonText: 'Yes, delete it!'
                                    });
                                } else {
                                    swal({
                                        title: 'Are you sure you want to submit?',
                                        // text: "It will permanently deleted !",
                                        //type: 'warning',
                                        icon: 'success',
                                        showCancelButton: true,
                                        confirmButtonColor: '#3085d6',
                                        cancelButtonColor: '#d33',
                                        // confirmButtonText: 'Yes, delete it!'
                                    }).then(function() {
                                        $("#icaGrade").submit();

                                    });
                                }

                            });

                    $("#saveas")
                        .click(
                            function() {
                                $(".newLoaderWrap").css('display',
                                    'block');
                                //alert("here");
                                var datastring = $("#icaGrade")
                                    .serialize();
                                $
                                    .ajax({
                                        type: "POST",
                                        url: "${pageContext.request.contextPath}/saveTeeAsDraft",
                                        data: datastring,
                                        success: function(data) {
                                            $(".newLoaderWrap")
                                                .css('display',
                                                    'none');
                                            console
                                                .log('data received is' +
                                                    data)
                                            if (data == 'saved') {
                                                swal(
                                                        'data saved successfully')
                                                    .then(
                                                        function() {
                                                            document.location
                                                                .reload();
                                                        });
                                            } 
                                        },
                                        error: function() {
                                            alert('Error here');
                                        }
                                    });
                            });

                </script>

                <script>
                    $(function() {
                        $('table tr.readOnlyClass')
                            .each(
                                function() {
                                    $(this).css('background-color',
                                        '#e8dddd');

                                    $(this)
                                        .find('td')
                                        .each(
                                            function() {

                                                console
                                                    .log('td found---');
                                                $(this)
                                                    .find(
                                                        'input,textarea')
                                                    .attr(
                                                        "readonly",
                                                        true);
                                                $(this)
                                                    .find(
                                                        'input,textarea')
                                                    .css(
                                                        'background-color',
                                                        '#e8dddd');
                                            });
                                });
                    });

                </script>
                <script>
                    $(document)
                        .ready(
                            function() {

                                var icaQ = $('#icaQ').val();

                                if (icaQ == 'Y') {
                                    $('#excel').hide();
                                } else {

                                    $('#manual').show();
                                    $('#excel').hide();
                                    $('#manualRad').click(function() {
                                        $('#manual').show();
                                        $('#excel').hide();
                                    });

                                    $('#excelRad').click(function() {
                                        $('#excel').show();
                                        $('#manual').hide();
                                    });
                                }
                                //var rowCount = $('table tr').length - 1;
                                $('#evaulateMarks tbody tr')
                                    .each(
                                        function(index) {
                                            console.log("TR being looped")
                                            //alert('${icaQuery}');
                                            if ('${teeQuery}' == 'true') {
                                                var isApproved = $( this) .find(".isApproved input[type=radio]:checked").val();
                                                var isAbsent = $(this).find(".isAbsent input[type=radio]:checked").val();
                                                //alert(index+" "+isApproved);
                                                 if (isApproved != undefined) {
                                                    //alert(index+" "+isApproved);
                                                    $(this).css('background-color','#e8dddd');
                                                    $(this).find('td').each(
                                                            function() {
                                                            	//Query raise is absent
                                                            	if(isAbsent == 'Y'){
                                                            		$(this).find('input,textarea').attr("readonly", true);
                                                                    $(this).find('.remarkTextArea').attr("readonly",false);
                                                                    $(this).find('.remarkTextArea').attr('required','required');
                                                                   	//$(this).find('input').val('0');
                                                                    //$(this).find('input[type=radio].absentStatus').val('N');
                                                                   	//$(this).find('textarea').val('absent');
                                                                    $(this).find('input,textarea').css('background-color','#e8dddd');
                                                                    $(this).find('.remarkTextArea').css('background-color','#ffffff');
                                                            	}
                                                            	//Query raise is absent
                                                                //console.log('td found---',isApproved);
                                                                if (isApproved == 'N') {
                                                                	console.log('td found---N');
                                                                    $(this).find('input,textarea').attr("readonly", true);
                                                                    $(this).find('.remarkTextArea').attr("readonly",false);
                                                                    $(this).find('.remarkTextArea').attr('required','required');
                                                                    //$(this).find('input').val('0');
                                                                    $(this).find('input[type=radio].loadApprovalStatus').val('N');
                                                                    //$(this).find('textarea').val('absent');
                                                                    $(this).find('input,textarea').css('background-color','#e8dddd');
                                                                    $(this).find('.remarkTextArea').css('background-color','#ffffff');
                                                                } else if (isApproved == 'Y') {
                                                                	console.log('td found---Y');
                                                                    $(this).css('background-color','#ffffff');
                                                                    $(this).find('td').each(function() {
                                                        				//console.log('td found---Y');
                                                                        $(this).find('input,textarea').attr("readonly",false);
                                                                        $(this).find('input,textarea').css('background-color','#ffffff');
                                                                        $(this).find('input[type=radio].loadApprovalStatus').val('Y');
                                                                        $(this).find('.remarkTextArea').removeAttr('required');
                                                                    });
                                                                }
                                                            });
                                                }
                                            }
                                        });

                            });

                    /* 	$("#genId").click(function() {
                    		swal({
                    			title : 'Are you sure you want to submit?',
                    			// text: "It will permanently deleted !",
                    			//type: 'warning',
                    			icon : 'success',
                    			showCancelButton : true,
                    			confirmButtonColor : '#3085d6',
                    			cancelButtonColor : '#d33',
                    		// confirmButtonText: 'Yes, delete it!'
                    		}).then(function() {
                    			$("#excelsubmit").submit();

                    		});
                    	}); */

                    function disableTr(trClass) {
                        console.log('function called');
                        $('table tr.' + trClass)
                            .each(
                                function() {
                                    $(this).css('background-color',
                                        '#e8dddd');
                                    $(this)
                                        .find('td')
                                        .each(
                                            function() {
                                                console
                                                    .log('td found---');
                                                $(this)
                                                    .find(
                                                        'input,textarea')
                                                    .attr(
                                                        "readonly",
                                                        true);
                                                $(this)
                                                    .find(
                                                        'input.marks')
                                                    .val(
                                                        '0');
                                                $(this)
                                                    .find(
                                                        'input[type=radio].absentStatus')
                                                    .val(
                                                        'Y');
                                                $(this)
                                                    .find(
                                                        'textarea.remarkTextArea')
                                                    .val(
                                                        'absent');
                                                $(this)
                                                    .find(
                                                        'input,textarea')
                                                    .css(
                                                        'background-color',
                                                        '#e8dddd');
                                            });
                                });

                    }

                    function rejectQuery(trClass) {
                        console.log('function called');
                        $('table tr.' + trClass)
                            .each(
                                function() {
                                    $(this).css('background-color',
                                        '#e8dddd');
                                    $(this)
                                        .find('td')
                                        .each(
                                            function() {
                                                console
                                                    .log('td found---');
                                                $(this)
                                                    .find(
                                                        'input,textarea')
                                                    .attr(
                                                        "readonly",
                                                        true);
                                                $(this)
                                                    .find(
                                                        '.remarkTextArea')
                                                    .attr(
                                                        "readonly",
                                                        false);
                                                $(this)
                                                    .find(
                                                        '.remarkTextArea')
                                                    .attr(
                                                        'required',
                                                        'required');
                                                //$(this).find('input').val('0');
                                                $(this)
                                                    .find(
                                                        'input[type=radio].loadApprovalStatus')
                                                    .val(
                                                        'N');
                                                //$(this).find('textarea').val('absent');
                                                $(this)
                                                    .find(
                                                        'input,textarea')
                                                    .css(
                                                        'background-color',
                                                        '#e8dddd');
                                                $(this)
                                                    .find(
                                                        '.remarkTextArea')
                                                    .css(
                                                        'background-color',
                                                        '#ffffff');
                                            });
                                });

                    }

                    function approvedQuery(trClass) {
                        console.log('enable function called' + trClass);
                        $('table tr.' + trClass)
                            .each(
                                function() {
                                    $(this).css('background-color',
                                        '#ffffff');
                                    var isAbsent = $(this).find(".isAbsent input[type=radio]:checked").val();
                                    $(this)
                                        .find('td')
                                        .each(
                                            function() {
                                                console
                                                    .log('td found---');
                                                if(isAbsent == 'Y'){
                                                	$(this).find('input[type=radio].loadApprovalStatus').val('Y');
                                                }else{
	                                                $(this).find('input,textarea').attr("readonly",false);
	                                                $(this).find('input,textarea').css('background-color','#ffffff');
	                                                $(this).find('input[type=radio].loadApprovalStatus').val('Y');
	                                                $(this).find('.remarkTextArea').removeAttr('required');
                                                }
                                            });
                                });
                    }

                    function enableTr(trClass) {
                        console.log('enable function called' + trClass);
                        $('table tr.' + trClass)
                            .each(
                                function() {

                                    $(this).css('background-color',
                                        '#ffffff');
                                    var isApproved = $(this).find(".isApproved input[type=radio]:checked").val();
                                    $(this).find('td').each(
                                        function() {
                                        	if(isApproved == 'Y'){
	                                            $(this).find('input,textarea').attr("readonly",false);
	                                            $(this).find('input,textarea').css('background-color','#ffffff');
	                                            $(this).find('input[type=radio].absentStatus').val('N');
                                        	}else{
                                        		$(this).find('input[type=radio].absentStatus').val('N');
                                        	}
                                        });
                                });
                    }

                </script>
