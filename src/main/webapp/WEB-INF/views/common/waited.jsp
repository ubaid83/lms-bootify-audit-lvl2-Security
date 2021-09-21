<jsp:include page="../common/header.jsp" />
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<body>



	<div class="table-responsive">
		<table class="table table-striped table-hover" style="font-size: 12px">

			<thead>
				<tr>
					<th>Task name</th>
					<th>Task type</th>
					<th>Task Id</th>
					<th>Task End Date</th>
					<th>Action Url</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${task}" var="entry" varStatus="loop">
					<tr>
						<td>${entry.taskName}</td>
						<td>${entry.type}</td>
						<td>${entry.id}</td>
						<td>${entry.endDate}</td>
						<td>${entry.url}</td>
					</tr>
				</c:forEach>
			</tbody>


		</table>
	</div>





	<jsp:include page="footer.jsp" />

</body>
