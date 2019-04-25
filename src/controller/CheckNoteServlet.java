package controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Logger;

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
 * Servlet implementation class CheckNoteServlet
 */
@WebServlet("/notes/CheckNoteServlet")
public class CheckNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setAttribute("CheckType", "Confirm");
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/CheckNote.jsp");
		view.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		Note note = (Note) session.getAttribute("note");
		
		
		logger.info("Nota titulo: "+note.getTitle());
		
		User user = (User) session.getAttribute("user");
		
		long idn = -1;
		
		if(note!=null) {
			idn = noteDao.add(note);
			logger.info("Nota idn: "+idn);
			session.removeAttribute("note");
			note = null;
			
		}
		
		UsersNotes userNotes = new UsersNotes();
		
		if(idn!=-1) {
			Integer intIdn = Math.toIntExact(idn);
			userNotes.setIdn(intIdn);
			userNotes.setIdu(user.getIdu());
			userNotes.setOwner(1);
			userNotes.setArchived(0);
			userNotes.setPinned(0);
			usersNotesDao.add(userNotes);
			userNotes = null;
		}
		
		response.sendRedirect("MainPageServlet");
		
	}

}
