<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>



	 <c:if test="${webPage.footer}">
    <div class="footer">
        <footer>
            <div class="col-md-6">
                &copy;2017 NMIMS. All Rights Reserved.<br>V.L.Mehta Road, Vile Parle (W), Mumbai, Maharashtra - 400056
            </div>
            <div class="col-md-3">
                <a href="mailto:portal@svkmmumbai.onmicrosoft.com">portal@svkmmumbai.onmicrosoft.com</a><br>1800-1025-136 ( Toll Free)
            </div>
             <!-- <div class="col-md-3 text-right">
                        Follow Us:
                        <a href="#"><i class="fa fa-facebook"></i></a>
                        <a href="#"><i class="fa fa-twitter"></i></a>
                        <a href="#"><i class="fa fa-google-plus"></i></a>
                        <a href="#"><i class="fa fa-youtube"></i></a>
                    </div> -->
        </footer>
    </div>
    </c:if>
	
	
	
	
	
	<!-- <script src="//ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script> -->
	<!-- <script
      src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"></script> -->

<!-- jQuery -->
<script src="<c:url value="/resources/js/jquery.min.js" />" type="text/javascript" ></script>
<!-- Bootstrap -->
<script src="<c:url value="/resources/js/bootstrap.min.js" />" type="text/javascript" ></script>
<!-- FastClick -->
<script src="<c:url value="/resources/js/fastclick.js" />" type="text/javascript" ></script>

<!-- Toggle.js -->
<script src="<c:url value="/resources/js/toggle.js" />" type="text/javascript" ></script>

<!-- Balance Height.js -->
<script type="text/javascript" src="<c:url value="/resources/js/jquery.balance.js" />" ></script>
<script type="text/javascript" src="<c:url value="/resources/js/jquery.fancybox.min.js" />" ></script>


<script type="text/javascript" src="<c:url value="/resources/js/wizard.js" />" ></script>



 <!-- Timer circles Style -->
  <script type="text/javascript" src="<c:url value="/resources/js/timecircles.js" />"></script>
   <script type="text/javascript" src="<c:url value="/resources/js/custom.js" />" ></script>
  <script type="text/javascript" src="<c:url value="/resources/js/custom-timecircles.js" />" ></script>
 
  
    <!-- NProgress -->
    <script src="<c:url value="/resources/vendors/nprogress/nprogress.js" />"></script>
    <!-- FullCalendar -->
    <script src="<c:url value="/resources/vendors/moment/min/moment.min.js" />"></script>
    <script src="<c:url value="/resources/vendors/fullcalendar/dist/fullcalendar.min.js" />"></script>
    


<%-- <script src="<c:url value="/resources/js/studentFeedback.js" />" type="text/javascript" ></script>
 --%>
<script>
$("form").submit(function () {
	
    var clickedForm = $(this); // Select Form
	var form = $(this).attr('id');
    
   // var elementArr = document.getElementById(form).elements;
   
    var elementArr = document.getElementsByTagName('input');
    console.log('elementArr length:'+elementArr.length);
    for(var i = 0; i < elementArr.length; i++){
    	
    	if(elementArr[i].type == 'text'){
    		var value = elementArr[i].value;
    		if(value.includes('<html>')){
    			alert('HTML Script is not allowed in text boxes');
    		      return false;
    		}
    	}
    		
      }

  });

</script>
<script>
	var csrf_token = $('meta[name="csrf-token"]').attr('content');
	$.ajaxPrefilter(function(options, originalOptions, jqXHR) {
		if (options.type.toLowerCase() === "post") {
			// initialize `data` to empty string if it does not exist
			options.data = options.data || "";

			// add leading ampersand if `data` is non-empty
			options.data += options.data ? "&" : "";

			// add _token entry
			options.data += "_token=" + csrf_token;
		}
	});
</script>



  