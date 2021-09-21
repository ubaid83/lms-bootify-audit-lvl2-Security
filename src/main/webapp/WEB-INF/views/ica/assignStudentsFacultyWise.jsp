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
					<li class="breadcrumb-item active" aria-current="page">Assign Students
						Faculty Wise</li>
				</ol>
			</nav>

			<jsp:include page="../common/alert.jsp" />

			<!-- Input Form Panel -->
			<div class="card bg-white border">
				<div class="card-body">
					<h5 class="text-center pb-2 border-bottom">Assign Students
						Faculty Wise
					</h5>

					<div class="col-12">
						<h5>Evaluation Details:</h5>
						<hr />
					</div>

					<div class="col-12 mt-3">
						<strong>ICA Name:</strong> <span>${ica.icaName}</span>
					</div>
					<div class="col-12 mt-3">
						<strong>Program:</strong> <span>${programName}</span>
					</div>
					<div class="col-12 mt-3">
						<strong>Subject:</strong> <span>${moduleName}</span>
					</div>
					<div class="col-12 mt-3">
						<strong>Year:</strong> <span>${ica.acadYear}</span>
					</div>
					<div class="col-12 mt-3">
						<strong>Acad Session:</strong> <span>${ica.acadSession}</span>
					</div>
					<div class="col-12 mt-3">
						<strong>Internal Marks:</strong> <span>${ica.internalMarks}</span>
					</div>
					<div class="col-12 mt-3">
						<strong>Internal Passing Marks:</strong> <span>${ica.internalPassMarks}</span>
					</div>
				</div>
			</div>


			<div class="card bg-white border">
				<div class="card-body">

					<%-- <form:form action="saveStudentsBatchWiseForIca" id="saveStudentsBatchWiseForIca"
						method="post" modelAttribute="ica">
						<form:input path="id"
														type="hidden" id="idIca" value="${ica.id}" /> --%>
						<input name="id"
														type="hidden" id="idIca" value="${ica.id}" />
						<!--FORM ITEMS STARTS HERE-->
						<!--FORM ITEMS ENDS HERE-->
						

    <div class="container-fluid">
        <div class="container">
            <div class="row">
                <div class="col-md-5 mt-3 border-right">
                    <h5 class="m-2">Student List</h5>

                    <div class="table-responsive" style="height: 500px; overflow-y: scroll">
                        <table id="studentList" class="table-bordered table-striped table-dragable" tabindex="0">
                            <thead class="bg-dark text-white">
                                <tr>
                                    <th>Sl. No. <span class="ml-2"><i class="fas fa-long-arrow-alt-down"></i><i class="fas fa-long-arrow-alt-up"></i></span></th>
                                    <th>Name <span class="ml-2"><i class="fas fa-long-arrow-alt-down"></i><i class="fas fa-long-arrow-alt-up"></i></span></th>
                                    <th>SAP ID <span class="ml-2"><i class="fas fa-long-arrow-alt-down"></i><i class="fas fa-long-arrow-alt-up"></i></span></th>
                                    <th>Roll No. <span class="ml-2"><i class="fas fa-long-arrow-alt-down"></i><i class="fas fa-long-arrow-alt-up"></i></span></th>
                                </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="students" items="${studentList}" varStatus="status">
                                <tr>
                                    <td><c:out value="${status.count}"/></td>
                                    <td><c:out value="${students.studentName}"/></td>
                                    <td><c:out value="${students.username}"/></td>
                                    <td><c:out value="${students.rollNo}"/></td>
                                </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="col-md-2 mt-3 border-left border-right position-relative">
                    <div class="moveButtons text-center">
                        <button id="moveToFaculty">
                            <i class="fas fa-long-arrow-alt-right"></i>
                        </button><br>
                        <button id="moveFromFaculty" class="mt-1">
                            <i class="fas fa-long-arrow-alt-left"></i>
                        </button><br>
                        <button id="moveAllStudents" class="mt-1">
                            <i class="fas fa-angle-double-right"></i>
                        </button><br>
                        <button id="moveAllStudentsFromFaculty" class="mt-1">
                            <i class="fas fa-angle-double-left"></i>
                        </button>
                    </div>
                </div>
                <div class="col-md-5 mt-3 border-left toDivision">
                    <select id="toFaculty" class="w-100 mb-3">
                        <option disabled selected>--Select Faculty--</option>
                         <c:forEach var="faculty" items="${facultyList}" varStatus="status">
                         <option value="${faculty.username}">${faculty.facultyName} - ${(faculty.username)}</option>
                         </c:forEach>
                        
                    </select>
                    <c:forEach var="faculty" items="${facultyList}" varStatus="status">
                    	<div id="outer${faculty.username}" class="table-responsive d-none" style="height: 500px; overflow-y: scroll">
                        <table id="${faculty.username}" class="table-bordered table-striped table-dragable " tabindex="0">
                            <thead class="bg-dark text-white">
                                <tr>
                                    <th>Sl. No.</th>
                                    <th>Name</th>
                                    <th>SAP ID</th>
                                    <th>Roll NO.</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                    </c:forEach>
                </div>
            </div>

        </div>
    </div>
    
    		<div class="col-12 mt-4">
							<button id="saveStudentsBatchWiseForIca"
								class="btn btn-large btn-primary mt-2" onclick="submitStudents()">Add Students Faculty Wise</button>
						</div>
					<%-- </form:form> --%>




				</div>
			</div>
			<!-- /page content: END -->

		</div>

		<!-- SIDEBAR START -->

		<!-- SIDEBAR END -->
		<jsp:include page="../common/newAdminFooter.jsp" />
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-json/2.6.0/jquery.json.min.js"></script>
		<script type="text/javascript">
		function submitStudents() {
			var id = $('#idIca').val();
			var TableData = new Array();
			var count = 0;
			<c:forEach var="faculty" items="${facultyList}" varStatus="status">
			$('#${faculty.username} tbody').each(function(){
			      if($('#${faculty.username} tbody tr').length == 0){
			    	count ++;
			      }
			});
			</c:forEach>
			if(count > 0){
				alert('Assign Students to all Faculties');
			}else{
			
			if($("#studentList tbody tr").length == 0){
			
			<c:forEach var="faculty" items="${facultyList}" varStatus="status">
			
            $('#${faculty.username} tbody tr').each(function(row, tr){
            	console.log('row--->'+row);
            	var facultyId = ${faculty.username};
            	console.log('facultyId--->'+facultyId);
            	var TableDataTemp ={ 
            	'icaId' : id,
            	'facultyId' : '${faculty.username}',		
    			'username' :$(tr).find('td:eq(2)').text() // value
        	/* + '\n'; */
    			}
            	TableData.push(TableDataTemp);
			});
            /* TableData.shift(); */
           
            
            console.log('TableData--->'+TableData);
           	</c:forEach>
           	
           var json = $.toJSON(TableData);
           console.log('json--->'+json);
           
           
           $
			.ajax({
				type : 'POST',
				dataType: 'JSON',
				url : '${pageContext.request.contextPath}/saveStudentsBatchWiseForIca',
				data: {
					json: json
              },
				success : function(data) {
					if(data.status == 'success'){
						console.log("sucess");
						console.log('returnData--->'+data);

						/* var returnJson = JSON.stringify(data);
						console.log('return---->'+returnJson); */
						
						window.location = '${pageContext.request.contextPath}/addIcaComponentsForm?id='
							+ encodeURIComponent(data.id);
						return false;
					}else if(data.status == 'blank'){
						alert('Select the Students');
					}
					
				}
			});
			} else{
				alert('Assign all the students');
			}
			}
		}
		</script>
		
		<script>
		//ica student faculty assign
		$(document).ready(function () {
			
		    var isDragging = false;
		    var isMouseDown = false;
		    var draggableFocus = false;
		    var ctrlIsPressed = false;
		    var selectedTable = "#studentList";
		    var prevSelectedTable = "#studentList";
		    var toDivTables = $(".toDivision .table-responsive")
		    var selectedToTable;
		    var selToFaculty = $("#toFaculty");
		    var currentDragTable, dragTable;


		        selToFaculty.change(function() {
		            toDivTables.addClass("d-none");
		            selectedToTable = "#outer" + $(this).val();
		            console.log(selectedToTable);
		            $(selectedToTable).removeClass("d-none")
		            prevSelectedTable = selectedTable;
		            selectedTable = "#" + $(selectedToTable + " table").attr("id");

		            if (selectedTable !== prevSelectedTable) {
		                funcSelectableTr();
		                console.log("Pre is not equl to curr")
		                console.log('current selected', selectedTable)
		                console.log('previous selected', prevSelectedTable)
		            }
		            console.log(selectedTable)
		        })

		        $(".table-dragable").hover(function() {
		            console.log("Hovered In");
		            draggableFocus = true;
		        }, function() {
		            console.log("Hovered Out")
		            draggableFocus = false;
		        })

		        $(".table-dragable").click(function() {
		            selectedTable = "#" + $(this).attr("id");
		            console.log("Selected table name is: ", selectedTable)
		        })


		        $(".table-dragable").mousedown(function() {
		            isMouseDown = true;

		        });
		        $(".table-dragable").mouseup(function() {
		            isMouseDown = false;
		        });


		        function funcSelectableTr(selectedTable) {
		            $.each($("" + selectedTable + " tbody tr"), function() {
		                console.log("Dynamic each function------------>", selectedTable);
		                let $this = $(this);
		                $this.addClass("is-selectable");

		                $(this).mousedown(function() {
		                    isDragging = false;
		                    isMouseDown = true;
		                    console.log(isMouseDown)

		                }).
		                mouseup(function() {
		                        isMouseDown = false;
		                    })
		                    .mousemove(function() {
		                        isDragging = true;


		                        if (isDragging === true && isMouseDown === true) {
		                            if ($this.hasClass("is-selected") === false) {
		                                $this.addClass("is-selected");
		                                console.log("Draging");
		                            }
		                        }

		                    })
		                    .mouseup(function() {
		                        isDragging = false;
		                        isMouseDown = false;
		                    });
		            });
		        }

		        funcSelectableTr(selectedTable);


		        $(".table-dragable").on('click', '.is-selectable', function() {
		            console.log("Dynamic working")
		            let $this = $(this);

		            if (ctrlIsPressed === true) {
		                if ($this.hasClass("is-selected") === false) {
		                    $(this).addClass("is-selected")
		                } else {
		                    $(this).removeClass("is-selected")
		                }
		            } else {
		                $(".is-selectable").removeClass("is-selected");
		                $(this).addClass("is-selected")
		            }
		        });


		        //SELECT ALL FUNCTION Ctrl + A
		        $(document).keydown(function(e) {

		        })

		        function functionMoveToFaculty() {
		            if (selectedToTable !== undefined) {
		                let movableTr = $("#studentList tbody .is-selected").removeClass("is-selected").remove().clone();
		                $("" + selectedToTable + " tbody").append(movableTr);
		                funcSelectableTr(selectedTable);

		            } else {
		                alert("Please select a faculty from faculty list.");
		            }
		        }

		        function functionMoveFromFaculty() {
		            let movableTr = $("" + selectedToTable + " tbody .is-selected").removeClass("is-selected").remove().clone();
		            $("#studentList tbody").append(movableTr);
		            funcSelectableTr("#studentList");
		        }


		        $(document).keyup(function(e) {
		            if (e.which == 17) {
		                ctrlIsPressed = false;
		                console.log(ctrlIsPressed)
		            }
		        })

		        $(document).keydown(function(e) {



		            if (draggableFocus == true) {
		                $("body").css({
		                    "-webkit-touch-callout": "none",
		                    "-webkit-user-select": "none",
		                    "-khtml-user-select": "none",
		                    "-moz-user-select": "none",
		                    "-ms-user-select": "none",
		                    "user-select": "none"
		                })
		            } else {
		                $("body").css({
		                    "-webkit-touch-callout": "unset",
		                    "-webkit-user-select": "unset",
		                    "-khtml-user-select": "unset",
		                    "-moz-user-select": "unset",
		                    "-ms-user-select": "unset",
		                    "user-select": "unset"
		                })
		            }

		            if (e.which == 17) {
		                ctrlIsPressed = true;
		                console.log(ctrlIsPressed)
		            }


		            if (e.which == 65 && ctrlIsPressed === true && draggableFocus === true) {

		                $("" + selectedTable + " tbody tr").addClass("is-selected");
		                console.log("Select All")
		            }

		            if (e.which == 13) {
		                functionMoveToFaculty();
		            }

		            if (e.which == 46) {
		                functionMoveFromFaculty();
		            }
		        })

		        function moveAllStudentsToFaculty() {
		            if (selectedToTable !== undefined) {
		                let movableTr = $("#studentList tbody tr").removeClass("is-selected").remove().clone();
		                $("" + selectedToTable + " tbody").append(movableTr);
		                funcSelectableTr(selectedTable);
		            } else {
		                alert("Please select a faculty from faculty list.");
		            }
		        }

		        function moveAllStudentsFromFaculty() {
		            let movableTr = $("" + selectedTable + " tbody tr").removeClass("is-selected").remove().clone();
		            $("#studentList tbody").append(movableTr);
		            funcSelectableTr("#studentList");
		        }


		        $("#moveToFaculty").click(function() {
		            selectedTable = "#" + selToFaculty.val();
		            console.log("SELECTED TABLE---->", selectedTable)
		            functionMoveToFaculty();
		        })

		        $("#moveFromFaculty").click(function() {
		            selectedTable = "#" + selToFaculty.val();
		            console.log("SELECTED TABLE---->", selectedTable)
		            functionMoveFromFaculty();
		        })
		        $("#moveAllStudents").click(function() {
		            selectedTable = "#" + selToFaculty.val();
		            console.log("SELECTED TABLE---->", selectedTable)
		            moveAllStudentsToFaculty();
		        })
		        $("#moveAllStudentsFromFaculty").click(function() {
		            selectedTable = "#" + selToFaculty.val();
		            console.log("SELECTED TABLE---->", selectedTable)
		            if (selectedToTable !== undefined) {
		                moveAllStudentsFromFaculty();
		            } else {
		                alert("Please select a faculty from faculty list.");
		            }
		        })

		        function sortTable(tableName, colIndex) {
		            var table, rows, switching, rowCount, x, y, shouldSwitch;
		            table = document.getElementById(tableName);
		            switching = true;
		            while (switching) {
		                switching = false;
		                rows = table.rows;
		                for (rowCount = 1; rowCount < (rows.length - 1); rowCount++) {
		                    shouldSwitch = false;
		                    x = rows[rowCount].getElementsByTagName("td")[colIndex];
		                    y = rows[rowCount + 1].getElementsByTagName("td")[colIndex];
		                    if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
		                        shouldSwitch = true;
		                        break;
		                    }
		                }
		                if (shouldSwitch) {
		                    rows[rowCount].parentNode.insertBefore(rows[rowCount + 1], rows[rowCount]);
		                    switching = true;
		                }
		            }
		        }

		       function sortTableNumber(tableName, colIndex) {
		            var table, rows, switching, rowCount, x, y, shouldSwitch;
		            table = document.getElementById(tableName);
		            switching = true;
		            while (switching) {
		                switching = false;
		                rows = table.rows;
		                for (rowCount = 1; rowCount < (rows.length - 1); rowCount++) {
		                    shouldSwitch = false;
		                    x = rows[rowCount].getElementsByTagName("td")[colIndex];
		                    y = rows[rowCount + 1].getElementsByTagName("td")[colIndex];

		                    a = Number(x.innerHTML);
		                    b = Number(y.innerHTML);

		                    console.log("" + a + " - " + b + " => ", a - b)

		                    if (a - b > 0) {
		                        shouldSwitch = true;
		                        break;
		                    }
		                }
		                if (shouldSwitch) {
		                    rows[rowCount].parentNode.insertBefore(rows[rowCount + 1], rows[rowCount]);
		                    switching = true;
		                }
		            }
		        }
		       
		       $(".table-dragable thead tr th:nth-child(1)").click(function(){
		    	   let dragableTable =  $(this).parent().parent().parent().attr("id");
		    	   sortTableNumber(dragableTable, 0);
		       })
		       $(".table-dragable thead tr th:nth-child(2)").click(function(){
		    	   let dragableTable =  $(this).parent().parent().parent().attr("id");
		    	   sortTable(dragableTable, 1);
		       })
		       $(".table-dragable thead tr th:nth-child(3)").click(function(){
                   console.log("Third TH Clicked")
		    	   let dragableTable =  $(this).parent().parent().parent().attr("id");
                   sortTableNumber(dragableTable, 2);
		       })
                $(".table-dragable thead tr th:nth-child(4)").click(function(){
                   console.log("Third TH Clicked")
		    	   let dragableTable =  $(this).parent().parent().parent().attr("id");
                   sortTable(dragableTable, 3);
		       })
		       
		       
		})

		
		</script>
		
		
		

