<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html lang="en">

<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
 -->
 
<%-- <link href="<c:url value="/resources/css/bootstrap.min.css" />"
	rel="stylesheet"> --%>
    <title>Hello, world!</title>

    <style>
        #marksheet {
            padding-left: 0 !important;
            z-index: 9999;
            
        }

        #marksheet .modal-header {
            justify-content: center !important;
        }

        input[type="text"] {
            text-align: center;
            border-style: none;
            border-bottom: 2px solid;
        }

        input[type="text"]:focus {
            outline: none;
        }

        .ms-student-img {
            width: 150px;
        }

        .ms-signature-img {
            width: 200px;
        }

        .f-25 {
            font-size: 25px;
        }
        .ms-hr {
                border-bottom: 1px solid #e5e5e5;
    width: 100%;
    margin-top: 20px;
            display: none;
        }
        
        
         @media (max-width: 767px) {
            .ms-hr {
                display: block;
            }
        }



        @media (min-width: 576px) {
            #marksheet .modal-dialog {
                max-width: 80% !important;
                margin: 1.75rem auto;
            }
        }

    </style>

</head>

<body>

    <!-- Modal -->
    <div class="modal fade rounded-0" id="marksheet"   data-backdrop="static">
        <div class="modal-dialog modal-dialog-centered" role="document">
            <div class="modal-content">
                <form:form method="post" action="addstudentdetails" modelAttribute="studentdetails" onsubmit="return confirm('Do you really want to submit the form?');">
                    <div class="modal-header">
                        <img src="https://images.static-collegedunia.com/public/college_data/images/logos/1506323004Logo.jpg" />
                    </div>
                    <div class="modal-body text-center">
                        <p>We, the Chancellor, Vice Chancellor and Members of the Board of Management of<br>SVKM's Narsee Monjee Institute of Management Studies,<br>certify that</p>
                        <span class="ms-detail">
                            <h3 class="font-weight-bold">${userBean.firstname} ${userBean.lastname}</h3>
                           <!--  <input type="text" class="ms-sname d-none" value="" placeholder="Your Name"/> -->
                            <form:input path="firstname" class="ms-sname d-none f_name"  disabled="disabled" placeholder="Your Name" required="required"/>
                            <input value="agree" name="firstnamedisagr" type="radio" required="required"/> <span>Agree &#47;&nbsp;</span>
                           <form:radiobutton path="firstnamedisagr" class="fnameDisagree" id="confirmed" value="disagree"/> <span>Disagree</span>
                        </span>
                        <hr />
                        <p class="mt-3">Son/Daughter of</p>
                        <div class="form-row">
                            <div class="col-md-6">
                            
                          <span class="ms-detail">
                            <h6 class="font-weight-bold">${userBean.fatherName}</h6>
                           <form:input path="fathername" class="ms-sname d-none"  disabled="disabled" placeholder="Father's Name" required="required"/>
                         
                          <!--   <input value="agree" type="radio" /> <span>Agree &#47;&nbsp;</span> -->
            			 <input type="radio" name="fathernamedisagr" id="none" value="agree" class="test1" required="required"/> <span>Agree &#47;&nbsp;</span>
                           <form:radiobutton path="fathernamedisagr" id="confirmed" value="disagree"/> <span>Disagree</span>
                        </span>
                            
                            </div>
                            <p class="ms-hr"></p>
                            <div class="col-md-6">
                               <span class="ms-detail">
		                            <h6 class="font-weight-bold">${userBean.motherName}</h6>
		                           
		                            <form:input path="mothername" class="ms-sname d-none"  disabled="disabled" placeholder="Mother's Name" required="required"/>
		                            <!-- <input value="agree" type="radio" /> <span>Agree &#47;&nbsp;</span> -->
		                            <input type="radio" name="mothernamedisagr" id="none" value="agree" class="test1" required="required"/> <span>Agree &#47;&nbsp;</span> 
		                           <!--  <input value="disagree" type="radio" /><span>Disagree</span> -->
		                           <form:radiobutton path="mothernamedisagr" id="confirmed" value="disagree"/> <span>Disagree</span>
                       		 </span>
                            </div>
                        </div>
                        <hr />
                        <div class="form-row">
                            <div class="col-md-6">
                            
                             <span class="ms-detail">
		                            <h6 class="font-weight-bold">${userBean.mobile}</h6>
		                           <!--  <input type="text" class="ms-sname d-none" value="" placeholder="Phone Number"/> -->
		                          <form:input path="mobile" minlength="10" maxlength="12"  class="ms-sname d-none"  disabled="disabled" placeholder="Mobile Number" required="required"/>
		                            <input value="agree" name="mobiledisagr" id="none" type="radio" class="test1" required="required"/> <span>Agree &#47;&nbsp;</span>
		                           <!--  <input value="disagree" type="radio" /><span>Disagree</span> -->
		                           <form:radiobutton path="mobiledisagr" id="confirmed" value="disagree"/> <span>Disagree</span>
		                             <small class="errorMsg"></small>
                       		 </span>
                           
                            </div>
                            <p class="ms-hr"></p>
                            <div class="col-md-6">
                              <span class="ms-detail">
		                            <h6 class="font-weight-bold">${userBean.email}</h6>
		                            <form:input path="email" class="ms-sname d-none" pattern="[^@]+@[^@]+\.[a-zA-Z]{2,6}" disabled="disabled" placeholder="Email" required="required"/>
		                            <input type="radio" name="emaildisagr" id="none" value="agree" class="test1" required="required"/> <span>Agree &#47;&nbsp;</span>
		                          
		                           <form:radiobutton path="emaildisagr" id="confirmed" value="disagree"/> <span>Disagree</span>
                       		 </span>
                          
                            </div>
                            
                          
                            
                        </div>
    					<div class="container text-center">
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
    					</div>

                        <p class="mt-3">has been examined and found qualified for the three years</p>
                        <h3 class="mt-4">Bachelor of Business Administration</h3>

                        <p class="mt-5">The said degree has been conferred on him/her at the convocation<br>held in the month of July in the year xxxx</p>
                        <p class="mt-4">In testimony whereof is set the seal of the said<br>Deemed-to-be University and the signature of the said Vice Chancellor.</p>
                        <p class="text-right ms-detail">
                          
                            <img
							src="${pageContext.request.contextPath}/savedImages/${userBean.username}.JPG"
							onerror="this.src='<c:url value="/resources/images/img-user.png" />'"
							class="user-ico ms-student-img" title="Name of the user" alt="Name of the user" />
                            <br>
                            
                            <form:radiobutton path="photo" id="none" value="agree" class="test1" required="required"/> <span>Agree</span> &#47;&nbsp;
                           <form:radiobutton path="photo" id="confirmed" value="disagree"/> <span>Disagree</span>
                        </p>
                    </div>
                    <div class="modal-footer text-center justify-content-center">
                        <!-- <button type="submit" class="btn btn-success" onclick="myFunction()">Proceed</button> -->
                        <button type="submit"  class="btn btn-success" >Proceed</button>
                    </div>
                </form:form>
            </div>
        </div>
    </div>



 
 <script src="<c:url value="/resources/js/jquery.min.js" />"></script>
<!-- popper -->
<script src="<c:url value="/resources/js/popper.min.js" />"
	type="text/javascript"></script>

<script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
</body>

<script>
$('#marksheet').modal('show');
//Input width
var inWidth = 0
$("input[type='text']").keyup(function(){
str = $(this).val()
if(str != 0) {
if($(this).hasClass('ms-sname')){
inWidth = 20 + str.length * 25
} else {
inWidth = 20 + str.length * 10
}
console.log(inWidth)
$(this).css("width", inWidth)
} else {
$(this).css("width", 172)
}
})

//Aggre by default
 /*  $(".ms-detail input[value='agree']").each(function(){
	$(this).prop('checked', true)
 	if(input[value='agree']){
		$(this).prop('checked', true)
	}else
		{
		$(this).prop('unchecked', false)
		} 
})  */ 

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
              $(this).parent().find('.ms-sname').addClass('d-none').removeClass('d-block m-auto').attr('required', true)
          }
      } else {
            $(this).parent().find($('input[value=agree]')).prop('checked', false)
          if($(this).parent().find('.ms-sname').length > 0){
              $(this).parent().find('h3').hide()
                $(this).parent().find('h6').hide()
              $(this).parent().find('.ms-sname').removeClass('d-none').addClass('d-block m-auto')
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
  

/* var fnameDisagree= $('.fnameDisagree').val();

var f_name = $('.f_name').val();

console.log('fnameDisagree----------'+fnameDisagree);

if(fnameDisagree == 'disagree')
	{
	if(f_name != null)
		{
		console.log("f_name--------->"+f_name);
		//alert('Nul');
		}
	}
 */
/* $(function() {
        $(this).bind("contextmenu", function(e) {
            e.preventDefault();
        });
    }); 
    
$(document).keydown(function (event) {
    if (event.keyCode == 123) { // Prevent F12
        return false;
    } else if (event.ctrlKey && event.shiftKey && event.keyCode == 73) { // Prevent Ctrl+Shift+I        
        return false;
    }
}); */
</script>

</html>
