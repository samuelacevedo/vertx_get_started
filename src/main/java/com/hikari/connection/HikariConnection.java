package com.hikari.connection;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class HikariConnection {
  private static final HikariConfig config;
  private static final HikariDataSource dataSource;

  static {
    config = new HikariConfig();
    config.setJdbcUrl("jdbc:mysql://localhost:3306/avila");
    config.setUsername("root");
    config.setPassword("root");
    config.addDataSourceProperty("cachePrepStmts", "true");
    config.addDataSourceProperty("prepStmtCacheSize", "250");
    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
    dataSource = new HikariDataSource(config);
  }

  private HikariConnection(){}

  public static final Connection getConnection() throws SQLException {
    return dataSource.getConnection();
  }

}
