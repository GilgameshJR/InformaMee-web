<%--
  Created by IntelliJ IDEA.
  User: franc
  Date: 09/04/2020
  Time: 21:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <c:set var="currcontext" value="${pageContext.request.contextPath}"/>
    <title>Title</title>
    <link href="editor.css" rel="stylesheet">
    <link href="${currcontext}/jquery/jquery-ui.css" rel="stylesheet">
    <link href="${currcontext}/leaflet/leaflet.css" rel="stylesheet">
    <link href="${currcontext}/map/map.css" rel="stylesheet">
    <link href="header.css" rel="stylesheet">
</head>
<body>
<span id="ServletContextPath" data-servpath="${currcontext}"></span>
<jsp:include page="header.jsp"></jsp:include>
<div id="mycontent">
    <h3>Quando?</h3>
    <p>Data inizio <input type="text" id="beginpicker" data-oldbegin="${result.beginTime.time}"></p>
    <p>Ora inizio <span id="beginDisp"></span></p>
    <div id="begin_slider"></div>
    <p>Data fine <input type="text" id="endpicker" data-oldend="${result.endTime.time}"></p>
    <p>Ora fine <span id="endDisp"></span></p>
    <div id="end_slider"></div>

    <p><label>Durata: </label><label id="durationDisp">invariata</label></p>
    <p>
    <h3>Cosa?</h3>
    <form method="post" action="edit" id="ToSubmit" autocomplete="off">
        <input type="hidden" id="begin" name="begin">
        <input type="hidden" id="end" name="end">
        <input type="hidden" id="eventidedit" name="id" value="${result.id}">
        <input type="hidden" id="typeedit" name="type" value="${typename}">
        <input type="hidden" id="caplist" name="cap" data-oldcapcounter="${result.involvedCap.size()-1}">

        <input type="radio" <c:if test="${type==1}">checked="checked"</c:if> disabled>
        <label>Atmosferico</label>
        <input type="radio" <c:if test="${type==2}">checked="checked"</c:if> disabled>
        <label>Sismico</label>
        <input type="radio" <c:if test="${type==3}">checked="checked"</c:if> disabled>
        <label>Terroristico</label>

        <c:if test="${type==1}">
        <input type="hidden" id="type" value="weather">
        <div class="weatherinputsglob">
            <label for="weatherdetail">In particolare: </label>
            <select id="weatherdetail" class="weatherinputs" required name="weatherdetail">
                <option value="1" <c:if test="${result.type==1}">selected</c:if> > Alluvione</option>
                <option value="2" <c:if test="${result.type==2}">selected</c:if> >Temporali e fulmini</option>
                <option value="3" <c:if test="${result.type==3}">selected</c:if> >Frana</option>
                <option value="4" <c:if test="${result.type==4}">selected</c:if> >Pioggia e grandine</option>
                <option value="5" <c:if test="${result.type==5}">selected</c:if> >Neve e gelo</option>
                <option value="6" <c:if test="${result.type==6}">selected</c:if> >Valanga</option>
                <option value="7" <c:if test="${result.type==7}">selected</c:if> >Nebbia</option>
                <option value="8" <c:if test="${result.type==8}">selected</c:if> >Venti e mareggiate</option>
                <option value="9" <c:if test="${result.type==9}">selected</c:if> >Ciclone</option>
                <option value="10" <c:if test="${result.type==10}">selected</c:if> >Altro</option>
            </select>
        </div>
        </c:if>
        <p class="redalert" id="emptydescriptionalert"></p>
        <p><textarea rows="4" cols="50" name="description" id="description">${result.description}</textarea></p>
        <div id="map"></div>
        <h3>Dove?</h3>
        <p>scrivi i CAP o clicca o trascina col tasto destro sulla mappa</p>
        <div id="capinputcontainer">
            <p id="cappar0">
                <input id="cap0" type="text" placeholder="Cap coinvolti" value="${result.involvedCap[0]}">
        <span id="capvalid0">
            <jsp:include page="/CapOkResponseString.jsp"></jsp:include>
        </span>
            </p>
            <div id="morecapcontainer">
                <c:forEach items="${result.involvedCap}" var="currentCap" varStatus="status" begin="1">
                    <p id="cappar${status.index}">
                        <input id="cap${status.index}" type="text" value="${currentCap}">
                        <button id="caprembtn${status.index}" type="button">-</button>
                        <span id="capvalid${status.index}">
                        <jsp:include page="/CapOkResponseString.jsp"></jsp:include>
                    </span>
                    </p>
                </c:forEach>
            </div>
            <button id="capbtn" type="button">+</button>
            <button type="button" id="removeall">- tutti</button>
        </div>
        <c:if test="${type==2}">
            <input type="hidden" id="type" value="seismic">
            <div class="seismicinputsglob">
                <p class="redalert" id="emptyepicentrecapalert"></p>
                <label for="epicentrecap">Cap epicentro: </label>
                <input type="text" class="seismicinputs" id="epicentrecap" name="epicentrecap"
                       value="${result.epicentreCAP}">
                <span id="epicentrecapcapvalid">
                    <jsp:include page="/CapOkResponseString.jsp"></jsp:include>
                </span>
            </div>
            </c:if>
        <h3>Come?</h3>
        <p class="redalert" id="emptydangeralert"></p>
        <label for="danger">Pericolo: </label>
        <select required name="danger" id="danger">
            <option <c:if test="${result.danger==1}">selected</c:if> value="1">1</option>
            <option <c:if test="${result.danger==2}">selected</c:if> value="2">2</option>
            <option <c:if test="${result.danger==3}">selected</c:if> value="3">3</option>
            <option <c:if test="${result.danger==4}">selected</c:if> value="4">4</option>
        </select>
        <c:if test="${type==1}">
        <span class="weatherinputsglob">
        <label for="windspeed">Velocita vento: </label>
        <input type="number" step="0.001" class="weatherinputs" name="windspeed" id="windspeed"
               value="${result.windSpeed}">
        </span>
        </c:if>
        <c:if test="${type==2}">
            <div class="seismicinputsglob">
                <p class="redalert" id="emptymercallialert"></p>
                <label for="mercalli">Magnitudo Mercalli: </label>
                <input type="number" step="0.001" id="mercalli" name="mercalli" class="seismicinputs"
                       value="${result.mercalliMagnitude}"><br>
                <p class="redalert" id="emptyrichteralert"></p>
                <label for="richter">Magnitudo Richter: </label>
                <input type="number" step="0.001" id="richter" name="richter" class="seismicinputs"
                       value="${result.richterMagnitude}">
            </div>
        </c:if>
        <c:if test="${type==3}">
            <input type="hidden" id="type" value="terrorist">
            </c:if>
            <p><input type="submit" id="confirm" class="buttonsubmit" value="Modifica evento"></p>
    </form>
    <form method="get" action="delete">
        <input type="hidden" id="typedelete" name="type" value="${typename}">
        <input type="hidden" id="eventiddelete" name="id" value="${result.id}">
        <input type="submit" value="Elimina evento">
    </form>
</div>
<script src="${currcontext}/jquery/jquery.js"></script>
<script src="${currcontext}/leaflet/leaflet.js"></script>
<script src="${currcontext}/jquery/jquery-ui.js"></script>

<script src="${currcontext}/map/CapInputsHandler.js"></script>
<script src="${currcontext}/map/L.CanvasLayer.js"></script>
<script src="${currcontext}/map/mapReverse.js"></script>
<script src="editorCommon.js"></script>
<script src="editorExisting.js"></script>
</body>
</html>