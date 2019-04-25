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

import dao.JDBCNoteDAOImpl;
import dao.JDBCUserDAOImpl;
import dao.JDBCUsersNotesDAOImpl;
import dao.NoteDAO;
import dao.UserDAO;
import dao.UsersNotesDAO;
import model.Note;
import model.User;
import model.UsersNotes;

/**
 * Servlet implementation class StopSharingNoteServlet
 */
@WebServlet("/notes/StopSharingNoteServlet")
public class StopSharingNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public StopSharingNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	Connection conn = (Connection) getServletContext().getAttribute("dbConn");

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
    		
    		List<UsersNotes> shared = userNotesDao.getAllByNote(idn);
    		List<User> friendsSharing = new ArrayList<User>();
    		
    		for(int i=0; i<shared.size(); i++) {
    			if(shared.get(i).getOwner()==0) { //DOuble check. Check if the friend is not the owner
    				User friend = userDao.get(shared.get(i).getIdu());
    				friendsSharing.add(friend);
    			}
    		}
    		
    		request.setAttribute("friends", friendsSharing);
    		
    		request.setAttribute("page", "Stop");

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
    	
    	String friendsShare[] = request.getParameterValues("friendShare");
		
		List <User> friendsToStopSharing = new ArrayList<User>();
		
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
					friendsToStopSharing.add(userFriend);
				}
			}
			
			String idnString = request.getParameter("idn");
			long idn = Long.parseLong(idnString);
			
			for(int i=0; i<friendsToStopSharing.size(); i++) { //Default notes shared are not pinned and not archived
				userNotesDao.delete(friendsToStopSharing.get(i).getIdu(), idn);
			}
			
			response.sendRedirect("MainPageServlet");
		}
		
	}

}
