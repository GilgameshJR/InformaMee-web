<%--
  Created by IntelliJ IDEA.
  User: franc
  Date: 14/04/2020
  Time: 00:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<html>
<head>
    <title>Errore!</title>
</head>
<body>
<h1>Oooops... qualcosa è andato storto!</h1>
<c:choose>
    <c:when test="${error==1}"> <%--MalformedURLException--%>
        <h3>Non riusciamo a soddisfare la tua richiesta, dei parametri non sono corretti. Ricontrolla i parametri e l'URL!</h3>
    </c:when>
    <c:when test="${error==2}"> <%--NumberFormatException--%>
        Hai inserito caratteri non previsti in un campo in cui sono previsti solo numeri. Correggi e riprova!
    </c:when>
    <c:when test="${error==3}"> <%--SQLError--%>
    <h3>E' colpa nostra! Non riusciamo a recuperare i dati, si è verificato un errore nel database. Riprova più tardi.</h3>
    </c:when>
    <c:when test="${error==4}"> <%--NullPointerException--%>
        <h3>Si è verificato un errore. Sei sicuro di aver compilato tutti i campi per la richiesta? Probabilmente manca qualche parametro. Se non è così è colpa nostra.</h3>
    </c:when>
    <c:when test="${error==5}"> <%--ClassNotFound--%>
        E' colpa nostra! Non riusciamo a caricare delle risorse necessarie (ClassNotFound)
    </c:when>
    <c:when test="${error==6}"> <%--CapParseException--%>
        C'è stato un errore nell'elaborazione del CAP. Assicurati sia corretto e composto da soli numeri.
    </c:when>
    <c:otherwise>
        <h3>Si è verificato un errore generico.</h3>
    </c:otherwise>
</c:choose>
<p><a href="index.html">Torna alla homepage</a></p>
</body>
</html>
