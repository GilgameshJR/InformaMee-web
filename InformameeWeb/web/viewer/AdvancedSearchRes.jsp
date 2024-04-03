<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <c:set var="currcontext" value="${pageContext.request.contextPath}"/>
    <link href="header.css" rel="stylesheet">
    <link href="Search.css">
    <meta charset="UTF-8">
    <title>Insert title here</title>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<h3><a href="AdvancedSearch.jsp">Ricerca avanzata</a></h3>
<h3><a href="SearchCurrent.jsp">Ricerca allerte correnti</a></h3>
<c:if test="${isglobal==true}"><h1>Risultati in tutta Italia</h1></c:if>
<h3 class="greysubtitle">clicca sui campi CAP per visualizzarli sulla mappa</h3>
<div id="mycontent">
    <c:set var="isEditor" scope="request" value="false"/>
    <jsp:include page="/table/ShowRes.jsp"></jsp:include>
</div>
</body>
</html>