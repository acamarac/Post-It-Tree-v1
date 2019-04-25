package controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.FriendsDAO;
import dao.JDBCFriendsDAOImpl;
import dao.JDBCNoteDAOImpl;
import dao.JDBCUserDAOImpl;
import dao.JDBCUsersNotesDAOImpl;
import dao.NoteDAO;
import dao.UserDAO;
import dao.UsersNotesDAO;
import model.Note;
import model.User;
import model.UsersNotes;
import util.NoteComp;
import util.Pair;

/**
 * Servlet implementation class AvdSearchServlet
 */
@WebServlet("/notes/AvdSearchServlet")
public class AvdSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger logger = 
			Logger.getLogger(HttpServlet.class.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AvdSearchServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    private List<Note> textSearch(String textOfSearch, NoteDAO noteDao, UsersNotesDAO usersNotesDao, User user, String optionText) {

    	List<Note>textSelectedNotes = new ArrayList<Note>();
    	if(!textOfSearch.isEmpty()) { //Then we have text input


    		switch (optionText) {

    		case "all":
    			textSelectedNotes = noteDao.getAllBySearchAll(textOfSearch);
    			break;

    		case "title":
    			textSelectedNotes = noteDao.getAllBySearchTitle(textOfSearch);
    			break;

    		case "content":
    			textSelectedNotes = noteDao.getAllBySearchContent(textOfSearch);
    			break;

    		default:

    		}
    	}

    	List <Note> userTextSelectedNotes = new ArrayList<Note>();

    	long idu = user.getIdu();
    	for (int i=0; i<textSelectedNotes.size(); i++) {
    		if(usersNotesDao.get(idu, textSelectedNotes.get(i).getIdn()) != null) { //Then the user doesn't have this note
    			userTextSelectedNotes.add(textSelectedNotes.get(i));
    		}
    	}
    	return userTextSelectedNotes;
    }

    private List<Note> colorFilter (List<Note> notesList, String color[]) {

    	List<Note> notesMatching = new ArrayList<Note>();

    	for (int i=0; i<color.length; i++) {
    		int idColor = Integer.parseInt(color[i]);
    		logger.info("Procesando color:" +idColor);
    		for(int j=0; j<notesList.size(); j++) {
    			logger.info("Color nota:" +notesList.get(j).getColor());
    			if(notesList.get(j).getColor() == idColor) {
    				notesMatching.add(notesList.get(j));
    			}
    		}
    	}

    	logger.info("Tamaño lista:" +notesMatching.size());

    	return notesMatching;

    }

    private List<Note> shareFilter(List<Note> noteList, String shared[], UsersNotesDAO usersNotesDao, String shareOption, User user) {

    	List<Note> notesFiltered = new ArrayList<Note>();

    	if (shareOption.equals("sharedBy")) { //Then the user doesn't own this note
    		for(int i=0; i<noteList.size(); i++) {
    			long iduOwner = usersNotesDao.getOwner(noteList.get(i).getIdn());
    			if(iduOwner != user.getIdu()) { //User is not the owner
    				for(int j=0; j<shared.length; j++) {
    					int iduShared = Integer.parseInt(shared[j]);
    					if( iduShared == iduOwner) {
    						notesFiltered.add(noteList.get(i));
    					}
    				}
    			}
    		}
    	} else { //Then the user owns this note and he is sharing it with others
    		for(int i=0; i<noteList.size(); i++) {
    			long iduOwner = usersNotesDao.getOwner(noteList.get(i).getIdn());
    			if (iduOwner == user.getIdu()) {
    				for (int j=0; j<shared.length; j++) {
    					int iduShared = Integer.parseInt(shared[j]);
    					UsersNotes userNotes = usersNotesDao.get(iduShared, noteList.get(i).getIdn());
    					if(userNotes!=null) { //Then the user is sharing it with that friend
    						notesFiltered.add(noteList.get(i));
    					}
    				}
    			}
    		}
    	}

    	return notesFiltered;

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
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		List<Integer> friendsIdu = new ArrayList<Integer>();
		friendsIdu = friendsDao.getAllByUser(user.getIdu());
		
		List<User> friendsList = new ArrayList<User>();
		
		for(int i=0; i<friendsIdu.size(); i++) {
			User userFriend = userDao.get(friendsIdu.get(i));
			friendsList.add(userFriend);
		}
		
		session.setAttribute("FriendsList", friendsList);
		
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/AdvSearch.jsp");
		view.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		FriendsDAO friendsDao = new JDBCFriendsDAOImpl();
		friendsDao.setConnection(conn);

		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);

		NoteDAO noteDao = new JDBCNoteDAOImpl();
		noteDao.setConnection(conn);

		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		List<String> messages = new ArrayList<String>();
		
		String textOfSearch = request.getParameter("search");
		String optionText = request.getParameter("searchOption");
		String color[] = request.getParameterValues("color");
		String shared[] = request.getParameterValues("friendSearch");
		String shareOption = request.getParameter("sharing");
		
		int situation = 0;
		
		if(textOfSearch.isEmpty() && color==null && shared==null)
			situation = 0;
		
		if(!textOfSearch.isEmpty() && color!=null && shared!=null)
			situation = 1;
		
		if(!textOfSearch.isEmpty() && color==null && shared==null)
			situation = 2;
		
		if(!textOfSearch.isEmpty() && color!=null && shared==null)
			situation = 3;
		
		if(!textOfSearch.isEmpty() && color==null && shared!=null)
			situation = 4;
		
		if(textOfSearch.isEmpty() && color!=null && shared==null)
			situation = 5;
		
		if(textOfSearch.isEmpty() && color!=null && shared!=null)
			situation = 6;
		
		if(textOfSearch.isEmpty() && color==null && shared!=null)
			situation = 7;
		
		List<Note> selectedNotes = new ArrayList<Note>();

		List<Note> noteList = new ArrayList<Note>();
		List<UsersNotes> userNotesList = new ArrayList<UsersNotes>();

		userNotesList = usersNotesDao.getAllByUser(user.getIdu());
		for(int i=0; i<userNotesList.size(); i++) {
			Note note = noteDao.get(userNotesList.get(i).getIdn());
			noteList.add(note);
		}
		
		switch (situation) {
		case 0:
			//Nothing to do
			messages.add("Nothing was searched");
			break;
			
		case 1:
			selectedNotes = textSearch(textOfSearch, noteDao, usersNotesDao, user, optionText);
			selectedNotes = colorFilter(selectedNotes, color);
			selectedNotes = shareFilter(selectedNotes, shared, usersNotesDao, shareOption, user);
			messages.add("Looking for text '" +textOfSearch+ "' in " +optionText);
			messages.add("Looking for notes of " +color.length+ " color/s");
			if(shareOption.equals("sharedBy"))
				messages.add("Looking for notes shared by " +shared.length+ " friend/s");
			else
				messages.add("Looking for notes shared with " +shared.length+ " friend/s");
			break;
			
		case 2:
			selectedNotes = textSearch(textOfSearch, noteDao, usersNotesDao, user, optionText);
			messages.add("Looking for text '" +textOfSearch+ "' in " +optionText);
			break;
			
		case 3:
			selectedNotes = textSearch(textOfSearch, noteDao, usersNotesDao, user, optionText);
			selectedNotes = colorFilter(selectedNotes, color);
			messages.add("Looking for text '" +textOfSearch+ "' in " +optionText);
			messages.add("Looking for notes of " +color.length+ " color/s");
			break;
			
		case 4:
			selectedNotes = textSearch(textOfSearch, noteDao, usersNotesDao, user, optionText);
			selectedNotes = shareFilter(selectedNotes, shared, usersNotesDao, shareOption, user);
			messages.add("Looking for text " +textOfSearch+ " in " +optionText);
			if(shareOption.equals("sharedBy"))
				messages.add("Looking for notes shared by " +shared.length+ " friend/s");
			else
				messages.add("Looking for notes shared with " +shared.length+ " friend/s");
			break;
			
		case 5:
			selectedNotes = colorFilter(noteList, color);
			messages.add("Looking for notes of " +color.length+ " color/s");
			break;
			
		case 6:
			selectedNotes = colorFilter(noteList, color);
			selectedNotes = shareFilter(selectedNotes, shared, usersNotesDao, shareOption, user);
			messages.add("Looking for notes of " +color.length+ " color/s");
			if(shareOption.equals("sharedBy"))
				messages.add("Looking for notes shared by " +shared.length+ " friend/s");
			else
				messages.add("Looking for notes shared with " +shared.length+ " friend/s");
			break;
			
		case 7:
			selectedNotes = shareFilter(noteList, shared, usersNotesDao, shareOption, user);
			if(shareOption.equals("sharedBy"))
				messages.add("Looking for notes shared by " +shared.length+ " friend/s");
			else
				messages.add("Looking for notes shared with " +shared.length+ " friend/s");
			break;

		default:
			break;
		}
		
		
		List<Pair<UsersNotes, Note>> pairListNotes = new ArrayList<Pair<UsersNotes, Note>>();

		List<Pair<UsersNotes, Note>> pairListNotesPinned = new ArrayList<Pair<UsersNotes, Note>>();

		if(!selectedNotes.isEmpty()) {
			
			Collections.sort(selectedNotes, new NoteComp());

			Iterator <Note> itNotes = selectedNotes.iterator();

			while(itNotes.hasNext()) {
				Note note = (Note) itNotes.next();
				UsersNotes userNote = usersNotesDao.get(user.getIdu(), note.getIdn());
				
				if(userNote.getPinned() == 0) {
					pairListNotes.add(new Pair<UsersNotes, Note> (userNote, note));
				} else {
					pairListNotesPinned.add(new Pair<UsersNotes, Note> (userNote, note));
				}

			}
		}
		
		request.setAttribute("listNotes", pairListNotes);

		request.setAttribute("listNotesPinned", pairListNotesPinned);
		
		request.setAttribute("messages", messages);
		
		request.setAttribute("searchRequest", textOfSearch);
		
		
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/AdvSearch.jsp");
		view.forward(request,response);
	}

}
