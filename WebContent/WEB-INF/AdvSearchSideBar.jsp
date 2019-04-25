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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar_menu_style.css">
</head>
<body>

	<div class="navigation">
		<div id="homePage">
			<a href="MainPageServlet"> <img
				src="${pageContext.request.contextPath}/img/mainIcon.png" id="logo"
				alt="Home Page">
			</a>
		</div>

		<div id="formSearch">
			<form action=? method="post">
				<div id="searchOptions">
					<input type="text" id="searchText" name="search" placeholder="Search..." value="${searchRequest}"> 
					<select id="selectAdv" name="searchOption">
						<option value="title" selected="selected">Title</option>
						<option value="content">Content</option>
						<option value="all">All</option>
					</select>
				</div>

				<div class="checkboxSearch">
				<fieldset class ="fieldSearch">
					<legend>Colors</legend>
					<input type="checkbox" name="color" value="0">White<br>
					<input type="checkbox" name="color" value="1">Pink<br>
					<input type="checkbox" name="color" value="2">Grey<br>
					<input type="checkbox" name="color" value="3">Blue<br>
					<input type="checkbox" name="color" value="4">Yellow<br>
				</fieldset>
				</div>

				<div class="checkboxSearch">
				<fieldset class ="fieldSearch"><legend>Shared</legend>
					<c:forEach var="friend" items="${FriendsList}">
						<input type="checkbox" name="friendSearch" value="${friend.idu}">${friend.username}<br>
					</c:forEach>
					<select class="selectAdv" name="sharing">
						<option value="sharedBy" selected="selected">Shared by</option>
						<option value="sharedWith">Shared with</option>
					</select>
				</fieldset>
				</div>
				
				<input type="submit" value="Search">
			</form>
		</div>
	</div>

</body>
</html>