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
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/Register.jsp");
		view.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		User user = new User();
		
		user.setUsername(request.getParameter("userNameRegister"));
		user.setEmail(request.getParameter("correoRegistro"));
		user.setPassword(request.getParameter("contraseniaRegistro"));
		
		List <String> validationMessages = new ArrayList<String>();
		
		User userExists = userDao.get(request.getParameter("userNameRegister"));
		if (userExists != null) {
			validationMessages.add("Username already exists");
		}
		
		if (user.validate(validationMessages) && validationMessages.isEmpty()) {
			long idu = userDao.add(user);
			int iduInt = (int) idu;
			user.setIdu(iduInt);
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			response.sendRedirect("notes/MainPageServlet");
		} else {
			request.setAttribute("messages", validationMessages);
			request.setAttribute("user", user);
			RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/Register.jsp");
			view.forward(request, response);
		}
		
	}

}
