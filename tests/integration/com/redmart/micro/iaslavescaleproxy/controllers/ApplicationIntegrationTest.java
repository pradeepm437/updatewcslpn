package com.redmart.micro.iaslavescaleproxy.controllers;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import play.mvc.Result;

public class ApplicationIntegrationTest extends DatabaseIntegrationTest {

  @Test
  public void testHealthCheck() {
    Result result = routeAndLog("GET", "/health");
    assertNotNull("Should not get null health check", result);
  }

}
