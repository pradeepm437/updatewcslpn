package com.redmart.micro.iaslavescaleproxy.controllers;

import com.redmart.commons.test.integration.IntegrationTest;
import com.redmart.micro.iaslavescaleproxy.constants.enums.scale.InstanceType;
import com.redmart.micro.iaslavescaleproxy.injector.WestFcSlaveScale2013DataSourceProvider;
import com.redmart.micro.iaslavescaleproxy.injector.WestFcSlaveScale2017DataSourceProvider;

import com.google.common.collect.ImmutableMap;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import javax.sql.DataSource;

public abstract class DatabaseIntegrationTest extends IntegrationTest {

  Statement statement;

  void setUp(final InstanceType instanceType) throws SQLException {
    statement = null;
    DataSource dataSource;
    if (instanceType.equals(InstanceType.WEST_FC_SCALE_2017)) {
      dataSource = this.app.injector().instanceOf(WestFcSlaveScale2017DataSourceProvider.class).get();
    } else {
      dataSource = this.app.injector().instanceOf(WestFcSlaveScale2013DataSourceProvider.class).get();
    }
    statement = dataSource.getConnection().createStatement();
  }

  @Override
  protected Map<String, Object> getExtraConfig() {
    return ImmutableMap.<String, Object>builder()
        .putAll(super.getExtraConfig())
        .put("db.west_fc_scale_2013.driver", "org.h2.Driver")
        .put("db.west_fc_scale_2013.url", "jdbc:h2:mem:default")
        .put("db.west_fc_scale_2013.username", "")
        .put("db.west_fc_scale_2013.password", "")
        .put("play.db.west_fc_scale_2013.prototype.minimumIdle", 1)
        .put("play.db.west_fc_scale_2013.prototype.maximumPoolSize", 1)
        .put("db.west_fc_scale_2017.driver", "org.h2.Driver")
        .put("db.west_fc_scale_2017.url", "jdbc:h2:mem:default")
        .put("db.west_fc_scale_2017.username", "")
        .put("db.west_fc_scale_2017.password", "")
        .put("play.db.west_fc_scale_2017.prototype.minimumIdle", 1)
        .put("play.db.west_fc_scale_2017.prototype.maximumPoolSize", 1)
        .build();
  }
}
