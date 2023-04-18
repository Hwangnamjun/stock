package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import action.Action;
import action.Actionforward;
import action.Ajaxaction;
import action.Kategorieaction;
import action.Searchaction;
import action.Updatedbaction;

@WebServlet("*.it")
public class Frontcontroller extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
		request.setCharacterEncoding("UTF-8");
		String command = request.getServletPath();
		
		Action action = null;
		Actionforward forward=null;
		
		switch (command) {
		
		case "/mainview/search.it":
			
			action = new Searchaction();
			forward = action.execute(request, response);
			break;
		
		case "/mainview/resultTable.it":
			
			forward=new Actionforward();
			forward.setPath("/mainview/ResultTable.jsp");
			break;
		
		case "/mainview/resultChart.it":
			
			forward=new Actionforward();
			forward.setPath("/mainview/ResultChart.jsp");
			break;
			
		case "/mainview/corp.it":
			
			action = new Updatedbaction();
			forward = action.execute(request, response);
			break;
			
		case "/mainview/detail.it":
			
			forward=new Actionforward();
			forward.setPath("/mainview/ResultDetail.jsp");
			break;
			
		case "/mainview/ajaxChart.it":
			
			action = new Ajaxaction();
			forward = action.execute(request, response);
			break;
			
		case "/mainview/ajaxKategorie.it":
			
			action = new Kategorieaction().settingbool(true);
			forward = action.execute(request, response);
			break;
			
		case "/mainview/ajaxKategorie2.it":
			
			action = new Kategorieaction().settingbool(false);
			forward = action.execute(request, response);
			break;
			
		case "/mainview/dashBoard.it":
			
			forward=new Actionforward();
			forward.setPath("/mainview/ResultDashboard.jsp");
			break;
		}
		
		/*
		if(command.equals("/mainview/search.it")) {
			
			action = new Searchaction();
			forward = action.execute(request, response);
			
		} else if(command.equals("/mainview/resultTable.it")) {
			
			forward=new Actionforward();
			forward.setPath("/mainview/Resulttable.jsp");
			
		} else if(command.equals("/mainview/resultChart.it")) {
			
			forward=new Actionforward();
			forward.setPath("/mainview/Resultchart.jsp");
			
		} else if(command.equals("/mainview/corp.it")) {
			
			action = new Updatedbaction();
			forward = action.execute(request, response);
			
		} else if(command.equals("/mainview/detail.it")) {
			
			forward=new Actionforward();
			forward.setPath("/mainview/Detailresult.jsp");
			
		} else if(command.equals("/mainview/ajaxChart.it")) {
			
			action = new Ajaxaction();
			forward = action.execute(request, response);
			
		} else if(command.equals("/mainview/ajaxKategorie.it")) {
			
			action = new Kategorieaction().settingbool(true);
			forward = action.execute(request, response);
			
		} else if(command.equals("/mainview/ajaxKategorie2.it")) {
			
			action = new Kategorieaction().settingbool(false);
			forward = action.execute(request, response);
			
		}
		*/
		
		
		
		if(forward!=null) 
		{
			if(forward.isRedirect()) {
				response.sendRedirect(forward.getPath());
			}else {
				RequestDispatcher dispatcher=request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}


}
