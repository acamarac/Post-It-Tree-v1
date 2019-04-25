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
 * Servlet implementation class ChangeProfileServlet
 */
@WebServlet("/notes/ChangeProfileServlet")
public class ChangeProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		
		User user = (User) session.getAttribute("user");
		
		request.setAttribute("user", user);
		
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/ChangeProfile.jsp");
		view.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		
		User newUserConfiguration = new User();
		newUserConfiguration.setIdu(user.getIdu());
		newUserConfiguration.setUsername(username);
		newUserConfiguration.setEmail(email);
		newUserConfiguration.setPassword(password1);
		newUserConfiguration.setUrlImage(user.getUrlImage());
		
		List<String> messages = new ArrayList<String>();
		
		if(!password1.equals(password2)) {
			messages.add("Passwords don't match");
			request.setAttribute("user", newUserConfiguration);
			request.setAttribute("messages", messages);
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/ChangeProfile.jsp");
			view.forward(request,response);
		}
		
		if(newUserConfiguration.validate(messages)) {
			if(userDao.get(newUserConfiguration.getUsername())!=null) {
				messages.add("Username already exists");
				request.setAttribute("user", newUserConfiguration);
				request.setAttribute("messages", messages);
				RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/ChangeProfile.jsp");
				view.forward(request,response);
			} else {
				userDao.save(newUserConfiguration);
				session.removeAttribute("user");
				session.setAttribute("user", newUserConfiguration);
				response.sendRedirect("MainPageServlet");
			}
		} else {
			request.setAttribute("user", newUserConfiguration);
			request.setAttribute("messages", messages);
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/ChangeProfile.jsp");
			view.forward(request,response);
		}
		
		
		
	}

}
