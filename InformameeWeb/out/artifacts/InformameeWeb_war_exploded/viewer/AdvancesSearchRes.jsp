<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link href="/InformameeWeb_war_exploded/leaflet/leaflet.css" rel="stylesheet">
    <link href="/InformameeWeb_war_exploded/mapcommon/map.css" rel="stylesheet">
    <link href="header.css" rel="stylesheet">
    <link href="/InformameeWeb_war_exploded/tablecommon/table.css" rel="stylesheet">
    <link href="Search.css">
    <meta charset="UTF-8">
    <title>Insert title here</title>
    <script src="/InformameeWeb_war_exploded/jquery/jquery.js"></script>
    <script src="/InformameeWeb_war_exploded/leaflet/leaflet.js"></script>
    <script src="/InformameeWeb_war_exploded/mapcommon/mapLookup.js"></script>
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<h3><a href="AdvancedSearch.jsp">Ricerca avanzata</a></h3>
<h3><a href="SearchCurrent.jsp">Ricerca allerte correnti</a></h3>
<c:choose>
<c:when test="${capsearch!=null}"><h1>Risultati al CAP <c:out value="${capsearch}"></c:out></h1></c:when>
<c:otherwise><h1>Risultati in tutta Italia</h1></c:otherwise>
</c:choose>
<div id="map"></div>
<div id="mycontent">
<jsp:include page="ShowRes.jsp"></jsp:include>
</div>
</body>
</html>