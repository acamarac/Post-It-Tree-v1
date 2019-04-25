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
 * Servlet implementation class AddFriendServlet
 */
@WebServlet("/notes/AddFriendServlet")
public class AddFriendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddFriendServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/AddDeleteFriend.jsp");
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
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		String username = request.getParameter("userFriend");
		
		User userExist = null;
		userExist = userDao.get(username);
		
		List <String> messages = new ArrayList<String>();
		
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
			
			if (friendsDao.areFriends(idu1, idu2)) {
				messages.add("Users are already friends");
				request.setAttribute("messages", messages);
				RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/AddDeleteFriend.jsp");
				view.forward(request,response);
			} else {
				if(idu1 == idu2) {
					messages.add("You can not add yourself as a friend");
					request.setAttribute("messages", messages);
					RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/AddDeleteFriend.jsp");
					view.forward(request,response);
				} else {
					friendsDao.add(idu1, idu2);
					response.sendRedirect("FriendsMainServlet");
				}
			}
			
		} else {
			
			messages.add("Username does not exist");
			request.setAttribute("username", username);
			request.setAttribute("messages", messages);
			
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/AddDeleteFriend.jsp");
			view.forward(request,response);
			
		}
		
	}

}
