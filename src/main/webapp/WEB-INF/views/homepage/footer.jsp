<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:if test="${webPage.footer}">
	<!-- Footer -->
	<footer class="main-footer navbar-bottom">
		<div class="container-fluid">
			<span class="copyright">All Rights Reserved © 2013-2015.</span>
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


</body>
</html>
