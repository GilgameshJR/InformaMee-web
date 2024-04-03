<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<link href="ResultsView.css" rel="stylesheet" type="text/css">
    <meta charset="ISO-8859-1">
    <title>Insert title here</title>
</head>
<body>
<c:set var="weathernumber" scope="page" value="${weatherres.size()}"></c:set>
<c:set var="seismicnumber" scope="page" value="${seismicres.size()}"></c:set>
<c:set var="terroristnumber" scope="page" value="${terroristres.size()}"></c:set>
<c:choose>
    <c:when test="${!(weathernumber>0||seismicnumber>0||terroristnumber>0)}"><h2>Nessun evento da mostrare</h2></c:when>
    <c:otherwise>
<c:if test="${weathernumber>0}">
    <h1>Eventi meteorologici:</h1><br>
    <table class="weathertable">
        <tr>
            <th>Descrizione</th>
            <th>Pericolo</th>
            <th>Ora inizio</th>
            <th>Ora fine</th>
            <th>Velocità vento</th>
            <th>Tipo</th>
        </tr>

        <c:forEach var="wevent" items="${weatherres}">
        <c:set var="currdanger" value="${wevent.danger}"></c:set>
<tr>
                <td><c:out value="${wevent.description}"></c:out></td><td><c:out value="${currdanger}"></c:out></td><td><c:out value="${wevent.beginTime}"></c:out></td><td><c:out value="${wevent.endTime}"></c:out></td><td><c:out value="${wevent.windSpeed}"></c:out></td><td><c:out value="${wevent.type}"></c:out></td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<c:if test="${seismicnumber>0}">
    <h1>Eventi sismici:</h1><br>
    <table class="seismictable">
        <tr>
            <th>Descrizione</th>
            <th>Pericolo</th>
            <th>Ora inizio</th>
            <th>Ora fine</th>
            <th>Magnitudo Richter</th>
            <th>Magnitudo Mercalli</th>
            <th>CAP epicentro</th>
        </tr>

        <c:forEach var="sevent" items="${seismicres}">
            <c:set var="currdanger" value="${sevent.danger}"></c:set>
            <tr>
            <td><c:out value="${sevent.description}"></c:out></td><td><c:out value="${currdanger}"></c:out></td><td><c:out value="${sevent.beginTime}"></c:out></td><td><c:out value="${sevent.endTime}"></c:out></td><td><c:out value="${sevent.richterMagnitude}"></c:out></td><td><c:out value="${sevent.mercalliMagnitude}"></c:out></td><td><c:out value="${sevent.epicentreCAP}"></c:out></td>
            </tr>
        </c:forEach>
    </table>
</c:if>
<c:if test="${terroristnumber>0}">
    <h1>Eventi terroristici:</h1><br>
    <table class="terroristtable">
        <tr>
            <th>Descrizione</th>
            <th>Pericolo</th>
            <th>Ora inizio</th>
            <th>Ora fine</th>
        </tr>
        <c:forEach var="tevent" items="${terroristres}">
            <c:set var="currdanger" value="${tevent.danger}"></c:set>
    <tr>
            <td><c:out value="${tevent.description}"></c:out></td><td><c:out value="${currdanger}"></c:out></td><td><c:out value="${tevent.beginTime}"></c:out></td><td><c:out value="${tevent.endTime}"></c:out></td>
            </tr>
        </c:forEach>
    </table>
</c:if>
    </c:otherwise>
</c:choose>
</body>
</html>