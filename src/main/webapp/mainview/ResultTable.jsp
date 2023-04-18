<%@page import="java.util.StringTokenizer"%>
<%@page import="bean.Resultbean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
HttpSession resultSession = request.getSession();

boolean ins = resultSession.getAttribute("result") != null && resultSession.getAttribute("target") != null
		&& resultSession.getAttribute("leng") != null ? true : false;

ArrayList<Resultbean> arr = (ArrayList<Resultbean>) resultSession.getAttribute("result");
StringTokenizer target = null;
StringTokenizer leng = null;

if (ins) {
	target = new StringTokenizer(resultSession.getAttribute("target").toString(), ",");
	leng = new StringTokenizer(resultSession.getAttribute("leng").toString(), ",");
}
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Stock - Dashboard</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<link href="./css/bootstrap.css" rel="stylesheet">
</head>
<jsp:include page="./Sidepage/Header.jsp" />
<body>
	<div id="layoutSidenav">
		<jsp:include page="./Sidepage/Sidebar.jsp" />
		<div id="layoutSidenav_content">
			<main>
				<div class="container-fluid px-4">
					<h1 class="mt-4">검색결과(표)</h1>
					<div class="pb-2">
						<%
						if (ins) {
						%>
						<select id="selectbox" class="form-select form-select-sm">
							<option value="0">회사선택</option>
							<%
							while (target.hasMoreTokens()) {
							%>
							<option value="<%=leng.nextToken()%>"><%=target.nextToken()%></option>
							<%
							}
							%>
						</select>
						<%
						}
						%>
					</div>
					<div class="card mb-4">
						<div class="card-header">
							<i class="fas fa-table me-1"></i> DataTable
						</div>
						<div class="card-body">
							<table id="datatablesSimple">
							</table>
						</div>
					</div>
				</div>
			</main>
		</div>
	</div>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<script src="js/scripts.js"></script>
	<script
		src="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/umd/simple-datatables.js"
		crossorigin="anonymous"></script>
	<jsp:include page="assets/demo/datatables-simple-demo.jsp" />
</body>
</html>