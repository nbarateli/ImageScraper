<%@ page import="control.ScraperDatabase" %>
<%@ page import="misc.Utils" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%--
  Created by IntelliJ IDEA.
  User: Niko
  Date: 11.07.2018
  Time: 16:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  ScraperDatabase database = (ScraperDatabase) getServletConfig().getServletContext().getAttribute("database");
%>
<html>
<head>
  <link rel="stylesheet" href="style/main.css">
  <title>Page Scraper</title>
</head>
<body>
<div id="header">Page Scraper</div>
<div id="content">
  <form target="/api/process" id="url-form">
    <input placeholder="URL" name="url" autocomplete="off">
    <button type="submit">Scrape</button>
  </form>
  <div>
    <p id="message">&nbsp;</p>
  </div>
  <table id="table-header">
    <thead class="fixedHeader">
    <tr>
      <th class="link-row">link</th>

      <th class="src-row">source</th>
    </tr>
    </thead>
  </table>
  <div id="tableContainer" class="tableContainer">

    <table border="0" cellpadding="0" cellspacing="0" width="100%" id="hyperlinks" class="scrollTable">

      <tbody class="scrollContent" id="links">
      <tr>
          <%
          Map<String, List<String>> links = database.getAllLinks();
          for (String k:links.keySet()){
            for(String el:links.get(k)){
            %>
      <tr>
        <td>
          <a href="<%=el%>"><span class="text-overflow"><%=Utils.escapeHTML(el)%></span> </a>

        </td>
        <td><a href="<%=k%>"><span class="text-overflow"><%=Utils.escapeHTML(k)%></span>
        </a>
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
