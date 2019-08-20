<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk"%>
<script src="../third-party/jquery.min.js"></script>
<script src="../third-party/mathquill/mathquill.min.js"></script>
<link rel="stylesheet" href="../third-party/mathquill/mathquill.css"/>
<meta http-equiv="Content-Type" content="text/html;charset=gbk"/>
<%
request.setCharacterEncoding("gbk");
response.setCharacterEncoding("gbk");
String content = request.getParameter("myEditor");



response.getWriter().print("<div class='content'>"+content+"</div>");

%>