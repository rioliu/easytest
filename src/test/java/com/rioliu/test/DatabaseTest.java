package com.rioliu.test;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.rioliu.test.base.AbstractTestBase;
import com.rioliu.test.logging.Logger;
import com.rioliu.test.logging.LoggerFactory;

/**
 * Created by rioliu on Nov 28, 2018
 */
public class DatabaseTest extends AbstractTestBase {

    private static Logger logger = LoggerFactory.getConsoleLogger(DatabaseTest.class);
    private MysqlDataSource ds;

    public DatabaseTest() {
        // launch mysql container
        // $ docker run --rm --name mysqldb -e MYSQL_ROOT_PASSWORD=welcome1 -p 3306:3306 -d mysql
    }

    @BeforeClass
    public void beforeClass() {

        ds = new MysqlDataSource();
        ds.setURL("jdbc:mysql://localhost:3306/mysql");
        ds.setUser("root");
        ds.setPassword("welcome1");

    }

    @Test
    public void testJDBCConnection() {

        Connection conn = null;
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            logger.error("connect to " + ds.getURL() + " failed", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }

        logger.info("Successfully connected with local mysqldb");

    }

    @Test
    public void testSqlQuery() {

        QueryRunner run = new QueryRunner();
        Connection conn = null;
        try {
            conn = ds.getConnection();
            HashMap<String, String> result =
                    run.query(conn, "select * from user where user=?",
                            new ResultSetHandler<HashMap<String, String>>() {

                                @Override
                                public HashMap<String, String> handle(ResultSet rs)
                                        throws SQLException {

                                    if (rs.next() == false) {
                                        return null;
                                    }

                                    HashMap<String, String> map = new HashMap<String, String>();

                                    for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                                        logger.info(rs.getMetaData().getColumnName(i + 1) + ":"
                                                + rs.getObject(i + 1));
                                    }

                                    String host = rs.getString("Host");
                                    String user = rs.getString("User");

                                    map.put("host", host);
                                    map.put("user", user);

                                    assertThat(host, equalToIgnoringCase("localhost"));
                                    assertThat(user, equalToIgnoringCase("mysql.session"));

                                    return map;
                                }

                            }, "mysql.session");

            logger.info("Host is " + result.get("host"));
            logger.info("User is " + result.get("user"));

        } catch (SQLException e) {
            logger.error("do db query failed", e);
        } finally {
            DbUtils.closeQuietly(conn);
        }

    }

}


