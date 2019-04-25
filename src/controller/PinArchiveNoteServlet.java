package controller;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.*;
import dao.*;

/**
 * Servlet implementation class PinNoteServlet
 */
@WebServlet("/notes/PinArchiveNoteServlet")
public class PinArchiveNoteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PinArchiveNoteServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public void pinUnpinNote(UsersNotes userNotes, UsersNotesDAO userNotesDao) {
    	if(userNotes.getPinned()==1) { //Then unpin note
			userNotes.setPinned(0);
			userNotesDao.save(userNotes);
		} else {
			userNotes.setPinned(1);
			userNotesDao.save(userNotes);
		}
    }
    
    public void archiveNote(UsersNotes userNotes, UsersNotesDAO userNotesDao) {
    	if(userNotes.getArchived()==1) { //Then the note is archived
			userNotes.setArchived(0);
			userNotesDao.save(userNotes);
		} else {	
			userNotes.setArchived(1);
			userNotesDao.save(userNotes);
		}
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UsersNotesDAO userNotesDao = new JDBCUsersNotesDAOImpl();
		userNotesDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		Integer idu = user.getIdu();
		long iduLong = idu.longValue();
		
		String idn = request.getParameter("idn");
		long idnLong = Long.parseLong(idn);
		
		UsersNotes userNotes = userNotesDao.get(iduLong, idnLong);
		
		String pin = request.getParameter("pin");
		
		Integer pinInt = Integer.parseInt(pin);
		
		switch (pinInt) {
		case 0:
			archiveNote(userNotes, userNotesDao);
			break;
			
		case 1:
			pinUnpinNote(userNotes, userNotesDao);
			break;

		default:
			break;
		}
		
		if(userNotes.getArchived()==1 && pinInt==0) { //Now it is archived but we want to continue in the main page
			response.sendRedirect("MainPageServlet");
		} else {
			if(userNotes.getArchived()==0 && pinInt==0) { //Now it is not archived but we want to continue in the archived notes page
				response.sendRedirect("ArchivedNotesServlet");
			} else {
				if(userNotes.getArchived()==1) {
					response.sendRedirect("ArchivedNotesServlet");
				} else {
					response.sendRedirect("MainPageServlet");
				}
			}
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
