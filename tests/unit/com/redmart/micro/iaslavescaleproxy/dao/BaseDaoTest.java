package com.redmart.micro.iaslavescaleproxy.dao;

import com.redmart.micro.iaslavescaleproxy.constants.enums.scale.InstanceType;

import play.db.Database;
import play.db.Databases;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import javax.sql.DataSource;

abstract class BaseDaoTest {

  Database database;
  Statement statement;
  Map<InstanceType, DataSource> dataSourceProvider;

  @SuppressWarnings("unchecked")
  void setDatabase() throws SQLException {
    System.setProperty("user.timezone", "Asia/Singapore");
    TimeZone.setDefault(null);

    database = Databases.inMemory();

    System.setProperty("user.timezone", "UTC");
    TimeZone.setDefault(null);

    DataSource dataSource = database.getDataSource();
    statement = dataSource.getConnection().createStatement();

    dataSourceProvider = Arrays.stream(InstanceType.values())
        .collect(Collectors.toMap(type -> type, type -> dataSource));
  }
}
