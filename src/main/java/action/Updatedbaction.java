package action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.Searchmain;

public class Updatedbaction implements Action {

	@Override
	public Actionforward execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

		new Searchmain().testall();
		
		response.setContentType("text/html;charset=UTF-8"); 
		PrintWriter out = response.getWriter(); 
		out.println("<script>"); 
		out.println("alert('완료')"); 
		out.println("history.back()");
		out.println("</script>");
		
		return null;
	}

}
