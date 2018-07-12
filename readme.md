 
A simple web page scraper.
<br>
runs on <a href="https://tomcat.apache.org/download-70.cgi">Tomcat 7</a> also comes with a built-in Java GUI.

Database used: MySQL.
                     
used libraries:
<ul>
    <li><a href="https://jsoup.org/"> jsoup </a></li>
    <li><a href="https://docs.oracle.com/javaee/7/api/javax/json/package-summary.html">javax.json</a>
</ul>

You should have a databaseinfo.json file in your working directory, formatted like this:

{
  <span style="color:red">"server"</span>: "",
  <span>"username"</span>: "",
  <span>"password"</span>: null,
  <span>"database"</span>: ""
}