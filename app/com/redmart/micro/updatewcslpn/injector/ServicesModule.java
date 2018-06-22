package com.redmart.micro.updatewcslpn.injector;

import com.redmart.micro.updatewcslpn.constants.enums.scale.InstanceType;
import com.redmart.micro.updatewcslpn.controllers.Application;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;

import javax.inject.Singleton;
import javax.sql.DataSource;

public class ServicesModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Application.class).in(Singleton.class);


    MapBinder<InstanceType, DataSource> dataSourceMapBinder = MapBinder
        .newMapBinder(binder(), InstanceType.class, DataSource.class);

    dataSourceMapBinder.addBinding(InstanceType.WEST_FC_SCALE_2017)
        .toProvider(WestFcSlaveScale2017DataSourceProvider.class).in(Singleton.class);
  }

}
