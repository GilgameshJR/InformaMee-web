    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<c:choose>

    <c:when test="${currentcap==null}">Nessun CAP impostato. <a href="SetDefaultCap.jsp"> Impostalo</a></c:when>


    <c:otherwise>
        Il tuo CAP è <c:out value="${currentcap}"></c:out>.<a href="SetDefaultCap.jsp"> Modificalo</a><br>


        <c:if test="${ !( requestScope.wcurrcount==null ||  requestScope.scurrcount==null || requestScope.tcurrcount==null ) }">
            <a href="index">


                <c:choose>

                    <c:when test="${requestScope.wcurrcount==0 && requestScope.scurrcount==0 && requestScope.tcurrcount==0}">
                        <span class="noalert">Nessun'allerta in corso</span>
            </c:when>


            <c:otherwise>
            <span class="redalert" id="alertsheader">
                <c:choose>
                    <c:when test="${requestScope.wcurrcount==1}">un'allerta meteorologica </c:when>
                    <c:when test="${requestScope.wcurrcount>1}"><c:out value="${wcurrcount}"></c:out> allerte meteorologiche </c:when>
                </c:choose>
                <c:choose>
                    <c:when test="${requestScope.scurrcount==1}">un'allerta sismica </c:when>
                    <c:when test="${requestScope.scurrcount>1}"><c:out value="${requestScope.scurrcount}"></c:out> allerte sismiche </c:when>
                </c:choose>
                <c:choose>
                    <c:when test="${requestScope.tcurrcount==1}">un'allerta terroristica </c:when>
                    <c:when test="${requestScope.tcurrcount>1}"><c:out value="${requestScope.tcurrcount}"></c:out> allerte terroristiche </c:when>
                </c:choose>
                in corso!</span>
            </c:otherwise>

                </c:choose>

            </a>


        </c:if>

    </c:otherwise>


</c:choose>
