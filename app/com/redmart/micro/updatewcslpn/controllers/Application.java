package com.redmart.micro.updatewcslpn.controllers;

import com.redmart.commons.filters.CorsAction;
import com.redmart.micro.updatewcslpn.constants.Constants;

import play.Configuration;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;

import javax.inject.Inject;


@With(CorsAction.class)
public class Application extends Controller {

  private final Configuration config;

  private final Constants constants;

  @Inject
  public Application(Configuration config, Constants constants) {
    this.config = config;
    this.constants = constants;
  }

  public Result healthCheck() {
    response().setContentType("text/plain");
    return ok(String.format("%s is healthy: %s", constants.getApplicationName(), config.getString("app.git.commit")));
  }
}
