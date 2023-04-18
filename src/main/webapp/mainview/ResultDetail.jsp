<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="bean.Resultbean"%>
<%@page import="java.util.ArrayList"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<meta charset="utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<title>Stock - Detail</title>
<link href="css/styles.css" rel="stylesheet" />
<script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"	crossorigin="anonymous"></script>
<script	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
</head>
<jsp:include page="./Sidepage/Header.jsp" />
<body class="sb-nav-fixed">
	<div id="layoutSidenav">
		<jsp:include page="./Sidepage/Sidebar.jsp" />
		<div id="layoutSidenav_content" style="padding-top: 24px">
			<main>
				<div class="container-fluid px-4">
					<h1 class="mt-4">Detail</h1>
					<div class="pb-2">
						<%
						if (ins) {
						%>
						<select id="selectbox" class="form-select form-select-sm" onchange="selectBoxChange()">
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
					<div class="row">
						<div class="col-xl-6">
							<div class="card mb-4">
								<div class="card-header">
									<i class="fas fa-chart-area me-1"></i> 일별 주식 현황
								</div>
								<div class="card-body">
									<canvas id="myLineChart" width="100%" height="40"></canvas>
								</div>
							</div>
						</div>
						<div class="col-xl-6">
							<div class="card mb-4">
								<div class="card-header">
									<i class="fas fa-chart-bar me-1"></i> 일일거래량
								</div>
								<div class="card-body">
									<canvas id="myBarChart" width="100%" height="40"></canvas>
								</div>
							</div>
						</div>
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
	<script	src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" crossorigin="anonymous"></script>
	<script	src="https://cdnjs.cloudflare.com/ajax/libs/Chart.js/2.8.0/Chart.min.js" crossorigin="anonymous"></script>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<script src="js/scripts.js"></script>
	<script	src="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/umd/simple-datatables.js" crossorigin="anonymous"></script>
	<script>
		function selectBoxChange() {
			lineChange();
			barChange();
		}
	</script>
	<jsp:include page="assets/demo/chart-area-demo.jsp" />
	<jsp:include page="assets/demo/chart-bar-demo.jsp" />
	<jsp:include page="assets/demo/datatables-simple-demo.jsp" />
</body>
</html>
