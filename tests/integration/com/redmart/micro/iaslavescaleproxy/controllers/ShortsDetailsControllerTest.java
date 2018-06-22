package com.redmart.micro.iaslavescaleproxy.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static test.util.FixtureHelper.readFixture;

import com.redmart.micro.iaslavescaleproxy.constants.enums.scale.InstanceType;
import com.redmart.micro.iaslavescaleproxy.dto.ShortsDetails;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import play.mvc.Result;
import test.util.FixtureHelper;
import test.util.IntegrationTestHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ShortsDetailsControllerTest extends DatabaseIntegrationTest {

  private static final String SHOULD_NOT_GET_NULL_CHECK = "Should not get null check";

  private IntegrationTestHelper integrationTestHelper = new IntegrationTestHelper();
  private Long rpc;

  private void setUpShortsSchema(final InstanceType instanceType) throws IOException, SQLException {
    super.setUp(instanceType);
    statement.executeUpdate(readFixture("fixtures/shorts/create.sql"));
  }

  private void setUpShortsData(final InstanceType instanceType) throws IOException, SQLException {
    setUpShortsSchema(instanceType);
    statement.executeUpdate(readFixture("fixtures/shorts/inserts.sql"));
  }

  private void cleanUpShortsData() throws IOException, SQLException {
    log.error("cleanup start");
    statement.executeUpdate(readFixture("fixtures/shorts/drop.sql"));
    log.error("cleanup done");
  }

  @Test
  public void shouldThrow500ForAllShortsWhenScaleProxyException() throws IOException, SQLException {
    Result result = routeAndLog("GET", "/v1.0/search/shorts");

    assertNotNull(SHOULD_NOT_GET_NULL_CHECK, result);
    assertEquals(500, result.status());
  }

  @Test
  public void shouldThrow404WhenNoShorts() throws IOException, SQLException {
    setUpShortsSchema(InstanceType.WEST_FC_SCALE_2013);

    Result result = routeAndLog("GET", "/v1.0/search/shorts");
    cleanUpShortsData();

    assertNotNull(SHOULD_NOT_GET_NULL_CHECK, result);
    assertEquals(404, result.status());
  }

  private void fetchAllShorts(Result result) throws IOException, SQLException {
    cleanUpShortsData();

    assertNotNull(SHOULD_NOT_GET_NULL_CHECK, result);
    assertEquals(200, result.status());

    List<ShortsDetails> actual = integrationTestHelper.parseList(ShortsDetails.class, result);
    List<ShortsDetails> expected = integrationTestHelper.parseList(ShortsDetails.class,
        FixtureHelper.readFixture("fixtures/shorts/shorts.json"));

    assertEquals(expected, actual);
  }

  @Test
  public void shouldFetchAllShorts() throws IOException, SQLException {
    setUpShortsData(InstanceType.WEST_FC_SCALE_2013);

    Result result = routeAndLog("GET", "/v1.0/search/shorts");
    fetchAllShorts(result);
  }

  @Test
  public void shouldFetchAllShortsWhenInstanceIsScale2017() throws IOException, SQLException {
    setUpShortsData(InstanceType.WEST_FC_SCALE_2017);

    Result result = routeAndLog("GET", "/v2.0/search/shorts/" + InstanceType.WEST_FC_SCALE_2017.getType());
    fetchAllShorts(result);

  }

  @Test
  public void shouldFetchAllShortsWhenInstanceIsScale2013() throws IOException, SQLException {
    setUpShortsData(InstanceType.WEST_FC_SCALE_2013);

    Result result = routeAndLog("GET", "/v2.0/search/shorts/" + InstanceType.WEST_FC_SCALE_2013.getType());
    fetchAllShorts(result);

  }

  @Test
  public void shouldThrowBadRequestForAllShortsWhenInstanceTypeInvalid() throws IOException, SQLException {
    setUpShortsData(InstanceType.WEST_FC_SCALE_2017);
    Result result = routeAndLog("GET", "/v2.0/search/shorts/invalid_instance");
    cleanUpShortsData();

    assertNotNull(SHOULD_NOT_GET_NULL_CHECK, result);
    assertEquals(400, result.status());
  }

  @Test
  public void shouldThrow500ForShortsByRpcWhenScaleProxyException() throws IOException, SQLException {
    rpc = 1_234L;
    Result result = routeAndLog("GET", "/v1.0/search/shorts/" + rpc.toString());

    assertNotNull(SHOULD_NOT_GET_NULL_CHECK, result);
    assertEquals(500, result.status());
  }

  @Test
  public void shouldThrow404WhenNoShortsForRpc() throws IOException, SQLException {
    setUpShortsData(InstanceType.WEST_FC_SCALE_2013);
    rpc = 1_234_567L;

    Result result = routeAndLog("GET", "/v1.0/search/shorts/" + rpc.toString());
    cleanUpShortsData();

    assertNotNull(SHOULD_NOT_GET_NULL_CHECK, result);
    assertEquals(404, result.status());
  }

  private void fetchShortsByRpc(Result result) throws IOException, SQLException {
    cleanUpShortsData();

    assertNotNull(SHOULD_NOT_GET_NULL_CHECK, result);
    assertEquals(200, result.status());

    List<ShortsDetails> actual = integrationTestHelper.parseList(ShortsDetails.class, result);
    List<ShortsDetails> expected = integrationTestHelper.parseList(ShortsDetails.class,
        FixtureHelper.readFixture("fixtures/shorts/shorts.json"))
        .stream()
        .filter(shorts -> shorts.getRpc() == rpc)
        .collect(Collectors.toList());

    assertEquals(expected, actual);
  }

  @Test
  public void shouldFetchShortsByRpc() throws IOException, SQLException {
    setUpShortsData(InstanceType.WEST_FC_SCALE_2013);
    rpc = 18_809L;

    Result result = routeAndLog("GET", "/v1.0/search/shorts/" + rpc.toString());
    fetchShortsByRpc(result);
  }



  @Test
  public void shouldFetchShortsByRpcWhenInstanceIsScale2017() throws IOException, SQLException {
    setUpShortsData(InstanceType.WEST_FC_SCALE_2017);
    rpc = 18_809L;

    Result result = routeAndLog("GET", "/v2.0/search/shorts/" + InstanceType.WEST_FC_SCALE_2017.getType() + "/"
        + rpc.toString());
    fetchShortsByRpc(result);

  }

  @Test
  public void shouldFetchShortsByRpcWhenInstanceIsScale2013() throws IOException, SQLException {
    setUpShortsData(InstanceType.WEST_FC_SCALE_2013);
    rpc = 18_809L;

    Result result = routeAndLog("GET", "/v2.0/search/shorts/" + InstanceType.WEST_FC_SCALE_2013.getType() + "/"
        + rpc.toString());
    fetchShortsByRpc(result);

  }

  @Test
  public void shouldThrowBadRequestForShortsByRpcWhenInstanceTypeInvalid() throws IOException, SQLException {
    setUpShortsData(InstanceType.WEST_FC_SCALE_2017);
    rpc = 18_809L;

    Result result = routeAndLog("GET", "/v2.0/search/shorts/invalid_instance/" + rpc.toString());
    cleanUpShortsData();

    assertNotNull(SHOULD_NOT_GET_NULL_CHECK, result);
    assertEquals(400, result.status());
  }

}