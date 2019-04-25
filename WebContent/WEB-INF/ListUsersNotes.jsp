<%@ page language="java" contentType="text/html; charset=UTF-8"     pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html id="list">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link rel="stylesheet"  href="${pageContext.request.contextPath}/css/stylesheet.css"   />
		<title>List of users and languages</title>
	</head>
	<body>
		<h1> Different Lists:</h1> 
		<h2> List of users and notes:</h2>
		<table>
			<thead>
				<tr>
					<th>Username</th>
					<th>User email</th>
					<th>Owner</th>
					<th>Archived</th>
					<th>Pinned</th>
					<th>Title</th>
					<th>Content</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="userNote" items="${usersNotes}">
					<tr> 
						<td>${userNote.first.username}</td>
						<td>${userNote.first.email}</td>
						<td>
							<c:choose>
								<c:when test="${userNote.second.owner==0}">
									<img src="${pageContext.request.contextPath}/img/empty.png" alt="Empty">
								</c:when>
								<c:otherwise>
									<img src="${pageContext.request.contextPath}/img/owner.png" alt="Owner">
								</c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${userNote.second.archived==0}">
									<img src="${pageContext.request.contextPath}/img/empty.png" alt="Empty">
								</c:when>
								<c:otherwise>
									<img src="${pageContext.request.contextPath}/img/archived.png" alt="Archived">
								</c:otherwise>
							</c:choose>
						</td> 
						<td>
							<c:choose>
								<c:when test="${userNote.second.pinned==0}">
									<img src="${pageContext.request.contextPath}/img/empty.png" alt="Empty">
								</c:when>
								<c:otherwise>
									<img src="${pageContext.request.contextPath}/img/pinned.png" alt="Pinned">
								</c:otherwise>
							</c:choose>
						</td> 
						<td>${userNote.third.title}</td>
		    			<td>${userNote.third.content}</td>
					</tr>	
		    	</c:forEach>
		    	</tbody>	
			</table>	
		<h2> List of notes by users:</h2>
		<c:forEach var="noteByUser" items="${notesByUser}"> 
			<h3>User: ${noteByUser.first.username}</h3>
			<h3>Email: ${noteByUser.first.email}</h3>
			<c:forEach var="note" items="${noteByUser.second}">
				<div>
					<h4>${note.second.title}</h4>
		    		<p>${note.second.content}</p> 
						<c:choose>
							<c:when test="${note.first.owner==0}">
								<img src="${pageContext.request.contextPath}/img/empty.png" alt="Empty">
							</c:when>
							<c:otherwise>
								<img src="${pageContext.request.contextPath}/img/owner.png" alt="Owner">
							</c:otherwise>
						</c:choose>
						<c:choose>
							<c:when test="${note.first.archived==0}">
								<img src="${pageContext.request.contextPath}/img/empty.png" alt="Empty">
							</c:when>
							<c:otherwise>
								<img src="${pageContext.request.contextPath}/img/archived.png" alt="Archived">
							</c:otherwise>
						</c:choose> 
						<c:choose>
							<c:when test="${note.first.pinned==0}">
								<img src="${pageContext.request.contextPath}/img/empty.png" alt="Empty">
							</c:when>
							<c:otherwise>
								<img src="${pageContext.request.contextPath}/img/pinned.png" alt="Pinned">
							</c:otherwise>
						</c:choose>
				</div>	
		    </c:forEach>
	    </c:forEach>
	</body>
</html>
