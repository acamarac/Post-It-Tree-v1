package controller;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.*;
import util.*;
import dao.*;


import javax.servlet.RequestDispatcher;

import java.sql.Connection;



/**
 * Servlet implementation class ListUsersNotesServlet
 */
@WebServlet("/notes/ListUsersNotesServlet")
public class ListUsersNotesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(HttpServlet.class.getName());
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListUsersNotesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		logger.info("Atendiendo GET");
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UserDAO userDAO = new JDBCUserDAOImpl();
		userDAO.setConnection(conn);

		UsersNotesDAO usersNotesDAO = new JDBCUsersNotesDAOImpl();
		usersNotesDAO.setConnection(conn);
		
		NoteDAO noteDAO = new JDBCNoteDAOImpl();
		noteDAO.setConnection(conn);
		
		
		List<UsersNotes> usersNotesList = usersNotesDAO.getAll();
		
		Iterator<UsersNotes> itUsersNotesList = usersNotesList.iterator();
		
		List<Triplet<User, UsersNotes, Note >> userNoteTripletList = new ArrayList<Triplet<User, UsersNotes, Note>>();
						
		while(itUsersNotesList.hasNext()) {
			UsersNotes usersNotes = (UsersNotes) itUsersNotesList.next();
			User user = userDAO.get(usersNotes.getIdu());
			Note note = noteDAO.get(usersNotes.getIdn());
			userNoteTripletList.add(new Triplet<User, UsersNotes, Note>(user,usersNotes,note));
			
		}
		request.setAttribute("usersNotes",userNoteTripletList);
		
		
		List<User> userList = userDAO.getAll();
		
		Iterator<User> itUserList = userList.iterator();
		
		List<Pair<User,List<Pair<UsersNotes, Note>>>> usersPairPairList = new ArrayList<Pair<User,List<Pair<UsersNotes, Note>>>>();
		
		while(itUserList.hasNext()) {
			User user = (User) itUserList.next();
			List<UsersNotes> usersNotesListByUser = usersNotesDAO.getAllByUser(user.getIdu());
			Iterator<UsersNotes> itUsersNotesListByUser = usersNotesListByUser.iterator();
			List<Pair<UsersNotes, Note>> userPairList = new ArrayList<Pair<UsersNotes, Note>>();
			while(itUsersNotesListByUser.hasNext()) {
				UsersNotes usersNotes = (UsersNotes) itUsersNotesListByUser.next();
				Note note = noteDAO.get(usersNotes.getIdn());
				userPairList.add(new Pair<UsersNotes, Note>(usersNotes,note));				
			}
			usersPairPairList.add(new Pair<User,List<Pair<UsersNotes, Note>>>(user,userPairList));
			
					
		}
		
		request.setAttribute("notesByUser",usersPairPairList);
		
		RequestDispatcher view = request.getRequestDispatcher("WEB-INF/ListUsersNotes.jsp");
		view.forward(request,response);
		
	
	}

	
}
