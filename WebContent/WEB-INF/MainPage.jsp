<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Post-it tree</title>
 <meta charset="UTF-8">
    <title>Post-it tree</title>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/img/logoIcon.ico"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main_page.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/noteCSS.css">
</head>
<body>


	<c:import url="/WEB-INF/SideBarMenu.jsp" />

	<div class="topNavigation">

		<div id="searchFunction">
			<form method="get" action="SearchServlet">
				<input type="text" id="searchInput" name="search"
					placeholder="Search..." value="${textSearch }"> <select id="selectSearch"
					name="searchOption">
					<option value="all">All</option>
					<option value="title">Title</option>
					<option value="content">Content</option>
				</select> <input type="submit" value="Search" id="searchButton">
			</form>
		</div>
	</div>


	<div class="content">

	<div id="introduction">
	<c:choose>
		<c:when test="${requestScope.page == 'ArchivedNotes'}">
			<h1>Archived notes</h1>
		</c:when>
		<c:otherwise>
			<ul id="createNote">
        		<li class="contentList"><a href="<c:url value="CreateNoteServlet"/>">Create new note</a></li>
    		</ul>
		</c:otherwise>
	</c:choose>
	</div>

    <div class="notesGeneralDiv">
        <fieldset class="notesFieldset">
            <legend>Pinned notes</legend>
            
            
            
            <c:if test="${fn:length(listNotesPinned) == 0}">
            	<p>Pin notes for a quick visualization<p>
            </c:if>

			<c:if test="${fn:length(listNotesPinned) > 0}">
			<c:forEach var="notePinned" items="${listNotesPinned}">
            <div class="note">
            <div class="color${notePinned.second.color}">
                <div class="topNoteDiv">
                    <div class="titleDiv">
                        <p>${notePinned.second.title}</p>
                    </div>
                    <div class="optionDiv">
                        <img src="${pageContext.request.contextPath}/img/optionsIcon.png" alt="Dropdown Menu" class="optionIcon">
                        <div class="dropableContent">
                            <a href="<c:url value="EditNoteServlet?idn=${notePinned.second.idn}"/>" > Edit note </a>
                            <a href="<c:url value="PinArchiveNoteServlet?idn=${notePinned.second.idn}&pin=1"/>">Unpin note</a>
                            <a href="<c:url value="PinArchiveNoteServlet?idn=${notePinned.second.idn}&pin=0"/>">
									<c:choose>
										<c:when test="${requestScope.page == 'ArchivedNotes'}">
											Unarchive note
										</c:when>
										<c:otherwise>
											Archive note
										</c:otherwise>
									</c:choose></a>
                            <a href="<c:url value="DeleteNoteServlet?idn=${notePinned.second.idn}"/>">Delete note</a>
                            <c:if test="${notePinned.first.owner==1}">
                           		<a href="<c:url value="ShareNoteServlet?idn=${notePinned.second.idn}"/>" > Share note </a>
                           		<a href="<c:url value="StopSharingNoteServlet?idn=${notePinned.second.idn}"/>" > Stop sharing note </a>
                            </c:if>
                        </div>
                    </div>
                </div>
                <div class="contentNote">
                    <p>
                    	${notePinned.second.content}
                    </p>
                </div>
                <div class="caracNotes">
                	<c:if test="${notePinned.first.owner==1}">
                		<img alt="Owner Image" src="${pageContext.request.contextPath}/img/owner.png">
                	</c:if>
                	<c:if test="${notePinned.first.pinned==1}">
                			<img alt="Pinned Image" src="${pageContext.request.contextPath}/img/pinned.png">
                	</c:if>
                	<c:if test="${notePinned.first.archived==1}">
                			<img alt="Archived Image" src="${pageContext.request.contextPath}/img/archived.png">
                	</c:if>
                </div>
                </div>
            </div>
		    </c:forEach>
		    </c:if>
		    </fieldset>
		    </div>
		    
		<div class="notesGeneralDiv">
        <fieldset class="notesFieldset">
            <legend>Other notes</legend>
            
            <c:if test="${fn:length(listNotes) == 0}">
            	<p>There aren't notes in this section<p>
            </c:if>
            
            <c:if test="${fn:length(listNotes) > 0}">
            <c:forEach var="note" items="${listNotes}">
            <div class="note">
            <div class="color${note.second.color}">
                <div class="topNoteDiv">
                    <div class="titleDiv">
                        <p>${note.second.title}</p>
                    </div>
                    <div class="optionDiv">
                        <img src="${pageContext.request.contextPath}/img/optionsIcon.png" alt="Dropdown Menu" class="optionIcon">
                        <div class="dropableContent">
                            <a href="EditNoteServlet?idn=${note.second.idn}">Edit note</a>
                            <a href="<c:url value="PinArchiveNoteServlet?idn=${note.second.idn}&pin=1"/>">Pin note</a>
                            <a href="<c:url value="PinArchiveNoteServlet?idn=${note.second.idn}&pin=0"/>">
                            	<c:choose>
									<c:when test="${requestScope.page == 'ArchivedNotes'}">
										Unarchive note
									</c:when>
									<c:otherwise>
										Archive note
									</c:otherwise>
								</c:choose></a>
                            <a href="<c:url value="DeleteNoteServlet?idn=${note.second.idn}"/>">Delete note</a>
                            <c:if test="${note.first.owner==1}">
                             <a href="<c:url value="ShareNoteServlet?idn=${note.second.idn}"/>" > Share note </a>
                             <a href="<c:url value="StopSharingNoteServlet?idn=${note.second.idn}"/>" > Stop sharing note </a>
                       		</c:if>
                        </div>
                    </div>
                </div>
                <div class="contentNote">
                    <p>
            			${note.second.content}
                    </p>
                </div>
                <div class="caracNotes">
                	<c:if test="${note.first.owner==1}">
                		<img alt="Owner Image" src="${pageContext.request.contextPath}/img/owner.png">
                	</c:if>
                	<c:if test="${note.first.archived==1}">
                			<img alt="Archived Image" src="${pageContext.request.contextPath}/img/archived.png">
                	</c:if>
                </div>
                </div>
            </div>
            </c:forEach>
            </c:if>
		</fieldset>
		</div>
</div>
            
	
</body>
</html>