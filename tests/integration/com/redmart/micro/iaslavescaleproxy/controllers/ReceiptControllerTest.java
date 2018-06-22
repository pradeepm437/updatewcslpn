package com.redmart.micro.iaslavescaleproxy.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static test.util.FixtureHelper.readFixture;

import com.redmart.micro.iaslavescaleproxy.constants.enums.scale.InstanceType;
import com.redmart.micro.iaslavescaleproxy.dto.recover.Receipt;

import org.junit.Test;
import play.mvc.Result;
import test.util.FixtureHelper;
import test.util.IntegrationTestHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ReceiptControllerTest extends DatabaseIntegrationTest {

  private IntegrationTestHelper integrationTestHelper = new IntegrationTestHelper();

  private void setUpReceiptsData(final InstanceType instanceType) throws IOException, SQLException {
    super.setUp(instanceType);
    statement.executeUpdate(readFixture("fixtures/receipt/create.sql"));
    statement.executeUpdate(readFixture("fixtures/receipt/inserts.sql"));
  }

  private void cleanUpReceiptsData() throws IOException, SQLException {
    statement.executeUpdate(readFixture("fixtures/receipt/drop.sql"));
  }

  private void fetchReceipts(Result result) throws IOException, SQLException {
    assertNotNull("Should not get null health check", result);
    assertEquals(200, result.status());

    List<Receipt> actual = integrationTestHelper.parseList(Receipt.class, result);
    List<Receipt> expected = integrationTestHelper.parseList(Receipt.class,
        FixtureHelper.readFixture("fixtures/receipt/receipt.json"));

    assertEquals(expected, actual);
    cleanUpReceiptsData();
  }

  @Test
  public void shouldFetchReceipts() throws IOException, SQLException {
    setUpReceiptsData(InstanceType.WEST_FC_SCALE_2013);

    Result result = routeAndLog("GET", "/v1.0/inventory/receipts");
    fetchReceipts(result);
  }

  @Test
  public void shouldFetchReceiptsWhenInstanceIsScale2017() throws IOException, SQLException {
    setUpReceiptsData(InstanceType.WEST_FC_SCALE_2017);

    Result result = routeAndLog("GET", "/v2.0/inventory/receipts/" + InstanceType.WEST_FC_SCALE_2017.getType());
    fetchReceipts(result);

  }

  @Test
  public void shouldFetchReceiptsWhenInstanceIsScale2013() throws IOException, SQLException {
    setUpReceiptsData(InstanceType.WEST_FC_SCALE_2013);

    Result result = routeAndLog("GET", "/v2.0/inventory/receipts/" + InstanceType.WEST_FC_SCALE_2013.getType());
    fetchReceipts(result);

  }

  @Test
  public void shouldThrowBadRequestWhenInstanceTypeInvalid() throws IOException, SQLException {
    setUpReceiptsData(InstanceType.WEST_FC_SCALE_2017);
    Result result = routeAndLog("GET", "/v2.0/inventory/receipts/invalid_instance");

    cleanUpReceiptsData();
    assertNotNull("Should not get null health check", result);
    assertEquals(400, result.status());
  }

}