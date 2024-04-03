<%--
  Created by IntelliJ IDEA.
  User: franc
  Date: 25/03/2020
  Time: 19:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="userheader">Sei l'applicazione di previsione <c:out value="${sessionScope.currentuser.id}"></c:out>. <a href="logout">Disconnetti</a></div>