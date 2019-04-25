package controller;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
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
 * Servlet implementation class SearchServlet
 */
@WebServlet("/notes/SearchServlet")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
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
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		String searchText = request.getParameter("search");
		String option = request.getParameter("searchOption");
		List <Note> selectNotes = new ArrayList<Note>();
		
		switch (option) {
		
			case "all":
				selectNotes = noteDao.getAllBySearchAll(searchText);
				break;
			
			case "title":
				selectNotes = noteDao.getAllBySearchTitle(searchText);
				break;
			
			case "content":
				selectNotes = noteDao.getAllBySearchContent(searchText);
				break;
			
			default:
			
		}
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		long idu = user.getIdu();
		
		List <Note> userSelectNotes = new ArrayList<Note>();

		if (!selectNotes.isEmpty()) {
			for (int i=0; i<selectNotes.size(); i++) {
				if(usersNotesDao.get(idu, selectNotes.get(i).getIdn()) != null) { //Then the user doesn't have this note
					userSelectNotes.add(selectNotes.get(i));
				}
			}
		}

		List<Pair<UsersNotes, Note>> pairListNotes = new ArrayList<Pair<UsersNotes, Note>>();

		List<Pair<UsersNotes, Note>> pairListNotesPinned = new ArrayList<Pair<UsersNotes, Note>>();

		if(!userSelectNotes.isEmpty()) {
			
			Collections.sort(userSelectNotes, new NoteComp());

			Iterator <Note> itNotes = userSelectNotes.iterator();

			while(itNotes.hasNext()) {
				Note note = (Note) itNotes.next();
				UsersNotes userNote = usersNotesDao.get(idu, note.getIdn());
				
				if(userNote.getPinned() == 0) {
					pairListNotes.add(new Pair<UsersNotes, Note> (userNote, note));
				} else {
					pairListNotesPinned.add(new Pair<UsersNotes, Note> (userNote, note));
				}

			}
		}

		request.setAttribute("listNotes", pairListNotes);

		request.setAttribute("listNotesPinned", pairListNotesPinned);
		
		request.setAttribute("textSearch", searchText);

		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/MainPage.jsp");
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
