<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta charset="UTF-8">
	<title>Post-it tree</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar_menu_style.css">
</head>


<body>
<div class="navigation">
    <div id="homePage">
        <a href="MainPageServlet">
            <img src="${pageContext.request.contextPath}/img/mainIcon.png" id="logo" alt="Home Page">
        </a>
    </div>

    <div id="menu">

        <ul id="menuList">
            <li class="contentList"><a href="<c:url value="MainPageServlet"/>"  >Notes</a></li>
            <li class="contentList"><a href="<c:url value="ArchivedNotesServlet"/>">Archive</a></li>
            <li class="contentList"><a href="<c:url value="FriendsMainServlet"/>">Friends</a></li>
            <li class="contentList"><a href="<c:url value="AvdSearchServlet"/>">Advanced Search</a></li>
        </ul>

    </div>

		<div id="profileLink">
			<a href="<c:url value="ChangeProfileServlet"/>"> 
			<c:choose>
					<c:when test="${not empty user.urlImage}">
						<img id="profileImaLink" alt="Link to account settings" src="${user.urlImage}">
					</c:when>
					<c:otherwise>
						<img alt="Link to account settings" src="${pageContext.request.contextPath}/img/profileLogo.png" id="profileImaLink">
					</c:otherwise>
			</c:choose> 
			</a>
			<p id="usernameP">${sessionScope.user.username}</p>
		</div>

		<div id="optionButtons">
    	<form action="LogoutServlet" method="post">
    		<input type="submit" id="buttonLogOut" value="Log out">
    	</form>
    </div>
</div>
</body>
</html>