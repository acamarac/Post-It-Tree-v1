package controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.*;
import dao.*;


/**
 * Servlet implementation class CreateEditNoteServlet
 */
@WebServlet("/notes/EditNoteServlet")
public class EditNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);
		
		UsersNotesDAO userNotesDao = new JDBCUsersNotesDAOImpl();
		userNotesDao.setConnection(conn);
		
		String idnota="idn";
		String valueIdn=request.getParameter(idnota);
		Integer intValueIdn = Integer.parseInt(valueIdn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		UsersNotes usersNotes = userNotesDao.get(user.getIdu(), intValueIdn);
		
		if (usersNotes == null) {	//Then the user doesn't have access to this note
			response.sendRedirect("MainPageServlet");
		} else {
			Note note = noteDao.get(intValueIdn);
			
			if(note != null) {
				request.setAttribute("note", note);
				RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/CreateEditNote.jsp");
				view.forward(request, response);
			}else {
				response.sendRedirect("MainPageServlet");
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);

		Note note = new Note();
		note.setIdn(Integer.parseInt(request.getParameter("idn")));
		note.setTitle(request.getParameter("title"));
		note.setContent(request.getParameter("content"));
		
		String color = request.getParameter("selectColor"); //EXTRA: note color
		Integer colorInt = Integer.parseInt(color);
		note.setColor(colorInt);
		
		List <String> errorMsg = new ArrayList<String>();
		
		if(note.validate(errorMsg)) {
			noteDao.save(note);
			note = null;
			response.sendRedirect("MainPageServlet");
		} else {
			request.setAttribute("messages", errorMsg);
			request.setAttribute("note", note);
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/CreateEditNote.jsp");
			view.forward(request,response);
		}
	}

}
