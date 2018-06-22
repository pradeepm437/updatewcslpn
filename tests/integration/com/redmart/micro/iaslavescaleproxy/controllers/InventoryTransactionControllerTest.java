package com.redmart.micro.iaslavescaleproxy.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static test.util.FixtureHelper.readFixture;

import com.redmart.micro.iaslavescaleproxy.constants.enums.scale.InstanceType;
import com.redmart.micro.iaslavescaleproxy.dto.recover.Transaction;

import org.junit.Test;
import play.mvc.Result;
import test.util.FixtureHelper;
import test.util.IntegrationTestHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class InventoryTransactionControllerTest extends DatabaseIntegrationTest {

  private IntegrationTestHelper integrationTestHelper = new IntegrationTestHelper();

  private void setUpTransactionsData(final InstanceType instanceType) throws IOException, SQLException {
    super.setUp(instanceType);
    statement.executeUpdate(readFixture("fixtures/transaction/create.sql"));
    statement.executeUpdate(readFixture("fixtures/transaction/inserts.sql"));
  }

  private void cleanUpTransactionsData() throws IOException, SQLException {
    statement.executeUpdate(readFixture("fixtures/transaction/drop.sql"));
  }

  private void fetchTransactions(Result result) throws IOException, SQLException {
    cleanUpTransactionsData();
    assertNotNull("Should not get null health check", result);
    assertEquals(200, result.status());

    List<Transaction> actual = integrationTestHelper.parseList(Transaction.class, result);
    List<Transaction> expected = integrationTestHelper.parseList(Transaction.class,
        FixtureHelper.readFixture("fixtures/transaction/transaction.json"));

    assertEquals(expected, actual);
  }

  @Test
  public void shouldFetchTransactions() throws IOException, SQLException {
    setUpTransactionsData(InstanceType.WEST_FC_SCALE_2013);

    Result result = routeAndLog("GET", "/v1.0/inventory/transactions");
    fetchTransactions(result);
  }

  @Test
  public void shouldFetchTransactionsWhenInstanceIsScale2017() throws IOException, SQLException {
    setUpTransactionsData(InstanceType.WEST_FC_SCALE_2017);

    Result result = routeAndLog("GET", "/v2.0/inventory/transactions/" + InstanceType.WEST_FC_SCALE_2017.getType());
    fetchTransactions(result);

  }

  @Test
  public void shouldFetchTransactionsWhenInstanceIsScale2013() throws IOException, SQLException {
    setUpTransactionsData(InstanceType.WEST_FC_SCALE_2013);

    Result result = routeAndLog("GET", "/v2.0/inventory/transactions/" + InstanceType.WEST_FC_SCALE_2013.getType());
    fetchTransactions(result);

  }

  @Test
  public void shouldThrowBadRequestWhenInstanceTypeInvalid() throws IOException, SQLException {
    setUpTransactionsData(InstanceType.WEST_FC_SCALE_2017);
    Result result = routeAndLog("GET", "/v2.0/inventory/transactions/invalid_instance");

    cleanUpTransactionsData();
    assertNotNull("Should not get null health check", result);
    assertEquals(400, result.status());
  }

}