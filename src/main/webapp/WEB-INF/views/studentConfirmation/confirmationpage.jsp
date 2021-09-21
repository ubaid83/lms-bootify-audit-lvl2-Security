<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

    <div class="modal fade rounded-0 donotprint" id="marksheet"   data-backdrop="static" oncontextmenu="return false" onselectstart="return false" ondragstart="return false"> 
        <div class="modal-dialog modal-dialog-centered" role="document">
       
            <div class="modal-content modal-content-marksheet">
               <div class="">
       
        <button type="button" class="close text-dark" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
                <form:form method="post" action="addstudentdetails" modelAttribute="studentdetails" onsubmit="return monitorconfirm();">
                   <!--  <div class="modal-header">
                        <img src="https://images.static-collegedunia.com/public/college_data/images/logos/1506323004Logo.jpg" />
                    </div> -->
                    <div class="modal-body text-center">
                    <!-- 
                        <p>We, the Chancellor, Vice Chancellor and Members of the Board of Management of<br>
                        SVKM's Narsee Monjee Institute of Management Studies,<br>certify that</p>
                         -->
                        <span class="ms-detail">
                            <h3 class="font-weight-bold">${userBean.firstname} ${userBean.lastname}</h3>
                            <form:input path="firstname" class="ms-sname d-none f_name"  disabled="disabled" placeholder="Your Name" required="required"/>
                            <!-- <input value="agree" name="firstnamedisagr" type="radio" required="required"/> <span>Agree &#47;&nbsp;</span> -->
                            <form:radiobutton value="agree" path="firstnamedisagr"  required="required"/> <span>Agree &#47;&nbsp;</span>
                           <form:radiobutton path="firstnamedisagr" class="fnameDisagree" id="confirmed" value="disagree"/> <span>Disagree</span>
                        </span>
                        <hr />
                        <p class="mt-3">Son/Daughter of</p>
                        <div class="form-row">
                            <div class="col-md-6"> 
                          <span class="ms-detail">
                            <h6 class="font-weight-bold">${userBean.fatherName}</h6>
                           <form:input path="fathername" class="ms-sname d-none"  disabled="disabled" placeholder="Father's Name" required="required"/>
            			 <!-- <input type="radio" name="fathernamedisagr" id="none" value="agree" class="test1" required="required"/> <span>Agree &#47;&nbsp;</span> -->
                          <form:radiobutton  path="fathernamedisagr" id="none" value="agree" class="test1" required="required"/> <span>Agree &#47;&nbsp;</span>
                           <form:radiobutton path="fathernamedisagr" id="confirmed" value="disagree"/> <span>Disagree</span>
                        </span>
                            
                            </div>
                            <p class="ms-hr"></p>
                            <div class="col-md-6">
                               <span class="ms-detail">
		                            <h6 class="font-weight-bold">${userBean.motherName}</h6>
		                            <form:input path="mothername" class="ms-sname d-none"  disabled="disabled" placeholder="Mother's Name" required="required"/>
		                            <!-- <input type="radio" name="mothernamedisagr" id="none" value="agree" class="test1" required="required"/> <span>Agree &#47;&nbsp;</span> -->
		                            <form:radiobutton  path="mothernamedisagr" id="none" value="agree" class="test1" required="required"/> <span>Agree &#47;&nbsp;</span> 
		                           <form:radiobutton path="mothernamedisagr" id="confirmed" value="disagree"/> <span>Disagree</span>
                       		 </span>
                            </div>
                        </div>
                        <hr />
                        <div class="form-row">
                            <div class="col-md-6">
                            
                             <span class="ms-detail">
                             <label>Mobile Number</label>
		                            <h6 class="font-weight-bold">${userBean.mobile}</h6>
		                            <form:hidden path="mobile" value="${userBean.mobile}"/>
		                           <!--  <input type="text" class="ms-sname d-none" value="" placeholder="Phone Number"/> -->
		                         <%--  <form:input path="mobile" minlength="10" maxlength="12"  class="ms-sname d-none"  disabled="disabled" placeholder="Mobile Number" required="required"/>
		                            <input value="agree" name="mobiledisagr" id="none" type="radio" class="test1" required="required"/> <span>Agree &#47;&nbsp;</span>
		                           <form:radiobutton path="mobiledisagr" id="confirmed" value="disagree"/> <span>Disagree</span>
		                             <small class="errorMsg"></small> --%>
                       		 </span>
                           
                            </div>
                            <p class="ms-hr"></p>
                            <div class="col-md-6">
                              <span class="ms-detail">
                              <label>Email Address</label>
		                            <h6 class="font-weight-bold">${userBean.email}</h6>
		                            <form:hidden path="email" value="${userBean.email}"/>
		                           <%--  <form:input path="email" class="ms-sname d-none" pattern="[^@]+@[^@]+\.[a-zA-Z]{2,6}" disabled="disabled" placeholder="Email" required="required"/>
		                            <input type="radio" name="emaildisagr" id="none" value="agree" class="test1" required="required"/> <span>Agree &#47;&nbsp;</span>
		                           <form:radiobutton path="emaildisagr" id="confirmed" value="disagree"/> <span>Disagree</span> --%>
                       		 </span>
                            </div>
                        </div>
                        <div class="form-row">
                            <div class="col-md-6">
                            
                             <span class="ms-detail">
                             <label>Address</label>
		                            <h6 class="font-weight-bold">${userBean.address}</h6>
		                            <form:hidden path="address" value="${userBean.address}"/>
		                           
                       		 </span>
                           
                            </div>
                         </div>
    				<%-- 	<div class="container text-center">
    					<div class="row my-4">
    					<div class="col-md-2">
    					<form:label path="secquestion" for="secquestion">Security Question</form:label>
    					</div>
    					 <div class="col-md-4">
                              <span class="ms-detail"> 
									<form:select id="secquestion" path="secquestion" class="form-control rounded-0" required="required">
									<form:option value="">Security Question</form:option>
									<c:forEach var="list" items="${secquestionList}" varStatus="status">
									<form:option value="${list.secQuestions}">${list.secQuestions}</form:option>
								</c:forEach>
							</form:select>
								</span>
                            </div>
                            <div class="col-md-4">
                            <form:input path="secAnswer" class="form-control" required="required"/>
                            </div>
    					</div>
    					</div> --%>
                         <p class="mt-3">has been examined and found qualified for the academic years</p>
                      <!--  <h3 class="mt-4">Bachelor of Business Administration</h3>
                        <p class="mt-5">The said degree has been conferred on him/her at the convocation<br>
                        held in the month of July in the year xxxx</p>
                        <p class="mt-4">In testimony whereof is set the seal of the said<br>
                        Deemed-to-be University and the signature of the said Vice Chancellor.</p> -->
                       
                        <p class="text-right ms-detail">   
                            <img
							src="${pageContext.request.contextPath}/savedImages/${userBean.username}.JPG"
							onerror="this.src='<c:url value="/resources/images/img-user.png" />'"
							class="ms-student-img" title="Name of the user" alt="Name of the user" />
                            <br>
                        <%--     <form:radiobutton path="photo" id="none" value="agree" class="test1" required="required"/> <span>Agree</span> &#47;&nbsp;
                           <form:radiobutton path="photo" id="confirmed" value="disagree"/> <span>Disagree</span> --%>
                        </p>
                    </div>
                    <div class="modal-footer text-center justify-content-center">
                        <!-- <button type="submit" class="btn btn-success" onclick="myFunction()">Proceed</button> -->
                        <button type="submit"  class="btn btn-success" >Submit</button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>


<script type="text/javascript"> 
$(document).ready(function () {
	function isKeyPressed(event) {
		  var x = document.getElementById("demo");
		  if (event.ctrlKey || event.which==80) {
		    x.innerHTML = "The CTRL+P key was pressed!";
		    console.log('X-------------------------'+x)
		  } else {
		    x.innerHTML = "The CTRL key was NOT pressed!";
		  }
		}

});
/* function keydown(event) {
	  var x = document.getElementById("marksheet");
	  if (event.ctrlKey && event.which==80) {
	    x.innerHTML = "The CTRL+P key was pressed!";
	  } else {
	    x.innerHTML = "The CTRL key was NOT pressed!";
	  }
	} */
</script>
<script  type="text/javascript">
var submitCounter = 0;
function monitorconfirm() {
    submitCounter++;
    if (submitCounter < 2) {
      alert("Do you really want to submit the form");
        console.log('Submitted. Attempt: ' + submitCounter);
        return true;
    }
    console.log('Not Submitted. Attempt: ' + submitCounter);
    return false;
}
</script>

</body>
</html>
