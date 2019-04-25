<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="shortcut icon" href="${pageContext.request.contextPath}/img/logoIcon.ico"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/SelectProfileImage.css">
<title>Post-it tree</title>
</head>
<body>

<c:import url="SideBarMenu.jsp"/>

	<div class="content">
		<h1 id="headerImage">Select your profile image</h1>
		<div id="images">
			<form method="post" action=?>
				<c:forEach var="image" items="${pImageList}">
					<c:choose>
						<c:when test="${image.idi == 0 }">
							<input type="radio" name="selectImage" value=0 id="noImage"
								checked="checked"> No profile image<br>
						</c:when>
						<c:otherwise>
							<input type="radio" name="selectImage" value="${image.idi}">
							<img class="imgShow" alt="Dog image" src="${image.urlimage}">
						</c:otherwise>
					</c:choose>
				</c:forEach>

				<br> <input type="submit" value="Confirm" id="bSubmitImg">
			</form>
		</div>
	</div>

</body>
</html>