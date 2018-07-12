<%--
  Created by IntelliJ IDEA.
  User: Niko
  Date: 11.07.2018
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <link rel="stylesheet" href="style/main.css">
  <title>Page Scraper</title>
</head>
<body>
<div id="header">Page Scraper</div>
<div id="content">
  <div id="tableContainer" class="tableContainer">
    <table border="0" cellpadding="0" cellspacing="0" width="100%" id="hyperlinks" class="scrollTable">
      <thead class="fixedHeader">
      <tr>
        <th width="400px">link</th>

        <th width="400px">source</th>
      </tr>
      </thead>
      <tbody class="scrollContent">
      <tr>
        <td width=""><a href="index.jsp">href</a></td>
        <td><a href="index.jsp">src</a></td>
      </tr>

    </table>
  </div>

  <div><br></div>
</div>

</body>
<script src="script/main.js"></script>
</html>
