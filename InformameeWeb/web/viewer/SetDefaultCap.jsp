<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <c:set var="currcontext" value="${pageContext.request.contextPath}"/>
    <meta charset="UTF-8">
    <title>Title</title>
    <script src="${currcontext}/jquery/jquery.js"></script>
    <link href="Search.css" rel="stylesheet">
    <link href="${currcontext}/leaflet/leaflet.css" rel="stylesheet">
    <link href="${currcontext}/map/map.css" rel="stylesheet">
</head>
<body>
<span id="ServletContextPath" data-servpath="${currcontext}"></span>
<div id="map"></div>
<div id="mycontent">
    <h1>Dove ti trovi?</h1>
    <h3>Inserisci il CAP della tua residenza</h3>
    <h3 class="greysubtitle">scrivilo o clicca col tasto destro sulla mappa</h3>
    <form action="SetCookieAndRedirect" method="get" id="ToSubmit" autocomplete="off">
        <p>
            <input id="cap" type="text" name="cap">
            <span id="capvalid"></span>
        </p>
        <p>
            <input type="checkbox" id="storecap" name="store" value="on">
            <label for="storecap">Memorizza come predefinito</label>
        </p>
        <p><input type="submit" value="Conferma"></p>
        <h3>oppure effettua direttamente una ricerca</h3>
        <h3><a href="AdvancedSearch.jsp">Ricerca avanzata</a></h3>
        <h3><a href="SearchCurrent.jsp">Ricerca allerte correnti</a></h3>
    </form>
</div>
<script src="${currcontext}/leaflet/leaflet.js"></script>
<script src="${currcontext}/map/L.CanvasLayer.js"></script>

<script src="${currcontext}/map/CapInputsHandler.js"></script>
<script src="${currcontext}/map/mapReverse.js"></script>
<script src="SetDefaultCap.js"></script>
</body>
</html>