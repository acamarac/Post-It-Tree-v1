package controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import model.*;
import util.*;
import dao.*;

/**
 * Servlet implementation class FriendsMainServlet
 */
@WebServlet("/notes/FriendsMainServlet")
public class FriendsMainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FriendsMainServlet() {
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
		
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		//First: obtain all the friends
		
		List <Integer> friendsList = new ArrayList<Integer>();
		
		friendsList = friendsDao.getAllByUser(user.getIdu());
		
		List <User> userFriendList = new ArrayList<User>();
		
		for (int i=0; i<friendsList.size(); i++) {
			int idu = friendsList.get(i);
			User userFriend = userDao.get(idu);
			userFriendList.add(userFriend);
		}
		
		request.setAttribute("friendsList", userFriendList);
		
		//Second: obtain all the notes shared with the user
		
		List<UsersNotes> usersNotes = new ArrayList<UsersNotes>();
		List<UsersNotes> usersNotesShared = new ArrayList<UsersNotes>();
		
		usersNotes = usersNotesDao.getAllByUser(user.getIdu());
		for (int i=0; i<usersNotes.size(); i++) {
			if(usersNotes.get(i).getOwner()==0) //Then this note is shared with him
				usersNotesShared.add(usersNotes.get(i));
		}
			
		Iterator <UsersNotes> itUserNotes = usersNotesShared.iterator();

		List<Pair<User, Note>> pairListNotes = new ArrayList<Pair<User, Note>>();

		while(itUserNotes.hasNext()) {
			UsersNotes userNote = (UsersNotes) itUserNotes.next();
			long iduOwner = usersNotesDao.getOwner(userNote.getIdn());
			User userOwner = userDao.get(iduOwner);
			
			Note note = noteDao.get(userNote.getIdn());
			
			pairListNotes.add(new Pair<User, Note> (userOwner, note));

		}
		
		request.setAttribute("NotesList", pairListNotes);
		
		//Third: obtain all the notes that I share with the users
		
		List<UsersNotes> notesShared = new ArrayList<UsersNotes>();
		List<UsersNotes> notesSharedOwner = new ArrayList<UsersNotes>(); //All notes that the user owns
		
		notesShared = usersNotesDao.getAllByUser(user.getIdu());
		for(int i=0; i<notesShared.size(); i++) {
			if(notesShared.get(i).getOwner()==1) {
				notesSharedOwner.add(notesShared.get(i));
			}
		}
		
		//Para cada una de las notas comprobar que usuarios la tienen compartida en usersNotes y añadirlos al par
		Iterator <UsersNotes> itUserNotesOwned = notesSharedOwner.iterator();

		List<Pair<User, Note>> pairListNotesOwned = new ArrayList<Pair<User, Note>>();

		while(itUserNotesOwned.hasNext()) {
			UsersNotes userNote = (UsersNotes) itUserNotesOwned.next();
			int idnShared = userNote.getIdn();
			
			List<UsersNotes> noteSharedDao = usersNotesDao.getAllByNote(idnShared);
			for(int i=0; i<noteSharedDao.size(); i++) {
				User userShared = userDao.get(noteSharedDao.get(i).getIdu());
				Note noteShared = noteDao.get(noteSharedDao.get(i).getIdn());
				
				pairListNotesOwned.add(new Pair<User, Note> (userShared, noteShared));
			}

		}
		
		request.setAttribute("NotesListUserOwner", pairListNotesOwned);
		
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/FriendsMainPage.jsp");
		view.forward(request,response);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
