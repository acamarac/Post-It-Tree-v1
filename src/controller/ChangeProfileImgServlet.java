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
 * Servlet implementation class ChangeProfileImgServlet
 */
@WebServlet("/notes/ChangeProfileImgServlet")
public class ChangeProfileImgServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangeProfileImgServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		ProfileImageDAO pImgDao = new JDBCProfileImageDAOImpl();
		pImgDao.setConnection(conn);
		
		List<ProfileImage> profileImageList = new ArrayList<ProfileImage>();
		profileImageList = pImgDao.getAll();
		
		request.setAttribute("pImageList", profileImageList);
		
		RequestDispatcher view = request.getRequestDispatcher("/WEB-INF/SelectProfileImage.jsp");
		view.forward(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		Connection conn = (Connection) getServletContext().getAttribute("dbConn");
		UserDAO userDao = new JDBCUserDAOImpl();
		userDao.setConnection(conn);
		
		ProfileImageDAO pImgDao = new JDBCProfileImageDAOImpl();
		pImgDao.setConnection(conn);
		
		String nImageString = request.getParameter("selectImage");
		int nImage = Integer.parseInt(nImageString);
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		if(nImage==0) { //Then the user want to have profile image
			user.setUrlImage(null);
		} else {
			ProfileImage pImage = new ProfileImage();
			pImage = pImgDao.get(nImage);
			
			user.setUrlImage(pImage.getUrlimage());
		}
		
		userDao.save(user);
		
		response.sendRedirect("ChangeProfileServlet");
		
	}

}
