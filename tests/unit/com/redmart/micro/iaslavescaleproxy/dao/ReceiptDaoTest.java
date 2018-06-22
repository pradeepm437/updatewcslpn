package com.redmart.micro.iaslavescaleproxy.dao;

import static java.lang.Boolean.TRUE;
import static org.junit.Assert.assertEquals;
import static test.util.FixtureHelper.readFixture;

import com.redmart.micro.iaslavescaleproxy.constants.enums.scale.InstanceType;
import com.redmart.micro.iaslavescaleproxy.dto.recover.Receipt;
import com.redmart.micro.iaslavescaleproxy.injector.JacksonModule;

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
public class ReceiptDaoTest extends BaseDaoTest {

  private Database database;
  private Statement statement;
  private ReceiptDao receiptDao;

  private static List<Receipt> getExpectedReceipts() throws IOException {
    ObjectMapper mapper = new JacksonModule.ObjectMapperProvider().get();
    JavaType type = mapper.getTypeFactory().constructCollectionType(List.class, Receipt.class);
    return mapper.readValue(readFixture("fixtures/receipt/receipt.json"), type);
  }

  private static String insertStatement() throws IOException {
    return readFixture("fixtures/receipt/inserts.sql");
  }

  private static String createSchema() throws IOException {
    return readFixture("fixtures/receipt/create.sql");
  }

  private static String dropSchema() throws IOException {
    return readFixture("fixtures/receipt/drop.sql");
  }

  @Before
  public void setUp() throws SQLException, IOException {
    super.setDatabase();
    receiptDao = new ReceiptDao(dataSourceProvider);

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
  public void shouldReturnTransactions() throws IOException {
    List<Receipt> receipts = receiptDao.getLastTwoHoursReceiptDetails(InstanceType.WEST_FC_SCALE_2013);
    assertEquals(receipts, getExpectedReceipts());
  }

  @Test()
  public void shouldReturnEmptyWhenException() throws SQLException, IOException {
    statement.executeUpdate(dropSchema());
    List<Receipt> receipts = receiptDao.getLastTwoHoursReceiptDetails(InstanceType.WEST_FC_SCALE_2017);
    assertEquals(receipts.isEmpty(), TRUE);
  }

}