<%--
  Created by IntelliJ IDEA.
  User: franc
  Date: 09/04/2020
  Time: 01:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <c:set var="currcontext" value="${pageContext.request.contextPath}"/>
    <title>Title</title>
    <link href="header.css" rel="stylesheet">
    <link href="${currcontext}/leaflet/leaflet.css" rel="stylesheet">
    <link href="${currcontext}/map/map.css" rel="stylesheet">
</head>
<body>
<jsp:include page="header.jsp"></jsp:include>
<h1>Eventi in corso e futuri che hai creato:</h1>
<h3><a href="editorNew.jsp">Inserisci un nuovo evento</a></h3>
<c:set var="isEditor" scope="request" value="true"/>
<jsp:include page="/table/ShowRes.jsp"></jsp:include>
</body>
</html>
