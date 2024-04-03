<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <c:set var="currcontext" value="${pageContext.request.contextPath}"/>
    <link href="${currcontext}/table/table.css" rel="stylesheet">
    <link href="header.css" rel="stylesheet">
    <link href="Search.css" rel="stylesheet">
    <meta charset="UTF-8">
    <title>Insert title here</title>

    <link href="${currcontext}/leaflet/leaflet.css" rel="stylesheet">
    <link href="${currcontext}/map/map.css" rel="stylesheet">
</head>
<body>
<span id="ServletContextPath" data-servpath="${currcontext}"></span>
<jsp:include page="header.jsp"></jsp:include>
<h3><a href="AdvancedSearch.jsp">Ricerca avanzata</a></h3>
<div id="map"></div>
<div id="recentscontainer"></div>
<div id="mycontent">
    <h1>Ricerca allerte correnti</h1>
    <h3>Inserisci i CAP delle zone che ti interessano</h3>
    <h3 class="greysubtitle">scrivili o clicca o trascina col tasto destro sulla mappa</h3>
    <form action="FetchCurrent" method="get" id="ToSubmit" autocomplete="off">
        <input type="hidden" id="caplist" name="cap">
        <div id="capinputcontainer">
            <p id="cappar0">
                <input id="cap0" type="text">
                <span id="capvalid0"></span>
            </p>
            <div id="morecapcontainer"></div>
            <button id="capbtn" type="button">+</button>
            <button type="button" id="removeall">- tutti</button>
        </div>
        <p><input type="submit" value="Ricerca allerte correnti"></p>
    </form>
</div>
<script src="${currcontext}/jquery/jquery.js"></script>
<script src="${currcontext}/leaflet/leaflet.js"></script>
<script src="${currcontext}/map/L.CanvasLayer.js"></script>

<script src="${currcontext}/map/CapInputsHandler.js"></script>
<script src="${currcontext}/map/mapReverse.js"></script>
<script src="RecentSearchesCommon.js"></script>
<script src="SearchCurrent.js"></script>
</body>
</html>