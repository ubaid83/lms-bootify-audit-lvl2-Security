<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="sec"
	uri="http://www.springframework.org/security/tags"%>
<!DOCTYPE html>
<html>
<head>
<style>
.wrapper {
    text-align: center;
}
</style>
<title>View Questions</title>

</head>
<body>
<hr>
	<c:forEach items="${ questionList }" var="quesList"
		varStatus="status">
		<div>Q.${status.count}) [${quesList.marks} Marks] ${quesList.description}</div>
		<br> 
		<hr>
	</c:forEach>
<div class="wrapper">
<button id="downloadPage" onclick="downloadPage()">download</button>
</div>
</body>

<script type="text/javascript"
      src="https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.0/MathJax.js?config=TeX-AMS_HTML"></script>
<script>
function downloadPage() {
  window.print();
}
</script>
</html>