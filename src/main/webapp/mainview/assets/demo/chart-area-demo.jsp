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
StringBuilder resultClpr = new StringBuilder();
StringBuilder resultHipr = new StringBuilder();
StringBuilder resultLopr = new StringBuilder();
StringBuilder resultMkp = new StringBuilder();

if(ins){ 
		resultDate.append("'").append(arr.get(0).getBaseYmd()).append("'");
		resultClpr.append(arr.get(0).getClpr()); 
		resultHipr.append(arr.get(0).getHipr());
		resultLopr.append(arr.get(0).getLopr());
		resultMkp.append(arr.get(0).getMkp());
		
	for(int i = 1; i < arr.size(); i++) {
		resultDate.append(",'").append(arr.get(i).getBaseYmd()).append("'");
		resultClpr.append(",").append(arr.get(i).getClpr());
		resultHipr.append(",").append(arr.get(i).getHipr());
		resultLopr.append(",").append(arr.get(i).getLopr());
		resultMkp.append(",").append(arr.get(i).getMkp());
	}
}
%>
<!DOCTYPE html>
<html>
<script>
Chart.defaults.global.defaultFontFamily = '-apple-system,system-ui,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,sans-serif';
Chart.defaults.global.defaultFontColor = '#292b2c';

var myLineChart;
var ctxLine = document.getElementById("myLineChart");

var lineDate = "<%=resultDate.toString() %>";
var clpr = "<%=resultClpr.toString() %>";
var hipr = "<%=resultHipr.toString() %>";
var lopr = "<%=resultLopr.toString() %>";
var mkp = "<%=resultMkp.toString() %>";

function lineChange() {
    var corpSelect = document.getElementById("selectbox");
    var selectValue = corpSelect.options[corpSelect.selectedIndex].value;
    
    if(selectValue != "0") {
     var beginIdx = corpSelect.options[corpSelect.options[corpSelect.selectedIndex].index - 1].value;
     var arrLineDate = lineDate.split(",").slice(beginIdx,selectValue-1);
     var arrClpr = clpr.split(",").slice(beginIdx,selectValue-1);
     var arrHipr = hipr.split(",").slice(beginIdx,selectValue-1);
     var arrLopr = lopr.split(",").slice(beginIdx,selectValue-1);
     var arrMkp = mkp.split(",").slice(beginIdx,selectValue-1);
     
     var yAxesMaxL = Math.max(...arrHipr);
     var yAxesMinL = Math.min(...arrLopr);
     var yAxesMaxLF = yAxesMaxL * 0.1 + yAxesMaxL;
     var yAxesMinLF = yAxesMinL - yAxesMinL * 0.1;
     var lineSize = arrLineDate.length > 10 ? arrLineDate.length * 0.3 : 10;
     
     var config = {
         type: 'bar',
          data: {
            labels: arrLineDate,
            datasets: [{
              label: "시가",
              type: 'line',
              fill : false,
              tension: 0,
              backgroundColor: "rgba(183,240,177,0.2)",
              borderColor: "rgba(183,240,177,1)", 
              pointBackgroundColor: "rgba(183,240,177,1)",
              pointBorderColor: "rgba(255,255,255,0.8)",
              pointHoverBackgroundColor: "rgba(183,240,177,1)",
              data: arrMkp
            },
            {
               label: "고가",
               type: 'line',
               fill : false,
               tension: 0,
               backgroundColor: "rgba(255,167,167,0.2)",
               borderColor: "rgba(255,167,167,1)", 
               pointBackgroundColor: "rgba(255,167,167,1)",
               pointBorderColor: "rgba(255,255,255,0.8)",
               pointHoverBackgroundColor: "rgba(255,167,167,1)",
               data: arrHipr
         },
            {
               label: "저가",
               type: 'line',
               fill : false,
               tension: 0,
               backgroundColor: "rgba(2,117,216,0.2)",
               borderColor: "rgba(2,117,216,1)", 
               pointBackgroundColor: "rgba(2,117,216,1)",
               pointBorderColor: "rgba(255,255,255,0.8)",
               pointHoverBackgroundColor: "rgba(2,117,216,1)",
               data: arrLopr
         },
           {
             label: "종가",
             type : 'bar',
             lineTension: 0.3,
             backgroundColor: "rgba(243,97,166,0.2)",
             borderColor: "rgba(243,97,166,1)", 
             data: arrClpr
           }
            ],
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
                  maxTicksLimit: lineSize
                }
              }],
              yAxes: [{
                ticks: {
                  min: yAxesMinLF,
                  max: yAxesMaxLF,
                  maxTicksLimit: lineSize
                },
                gridLines: {
                  color: "rgba(0, 0, 0, .125)",
                }
              }],
            },
            legend: {
              display: false
            }
          }
        };
      if(myLineChart == null) {
     	myLineChart = new Chart(ctxLine, config);
     } else {
     	myLineChart.destroy();
     	myLineChart = new Chart(ctxLine, config);
     } 
    }
}
</script>
</html>