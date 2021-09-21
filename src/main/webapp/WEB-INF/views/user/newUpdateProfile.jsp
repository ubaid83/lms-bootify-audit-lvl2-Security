<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<jsp:include page="../common/newDashboardHeader.jsp" />
<div class="d-flex" id="prilfeDetailsPage">
	<jsp:include page="../common/newLeftNavbar.jsp" />
	<sec:authorize access="hasRole('ROLE_STUDENT')">
		<jsp:include page="../common/newLeftSidebar.jsp" />
	</sec:authorize>
	<sec:authorize access="hasRole('ROLE_PARENT')">
		<%-- <jsp:include page="../common/ newLeftSidebarParent.jsp" /> --%>
	</sec:authorize>
	<jsp:include page="../common/rightSidebar.jsp" />


	<!-- DASHBOARD BODY STARTS HERE -->

	<div class="container-fluid m-0 p-0 dashboardWraper assignmentW">

		<jsp:include page="../common/newTopHeader.jsp" />

		<div class="container mt-5">
			<div class="row">
				<!-- SEMESTER CONTENT -->
				<div
					class="col-lg-9 col-md-9 col-sm-12 dashboardBody ml-auto mr-auto">
					<jsp:include page="../common/alert.jsp" />
					<div class="bg-white pb-5">
						<nav class="" aria-label="breadcrumb">
							<ol class="breadcrumb d-flex">
								<li class="breadcrumb-item"><a
									href="${pageContext.request.contextPath}/homepage">Dashboard</a></li>
								<li class="breadcrumb-item active" aria-current="page">Update Profile
								</li>
							</ol>

						</nav>

						<form:form action="updateProfile" id="user" method="post"
							modelAttribute="user"  enctype="multipart/form-data">


							<div class="row ml-2 mr-2">
								<div class="col-md-4 col-sm-6 col-xs-12 column">
									<div class="form-group">
										<label for="abbr">First Name:</label> ${user.firstname }
									</div>
								</div>
								<div class="col-md-4 col-sm-6 col-xs-12 column">
									<div class="form-group">
										<label for="abbr">Last Name:</label> ${user.lastname }
									</div>
								</div>
							</div>
							<div class="row ml-2 mr-2">
								<div class="col-md-4 col-sm-6 col-xs-12 column">
									<div class="form-group">
										<label for="email">Email <span style="color: red">*</span></label>
										<form:input path="email" type="email"
											placeholder="Enter Email ID" class="form-control"
											required="required" />
									</div>
								</div>
								<div class="col-md-4 col-sm-6 col-xs-12 column">

									<div class="form-group">
										<label for="mobile">Mobile <span style="color: red">*</span></label>
										<form:input path="mobile" type="text" maxlength="10" onkeypress="return event.charCode >= 48 && event.charCode <= 57"
											placeholder="Enter Mobile" class="form-control"
											required="required" />
									</div>
								</div>
								<div class="col-md-4 col-sm-6 col-xs-12 column">

									<div class="form-group">
										<label for="mobile">Address <span style="color: red">*</span></label>
										<form:textarea path="address" 
											placeholder="Enter Address" class="form-control"
											required="required" />
									</div>
								</div>
								<div class="clearfix"></div>
							</div>
							<div class="row ml-2 mr-2">
								<div class="col-md-4 col-sm-6 col-xs-12 column">
									<div class="form-group">
				                      <img
											src="${pageContext.request.contextPath}/savedImages/${userBean.username}.jpg"
											onerror="this.src='<c:url value="/resources/images/img-user.png" />'"
											class="ms-student-img" title="${userBean.firstname}" alt="Name of the user" id="priviewimage"/> 
				                    </div>
								</div>
								<div class="col-md-4 col-sm-6 col-xs-12 column">
				                  <input type="file" name="file" class="file fileuploadMasterButton" id="flUpload" accept=".jpg"/>
				                  
				                  <p class="imgAlert small"></p>
				                  <span><c:out value="${fileuploaderror}"></c:out></span>
				                </div>
							</div>
							<div class="row ml-2 mr-2">
							<strong>Note:- If "Image accepted" message is displayed then only your image will be uploaded on portal.</strong>
							</div>
							<div class="row ml-2 mr-2">
								<h5>Photograph Specification:</h5>
			                    <ol>
			                    	<li>Height 25  mm and Width 20 mm (file size not to exceed 150 kb).</li>
			                    	<li>A very clear colour image.</li>
			                    	<li>Taken within the last 6 months to reflect current appearance.</li>
			                    	<li>Taken in front of a plain white or off-white background.</li>
			                    	<li>Taken in full-face view directly facing the camera (No side view please).</li>
			                    	<li>With a neutral facial expression and both eyes open.</li>
			                    	<li>Taken in formal wear (avoid T shirts etc.)</li>
			                    	<li>Photo taken in a closed environment (not in open area).</li>
			                    	<li>Do not wear a hat or anything that covers head, unless worn daily for religious purpose.</li>
			                    	<li>Your seeing this screen to enable your new photo.<br>Please note that the photograph which will be uploaded on degree certificate</li>
			                    	
			
			                    </ol>
							</div>
							<div class="row ml-2 mr-2">
								<div class="col-md-4 col-sm-6 col-xs-12 column">
									<div class="form-group">
										<button id="submit" class="btn btn-large btn-primary ml-2"
											formaction="updateProfile">Update</button>
										<button id="cancel" class="btn btn-danger ml-2"
											formaction="homepage" formnovalidate="formnovalidate">Cancel</button>
									</div>
								</div>

							</div>



						</form:form>
					</div>




				</div>



				<!-- SIDEBAR START -->
				<jsp:include page="../common/newSidebar.jsp" />
				<!-- SIDEBAR END -->
				<jsp:include page="../common/footer.jsp" />
<script>
$(document).ready(function() {
	var imgSize;
$("#flUpload").change(function() {
		imgSize = this.files[0].size/1024
		readURL(this);
	});
	
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

	});
</script>
