package com.redmart.micro.updatewcslpn.injector;

import com.redmart.micro.updatewcslpn.constants.enums.scale.InstanceType;

import play.db.DB;

import javax.inject.Provider;
import javax.sql.DataSource;

public class WestFcSlaveScale2017DataSourceProvider implements Provider<DataSource> {

  @Override
  public DataSource get() {
    return DB.getDataSource(InstanceType.WEST_FC_SCALE_2017.getType());
  }

}