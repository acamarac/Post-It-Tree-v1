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
			<h1> ${requestScope.CheckType} note</h1>
		</div>
		<div id="noteForm">
        	<div id="textNote">
                <fieldset class="notesFieldset"><legend>Note</legend>
                <div id="wrapNote">
                   <ul>
				   		<li class="ulCheckNote"> <strong>Title</strong>: ${note.title} </li>
						<li class="ulCheckNote"> <strong>Content</strong>: ${note.content} </li>
				   </ul>
				</div>
                </fieldset>
            </div>
        </div>
        <div id="selectedColor">
                <fieldset class="notesFieldset"><legend>Color</legend>
				<c:choose>
					<c:when test="${note.color==0}">
						<p> Note color: white </p>
					</c:when>
					<c:when test="${note.color==1}">
						<p> Note color: pink </p>
					</c:when>
					<c:when test="${note.color==2}">
						<p> Note color: grey </p>
					</c:when>
					<c:when test="${note.color==3}">
						<p> Note color: blue </p>
					</c:when>
					<c:when test="${note.color==4}">
						<p> Note color: yellow </p>
					</c:when>
				</c:choose>
			</fieldset>
        </div>
            <div id="submitNote">
                <form method="post" action=?>
				<input id="submitButton" type="submit" value="${requestScope.CheckType} note" />
				</form>
            </div>
        </div>
	</div>
</body>
</html>