<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.StringTokenizer"%>
<%@page import="bean.Resultbean"%>
<%@page import="java.util.ArrayList"%>
<%@page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
HttpSession resultSession = request.getSession();

%>
<!DOCTYPE html>
<html>
<script>

$(document).ready(function(){
    // Simple-DataTables
    // https://github.com/fiduswriter/Simple-DataTables/wiki

    const datatablesSimple = document.getElementById('datatablesSimple');
    
    var myData = {
			"headings" : [
				"기준일자","기업명","주식코드","종가","전일대비등락","등락비율","시가","고가","저가",
				"거래량","거래대금","당기순이익","전기순이익","전전기순이익","당기자산총계","전기자산총계","전전기자산총계",
				"당기부채총계","전기부채총계","전전기부채총계","당기자본총계","전기자본총계","전전기자본총계",
				"상장주식수","시가총액","EPS","PER","BPS","PBR"
			]
    };
    
    const dataTable = new simpleDatatables.DataTable(datatablesSimple,{
		perPageSelect : [["10", 10], ["50", 50], ["100", 100], ["All", 0]],
		data: myData,
	    columns: [
	        { select: 0, sort: "desc" },
	        { select: 3, type: "number" },  { select: 4, type: "number" },  { select: 5, type: "number" },
	        { select: 6, type: "number" },  { select: 7, type: "number" },  { select: 8, type: "number" },
	        { select: 9, type: "number" },  { select: 10, type: "number" }, { select: 11, type: "number" },
	        { select: 12, type: "number" }, { select: 13, type: "number" }, { select: 14, type: "number" },
	        { select: 15, type: "number" }, { select: 16, type: "number" }, { select: 17, type: "number" },
	        { select: 18, type: "number" }, { select: 19, type: "number" }, { select: 20, type: "number" },
	        { select: 21, type: "number" }, { select: 22, type: "number" }, { select: 23, type: "number" },
	        { select: 24, type: "number" }, { select: 25, type: "number" }, { select: 26, type: "number" },
	        { select: 27, type: "number" }, { select: 28, type: "number" }
	    ]
	});

    $("#selectbox").change(function() {
    	var selectVal = $("#selectbox option:selected").val();
    	var index = $("#selectbox option").index($("#selectbox option:selected"));
    	var beginIdx = $("#selectbox option:eq("+(index-1)+")").val();

    	if(selectVal != "0") {
    		$.ajax({
    			type:'post',
    			url:'ajaxChart.it', 
    			data:{ begin: beginIdx,
    				   end  : selectVal}, 
    			dataType:'json',
    			success : function(resultJson){
    				dataTable.destroy();
    				dataTable.init();
    				dataTable.insert(resultJson);
    			}
    		});
    	}
    });
});
</script>
</html>