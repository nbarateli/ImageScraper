package control;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;

import javax.sql.PooledConnection;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implements the ScrapperDatabase interface using the MySQL database.
 */
public class MySQLScraperDatabase implements ScraperDatabase {
    private static MySQLScraperDatabase instance;
    private Lock databaseLock;

    /**
     * Returns the instance of database
     */
    public static MySQLScraperDatabase getInstance() throws FileNotFoundException, SQLException {
        return instance == null ? instance = new MySQLScraperDatabase() : instance;
    }

    private PooledConnection pool;
    private final Map<String, String> dbInfo;

    private MySQLScraperDatabase() throws SQLException, FileNotFoundException {
        databaseLock = new ReentrantLock();
        dbInfo = MySQLInfo.readDBInfo();
        MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setURL(dbInfo.get("server"));
        dataSource.setUser(dbInfo.get("username"));
        if (dbInfo.get("password").length() > 0) {
            dataSource.setPassword(dbInfo.get("password"));
            pool = dataSource.getPooledConnection(dbInfo.get("username"), dbInfo.get("password"));
        } else
            pool = dataSource.getPooledConnection();

    }

    /**
     * Returns whether a connection is available
     */
    public boolean checkConnection() {
        try {
            pool.getConnection();
            return true;
        } catch (SQLException e) {
            MysqlConnectionPoolDataSource dataSource = new MysqlConnectionPoolDataSource();
            try {
                try {
                    pool.close();
                } catch (Exception ignored) {

                }
                dataSource.setURL(dbInfo.get("server"));
                dataSource.setUser(dbInfo.get("username"));
                if (dbInfo.get("password").length() > 0) {
                    dataSource.setPassword(dbInfo.get("password"));
                    pool = dataSource.getPooledConnection(dbInfo.get("username"), dbInfo.get("password"));
                } else
                    pool = dataSource.getPooledConnection();
            } catch (Exception ee) {
                return false;
            }
            return true;
        }
    }

    @Override
    public void addLink(String url, String src) {

        addLinks(src, Collections.singletonList(url));
    }

    @Override
    public void addLinks(String src, Collection<String> urls) {
        try {
            addSource(src);
            int id = getSourceId(src);
            StringBuilder builder = new StringBuilder("INSERT INTO hyperlinks (href, src) VALUES\n");
            urls.forEach(url -> builder.append("(?, ?),"));
            builder.setCharAt(builder.length() - 1, ' ');
            Object[] params = new Object[urls.size() * 2];
            Iterator<String> iterator = urls.iterator();
            for (int i = 0; i < params.length; i += 2) {
                params[i] = iterator.next();
                params[i + 1] = id;
            }
            executeUpdate(builder.toString(), params);
        } catch (Exception ignored) {

        }
    }

    @Override
    public Map<String, List<String>> getAllLinks() {
        Map<Integer, String> sources = getSources();
        Map<String, List<String>> result = new HashMap<>();
        try (ResultSet resultSet = executeQuery("SELECT * FROM hyperlinks")) {
            while (resultSet.next()) {
                addToList(result, sources.get(resultSet.getInt("src")), resultSet.getString("href"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param src the hyperlink of the given source
     * @return the id in mysql database
     */
    private int getSourceId(String src) {
        databaseLock.lock();
        int result = 0;
        try (ResultSet set = executeQuery("SELECT * FROM sources WHERE src_link = ?", src)) {
            set.next();
            result = set.getInt("src_id");
        } catch (SQLException e) {

        } finally {
            databaseLock.unlock();
        }
        return result;
    }

    private void addToList(Map<String, List<String>> result, String src, String href) {
        List<String> links = result.computeIfAbsent(src, k -> new ArrayList<>());
        links.add(href);
    }


    /**
     * Returns the id:source_link map of all the sources
     */
    private Map<Integer, String> getSources() {
        try (ResultSet resultSet = executeQuery("SELECT  * FROM sources")) {
            Map<Integer, String> sources = new HashMap<>();
            while (resultSet.next()) {
                sources.put(resultSet.getInt("src_id"), resultSet.getString("src_link"));
            }
            return sources;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    /**
     * Executes the passed sql query and returns fetched results.
     *
     * @param sql    a SQL statement to be sent to the database, typically a
     *               static SQL <code>SELECT</code> statement
     * @param values an array of values to replace "?" characters in the SQL statement
     * @return a <code>ResultSet</code> object that contains the data produced
     * by the given query; never <code>null</code>
     * @throws SQLException if a database access error occurs,
     *                      the given SQL statement produces anything other than a single
     *                      <code>ResultSet</code> object, the method is called on a
     *                      <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    public ResultSet executeQuery(String sql, Object... values) throws SQLException {
        return (ResultSet) execute(sql, values, false);
    }

    /**
     * Executes passed SQL query and returns number of rows affected.
     *
     * @param sql    a SQL Data Manipulation Language (DML) statement, such as <code>INSERT</code>,
     *               <code>UPDATE</code> or <code>DELETE</code>; or a SQL statement that returns nothing,
     * @param values an array of values to replace "?" characters in the SQL statement
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     * or (2) 0 for SQL statements that return nothing
     * @throws SQLException if a database access error occurs,
     *                      this method is called on a closed <code>Statement</code>, the given
     *                      SQL statement produces a <code>ResultSet</code> object, the method is called on a
     */
    public int executeUpdate(String sql, Object... values) throws SQLException {
        return (int) execute(sql, values, true);
    }

    /**
     * Adds the given source the list of sources in database
     */
    private void addSource(String src) {
        try {
            databaseLock.lock();
            executeUpdate("INSERT INTO sources (src_link) VALUE (?)", src);

        } catch (Exception ignored) {
        } finally {
            databaseLock.unlock();
        }

    }

    /**
     * Connects to database, executes passed and returns either a <code>ResultSet</code> or an int,
     * based on what type of query was passed.
     *
     * @return a <code>ResultSet</code> if the query type is not update, an int denoting the number of rows if it is.
     * @throws SQLException if database access error occurs.
     */
    private Object execute(String sql, Object[] values, boolean isUpdate) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = pool.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeQuery("USE " + dbInfo.get("database") + ";");
            if (values != null) {
                addParameters(statement, values);
            }

            if (isUpdate) {
                int nRows = statement.executeUpdate();
                statement.close();
                return nRows;
            } else {
                return statement.executeQuery();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * adds given parameters to given PreparedStatement
     *
     * @param statement PreparedStatement which we need to add parameters to
     * @param values    parameters needed to be added to PreparedStatement
     * @throws SQLException if database access error occurs.
     */
    private void addParameters(PreparedStatement statement, Object[] values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            statement.setObject(i + 1, values[i]);
        }
    }
}
