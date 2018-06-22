package com.redmart.micro.iaslavescaleproxy.dao;

import static org.junit.Assert.assertEquals;
import static test.util.FixtureHelper.readFixture;

import com.redmart.micro.iaslavescaleproxy.constants.enums.scale.InstanceType;
import com.redmart.micro.iaslavescaleproxy.dto.WarehouseEstimate;
import com.redmart.micro.iaslavescaleproxy.injector.JacksonModule;
import com.redmart.micro.iaslavescaleproxy.utils.ScaleProxyException;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import play.db.Database;
import play.db.Databases;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

@Slf4j
public class WarehouseEstimatesDaoTest extends BaseDaoTest {

  private Database database;
  private Statement statement;
  private WarehouseEstimatesDao warehouseEstimatesDao;

  private static WarehouseEstimate getExpectedEstimates() throws IOException {
    ObjectMapper mapper = new JacksonModule.ObjectMapperProvider().get();
    return mapper.readValue(readFixture("fixtures/estimate/warehouse_estimate.json"), WarehouseEstimate.class);
  }

  private static String insertStatement() throws IOException {
    return readFixture("fixtures/estimate/warehouse_estimate_inserts.sql");
  }

  private static String createSchema() throws IOException {
    return readFixture("fixtures/estimate/warehouse_estimate_create.sql");
  }

  private static String dropSchema() throws IOException {
    return readFixture("fixtures/estimate/warehouse_estimate_drop.sql");
  }

  @Before
  public void setUp() throws SQLException, IOException {
    super.setDatabase();
    warehouseEstimatesDao = new WarehouseEstimatesDao(dataSourceProvider);

    database = Databases.inMemory();

    DataSource dataSource = database.getDataSource();
    statement = dataSource.getConnection().createStatement();
    statement.executeUpdate(createSchema());
    statement.executeUpdate(insertStatement());
  }

  @After
  public void cleanUp() {
    try {
      statement.executeUpdate(dropSchema());
    } catch (Exception ex) {
      log.info("exception -- schema is already dropped.");
    } finally {
      database.shutdown();
    }
  }

  @Test
  public void shouldReturnEstimates() throws Exception {
    WarehouseEstimate warehouseEstimate = warehouseEstimatesDao.getWarehouseEstimates(InstanceType.WEST_FC_SCALE_2013);
    assertEquals(warehouseEstimate, getExpectedEstimates());
  }

  @Test(expected = ScaleProxyException.class)
  public void shouldThrowException() throws SQLException, IOException {
    statement.executeUpdate(dropSchema());
    warehouseEstimatesDao.getWarehouseEstimates(InstanceType.WEST_FC_SCALE_2017);
  }
}