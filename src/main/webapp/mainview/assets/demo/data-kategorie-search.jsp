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

    const datatablesSimple1 = document.getElementById('datatables1');
    const datatablesSimple2 = document.getElementById('datatables2');
    const datatablesSimple3 = document.getElementById('datatables3');
    
    var myData1 = {
			"headings" : [
				"기업명","EPS","PER","BPS","PBR"
			]
    };
    var myData2 = {
			"headings" : [
				"기업명","당기순이익","당기자산총계","당기부채총계","당기자본총계"
			]
    };
    var myData3 = {
			"headings" : [
				"순번","일자","기업명","종가","전일대비등락","등락비율"
			]
    };
    const dataTable1 = new simpleDatatables.DataTable(datatablesSimple1,{
		data: myData1,
		fixedHeight: true,
		searchable: false,
		perPageSelect: false,
		columns: [
			{ select: 1, type: "number"},
			{ select: 2, type: "number"},
			{ select: 3, type: "number"}
		]
	});
    const dataTable2 = new simpleDatatables.DataTable(datatablesSimple2,{
		data: myData2,
		fixedHeight: true,
		searchable: false,
		perPageSelect: false,
		columns: [
			{ select: 1, type: "number"},
			{ select: 2, type: "number"},
			{ select: 3, type: "number"},
			{ select: 4, type: "number"}
		]
	});
    const dataTable3 = new simpleDatatables.DataTable(datatablesSimple3,{
		data: myData3,
		fixedHeight: true,
		searchable: false,
		perPageSelect: false,
		columns: [
			{ select: 0, type: "number"},
			{ select: 3, type: "number"},
			{ select: 4, type: "number"},
			{ select: 5, type: "number"}
		]
	});
    $("#kategoriebox").change(function() {
    	var selectVal_k = $("#kategoriebox option:selected").val();
    	
		if(selectVal_k != "0") {
    		$.ajax({
    			type:'post',
    			url:'ajaxKategorie.it', 
    			data:{ parm  : selectVal_k }, 
    			dataType:'json',
    			success : function(resultJson){
    				dataTable1.destroy();
    				dataTable1.init();
    				dataTable1.insert(resultJson);
    				
    				dataTable2.destroy();
    				dataTable2.init();
    				dataTable2.insert(resultJson);
    			}
    		});
    		$.ajax({
    			type:'post',
    			url:'ajaxKategorie2.it', 
    			data:{ parm  : selectVal_k }, 
    			dataType:'json',
    			success : function(resultJson){
    				dataTable3.destroy();
    				dataTable3.init();
    				dataTable3.insert(resultJson);
    			}
    		});
    	}
    });
});
</script>
</html>