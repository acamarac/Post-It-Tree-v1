<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
       <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Post-it tree</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/FriendsCSS.css">
</head>
<body>

<c:import url="SideBarMenu.jsp"/>

	<div class="content">
	<h1>Delete friend</h1>
	<h2>Are you sure you want to delete this friend?</h2>
		<fieldset id="friendFieldset">
			<legend>Friend</legend>

			<div id="friendImage">
				<c:choose>
					<c:when test="${empty userDelete.urlImage}">
						<img class="profileImage" alt="FriendImage"
							src="${pageContext.request.contextPath}/img/friendIcon.png">
					</c:when>
					<c:otherwise>
						<img class="profileImage" alt="FriendImage"
							src="${userDelete.urlImage}">
					</c:otherwise>
				</c:choose>
			</div>

			<div id="profile">
				<p>Name: ${userDelete.username}</p>
				<p>Email: ${userDelete.email}</p>
			</div>
		</fieldset>
		
		<div id="buttonDelete">
			<form action="DeleteFriendServlet" method="post">
				<input type="hidden" name="iduDelete" value="${userDelete.idu}">
				<input type="submit" value="Delete friend">
			</form>
		</div>
	</div>
</body>
</html>