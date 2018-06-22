package com.redmart.micro.updatewcslpn.constants;

import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Constants {
  private final String applicationName;

  @Inject
  protected Constants(final Configuration configuration) {
    applicationName = configuration.getString("project.name");
  }

  public String getApplicationName() {
    return applicationName;
  }
}
