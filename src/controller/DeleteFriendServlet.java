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
 * Servlet implementation class DeleteFriendServlet
 */
@WebServlet("/notes/DeleteFriendServlet")
public class DeleteFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteFriendServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		FriendsDAO friendsDao = new JDBCFriendsDAOImpl();
		friendsDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User userSession = (User) session.getAttribute("user");
		
		String iduString = request.getParameter("idu");
		
		long idu = Long.parseLong(iduString);
		
		User user = userDao.get(idu);
		
		if(user==null) {
			response.sendRedirect("FriendsMainServlet");
		}else {
			
			int idu1, idu2;
			int idUserFriend = user.getIdu();
			if(idUserFriend < userSession.getIdu()) { //To insert friends in order. Idu1 is always the lowest value
				idu1 = idUserFriend;
				idu2 = userSession.getIdu();
			} else {
				idu1 = userSession.getIdu();
				idu2 = idUserFriend;
			}
			
			if(friendsDao.areFriends(idu1, idu2)) {
				request.setAttribute("userDelete", user);
				
				RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/CheckFriend.jsp");
				view.forward(request,response);
			} else {
				response.sendRedirect("FriendsMainServlet");
			}
		}
		
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
		
		UsersNotesDAO usersNotesDao = new JDBCUsersNotesDAOImpl();
		usersNotesDao.setConnection(conn);
		
		String iduDeleteString = request.getParameter("iduDelete");
		long iduDelete = Long.parseLong(iduDeleteString);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		User userExist = null;
		userExist = userDao.get(iduDelete);
		
		List<String> messages = new ArrayList<String>();
		
		if(userExist != null) {
			
			int idu1, idu2;
			int idUserFriend = userExist.getIdu();
			if(idUserFriend < user.getIdu()) { //To insert friends in order. Idu1 is always the lowest value
				idu1 = idUserFriend;
				idu2 = user.getIdu();
			} else {
				idu1 = user.getIdu();
				idu2 = idUserFriend;
			}
			
			if (!friendsDao.areFriends(idu1, idu2)) {
				messages.add("Users are not friends");
				request.setAttribute("messages", messages);
				response.sendRedirect("FriendsMainServlet");
			} else {
				
				List<UsersNotes> userNotes = usersNotesDao.getAllByUser(user.getIdu()); //Delete all the userNotes shared by that friend
				for (int i=0; i<userNotes.size(); i++) {
					long iduOwner = usersNotesDao.getOwner(userNotes.get(i).getIdn());
					
					if(iduOwner == userExist.getIdu())
						usersNotesDao.delete(user.getIdu(), userNotes.get(i).getIdn());
				}
				
				List<UsersNotes> userFriendNotes = usersNotesDao.getAllByUser(userExist.getIdu()); //Delete all the userNotes that the user shared with that friend
				for(int i=0; i<userFriendNotes.size(); i++) {
					long iduOwner = usersNotesDao.getOwner(userFriendNotes.get(i).getIdn());
					
					if(iduOwner == user.getIdu()) {
						usersNotesDao.delete(userExist.getIdu(), userFriendNotes.get(i).getIdn());
					}
				}
				
				friendsDao.delete(idu1, idu2);
				response.sendRedirect("FriendsMainServlet");
			}
			
		} else {
			
			messages.add("User does not exist");
			request.setAttribute("messages", messages);
			response.sendRedirect("FriendsMainServlet");
			
		}
		
	}

}
