<%@ page import="control.JSONScraperDatabase" %>
<%@ page import="control.MySQLScraperDatabase" %>
<%@ page import="control.ScraperDatabase" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %><%--
  Created by IntelliJ IDEA.
  User: Niko
  Date: 11.07.2018
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  ScraperDatabase database;
  try {
    database = MySQLScraperDatabase.getInstance();
  } catch (SQLException e) {
    database = JSONScraperDatabase.getInstance();
  }
%>
<html>
<head>
  <link rel="stylesheet" href="style/main.css">
  <title>Page Scraper</title>
</head>
<body>
<div id="header">Page Scraper</div>
<div id="content">
  <form target="" id="url-form">
    <input placeholder="URL" autocomplete="off">
    <button type="submit">Scrape</button>
  </form>
  <div>
    <p id="message">&nbsp;</p>
  </div>
  <div id="tableContainer" class="tableContainer">
    <table border="0" cellpadding="0" cellspacing="0" width="100%" id="hyperlinks" class="scrollTable">
      <thead class="fixedHeader">
      <tr>
        <th width="400px">link</th>

        <th width="400px">source</th>
      </tr>
      </thead>
      <tbody class="scrollContent" id="links">
      <tr>
          <%
          Map<String, List<String>> links = database.getAllLinks();
          for (String k:links.keySet()){
            for(String el:links.get(k)){
            %>
      <tr>
        <td width="400px"><a href="<%=el%>"><%=el%>
        </a>
        </td>
        <td width="50%"><a href="<%=k%>"><%=k%>
      </tr>
      <%
          }
        }
      %>
      </tr>
      </tbody>
    </table>
  </div>

</div>

</body>
<script src="script/main.js"></script>
</html>
