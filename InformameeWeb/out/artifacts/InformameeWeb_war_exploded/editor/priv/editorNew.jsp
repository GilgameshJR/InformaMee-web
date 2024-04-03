<%--
  Created by IntelliJ IDEA.
  User: franc
  Date: 26/03/2020
  Time: 00:32
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <c:set var="currcontext" value="${pageContext.request.contextPath}"/>
    <title>Title</title>
    <link href="editor.css" rel="stylesheet">
    <link href="${currcontext}/leaflet/leaflet.css" rel="stylesheet">
    <link href="${currcontext}/map/map.css" rel="stylesheet">
    <link href="${currcontext}/jquery/jquery-ui.css" rel="stylesheet">
    <link href="header.css" rel="stylesheet">
</head>
<body>
<span id="ServletContextPath" data-servpath="${currcontext}"></span>
<jsp:include page="header.jsp"></jsp:include>
<div id="mycontent">
    <h3>Quando?</h3>
    <p>Data inizio <input type="text" id="beginpicker"></p>
    <p>Ora inizio <span id="beginDisp"></span></p>
    <div id="begin_slider"></div>

    <p>Data fine <input type="text" id="endpicker"></p>
    <p>Ora fine <span id="endDisp"></span></p>
    <div id="end_slider"></div>
    <p><label>Durata: </label><label id="durationDisp" style="color: red">Scegli l'inizio e la fine</label></p>
    <p>
    <h3>Cosa?</h3>
    <form method="post" action="newevent" id="ToSubmit" autocomplete="off">
        <input type="radio" checked="checked" id="weathertype" name="eventtype" value="weather">
        <label for="weathertype">Atmosferico</label>
        <input type="radio" id="seismictype" name="eventtype" value="seismic">
        <label for="seismictype">Sismico</label>
        <input type="radio" id="terroristtype" name="eventtype" value="terrorist">
        <label for="terroristtype">Terroristico</label>
        <div class="weatherinputsglob">
            <p class="redalert" id="emptyweatherdetailalert"></p>
            <label for="weatherdetail">In particolare: </label>
            <select id="weatherdetail" class="weatherinputs" required name="weatherdetail">
            <option value="0" selected hidden>Scegli il tipo</option>
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
        <p class="redalert" id="emptydescriptionalert"></p>
        <p><textarea rows="4" cols="50" name="description" id="description"
                     placeholder="Inserisci descrizione"></textarea></p>
        <input type="hidden" id="begin" name="begin">
        <input type="hidden" id="end" name="end">
        <input type="hidden" id="caplist" name="cap">

        <div id="map"></div>
        <h3>Dove?</h3>
        <p>scrivi i CAP o clicca o trascina col tasto destro sulla mappa</p>
        <div id="capinputcontainer">
            <p id="cappar0">
                <input id="cap0" type="text">
                <span id="capvalid0"></span>
            </p>
            <div id="morecapcontainer"></div>
            <button id="capbtn" type="button">+</button>
            <button type="button" id="removeall">- tutti</button>
        </div>
        <div class="seismicinputsglob" style="display: none">
            <p class="redalert" id="emptyepicentrecapalert"></p>
            <label for="epicentrecap">Cap epicentro: </label>
            <input type="text" disabled="disabled" class="seismicinputs" id="epicentrecap" name="epicentrecap">
            <span id="epicentrecapcapvalid"></span>
        </div>
        <h3>Come?</h3>
        <p class="redalert" id="emptydangeralert"></p>
        <label for="danger">Pericolo: </label>
        <select required name="danger" id="danger">
            <option value="0" selected hidden>Scegli il livello</option>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">3</option>
            <option value="4">4</option>
        </select>
        <div class="weatherinputsglob">
            <label for="windspeed">Velocita vento: </label>
            <input type="number" step="0.001" class="weatherinputs" name="windspeed" id="windspeed"
                   placeholder="Puo essere vuoto">
        </div>
        <div class="seismicinputsglob" style="display: none">
            <p class="redalert" id="emptymercallialert"></p>
            <label for="mercalli">Magnitudo Mercalli: </label>
            <input type="number" step="0.001" disabled="disabled" id="mercalli" name="mercalli" class="seismicinputs"
                   placeholder="Inserisci magnitudo"><br>
            <p class="redalert" id="emptyrichteralert"></p>
            <label for="richter">Magnitudo Richter: </label>
            <input type="number" step="0.001" disabled="disabled" id="richter" name="richter" class="seismicinputs"
                   placeholder="Inserisci magnitudo">
        </div>
        <p><input type="submit" id="confirm" class="buttonsubmit" value="Aggiungi evento"></p>
    </form>
</div>
<script src="${currcontext}/jquery/jquery.js"></script>
<script src="${currcontext}/jquery/jquery-ui.js"></script>
<script src="${currcontext}/leaflet/leaflet.js"></script>
<script src="${currcontext}/map/L.CanvasLayer.js"></script>

<script src="${currcontext}/map/CapInputsHandler.js"></script>
<script src="${currcontext}/map/mapReverse.js"></script>
<script src="editorCommon.js"></script>
<script src="editorNew.js"></script>
</body>
</html>