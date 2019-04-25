<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Post-it tree</title>
<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/postItTreeCircleBuena.ico"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/create_noteCSS.css">
</head>
<body>


<c:import url="SideBarMenu.jsp"/>
	<div class="content">
		<div id="noteSpace">
			<div id="introduction">
				<c:choose>
					<c:when test="${requestScope.page == 'Stop' }">
						<h1>Stop sharing note</h1>
					</c:when>
					<c:otherwise>
						<h1>Share note</h1>
					</c:otherwise>
				</c:choose>
				
				<p>${messages}</p>
			</div>

			<div id="noteForm">
				<div id="textNote">
					<fieldset class="notesFieldset">
						<legend>Note</legend>
						<div id="wrapNote">
							<ul>
								<li class="ulCheckNote"><strong>Title</strong>:
									${note.title}</li>
								<li class="ulCheckNote"><strong>Content</strong>:
									${note.content}</li>
							</ul>
						</div>
					</fieldset>
				</div>
			</div>

			<div id="friendShare">
				<form action=? method="post">
					<c:choose>
						<c:when test="${requestScope.page == 'Stop' }">
							<p>You are currently sharing this note with the following
								users. Select the users you want to stop sharing this note:</p>
						</c:when>
						<c:otherwise>
							<p>Select the user/s you want to share this note with</p>
						</c:otherwise>
					</c:choose>


					<c:choose>
						<c:when test="${empty friends}">
							<c:choose>
								<c:when test="${requestScope.page == 'Stop' }">
									<p>You are not sharing this note</p>
								</c:when>
								<c:otherwise>
									<p>Add friends to share notes</p>
								</c:otherwise>
							</c:choose>

						</c:when>
						<c:otherwise>
							<c:forEach var="friend" items="${friends}">
								<input type="checkbox" name="friendShare" value="${friend.idu}">${friend.username}<br>
							</c:forEach>
							<input type="hidden" name="idn" value="${note.idn}">
							<c:choose>
								<c:when test="${requestScope.page == 'Stop' }">
									<input type="submit" value="Stop sharing" id="submitButton">
								</c:when>
								<c:otherwise>
									<input type="submit" value="Share" id="submitButton">
								</c:otherwise>
							</c:choose>
						</c:otherwise>
					</c:choose>

				</form>
			</div>
		</div>
	</div>

</body>
</html>