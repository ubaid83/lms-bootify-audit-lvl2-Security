<%@page import="com.spts.lms.beans.announcement.Announcement"%>
<%@ page import="com.spts.lms.web.utils.Utils"%>
<%@page import="java.util.*"%>
<%@page import="com.spts.lms.beans.dashboard.DashBoard"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>


<%
	List<DashBoard> courseDetailList = (List<DashBoard>) session
.getAttribute("courseDetailList");

Map<String,List<DashBoard>> dashboardListSemesterWise = (Map<String,List<DashBoard>>)
session.getAttribute("sessionWiseCourseListMap");
%>

<jsp:include page="../common/newDashboardHeader.jsp" />

<script type="text/javascript">
	var headerCourseId = null;
	function theFunction(obj) {

		return true;
	}
</script>
<div class="d-flex" id="dashboardPage">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftSidebar.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_PARENT')">
		<jsp:include page="../common/newLeftSidebarParent.jsp" />
	</sec:authorize>
	<jsp:include page="../common/rightSidebar.jsp" />

	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper">

		<jsp:include page="../common/newTopHeader.jsp" />

		<div class="container mt-5">

			<div class="row" style="padding-top: 50px;">

				<%
					List<Announcement> announcmentList = (List<Announcement>) session
											.getAttribute("announcmentList");

									if (announcmentList.size() > 0) {
										int count = 1;

										for (Announcement a : announcmentList) {
											
												String dateInWords = Utils.formatDate(
														"yyyy-mm-dd hh:mm:ss", "MMMMM dd, yyyy", a
																.getCreatedDate().toString());
				%>
				<%
					if (a.getAnnouncementType().equals("TIMETABLE")){
				%>
				<div class="col-12">
					<sec:authorize access="hasRole('ROLE_STUDENT')">

						<marquee class="w-100 p-3 timeTableMarquee font-weight-bold">Time
							table has arrived </marquee>

					</sec:authorize>
				</div>
				<%
					break; }
				                	count++;
				                		} 
										}
				%>
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">


					<!-- <a href="#">
                        <div class="alert alert-blue alert-dismissible fade show" role="alert">
                            <strong>Welcome <span id="feedUser">Kapil!</span></strong> Click me to share your feedback about the new interface?
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </a>  -->
					<sec:authorize access="hasRole('ROLE_STUDENT')">
						<jsp:include page="../common/dashboardSemester.jsp"></jsp:include>
					</sec:authorize>
					<sec:authorize access="hasRole('ROLE_PARENT')">
						<jsp:include page="../homepage/dashboardSemesterParent.jsp"></jsp:include>
					</sec:authorize>
				</div>

				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />

				

<script>
	$(document).ready(function() {
				var imgSize;
		
				//MARKSHEET JS
				
				var proceedMarksheet = <c:out value="${firstvalidationcheck}"></c:out>;
				var marksheet = "<c:out value='${uservalid}'></c:out>";
				if($("#proceed-marksheet").length > 0 && proceedMarksheet == false) {
					$("#proceed-marksheet").modal('show');
				}
				else if ($("#marksheet").length > 0 && marksheet == 'N') {
					$("#marksheet").modal('show');
				}
				
				
				$("#flUpload").change(function() {
					imgSize = this.files[0].size/1024
					readURL(this);
				});
				
				//IMAGE PREVIEW AND RESOLUTION VALIDATION
				
				/* Image Priview function Start */ 
		 
		  function readURL(input) {
			  if (input.files[0]) {
			    var reader = new FileReader();
			    reader.onload = async function(e) {
			      await $('#priviewimage').delay('slow').attr('src', e.target.result);
				  var naturalW = $('#priviewimage').prop('naturalWidth')
				  var naturalH = $('#priviewimage').prop('naturalHeight')
					
				  console.log(naturalW)
				  console.log(naturalH)
				  console.log(imgSize)
				  if(naturalW < 157 || naturalW > 197 || naturalH < 197 || naturalH > 246 || imgSize > 150) {
					$('.imgAlert').html('Image not accepted: <br> Highest resolution: 197 x 246 <br> Lowest resolution: 157 x 197 <br> Max file size: 150kb').addClass('text-danger')
					$('#flUpload').val('')
				  } else {
					  $('.imgAlert').html('Image accepted.').removeClass('text-danger').addClass('text-success')
				  }
			    }
				 reader.readAsDataURL(input.files[0]);
				
				
			  }
			}
			

			
		 
			 /* Image Priview function End */ 

				
		
		 /* Image Priview function Start */ 
		 
		/*  function readURL(input) {
			  if (input.files && input.files[0]) {
			    var reader = new FileReader();
			    
			    reader.onload = function(e) {
			      $('#priviewimage').attr('src', e.target.result);
			    }
			    
			    reader.readAsDataURL(input.files[0]);
			  }
			}

			$("#flUpload").change(function() {
			  readURL(this);
			}); */
		 
			 /* Image Priview function End */ 

		// Making only one radio clickable

		var fname = $('.fnameDisagree').val();
		var f_name = $('.f_name');
		if(fname ===!null){
			alert(f_name);
		}

		  $('#marksheet .ms-detail input[type=radio]').click(function(){
		      console.log($(this).val())
		      if($(this).val() == 'agree') {
		            $(this).parent().find($('input[value=disagree]')).prop('checked', false)
		          if($(this).parent().find('.ms-sname').length > 0){
		              $(this).parent().find('h3').show()
		              $(this).parent().find('h6').show()
		              $(this).parent().find('.ms-sname').addClass('d-none').removeClass('d-block m-auto').attr('required', false)
		          }
		      } else {
		            $(this).parent().find($('input[value=agree]')).prop('checked', false)
		          if($(this).parent().find('.ms-sname').length > 0){
		              $(this).parent().find('h3').hide()
		                $(this).parent().find('h6').hide()
		              $(this).parent().find('.ms-sname').removeClass('d-none').addClass('d-block m-auto').attr('required', true)
		          }
		      }
		  })
		  
		function validate(form) {

		    // validation code here ...


		    if(!valid) {
		        alert('Please fill correct information!');
		        return false;
		    }
		    else {
		        return confirm('Do you really want to submit the form?');
		    }
		}
		
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

				var mapValue = $('#mapSize').val();
				console.log('map value--->' + mapValue);

				$('[id^=flip]').each(function() {
					var flipId = $(this).attr('id');

					$('#' + flipId).click(function() {
						console.log('flip entered');

						var counter = flipId.split("flip");
						var array = JSON.parse("[1" + counter + "]");

						console.log('count--->' + array[1]);
						$("#dashboardData" + array[1]).slideToggle("slow");
					});
				});
			});

	
	
	
	
						$("#result")
								.click(
										function() {
											console
													.log("calling ajax.111");
											//$(this).css('color', 'black');
											var username = $(this).attr("username");
											var announcementId =$(this).attr("announcement_id");
											console.log('úsername'+username+'id'+announcementId);
	
											$
													.ajax({
														type : 'GET',
														url : '${pageContext.request.contextPath}/timetableResult?'
																+'username='+username & 'announcement_id=' + announcement_id +'',
														success : function(data) {

															 $('#result').html(data.notification);
															   if(data.unseen_notification > 0)
															   {
															    $('#count').html(data.unseen_notification);
															   }

														}

													});

										});
					</script>
 <script>
$(document).ready(function() {
	$('#selectSem').trigger('change');
});
</script> 
	
					
					