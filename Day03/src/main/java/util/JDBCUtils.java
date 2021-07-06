package util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCUtils {
    private static DataSource ds;
    private static ThreadLocal<Connection> tl = new ThreadLocal<>();
    static {
        Properties p = new Properties();
        try {
            p.load(JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties"));
            ds = DruidDataSourceFactory.createDataSource(p);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DataSource getDataSource() {
        return ds;
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = tl.get();
        if(conn == null) {
            conn = ds.getConnection();
        }
        return conn;
    }

    public static void close(Statement stmt,Connection conn) {
        close(null,stmt,conn);
    }


    public static void close(ResultSet rs,Statement stmt, Connection conn) {
        if(rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if(conn != null) {
            try {
                tl.remove();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
