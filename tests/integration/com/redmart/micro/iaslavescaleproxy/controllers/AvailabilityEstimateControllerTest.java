package com.redmart.micro.iaslavescaleproxy.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static test.util.FixtureHelper.readFixture;

import com.redmart.micro.iaslavescaleproxy.constants.enums.scale.InstanceType;
import com.redmart.micro.iaslavescaleproxy.dto.WarehouseEstimate;

import org.junit.Test;
import play.mvc.Result;
import test.util.FixtureHelper;
import test.util.IntegrationTestHelper;

import java.io.IOException;
import java.sql.SQLException;

public class AvailabilityEstimateControllerTest extends DatabaseIntegrationTest {

  private IntegrationTestHelper integrationTestHelper = new IntegrationTestHelper();

  private void setUpWarehouseEstimatesData(final InstanceType instanceType) throws IOException, SQLException {
    super.setUp(instanceType);
    statement.executeUpdate(readFixture("fixtures/estimate/warehouse_estimate_create.sql"));
    statement.executeUpdate(readFixture("fixtures/estimate/warehouse_estimate_inserts.sql"));
  }

  private void cleanUpWarehouseData() throws IOException, SQLException {
    statement.executeUpdate(readFixture("fixtures/estimate/warehouse_estimate_drop.sql"));
  }

  private void fetchWarehouseEstimates(Result result) throws IOException, SQLException {
    cleanUpWarehouseData();

    assertNotNull("Should not get null health check", result);
    assertEquals(200, result.status());

    WarehouseEstimate warehouseEstimateActual = integrationTestHelper.parseResult(WarehouseEstimate.class, result);
    WarehouseEstimate warehouseEstimateExpected = integrationTestHelper.parseResult(WarehouseEstimate.class,
        FixtureHelper.readFixtureAsBytes("fixtures/estimate/warehouse_estimate.json"));

    assertEquals(warehouseEstimateExpected, warehouseEstimateActual);
  }

  @Test
  public void shouldFetchWarehouseEstimates() throws IOException, SQLException {
    setUpWarehouseEstimatesData(InstanceType.WEST_FC_SCALE_2013);

    Result result = routeAndLog("GET", "/v1.0/estimates/warehouse");
    fetchWarehouseEstimates(result);
  }

  @Test
  public void shouldFetchWarehouseEstimatesWhenInstanceIsScale2017() throws IOException, SQLException {
    setUpWarehouseEstimatesData(InstanceType.WEST_FC_SCALE_2017);

    Result result = routeAndLog("GET", "/v2.0/estimates/warehouse/" + InstanceType.WEST_FC_SCALE_2017.getType());
    fetchWarehouseEstimates(result);

  }

  @Test
  public void shouldFetchWarehouseEstimatesWhenInstanceIsScale2013() throws IOException, SQLException {
    setUpWarehouseEstimatesData(InstanceType.WEST_FC_SCALE_2013);

    Result result = routeAndLog("GET", "/v2.0/estimates/warehouse/" + InstanceType.WEST_FC_SCALE_2013.getType());
    fetchWarehouseEstimates(result);

  }

  @Test
  public void shouldThrowBadRequestWhenInstanceTypeInvalid() throws IOException, SQLException {
    setUpWarehouseEstimatesData(InstanceType.WEST_FC_SCALE_2017);
    Result result = routeAndLog("GET", "/v2.0/estimates/warehouse/invalid_instance");

    cleanUpWarehouseData();
    assertNotNull("Should not get null health check", result);
    assertEquals(400, result.status());
  }

}
