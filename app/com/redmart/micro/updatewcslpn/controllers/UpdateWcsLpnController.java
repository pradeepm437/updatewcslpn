package com.redmart.micro.updatewcslpn.controllers;

import com.redmart.commons.monitoring.Metrics;

import com.redmart.micro.updatewcslpn.dao.UpdateWcsLpnDao;

import lombok.extern.slf4j.Slf4j;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import javax.inject.Provider;

@Slf4j
public class UpdateWcsLpnController extends Controller {

  private final Provider<UpdateWcsLpnDao> updateWcsLpnDaoProvider;

  @Inject
  public UpdateWcsLpnController(Provider<UpdateWcsLpnDao> updateWcsLpnDaoProvider) {
    this.updateWcsLpnDaoProvider = updateWcsLpnDaoProvider;
  }

  private Result updateLpn(final String wcsLpn,
                                                  final String container) {
    try {
      String result = updateWcsLpnDaoProvider.get().updateWcsLpnOntoContainer(wcsLpn, container);
      if (result.equals("")) {
        log.info("Update of LPN failed for container {}.", container);
        return status(NOT_FOUND);
      }
      return status(OK, Json.toJson(result));
    } catch (Exception e) {
      log.error("Exception in updating LPN for container {}: ", container, e);
      return status(INTERNAL_SERVER_ERROR, e.getMessage());
    }
  }

  /**
   * Updates the WCS LPN on to Shipping Container.
   *
   * @param wcsLpn .
   * @param container .
   * @return the result.
   */
  @Metrics("update-wcs-lpn-onto-shipping-container")
  public Result updateWcsLpnonShippingContainer(final String wcsLpn,final String container) {
    log.info("Request received to Update WCS LPN {} for the shipping container {}.", wcsLpn, container);

    return updateLpn(wcsLpn, container);
  }
}
