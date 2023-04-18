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

<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" >
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker.min.css" 
	  integrity="sha512-mSYUmp1HYZDFaVKK//63EcZq4iFWFjxSL+Z3T/aCt4IO9Cejm03q3NKKYN6pFQzY0SBOr8h+eCIAZHPXcpZaNw==" crossorigin="anonymous" 
	  referrerpolicy="no-referrer" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/css/bootstrap-datepicker3.standalone.min.css">
<script src="https://code.jquery.com/jquery-3.2.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/js/bootstrap-datepicker.min.js" 
		integrity="sha512-T/tUfKSV1bihCnd+MxKD0Hm1uBBroVYBOYSk1knyvQ9VyZJpc/ALb4P0r6ubwVPSGB2GvjeoMAJJImBG12TiaQ==" crossorigin="anonymous" 
		referrerpolicy="no-referrer"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.9.0/locales/bootstrap-datepicker.ko.min.js" 
		integrity="sha512-L4qpL1ZotXZLLe8Oo0ZyHrj/SweV7CieswUODAAPN/tnqN3PA1P+4qPu5vIryNor6HQ5o22NujIcAZIfyVXwbQ==" crossorigin="anonymous" 
		referrerpolicy="no-referrer"></script>
		
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
			class="d-none d-md-inline-block form-inline me-auto ms-0 ms-md-3 my-2 my-md-0" action="search.it" method="post">
			<div class="input-group flex-nowrap" style="width: 1000px;">
				<input class="form-control col-md-8" type="text" placeholder="회사명1,회사명2..." 
				aria-label="Search for..." aria-describedby="btnNavbarSearch" name="targetNm" <%if(ins) {%> value="<%=target %>" <%} %> required="required" />
				<input type="text" id="datePicker_b" class="form-control col-md-2" placeholder="시작일" name="startDt" <%if(ins) {%> value="<%=startDt %>" <%} %> required="required"/>
				<input type="text" id="datePicker_e" class="form-control col-md-2" placeholder="종료일" name="endDt" <%if(ins) {%> value="<%=endDt %>" <%} %> required="required"/>
				<input type="hidden" name="pageNm" value="<%=request.getRequestURL().toString() %>" />
				<button class="btn btn-primary" id="btnNavbarSearch" type="submit">
					<i class="fas fa-search"></i>
				</button>
			</div>
		</form>
		<!-- <button class="btn btn-primary" type="button" style="margin-right: 10px;" onclick="location.href='corp.it';">코드 수동갱신</button> -->
	</nav>
	<script type="text/javascript" src="./js/bootstrap.js"></script>
	<script src="js/scripts.js"></script>
	<script>
		var strDate = $("#datePicker_b").val();
		var edDate = $("#datePicker_e").val();
		
		$('#datePicker_b').datepicker({
			format: "yyyymmdd",
	        endDate: '+0d', //달력에서 선택 할 수 있는 가장 느린 날짜. 이후로 선택 불가 ( d : 일 m : 달 y : 년 w : 주)
	        autoclose: true, //사용자가 날짜를 클릭하면 자동 캘린더가 닫히는 옵션
	        clearBtn: false, //날짜 선택한 값 초기화 해주는 버튼 보여주는 옵션 기본값 false 보여주려면 true
	        daysOfWeekDisabled: [0, 6], //선택 불가능한 요일 설정 0 : 일요일 ~ 6 : 토요일
	        isableTouchKeyboard: false, //모바일에서 플러그인 작동 여부 기본값 false 가 작동 true가 작동 안함.
	        immediateUpdates: false, //사용자가 보는 화면으로 바로바로 날짜를 변경할지 여부 기본값 :false
	        multidate: false, //여러 날짜 선택할 수 있게 하는 옵션 기본값 :false
	        multidateSeparator: ',', //여러 날짜를 선택했을 때 사이에 나타나는 글짜 2019-05-01,2019-06-01
	        templates: {
	           leftArrow: '&laquo;',
	           rightArrow: '&raquo;',
	        }, //다음달 이전달로 넘어가는 화살표 모양 커스텀 마이징
	        showWeekDays: true, // 위에 요일 보여주는 옵션 기본값 : true
	        title: '시작일', //캘린더 상단에 보여주는 타이틀
	        weekStart: 0, //달력 시작 요일 선택하는 것 기본값은 0인 일요일
	        language: 'ko', //달력의 언어 선택, 그에 맞는 js로 교체해줘야한다.
		})
		.on('changeDate', function(){
			var target = $("#datePicker_b").val();
			$('#datePicker_e').datepicker("setStartDate", target.substring(0,4)+'-'+target.substring(4,6)+'-'+target.substring(6));
		});
		
		$('#datePicker_e').datepicker({
			format: "yyyymmdd",
	        endDate: '+0d', //달력에서 선택 할 수 있는 가장 느린 날짜. 이후로 선택 불가 ( d : 일 m : 달 y : 년 w : 주)
	        autoclose: true, //사용자가 날짜를 클릭하면 자동 캘린더가 닫히는 옵션
	        clearBtn: false, //날짜 선택한 값 초기화 해주는 버튼 보여주는 옵션 기본값 false 보여주려면 true
	        daysOfWeekDisabled: [0, 6], //선택 불가능한 요일 설정 0 : 일요일 ~ 6 : 토요일
	        isableTouchKeyboard: false, //모바일에서 플러그인 작동 여부 기본값 false 가 작동 true가 작동 안함.
	        immediateUpdates: false, //사용자가 보는 화면으로 바로바로 날짜를 변경할지 여부 기본값 :false
	        multidate: false, //여러 날짜 선택할 수 있게 하는 옵션 기본값 :false
	        multidateSeparator: ',', //여러 날짜를 선택했을 때 사이에 나타나는 글짜 2019-05-01,2019-06-01
	        templates: {
	           leftArrow: '&laquo;',
	           rightArrow: '&raquo;',
	        }, //다음달 이전달로 넘어가는 화살표 모양 커스텀 마이징
	        showWeekDays: true, // 위에 요일 보여주는 옵션 기본값 : true
	        title: '종료일', //캘린더 상단에 보여주는 타이틀
	        weekStart: 0, //달력 시작 요일 선택하는 것 기본값은 0인 일요일
	        language: 'ko', //달력의 언어 선택, 그에 맞는 js로 교체해줘야한다.
		})
		.on("changeDate", function(){
			var target = $("#datePicker_e").val();
			$('#datePicker_b').datepicker("setEndDate", target.substring(0,4)+'-'+target.substring(4,6)+'-'+target.substring(6));
			console.log($('#datePicker_e').val());
		});
	</script>
</body>
</html>