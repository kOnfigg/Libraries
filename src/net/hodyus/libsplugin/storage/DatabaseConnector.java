package net.hodyus.libsplugin.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnector {

    private String password;
    private String url;
    private String user;
    private Connection connection;

    public DatabaseConnector(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public void connect() {

        try {
            HikariConfig hikariConfig = new HikariConfig();

            hikariConfig.setJdbcUrl(url);
            hikariConfig.setUsername(user);
            hikariConfig.setPassword(password);
            hikariConfig.addDataSourceProperty("cachePrepStmts", "true");

            DataSource dataSource = new HikariDataSource(hikariConfig);
            connection = dataSource.getConnection();

            System.out.println("Conexão com SQL estabelecida.");

        } catch (SQLException e) {
            System.out.println("Ocorreu um erro ao estabelecer a conexão SQL.");
            return;
        }

    }

    public void close() {
        if (this.connection != null) {
            try {
                this.connection.close();
                this.connection = null;
                System.out.println("Conexao fechada com sucesso.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }


}
