<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="modal fade masterData" id="proceed-marksheet"  data-backdrop="static" oncontextmenu="return false" onselectstart="return false" ondragstart="return false">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content my-5" >
            <jsp:include page="../common/alert.jsp" />
            <div class="modal-header">
        <p class="modal-title alert-success pl-3 pb-2">Dear Student,<br>You are required to check, confirm or update your credentials 
        ( email id, phone number) along with your photograph. Please note that the same photograph 
        ( as visible on the screen) will be printed on your degree certificate.</p>
        <button type="button" class="close text-dark" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
             <form:form method="post" action="updateUserProfileForMaster" modelAttribute="userprofile" enctype="multipart/form-data" onsubmit="return monitor();" >
                    <div class="modal-body text-center">
                        <div class="form-row my-5">
                            <div class="col-md-6">
                              <span class="ms-detail">
                              <h6>Email Address:</h6>
                            <%--   <h6>${userBean.email}</h6> --%>
		                            <%-- <h6 class="font-weight-bold">${userBean.email}</h6> --%>
		                            <form:input path="email"  onkeyup="ValidateEmail();"  id="email_address" placeholder="Enter Email Address"
																 class="form-control m-auto" pattern="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
																required="required" />
															 <span id="lblError" style="color: red"></span>	<!-- <span id="email-error"></span> -->
                       		 </span>
                            </div>
                             <div class="col-md-6">
                             <span class="ms-detail">
		                            <h6>Mobile No: </h6>
		                           <%--  <h6>${userBean.mobile}</h6> --%>
		                            <form:input path="mobile" minlength="10" maxlength="10" id="mobile_number" 
															 onKeyDown="if(this.value.length==10 && event.keyCode!=8) return false;" onkeypress="if ( isNaN(this.value + String.fromCharCode(event.keyCode) )) return false;" 	placeholder="Enter Mobile" class="form-control m-auto"
																required="required"/>
                       		 </span>
                            </div>
                            <p class="ms-hr"></p>
                        </div>
    					<div class="container text-center">
    					<div class="row my-4">
    					 <div class="col-md-6">
                              <span class="ms-detail">
									<form:select id="secquestion" path="secquestion" class="form-control rounded-0" required="required">
									<form:option value="">Security Question</form:option>
									<c:forEach var="list" items="${secquestionList}" varStatus="status">
									<form:option value="${list}">${list}</form:option>
								</c:forEach>
							</form:select>
								</span>
                            </div>
                            <div class="col-md-6">
                            <form:input path="secAnswer" id="sec_Ans" class="form-control " placeholder="Enter Your Answer" required="required"/>
                            </div>
    					</div>
    					</div>
    					<div class="form-row my-5">
                            <div class="col-md-6">
                              <span class="ms-detail">
                              <h6>Address:</h6>
                            <%--   <h6>${userBean.email}</h6> --%>
		                            <%-- <h6 class="font-weight-bold">${userBean.email}</h6> --%>
		                            <form:textarea path="address"  id="address" placeholder="Enter Address"
																 class="form-control m-auto"
																required="required" />
															 <span id="lblError" style="color: red"></span>	<!-- <span id="email-error"></span> -->
                       		 </span>
                            </div>
                            <p class="ms-hr"></p>
                        </div>
    					<!-- Select Religin Caste -->
    					<%-- <div class="container text-center">
    					<div class="row my-2">
    					 <div class="col-md-12 ">
                              <span class="ms-detail">
									<form:select id="religion" path="religion" class="form-control rounded-0" required="required">
									<form:option value="">Select Caste</form:option>
									<c:forEach var="list" items="${religionList}" varStatus="status">
									<form:option value="${list.religion}">${list.religion}</form:option>
								</c:forEach>
							</form:select>
								</span>
                            </div>
    					</div>
    					</div> --%>
    					
    					
    					<%-- <c:forEach items="${schoolListMaster}" var="list">
    					
    					<c:if test="${fn:contains(list, app)}"> --%>
    					<%-- 	<div class="container text-center">
    					<div class="row my-2">
    					 <div class="col-md-12 ">
                              <span class="ms-detail">
									<form:select id="NAD" path="NAD" class="form-control rounded-0" required="required">
							<form:checkbox path="nad" value="Y" /> NAD (National Academic Depository ) 
							</form:select>
								</span>
                            </div>
    					</div>
    					</div>  --%>
    					
    							<div class="container text-center">
    					<div class="row my-2">
    					 <div class="col-md-12 ">
                            
									<%-- <form:select id="NAD" path="NAD" class="form-control rounded-0" required="required"> --%>
							<%-- <form:checkbox path="nad" value="Y" /> NAD (National Academic Depository )  --%>
							NAD (National Academic Depository )
							<form:radiobutton path="nad" value="Y" required="required" />Yes
							<form:radiobutton path="nad" value="N"/>No
							<%-- </form:select> --%>
								
                            </div>
    					</div>
    					</div> 
    					<%-- </c:if>
    					</c:forEach> --%>
    					
    				<!-- Select NAD(National Academic Depository ) Caste -->
    			<%-- 	<c:if test = "${fn:contains(schoolListMaster, app)}">		
    					<div class="container text-center">
    					<div class="row my-2">
    					 <div class="col-md-12 ">
                              <span class="ms-detail">
									<form:select id="NAD" path="NAD" class="form-control rounded-0" required="required">
							<form:checkbox path="NAD" value="NAD" required="required"/> NAD (National Academic Depository ) 
							</form:select>
								</span>
                            </div>
    					</div>
    					</div> 
    				</c:if> --%>
    				
    					
    					<%-- <c:if test = "${fn:contains(schoolListMaster, app)}">
     						<p>Found TEST string<p>
  						</c:if> --%>
                   <div class="text-left">
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
                  <div class="col-md-12">
                                      <div id="thumbwrapformaster">
<a class="thumbformaster  font-weight-bold" href="#">View Sample Image<span style="width:170px;height:200px;">
<img src="${pageContext.request.contextPath}/resources/images/dummy-man-570x570.jpg" alt=""></span></a>

</div>
                    <div class="text-right">
                      <img
							src="${pageContext.request.contextPath}/savedImages/${userBean.username}.jpg"
							onerror="this.src='<c:url value="/resources/images/img-user.png" />'"
							class="ms-student-img" title="${userBean.firstname}" alt="Name of the user" id="priviewimage" required="required"/> 
                    </div>
                  </div>
                  <div class="col-md-12 text-right">
                  <input type="file" name="file" class="file fileuploadMasterButton" id="flUpload" required="required"  accept=".jpg"/>
                  
                  <p class="imgAlert small"></p>
                  <span><c:out value="${fileuploaderror}"></c:out></span>
                  </div>
                    </div>
                    <div class="modal-footer text-center justify-content-center">
                        <button id="agreeProceed" type="submit"  class="btn btn-success" >Agree and Proceed</button>
                   <!--  <Button id="agreeProceed"  formaction="updateUserProfileForMaster" onclick="this.disabled=true;this.value='Submitting...'; this.form.submit();" class="btn btn-success">Agree and Proceed</Button> -->
                    </div>
               </form:form>    
            </div>
        </div>
    </div>

  
<script>
function ValidateEmail() {
    var email = document.getElementById("email_address").value;
    var lblError = document.getElementById("lblError");
    lblError.innerHTML = "";
    var expr = /^([0-9a-zA-Z]([-_\\.]*[0-9a-zA-Z]+)*)@([0-9a-zA-Z]([-_\\.]*[0-9a-zA-Z]+)*)[\\.]([a-zA-Z]{2,9})$/;

    if (!expr.test(email) && email.length > 0) {
        lblError.innerHTML = "Invalid email address.";
    }
   
    
}

var submitCounter = 0;
function monitor() {
    submitCounter++;
    if (submitCounter < 2) {
        console.log('Submitted. Attempt: ' + submitCounter);
        return true;
    }
    console.log('Not Submitted. Attempt: ' + submitCounter);
    return false;
}

/* 
 $('#agreeProceed').on('click', function(){
    $('#disable').attr("disabled", true);
});  */



/* function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        var errorMsg = document.getElementById("errorMsg");
        errorMsg.style.display = "block";
        document.getElementById("errorMsg").innerHTML = "  Please enter only Numbers.  ";
        return false;
    }
   
    return true;
}  */
</script>

