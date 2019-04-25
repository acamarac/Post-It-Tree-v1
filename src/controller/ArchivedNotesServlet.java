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

import dao.JDBCNoteDAOImpl;
import dao.JDBCUsersNotesDAOImpl;
import dao.NoteDAO;
import dao.UsersNotesDAO;
import model.Note;
import model.User;
import model.UsersNotes;
import util.Pair;
import util.UsersNotesComp;

/**
 * Servlet implementation class dzv
 */
@WebServlet("/notes/ArchivedNotesServlet")
public class ArchivedNotesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ArchivedNotesServlet() {
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
		
		NoteDAO notesDao = new JDBCNoteDAOImpl();
		notesDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		
		List <UsersNotes> userNotesList = userNotesDao.getAllByUser(user.getIdu());
		
		Collections.sort(userNotesList, new UsersNotesComp());
		
		Iterator <UsersNotes> itUserNotes = userNotesList.iterator();
		
		List<Pair<UsersNotes, Note>> pairListArchivedNotes = new ArrayList<Pair<UsersNotes, Note>>();
		
		List<Pair<UsersNotes, Note>> pairListArchivedNotesPinned = new ArrayList<Pair<UsersNotes, Note>>();
		
		while(itUserNotes.hasNext()) {
			UsersNotes userNote = (UsersNotes) itUserNotes.next();
			Note note = notesDao.get(userNote.getIdn());
			if(userNote.getPinned()==1 && userNote.getArchived()==1) {
				pairListArchivedNotesPinned.add(new Pair<UsersNotes, Note> (userNote, note));
			} else {
				if(userNote.getArchived()==1)
					pairListArchivedNotes.add(new Pair<UsersNotes, Note> (userNote, note));
			}
			
		}
		
		request.setAttribute("listNotes", pairListArchivedNotes);
		
		request.setAttribute("listNotesPinned", pairListArchivedNotesPinned);

		request.setAttribute("page", "ArchivedNotes");
		
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/MainPage.jsp");
		view.forward(request, response);
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
