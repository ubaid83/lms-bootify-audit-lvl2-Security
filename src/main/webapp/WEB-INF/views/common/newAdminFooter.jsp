<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<footer class="col-12 p-0 m-0 text-white">
	<div class="container-fluid">
		<div class="container pt-3 footer-nav">
			<div class="row">
				<div class="col-lg-4 col-md-4 col-sm-12">
					<h6>${userBean.firstname}${userBean.lastname}</h6>
					<hr />
					<ul class="list-unstyled">
					<sec:authorize access="!hasAnyRole('ROLE_STAFF','ROLE_INTL')">
						<li><a
							href="${pageContext.request.contextPath}/profileDetails">Profile</a></li>
					</sec:authorize>
						<li><a href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
						<li><a href="${pageContext.request.contextPath}/loggedout">Log
								out</a></li>
					</ul>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-12">
				<sec:authorize access="!hasAnyRole('ROLE_STAFF','ROLE_INTL')">
					<h6>Quick Links</h6>
					<hr />
					
					<ul class="list-unstyled">
						<li><a
							href="${pageContext.request.contextPath}/showMyCourseStudents">My
								Program</a></li>
						<li><a
							href="${pageContext.request.contextPath}/downloadReportMyCourseStudentForm">Report</a></li>
						<li><a
							href="${pageContext.request.contextPath}/searchFeedback">View
								Feedback</a></li>
						<li><a
							href="${pageContext.request.contextPath}/searchAnnouncement">Announcements</a></li>
					</ul>
					</sec:authorize>
				</div>
				<div class="col-lg-4 col-md-4 col-sm-12">
					<h6>Download App From Play Store</h6>
					<hr />
					<a
						href="https://play.google.com/store/apps/details?id=com.nmims.app"
						target="_blank"> <img
						src="${pageContext.request.contextPath}/resources/images/portalapp.jpg" />
					</a>
				</div>
			</div>
		</div>
	</div>
	<div class="container-fluid footer-nav2 pb-3">
		<hr />
		<div class="container text-center">
			© Copyright
			<script>document.write(new Date().getFullYear())</script>
			| NMIMS
		</div>
	</div>

</footer>







<!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script> -->
<script src="<c:url value="/resources/js/jquery.min.js" />"></script>
<!-- popper -->
<script src="<c:url value="/resources/js/popper.min.js" />"
	type="text/javascript"></script>

<script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.richtext.min.js" />"></script>
<script
	src="<c:url value="/resources/js/vendor/bootstrap-editable.js" />"></script>
<%-- <script src="<c:url value="/resources/js/vendor/MathJax.js" />"></script>  --%>
<script src="<c:url value="/resources/js/moment.min.js" />"></script>
<script src="<c:url value="/resources/js/daterangepicker.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>
<script
	src="<c:url value="/resources/js/dataTables.bootstrap4.min.js" />"></script>
<!-- Timer circles Style -->
<script type="text/javascript"
	src="<c:url value="/resources/js/timecircles.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/custom-timecircles.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/Chart.min.js"/>"></script>
<script src="<c:url value="/resources/ckeditor/ckeditor.js" />"></script>
<script>var myContextPath = "${pageContext.request.contextPath}"</script>
<script type="text/javascript"
	src="<c:url value="/resources/js/style.js" />"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/select2.min.js"></script>
<script src="<c:url value="/resources/js/sweetalert.min.js" />"></script>

<!--  DashBoard Js -->

<script>

/* new script added by akshay */
  
	$("form").submit(function () {
		
		  var clickedForm = $(this); // Select Form
			var form = $(this).attr('id');
		   
		   // var elementArr = document.getElementById(form).elements;
		   
		    var elementArr = document.getElementsByTagName('input');
		    console.log('elementArr length:'+elementArr.length);
		    for(var i = 0; i < elementArr.length; i++){
		    	
		    	if(elementArr[i].type == 'text'){
		    		
		    		var value =elementArr[i].value.trim();
		    		
		    		if(/<\/?[a-z][\s\S]*>/i.test(value)){
		    			alert('HTML Script is not allowed in text boxes');
		    		      return false;
		    		}
		    	}
		    		
		      }
	
	  });
 

if($('.editable').length > 0){
    $('.table')
          .on( 'order.dt',  function () { 
           callEditable();
          })
          .on( 'search.dt', function () {
           callEditable();
          })
          .on( 'page.dt',   function () {
           setTimeout(function(){ callEditable(); }, 100);
          })
          .dataTable();

 function callEditable(){
	 
 $.fn.editable.defaults.mode = 'inline';
 $('.editable').each(function() {
  $(this).editable({
   success : function(response, newValue) {
    obj = JSON.parse(response);
    if (obj.status == 'error') {
     return obj.msg; // msg will be shown in editable
     // form
    }
   }
  });
 })};
 callEditable();
};
</script>


<script>
  
	$(function() {
		
		$('#selectSem').on('change', function() {
			
			var selected = $('#selectSem').val();
			console.log(selected);
			
			if(dashboardPie){
				dashboardPie.destroy();
			}
			if(dashboardBar){
				dashboardBar.destroy();
			}
			
			var dataArr = [];
			$.ajax({
				type : 'POST',
				url : myContextPath + '/getTestStatsBySem?acadSess='+ selected ,
				success : function(data) {
			
					var parsedObj = JSON.parse(data);
					if(parsedObj.hasOwnProperty("passed")){
						dataArr.push(Number(parsedObj.passed));
						dataArr.push(Number(parsedObj.pending));
						dataArr.push(Number(parsedObj.failed));
						console.log(dataArr);
						
						dashboardPie = new Chart(document.getElementById("testPieChart"), {
						    type: 'pie',
						    data: {
						      labels: ["Completed", "Pending", "Failed"],
						      datasets: [
						         {
						          label: "Test",
						          backgroundColor: ["#39b54a", "#feb42f","#d53439"],
						          data: dataArr
						        }
						      ]
						    },
						    options: {
						      legend: { display: false },
						      title: {
						        display: true,
						        text: 'Test Pie Chart'
						      }
						    }
						});
					}else{
						dashboardPie = new Chart(document.getElementById("testPieChart"), {
						    type: 'pie',
						    data: {
						    	labels: ["Completed", "Pending", "Failed","No Data"],
							    datasets: [
						          	{
							          label: "Test",
							          backgroundColor: ["#39b54a", "#feb42f","#d53439","#ddd"],
							          data: [0,0,0,1]
						        	}
							   ]
						    },
						    options: {
						      legend: { display: false },
						      title: {
						        display: true,
						        text: 'Test Pie Chart'
						      }
						    }
						});
					}
				},
				error : function(result) {
					var parsedObj = JSON.parse(result);
					console.log('error' + parsedObj);
				}
			});
			
			var dataArr1 = [];
			$.ajax({
	    		type : 'POST',
	    		url : myContextPath + '/getAssignmentStatsBySem?acadSess='+ selected ,
	    		success : function(data) {
	    	
	    			var parsedObj = JSON.parse(data);
	    			
	    			if(parsedObj.hasOwnProperty("completed")){
	    				dataArr1.push(Number(parsedObj.completed));
	    				dataArr1.push(Number(parsedObj.pending));
	    				dataArr1.push(Number(parsedObj.lateSubmitted));
	    				dataArr1.push(Number(parsedObj.rejected));
	    				console.log(dataArr1);
	    				
	    				dashboardBar = new Chart(document.getElementById("assignBarChart"), {
	    					  type: 'bar',
	    					    data: {
	    					      labels: ["Completed", "Pending", "Late Submitted", "Rejected"],
	    					      datasets: [
	    					        {
	    					          label: "Assignments",
	    					          backgroundColor: ["#39b54a", "#feb42f", "#8E5EA2","#d53439"],
	    					          data: dataArr1
	    					        }
	    					      ]
	    					    },
	    					    options: {
	    					      legend: { display: false },
	    					      title: {
	    					        display: true,
	    					        text: 'Assignments Bar Chart'
	    					      },
	    					        scales: {
	    					        yAxes: [{
	    					            ticks: {
	    					                min: 0,
	    					                stepSize: 20
	    					            }
	    					        }]
	    					    }
	    					    }
	    					}); 
	    			}else{
	    				dashboardBar = new Chart(document.getElementById("assignBarChart"), {
	    					  type: 'bar',
	    					    data: {
	    					      labels: ["Completed", "Pending", "Late Submitted", "Rejected"],
	    					      datasets: [
	    					        {
	    					          label: "Assignments",
	    					          backgroundColor: ["#39b54a", "#feb42f", "#8E5EA2","#d53439"],
	    					          data: [0,0,0,0]
	    					        }
	    					      ]
	    					    },
	    					    options: {
	    					      legend: { display: false },
	    					      title: {
	    					        display: true,
	    					        text: 'Assignments Bar Chart  (No Data)'
	    					      },
	    					        scales: {
	    					        yAxes: [{
	    					            ticks: {
	    					                min: 0,
	    					                stepSize: 1
	    					            }
	    					        }]
	    					    }
	    					    }
	    					}); 
	    			}
	    		},
	    		error : function(result) {
	    			var parsedObj = JSON.parse(result);
	    			console.log('error' + parsedObj);
	    		}
	    	});

		});

	});
</script>
<!--  END DashBoard Js -->

<!--  myCourse Js -->
<script>
  
	$(function() {
		
		$('#semSelect').on('change', function() {
			
			var selected = $('#semSelect').val();
			console.log(selected);
			
			<c:forEach var='sem' items='${ sessionWiseCourseList }'>
				if(selected == '<c:out value="${sem.key}"/>'){
					
					document.getElementById("courseListSemWise").innerHTML= ""
						<c:forEach var='group' items='${ sessionWiseCourseList[sem.key] }'>
						+ '<div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5"> '
						+ '<div class="courseAsset d-flex align-items-start flex-column"> '
						+ '<h6 class="text-uppercase mb-auto">${ group.courseName }</h6>'
						+ '<span class="courseNav"> <a href="${pageContext.request.contextPath}/viewAssignmentFinal?courseId=${group.id}">'
						+ '		<p class="caBg">View Assignment</p>'
						+ '</a> <a href="#">'
						+ '		<p class="ctBg">View Test</p>'
						+ '</a>'
						+ '</span>'
						+ '</div>'
						+ '</div>'
						</c:forEach>;
				}
			</c:forEach>

		});

	});
</script>
<!-- END myCourse Js -->

<script>
$(document).ready(function(){
	
	  var custToggleVal = $('.custToggle').val();
	    if (custToggleVal == 'Y') {
	                    $('.custToggle').prop('checked', true);
	    } else {
	                    $('.custToggle').prop('checked', false);
	    }	
	
	var randQReq = $('#randQReq').val();
	console.log('random q '+randQReq);
	if (randQReq == 'Y') {
		$('#randQReq').prop('checked', true);
		$('#inputMaxQ').parent().toggleClass('d-none');
		$('#sameMarks').toggleClass('d-none');
	} else {
		$('#randQReq').prop('checked', false);
	}
	/* ---- */
	var setTestPwd = $('#setTestPwd').val();
	if (setTestPwd == 'Y') {
		$('#setTestPwd').prop('checked', true);
		$('#testPwdVal').parent().toggleClass('d-none');
	} else {
		$('#setTestPwd').prop('checked', false);
	}
	/* ---- */
	var smqChk = $('#smqChk').val();
	console.log('smq check is:---><'+smqChk);
	if (smqChk == 'Y') {
		$('#smqChk').prop('checked', true);
		$('#marksPerQueIn').parent().toggleClass('d-none');
		
	} else {
		$('#smqChk').prop('checked', false);
	}
	/* ---- */
	
	$('#smqChk').click(function(e) {
		if($("#testType").val().toLowerCase() !== "mix" || ($("#testType").val().toLowerCase() === "mix") && $("#randQReq").is(":checked") !== true){
			var smqChk = $('#smqChk').val();
			$('#marksPerQueIn').parent().toggleClass('d-none');
	
			if (smqChk == 'Y') {
				$('#smqChk').val('N');
			} else {
				$('#smqChk').val('Y');
			}
		}else {
			e.preventDefault()
		}
	});
	
	var subAfterEndDate = $('#subAfterEndDate').val();
	if (subAfterEndDate == 'Y') {
		$('#subAfterEndDate').prop('checked', true);
	} else {
		$('#subAfterEndDate').prop('checked', false);
	}
	/* ---- */
	var showResult = $('#showResult').val();
	if (showResult == 'Y') {
		$('#showResult').prop('checked', true);
	} else {
		$('#showResult').prop('checked', false);
	}
	/* ---- */
	var autoAllocateTest = $('#autoAllocateTest').val();
	if (autoAllocateTest == 'Y') {
		$('#autoAllocateTest').prop('checked', true);
	} else {
		$('#autoAllocateTest').prop('checked', false);
	}
	/* ---- */
	var sendEmailAlert = $('#sendEmailAlert').val();
	if (sendEmailAlert == 'Y') {
		$('#sendEmailAlert').prop('checked', true);
	} else {
		$('#sendEmailAlert').prop('checked', false);
	}
	/* ---- */
	var sendSmsAlert = $('#sendSmsAlert').val();
	if (sendSmsAlert == 'Y') {
		$('#sendSmsAlert').prop('checked', true);
	} else {
		$('#sendSmsAlert').prop('checked', false);
	}
	/* ---- */
	var submitByOneInGroup = $('#submitByOneInGroup').val();
	if (submitByOneInGroup == 'Y') {
		$('#submitByOneInGroup').prop('checked', true);
	} else {
		$('#submitByOneInGroup').prop('checked', false);
	}
	/* ---- */
	var rightGrant = $('#rightGrant').val();
	if (rightGrant == 'Y') {
		$('#rightGrant').prop('checked', true);
	} else {
		$('#rightGrant').prop('checked', false);
	}
	/* Multiple assignment */
    var allowAfterEndDate1 = $('#allowAfterEndDate1').val();
	if (rightGrant == 'Y') {
		$('#allowAfterEndDate1').prop('checked', true);
	} else {
		$('#allowAfterEndDate1').prop('checked', false);
	}
	/* ----- */
	var showResultsToStudents1 = $('#showResultsToStudents1').val();
	if (showResultsToStudents1 == 'Y') {
		$('#showResultsToStudents1').prop('checked', true);
	} else {
		$('#showResultsToStudents1').prop('checked', false);
	}
	/* ----- */
	var sendEmailAlert1 = $('#sendEmailAlert1').val();
	if (sendEmailAlert1 == 'Y') {
		$('#sendEmailAlert1').prop('checked', true);
	} else {
		$('#sendEmailAlert1').prop('checked', false);
	}
	/* ----- */
	var sendSmsAlert1 = $('#sendSmsAlert1').val();
	if (sendSmsAlert1 == 'Y') {
		$('#sendSmsAlert1').prop('checked', true);
	} else {
		$('#sendSmsAlert1').prop('checked', false);
	}
	/* ----- */
	var sendEmailAlertToParents1 = $('#sendEmailAlertToParents1').val();
	if (sendEmailAlertToParents1 == 'Y') {
		$('#sendEmailAlertToParents1').prop('checked', true);
	} else {
		$('#sendEmailAlertToParents1').prop('checked', false);
	}
	/* ----- */
	var sendSmsAlertToParents1 = $('#sendSmsAlertToParents1').val();
	if (sendSmsAlertToParents1 == 'Y') {
		$('#sendSmsAlertToParents1').prop('checked', true);
	} else {
		$('#sendSmsAlertToParents1').prop('checked', false);
	}
	
	/* SEND ALERT TO PARENTS */
	var sendEmailAlertToParents = $('#sendEmailAlertParents').val();
	if (sendEmailAlertToParents == 'Y') {
		$('#sendEmailAlertParents').prop('checked', true);
	} else {
		$('#sendEmailAlertParents').prop('checked', false);
	}
	/* ----- */
	var sendSmsAlertToParents = $('#sendSmsAlertToParents').val();
	if (sendSmsAlertToParents == 'Y') {
		$('#sendSmsAlertToParents').prop('checked', true);
	} else {
		$('#sendSmsAlertToParents').prop('checked', false);
	}
	
	
	
	
	
	
	/* ----- */
	var examViewType = $('#examViewType').val();
	if (examViewType == 'Y') {
		$('#examViewType').prop('checked', true);
	} else {
		$('#examViewType').prop('checked', false);
	}
	
	
	/* ----- */
	var setTestPwd = $('#setTestPwd').val();
	if (setTestPwd == 'Y') {
		$('#setTestPwd').prop('checked', true);
	} else {
		$('#setTestPwd').prop('checked', false);
	}
});
</script>

<!--  viewAssignmentFinal Js -->
<script>
  
	$(function() {
		
		$('#assignSem').on('change', function() {
			
			var selected = $('#assignSem').val();
			console.log(selected);
			
			<c:forEach var='sem' items='${ sessionWiseCourseList }'>
				if(selected == '<c:out value="${sem.key}"/>'){
					
					var optionsAsString = "";
					
					$('#assignCourse').find(
					'option')
					.remove();
					
					optionsAsString += "<option value='' disabled selected>--SELECT COURSE--</option>";
					<c:forEach var='group' items='${ sessionWiseCourseList[sem.key] }'>
						optionsAsString += "<option value='${ group.id }'>${ group.courseName }</option>";
					</c:forEach>
					
					console
					.log("optionsAsString"
							+ optionsAsString);

					$('#assignCourse')
							.append(
									optionsAsString);
				}
			</c:forEach>
			
			if(AssignmentBar){
				AssignmentBar.destroy();
			}
			
			AssignmentBar = new Chart(document.getElementById("assignmentBarChart"), {
				  type: 'bar',
				    data: {
				      labels: ["Completed", "Pending", "Late Submitted", "Rejected"],
				      datasets: [
				        {
				          label: "Total",
				          backgroundColor: ["#2ea745","#d69400", "#8e5ea2","#d53439"],
				          data: [0,0,0,0]
				        }
				      ]
				    },
				  options: {
				        responsive: true,
				        maintainAspectRatio: false,
				        legend: {
				        display: false
				        },
				        scales: {
	  				        yAxes: [{
	  				            ticks: {
	  				                min: 0,
	  				                stepSize: 1
	  				            }
	  				        }],
	  				            xAxes: [{
	  				            ticks: {
	  				                fontSize: 14
	  				            }
	  				        }]
				       },

				      title: {
				        display: true,
				        text: 'Overall Assignment Data (No Data)'
				      }
				  }
				}); 

		});
		
		$('#assignCourse').on('change', function() {
			
			var acadSession = $('#assignSem').val();
			var courseId = $('#assignCourse').val();
			
			if(AssignmentBar){
				AssignmentBar.destroy();
			}
			
			$.ajax({
		  		type : 'POST',
		  		url : myContextPath + '/viewAssignmentFinalAjax?courseId='+ courseId ,
		  		success : function(data) {
		  	
		  			var parsedObj = JSON.parse(data);
		  			console.log(parsedObj);
		  			
		  		},
		  		error : function(result) {
		  			var parsedObj = JSON.parse(result);
		  			console.log('error' + parsedObj);
		  		}
		  	});
			
			var dataArr1 = [];
		  	$.ajax({
		  		type : 'POST',
		  		url : myContextPath + '/getAssignmentStatsBySem?acadSess='+ acadSession +'&courseId='+ courseId ,
		  		success : function(data) {
		  	
		  			var parsedObj = JSON.parse(data);
		  			
		  			if(parsedObj.hasOwnProperty("completed")){
		  				dataArr1.push(Number(parsedObj.completed));
		  				dataArr1.push(Number(parsedObj.pending));
		  				dataArr1.push(Number(parsedObj.lateSubmitted));
		  				dataArr1.push(Number(parsedObj.rejected));
		  				console.log(dataArr1);
		  				
		  				if(AssignmentBar){
		  				  AssignmentBar.destroy();
		  				}
		  				AssignmentBar = new Chart(document.getElementById("assignmentBarChart"), {
							  type: 'bar',
							    data: {
							      labels: ["Completed", "Pending", "Late Submitted", "Rejected"],
							      datasets: [
							        {
							          label: "Total",
							          backgroundColor: ["#2ea745","#d69400", "#8e5ea2","#d53439"],
							          data: dataArr1
							        }
							      ]
							    },
							  options: {
			  				        responsive: true,
			  				        maintainAspectRatio: false,
			  				        legend: {
			  				        display: false
			  				        },
			  				        scales: {
				  				        yAxes: [{
				  				            ticks: {
				  				                min: 0,
				  				                stepSize: 1
				  				            }
				  				        }],
				  				            xAxes: [{
				  				            ticks: {
				  				                fontSize: 14
				  				            }
				  				        }]
			  				       },

			  				      title: {
			  				        display: true,
			  				        text: 'Overall Assignment Data'
			  				      }
							  }
							}); 
		  			}else{
		  				AssignmentBar = new Chart(document.getElementById("assignmentBarChart"), {
		  					  type: 'bar',
		  					    data: {
		  					      labels: ["Completed", "Pending", "Late Submitted", "Rejected"],
		  					      datasets: [
		  					        {
		  					          label: "Total",
		  					          backgroundColor: ["#2ea745","#d69400", "#8e5ea2","#d53439"],
		  					          data: [0,0,0,0]
		  					        }
		  					      ]
		  					    },
		  					  options: {
			  				        responsive: true,
			  				        maintainAspectRatio: false,
			  				        legend: {
			  				        display: false
			  				        },
			  				        scales: {
				  				        yAxes: [{
				  				            ticks: {
				  				                min: 0,
				  				                stepSize: 1
				  				            }
				  				        }],
				  				            xAxes: [{
				  				            ticks: {
				  				                fontSize: 14
				  				            }
				  				        }]
			  				       },

			  				      title: {
			  				        display: true,
			  				        text: 'Overall Assignment Data (No Data)'
			  				      }
		  					  }
		  					}); 
		  			}
		  		},
		  		error : function(result) {
		  			var parsedObj = JSON.parse(result);
		  			console.log('error' + parsedObj);
		  		}
		  	});
		  	
		});
		
 	$('#viewAssignmentTable').DataTable();
 		
	});	
</script>
<!-- DataTable JS -->
<script>
$(".table").DataTable().destroy();
$('.table').DataTable( {
        initComplete: function () {
            this.api().columns().every( function () {
                var column = this;
                var select = $('<select><option value=""></option></select>')
                    .appendTo( $(column.footer()).empty() )
                    .on( 'change', function () {
                        var val = $.fn.dataTable.util.escapeRegex(
                            $(this).val()
                        );
 
                        column
                            .search( val ? '^'+val+'$' : '', true, false )
                            .draw();
                    } );
 
                column.data().unique().sort().each( function ( d, j ) {
                    select.append( '<option value="'+d+'">'+d+'</option>' )
                } );
            } );
        }
    } );


$('#example-select-all').on(
'click',
function() {
	// Check/uncheck all checkboxes in the table
	var rows = table.rows({
		'search' : 'applied'
	}).nodes();
	$('input[type="checkbox"]', rows).prop('checked',
			this.checked);
});

</script>
<!-- END viewAssignmentFinal Js -->




<script>
        /*SET RAND QUESTION AND PASSWORD FOR TEST*/

        $('#randQReq').click(function() {
            var randQReq = $('#randQReq').val();
            $('#inputMaxQ').parent().toggleClass('d-none');
            //IF Y AND N
            
            if (randQReq == 'Y') {
                  $('#randQReq').val('N');
            } else {
                  $('#randQReq').val('Y');
            }
            
        });
        
        $('#setTestPwd').click(function() {
            var setTestPwd = $('#setTestPwd').val();
            $('#testPwdVal').parent().toggleClass('d-none');
            
            
            if (setTestPwd == 'Y') {
                  $('#setTestPwd').val('N');
            } else {
                  $('#setTestPwd').val('Y');
            }
            
            
        });
    
     
        
        

        /*CALLING RICH TEXT EDITOR*/
  /*   $('.testDesc').richText();   */

        /*CALLING DATE PICKER*/
        if ($('#testDateRangeBtn').length > 0) {
        $(function() {

            $('#testDateRangeBtn').daterangepicker({

                "showDropdowns": true,
                "timePicker": true,
                "showCustomRangeLabel": false,
                "alwaysShowCalendars": true,
                "opens": "center",

                /* autoUpdateInput: false,
                locale: {
                    cancelLabel: 'Clear'
                } */
            }, 
            
            function(start, end, label) {
                  var sDate = start.format('YYYY-MM-DD HH:mm:ss');
                  var eDate = end.format('YYYY-MM-DD HH:mm:ss');
                  $('#startDate').val(sDate+'-'+eDate);
                  $('#testStartDate').val(sDate);
                  $('#testEndDate').val(eDate);
                
              });
            

           /*  $('#startDate').on('apply.daterangepicker', function(ev, picker) {
                $(this).val(picker.startDate.format('YYYY/MM/DD HH:mm:ss') + ' - ' + picker.endDate.format('YYYY/MM/DD HH:mm:ss'));
            });
            
            $('#testDateRangeBtn').on('apply.daterangepicker', function(ev, picker) {
                
      $('#startDate').val(picker.startDate.format('YYYY/MM/DD HH:mm:ss') + ' - ' + picker.endDate.format('YYYY/MM/DD HH:mm:ss'));
        
      var x = $('#startDate').val(picker.startDate.format('YYYY/MM/DD HH:mm:ss') + ' - ' + picker.endDate.format('YYYY/MM/DD HH:mm:ss'));
      
      console.log($('#startDate').val(picker.startDate.format('YYYY/MM/DD HH:mm:ss') + ' - ' + picker.endDate.format('YYYY/MM/DD HH:mm:ss')));
                
      $('#startDate').on('apply.daterangepicker', function(start, end) {
        console.log("A new date selection was made: " + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
      }); 
            
            }); */

           
            
            
            $('#startDate').on('cancel.daterangepicker', function(ev, picker) {
                $(this).val('');
            });
            $('#testDateRangeBtn').on('cancel.daterangepicker', function(ev, picker) {
                $('#startDate').val('');
            });

        });
        
        }

    </script>

<script>
$(function() {
            
            $('#semDashboardFaculty').on('change', function() {
                  
                  var selected = $('#semDashboardFaculty').val();
                  var vals = selected.split('-')[0];
                  

                  console.log(vals);

                  <c:forEach var='sem' items='${ sessionWiseCourseListMap }'>
                        if(selected == '<c:out value="${sem.key}"/>'){
                              
                              document.getElementById("courseListSemWisefaculty").innerHTML= ""
                                    <c:forEach var='group' items='${ sessionWiseCourseListMap[sem.key] }'>
                                    + '<div class="col-lg-3 col-md-3 col-sm-6 col-6 mt-5"> '
                                    + '<div class="courseAsset d-flex align-items-start flex-column"> '
                                    + '<h6 class="text-uppercase mb-auto">${ group.course.courseName }</h6>'
                                    + '<span class="courseNav"> <a href="${pageContext.request.contextPath}/viewCourse?id=${group.course.id}">'
                                    + '         <p class="caBg">View Course</p>'
                                    + '</a>'
                                    + '</span>'
                                    + '</div>'
                                    + '</div>'
                                    </c:forEach>;
                        }
                  </c:forEach>

            });

      });

</script>



<!-- Start newUserAnnouncementList -->
<script>
$(function() {

		$('#stProgram')
				.on('change',
						function() {
						
						var selected = $('#stProgram').val();
						console.log(selected);
						var str = "";
						<c:forEach var="announcement"
							items="${announcementTypeMap['PROGRAM']}" varStatus="status">
							if (selected == 'ALL') {
								str += ' <div class="announcementItem" data-toggle="modal" '
									
									+' data-target="#modalAnnounceProgram${status.count}"> '
									+' <h6 class="card-title">${announcement.subject}<sup '
									+' class="announcementDate text-danger font-italic"><small><span '
									+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
									+' class="toDate">${announcement.endDate}</span></small></sup> '
									+' </h6> '
									
									+' <p class="border-bottom"></p> '
									+' </div> '
									+' <div id="modalAnnouncement position-fixed"> '
									+' <div class="modal fade fnt-13" id="modalAnnounceProgram${status.count}" '
									+' tabindex="-1" role="dialog" '
									+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
									+' <div '
									+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
									+' role="document"> '
									+' <div class="modal-content"> '
									+' <div class="modal-header"> '
									+' <h6 class="modal-title">${announcement.subject}</h6> '
									+' <button type="button" class="close" data-dismiss="modal" '
									+' aria-label="Close"> '
									+' <span aria-hidden="true">&times;</span> '
									+' </button> '
									+' </div> '
									+' <div class="modal-body"> '
									+' <div class="d-flex font-weight-bold"> '
									+' <p class="mr-auto"> '
									+' Start Date: <span>${announcement.startDate}</span> '
									+' </p> '
									+' <p> '
									+' End Date: <span>${announcement.endDate}</span> '
									+' </p> '
									+' </div><c:if test="${announcement.filePath ne null}"> '
									+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
									+' </c:if> '

									+' <p class="announcementDetail"> ' ;
									
								str	+= `${announcement.description}`;
								
								str += ' </p> ' 
									+' </div> '
									+' <div class="modal-footer"> '
									+' <button type="button" class="btn btn-modalClose" '
									+' data-dismiss="modal">Close</button> '
									+' </div> '
									+' </div> '
									+' </div> '
									+' </div> '
									+' </div> ';
							}else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
							str += ' <div class="announcementItem" data-toggle="modal" '
							
								+' data-target="#modalAnnounceProgram${status.count}"> '
								+' <h6 class="card-title">${announcement.subject}<sup '
								+' class="announcementDate text-danger font-italic"><small><span '
								+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
								+' class="toDate">${announcement.endDate}</span></small></sup> '
								+' </h6> '
								
								+' <p class="border-bottom"></p> '
								+' </div> '
								+' <div id="modalAnnouncement position-fixed"> '
								+' <div class="modal fade fnt-13" id="modalAnnounceProgram${status.count}" '
								+' tabindex="-1" role="dialog" '
								+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
								+' <div '
								+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
								+' role="document"> '
								+' <div class="modal-content"> '
								+' <div class="modal-header"> '
								+' <h6 class="modal-title">${announcement.subject}</h6> '
								+' <button type="button" class="close" data-dismiss="modal" '
								+' aria-label="Close"> '
								+' <span aria-hidden="true">&times;</span> '
								+' </button> '
								+' </div> '
								+' <div class="modal-body"> '
								+' <div class="d-flex font-weight-bold"> '
								+' <p class="mr-auto"> '
								+' Start Date: <span>${announcement.startDate}</span> '
								+' </p> '
								+' <p> '
								+' End Date: <span>${announcement.endDate}</span> '
								+' </p> '
								+' </div><c:if test="${announcement.filePath ne null}"> '
								+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
								+' </c:if> '

								+' <p class="announcementDetail"> ' ;
								
							str	+= `${announcement.description}`;
							
							str += ' </p> ' 
								+' </div> '
								+' <div class="modal-footer"> '
								+' <button type="button" class="btn btn-modalClose" '
								+' data-dismiss="modal">Close</button> '
								+' </div> '
								+' </div> '
								+' </div> '
								+' </div> '
								+' </div> ';
							}
						</c:forEach>
						document.getElementById("programAnn").innerHTML = "" + str;
			});
		
		$('#stCourse')
		.on('change',
				function() {
				
				var selected = $('#stCourse').val();
				console.log(selected);
				var str = "";
				<c:forEach var="announcement"
					items="${announcementTypeMap['COURSE']}" varStatus="status">
					if (selected == 'ALL') {
						str += ' <div class="announcementItem" data-toggle="modal" '
							
							+' data-target="#modalAnnounceCourse${status.count}"> '
							+' <h6 class="card-title">${announcement.subject}<sup '
							+' class="announcementDate text-danger font-italic"><small><span '
							+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
							+' class="toDate">${announcement.endDate}</span></small></sup> '
							+' </h6> '
							
							+' <p class="border-bottom"></p> '
							+' </div> '
							+' <div id="modalAnnouncement position-fixed"> '
							+' <div class="modal fade fnt-13" id="modalAnnounceCourse${status.count}" '
							+' tabindex="-1" role="dialog" '
							+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
							+' <div '
							+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
							+' role="document"> '
							+' <div class="modal-content"> '
							+' <div class="modal-header"> '
							+' <h6 class="modal-title">${announcement.subject}</h6> '
							+' <button type="button" class="close" data-dismiss="modal" '
							+' aria-label="Close"> '
							+' <span aria-hidden="true">&times;</span> '
							+' </button> '
							+' </div> '
							+' <div class="modal-body"> '
							+' <div class="d-flex font-weight-bold"> '
							+' <p class="mr-auto"> '
							+' Start Date: <span>${announcement.startDate}</span> '
							+' </p> '
							+' <p> '
							+' End Date: <span>${announcement.endDate}</span> '
							+' </p> '
							+' </div><c:if test="${announcement.filePath ne null}"> '
							+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
							+' </c:if> '

							+' <p class="announcementDetail"> ' ;
							
						str	+= `${announcement.description}`;
						
						str += ' </p> ' 
							+' </div> '
							+' <div class="modal-footer"> '
							+' <button type="button" class="btn btn-modalClose" '
							+' data-dismiss="modal">Close</button> '
							+' </div> '
							+' </div> '
							+' </div> '
							+' </div> '
							+' </div> ';
					}else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
					str += ' <div class="announcementItem" data-toggle="modal" '
					
						+' data-target="#modalAnnounceCourse${status.count}"> '
						+' <h6 class="card-title">${announcement.subject}<sup '
						+' class="announcementDate text-danger font-italic"><small><span '
						+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
						+' class="toDate">${announcement.endDate}</span></small></sup> '
						+' </h6> '
						
						+' <p class="border-bottom"></p> '
						+' </div> '
						+' <div id="modalAnnouncement position-fixed"> '
						+' <div class="modal fade fnt-13" id="modalAnnounceCourse${status.count}" '
						+' tabindex="-1" role="dialog" '
						+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
						+' <div '
						+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
						+' role="document"> '
						+' <div class="modal-content"> '
						+' <div class="modal-header"> '
						+' <h6 class="modal-title">${announcement.subject}</h6> '
						+' <button type="button" class="close" data-dismiss="modal" '
						+' aria-label="Close"> '
						+' <span aria-hidden="true">&times;</span> '
						+' </button> '
						+' </div> '
						+' <div class="modal-body"> '
						+' <div class="d-flex font-weight-bold"> '
						+' <p class="mr-auto"> '
						+' Start Date: <span>${announcement.startDate}</span> '
						+' </p> '
						+' <p> '
						+' End Date: <span>${announcement.endDate}</span> '
						+' </p> '
						+' </div><c:if test="${announcement.filePath ne null}"> '
						+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
						+' </c:if> '

						+' <p class="announcementDetail"> ' ;
						
					str	+= `${announcement.description}`;
					
					str += ' </p> ' 
						+' </div> '
						+' <div class="modal-footer"> '
						+' <button type="button" class="btn btn-modalClose" '
						+' data-dismiss="modal">Close</button> '
						+' </div> '
						+' </div> '
						+' </div> '
						+' </div> '
						+' </div> ';
					}
				</c:forEach>
				document.getElementById("courseAnn").innerHTML = "" + str;
		});
		
		$('#stInstitute')
		.on('change',
				function() {
				
				var selected = $('#stInstitute').val();
				console.log(selected);
				var str = "";
				<c:forEach var="announcement"
					items="${announcementTypeMap['INSTITUTE']}" varStatus="status">
					if (selected == 'ALL') {
						str += ' <div class="announcementItem" data-toggle="modal" '
							
							+' data-target="#modalAnnounceInstitute${status.count}"> '
							+' <h6 class="card-title">${announcement.subject}<sup '
							+' class="announcementDate text-danger font-italic"><small><span '
							+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
							+' class="toDate">${announcement.endDate}</span></small></sup> '
							+' </h6> '
							
							+' <p class="border-bottom"></p> '
							+' </div> '
							+' <div id="modalAnnouncement position-fixed"> '
							+' <div class="modal fade fnt-13" id="modalAnnounceInstitute${status.count}" '
							+' tabindex="-1" role="dialog" '
							+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
							+' <div '
							+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
							+' role="document"> '
							+' <div class="modal-content"> '
							+' <div class="modal-header"> '
							+' <h6 class="modal-title">${announcement.subject}</h6> '
							+' <button type="button" class="close" data-dismiss="modal" '
							+' aria-label="Close"> '
							+' <span aria-hidden="true">&times;</span> '
							+' </button> '
							+' </div> '
							+' <div class="modal-body"> '
							+' <div class="d-flex font-weight-bold"> '
							+' <p class="mr-auto"> '
							+' Start Date: <span>${announcement.startDate}</span> '
							+' </p> '
							+' <p> '
							+' End Date: <span>${announcement.endDate}</span> '
							+' </p> '
							+' </div><c:if test="${announcement.filePath ne null}"> '
							+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
							+' </c:if> '

							+' <p class="announcementDetail"> ' ;
							
						str	+= `${announcement.description}`;
						
						str += ' </p> ' 
							+' </div> '
							+' <div class="modal-footer"> '
							+' <button type="button" class="btn btn-modalClose" '
							+' data-dismiss="modal">Close</button> '
							+' </div> '
							+' </div> '
							+' </div> '
							+' </div> '
							+' </div> ';
					}else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
					str += ' <div class="announcementItem" data-toggle="modal" '
					
						+' data-target="#modalAnnounceInstitute${status.count}"> '
						+' <h6 class="card-title">${announcement.subject}<sup '
						+' class="announcementDate text-danger font-italic"><small><span '
						+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
						+' class="toDate">${announcement.endDate}</span></small></sup> '
						+' </h6> '
						
						+' <p class="border-bottom"></p> '
						+' </div> '
						+' <div id="modalAnnouncement position-fixed"> '
						+' <div class="modal fade fnt-13" id="modalAnnounceInstitute${status.count}" '
						+' tabindex="-1" role="dialog" '
						+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
						+' <div '
						+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
						+' role="document"> '
						+' <div class="modal-content"> '
						+' <div class="modal-header"> '
						+' <h6 class="modal-title">${announcement.subject}</h6> '
						+' <button type="button" class="close" data-dismiss="modal" '
						+' aria-label="Close"> '
						+' <span aria-hidden="true">&times;</span> '
						+' </button> '
						+' </div> '
						+' <div class="modal-body"> '
						+' <div class="d-flex font-weight-bold"> '
						+' <p class="mr-auto"> '
						+' Start Date: <span>${announcement.startDate}</span> '
						+' </p> '
						+' <p> '
						+' End Date: <span>${announcement.endDate}</span> '
						+' </p> '
						+' </div><c:if test="${announcement.filePath ne null}"> '
						+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
						+' </c:if> '

						+' <p class="announcementDetail"> ' ;
						
					str	+= `${announcement.description}`;
					
					str += ' </p> ' 
						+' </div> '
						+' <div class="modal-footer"> '
						+' <button type="button" class="btn btn-modalClose" '
						+' data-dismiss="modal">Close</button> '
						+' </div> '
						+' </div> '
						+' </div> '
						+' </div> '
						+' </div> ';
					}
				</c:forEach>
				document.getElementById("instituteAnn").innerHTML = "" + str;
	});
		
		$('#stLibrary')
		.on('change',
				function() {
				
				var selected = $('#stLibrary').val();
				console.log(selected);
				var str = "";
				<c:forEach var="announcement"
					items="${announcementTypeMap['LIBRARY']}" varStatus="status">
					if (selected == 'ALL') {
						str += ' <div class="announcementItem" data-toggle="modal" '
							
							+' data-target="#modalAnnounceLibrary${status.count}"> '
							+' <h6 class="card-title">${announcement.subject}<sup '
							+' class="announcementDate text-danger font-italic"><small><span '
							+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
							+' class="toDate">${announcement.endDate}</span></small></sup> '
							+' </h6> '
							
							+' <p class="border-bottom"></p> '
							+' </div> '
							+' <div id="modalAnnouncement position-fixed"> '
							+' <div class="modal fade fnt-13" id="modalAnnounceLibrary${status.count}" '
							+' tabindex="-1" role="dialog" '
							+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
							+' <div '
							+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
							+' role="document"> '
							+' <div class="modal-content"> '
							+' <div class="modal-header"> '
							+' <h6 class="modal-title">${announcement.subject}</h6> '
							+' <button type="button" class="close" data-dismiss="modal" '
							+' aria-label="Close"> '
							+' <span aria-hidden="true">&times;</span> '
							+' </button> '
							+' </div> '
							+' <div class="modal-body"> '
							+' <div class="d-flex font-weight-bold"> '
							+' <p class="mr-auto"> '
							+' Start Date: <span>${announcement.startDate}</span> '
							+' </p> '
							+' <p> '
							+' End Date: <span>${announcement.endDate}</span> '
							+' </p> '
							+' </div><c:if test="${announcement.filePath ne null}"> '
							+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
							+' </c:if> '

							+' <p class="announcementDetail"> ' ;
							
						str	+= `${announcement.description}`;
						
						str += ' </p> ' 
							+' </div> '
							+' <div class="modal-footer"> '
							+' <button type="button" class="btn btn-modalClose" '
							+' data-dismiss="modal">Close</button> '
							+' </div> '
							+' </div> '
							+' </div> '
							+' </div> '
							+' </div> ';
					}else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
					str += ' <div class="announcementItem" data-toggle="modal" '
					
						+' data-target="#modalAnnounceLibrary${status.count}"> '
						+' <h6 class="card-title">${announcement.subject}<sup '
						+' class="announcementDate text-danger font-italic"><small><span '
						+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
						+' class="toDate">${announcement.endDate}</span></small></sup> '
						+' </h6> '
						
						+' <p class="border-bottom"></p> '
						+' </div> '
						+' <div id="modalAnnouncement position-fixed"> '
						+' <div class="modal fade fnt-13" id="modalAnnounceLibrary${status.count}" '
						+' tabindex="-1" role="dialog" '
						+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
						+' <div '
						+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
						+' role="document"> '
						+' <div class="modal-content"> '
						+' <div class="modal-header"> '
						+' <h6 class="modal-title">${announcement.subject}</h6> '
						+' <button type="button" class="close" data-dismiss="modal" '
						+' aria-label="Close"> '
						+' <span aria-hidden="true">&times;</span> '
						+' </button> '
						+' </div> '
						+' <div class="modal-body"> '
						+' <div class="d-flex font-weight-bold"> '
						+' <p class="mr-auto"> '
						+' Start Date: <span>${announcement.startDate}</span> '
						+' </p> '
						+' <p> '
						+' End Date: <span>${announcement.endDate}</span> '
						+' </p> '
						+' </div><c:if test="${announcement.filePath ne null}"> '
						+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
						+' </c:if> '

						+' <p class="announcementDetail"> ' ;
						
					str	+= `${announcement.description}`;
					
					str += ' </p> ' 
						+' </div> '
						+' <div class="modal-footer"> '
						+' <button type="button" class="btn btn-modalClose" '
						+' data-dismiss="modal">Close</button> '
						+' </div> '
						+' </div> '
						+' </div> '
						+' </div> '
						+' </div> ';
					}
				</c:forEach>
				document.getElementById("libraryAnn").innerHTML = "" + str;
	});
		
		$('#stCounselor')
		.on('change',
				function() {
				
				var selected = $('#stCounselor').val();
				console.log(selected);
				var str = "";
				<c:forEach var="announcement"
					items="${announcementTypeMap['COUNSELOR']}" varStatus="status">
					if (selected == 'ALL') {
						str += ' <div class="announcementItem" data-toggle="modal" '
							
							+' data-target="#modalAnnounceCounselor${status.count}"> '
							+' <h6 class="card-title">${announcement.subject}<sup '
							+' class="announcementDate text-danger font-italic"><small><span '
							+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
							+' class="toDate">${announcement.endDate}</span></small></sup> '
							+' </h6> '
							
							+' <p class="border-bottom"></p> '
							+' </div> '
							+' <div id="modalAnnouncement position-fixed"> '
							+' <div class="modal fade fnt-13" id="modalAnnounceCounselor${status.count}" '
							+' tabindex="-1" role="dialog" '
							+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
							+' <div '
							+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
							+' role="document"> '
							+' <div class="modal-content"> '
							+' <div class="modal-header"> '
							+' <h6 class="modal-title">${announcement.subject}</h6> '
							+' <button type="button" class="close" data-dismiss="modal" '
							+' aria-label="Close"> '
							+' <span aria-hidden="true">&times;</span> '
							+' </button> '
							+' </div> '
							+' <div class="modal-body"> '
							+' <div class="d-flex font-weight-bold"> '
							+' <p class="mr-auto"> '
							+' Start Date: <span>${announcement.startDate}</span> '
							+' </p> '
							+' <p> '
							+' End Date: <span>${announcement.endDate}</span> '
							+' </p> '
							+' </div><c:if test="${announcement.filePath ne null}"> '
							+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
							+' </c:if> '

							+' <p class="announcementDetail"> ' ;
							
						str	+= `${announcement.description}`;
						
						str += ' </p> ' 
							+' </div> '
							+' <div class="modal-footer"> '
							+' <button type="button" class="btn btn-modalClose" '
							+' data-dismiss="modal">Close</button> '
							+' </div> '
							+' </div> '
							+' </div> '
							+' </div> '
							+' </div> ';
					}else if (selected == '<c:out value="${announcement.announcementSubType}"/>') {
					str += ' <div class="announcementItem" data-toggle="modal" '
					
						+' data-target="#modalAnnounceCounselor${status.count}"> '
						+' <h6 class="card-title">${announcement.subject}<sup '
						+' class="announcementDate text-danger font-italic"><small><span '
						+' class="fromDate">${announcement.startDate}</span>&nbsp;-&nbsp;<span '
						+' class="toDate">${announcement.endDate}</span></small></sup> '
						+' </h6> '
						
						+' <p class="border-bottom"></p> '
						+' </div> '
						+' <div id="modalAnnouncement position-fixed"> '
						+' <div class="modal fade fnt-13" id="modalAnnounceCounselor${status.count}" '
						+' tabindex="-1" role="dialog" '
						+' aria-labelledby="submitAssignmentTitle" aria-hidden="true"> '
						+' <div '
						+' class="modal-dialog modal-dialog-centered modal-dialog-scrollable" '
						+' role="document"> '
						+' <div class="modal-content"> '
						+' <div class="modal-header"> '
						+' <h6 class="modal-title">${announcement.subject}</h6> '
						+' <button type="button" class="close" data-dismiss="modal" '
						+' aria-label="Close"> '
						+' <span aria-hidden="true">&times;</span> '
						+' </button> '
						+' </div> '
						+' <div class="modal-body"> '
						+' <div class="d-flex font-weight-bold"> '
						+' <p class="mr-auto"> '
						+' Start Date: <span>${announcement.startDate}</span> '
						+' </p> '
						+' <p> '
						+' End Date: <span>${announcement.endDate}</span> '
						+' </p> '
						+' </div><c:if test="${announcement.filePath ne null}"> '
						+' <p class="font-weight-bold">Attachment: <a href="sendAnnouncementFile?id=${announcement.id}">Download</a></p> '
						+' </c:if> '

						+' <p class="announcementDetail"> ' ;
						
					str	+= `${announcement.description}`;
					
					str += ' </p> ' 
						+' </div> '
						+' <div class="modal-footer"> '
						+' <button type="button" class="btn btn-modalClose" '
						+' data-dismiss="modal">Close</button> '
						+' </div> '
						+' </div> '
						+' </div> '
						+' </div> '
						+' </div> ';
					}
				</c:forEach>
				document.getElementById("counselorAnn").innerHTML = "" + str;
	});
						
	});
</script>
<!-- End newUserAnnouncementList -->


<!-- DATE RANGE PICKER -->
<script>	
if ($('#startDate').length > 0) {
		$(function() {

  		  $('#startDate').daterangepicker({
  		      autoUpdateInput: false,
  		      locale: {
  		          cancelLabel: 'Clear'
  		      },
  		      "singleDatePicker": true,
          	  "showDropdowns" : true,
                "timePicker" : true,
                "showCustomRangeLabel" : false,
                "alwaysShowCalendars" : true,
                "opens" : "center"
  		  });

  		  $('#startDate').on('apply.daterangepicker', function(ev, picker) {
  		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
  		  });

  		  $('#startDate').on('cancel.daterangepicker', function(ev, picker) {
  		      $(this).val('');
  		  });

  		});
} 	
  	
if ($('#endDate').length > 0) {
  	$(function() {

		  $('#endDate').daterangepicker({
		      autoUpdateInput: false,
		      locale: {
		          cancelLabel: 'Clear'
		      },
		      "singleDatePicker": true,
        	  "showDropdowns" : true,
              "timePicker" : true,
              "showCustomRangeLabel" : false,
              "alwaysShowCalendars" : true,
              "opens" : "center"
		  });

		  $('#endDate').on('apply.daterangepicker', function(ev, picker) {
		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
		  });

		  $('#endDate').on('cancel.daterangepicker', function(ev, picker) {
		      $(this).val('');
		  });

		});
}

if ($('#startDate1').length > 0) {
  	$(function() {

		  $('#startDate1').daterangepicker({
		      autoUpdateInput: false,
		      locale: {
		          cancelLabel: 'Clear'
		      },
		      "singleDatePicker": true,
      	  "showDropdowns" : true,
            "timePicker" : true,
            "showCustomRangeLabel" : false,
            "alwaysShowCalendars" : true,
            "opens" : "center"
		  });

		  $('#startDate1').on('apply.daterangepicker', function(ev, picker) {
		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
		  });

		  $('#startDate1').on('cancel.daterangepicker', function(ev, picker) {
		      $(this).val('');
		  });

		});
}
	
if ($('#endDate1').length > 0) {
	$(function() {

	  $('#endDate1').daterangepicker({
	      autoUpdateInput: false,
	      locale: {
	          cancelLabel: 'Clear'
	      },
	      "singleDatePicker": true,
    	  "showDropdowns" : true,
          "timePicker" : true,
          "showCustomRangeLabel" : false,
          "alwaysShowCalendars" : true,
          "opens" : "center"
	  });

	  $('#endDate1').on('apply.daterangepicker', function(ev, picker) {
	      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
	  });

	  $('#endDate1').on('cancel.daterangepicker', function(ev, picker) {
	      $(this).val('');
	  });

	});
}
	
	/* Allocate Feedback JS DatePicker */
	if ($('#startDateProgram').length > 0) {
	$(function() {

  		  $('#startDateProgram').daterangepicker({
  		      autoUpdateInput: false,
  		      locale: {
  		          cancelLabel: 'Clear'
  		      },
  		      "singleDatePicker": true,
          	  "showDropdowns" : true,
                "timePicker" : true,
                "showCustomRangeLabel" : false,
                "alwaysShowCalendars" : true,
                "opens" : "center"
  		  });

  		  $('#startDateProgram').on('apply.daterangepicker', function(ev, picker) {
  		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
  		  });

  		  $('#startDateProgram').on('cancel.daterangepicker', function(ev, picker) {
  		      $(this).val('');
  		  });

  		});
	}
  	
	if ($('#endDateProgram').length > 0) {
  	$(function() {

		  $('#endDateProgram').daterangepicker({
		      autoUpdateInput: false,
		      locale: {
		          cancelLabel: 'Clear'
		      },
		      "singleDatePicker": true,
        	  "showDropdowns" : true,
              "timePicker" : true,
              "showCustomRangeLabel" : false,
              "alwaysShowCalendars" : true,
              "opens" : "center"
		  });

		  $('#endDateProgram').on('apply.daterangepicker', function(ev, picker) {
		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
		  });

		  $('#endDateProgram').on('cancel.daterangepicker', function(ev, picker) {
		      $(this).val('');
		  });

		});
	}
  	/* Allocate Feedback To Semester JS DatePicker */
  	
  		if ($('#startDateAcadSession').length > 0) {
	$(function() {

  		  $('#startDateAcadSession').daterangepicker({
  		      autoUpdateInput: false,
  		      locale: {
  		          cancelLabel: 'Clear'
  		      },
  		      "singleDatePicker": true,
          	  "showDropdowns" : true,
                "timePicker" : true,
                "showCustomRangeLabel" : false,
                "alwaysShowCalendars" : true,
                "opens" : "center"
  		  });

  		  $('#startDateAcadSession').on('apply.daterangepicker', function(ev, picker) {
  		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
  		  });

  		  $('#startDateAcadSession').on('cancel.daterangepicker', function(ev, picker) {
  		      $(this).val('');
  		  });

  		});
  		}
  	
  	
  		if ($('#endDateAcadSession').length > 0) {
  	$(function() {

		  $('#endDateAcadSession').daterangepicker({
		      autoUpdateInput: false,
		      locale: {
		          cancelLabel: 'Clear'
		      },
		      "singleDatePicker": true,
        	  "showDropdowns" : true,
              "timePicker" : true,
              "showCustomRangeLabel" : false,
              "alwaysShowCalendars" : true,
              "opens" : "center"
		  });

		  $('#endDateAcadSession').on('apply.daterangepicker', function(ev, picker) {
		      $(this).val(picker.startDate.format('YYYY-MM-DD HH:mm:ss'));
		  });

		  $('#endDateAcadSession').on('cancel.daterangepicker', function(ev, picker) {
		      $(this).val('');
		  });

		});
  		}
		</script>

<!-- CKEDITOR  -->
<script>
		$(".ckeditorClass")
			.each(
					function() {
						console.log("id--->" + ($(this).attr('id')));

						CKEDITOR
								.replace(
										$(this).attr('id')
									);

					});
		
		</script>
<script>
$('#scaledReq').click(function() {
			
			console.log('scaled req clicked');
			var scaleReq = $('#scaledReq').val();
			$('#scaledMarks').parent().toggleClass('d-none');
			$('#sameMarks').toggleClass('d-none');
			//IF Y AND N

			if (scaleReq == 'Y') {
				$('#scaledReq').val('N');
				$("#scaledMarks").prop('required', false);
			} else {
				$('#scaledReq').val('Y');
				
				$("#scaledMarks").prop('required', true);
			}

		});
		
$('#isPublishCompWise').click(function() {
	
	console.log('isPublishCompWise req clicked');
	var isPcw = $('#isPublishCompWise').val();
	
	//IF Y AND N

	if (isPcw == 'Y') {
		$('#isPublishCompWise').val('N');
		
	} else {
		$('#isPublishCompWise').val('Y');
		
	
	}

});


		</script>
		<script>
			$(document).ready(function(){
				/* ---- */
				var subAfterEndDate = $('#subAfterEndDate').val();
				if (subAfterEndDate == 'Y') {
					$('#subAfterEndDate').prop('checked', true);
				} else {
					$('#subAfterEndDate').prop('checked', false);
				}
				/* ---- */
				var submitByOneInGroup = $('#submitByOneInGroup').val();
				if (submitByOneInGroup == 'Y') {
					$('#submitByOneInGroup').prop('checked', true);
				} else {
					$('#submitByOneInGroup').prop('checked', false);
				}
				/* ---- */
				var showResult = $('#showResult').val();
				if (showResult == 'Y') {
					$('#showResult').prop('checked', true);
				} else {
					$('#showResult').prop('checked', false);
				}
				/* ---- */
				var rightGrant = $('#rightGrant').val();
				if (rightGrant == 'Y') {
					$('#rightGrant').prop('checked', true);
				} else {
					$('#rightGrant').prop('checked', false);
				}
				/* ---- */
				var sendEmailAlert = $('#sendEmailAlert').val();
				if (sendEmailAlert == 'Y') {
					$('#sendEmailAlert').prop('checked', true);
				} else {
					$('#sendEmailAlert').prop('checked', false);
				}
				/* ---- */
				var sendSmsAlert = $('#sendSmsAlert').val();
				if (sendSmsAlert == 'Y') {
					$('#sendSmsAlert').prop('checked', true);
				} else {
					$('#sendSmsAlert').prop('checked', false);
				}
				/* ---- */
				
				 /*  For Assignment Pool Start */
			    var isQuesConfigFromPool = $('#isQuesConfigFromPool').val();
				if (isQuesConfigFromPool == 'Y') {
					$('#isQuesConfigFromPool').prop('checked', true);
				} else {
					$('#isQuesConfigFromPool').prop('checked', false);
				}
				/*  For Assignment Pool End */
			});
			$('#subAfterEndDate').click(function() {
				var subAfterEndDate = $('#subAfterEndDate').val();
				if (subAfterEndDate == 'Y') {
					$('#subAfterEndDate').val('N');
				} else {
					$('#subAfterEndDate').val('Y');
				}

			});
			$('#submitByOneInGroup').click(function() {
				var submitByOneInGroup = $('#submitByOneInGroup').val();
				if (submitByOneInGroup == 'Y') {
					$('#submitByOneInGroup').val('N');
				} else {
					$('#submitByOneInGroup').val('Y');
				}

			});
			$('#showResult').click(function() {
				var showResult = $('#showResult').val();
				if (showResult == 'Y') {
					$('#showResult').val('N');
				} else {
					$('#showResult').val('Y');
				}

			});
			$('#rightGrant').click(function() {
				var rightGrant = $('#rightGrant').val();
				if (rightGrant == 'Y') {
					$('#rightGrant').val('N');
				} else {
					$('#rightGrant').val('Y');
				}

			});
			$('#sendEmailAlert').click(function() {
				var sendEmailAlert = $('#sendEmailAlert').val();
				if (sendEmailAlert == 'Y') {
					$('#sendEmailAlert').val('N');
				} else {
					$('#sendEmailAlert').val('Y');
				}

			});
			$('#sendSmsAlert').click(function() {
				var sendSmsAlert = $('#sendSmsAlert').val();
				if (sendSmsAlert == 'Y') {
					$('#sendSmsAlert').val('N');
				} else {
					$('#sendSmsAlert').val('Y');
				}

			});
			/*  For Assignment Pool Start */
			$('#isQuesConfigFromPool').click(function() {
				var isQuesConfigFromPool = $('#isQuesConfigFromPool').val();
				if (isQuesConfigFromPool == 'Y') {
					$('#isQuesConfigFromPool').val('N');
				} else {
					$('#isQuesConfigFromPool').val('Y');
				}

			});
			/*  For Assignment Pool End */
			
		</script>

</body>
</html>