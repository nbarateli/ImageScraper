DROP DATABASE IF EXISTS pagescraper;
CREATE DATABASE pagescraper;
USE pagescraper;


CREATE TABLE sources
(
  src_id   INT PRIMARY KEY,
  src_link VARCHAR(255) NOT NULL
);
CREATE UNIQUE INDEX sources_src_link_uindex
  ON sources (src_link);
CREATE TABLE hyperlinks
(
  link_id INT PRIMARY KEY,
  href    VARCHAR(255),
  src     INT,
  CONSTRAINT hyperlinks_sources_src_id_fk FOREIGN KEY (src) REFERENCES sources (src_id)
    ON DELETE CASCADE
);