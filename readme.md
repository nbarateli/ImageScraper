 
A simple web page scraper.
<br>
runs on <a href="https://tomcat.apache.org/download-70.cgi">Tomcat 7</a>.

Database used: MySQL.
                     
used libraries:
<ul>
    <li><a href="https://jsoup.org/"> jsoup </a></li>
    <li><a href="https://docs.oracle.com/javaee/7/api/javax/json/package-summary.html">javax.json</a>
</ul>

You should have <b>databaseinfo.json</b> file in your deployment directory, formatted like this:

{<br>
    "server": "",<br>
    "username": "",<br>
    "password": null,<br>
    "database": ""<br>
}

API:
    
    ProcessSource:
    URL: POST /api/process
    Usage: /api/process
    parameters: url - the url to process
    result data: { your_url: an array of fetched non-duplicate hyperlinks } 
    
    
