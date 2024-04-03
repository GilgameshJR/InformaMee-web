<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <c:set var="currcontext" value="${pageContext.request.contextPath}"/>
    <meta charset="UTF-8">
    <title>Ricerca avanzata</title>
    <link href="${currcontext}/jquery/jquery-ui.css" rel="stylesheet">
    <link href="${currcontext}/jquery/jquery.ui.timepicker.css" rel="stylesheet">
    <link href="Search.css" rel="stylesheet">
    <link href="header.css" rel="stylesheet">
    <link href="${currcontext}/leaflet/leaflet.css" rel="stylesheet">
    <link href="${currcontext}/map/map.css" rel="stylesheet">

</head>
<body>
<span id="ServletContextPath" data-servpath="${currcontext}"></span>
<jsp:include page="header.jsp"></jsp:include>
<h3><a href="SearchCurrent.jsp">Ricerca allerte correnti</a></h3>
<div id="recentscontainer"></div>
<div id="mycontent">
    <h3>Quando?</h3>
    <form method="get" action="FetchAdvanced" id="ToSubmit" autocomplete="off">
        <input type="radio" id="instantradio" checked name="timecode" value="instant">
        <label for="instantradio">Istante temporale</label>
        <input type="radio" name="timecode" id="intervalradio" value="interval">
        <label for="intervalradio">Intervallo temporale</label>
        <div class="instantgeneric">
            <p>Il <input type="text" id="datetxt" class="instantinput"> alle <input type="text" id="timetxt"
                                                                                    class="instantinput"></p>
        </div>
        <div class="intervalgeneric" style="display: none">
            <p>Dal <input type="text" disabled="disabled" id="begindatetxt" class="intervalinput"> alle <input
                    type="text" disabled="disabled" id="begintimetxt" class="intervalinput"></p>
            <p>Al <input type="text" disabled="disabled" class="intervalinput" id="enddatetxt"> alle <input type="text"
                                                                                                            disabled="disabled"
                                                                                                            class="intervalinput"
                                                                                                            id="endtimetxt">
            </p>
            <p><label>Lunghezza intervallo: </label><label id="durationDisp" style="color: red">Scegli l'inizio e la
                fine</label></p>
        </div>
        <p>
        <h3>Cosa?</h3>
        <p class="redalert" id="emptytypealert"></p>
        <input type="hidden" id="caplist" name="cap">
        <input type="checkbox" id="isWeather" name="isW" value="true">
        <label for="isWeather">Atmosferico</label>
        <div class="weatherinputsglob" style="display: none">
            <label for="weatherdetail">, in particolare</label>
            <select id="weatherdetail" class="weatherinputs" required name="weatherdetail">
                <option selected value="-1">Qualsiasi tipo</option>
                <option value="1">Alluvione</option>
                <option value="2">Temporali e fulmini</option>
                <option value="3">Frana</option>
                <option value="4">Pioggia e grandine</option>
                <option value="5">Neve e gelo</option>
                <option value="6">Valanga</option>
                <option value="7">Nebbia</option>
                <option value="8">Venti e mareggiate</option>
                <option value="9">Ciclone</option>
                        <option value="10">Altro</option>
            </select>
        </div>
        <br>
        <input type="checkbox" id="isSeismic" name="isS" value="true">
        <label for="isSeismic">Sismico</label><br>
        <input type="checkbox" id="isTerrorist" name="isT" value="true">
        <label for="isTerrorist">Terroristico</label><br>

        <input class="intervalinput" disabled="disabled" type="hidden" id="begin" name="begin">
        <input class="intervalinput" disabled="disabled" type="hidden" id="end" name="end">
        <input class="instantinput" type="hidden" id="instant" name="instant">

        <div id="map"></div>
        <h3>Dove?</h3>
        <p class="greysubtitle">scrivi i CAP o clicca o trascina col tasto destro sulla mappa, oppure lascia vuoto</p>
        <div id="capinputcontainer">
            <p id="cappar0">
                <input id="cap0" type="text" placeholder="tutta Italia">
                <span id="capvalid0"></span>
            </p>
            <div id="morecapcontainer"></div>
            <button id="capbtn" type="button">+</button>
            <button type="button" id="removeall">- tutti</button>
        </div>
        <br>
        <h3>Come?</h3>
        <label for="danger">Pericolo: </label>
        <select required name="danger" id="danger">
            <option value="-1" selected>Qualsiasi</option>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
        </select><br>
        <p><input type="submit" id="confirm" class="buttonsubmit" value="Cerca evento"></p>
    </form>
</div>
<script src="${currcontext}/jquery/jquery.js"></script>
<script src="${currcontext}/leaflet/leaflet.js"></script>
<script src="${currcontext}/jquery/jquery-ui.js"></script>
<script src="${currcontext}/jquery/jquery.ui.timepicker.js"></script>
<script src="${currcontext}/map/L.CanvasLayer.js"></script>

<script src="${currcontext}/map/CapInputsHandler.js"></script>
<script src="${currcontext}/map/mapReverse.js"></script>
<script src="RecentSearchesCommon.js"></script>
<script src="AdvancedSearch.js"></script>
</body>
</html>