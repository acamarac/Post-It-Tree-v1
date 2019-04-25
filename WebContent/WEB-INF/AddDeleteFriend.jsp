<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/FriendsCSS.css">
<title>Post-it tree</title>
</head>
<body>

	<c:import url="SideBarMenu.jsp" />
	<div class="content">
		<h1>Add a new friend</h1>
		<p>Introduce the username of the user you want to add as a friend:</p>

		<p>${messages}</p>

		<form action=? method="post">
			<input type="text" name="userFriend" placeholder="Username" value="${username}"required>
			<br> <input type="submit" value="Continue">

		</form>
	</div>

</body>
</html>