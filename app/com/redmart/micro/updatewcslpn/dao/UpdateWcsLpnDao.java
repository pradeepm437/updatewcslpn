package com.redmart.micro.updatewcslpn.dao;

import com.redmart.micro.updatewcslpn.utils.ScaleProxyException;

import lombok.extern.slf4j.Slf4j;

import play.db.DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;


@Slf4j
public class UpdateWcsLpnDao {

    /**
     * Updates wcsLpn for container.
     *
     * @param wcsLpn    .
     * @param container .
     * @return 200 on success 220 on error .
     */
  public String updateWcsLpnOntoContainer(final String wcsLpn, final String container) {
    return updateWcsLpn(wcsLpn, container);
  }

  private String updateWcsLpn(final String wcsLpn,
                                          final String container) {
    Connection connection = null;
    String result = "";
    try {
      connection = DB.getConnection();

      CallableStatement stmt = connection.prepareCall("{call UpdateWCSLpn(?,?,?)}");

      if (container != null) {
        stmt.setString(1, container);
      }

      if (wcsLpn != null)  {
        stmt.setString(2, wcsLpn);
      }
      stmt.registerOutParameter(3,java.sql.Types.INTEGER);
      stmt.execute();

      int outputValue = stmt.getInt(3);

      if (outputValue == 200)  {
        result = "LPN " + wcsLpn + " Updated Successfully on to Shipping Container " + container ;
      } else if (outputValue == 220)  {
        result = "Invalid Container " + container + " please check the container passed";
      } else {
        result = "Update Failed for LPN " + wcsLpn + " please Contact WMS SCALE Team";
      }

    } catch (SQLException e) {
      log.error("Error while updating LPN to container, error:{}", e);
      throw new ScaleProxyException("Error while updating LPN to container");
    }

    return result;
  }

}
