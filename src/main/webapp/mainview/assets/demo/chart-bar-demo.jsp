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

boolean ins = resultSession.getAttribute("result") != null ? true : false;

ArrayList<Resultbean> arr = (ArrayList<Resultbean>)resultSession.getAttribute("result");

StringBuilder resultDate = new StringBuilder();
StringBuilder resultTrqu = new StringBuilder();

if(ins){ 
		resultDate.append("'").append(arr.get(0).getBaseYmd()).append("'");
		resultTrqu.append(arr.get(0).getTrqu()); 
		
	for(int i = 1; i < arr.size(); i++) {
		resultDate.append(",'").append(arr.get(i).getBaseYmd()).append("'");
		resultTrqu.append(",").append(arr.get(i).getTrqu());
	}
}
%>
<!DOCTYPE html>
<html>
<script>
//Set new default font family and font color to mimic Bootstrap's default styling
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';


var myBarChart;
var ctxBar = document.getElementById("myBarChart");

var barDate = "<%=resultDate.toString() %>";
var trqu = "<%=resultTrqu.toString() %>";

function barChange() {
    var corpSelect = document.getElementById("selectbox");
    var selectValue = corpSelect.options[corpSelect.selectedIndex].value;
    
    if(selectValue != "0") {
    
     var beginIdx = corpSelect.options[corpSelect.options[corpSelect.selectedIndex].index - 1].value;	
     var arrBarDate = barDate.split(",").slice(beginIdx,selectValue-1);
     var arrTrqu = clpr.split(",").slice(beginIdx,selectValue-1);
     var yAxesMaxB = Math.max(...arrTrqu);
     var yAxesMinB = Math.min(...arrTrqu);
     var yAxesMaxBF = yAxesMaxB * 0.1 + yAxesMaxB;
     var yAxesMinBF = yAxesMinB - yAxesMinB * 0.1;
     var barSize = arrBarDate.length > 10 ? arrBarDate.length * 0.3 : 10;
     var config = {
    		  type: 'bar',
    		  data: {
    		    labels: arrBarDate,
    		    datasets: [{
    		      label: "거래량",
    		      backgroundColor: "rgba(2,117,216,1)",
    		      borderColor: "rgba(2,117,216,1)",
    		      data: arrTrqu,
    		    }],
    		  },
    		  options: {
                  tooltips: {
                      mode: 'index'
                  },
    		    scales: {
    		      xAxes: [{
    		        time: {
    		          unit: 'day'
    		        },
    		        gridLines: {
    		          display: false
    		        },
    		        ticks: {
    		          maxTicksLimit: barSize
    		        }
    		      }],
    		      yAxes: [{
    		        ticks: {
   	                  min: yAxesMinBF,
   	                  max: yAxesMaxBF,
    		          maxTicksLimit: barSize
    		        },
    		        gridLines: {
    		          display: true
    		        }
    		      }],
    		    },
    		    legend: {
    		      display: false
    		    }
    		  }
    		};

      if(myBarChart == null) {
    	  myBarChart = new Chart(ctxBar, config);
     } else {
    	 myBarChart.destroy();
     	myBarChart = new Chart(ctxBar, config);
     } 
    }
}
</script>
</html>