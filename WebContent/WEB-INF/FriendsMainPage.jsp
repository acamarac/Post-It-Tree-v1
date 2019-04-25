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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/noteCSS.css">
</head>
<body>

	<c:import url="SideBarMenu.jsp"/>

	<div class="content">
		<h1 id="mainTitle">Friends</h1>
		<p>${messages}</p>

		<ul class="addFriend">
			<li><a href="<c:url value="AddFriendServlet"/>">Add friend</a><li>
		</ul>
		<div id="listFriends">
			<c:forEach var="friend" items="${friendsList}">
				<fieldset id="friendFieldset">
					<legend>Friend</legend>

					<div id="friendImage">
						<c:choose>
							<c:when test="${empty friend.urlImage}">
								<img class="profileImage" alt="FriendImage"
									src="${pageContext.request.contextPath}/img/friendIcon.png">
							</c:when>
							<c:otherwise>
								<img class="profileImage" alt="FriendImage"
									src="${friend.urlImage}">
							</c:otherwise>
						</c:choose>
					</div>


					<div id="profile">
						<p>Name: ${friend.username}</p>
						<p>Email: ${friend.email}</p>
					</div>

					<div id="sharedNotes">
						<p>Notes of that user shared with me:</p>

						<c:forEach var="note" items="${NotesList}">
							<c:if test="${note.first.idu == friend.idu}">
								<div id="wrapTitle">
									<div class="color${note.second.color}">
										<p class="NoteTitle">${note.second.title}</p></div>
								</div>
							</c:if>
						</c:forEach>

						<p>Notes that I share with that user:</p>

						<c:forEach var="noteOwner" items="${NotesListUserOwner}">
							<c:if test="${noteOwner.first.idu == friend.idu}">
								<div id="wrapTitle">
									<div class="color${noteOwner.second.color}">
										<p class="NoteTitle">${noteOwner.second.title}</p></div>
								</div>
							</c:if>
						</c:forEach>
					</div>
					<div id="deleteFriend">
						<ul id="deleteUl">
							<li><a
								href="<c:url value="DeleteFriendServlet?idu=${friend.idu}"/>">Delete
									friend</a></li>
						</ul>
					</div>
				</fieldset>
			</c:forEach>
		</div>
	</div>

</body>
</html>