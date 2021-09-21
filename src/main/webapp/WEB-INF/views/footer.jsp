<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${webPage.footer}">
	<!-- Footer -->
	<footer class="main-footer navbar-bottom"  >
		<div class="container-fluid" >
		
			<span class="copyright">© 2017 NMIMS. All Rights Reserved<br/>V.L Mehta Road, Vile Parle(W),Mumbai , Maharashtra-400056.</span>
			<span class="copyright" style="margin-left:220px;">  ngasce@nmims.edu <br />1800-1025-136(Toll Free)</span>
			<span class="copyright" style=" margin-left:75%;margin-top:-60px;font-size:30px;">Follow Us <a href="#" ><img src="http://www.atelier-mo.infini.fr/IMG/png/facebook.png" height="30%"
								
									style="border-radius: 10px; height: 32px; width: 28px; padding: 1px;"/></a>
		
<a href="#"><img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShFWlPfmvJW6qI3yBr4hX-vkG5Rs_qKaQhLf2eVNiEdeIN6KdB_Q" 
height="30%"
								
									style="border-radius: 10px; height: 32px; width: 28px; padding: 1px;"/></a>
<a href="#"><img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS52eYbY4EQx2QcFS-U7Drs441oqvAYnKjpsixbZjGcrupqYFrl"
height="30%"
								
									style="border-radius: 10px; height: 32px; width: 28px; padding: 1px;" /></a>
<a href="#"><img src="https://siteassets.pagecloud.com/journeyorl/images/youtube-4-xxl-ID-1f3046b1-5dba-4f08-81ab-06b00d9b3333.png?nocache=a5788908-2596-423e-e6e3-1122c4249110" height="30%"
								
									style="border-radius: 10px; height: 32px; width: 28px; padding: 1px;"/>
 </a></span>
			
		</div>
	</footer>
</c:if>
<!-- data-main attribute tells require.js to load
    js/main.js after require.js loads. -->
<script data-main="resources/js/main"
	src="resources/js/vendor/require.js"></script>
<script type="text/javascript">
	// Load the main config file before proceeding
	require([ "main" ], function() {
		// Load the common jquery and bootstrap files before proceeding
		require([ "bootstrap-toggle" ], function() {
			<c:if test="${webPage.leftNav}">
			require([ "leftNav" ], function() {

			})
			</c:if>
			// Load webpage specific js files
			<c:if test="${webPage.js}">
			require([ "${webPage.name}" ], function() {

			})
			</c:if>

		});
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

<!-- Include IE8 JS for Froala wysiwyg editor. -->
<!--[if lt IE 9]>
	    <script src="/resources/js/vendor/froala/froala_editor_ie8.min.js"></script>
	<![endif]-->

<!-- jQuery Editable element Plugin -->
<!-- Included directly inside evaluateAssignment.js -->
<script src="<c:url value="/resources/js/vendor/bootstrap-editable.js" />"></script>

</body>
</html>
