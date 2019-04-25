<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/accountCSS.css">
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/img/logoIcon.ico"/>
<title>Post-it tree</title>
</head>
<body>

<c:import url="SideBarMenu.jsp"/>

<div class="content">

    <h1 id="title">Edit profile</h1>
    <h2>${messages}</h2>

    <form action="?" method="post">

			<div id="profileImage">
				<a href="<c:url value="ChangeProfileImgServlet"/>">
					<c:choose>
						<c:when test="${not empty user.urlImage}">
							<img id="perfil_image" alt="profile image" src="${user.urlImage}">
						</c:when>
						<c:otherwise>
							<img alt="profile image" src="https://images.vexels.com/media/users/3/129733/isolated/preview/a558682b158debb6d6f49d07d854f99f-casual-avatar-silueta-masculina-by-vexels.png" id="perfil_image">
						</c:otherwise>
					</c:choose>
				</a>
			</div>

			<div id="userDetails">
            <input type="text" value="${user.username}" class="formElement" name="username"><br>
            <input type="text" value="${user.email}" class="formElement" name="email"><br>
            <input type="password" placeholder="New password" class="formElement" name="password1"><br>
            <input type="password" placeholder="Confirm new password" class="formElement" name="password2">
        </div>

        <div id=submitB>
            <input type="submit" value="Save changes" class="sButton" id="bSaveChanges">
        </div>
    </form>
    
    <div id="deleteAccount">
    	<form action="DeleteAccountServlet" method="post">
    		<input type="submit" id="deleteButton" value="Delete account">
    	</form>
    </div>

</div>
</body>
</html>