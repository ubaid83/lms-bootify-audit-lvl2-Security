<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>

<%--  <c:if test="${webPage.footer}"> --%>
    <div class="footer">
        <footer >
            <div class="col-md-6">
                &copy;2017 NMIMS. All Rights Reserved.<br>V.L.Mehta Road, Vile Parle (W), Mumbai, Maharashtra - 400056
            </div>
            <div class="col-md-3">
                <a href="mailto:portal_app_team@svkm.ac.in">portal_app_team@svkm.ac.in</a><br>022-42199993
            </div>
            <!--  <div class="col-md-3 text-right">
                        Follow Us:
                        <a href="#"><i class="fa fa-facebook"></i></a>
                        <a href="#"><i class="fa fa-twitter"></i></a>
                        <a href="#"><i class="fa fa-google-plus"></i></a>
                        <a href="#"><i class="fa fa-youtube"></i></a>
                    </div> -->
        </footer>
    </div>


<%--   </c:if> --%>

<script src="<c:url value="/resources/js/jquery.min.js" />"></script>
<!-- popper -->
<script src="<c:url value="/resources/js/popper.min.js" />"
	type="text/javascript"></script>

<script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.richtext.min.js" />"></script>
<script src="<c:url value="/resources/js/vendor/bootstrap-editable.js" />"></script>
<script src="<c:url value="/resources/js/moment.min.js" />"></script> 
 <script src="<c:url value="/resources/js/daterangepicker.min.js" />"></script> 

<script src="<c:url value="/resources/js/jquery.dataTables.min.js" />"></script>
<script
	src="<c:url value="/resources/js/dataTables.bootstrap4.min.js" />"></script>

<script type="text/javascript"
	src="<c:url value="/resources/js/Chart.min.js"/>"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/timecircles.js" />"></script>
<script>
	var myContextPath = "${pageContext.request.contextPath}"
</script>
<script src="<c:url value="/resources/js/sweetalert.min.js" />"></script>
<script type="text/javascript"
	src="<c:url value="/resources/js/style.js" />"></script>
	
 <script src="<c:url value="/resources/ckeditor/ckeditor.js" />"></script>  

</body>
</html>