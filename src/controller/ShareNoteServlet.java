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
 * Servlet implementation class ShareNoteServlet
 */
@WebServlet("/notes/ShareNoteServlet")
public class ShareNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShareNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		FriendsDAO friendsDao = new JDBCFriendsDAOImpl();
		friendsDao.setConnection(conn);
		
		UsersNotesDAO userNotesDao = new JDBCUsersNotesDAOImpl();
		userNotesDao.setConnection(conn);
		
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);
		
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		String idnString = request.getParameter("idn");
		long idn = Long.parseLong(idnString);
		
		UsersNotes userNotes = userNotesDao.get(user.getIdu(), idn);
		
		if(userNotes==null || userNotes.getOwner()!=1) { //Then the user don't have access to this note
			response.sendRedirect("MainPageServlet");
		} else {
			Note note = noteDao.get(idn);
			request.setAttribute("note", note);

			List <Integer> iduFriends = new ArrayList<Integer>();

			iduFriends = friendsDao.getAllByUser(user.getIdu());

			List <User> userFriends = new ArrayList<User>();

			for (int i=0; i<iduFriends.size(); i++) {
				User userF = userDao.get(iduFriends.get(i));
				userFriends.add(userF);
			}

			request.setAttribute("friends", userFriends);

			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/ShareNote.jsp");
			view.forward(request,response);

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
		
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		FriendsDAO friendsDao = new JDBCFriendsDAOImpl();
		friendsDao.setConnection(conn);
		
		String friendsShare[] = request.getParameterValues("friendShare");
		
		List <User> friendsToShare = new ArrayList<User>();
		
		if(friendsShare==null) {
			List<String> messages = new ArrayList<String>();
			messages.add("Select at least one friend to share");
			
			request.setAttribute("messages", messages);
			
			doGet(request, response);
			
		} else {
			for(int i=0; i<friendsShare.length; i++) {
				int idFriend = Integer.parseInt(friendsShare[i]);
				User userFriend = userDao.get(idFriend);
				if(userFriend!=null) {
					friendsToShare.add(userFriend);
				}
			}
			
			String idnString = request.getParameter("idn");
			long idn = Long.parseLong(idnString);
			
			for(int i=0; i<friendsToShare.size(); i++) { //Default notes shared are not pinned and not archived
				UsersNotes userNotes = new UsersNotes();
				userNotes.setIdu(friendsToShare.get(i).getIdu());
				userNotes.setIdn((int) idn);
				userNotes.setOwner(0);
				userNotes.setPinned(0);
				userNotes.setArchived(0);
				
				userNotesDao.add(userNotes);
			}
			
			response.sendRedirect("MainPageServlet");
		}
	}

}
