package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.*;

/**
 * Servlet implementation class CreateNoteServlet
 */
@WebServlet("/notes/CreateNoteServlet")
public class CreateNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		if (session.getAttribute("note")!=null) session.removeAttribute("note"); //Prevent 
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/CreateEditNote.jsp");
		view.forward(request,response);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		Note note = new Note();
		note.setTitle(request.getParameter("title"));
		note.setContent(request.getParameter("content"));
		
		String color = request.getParameter("selectColor");
		int colorInt = Integer.parseInt(color);
		note.setColor(colorInt);
		
		List <String> errorMsg = new ArrayList<String>();
		
		if(note.validate(errorMsg)) {
			HttpSession session = request.getSession();
			session.setAttribute("note", note);
			response.sendRedirect("CheckNoteServlet");
			logger.info("Nota name:" +note.getTitle());
		} else {
			request.setAttribute("note", note);
			request.setAttribute("messages", errorMsg);
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/CreateEditNote.jsp");
			view.forward(request,response);
		}
		
		
		
	}

}
