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
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
<meta name="description" content="" />
<meta name="author" content="" />
<link href="css/styles.css" rel="stylesheet" />
<link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script src="https://use.fontawesome.com/releases/v6.3.0/js/all.js"	crossorigin="anonymous"></script>
</head>
<body>
	<nav class="sb-topnav navbar navbar-expand navbar-dark bg-dark">
		<!-- Navbar Brand-->
		<a class="navbar-brand ps-3" href="dashBoard.it">Stock</a>
		<!-- Sidebar Toggle-->
		<button class="btn btn-link btn-sm order-1 order-lg-0 me-4 me-lg-0" id="sidebarToggle" href="#!">
			<i class="fas fa-bars"></i>
		</button>
		<!-- Navbar Search-->
		<form
			class="d-none d-md-inline-block form-inline me-auto ms-0 ms-md-3 my-2 my-md-0 w-auto" action="search.it" method="post">
			<div class="input-group flex-nowrap" style="width: 1000px;">
				<input class="form-control col-md-8 w-50" type="text" placeholder="회사명1,회사명2..." 
				aria-label="Search for..." aria-describedby="btnNavbarSearch" name="targetNm" <%if(ins) {%> value="<%=target %>" <%} %> required="required" />
				<input type="text" id="datePicker_b" class="form-control col-md-2 w-5" placeholder="시작일" name="startDt" <%if(ins) {%> value="<%=startDt %>" <%} %> required="required" readonly="readonly"/>
				<input type="text" id="datePicker_e" class="form-control col-md-2 w-5" placeholder="종료일" name="endDt" <%if(ins) {%> value="<%=endDt %>" <%} %> required="required" readonly="readonly"/>
				<input type="hidden" name="pageNm" value="<%=request.getRequestURL().toString() %>" />
				<button class="btn btn-primary" id="btnNavbarSearch" type="submit">
					<i class="fas fa-search"></i>
				</button>
				<div><button class="btn btn-primary" type="button" onclick="return submit2(this.form);">코드 수동갱신</button></div>
			</div>
		</form>
	</nav>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<script src="js/scripts.js"></script>
	<script type="text/javascript">
	  function submit2(frm) { 
		    frm.action='corp.it'; 
		    frm.submit(); 
		    return true; 
		  } 
	</script>
	<script>
		var strDate = $("#datePicker_b").val();
		var edDate = $("#datePicker_e").val();
		
		   $(function() {
		       $("#datePicker_b,#datePicker_e").datepicker({
		            dateFormat: 'yy-mm-dd' //달력 날짜 형태
		           ,showOtherMonths: true //빈 공간에 현재월의 앞뒤월의 날짜를 표시
		           ,showMonthAfterYear:true // 월- 년 순서가아닌 년도 - 월 순서
		           ,changeYear: true //option값 년 선택 가능
		           ,changeMonth: true //option값  월 선택 가능                
		           ,showOn: "both" //button:버튼을 표시하고,버튼을 눌러야만 달력 표시 ^ both:버튼을 표시하고,버튼을 누르거나 input을 클릭하면 달력 표시  
		           ,buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif" //버튼 이미지 경로
		           ,buttonImageOnly: false
		           ,buttonText: "선택" //버튼 호버 텍스트              
		           ,yearSuffix: "년" //달력의 년도 부분 뒤 텍스트
		           ,monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 텍스트
		           ,monthNames: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'] //달력의 월 부분 Tooltip
		           ,dayNamesMin: ['일','월','화','수','목','금','토'] //달력의 요일 텍스트
		           ,dayNames: ['일요일','월요일','화요일','수요일','목요일','금요일','토요일'] //달력의 요일 Tooltip
		           ,minDate: "2020-01-01" //최소 선택일자(-1D:하루전, -1M:한달전, -1Y:일년전)
		           ,maxDate: "today" //최대 선택일자(+1D:하루후, -1M:한달후, -1Y:일년후)  
				   ,onSelect: function(dateText) {
				        console.log("Selected date: " + dateText + "; input's current value: " + this.id);
				        if(this.id == "datePicker_b") {
				        	$("#datePicker_e").datepicker("option","minDate", dateText);
				        } else if(this.id == "datePicker_e") {
				        	$("#datePicker_b").datepicker("option","maxDate", dateText);
				        }
				   }
				   ,beforeShowDay: function(date){
						var day = date.getDay();
						return [(day != 0 && day != 6)];
				   }
		       });
		       $("#datePicker_b").datepicker('setDate', strDate);
		       $("#datePicker_e").datepicker('setDate', edDate);
		       
		   });

	</script>
</body>
</html>