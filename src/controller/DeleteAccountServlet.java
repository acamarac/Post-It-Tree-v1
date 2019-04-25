package controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.*;
import dao.*;

/**
 * Servlet implementation class DeleteAccountServlet
 */
@WebServlet("/notes/DeleteAccountServlet")
public class DeleteAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteAccountServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect("LoginServlet");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UsersNotesDAO userNotesDao = new JDBCUsersNotesDAOImpl();
		userNotesDao.setConnection(conn);
		
		NoteDAO notesDao = new JDBCNoteDAOImpl();
		notesDao.setConnection(conn);
		
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		FriendsDAO friendsDao = new JDBCFriendsDAOImpl();
		friendsDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		List <Integer> notesOwned = new ArrayList <Integer>();
		
		List <UsersNotes> usersNotesList = userNotesDao.getAllByUser(user.getIdu());
		
		
		if (!usersNotesList.isEmpty()) {	//Remove all the userNotes of that user
			for (int i=0; i<usersNotesList.size(); i++) {
				UsersNotes usersNotes = usersNotesList.get(i);
				if(usersNotes.getOwner() == 1)	//Stores the idn of the notes that the user owns
					notesOwned.add(usersNotes.getIdn());
				userNotesDao.delete(user.getIdu(), usersNotes.getIdn());
			}
		}
		
		if (!notesOwned.isEmpty()) {	//Remove all the userNotes and notes that this user owned
			for (int i=0; i<notesOwned.size(); i++) {
				int idn = notesOwned.get(i);
				userNotesDao.deleteIdn(idn);
				notesDao.delete(idn);
			}
		}
		
		userDao.delete(user.getIdu());
		
		friendsDao.deleteAllByUser(user.getIdu());
		
		session.removeAttribute("user");
		
		response.sendRedirect("LoginServlet");
		
	}

}
