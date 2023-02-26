package com.xmg.jdbc;

import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class JdbcTest {

    @Test
    public void  test0() throws Exception {
        Properties properties =new Properties();
        properties.load(new FileInputStream("src/main/java/com/xmg/jdbc/sql.properties"));
        Driver dr = new com.mysql.cj.jdbc.Driver();
        final String url = (String) properties.get("url");
        final String password = (String) properties.get("password");
        final String user = (String) properties.get("user");
        DriverManager.registerDriver(dr);
        final Connection connection = DriverManager.getConnection(url, user, password);
        final Statement statement = connection.createStatement();
        final int i = statement.executeUpdate("insert  into user value (1,'test1','111')");
        System.out.println("受影响的行数" + i);
    }
}
