<%--
  Created by IntelliJ IDEA.
  User: franc
  Date: 26/03/2020
  Time: 00:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="currcontext" value="${pageContext.request.contextPath}"/>
<c:set var="weathersize" scope="page" value="${results.weather.size()}"></c:set>
<c:set var="seismicsize" scope="page" value="${results.seismic.size()}"></c:set>
<c:set var="terroristsize" scope="page" value="${results.terrorist.size()}"></c:set>

<span id="ServletContextPath" data-servpath="${currcontext}"></span>
<link href="${currcontext}/table/table.css" rel="stylesheet">
<link href="${currcontext}/leaflet/leaflet.css" rel="stylesheet">
<link href="${currcontext}/map/map.css" rel="stylesheet">
<a name="map"></a>
<div id="map"></div>
<c:choose>
    <c:when test="${!(weathersize>0||seismicsize>0||terroristsize>0)}"><h2>Nessun evento da mostrare</h2></c:when>
    <c:otherwise>
        <c:if test="${weathersize>0}">
            <h2>Eventi meteorologici:</h2><br>
            <table class="weathertable">
                <tr>
                    <th>Descrizione</th>
                    <th>Pericolo</th>
                    <th>Ora inizio</th>
                    <th>Ora fine</th>
                    <th>Velocità vento</th>
                    <th>Tipo</th>
                    <th>CAP coinvolti</th>
                </tr>
                <c:forEach var="wevent" items="${results.weather}">
                    <c:set var="currdanger" value="${wevent.danger}"></c:set>
                    <c:choose>
                        <c:when test="${currdanger==1}"><tr class="danger1"></c:when>
                        <c:when test="${currdanger==2}"><tr class="danger2"></c:when>
                        <c:when test="${currdanger==3}"><tr class="danger3"></c:when>
                        <c:when test="${currdanger==4}"><tr class="danger4"></c:when>
                        <c:otherwise><tr></c:otherwise>
                    </c:choose>
                    <td><c:out value="${wevent.description}"></c:out></td>
                    <td><c:out value="${currdanger}"></c:out></td>
                    <td><fmt:formatDate value="${wevent.beginTime}" type="both" dateStyle="full"
                                        timeStyle="short"/></td>
                    <td><fmt:formatDate value="${wevent.endTime}" type="both" dateStyle="full" timeStyle="short"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${wevent.windSpeed==0}">
                                -
                            </c:when>
                            <c:otherwise>
                                <fmt:formatNumber value="${wevent.windSpeed}" type="number" maxFractionDigits="3"/>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${wevent.type==1}">Alluvione</c:when>
                            <c:when test="${wevent.type==2}">Temporali e fulmini</c:when>
                            <c:when test="${wevent.type==3}">Frana</c:when>
                            <c:when test="${wevent.type==4}">Pioggia e grandine</c:when>
                            <c:when test="${wevent.type==5}">Neve e gelo</c:when>
                            <c:when test="${wevent.type==6}">Valanga</c:when>
                            <c:when test="${wevent.type==7}">Nebbia</c:when>
                            <c:when test="${wevent.type==8}">Venti e mareggiate</c:when>
                            <c:when test="${wevent.type==9}">Ciclone</c:when>
                            <c:when test="${wevent.type==10}">Altro</c:when>
                            <c:otherwise>Non disponibile</c:otherwise>
                        </c:choose>
                    </td>
                    <td data-capjson="${wevent.involvedCapJson}" class="captd">
                        <c:forEach var="involvedcap" items="${wevent.involvedCap}">
                            <jsp:directive.include file="CapPrinter.txt"></jsp:directive.include>
                        </c:forEach>
                    </td>
                    <c:if test="${isEditor==true}">
                        <td><a href="getEditor?id=${wevent.id}&type=weather">Modifica</a></td>
                        <td><a href="delete?id=${wevent.id}">Elimina</a></td>
                    </c:if>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        <c:if test="${seismicsize>0}">
            <h2>Eventi sismici:</h2><br>
            <table class="seismictable">
                <tr>
                    <th>Descrizione</th>
                    <th>Pericolo</th>
                    <th>Ora inizio</th>
                    <th>Ora fine</th>
                    <th>Magnitudo Richter</th>
                    <th>Magnitudo Mercalli</th>
                    <th>CAP epicentro</th>
                    <th>CAP coinvolti</th>
                </tr>

                <c:forEach var="sevent" items="${results.seismic}">
                    <c:set var="currdanger" value="${sevent.danger}"></c:set>
                    <c:choose>
                        <c:when test="${currdanger==1}"><tr class="danger1"></c:when>
                        <c:when test="${currdanger==2}"><tr class="danger2"></c:when>
                        <c:when test="${currdanger==3}"><tr class="danger3"></c:when>
                        <c:when test="${currdanger==4}"><tr class="danger4"></c:when>
                        <c:otherwise><tr></c:otherwise>
                    </c:choose>
                    <td><c:out value="${sevent.description}"></c:out></td>
                    <td><c:out value="${currdanger}"></c:out></td>
                    <td><fmt:formatDate value="${sevent.beginTime}" type="both" dateStyle="full"
                                        timeStyle="short"/></td>
                    <td><fmt:formatDate value="${sevent.endTime}" type="both" dateStyle="full" timeStyle="short"/></td>
                    <td><fmt:formatNumber value="${sevent.richterMagnitude}" type="number" maxFractionDigits="3"/></td>
                    <td><fmt:formatNumber value="${sevent.mercalliMagnitude}" type="number" maxFractionDigits="3"/></td>
                    <td data-capjson="${sevent.epicentreCAPJson}" class="captd"><c:out
                            value="${sevent.epicentreCAP}"></c:out></td>
                    <td data-capjson="${sevent.involvedCapJson}" class="captd">
                        <c:forEach var="involvedcap" items="${sevent.involvedCap}">
                            <jsp:directive.include file="CapPrinter.txt"></jsp:directive.include>
                        </c:forEach>
                    </td>
                    <c:if test="${isEditor==true}">
                        <td><a href="getEditor?id=${sevent.id}&type=seismic">Modifica</a></td>
                        <td><a href="delete?id=${sevent.id}">Elimina</a></td>
                    </c:if>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
        <c:if test="${terroristsize>0}">
            <h2>Eventi terroristici:</h2><br>
            <table class="terroristtable">
                <tr>
                    <th>Descrizione</th>
                    <th>Pericolo</th>
                    <th>Ora inizio</th>
                    <th>Ora fine</th>
                    <th>CAP coinvolti</th>
                </tr>
                <c:forEach var="tevent" items="${results.terrorist}">
                    <c:set var="currdanger" value="${tevent.danger}"></c:set>
                    <c:choose>
                        <c:when test="${currdanger==1}"><tr class="danger1"></c:when>
                        <c:when test="${currdanger==2}"><tr class="danger2"></c:when>
                        <c:when test="${currdanger==3}"><tr class="danger3"></c:when>
                        <c:when test="${currdanger==4}"><tr class="danger4"></c:when>
                        <c:otherwise><tr></c:otherwise>
                    </c:choose>
                    <td><c:out value="${tevent.description}"></c:out></td>
                    <td><c:out value="${currdanger}"></c:out></td>
                    <td><fmt:formatDate value="${tevent.beginTime}" type="both" dateStyle="full"
                                        timeStyle="short"/></td>
                    <td><fmt:formatDate value="${tevent.endTime}" type="both" dateStyle="full" timeStyle="short"/></td>
                    <td data-capjson="${tevent.involvedCapJson}" class="captd">
                        <c:forEach var="involvedcap" items="${tevent.involvedCap}">
                            <jsp:directive.include file="CapPrinter.txt"></jsp:directive.include>
                        </c:forEach>
                    </td>
                    <c:if test="${isEditor==true}">
                        <td><a href="getEditor?id=${tevent.id}&type=terrorist">Modifica</a></td>
                        <td><a href="delete?id=${tevent.id}">Elimina</a></td>
                    </c:if>
                    </tr>
                </c:forEach>
            </table>
        </c:if>
    </c:otherwise>
</c:choose>
<script src="${currcontext}/jquery/jquery.js"></script>
<script src="${currcontext}/leaflet/leaflet.js"></script>
<script src="${currcontext}/map/mapLookup.js"></script>
<script src="${currcontext}/table/ShowRes.js"></script>