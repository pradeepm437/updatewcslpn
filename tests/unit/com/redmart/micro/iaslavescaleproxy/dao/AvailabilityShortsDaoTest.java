package com.redmart.micro.iaslavescaleproxy.dao;

import static org.junit.Assert.assertEquals;
import static test.util.FixtureHelper.readFixture;

import com.redmart.micro.iaslavescaleproxy.constants.enums.scale.InstanceType;
import com.redmart.micro.iaslavescaleproxy.dto.ShortsDetails;
import com.redmart.micro.iaslavescaleproxy.injector.JacksonModule;
import com.redmart.micro.iaslavescaleproxy.utils.ScaleProxyException;

import com.fasterxml.jackson.databind.JavaType;
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
import java.util.List;
import javax.sql.DataSource;

@Slf4j
public class AvailabilityShortsDaoTest extends BaseDaoTest {

  private Database database;
  private Statement statement;
  private AvailabilityShortsDao availabilityShortsDao;
  private Long rpc;

  private static List<ShortsDetails> getExpectedShorts() throws IOException {
    ObjectMapper mapper = new JacksonModule.ObjectMapperProvider().get();
    JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, ShortsDetails.class);
    return mapper.readValue(readFixture("fixtures/shorts/shorts.json"), type);
  }

  private static String insertStatement() throws IOException {
    return readFixture("fixtures/shorts/inserts.sql");
  }

  private static String createSchema() throws IOException {
    return readFixture("fixtures/shorts/create.sql");
  }

  private static String dropSchema() throws IOException {
    return readFixture("fixtures/shorts/drop.sql");
  }

  @Before
  public void setUp() throws SQLException, IOException {
    rpc = 18_809L;

    super.setDatabase();
    availabilityShortsDao = new AvailabilityShortsDao(dataSourceProvider);

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
  public void shouldReturnAllShorts() throws Exception {
    List<ShortsDetails> shorts = availabilityShortsDao.getCurrentShortsDetails(InstanceType.WEST_FC_SCALE_2013);
    assertEquals(shorts, getExpectedShorts());
  }

  @Test(expected = ScaleProxyException.class)
  public void shouldThrowExceptionForAllShorts() throws SQLException, IOException {
    statement.executeUpdate(dropSchema());
    availabilityShortsDao.getCurrentShortsDetails(InstanceType.WEST_FC_SCALE_2017);
  }

  @Test
  public void shouldReturnShortsForRpc() throws Exception {
    List<ShortsDetails> shorts =
        availabilityShortsDao.getCurrentShortsDetailsForRpc(InstanceType.WEST_FC_SCALE_2013, rpc.toString());
    assertEquals(shorts.get(0), getExpectedShorts().get(0));
  }

  @Test(expected = ScaleProxyException.class)
  public void shouldThrowExceptionForShortsByRpc() throws SQLException, IOException {
    statement.executeUpdate(dropSchema());
    availabilityShortsDao.getCurrentShortsDetailsForRpc(InstanceType.WEST_FC_SCALE_2017, rpc.toString());
  }

}