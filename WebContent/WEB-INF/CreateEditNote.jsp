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
            <h1>Create new note</h1>
            <p> ${messages} </p>
        </div>
        <div id="noteForm">
            <form action=? method="post">
  				<input type="hidden" name="idn" value="${note.idn}">
                <div id="textNote">
                    <fieldset class="notesFieldset"><legend>Note</legend>
                    
                    	<input type="text" name="title" id="noteTitle" placeholder="Title" value="${note.title}">
                    	<textarea id="contentNote" name="content" placeholder="Start typing your note...">${note.content}</textarea>
                    
                    </fieldset>
                </div>
                <div id="changeColor">
                    <fieldset class="notesFieldset"><legend>Color</legend>
                    	<select name="selectColor">
                    		<option value="0">White</option>
  							<option value="1">Pink</option>
  							<option value="2">Grey</option>
  							<option value="3">Blue</option>
  							<option value="4">Yellow</option>
						</select>
                    
                    </fieldset>
                </div>
                <div id="submitNote">
					<c:choose>
						<c:when test="${not empty note.idn}">
							<input type="submit" id="submitButton" value="Edit note">
						</c:when>
						<c:otherwise>
							<input type="submit" id="submitButton" value="Create note">
						</c:otherwise>
					</c:choose>


				</div>
            </form>
        </div>
    </div>
</div>
</body>
</html>