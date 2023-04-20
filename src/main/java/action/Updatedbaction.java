package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Infobean;
import main.Searchmain;

public class Updatedbaction implements Action {

	@Override
	public Actionforward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Infobean bean = new Infobean();
		
		bean.setStartDT(request.getParameter("startDt").replaceAll("-", ""));
		bean.setEndDT(request.getParameter("endDt").replaceAll("-", ""));
		bean.setTarget(request.getParameter("targetNm"));
		
		if(!(request.getParameter("targetNm").length() == 0))
			new Searchmain().testall(bean);
		
		response.setContentType("text/html;charset=UTF-8"); 
		PrintWriter out = response.getWriter(); 
		out.println("<script>"); 
		out.println("alert('완료')"); 
		out.println("history.back()");
		out.println("</script>");
		
		return null;
	}

}
