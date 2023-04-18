<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
request.setCharacterEncoding("UTF-8");
HttpSession resultSession = request.getSession();

boolean ins = resultSession.getAttribute("target") != null && resultSession.getAttribute("startDt") != null && resultSession.getAttribute("endDt") != null ? true : false;

String target = null;
String startDt = null;
String endDt = null;

if(ins) {
	target = resultSession.getAttribute("target").toString();
	startDt = resultSession.getAttribute("startDt").toString();
	endDt = resultSession.getAttribute("endDt").toString();
}
%>
<html>
<head>
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport"
	content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<title>Stock - Dashboard</title>
<link
	href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css"
	rel="stylesheet" />
<link href="css/styles.css" rel="stylesheet" />
<script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"
	crossorigin="anonymous"></script>
</head>
<body>
	<nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
		<!-- Navbar Brand-->
		<a class="navbar-brand ps-3" href="#">Stock</a>
		<!-- Sidebar Toggle-->
		<button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0"
			id="sidebarToggle" href="#!">
			<i class="fas fa-bars"></i>
		</button>
		<!-- Navbar Search-->
		<form
			class="d-none d-md-inline-block form-inline me-auto ms-0 ms-md-3 my-2 my-md-0"
			action="search.it" method="post">
			<div class="input-group">
				<input class="form-control w-50" type="text"
					placeholder="회사명1,회사명2..." aria-label="Search for..."
					aria-describedby="btnNavbarSearch" name="targetNm" <%if(ins) {%>
					value="<%=target %>" <%} %> required="required" /> <input
					class="form-control w-auto" type="date" name="startDt"
					<%if(ins) {%> value="<%=startDt %>" <%} %> required="required">
				<input class="form-control w-auto" type="date" name="endDt"
					<%if(ins) {%> value="<%=endDt %>" <%} %> required="required">
				<input type="hidden" name="pageNm"
					value="<%=request.getRequestURL().toString() %>" />
				<button class="btn btn-primary" id="btnNavbarSearch" type="submit">
					<i class="fas fa-search"></i>
				</button>
			</div>
		</form>
		<button class="btn btn-primary" type="button"
			style="margin-right: 10px;" onclick="location.href='corp.it';">코드
			수동갱신</button>
	</nav>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<script src="js/scripts.js"></script>
</body>
</html>