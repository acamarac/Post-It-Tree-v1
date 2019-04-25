package controller;

import java.io.IOException;
import java.sql.Connection;

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
 * Servlet implementation class DeleteNoteServlet
 */
@WebServlet("/notes/DeleteNoteServlet")
public class DeleteNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteNoteServlet() {
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
		
		String idnString = request.getParameter("idn");
		
		long idn = Long.parseLong(idnString);
		
		Note note = noteDao.get(idn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		UsersNotes userNotes = null;
		userNotes = userNotesDao.get(user.getIdu(), idn);
		
		if (userNotes != null) {
			if( note != null ) {
				session.setAttribute("note", note);
				request.setAttribute("CheckType", "Delete");
				RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/CheckNote.jsp");
				view.forward(request,response);
			} else {
				response.sendRedirect("MainPageServlet");
			}
		} else {
			response.sendRedirect("MainPageServlet");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UsersNotesDAO userNotesDao = new JDBCUsersNotesDAOImpl();
		userNotesDao.setConnection(conn);
		
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		Note note = (Note) session.getAttribute("note");
		
		long idu = user.getIdu();
		
		if ( note != null) {
			
			long idn = note.getIdn();
			
			if(userNotesDao.get(idu, idn).getOwner() == 1 ) { //If user is the owner of the note then delete it for all
				userNotesDao.deleteIdn(idn); //This method delete all userNotes with that idn
				noteDao.delete(idn);
			} else {
				userNotesDao.delete(idu, idn);
			}
			
			note = null;
			session.removeAttribute("note");
		}
		
		response.sendRedirect("MainPageServlet");
		
	}

}
