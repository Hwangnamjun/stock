package action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Infobean;
import bean.Resultbean;
import main.Searchmain;
import util.Customexception;

public class Searchaction implements Action{

	@Override
	public Actionforward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Actionforward forward = null;
		HttpSession session = request.getSession();
		
		try {
			String target = request.getParameter("targetNm");
			String startDT = request.getParameter("startDt");
			String endDT = request.getParameter("endDt");
			String pageNm = request.getParameter("pageNm");
			
			ArrayList<Resultbean> result = new ArrayList<>();
			Infobean bean = new Infobean();
			StringBuilder leng = new StringBuilder();
			StringTokenizer targetToken = new StringTokenizer(target,",");
			StringBuilder resTarget = new StringBuilder();
			
			bean.setStartDT(startDT);
			bean.setEndDT(endDT);
			
			while(targetToken.hasMoreTokens()) {
				bean.setTarget(targetToken.nextToken().toUpperCase());
				result.addAll(new Searchmain().Startwork(bean));
				resTarget.append(result.get(result.size()-1).getCorpName()).append(",");
				leng.append(result.size()).append(",");
			}
			
			resTarget.deleteCharAt(resTarget.length()-1);
			
			session.setAttribute("result", result);
			session.setAttribute("target", resTarget);
			session.setAttribute("startDt", request.getParameter("startDt"));
			session.setAttribute("endDt", request.getParameter("endDt"));
			session.setAttribute("leng", leng.toString());
			
			if(pageNm.contains("table")) {
				forward=new Actionforward();
				forward.setPath("resultTable.it");
				forward.setRedirect(false);
			} else if(pageNm.contains("chart")) {
				forward=new Actionforward();
				forward.setPath("resultChart.it");
				forward.setRedirect(false);
			} else if(pageNm.contains("Detail")) {
				forward=new Actionforward();
				forward.setPath("detail.it");
				forward.setRedirect(false);
			} else if(pageNm.contains("Dashboard")) {
				forward=new Actionforward();
				forward.setPath("detail.it");
				forward.setRedirect(false);
			}
		} catch (Customexception e) {
			response.setContentType("text/html;charset=UTF-8"); 
			session.removeAttribute("result");
			session.removeAttribute("target");
			PrintWriter out = response.getWriter();
			out.println("<script>"); 
			out.println("alert('"+e.getMessage()+"')"); 
			out.println("history.back()");
			out.println("</script>");
		}
		return forward;
	}
 
	
}
